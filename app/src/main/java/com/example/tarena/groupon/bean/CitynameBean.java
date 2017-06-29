package com.example.tarena.groupon.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by tarena on 2017/6/21.
 */
@DatabaseTable
public class CitynameBean implements Serializable {
    @DatabaseField(id=true) //创建文本类型字段,id=true表示主键列
    String cityName;//城市中文名字
    @DatabaseField
    String pyName;//拼音名字
    @DatabaseField
    char letter;//城市拼音首字

    public CitynameBean() {
    }

    public CitynameBean(String cityName, String pyName, char letter) {
        this.cityName = cityName;
        this.pyName = pyName;
        this.letter = letter;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPyName() {
        return pyName;
    }

    public void setPyName(String pyName) {
        this.pyName = pyName;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return "CitynameBean{" +
                "cityName='" + cityName + '\'' +
                ", pyName='" + pyName + '\'' +
                ", letter=" + letter +
                '}';
    }
}
