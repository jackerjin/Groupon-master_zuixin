package com.example.tarena.groupon.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.tarena.groupon.R;
import com.example.tarena.groupon.adpter.CityAdapter;
import com.example.tarena.groupon.app.Myapp;
import com.example.tarena.groupon.bean.CityBean;
import com.example.tarena.groupon.bean.CitynameBean;
import com.example.tarena.groupon.util.DBUtil;
import com.example.tarena.groupon.util.HttpUtil;
import com.example.tarena.groupon.util.PinYinUtil;
import com.example.tarena.groupon.view.MyLetterView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityActivity extends Activity {
    @BindView(R.id.city_recyclerlayout)
    RecyclerView recyclerView;
    CityAdapter adapter;
    List<CitynameBean> datas;
    @BindView(R.id.myletterView_city)
    MyLetterView myLetterView;
DBUtil dbUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        dbUtil=new DBUtil(this);
        ButterKnife.bind(this);
        initRecyclerView();
        myLetterView.setOnTouchLEtterListener(new MyLetterView.OnTouchLEtterListener() {
            @Override
            public void onTouchLetter(MyLetterView view,String letter) {
              LinearLayoutManager manager= (LinearLayoutManager) recyclerView.getLayoutManager();
                if ("热门".equals(letter)){
                    manager.scrollToPosition(0);
                }else {
                    int position=adapter.getPositionForSection(letter.charAt(0));
                    //移动到position个视图位置，且该视图位于当前recyclerview顶端，offset为偏移像素
                    //大于零往下移，小于零往上移动
                    manager.scrollToPositionWithOffset(position+1,0);
                }

            }
        });
    }

    private void initRecyclerView() {
        //初始化数据源，适配器
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        datas = new ArrayList<>();
        adapter = new CityAdapter(this, datas);
        recyclerView.setAdapter(adapter);
        View headView= LayoutInflater.from(this).inflate(R.layout.include_cityname_header,recyclerView,false);
        adapter.addHeaderView(headView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new CityAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View itemView, int position) {
                CitynameBean citynameBean = datas.get(position);
                //Toast.makeText(CityActivity.this, citynameBean.getCityName(), Toast.LENGTH_SHORT).show();
                String city=citynameBean.getCityName();
//                Intent intent=new Intent(CityActivity.this,MainActivity.class);
//                intent.putExtra("city",city);
//                startActivity(intent);
                Intent data=new Intent();
                data.putExtra("city",city);
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Refresh();
    }

    private void Refresh() {
        //从内存缓存中读取城市数据
        if (Myapp.citynameBeanList!=null&&Myapp.citynameBeanList.size()>0){
            adapter.addAll(Myapp.citynameBeanList,true);
            Log.d("TAG", "城市数据从内存缓存中加载的");
            //将数据缓起来
            return;
        }
      //从数据库中取城市数据
        List<CitynameBean> list=dbUtil.query();
        if (list!=null&&list.size()>0){
            adapter.addAll(list,true);
            Myapp.citynameBeanList=list;
            Log.d("TAG", "城市数据从数据库加载的");
            return;
        }



        //调用HttpUtil获取城市信息
        HttpUtil.getCitiesByRetrofit(new Callback<CityBean>() {
            @Override
            public void onResponse(Call<CityBean> call, Response<CityBean> response) {
//                Log.d("TAG", "onResponse: "+response);
                CityBean cityBean = response.body();
                //"全国，上海，杭州，北京，其它城市..."
                List<String> list = cityBean.getCities();
                //根据List<String>创建一个List<CitynameBean>
                //将List<CitynameBean>放到RecyclerView中显示]
                final List<CitynameBean> citynameBeanList=new ArrayList<CitynameBean>();
                for (String name:list){
                    if (!name.equals("全国")&&!name.equals("其他城市")&&!name.equals("点评实验室")){
                    CitynameBean citynameBean=new CitynameBean();
                        citynameBean.setCityName(name);
                        citynameBean.setPyName(PinYinUtil.getPinYin(name));
                        citynameBean.setLetter(PinYinUtil.getLetter(name));
                        Log.d("TAG", "onResponse: "+citynameBean);
                        citynameBeanList.add(citynameBean);
                    }
                }
                Collections.sort(citynameBeanList, new Comparator<CitynameBean>() {
                    @Override
                    public int compare(CitynameBean o1, CitynameBean o2) {
                        return o1.getPyName().compareTo(o2.getPyName());
                    }
                });
                adapter.addAll(citynameBeanList, true);

                //将数据缓存起来
                Myapp.citynameBeanList=citynameBeanList;
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        long start=System.currentTimeMillis();
                        dbUtil.insertBatch(citynameBeanList);
//                        Log.d("TAG", "写入数据库完毕，耗时："+(System.currentTimeMillis()-start));
                    }
                }.start();
            }

            @Override
            public void onFailure(Call<CityBean> call, Throwable throwable) {

            }
        });

    }
    @OnClick(R.id.ll_header_search)
    public void jumpTo(View view){
        Intent intent=new Intent(CityActivity.this,SearchActivity.class);
        startActivityForResult(intent,101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==101){
            setResult(RESULT_OK,data);
            finish();
        }
    }

}
