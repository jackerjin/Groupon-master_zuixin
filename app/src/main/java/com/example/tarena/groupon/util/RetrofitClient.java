package com.example.tarena.groupon.util;

import android.util.Log;

import com.example.tarena.groupon.config.Constant;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by tarena on 2017/6/19.
 */

public class RetrofitClient {
    private static RetrofitClient INSTANCE;
    public static RetrofitClient getINSTANCE(){
        if (INSTANCE==null){
            synchronized (RetrofitClient.class){
                if (INSTANCE==null){
                INSTANCE=new RetrofitClient();
                }
            }
        }
        return INSTANCE;
    }
    private Retrofit retrofit;
    private NetService netService;
    private RetrofitClient(){
        retrofit=new Retrofit.Builder().baseUrl(Constant.BASEURL).addConverterFactory(ScalarsConverterFactory.create()).build();
        netService=retrofit.create(NetService.class);
    }
    public void test(){
        Map<String,String> params=new HashMap<>();
        params.put("city","北京");
        params.put("category","美食");
        String sign=HttpUtil.getSign(HttpUtil.APPKEY,HttpUtil.APPSECRECT,params);
        Call<String> call=netService.test(HttpUtil.APPKEY,sign,params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String string=response.body();
                Log.d("TAG", "onResponse: "+string);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {

            }
        });
    }
}
