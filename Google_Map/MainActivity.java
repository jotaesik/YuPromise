package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.PathOverlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity  implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MapView mapView;
    LatLng SEOUL = new LatLng(37.56, 126.97);
    private String option=null;
    private TextView text;
    private String input,output;
    private static final String API_KEY = "";
    private String str_url = null; // EditText의 값과 원래의 URL을 합쳐 검색 URL을 만들어 저장
    private String entire_step = null; // 여러 개의 단계를 하나의 단계로 합쳐 저장하기 위한 변수
    private int r_list_len = 0; // 배열의 동적 생성을 위한 변수
    private int []list_len=null;
    ArrayList<LatLng> entire_path;

    private String []full_time;
    private String[] hours;
    private String[] min;
    private String departure_lat = null;
    private String departure_lng = null;
    private String[][] goingS_lat;
    private String[][] goingS_lng;
    private String[][] goingE_lat;
    private String[][] goingE_lng;
    private String arrival_lat = null;
    private String arrival_lng = null;
    private String[][] TransitName;
    private String[][] getPolyline;
    private String[][] getInstructions;
    private String[][] step = null;
    private String getOverview = null;
    private String REQUEST_DEP = null;
    private String REQUEST_ARR = null;



    public class Task extends AsyncTask<String, Void, String> {

        private String str, receiveMsg;

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(str_url); // str_url로 연결

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //url.openConnection()을 이용해서, HTTP Connection을 열고 호출

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString(); // JSON 파일을 String으로 바꿔 receiveMsg 변수에 저장
                    //Toast.makeText(getApplication(),"성공",Toast.LENGTH_SHORT).show();
                    reader.close();
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            } catch (MalformedURLException e) { // 예외 처리
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }
    public void directions(String a,String b){

        str_url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + a + "&destination=" + b + "&mode=transit&departure_time=now&alternatives=true&language=Korean&key=" + API_KEY;
        String resultText = "값이 없음";
        try {
            resultText = new Task().execute().get(); // URL 연결하는 함수를 호출한 후 반환
            //text.setText(resultText);
            //Toast.makeText(getApplication(),resultText,Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = new JSONObject(resultText);
            String routes = jsonObject.getString("routes");
            JSONArray routesArray = new JSONArray(routes);
            //text.setText(routes);
            r_list_len=routesArray.length();
            list_len=new int[r_list_len];


            JSONObject subJsonObject = routesArray.getJSONObject(0);
            String bounds=subJsonObject.getString("bounds");
            //text.setText(bounds);
            String legs = subJsonObject.getString("legs");
            JSONArray LegArray = new JSONArray(legs);
            JSONObject legJsonObject = LegArray.getJSONObject(0);
            //text.setText(legs);
            String steps = legJsonObject.getString("steps");
            //text.setText(steps);
            String duration=legJsonObject.getString("duration");
            text.setText(duration+"\n"+input); //정답
            JSONArray stepsArray = new JSONArray(steps);
            r_list_len = stepsArray.length(); // stepsArray의 길이를 list_len 변수에 저장
            //Toast.makeText(getApplication(),String.valueOf(stepsArray.length()),Toast.LENGTH_SHORT).show();
            full_time=new String[r_list_len];
            hours = new String[r_list_len];
            min = new String[r_list_len];

            goingS_lat = new String[r_list_len][20]; // route의 개수만큼 그리고 그 안에 자잘한 route들을 최대 20으로 배열을 생성
            goingS_lng = new String[r_list_len][20];
            goingE_lat = new String[r_list_len][20];
            goingE_lng = new String[r_list_len][20];
            getPolyline = new String[r_list_len][20];
            getInstructions = new String[r_list_len][];
            TransitName = new String[r_list_len][20];
            step = new String[r_list_len][];

            String[] getInstructions = new String[r_list_len];
            String[] arrival_name = new String[r_list_len];
            String[] depart_name = new String[r_list_len];
            String[] getHeadsign = new String[r_list_len];
            String[] getBusNo = new String[r_list_len];
            JSONObject preferredObject = routesArray.getJSONObject(0);
            String singleRoute = preferredObject.getString("overview_polyline");
            JSONObject pointsObject = new JSONObject(singleRoute);
            String points = pointsObject.getString("points");
            getOverview = points;
            for (int i = 0; i < r_list_len; i++) { // 리스트의 길이만큼 반복
                JSONObject stepsObject=stepsArray.getJSONObject(i);
                String end_location=stepsObject.getString("end_location");

                String temp=text.getText().toString();
                text.setText(temp+"\n"+end_location);
                if(i==r_list_len-1){

                }
                else{
                    //String start_location=stepsObject.getString("start_location");
                    //temp=text.getText().toString();
                    //text.setText(temp+"\n"+start_location);
                }



                //Toast.makeText(getApplication(),end_location,Toast.LENGTH_SHORT).show();
                /*JSONObject stepsObject = stepsArray.getJSONObject(i);
                getInstructions[i] = stepsObject.getString("html_instructions");
                String[] Check = getInstructions[i].split(" ");
                String TransitCheck = Check[0];
                if (TransitCheck.equals("Bus") || TransitCheck.equals("Subway")
                        || TransitCheck.equals("train") || TransitCheck.equals("rail")
                        || TransitCheck.equals("버스") || TransitCheck.equals("지하철")) {

                    String transit_details = stepsObject.getString("transit_details");
                    JSONObject transitObject = new JSONObject(transit_details);

                    String arrival_stop = transitObject.getString("arrival_stop");
                    JSONObject arrivalObject = new JSONObject(arrival_stop);
                    arrival_name[i] = arrivalObject.getString("name");

                    String depart_stop = transitObject.getString("departure_stop");
                    JSONObject departObject = new JSONObject(depart_stop);
                    depart_name[i] = departObject.getString("name");

                    getHeadsign[i] = transitObject.getString("headsign");

                    String line = transitObject.getString("line");
                    JSONObject lineObject = new JSONObject(line);
                    getBusNo[i] = lineObject.getString("short_name");
                Toast.makeText(getApplication(),String.valueOf(arrival_name[i]),Toast.LENGTH_SHORT).show();
                }*/




            }



        } catch(InterruptedException e) { // 예외 처리
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        text=(TextView)findViewById(R.id.text);
        input="35.8301,128.7610";
        output= "35.855148,128.559874";
        directions(input,output);
        //mMap.clear();
        PolylineOptions polylineOptions=new PolylineOptions();
        if (getOverview != null) {
            //Toast.makeText(getApplication(),getOverview,Toast.LENGTH_SHORT).show();
            //text.setText(getOverview+"\n");
            entire_path = decodePolyPoints(getOverview);
            for (int i = 0; i < entire_path.size(); i++) {
                //String temp=text.getText().toString();
                //text.setText(temp+String.valueOf(entire_path.get(i))+"\n");
            }
        }
        //mMap.addPolyline(polylineOptions);




    }
    public static ArrayList<LatLng> decodePolyPoints(String encodedPath) {
        int len = encodedPath.length();

        final ArrayList<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }
    //@Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        // LatLng SEOUL = new LatLng(37.56, 126.97)

        /*MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        mMap.addMarker(markerOptions);
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.4,126.9)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);*/
        /*for (int idx = 0; idx < 10; idx++) {
            // 1. 마커 옵션 설정 (만드는 과정)
            double a=0.01;
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                    .position(new LatLng(37.52487-idx*a, 126.92723))
                    .title("마커" + idx); // 타이틀.

            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions);
        }*/
        Polyline line=null;
        if (line == null) {
            line = mMap.addPolyline(new PolylineOptions()
                    .color(Color.rgb(0, 153, 255))
                    .geodesic(true)
                    .addAll(entire_path));
        }
        // 카메라를 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(35.8301,128.7610)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new com.naver.maps.geometry.LatLng(35.8301,128.7610));
        naverMap.moveCamera(cameraUpdate);
        PathOverlay path=new PathOverlay();
        ArrayList<com.naver.maps.geometry.LatLng>test=new ArrayList<>();
        for(int i=0;i<entire_path.size();i++){
            String str=String.valueOf(entire_path.get(i));
            Toast.makeText(getApplication(),String.valueOf(entire_path.get(i).latitude),Toast.LENGTH_LO	NG).show();

            com.naver.maps.geometry.LatLng a=new com.naver.maps.geometry.LatLng(
                    entire_path.get(i).latitude,
                    entire_path.get(i).longitude);
            test.add(a);
            //com.naver.maps.geometry.LatLng=new com.naver.maps.geometry.LatLng(35,136);
        }
        /*path.setCoords(Arrays.asList(
                new com.naver.maps.geometry.LatLng(37.57152,126.977),
                new com.naver.maps.geometry.LatLng(37.5,126.977),
                new com.naver.maps.geometry.LatLng(37.5,126.977),
                new com.naver.maps.geometry.LatLng(37.5,126.977)));*/
        path.setCoords(test);
        path.setColor(Color.RED);
        path.setMap(naverMap);


    }
}