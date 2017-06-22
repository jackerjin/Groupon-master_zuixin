package com.example.tarena.groupon.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * Created by tarena on 2017/6/21.
 */

public class PinYinUtil {
    public static String getPinYin(String name){
        try {
            String result=null;
            //1)设定汉语拼音的格式
            HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//大写
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//不要语音语调
            //根据设置好的格式，逐字进行汉子道拼音的转换
            StringBuilder sb=new StringBuilder();
            for (int i=0;i<name.length();i++){
                char c=name.charAt(i);
                String [] pinyin= PinyinHelper.toHanyuPinyinStringArray(c,format);
                if (pinyin.length>0){
                    sb.append(pinyin[0]);
                }
            }
            result=sb.toString();
            return result;
        }catch (Exception e
                ){
            e.printStackTrace();
            throw new RuntimeException("不正确的汉语拼音格式");
        }
    }
    public static char getLetter(String name){
        return getPinYin(name).charAt(0);
    }

}
