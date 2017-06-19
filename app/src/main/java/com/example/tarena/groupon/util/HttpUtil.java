package com.example.tarena.groupon.util;

import android.util.Log;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * 网络访问工具类
 * <p>
 * 符合大众点评服务器要求的地址：
 * http://网址部分?参数1=值1&参数2=值2....
 * <p>
 * http://api.dianping.com/v1/business/find_businesses?appkey=49814079&sign=生成的签名&city=%xx%xx%xx%xx%xx%xx&category=%xx%xx%xx%xx%xx%xx
 * <p>
 * 请求地址中签名的生成：
 * 利用Appkey,AppSecret,以及其他访问参数(例如city,category)
 * 首先将Appkey,AppSecret以及其他访问参数拼接成一个字符串
 * 例:49814079category美食city上海90e3438a41d646848033b6b9d461ed54
 * 将拼接好的字符串进行转码(转码算法为SHA1算法)
 * 转码后就得到了签名
 * <p>
 * Created by pjy on 2017/6/19.
 */

public class HttpUtil {
    public static final String APPKEY = "49814079";
    public static final String APPSECRECT = "90e3438a41d646848033b6b9d461ed54";

    //获得满足大众点评要求的请求路径
    public static String getURL(String url, Map<String, String> params) {
        String result = "";
        String sign = getSign(APPKEY, APPSECRECT, params);
        String query = getQuery(APPKEY, sign, params);
        return result;
    }

    public static String getSign(String appkey, String appsecrect, Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();

// 对参数名进行字典排序
        String[] keyArray = params.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);
// 拼接有序的参数名-值串
        stringBuilder.append(appkey);
        for (String key : keyArray) {
            stringBuilder.append(key).append(params.get(key));
        }
        String codes = stringBuilder.append(appsecrect).toString();
        //在纯java环境中，利用Codec对字符串进行SHA1转码采用如下方式
//        String sign = org.apache.commons.codec.digest.DigestUtils.shaHex(codes).toUpperCase();
        //安卓环境中，利用Codec对字符串进行SHA1转码采用如下方式
        String sign = new String(Hex.encodeHex(DigestUtils.sha(codes))).toUpperCase();
        return sign;
    }

    public static String getQuery(String appkey, String sign, Map<String, String> params) {

        try {
            // 添加签名
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("appkey=").append(appkey).append("&sign=").append(sign);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuilder.append('&').append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "utf-8"));
            }
            String queryString = stringBuilder.toString();
            return queryString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            //仍异常
            throw new RuntimeException("使用了不正确的字符");

        }
    }
    public static void testVolley(){
      VolleyClient.getINSTANCE().test();

    }
    public static void testRetrofit(){
      RetrofitClient.getINSTANCE().test();
    }
}
