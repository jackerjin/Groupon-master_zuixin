package com.example.tarena.groupon.adpter;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tarena.groupon.R;
import com.example.tarena.groupon.bean.Comment;
import com.example.tarena.groupon.util.HttpUtil;
import com.example.tarena.groupon.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tarena on 2017/6/28.
 */

public class CommentAdapter extends MyBaseAdapter<Comment> {
    public CommentAdapter(Context context, List<Comment> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 final ViewHolder holder;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.comment_detail_item_layout,parent,false);
            holder=new ViewHolder(convertView);
            holder.llContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int width=holder.llContainer.getWidth();
                    int margin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,3,context.getResources().getDisplayMetrics());
                    int size=(width-2*margin)/3;
                    for (int i=0;i<holder.llContainer.getChildCount();i++){
                        ImageView iv= (ImageView) holder.llContainer.getChildAt(i);
                        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(size,size);
                        if (i!=0){
                            params.setMargins(margin,0,0,0);
                        }
                        iv.setLayoutParams(params);
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);

                    }
                    holder.llContainer.requestLayout();
                }
            });
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        Comment comment=getItem(position);
        HttpUtil.loadImage(comment.getAvatar(),holder.ivAvatar);
        holder.tvName.setText(comment.getName());
        holder.tvDate.setText(comment.getDate());
        holder.tvPrice.setText(comment.getPrice());

        int[] resIds = new int[]{
                R.drawable.movie_star10,
                R.drawable.movie_star20,
                R.drawable.movie_star30,
                R.drawable.movie_star35,
                R.drawable.movie_star40,
                R.drawable.movie_star45,
                R.drawable.movie_star50
        };
        //star40
        int resId = 0;
        String rating = comment.getRating();
        if (rating.contains("20")) {
            resId = 1;
        } else if (rating.contains("30")) {
            resId = 2;
        } else if (rating.contains("35")) {
            resId = 3;
        } else if (rating.contains("40")) {
            resId = 4;
        } else if (rating.contains("45")) {
            resId = 5;
        } else if (rating.contains("50")) {
            resId = 6;
        }
        holder.ivRating.setImageResource(resIds[resId]);

        holder.tvContent.setText(comment.getContent());
        String[] imgs=comment.getImgs();
        ImageView[] ivs=new ImageView[3];
        ivs[0] = holder.ivImg1;
        ivs[1] = holder.ivImg2;
        ivs[2] = holder.ivImg3;
        for (int idx=0;idx<3;idx++){
            ivs[idx].setVisibility(View.GONE);
        }
        for (int idx=0;idx<3;idx++){
            ivs[idx].setVisibility(View.VISIBLE);
            HttpUtil.loadImage(imgs[idx],ivs[idx]);
        }
        return convertView;
    }
    public class ViewHolder{
        @BindView(R.id.iv_comment_header)
        CircleImageView ivAvatar;
        @BindView(R.id.tv_comment_name)
        TextView tvName;
        @BindView(R.id.tv_comment_date)
        TextView tvDate;
        @BindView(R.id.tv_comment_price)
        TextView tvPrice;
        @BindView(R.id.iv_comment_rate)
        ImageView ivRating;
        @BindView(R.id.tv_comment_content)
        TextView tvContent;
        @BindView(R.id.iv_comment_img01)
        ImageView ivImg1;
        @BindView(R.id.iv_comment_img02)
        ImageView ivImg2;
        @BindView(R.id.iv_comment_img03)
        ImageView ivImg3;
        @BindView(R.id.ll_comment_item_container)
        LinearLayout llContainer;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
