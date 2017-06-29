package com.example.tarena.groupon.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.tarena.groupon.R;
import com.example.tarena.groupon.app.Myapp;
import com.example.tarena.groupon.bean.BusinessBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindActivity extends Activity {
    BusinessBean.BusinessesBean business;
    MapView mMapView = null;
    BaiduMap baiduMap;
    String from;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    @BindView(R.id.btn_find_search)
    Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        business = (BusinessBean.BusinessesBean) getIntent().getSerializableExtra("business");
       ButterKnife.bind(this);
        from = getIntent().getStringExtra("from");
        mMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mMapView.getMap();
        //更改地图默认的比例尺
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17);
        baiduMap.animateMapStatus(msu);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle=marker.getExtraInfo();
                if (bundle!=null) {
                    String name = bundle.getString("name");
                    String address = bundle.getString("address");
                    String number = bundle.getString("number");
                    double distance = com.example.tarena.groupon.util.DistanceUtil.getDistance1(Myapp.myLocation, marker.getPosition());
                    String dis = (int) (distance * 100) / 100.0 + "米";
                    View view = LayoutInflater.from(FindActivity.this).inflate(R.layout.infowindow_layout, null);
                    TextView tv1 = (TextView) view.findViewById(R.id.tv_find_name);
                    tv1.setText(name);
                    TextView tv2 = (TextView) view.findViewById(R.id.tv_find_address);
                    tv2.setText(address);
                    TextView tv3 = (TextView) view.findViewById(R.id.tv_find_phone);
                    tv3.setText(number);
                    TextView tv4 = (TextView) view.findViewById(R.id.tv_find_distance);
                    tv4.setText(dis);
                    InfoWindow infoWindow = new InfoWindow(view, marker.getPosition(), -50);
                    baiduMap.showInfoWindow(infoWindow);
                }

                return true;
            }
        });
        if ("main".equals(from)) {
            //进行定位
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                //判断权限
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },100);

                }else {
                 showMyLocation();
                }

            }else {
         showMyLocation();//进行定位
            }


        } else {

            showAddress();
        }
    }

    private void showMyLocation() {
        btnSearch.setVisibility(View.VISIBLE);
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
        //真正发起定位
        mLocationClient.start();
    }

    /**
     * 在地图上显示百度地图
     */
    private void showAddress() {
        btnSearch.setVisibility(View.INVISIBLE);
        //根据地址查询出经纬度（地理编码查询）
        //（根据经纬度反查具体地址）称为反向地理编码查询
        GeoCoder geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (geoCodeResult == null && geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(FindActivity.this, "服务器繁忙，请稍后重试", Toast.LENGTH_SHORT).show();
                } else {
                    //地址对应的经纬度
                    LatLng location = geoCodeResult.getLocation();
                    //在location所对应的经纬度插上一个标志物
                    MarkerOptions option = new MarkerOptions();
                    option.position(location);
                    option.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_locate));
                    baiduMap.addOverlay(option);
                    //移动屏幕的中心点到location所对应的位置
                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(location);
                    baiduMap.animateMapStatus(msu);
                    //添加一个信息窗
                    TextView tv = new TextView(FindActivity.this);
                    tv.setText(business.getAddress());
                    tv.setPadding(8, 8, 8, 8);
                    tv.setBackgroundColor(Color.GRAY);
                    tv.setTextColor(Color.BLUE);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    InfoWindow infoWindow = new InfoWindow(tv, location, -40);
                    baiduMap.showInfoWindow(infoWindow);

                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                //
            }
        });
        //真正发起地理编号查询
        GeoCodeOption option = new GeoCodeOption();
        option.address(business.getAddress());
        option.city(business.getCity());
        geoCoder.geocode(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient = null;
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double lat = -1;
            double lng = -1;

            int type = bdLocation.getLocType();
            if (type == 61 || type == 65 || type == 66 || type == 161) {
                //定位成功
                lat =  bdLocation.getLatitude();
                lng =  bdLocation.getLongitude();

            } else {
                //定位失败
                //手动指定一个位置 潘家园，建业苑写字楼
                lng =  116.465037;//经度
                lat =  39.876425;//纬度


            }
            //添加标志物，

            LatLng location=new LatLng(lat,lng);
            Myapp.myLocation=location;
            MarkerOptions option=new MarkerOptions();
            option.position(location);
            option.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_locate));
            baiduMap.addOverlay(option);
            //移动屏幕中心点
            MapStatusUpdate msu=MapStatusUpdateFactory.newLatLng(location);
            baiduMap.animateMapStatus(msu);
            //信息窗
            TextView tv = new TextView(FindActivity.this);
            tv.setText("我在这儿");
            tv.setPadding(8, 8, 8, 8);
            tv.setBackgroundColor(Color.RED);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            InfoWindow infoWindow=new InfoWindow(tv,location,-50);
            baiduMap.showInfoWindow(infoWindow);
            //停止定位
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient = null;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100){
            showMyLocation();
        }
    }
    @OnClick(R.id.btn_find_search)
    public void search(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("请选择...");
        builder.setIcon(R.drawable.ic_launcher);
        final String[] items=new String[]{"美食","商城","银行","电影院","厕所"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String item = items[i];
                searchNear(item);

            }


        });
        builder.create().show();
    }
    /**
     * 根据用户的关键字和关键词进行搜索
     * @param item
     */
    private void searchNear(final String item) {
        //兴趣点（poi）搜索
        PoiSearch poiSearch=PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult==null||poiResult.error!= SearchResult.ERRORNO.NO_ERROR){
                    Toast.makeText(FindActivity.this, "您附近没有"+item, Toast.LENGTH_SHORT).show();
                }else {
                    List<PoiInfo> poiInfos = poiResult.getAllPoi();
                    //清除覆盖物
                    baiduMap.clear();
                    MarkerOptions opt=new MarkerOptions();
                    opt.position(Myapp.myLocation);
                    opt.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_locate));
                    baiduMap.addOverlay(opt);
                    for (PoiInfo poi:poiInfos){
                        LatLng location = poi.location;
                        MarkerOptions op=new MarkerOptions();
                        op.position(location);
                        op.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_locate));

                        Marker marker = (Marker) baiduMap.addOverlay(op);
                        Bundle bundle=new Bundle();
                        bundle.putString("name",poi.name);
                        bundle.putString("address",poi.address);
                        bundle.putString("number",poi.phoneNum);

                        marker.setExtraInfo(bundle);
                    }
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });
        PoiNearbySearchOption option=new PoiNearbySearchOption();
        option.location(Myapp.myLocation);
        option.radius(3000);//半径为米
        option.keyword(item);//搜索关键字
        poiSearch.searchNearby(option);
    }
}
