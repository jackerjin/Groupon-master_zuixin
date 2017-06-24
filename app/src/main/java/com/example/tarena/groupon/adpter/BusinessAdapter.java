package com.example.tarena.groupon.adpter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tarena.groupon.R;
import com.example.tarena.groupon.bean.BusinessBean;
import com.example.tarena.groupon.bean.TuanBean;
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
        if (convertView==null){
            convertView= inflater.inflate(R.layout.item_business_layout,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        BusinessBean.BusinessesBean businessesBean=getItem(position);
        //呈现图片
        HttpUtil.displayImage(businessesBean.getS_photo_url(),holder.ivPic);

        holder.tvName.setText(businessesBean.getName()+"("+businessesBean.getBranch_name()+")");
        holder.ivEvaluate.setImageResource(R.drawable.movie_star35);
        holder.tvAddress.setText(businessesBean.getAddress()+" ("+businessesBean.getCategories()+")");
        Random random=new Random();
        int price=random.nextInt(100)+50;
        holder.tvConsume.setText(String.valueOf(price));
        holder.tvDistance.setText("100m");
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
