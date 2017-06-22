package com.example.tarena.groupon.adpter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.tarena.groupon.R;
import com.example.tarena.groupon.bean.CitynameBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tarena on 2017/6/21.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> implements SectionIndexer {

    //声明属性
    Context context;
    List<CitynameBean> datas;
    LayoutInflater inflater;

 OnItemClickListener listener;
    //为recyclerView添加的头部视图
    View headerView;
    private static final int HEADER=0;
    private static final int ITEM=1;



    public CityAdapter(Context context, List<CitynameBean> datas) {
        this.context = context;
        this.datas = datas;
        this.inflater = LayoutInflater.from(context);
    }

  public void setOnItemClickListener(OnItemClickListener listener){
      this.listener=listener;
  }
  public void addHeaderView(View v){
      if (headerView==null){
      this.headerView=v;
          notifyItemChanged(0);
      }else {
          throw new RuntimeException("不允许添加多个头部");

      }
  }

    @Override
    public int getItemViewType(int position) {
        if (this.headerView!=null){
            if (position==0){
                return HEADER;
            }else {
                return ITEM;
            }
        }
        return ITEM;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==HEADER){
            View v=inflater.inflate(R.layout.include_cityname_header,parent,false);
            MyViewHolder myViewHolder=new MyViewHolder(v);
            return myViewHolder;
        }

        //创建viewholder
        View view = inflater.inflate(R.layout.item_city_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (headerView!=null&&position==0){
            return;
        }
        final int dataIndex=getDataIndex(position);
        //将第position位置的数据放到viewholder中显示
        CitynameBean citynameBean = datas.get(dataIndex);
        holder.tvCityName.setText(citynameBean.getCityName());
        holder.tvLetter.setText(citynameBean.getLetter()+"");
        //position这个位置上的数据，是不是该数据所属分组的起始位置
        if (dataIndex==getPositionForSection(getSectionForPosition(dataIndex))){
            holder.tvLetter.setVisibility(View.VISIBLE);

        }else {
            holder.tvLetter.setVisibility(View.GONE);
        }
        View itemView=holder.itemView;
        if (this.listener!=null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(v,dataIndex);
                }
            });
        }
    }

    private int getDataIndex(int position) {
        return headerView==null?position:position-1;
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addAll(List<CitynameBean> list, boolean isClear) {
        if (isClear) {
            datas.clear();
        }
            datas.addAll(list);
            notifyDataSetChanged();

    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 某一个分组的其实位置是什么
     * @param sectionIndex
     * @return
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i=0;i<datas.size();i++){
            if (datas.get(i).getLetter()==sectionIndex){
                return i;
            }
        }
        //当前的数据源没有任何一个数据属于传入的sectionIndex
        return datas.size()+1;//不存在返回一个不存在的值
    }

    /**
     *  第position上的数据的分组是什么
     * @param position
     * @return
     */
    @Override
    public int getSectionForPosition(int position) {
        return datas.get(position).getLetter();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //利用butterknife完成对viewholder的赋值
        @Nullable
        @BindView(R.id.tv_itemCity_alpha)
        TextView tvLetter;
        @Nullable
        @BindView(R.id.tv_itemCity_city)
        TextView tvCityName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public interface OnItemClickListener{
        void OnItemClick(View itemView,int position);
    }
}
