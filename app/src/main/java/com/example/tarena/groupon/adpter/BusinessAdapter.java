package com.example.tarena.groupon.adpter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.tarena.groupon.R;
import com.example.tarena.groupon.app.Myapp;
import com.example.tarena.groupon.bean.BusinessBean;
import com.example.tarena.groupon.util.HttpUtil;
import java.util.List;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tarena on 2017/6/23.
 */

public class BusinessAdapter extends MyBaseAdapter<BusinessBean.BusinessesBean> {
    public BusinessAdapter(Context context, List<BusinessBean.BusinessesBean> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_business_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BusinessBean.BusinessesBean businessesBean = getItem(position);
        //呈现图片
        HttpUtil.displayImage(businessesBean.getS_photo_url(), holder.ivPic);

        String name = businessesBean.getName().substring(0, businessesBean.getName().indexOf("("));
        if (!TextUtils.isEmpty(businessesBean.getBranch_name())) {
            name = name + "(" + businessesBean.getBranch_name() + ")";
        }
        holder.tvName.setText(name);

        Random random = new Random();
        int[] stars = new int[]{R.drawable.movie_star10,
                R.drawable.movie_star20,
                R.drawable.movie_star30,
                R.drawable.movie_star35,
                R.drawable.movie_star40,
                R.drawable.movie_star45,
                R.drawable.movie_star50,
        };
        Random random1 = new Random();
        int idx = random1.nextInt(7);
        holder.ivEvaluate.setImageResource(stars[idx]);
        int price = random.nextInt(100) + 50;
        holder.tvConsume.setText(String.valueOf(price));

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < businessesBean.getRegions().size(); j++) {
            if (j == 0) {
                sb.append(businessesBean.getRegions().get(j));
            } else {
                sb.append("/").append(businessesBean.getRegions().get(j));
            }
        }
        sb.append("/");
        for (int j = 0; j < businessesBean.getCategories().size(); j++) {
            if (j == 0) {
                sb.append(businessesBean.getCategories().get(j));
            } else {
                sb.append("/").append(businessesBean.getCategories().get(j));
            }
        }
        holder.tvAddress.setText(sb.toString());
        //根据百度地图地位距离(经度，纬度)
        if (Myapp.myLocation != null) {
//            double distance = DistanceUtil.getDistance(businessesBean.getLongitude(), businessesBean.getLatitude(),
//                    Myapp.myLocation.longitude, Myapp.myLocation.latitude);
            double distance = DistanceUtil.getDistance(new LatLng(businessesBean.getLatitude(), businessesBean.getLongitude()), Myapp.myLocation);
            holder.tvDistance.setText(distance + "米");
        } else {
            holder.tvDistance.setText("");
        }
        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.item_business_image)
        ImageView ivPic;
        @BindView(R.id.item_business_evaluate)
        ImageView ivEvaluate;
        @BindView(R.id.item_business_priceforone)
        TextView tvConsume;

        @BindView(R.id.item_business_nameandlittleAddress)
        TextView tvName;
        @BindView(R.id.item_business_Bigaddress_foodstyle)
        TextView tvAddress;
        @BindView(R.id.item_business_distance)
        TextView tvDistance;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
