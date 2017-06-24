package com.example.tarena.groupon.app;

import android.app.Application;

import com.example.tarena.groupon.bean.BusinessBean;
import com.example.tarena.groupon.bean.CitynameBean;

import java.util.List;

/**
 * Created by tarena on 2017/6/19.
 */

public class Myapp extends Application {
    public static Myapp CONTEXT;
    //城市名称缓存
    public static List<CitynameBean> citynameBeanList;
    public static List<BusinessBean.BusinessesBean> businessBeanList;
    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT=this;

    }
}
