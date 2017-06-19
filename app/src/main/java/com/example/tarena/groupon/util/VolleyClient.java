package com.example.tarena.groupon.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tarena.groupon.app.Myapp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tarena on 2017/6/19.
 */

public class VolleyClient {
    //声明一个私有的静态的属性
    private static VolleyClient INSTANCE;

    //声明一个公有的静态的获得1）属性的方法
    public  static VolleyClient getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (VolleyClient.class) { //同步代码块，加锁
                if (INSTANCE == null) {
                    INSTANCE = new VolleyClient();
                }
            }
        }
        return INSTANCE;
    }

    RequestQueue queue;

    //构造器私有化
    private VolleyClient() {
        queue = Volley.newRequestQueue(Myapp.CONTEXT);
    }
    private VolleyClient(Context context){
        queue=Volley.newRequestQueue(context);
    }

    public void test() {
        Map<String, String> params = new HashMap<>();
        params.put("city", "北京");
        params.put("category", "美食");
        String url = HttpUtil.getURL("http://api.dianping.com/v1/business/find_businesses", params);
        //请求队列
        //请求对象StringRequest

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("TAG", "利用Volley获取的服务器响应内容： " + s);
            }
        },null);
        //将请求对象放在请求队列中
        queue.add(request);
    }
}
