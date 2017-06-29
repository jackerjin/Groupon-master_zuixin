package com.example.tarena.groupon.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tarena.groupon.R;
import com.example.tarena.groupon.adpter.BusinessAdapter;
import com.example.tarena.groupon.app.Myapp;
import com.example.tarena.groupon.bean.BusinessBean;
import com.example.tarena.groupon.bean.DistrictBean;
import com.example.tarena.groupon.util.DBUtil;
import com.example.tarena.groupon.util.HttpUtil;
import com.example.tarena.groupon.util.SPUtil;
import com.example.tarena.groupon.view.MyBanner;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    SPUtil spUtil;
    @BindView(R.id.iv_business_loading)
    ImageView ivLoading;
    List<DistrictBean.CitiesBean.DistrictsBean> districtList;
    ArrayAdapter<String> leftAdapter;
    ArrayAdapter<String> rightAdapter;
    List<String> leftDatas;
    List<String> rightDatas;
    @BindView(R.id.street_listView)
    ListView rightListView;
    @BindView(R.id.district_listView)
    ListView leftListView;
    @BindView(R.id.district_select_layout)
    View DistrictLayout;
    @BindView(R.id.tv_business_fujin)
    TextView tvRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        city = getIntent().getStringExtra("city");
        ButterKnife.bind(this);
        dbUtil = new DBUtil(this);
        spUtil = new SPUtil(this);
        initListView();
    }

    @OnClick(R.id.tv_business_fujin)
    public void toggleMenu(View view) {
        if (DistrictLayout.getVisibility() == View.VISIBLE) {
            DistrictLayout.setVisibility(View.INVISIBLE);
        } else {
            DistrictLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initListView() {
        listView = ptrListView.getRefreshableView();
        datas = new ArrayList<>();
        adapter = new BusinessAdapter(this, datas);
        if (!spUtil.isCloseBanner()) {
            final MyBanner myBanner = new MyBanner(this, null);
            myBanner.setOnCloseBannerListener(new MyBanner.OnCloseBannerListener() {
                @Override
                public void onClose() {
                    spUtil.setCloseBanner(true);
                    listView.removeHeaderView(myBanner);
                }
            });
            listView.addHeaderView(myBanner);
        }
        listView.setAdapter(adapter);
//        lvStreet.setAdapter(arrayAdapter);
        AnimationDrawable drawable = (AnimationDrawable) ivLoading.getDrawable();
        drawable.start();
        listView.setEmptyView(ivLoading);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusinessBean.BusinessesBean business;
                if (spUtil.isCloseBanner()){
                    business=adapter.getItem(position-1);
                }else {
                    business=adapter.getItem(position-2);
                }
                Intent intent=new Intent(BusinessActivity.this,DetailActivity.class);
                intent.putExtra("business",business);
                startActivity(intent);
            }
        });
        leftDatas = new ArrayList<>();
        leftAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, leftDatas);
        leftListView.setAdapter(leftAdapter);
        rightDatas = new ArrayList<>();
        rightAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rightDatas);
        rightListView.setAdapter(rightAdapter);
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                DistrictBean.CitiesBean.DistrictsBean district = districtList.get(i);
                List<String> neighborhoods = new ArrayList<String>(district.getNeighborhoods());
                neighborhoods.add(0, "全部" + district.getDistrict_name());
                rightDatas.clear();
                rightDatas.addAll(neighborhoods);
                rightAdapter.notifyDataSetChanged();

            }
        });
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String region = rightAdapter.getItem(position);
                if (position == 0) {
                    //region "全部xx区"
                    region.substring(2, region.length());
                }
                tvRegion.setText(region);
                Myapp.region=region;
                DistrictLayout.setVisibility(View.INVISIBLE);
                adapter.removeAll();
                HttpUtil.getBusinessByRetrofit(city, region,new Callback<BusinessBean>() {
                    @Override
                    public void onResponse(Call<BusinessBean> call, Response<BusinessBean> response) {
                        BusinessBean businessbean = response.body();
                        List<BusinessBean.BusinessesBean> deals = businessbean.getBusinesses();
                        adapter.addAll(deals, true);
                    }
                    @Override
                    public void onFailure(Call<BusinessBean> call, Throwable throwable) {
                    }
                });
            }
        });
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

        if (Myapp.region==null){
            tvRegion.setText("全部");
        }else {
            tvRegion.setText(Myapp.region);
        }

        HttpUtil.getBusinessByRetrofit(city, Myapp.region,new Callback<BusinessBean>() {
            @Override
            public void onResponse(Call<BusinessBean> call, Response<BusinessBean> response) {
                    BusinessBean businessbean = response.body();
                    List<BusinessBean.BusinessesBean> deals = businessbean.getBusinesses();
                    adapter.addAll(deals, true);
            }
            @Override
            public void onFailure(Call<BusinessBean> call, Throwable throwable) {
            }
        });
        HttpUtil.getDistrict(city, new Callback<DistrictBean>() {
            @Override
            public void onResponse(Call<DistrictBean> call, Response<DistrictBean> response) {
                Log.d("TAG", "DistrictBean" + response);
                DistrictBean districtBean = response.body();
                districtList = districtBean.getCities().get(0).getDistricts();
                List<String> districtName = new ArrayList<String>();
                for (int i = 0; i < districtList.size(); i++) {

                    DistrictBean.CitiesBean.DistrictsBean district = districtList.get(i);
                    districtName.add(district.getDistrict_name());

                }
                leftDatas.clear();
                leftDatas.addAll(districtName);
                leftAdapter.notifyDataSetChanged();
                List<String> neighborhoods = new ArrayList<String>(districtList.get(0).getNeighborhoods());
                String districtNames = districtList.get(0).getDistrict_name();
                neighborhoods.add(0, "全部" + districtNames);
                rightDatas.clear();
                rightDatas.addAll(neighborhoods);
                rightAdapter.notifyDataSetChanged();
                ptrListView.onRefreshComplete();
            }


            @Override
            public void onFailure(Call<DistrictBean> call, Throwable throwable) {

            }
        });

    }
}
