package com.example.tarena.groupon.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.tarena.groupon.bean.BusinessBean;
import com.example.tarena.groupon.bean.CitynameBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * 创建保存的数据表
 * Created by tarena on 2017/6/22.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static DBHelper INSTANCE;

    public static DBHelper getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (DBHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DBHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public DBHelper(Context context) {
        super(context, "city.db", null, 1);
    }

    /**
     * @param sqLiteDatabase
     * @param connectionSource 建立与数据库的连接
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        //在第一次常见city.db数据库时，该方法会被调用
        //创建存储的数据表
        try {
            TableUtils.createTableIfNotExists(connectionSource, CitynameBean.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, CitynameBean.class, true);//true代表强行删除
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
