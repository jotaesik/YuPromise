package com.example.last;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Fragment_Ten extends Fragment implements OnBackPressedListener, OnMapReadyCallback {
    private View view;
    private MapView mapView;
    private static NaverMap naverMap;
    private  String [] time_split;
    private int going_time_hour;
    private ArrayList<com.naver.maps.geometry.LatLng> one;
    private String test_input,test_output,test_geo_str;
    private static final String API_KEY = "";
    private String str_url=null;
    private int r_list_len=0;
    ArrayList<com.google.android.gms.maps.model.LatLng> entire_path;
    private int going_time;
    private String going_time1;
    private String getOverview = null;
    private String path1;
    ArrayList<Double> lat_path;
    ArrayList<Double> lng_path;

    private Button promise_friend_list,to_do_list_add;
    private Button promise_delete_button;
    private TextView alarm_text;
    private int promise_exist;
    private Fragment_Twelve1 fragment_twelve1;
    private ArrayList<Test3> test3s;
    private ListView test3_list_view;
    private SpinnerAdapter1 adapter;
    private Spinner spinner1;
    private int choose_promise_number;
    private Test3Adapter test3Adapter;
    private TextView promise_title,time_text;
    private Dialog dialog;
    Fragment_Main_Activity fragment_main_activity;
    long backKeyPressedTime;
    int to_do_list_number=0;
    Toast toast;
    private RadioButton first_button,second_button;
    private RadioGroup radio_group;
    public void directions(String a,String b){

        str_url="";
        String resultText = "값이 없음";
        try {
            resultText = new Task().execute().get(); // URL 연결하는 함수를 호출한 후 반환
            //Toast_Make(String.valueOf(resultText.length()));
            JSONObject jsonObject = new JSONObject(resultText);
            String routes = jsonObject.getString("routes");
            JSONArray routesArray = new JSONArray(routes);
            r_list_len=routesArray.length();
            JSONObject subJsonObject = routesArray.getJSONObject(0);
            String bounds=subJsonObject.getString("bounds");
            String legs = subJsonObject.getString("legs");
            //Toast_Make(legs);
            JSONArray LegArray = new JSONArray(legs);
            JSONObject legJsonObject = LegArray.getJSONObject(0);
            String steps = legJsonObject.getString("steps");
            String duration=legJsonObject.getString("duration");
            JSONObject jsonObject1=new JSONObject(duration);
            String text_duration=jsonObject1.getString("text");
            going_time1=text_duration;
            //going_time1=Integer.parseInt(duration);
            //Toast_Make(text_duration);
            JSONArray stepsArray = new JSONArray(steps);
            r_list_len = stepsArray.length(); // stepsArray의 길이를 list_len 변수에 저장
            JSONObject preferredObject = routesArray.getJSONObject(0);
            String singleRoute = preferredObject.getString("overview_polyline");
            JSONObject pointsObject = new JSONObject(singleRoute);
            String points = pointsObject.getString("points");
            getOverview = points;
            /*for (int i = 0; i < r_list_len; i++) { // 리스트의 길이만큼 반복
                JSONObject stepsObject=stepsArray.getJSONObject(i);
                String end_location=stepsObject.getString("end_location");
                if(i==r_list_len-1){
                }
                else{
                }
            }*/
        } catch(InterruptedException e) { // 예외 처리
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void find_path(String a,String b){
        str_url = "";
        String resultText = "값이 없음";
        going_time=0;
        try {
            resultText = new Task().execute().get(); // URL 연결하는 함수를 호출한 후 반환
            //Toast_Make(resultText);
            JSONObject jsonObject = new JSONObject(resultText);
            String route = jsonObject.getString("route");
            //Toast_Make(route);
            JSONObject jsonObject1=new JSONObject(route);
            String trafast=jsonObject1.getString("trafast");
            //text.setText(trafast);
            JSONArray trafastArray = new JSONArray(trafast);
            JSONObject subJsonObject = trafastArray.getJSONObject(0);
            String summary=subJsonObject.getString("summary");
            //text.setText(summary);
            JSONObject jsonObject2=new JSONObject(summary);
            String duration=jsonObject2.getString("duration");
            //text.setText(duration);
            going_time=Integer.parseInt(duration);
            going_time=going_time/1000/60;
            //Toast_Make(String.valueOf(going_time));
            //text.setText(String.valueOf(time)+"분입니다");

            path1=subJsonObject.getString("path");
            //text.setText(path1);

            one=new ArrayList<>();
            //lat_path=new ArrayList<>();
            //lng_path=new ArrayList<>();
            String path2=path1.substring(1,path1.length()-1);
            //Toast.makeText(getApplication(),String.valueOf(path2.length()),Toast.LENGTH_SHORT).show();
            String test[]=path2.split(",");
            Double test1[]=new Double[test.length];
            //Toast.makeText(getApplication(),String.valueOf(test1.length),Toast.LENGTH_SHORT).show();
            for(int i=0;i< test.length;i++){
                //String temp=text.getText().toString();

                if(i%2==0){

                    String temp1=test[i].substring(1);
                    test1[i]=Double.parseDouble(temp1);
                    //lat_path.add(test1[i]);
                    //text.setText(temp+temp1+" ");
                }
                else{
                    String temp1=test[i].substring(0,test[i].length()-1);
                    test1[i]=Double.parseDouble(temp1);
                    //lng_path.add(test1[i]);
                    //text.setText(temp+temp1+" ");
                }
            }
            for(int i=0;i<test1.length;i=i+2){
                com.naver.maps.geometry.LatLng temp=new com.naver.maps.geometry.LatLng(test1[i+1],test1[i]);
                one.add(temp);
            }
            //Toast.makeText(getContext(),String.valueOf(one.size()),Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplication(),String.valueOf(one.size()),Toast.LENGTH_SHORT).show();
            //Toast_Make(String.valueOf(lng_path.size()));
            /*for(int i=0;i<10;i++){
                Toast_Make(String.valueOf(lng_path.get(i)));
            }*/
        } catch(InterruptedException e) { // 예외 처리
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void geo_find(String a){
        str_url="";
        String resultText="값이 없음";
        //text.setText(str_url);
        try{
            resultText=new Task().execute().get();
            //Toast_Make(resultText);
            //text.setText(resultText);
            JSONObject jsonObject=new JSONObject(resultText);
            String addresses=jsonObject.getString("addresses");
            //text.setText(addresses);
            JSONArray addressesArray = new JSONArray(addresses);
            JSONObject subJsonObject = addressesArray.getJSONObject(0);
            String first=subJsonObject.getString("x");
            test_input=first;
            //text.setText(first);
            String second=subJsonObject.getString("y");
            test_input=test_input+","+second;
            //test_output=second;
            //Toast_Make(first+" "+second);

            //text.setText(second);
            //String latx=jsonObject.getString("x");
            //text.setText(latx);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            toast.show();
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            getActivity().finish();
            fragment_main_activity.onShutDown();
            //getActivity().finish();
            toast.cancel();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        fragment_main_activity.setOnBackPressedListener(this);
    }
    public void set_default_promise(){
        promise_title.setText("약속리스트를 클릭해주세요!!!");
        //약속이 존재한다면 가장 가까운 약속으로 시간표시와 약속친구리스트 알람설정 지도구현해주면 됩니다
        //추가적으로 지도구현해주면될듯?
    }
    public void Toast_Make(String message){
        LayoutInflater inflater=getLayoutInflater();    //레이아웃 인플레이터 객체 참조 뷰 사용하기위함
        View layout=inflater.inflate(R.layout.toast_design_layout,(ViewGroup) getActivity().findViewById(R.id.toast_design_layout_main));
        //토스트를위한 레이아웃 인플레이트
        Toast toast=Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
        TextView textView=layout.findViewById(R.id.toast_textview);

        textView.setText(message);
        toast.setGravity(Gravity.BOTTOM,0,500);
        toast.setView(layout);  //토스트 뷰 설정
        toast.show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_ten,container,false);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        mapView=(MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        first_button=(RadioButton)view.findViewById(R.id.first_button);
        second_button=(RadioButton)view.findViewById(R.id.second_button);
        first_button.setClickable(false);
        second_button.setClickable(false);
        radio_group=(RadioGroup)view.findViewById(R.id.radio_group);
        radio_group.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        promise_friend_list=(Button)view.findViewById(R.id.promise_friend_list);
        time_text=(TextView)view.findViewById(R.id.time_text);
        //time_text1=(TextView)view.findViewById(R.id.time_text1);
        alarm_text=(TextView)view.findViewById(R.id.alarm_text);
        to_do_list_add=(Button)view.findViewById(R.id.to_do_list_add);
        test3s=new ArrayList<>();
        test3_list_view=(ListView)view.findViewById(R.id.todo_listview);
        test3Adapter=new Test3Adapter(getContext(),test3s);
        test3_list_view.setAdapter(test3Adapter);
        promise_delete_button=(Button)view.findViewById(R.id.promise_delete_button);
        promise_exist=Global.getInstance().getPromise_exist();
        promise_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_layout4);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button yes_button=dialog.findViewById(R.id.yes_button);
                Button no_button=dialog.findViewById(R.id.no_button);
                yes_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //뭔가 약속의 개수를 담당해주는 것이 잇어야할듯 그래야 취소를 눌럿을때 남아있는 약속의 개수가 0이면
                        //하나도 없는 약속메인으로가게디고 약속이 남아잇으면 약속이 있는 메인화면으로가니
                        //해결
                        promise_exist--;
                        //Toast.makeText(getContext(),String.valueOf(promise_exist),Toast.LENGTH_SHORT).show();
                        Global.getInstance().setPromise_exist(promise_exist);
                        if(0==Global.getInstance().getPromise_exist()){ //약속이 없으면 8번화면
                            Fragment_Eight fragment_eight=new Fragment_Eight();
                            transaction.replace(R.id.fragment_main, fragment_eight);
                        }
                        else{
                            Fragment_Ten fragment_ten=new Fragment_Ten();
                            transaction.replace(R.id.fragment_main, fragment_ten);
                        }
                        transaction.commit();
                        //transaction.replace(R.id.fragment_main,fragment_fifthteen);
                        //transaction.commit();
                        //Toast.makeText(getContext(),"성공",Toast.LENGTH_SHORT).show();
                    }
                });
                no_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        /*for(int i=0;i<10;i++){
            test3s.add(new Test3(i+"happy"));
        }*/

        //알람스피너
        List<Spinner1> spinnerList = new ArrayList<>();
        String []when_alarm=new String[]{"알람설정","하루전","12시간전","6시간전","2시간전","1시간전","30분전","15분전","10분전","5분전"};
        for(int i=0;i<9;i++){
            Spinner1 test=new Spinner1();
            test.setName(when_alarm[i]);
            spinnerList.add(test);
        }
        spinner1=(Spinner) view.findViewById(R.id.alarm_timer);
        adapter=new SpinnerAdapter1(getActivity(),spinnerList);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    alarm_text.setText("알람이 설정되지 않았습니다");
                }
                else{
                    alarm_text.setText(when_alarm[position]+"으로 알람이 설정되었습니다");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                alarm_text.setText("알람이 설정되지 않았습니다");
            }
        });
        long a=spinner1.getSelectedItemPosition();
        String text=String.valueOf(a);  //알림설정 값 저장
        //Toast_Make(text);
        /*if(a==0){
            alarm_text.setText("알람이 설정되지 않았습니다");
        }
        else{
            alarm_text.setText(text+"으로 알람이 설정되었습니다");
        }*/

        to_do_list_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //과거에 해야할일 추가해놧으면 불러오기
                //EditText todo_title=(EditText)view.findViewById(R.id.todo_title);
                to_do_list_number++;
                test3s.add(new Test3());
                test3_list_view.setAdapter(test3Adapter);
            }
        });
        promise_friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //수정할부분
                //약속할친구로 선택한 친구들 불러오기 db에서
                dialog=new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_layout3);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button close_button=(Button)dialog.findViewById(R.id.close_button);
                close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ListView listView7=dialog.findViewById(R.id.listview7);
                ArrayList<Test7> test7s=new ArrayList<>();
                Test7Adapter test6Adapter=new Test7Adapter(getContext(),test7s);
                listView7.setAdapter(test6Adapter);
                int count=0;
                for(int i=0;i<100;i++){
                    test7s.add(new Test7("태식이"));
                }
            }
        });
        toast=Toast.makeText(getContext(),"한번더 뒤로가기 누르면 종료",Toast.LENGTH_SHORT);
        //other_promise=(Button)view.findViewById(R.id.other_promise);
        promise_title=(TextView)view.findViewById(R.id.promise_title);
        //default값 정해줘야할듯? 약속가장가까운순으로
        set_default_promise();
        promise_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog=new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_layout2);
                //dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                ListView listView6=dialog.findViewById(R.id.listview6);
                ArrayList<Test6> test6s=new ArrayList<>();
                Test6Adapter test6Adapter=new Test6Adapter(getContext(),test6s);
                listView6.setAdapter(test6Adapter);
                int count=0;
                for(int i=0;i<Global.getInstance().getPromise_exist();i++){
                    //약속한 개수에 따른 각각의 번호와 약속위치 그리고 함께할 친구리스트 추가
                    test6s.add(new Test6(String.valueOf(++count)+"번째","약속장소위치","함께할 친구들"));
                }
                listView6.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        first_button.setClickable(true);
                        second_button.setClickable(true);
                        //Toast_Make(String.valueOf(position)+"출력"+test6s.get(position).getPromise_where());
                        String just0=test6s.get(position).getPromise_number();
                        String just1=test6s.get(position).getPromise_where();
                        //choose_promise_number=Integer.parseInt(just0);
                        promise_title.setText("약속장소는 "+just1+"입니다");
                        geo_find("");    //여기에 약속장소를 넣어서 test_input결정
                        //geo_find(just1);
                        //geo_find에서 저장된 first와 second는 약속장소 위치이므로
                        //test_input="";
               
                        test_output="";
                        find_path(test_input,test_output);
                        directions("","");
                        PolylineOptions polylineOptions=new PolylineOptions();
                        if (getOverview != null) {
                            entire_path = decodePolyPoints(getOverview);
                        }
                        going_time_hour=going_time/60;
                        //setNaverMap1(naverMap);
                        going_time=going_time-(going_time_hour*60);
                        //약속장소에 따른 유저의 위치로 시간계산해서 보여주기
                        time_split=going_time1.split("\\s");
                        //0번째와 2번째
                        /*time_text.setText("자가용으론 "+String.valueOf(going_time_hour)+"시간 " +
                                        String.valueOf(going_time)+"분 걸립니다");*/
                        /*time_text1.setText("대중교통으론 "+time_split[0]+"시간 "+time_split[2]+"분"+" 걸립니다");*/
                        //setNaverMap(naverMap);

                        //time_text.setText("가는데에 얼마가 걸릴까요??");
                        //Toast_Make(just0+" "+just1);
                        //Toast_Make("eror?");
                        dialog.dismiss();
                    }
                });

            }
        });
        return view;
    }
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.first_button){
                time_text.setText("자가용으론 "+String.valueOf(going_time_hour)+"시간 " +
                        String.valueOf(going_time)+"분 걸립니다");
                setNaverMap(naverMap);
            }
            else if(i == R.id.second_button){
                //Toast_Make(going_time1);

                time_text.setText("대중교통으론 "+time_split[0]+"시간 "+time_split[2]+"분"+" 걸립니다");
                setNaverMap1(naverMap);
            }
        }
    };
    PathOverlay path=new PathOverlay();
    PathOverlay path0=new PathOverlay();
    public void setNaverMap(NaverMap map){
        naverMap=map;
        path0.setMap(null);
        path=new PathOverlay();
        path.setCoords(one);
        path.setMap(naverMap);

    }
    public void setNaverMap1(NaverMap map){
        naverMap=map;
        path.setMap(null);
        path0=new PathOverlay();
        ArrayList<com.naver.maps.geometry.LatLng>test=new ArrayList<>();
        for(int i=0;i<entire_path.size();i++){
            String str=String.valueOf(entire_path.get(i));
            //Toast.makeText(getApplication(),String.valueOf(entire_path.get(i).latitude),Toast.LENGTH_LONG).show();
            com.naver.maps.geometry.LatLng a=new com.naver.maps.geometry.LatLng(
                    entire_path.get(i).latitude,
                    entire_path.get(i).longitude);
            test.add(a);
        }
        path0.setCoords(test);
        path0.setColor(Color.RED);
        path0.setMap(naverMap);
    }
    public class Task extends AsyncTask<String, Void, String> {
        private String str, receiveMsg;
        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(str_url); // str_url로 연결

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();


                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString(); // JSON 파일을 String으로 바꿔 receiveMsg 변수에 저장
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
    public static ArrayList<com.google.android.gms.maps.model.LatLng> decodePolyPoints(String encodedPath) {
        int len = encodedPath.length();
        final ArrayList<com.google.android.gms.maps.model.LatLng> path = new ArrayList<com.google.android.gms.maps.model.LatLng>();
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

            path.add(new com.google.android.gms.maps.model.LatLng(lat * 1e-5, lng * 1e-5));
        }
        return path;
    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap=naverMap;
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(Global.getInstance().getLatitude(), Global.getInstance().getLongitude()),  // 위치 지정
                12                           // 줌 레벨
        );
        naverMap.setCameraPosition(cameraPosition);
        Marker marker=new Marker();
        marker.setPosition(new LatLng(Global.getInstance().getLatitude(), Global.getInstance().getLongitude()));
        marker.setMap(naverMap);
        /*PathOverlay path=new PathOverlay();
        ArrayList<com.naver.maps.geometry.LatLng>test=new ArrayList<>();
        for(int i=0;i<entire_path.size();i++){
            String str=String.valueOf(entire_path.get(i));
            //Toast.makeText(getApplication(),String.valueOf(entire_path.get(i).latitude),Toast.LENGTH_LONG).show();
            com.naver.maps.geometry.LatLng a=new com.naver.maps.geometry.LatLng(
                    entire_path.get(i).latitude,
                    entire_path.get(i).longitude);
            test.add(a);
        }
        path.setCoords(test);
        path.setColor(Color.RED);
        path.setMap(naverMap);*/
    }

    class Test7Adapter extends ArrayAdapter implements AdapterView.OnItemClickListener {

        private Context context;
        private List list;

        public Test7Adapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
        }

        class ViewHolder{
            public TextView promise_friend_nickname;
            public ImageView choose_image;
        }

        public Test7Adapter(Context context, ArrayList list){
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.item_promise_friend_list, parent, false);
            }
            viewHolder = new ViewHolder();
            viewHolder.promise_friend_nickname = (TextView) convertView.findViewById(R.id.promise_friend_nickname);
            viewHolder.choose_image=(ImageView) convertView.findViewById(R.id.choose_image);
            final Test7 test7 = (Test7) list.get(position);
            viewHolder.promise_friend_nickname.setText(test7.getPromise_friend_nickname());
            //친구가 오기로 했으면 check로 친구가 오지않기록했으면 check_box로 구현해야함
            if((position%2)==1){
                viewHolder.choose_image.setImageResource(R.drawable.check);
            }
            else{
                viewHolder.choose_image.setImageResource(R.drawable.check_box);
            }
            return convertView;
        }

    }
    class Test6Adapter extends ArrayAdapter implements AdapterView.OnItemClickListener {

        private Context context;
        private List list;

        public Test6Adapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
        }

        class ViewHolder{
            public TextView promise_number,promise_where,promise_friend;
        }

        public Test6Adapter(Context context, ArrayList list){
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.item_promise_list, parent, false);
            }
            viewHolder = new ViewHolder();
            viewHolder.promise_number = (TextView) convertView.findViewById(R.id.promise_number);
            viewHolder.promise_where=(TextView) convertView.findViewById(R.id.promise_where);
            viewHolder.promise_friend=(TextView) convertView.findViewById(R.id.promise_friend);
            //viewHolder.button.setOnClickListener(onButtonChooseClicked);

            final Test6 test6 = (Test6) list.get(position);
            viewHolder.promise_friend.setText(test6.getPromise_friend());
            viewHolder.promise_where.setText(test6.getPromise_where());
            viewHolder.promise_number.setText(test6.getPromise_number());
            return convertView;
        }

    }
    class Test3Adapter extends ArrayAdapter implements AdapterView.OnItemClickListener {

        private Context context;
        private List list;
        public ArrayList<Test3> listview=new ArrayList<Test3>();
        public Test3Adapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
        }

        class ViewHolder{
            public EditText todo_title;
            public Button todo_delete_button;

        }

        public Test3Adapter(Context context, ArrayList list){
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.item_todo, parent, false);
            }
            viewHolder = new Test3Adapter.ViewHolder();
            viewHolder.todo_title = (EditText) convertView.findViewById(R.id.todo_title);
            viewHolder.todo_delete_button = (Button) convertView.findViewById(R.id.todo_delete_button);
            //viewHolder.button.setOnClickListener(onButtonChooseClicked);

            final Test3 test3 = (Test3) list.get(position);
            //viewHolder.todo_title.setText(test3.getFirst());
            //viewHolder.todo_title.setText(test3.getFirst());
            viewHolder.todo_delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    test3_list_view.setAdapter(test3Adapter);
                    //Bundle bundle=new Bundle();
                    //bundle.putString("we_where",test1.getFirst());
                    //bundle.putString("we_where1", test1.getSecond());
                    //FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    //fragment_fourteen1=new Fragment_FourTeen1();
                    //fragment_fourteen1.setArguments(bundle);
                    //transaction.replace(R.id.fragment_main, fragment_fourteen1);
                    //transaction.commit();
                }
            });
            //final EditText todo_title=(EditText) convertView.findViewById(R.id.todo_title);
            viewHolder.todo_title.setText(test3.getTodo_title());
            viewHolder.todo_title.addTextChangedListener(test3.textWatcher);
            return convertView;
        }



    }
     public class Test3 {
        public String todo_title;
        public TextWatcher textWatcher;
        public Test3() {
            //this.first=first;
            todo_title="";
            textWatcher=new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                   todo_title=s.toString();
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            };
        }
        public String getTodo_title() {
            return todo_title;
        }



    }
    class Test6{
        private String promise_number;
        private String promise_where;
        private String promise_friend;

        public Test6(String promise_number,String promise_where,String promise_friend){
            this.promise_number=promise_number;
            this.promise_where=promise_where;
            this.promise_friend=promise_friend;
        }
        public String getPromise_friend(){
            return promise_friend;
        }
        public String getPromise_where(){
            return promise_where;
        }
        public String getPromise_number(){
            return promise_number;
        }
    }
    class Test7{
        private String promise_friend_nickname;
        public Test7(String promise_friend_nickname){
            this.promise_friend_nickname=promise_friend_nickname;
        }
        public String getPromise_friend_nickname(){
            return promise_friend_nickname;
        }

    }
}
