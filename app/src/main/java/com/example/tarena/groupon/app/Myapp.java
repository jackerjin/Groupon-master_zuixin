package com.example.tarena.groupon.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.example.tarena.groupon.bean.BusinessBean;
import com.example.tarena.groupon.bean.CitynameBean;
import com.example.tarena.groupon.util.SPUtil;

import java.util.List;

/**
 * Created by tarena on 2017/6/19.
 */

public class Myapp extends Application {
    public static Myapp CONTEXT;
    //城市名称缓存
    public static List<CitynameBean> citynameBeanList;
    public static List<BusinessBean.BusinessesBean> businessBeanList;
    public static LatLng myLocation;
    public static String region;
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        CONTEXT=this;
        SPUtil spUtil=new SPUtil(this);
        spUtil.setCloseBanner(false);

    }
}
