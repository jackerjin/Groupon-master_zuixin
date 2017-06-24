package com.example.tarena.groupon.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tarena.groupon.R;
import com.example.tarena.groupon.adpter.BusinessAdapter;
import com.example.tarena.groupon.app.Myapp;
import com.example.tarena.groupon.bean.BusinessBean;
import com.example.tarena.groupon.util.DBUtil;
import com.example.tarena.groupon.util.HttpUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessActivity extends Activity {


    @BindView(R.id.pullToRefresh_ListView_business)
    PullToRefreshListView ptrListView;
    ListView listView;
    List<BusinessBean.BusinessesBean> datas;
    BusinessAdapter adapter;
    private String city;
    DBUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        ButterKnife.bind(this);
        dbUtil = new DBUtil(this);
        initListView();
    }

    private void initListView() {
        listView = ptrListView.getRefreshableView();
        datas = new ArrayList<>();
        adapter = new BusinessAdapter(this, datas);
        listView.setAdapter(adapter);
        ptrListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
//        //从内存缓存中读取城市数据
//        if (Myapp.businessBeanList != null && Myapp.businessBeanList.size() > 0) {
//            adapter.addAll(Myapp.businessBeanList, true);
//            Log.d("TAG", "城市数据从内存缓存中加载的");
//            //将数据缓起来
//            return;
//        }
//        //从数据库中取城市数据
//        List<BusinessBean.BusinessesBean> list = dbUtil.query();
//        if (list != null && list.size() > 0) {
//            adapter.addAll(list, true);
//            Myapp.businessBeanList = list;
//            Log.d("TAG", "城市数据从数据库加载的");
//            return;
//        }


        city = getIntent().getStringExtra("city");
        HttpUtil.getBusinessByRetrofit(city, new Callback<BusinessBean>() {
            @Override
            public void onResponse(Call<BusinessBean> call, Response<BusinessBean> response) {
                if (response != null) {
                    BusinessBean businessbean = response.body();
                    List<BusinessBean.BusinessesBean> deals = businessbean.getBusinesses();
                    Log.d("TAG", "BusinessesBean" + deals);
                    adapter.addAll(deals, true);
                } else {
                    Toast.makeText(BusinessActivity.this, "今日无新增团购内容", Toast.LENGTH_SHORT).show();
                }
                ptrListView.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<BusinessBean> call, Throwable throwable) {

            }
        });
    }
}
