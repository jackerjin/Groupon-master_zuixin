package com.example.tarena.groupon.ui;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.example.tarena.groupon.R;
import com.example.tarena.groupon.adpter.MyPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuideActivity extends FragmentActivity {
    @BindView(R.id.vp_main)
    ViewPager viewPager;
    MyPagerAdapter adapter;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initViewPager();
    }

    private void initViewPager() {
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        //当前运行程序所使用的设备的屏幕密度
        //低密度Ldpi 120px/linch(2.54cm)
        //中密度mdpi 160px/linch
        //高密度hdpi 240
        //很高密度xhdpi 320
        //非常高密度xxhpdi 480
        //dp绝对单位 1dp=1/160 linch
        //1dp 在低密度屏幕上等于0.75px
        //1dp 在中密度上等于1px

        //另外一种获得10dp在当前设备屏幕密度上的像素
        // float px= TypedValue.applyDimension()
        final float density = getResources().getDisplayMetrics().density;
        indicator.setBackgroundColor(0xFFffffff);
        //5dp在当前涉笔上所对应的像素值（px）

        indicator.setRadius(5 * density);
        indicator.setPageColor(0xffffffff);
        indicator.setFillColor(0xffE15A36);
        indicator.setStrokeColor(0xFF000000);
        indicator.setStrokeWidth(1 * density);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //NO_OP
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    indicator.setVisibility(View.INVISIBLE);
                } else {
                    indicator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
               //NO_OP
            }
        });
    }
}
