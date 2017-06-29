package com.example.tarena.groupon.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.tarena.groupon.bean.BusinessBean;
import com.example.tarena.groupon.bean.CityBean;
import com.example.tarena.groupon.bean.DistrictBean;
import com.example.tarena.groupon.bean.TuanBean;
import com.example.tarena.groupon.config.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by tarena on 2017/6/19.
 */

public class RetrofitClient {
    private static RetrofitClient INSTANCE;

    public static RetrofitClient getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (RetrofitClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitClient();
                }
            }
        }
        return INSTANCE;
    }

    private Retrofit retrofit;
    private NetService netService;
    private OkHttpClient okHttpClient;

    private RetrofitClient() {
        okHttpClient = new OkHttpClient.Builder().addInterceptor(new MyOkHttpInterceptor()).build();
        retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(Constant.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        netService = retrofit.create(NetService.class);
    }

    public void test(String city,String region, final Callback<BusinessBean> beanCallback) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("city", city);
        params.put("category","美食");
        if(!TextUtils.isEmpty(region)){
            params.put("region",region);
        }
        Call<BusinessBean> call2 = netService.getBusiness(params);
        call2.enqueue(beanCallback);


    }


    public void getDailyDeals3(String city, final Callback<TuanBean> callback2) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("city", city);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
        params.put("date", date);
        Call<String> idcall = netService.getDailyIds2(params);
        idcall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("id_list");

                    int size = jsonArray.length();
                    if (size > 40) {
                        size = 40;
                    }

                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < size; i++) {
                        String id = jsonArray.getString(i);
                        sb.append(id).append(",");
                    }
                    if (sb.length() > 0) {
                        String idlist = sb.substring(0, sb.length() - 1);

                        Map<String, String> params2 = new HashMap<String, String>();
                        params2.put("deal_ids", idlist);
                        Call<TuanBean> dealCall = netService.getDailyDeals3(params2);
                        dealCall.enqueue(callback2);
                    } else {
                        callback2.onResponse(null, null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {

            }
        });
    }

    public void getDailyDeals(String city, final Callback<String> callback2) {
        final Map<String, String> params = new HashMap<>();
        params.put("city", city);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
        params.put("date", date);
        String sign = HttpUtil.getSign(HttpUtil.APPKEY, HttpUtil.APPSECRET, params);
        Call<String> ids = netService.getDailyIds(HttpUtil.APPKEY, sign, params);
        ids.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("id_list");
                    Log.d("TAG", "jsonArray: " + jsonArray);
                    int size = jsonArray.length();
                    if (size > 40) {
                        size = 40;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < size; i++) {
                        String id = jsonArray.getString(i);
                        sb.append(id).append(",");
                    }
                    if (sb.length() > 0) {
                        String idlist = sb.substring(0, sb.length() - 1);
                        Map<String, String> param2 = new HashMap<String, String>();
                        param2.put("deal_ids", idlist);
                        Log.d("TAG", "deal_ids: " + idlist);
                        String sign2 = HttpUtil.getSign(HttpUtil.APPKEY, HttpUtil.APPSECRET, param2);
                        Log.d("TAG", "sign2: " + sign2);
                        Call<String> call2 = netService.getDailyDeals(HttpUtil.APPKEY, sign2, param2);
                        Log.d("TAG", "param2: " + param2);
                        Log.d("TAG", "call2: " + call2);
                        call2.enqueue(callback2);
                    } else {
                        callback2.onResponse(null, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {

            }
        });

    }

    public void getCities(Callback<CityBean> callback) {
        Call<CityBean> call = netService.getCities();
        Log.d("TAG", "getcities------> " + call);
        call.enqueue(callback);

    }

    /**
     * OKHTTP的拦截器
     */
    public class MyOkHttpInterceptor implements Interceptor {


        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            //获得请求对象
            Request request = chain.request();
            //获得请求路径
            //比如：http://baseurl/deal/get_daily_new_id_list?city=xxx&date=xxx
            HttpUrl url = request.url();
            //取出原有请求路径中的参数
            HashMap<String, String> params = new HashMap<>();
            //原有请求路径中，请求参数的名字
            //例如{city,date}
            Set<String> set = url.queryParameterNames();
            for (String key : set) {
                params.put(key, url.queryParameter(key));
            }
            String sign = HttpUtil.getSign(HttpUtil.APPKEY, HttpUtil.APPSECRET, params);
            //字符串形式的http://baseurl/deal/get_daily_new_id_list?city=xxx&date=xxx
            String urlString = url.toString();
            Log.d("TAG", "原始请求路径------> " + urlString);
            StringBuilder sb = new StringBuilder(urlString);
            if (set.size() == 0) {
                //意味着原有请求路径中没有参数
                sb.append("?");
            } else {
                sb.append("&");
            }
            sb.append("appkey=").append(HttpUtil.APPKEY);
            sb.append("&").append("sign=").append(sign);
            //http://baseurl/deal/get_daily_new_id_list?city=xxx&date=xxx&appkey=xxx&sign=xxx
            Log.d("TAG", "新的请求路径------>: " + sb.toString());
            okhttp3.Request newRequest = new Request.Builder().url(sb.toString()).build();
            return chain.proceed(newRequest);
        }
    }


    public void District(String city, final Callback<DistrictBean> beanCallback) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("city", city);
        Call<DistrictBean> call3 =  netService.getDistrict(params);
        call3.enqueue(beanCallback);


    }
}
