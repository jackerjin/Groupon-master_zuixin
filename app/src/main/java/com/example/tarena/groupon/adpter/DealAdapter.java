package com.example.tarena.groupon.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tarena.groupon.R;
import com.example.tarena.groupon.bean.TuanBean;
import com.example.tarena.groupon.util.HttpUtil;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tarena on 2017/6/20.
 */

public class DealAdapter extends MyBaseAdapter<TuanBean.Deal> {

    public DealAdapter(Context context, List<TuanBean.Deal> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
     ViewHolder vh;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_deal_layout,parent,false);
            vh=new ViewHolder(convertView);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }

        TuanBean.Deal deal=getItem(position);
        //呈现图片
//        HttpUtil.loadImage(deal.getS_image_url(),vh.ivPic);
       HttpUtil.displayImage(deal.getS_image_url(),vh.ivPic);
        vh.tvTitle.setText(deal.getTitle());
        vh.tvDetail.setText(deal.getDescription());
        vh.tvPrice.setText(deal.getCurrent_price()+"");
        Random random=new Random();
        int count=random.nextInt(2000)+500;
        vh.tvCount.setText("已售"+count);
        //TODO 距离 vh.tvDistance.setText("xxxx");
        return convertView;
    }
    public class ViewHolder{
        @BindView(R.id.item_deal_image)
        ImageView ivPic;
        @BindView(R.id.item_deal_name)
        TextView tvTitle;
        @BindView(R.id.item_deal_detail)
        TextView tvDetail;
        @BindView(R.id.item_deal_distance)
        TextView tvDistance;
        @BindView(R.id.item_deal_price)
        TextView tvPrice;
        @BindView(R.id.item_deal_sellcount)
        TextView tvCount;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
