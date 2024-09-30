package com.example.yupromise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.naver.maps.geometry.Tm128;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private double curLat;
    private double curLng;
    private Tm128 firstTm;
    private ArrayList tm128List = new ArrayList<Tm128>();
    private ArrayList markerList = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchplace);
                    //위치 권한이 거부되어 있으면 권한 요청
                    int permissionCehck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionCehck == PackageManager.PERMISSION_DENIED)
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    permissionCehck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (permissionCehck == PackageManager.PERMISSION_DENIED)
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

                    //사용자 현재 위치좌표
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location curPos = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    curLat = curPos.getLatitude();
                    curLng = curPos.getLongitude();

                    //네이버맵 연동
                    FragmentManager fm = getSupportFragmentManager();
                    MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.naver_map);
                    if (mapFragment == null) {
                        mapFragment = MapFragment.newInstance();
                        fm.beginTransaction().add(R.id.naver_map, mapFragment).commit();
                    }
                    mapView = findViewById(R.id.map_view);
                    mapView.onCreate(savedInstanceState);
                    mapView.getMapAsync(this);
                    locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }

                @Override
                public void onMapReady(@NonNull NaverMap naverMap) {
                    this.naverMap = naverMap;
                    naverMap.setLocationSource(locationSource);
                    naverMap.setLocationTrackingMode(LocationTrackingMode.Face);
                    EditText edit = findViewById(R.id.edit);
                    Button searchBtn = findViewById(R.id.searchBtn);
                    Button curPosBtn = findViewById(R.id.curPosBtn);
                    searchBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            String keyword = edit.getText().toString();
                            Thread parseThread = new Thread(() -> {
                                InputStream is = keywordSetting(keyword);
                                parsing(is);
                                parsing(is);
                                parsing(is);
                            });

                for(int i = 0; i < markerList.size(); i++) {
                    Marker marker = (Marker)markerList.get(i);
                    marker.setMap(null);
                }
                markerList.clear();
                tm128List.clear();

                parseThread.start();
                try {
                    parseThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //검색 결과 마커 생성, 카메라 이동 처리
                for(int i = 0; i < tm128List.size(); i++) {
                    markerList.add(new Marker());
                    Marker marker = (Marker) markerList.get(i);
                    Tm128 tm = (Tm128) tm128List.get(i);
                    marker.setPosition(tm.toLatLng());
                    marker.setMap(naverMap);
                    marker.setMap(naverMap);
                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(firstTm.toLatLng());
                    naverMap.moveCamera(cameraUpdate);
                }
            }
        });

        curPosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                naverMap.setLocationSource(locationSource);
                naverMap.setLocationTrackingMode(LocationTrackingMode.Face);
            }
        });
    }

    private InputStream keywordSetting(String keyword) {
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException("encoding fail!", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/local.xml?query="+keyword+"&display=5&start=1&sort=comment";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", "");
        requestHeaders.put("X-Naver-Client-Secret", "");
        InputStream responseBody = get(apiURL, requestHeaders);

        return responseBody;
    }

    //xml 파싱
    private void parsing(InputStream responseBody) {
        boolean inItem = false, inTitle = false, inAddress = false, inMapx = false, inMapy = false;
        String title = null, address = null, mapx = null, mapy = null;

        XmlPullParserFactory factory= null;
        try {
            factory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        XmlPullParser parser = null;
        try {
            parser = factory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        try {
            parser.setInput(responseBody, null);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        int eventType = 0;
        try {
            eventType = parser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        int mapxLat = 0, mapyLng = 0;
        while(eventType != XmlPullParser.END_DOCUMENT) {
            switch(eventType) {
                case XmlPullParser.START_TAG:
                    if(parser.getName().equals("item")) {
                        inItem=true;
                    }
                    if(parser.getName().equals("title")) {
                        inTitle = true;
                    }
                    if(parser.getName().equals("address")) {
                        inAddress = true;
                    }
                    if(parser.getName().equals("mapx")) {
                        inMapx = true;
                    }
                    if(parser.getName().equals("mapy")) {
                        inMapy = true;
                    }
                    if(parser.getName().equals("message")) {

                    }
                    break;
                case XmlPullParser.TEXT:
                    if(inTitle) {
                        title = parser.getText();
                        inTitle= false;
                    }
                    if(inAddress) {
                        address = parser.getText();
                        inAddress = false;
                    }
                    if(inMapx) {
                        mapx = parser.getText();
                        mapxLat = Integer.parseInt(mapx);
                        inMapx = false;
                    }
                    if(inMapy) {
                        mapy = parser.getText();
                        mapyLng = Integer.parseInt(mapy);
                        inMapy = false;
                    }
                    if(mapxLat != 0 && mapyLng != 0) {
                        Tm128 tm = new Tm128(mapxLat, mapyLng);
                        tm128List.add(tm);
                        firstTm = tm;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(parser.getName().equals("item")) {
                        //
                        inItem = false;
                    }
                    break;
            }
            try {
                eventType = parser.next();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }

    private InputStream get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection connection = connect(apiUrl);
        try {
            connection.setRequestMethod("GET");
            for(Map.Entry<String, String> header : requestHeaders.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return connection.getInputStream();
            } else {
                return connection.getErrorStream();
            }
        } catch (IOException e) {
            throw new RuntimeException("API failed.", e);
        } finally {
            connection.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL is not correct.", e);
        } catch(IOException e) {
            throw new RuntimeException("Connection failed." + apiUrl, e);
        }
    }
}