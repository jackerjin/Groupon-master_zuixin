package com.example.tarena.groupon.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.example.tarena.groupon.bean.BusinessBean;
import com.example.tarena.groupon.bean.CitynameBean;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by tarena on 2017/6/22.
 */

public class DBUtil {
    DBHelper dbHelper;
    Dao<CitynameBean, String> dao;

    public DBUtil(Context context) {

        try {
            dbHelper =DBHelper.getINSTANCE(context);
            dao = dbHelper.getDao(CitynameBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * batch批量操作
     */
    public void insertBatch(final List<CitynameBean> list){
        //建立连接后，一次性将数据全部写入
        try {
            dao.callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (CitynameBean bean:list){
                        insert(bean);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加数据
     *
     * @param citynameBean
     */
    public void insert(CitynameBean citynameBean) {
        try {
            dao.createIfNotExists(citynameBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertAll(List<CitynameBean> list) {
        for (CitynameBean bean : list) {
            insert(bean);
        }
    }

    public List<CitynameBean> query() {
        try {
            List<CitynameBean> citynameBeanList = dao.queryForAll();
            return citynameBeanList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询数据库时出现异常");
        }
    }

}
