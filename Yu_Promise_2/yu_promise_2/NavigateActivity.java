package com.example.yupromise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NavigateActivity extends AppCompatActivity implements OnMapReadyCallback {
    private NaverMap naverMap;
    private MapView mapView;
    Location curPos = null;
    double curLat = 0.0, curLng = 0.0;
    double goalLat = 0.0, goalLng = 0.0, startLat = 0.0, startLng = 0.0;
    LocationManager locationManager = null;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    List<Address> goalAddrList = null;
    List<Address> startAddrList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

        //네이버맵 연동
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.naver_map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.naver_map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        //위치 권한이 거부되어 있으면 권한 요청
        int permissionCehck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCehck == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        permissionCehck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCehck == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        curPos = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Face);
        //naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true);

        String APIKEY_ID = ""; //네이버api key id
        String APIKEY = ""; //네이버 api key
        Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
        NaverApi api = retrofit.create(NaverApi.class);
        final Geocoder geocoder = new Geocoder(this.getApplicationContext());
        EditText startAddr = findViewById(R.id.startEddr);
        EditText goalAddr = findViewById(R.id.goalEddr);
        Button navigateBtn = findViewById(R.id.navigateBtn);
        navigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //출발지 좌표 지정 - 사용자 현재 위치
                try{
                    String startAddrString = startAddr.getText().toString();
                    startAddrList = geocoder.getFromLocationName(startAddrString, 10);
                    if(startAddrList.size() == 0) {
                        castToastMsg("주소가 올바르지 않습니다.");
                    } else {
                        Address addr = startAddrList.get(0);
                        startLat = addr.getLatitude();
                        startLng = addr.getLongitude();
                        Log.d("로그", "1. start addr : " + startLat + " , " + startLng);
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }

                //도착지 좌표 지정
                try {
                    String goalAddrString = goalAddr.getText().toString();
                    goalAddrList = geocoder.getFromLocationName(goalAddrString, 10);
                    if(goalAddrList.size() == 0) {
                        castToastMsg("주소가 올바르지 않습니다.");
                    } else {
                        Address addr = goalAddrList.get(0);
                        goalLat = addr.getLatitude();
                        goalLng = addr.getLongitude();
                        Log.d("로그", "2. goal addr : " + goalLat + " , " + goalLng);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //출발 지점(start), 도착 지점(goal) 좌표 입력 - String
                //두 지점 거리는 3km 이내
                String startLatlngString = Double.toString(startLat) + ", " + Double.toString(startLng);
                String goalLatlngString = Double.toString(goalLat) + ", " + Double.toString(goalLng);
                Log.d("로그", "start : " + startLatlngString + " , " + "goal : " + goalLatlngString);

                Call<ResultPath> callgetPath = api.getPath(APIKEY_ID, APIKEY, startLatlngString, goalLatlngString);
                //길찾기
                Callback<ResultPath> object = new Callback<ResultPath>() {
                    @Override
                    public void onResponse(Call<ResultPath> call, Response<ResultPath> response) {
                        try {
                            ArrayList<ResultPathData> pathCordsList = response.body().route.traoptimal;

                            PathOverlay path = new PathOverlay();
                            ArrayList pathContainer = new ArrayList<LatLng>();

                            for (int i = 0; i < pathCordsList.size(); i++) {
                                for (int j = 0; j < pathCordsList.get(i).path.size(); j++) {
                                    Double x = pathCordsList.get(i).path.get(j).get(1);
                                    Double y = pathCordsList.get(i).path.get(j).get(0);
                                    LatLng coords = new LatLng(x, y);
                                    pathContainer.add(coords);
                                }
                            }

                            path.setCoords(pathContainer);
                            path.setColor(Color.RED);
                            path.setMap(naverMap);

                            CameraUpdate camera = CameraUpdate.scrollTo(path.getCoords().get(0));
                            naverMap.moveCamera(camera);

                            //출발 지점 마커
                            Marker startMarker = new Marker();
                            startMarker.setPosition(path.getCoords().get(0));
                            startMarker.setMap(naverMap);

                            //도착 지점 마커
                            Marker goalMarker = new Marker();
                            goalMarker.setPosition(path.getCoords().get(path.getCoords().size() - 1));
                            goalMarker.setMap(naverMap);

    } catch(NullPointerException e) {
        castToastMsg(response.body().message);
    }
}

    @Override
    public void onFailure(Call<ResultPath> call, Throwable t) {

    }

};
                callgetPath.enqueue(object);
                        }
                        });
                        }

private void castToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}