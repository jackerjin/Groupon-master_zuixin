package com.example.tarena.groupon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tarena.groupon.R;
import com.example.tarena.groupon.adpter.CommentAdapter;
import com.example.tarena.groupon.bean.BusinessBean;
import com.example.tarena.groupon.bean.Comment;
import com.example.tarena.groupon.util.HttpUtil;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends Activity {
    BusinessBean.BusinessesBean business;
    @BindView(R.id.detail_listView)
    ListView listView;

    List<Comment> datas;
    CommentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        business = (BusinessBean.BusinessesBean) getIntent().getSerializableExtra("business");
        Log.d("TAG", "Detail+onCreate: " + business.getName());
        initListView();
    }

    private void initListView() {
        //为listview添加若干头部
        datas = new ArrayList<>();
        adapter = new CommentAdapter(this,datas);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View listHeaderDetail = inflater.inflate(R.layout.item_business_layout, listView, false);
        View listHeaderLocate = inflater.inflate(R.layout.header_detail_layout, listView, false);

        initHeaderBusiness(listHeaderDetail);
        initHeaderInfo(listHeaderLocate);
        listView.addHeaderView(listHeaderDetail);
        listView.addHeaderView(listHeaderLocate);
        listView.setAdapter(adapter);


    }
    private void initHeaderInfo(View view) {
        TextView tvAddress = (TextView) view.findViewById(R.id.tv_detail_locate);
        tvAddress.setText(business.getAddress());
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this,FindActivity.class);
                intent.putExtra("business",business);
                startActivity(intent);
            }
        });

        TextView tvTelphone = (TextView) view.findViewById(R.id.tv_detail_phone);
        tvTelphone.setText(business.getTelephone());
    }

    private void initHeaderBusiness(View view) {
        ImageView ivPic = (ImageView) view.findViewById(R.id.item_business_image);
        ImageView ivRating = (ImageView) view.findViewById(R.id.item_business_evaluate);
        TextView tvName = (TextView) view.findViewById(R.id.item_business_nameandlittleAddress);
        TextView tvPrice = (TextView) view.findViewById(R.id.item_business_priceforone);
        TextView tvInfo = (TextView) view.findViewById(R.id.item_business_Bigaddress_foodstyle);
        TextView tvDistance = (TextView) view.findViewById(R.id.item_business_distance);
        TextView tvLocate= (TextView) view.findViewById(R.id.tv_detail_locate);

        HttpUtil.loadImage(business.getPhoto_url(), ivPic);

        String name = business.getName().substring(0, business.getName().indexOf("("));
        if (!TextUtils.isEmpty(business.getBranch_name())) {
            name = name + "(" + business.getBranch_name() + ")";
        }
        tvName.setText(name);

        int[] stars = new int[]{R.drawable.movie_star10,
                R.drawable.movie_star20,
                R.drawable.movie_star30,
                R.drawable.movie_star35,
                R.drawable.movie_star40,
                R.drawable.movie_star45,
                R.drawable.movie_star50};
        Random rand = new Random();
        int idx = rand.nextInt(7);
        ivRating.setImageResource(stars[idx]);

        int price = rand.nextInt(100) + 50;

        tvPrice.setText("￥" + price + "/人");

        StringBuilder sb = new StringBuilder();

        for (int j = 0; j < business.getRegions().size(); j++) {

            if (j == 0) {
                sb.append(business.getRegions().get(j));
            } else {
                sb.append("/").append(business.getRegions().get(j));
            }


        }

        sb.append(" ");

        for (int j = 0; j < business.getCategories().size(); j++) {
            if (j == 0) {
                sb.append(business.getCategories().get(j));
            } else {
                sb.append("/").append(business.getCategories().get(j));
            }
        }

        tvInfo.setText(sb.toString());

        tvDistance.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {

        HttpUtil.getComment(business.getReview_list_url(), new HttpUtil.OnResponseListener<Document>() {
            @Override
            public void onResponse(Document document) {
                //1）解析
                List<Comment> comments = new ArrayList<Comment>();
                Elements elements = document.select("div[class=comment-list] li[data-id]");
                for (Element element : elements) {
                    Comment comment = new Comment();
                    Element imgElement = element.select("div[class=pic] img").get(0);
                    comment.setName(imgElement.attr("title"));
                    comment.setAvatar(imgElement.attr("src"));

                    Elements spanElements = element.select("div[class=user-info] span[class=comm-per]");

                    if (spanElements.size() > 0) {
                        //人均 ￥85
                        comment.setPrice(spanElements.get(0).text().split(" ")[1] + "/人");
                    } else {
                        comment.setPrice("");
                    }

                    Element spanElement = element.select("div[class=user-info] span[title]").get(0);

                    String rate = spanElement.attr("class");
                    //star40
                    comment.setRating(rate.split("-")[3]);

                    Element divElement = element.select("div[class=J_brief-cont]").get(0);

                    comment.setContent(divElement.text());

                    Elements imgElements = element.select("div[class=shop-photo] img");

                    int size = imgElements.size();

                    if (size > 3) {
                        size = 3;
                    }
                    String[] imgs = new String[size];

                    for (int i = 0; i < size; i++) {
                        imgs[i] = imgElements.get(i).attr("src");
                    }

                    comment.setImgs(imgs);

                    Element spanEle = element.select("div[class=misc-info] span[class=time]").get(0);

                    comment.setDate(spanEle.text());

                    comments.add(comment);


                }
                Log.d("TAG", "评论内容: " + comments);
                //2）放到listview中呈现
                adapter.addAll(comments,true);
            }
        });
    }

}
