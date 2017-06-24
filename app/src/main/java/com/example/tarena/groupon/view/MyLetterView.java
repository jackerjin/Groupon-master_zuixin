package com.example.tarena.groupon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.tarena.groupon.R;


/**
 *自定义View
 * Created by tarena on 2017/6/23.
 */

public class MyLetterView extends View {
    private String[] letters={"热门","A","B","C","D","E","F","G","H","I","G",
            "K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    Paint paint;
    OnTouchLEtterListener listener;
    int lettercolor;
    public MyLetterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray t=context.obtainStyledAttributes(attrs, R.styleable.MyLetterView);
        lettercolor=t.getColor(R.styleable.MyLetterView_letter_color,Color.BLACK);
        t.recycle();
        initPaint();
    }
    public void setOnTouchLEtterListener( OnTouchLEtterListener listener){
        this.listener=listener;

    }
    private void initPaint() {
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        float size= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,14,getResources().getDisplayMetrics() );//20sp在当前设备上所对应的像素值（px）
        paint.setTextSize(size);
        paint.setColor(lettercolor);
    }

    /**
     * 是用来设定myletterview尺寸
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**+
     *
     * 一定要重写
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width=getWidth();
        int height=getHeight();

        for (int i=0;i<letters.length;i++){

            String letter=letters[i];
            //获取文字边界的大小
            Rect bounds=new Rect();
            paint.getTextBounds(letter,0,letter.length(),bounds);
            //bounds.width();
            float x=width/2-bounds.width()/2;//分配给letter小空间的宽度一半-letter宽度的一半
            float y=height/letters.length/2+bounds.height()/2+i*height/letters.length;//分配给letter小空间的高度一半+letter高度的一半
            canvas.drawText(letter,x,y,paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //按下，还是移动，还是抬起
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //改变背景
                setBackgroundColor(Color.GRAY);
                if (listener!=null){
                    //手指按下或移动时距离没有letter顶的距离
                    float y = event.getY();
                    //根据距离y换算文字的下标值
                    int idx= (int) ((y*letters.length)/getHeight());
                    if (idx>=0&&idx<letters.length){
                        String letter=letters[idx];
                        listener.onTouchLetter(this,letter);
                    }
                }
                break;
            default:
                setBackgroundColor(Color.TRANSPARENT);
                break;
        }
        return true;
    }
    public interface OnTouchLEtterListener{
        void onTouchLetter(MyLetterView view,String letter);

    }
}
