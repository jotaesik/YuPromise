package com.example.last;

import android.content.Context;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Fragment_SevenTeen extends Fragment implements OnBackPressedListener {
    private View view;
    private Button remake_button1,logout_button;
    private Fragment_EightTeen fragment_eightteen;
    private Fragment_NineTeen fragment_nineteen;
    private Switch sw,sw1;
    private TextView now_temp,now_sky,now_up,now_down;
    private String str_url = null; // EditText의 값과 원래의 URL을 합쳐 검색 URL을 만들어 저장
    private TextView alarm_switch_on,alarm_switch_off;
    private TextView jindong_switch_on,jindong_switch_off;
    Fragment_Main_Activity fragment_main_activity;
    private int just;
    private ImageView my_image;
    private TextView my_nickname,my_id_number;
    long backKeyPressedTime;
    String newdate;
    Toast toast;
    private int time_index;
    private double lon=Global.getInstance().getLongitude();
    private double lat=Global.getInstance().getLatitude();
    private String temp_array[]=new String[9];
    private String time_array[]=new String[9];
    private double temp_array1[]=new double[9];
    private String sky_array[]=new String[9];
    private double high_temp,low_temp;
    private TextView dress_text;
    private ImageView dress_image,weather_iamge;
    int promise_exist;
    private Context mContext;
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

    public void set_nickname_id_number(){   //여기에 이제 유저의 이미지와 닉네임 전화번호 받아오기
        my_nickname.setText(Global.getInstance().getUser_nickname());
        my_id_number.setText(Global.getInstance().getUser_id() + " / " + Global.getInstance().getUser_phoneNo());
        FirebaseStorage.getInstance().getReference().child("profile_picture/" + Global.getInstance().getUser_id() +".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Fragment_SevenTeen.this).load(uri).into(my_image);
            }
        });
    }
    public void set_switch_default(){
        alarm_switch_on.setTextColor(Color.parseColor("#6667AB"));
        jindong_switch_on.setTextColor(Color.parseColor("#6667AB"));
        Global.getInstance().setAlarm_switch_value(0);
        Global.getInstance().setJindong_switch_value(0);
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
    public void weather_find(){
        str_url="";
        String resultText="값이 없음";
        try{
            /*
            main,temp 현재온도
            main,humidity 현재습도
            weather,main 날씨
            time 시간
             */
            resultText=new Task().execute().get();
            JSONObject jsonObject=new JSONObject(resultText);
            String list=jsonObject.getString("list");
            JSONArray listArray = new JSONArray(list);

            for(int i=0;i<9;i++){
                //String left=text.getText().toString();
                JSONObject subJsonObject=listArray.getJSONObject(i);
                String main=subJsonObject.getString("main");
                JSONObject jsonObject2=new JSONObject(main);
                String temp=jsonObject2.getString("temp");
                temp_array[i]=temp;
                temp_array1[i]=Double.parseDouble(temp);
                String humidity=jsonObject2.getString("humidity");
                String weather=subJsonObject.getString("weather");
                JSONArray weatherArray=new JSONArray(weather);
                JSONObject subJsonObject3=weatherArray.getJSONObject(0);
                String main1=subJsonObject3.getString("main");
                sky_array[i]=main1;
                String time=subJsonObject.getString("dt_txt");
                time_array[i]=time.substring(11,13);
                //text.setText(left+"  "+temp+"  "+humidity+"  "+main1+"  "+time+"\n");
            }
            high_temp=temp_array1[0];
            for(double num:temp_array1){
                if(num>high_temp){
                    high_temp=num;
                }
            }
            low_temp=temp_array1[0];
            for(double num:temp_array1){
                if(num<low_temp){
                   low_temp=num;
                }
            }


        } catch (Exception e){
            e.printStackTrace();
        }
        //여기에 디자인수정
        /*for(int i=0;i<9;i++){
            temp_array1[i]=Integer.parseInt(temp_array[i])-273;
            //Toast_Make(time_array[i]);
        }*/
        double min=100;
        int answer_time=-1;
        for(int i=0;i<9;i++){
            double abs=Math.abs(Integer.parseInt(time_array[i])-Integer.parseInt(Global.getInstance().getNow_hour()));
            if(abs<min){
                min=abs;
                answer_time=i;
            }
        }
        time_index=answer_time;
        //now_temp.setText(String.valueOf(temp_array[answer_time]));
    }
    public void now_sky_set(String a){
        if(a.equals("Rain")){
            now_sky.setText("비 내림");
            weather_iamge.setImageResource(R.drawable.rainrain);
        }
        else if(a.equals("Clouds")){
            now_sky.setText("구름 많음");
            weather_iamge.setImageResource(R.drawable.cloudycloudy);
        }
        else{
            now_sky.setText("맑음");
            weather_iamge.setImageResource(R.drawable.sunsun);
        }
    }
    public void dress_set(int a){
        //dress_text.setText("fuck");
        if(a>=28){
            dress_text.setText("민소매,반팔,반바지,짧은 치마,린넨 옷");
            dress_image.setImageResource(R.drawable.bear3);
        }else if(a>=23 && a<=27){
            dress_text.setText("반팔,얇은 셔츠,반바지,면바지");
            dress_image.setImageResource(R.drawable.bear);
        }else if(a>=20 && a<=22){
            dress_text.setText("블라우스,긴팔 티,면바지,슬랙스");
            dress_image.setImageResource(R.drawable.bear2);
        }else if(a>=17 && a<=19) {
            dress_text.setText("얇은 가디건,니트,맨투맨,후드,긴 바지");
            dress_image.setImageResource(R.drawable.bear4);
        }else if(a>=12 && a<=16){
            dress_text.setText("자켓,가디건,청자켓,니트,스타킹,청바지");
            dress_image.setImageResource(R.drawable.bear5);
        }else if(a>=9 && a<=11){
            dress_text.setText("트렌치 코트,야상,점퍼,스타킹,기모바지");
            dress_image.setImageResource(R.drawable.bear6);
        }else if(a>=5 && a<=8){
            dress_text.setText("울 코트,히트텍,가죽 옷,기모");
            dress_image.setImageResource(R.drawable.bear7);
        }else {
            dress_text.setText("패딩,두꺼운 코트,누빔 옷,기모,목도리");
            dress_image.setImageResource(R.drawable.bear1);
        }

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_seventeen,container,false);
        weather_find();
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        toast=Toast.makeText(getContext(),"한번더 뒤로가기 누르면 종료",Toast.LENGTH_SHORT);
        now_temp=(TextView)view.findViewById(R.id.now_temp);
        //Toast_Make(time_array[time_index]+Global.getInstance().getNow_hour());
        just= (int) (temp_array1[time_index]-273);
        now_temp.setText(String.valueOf(just)+"도");
        now_sky=(TextView)view.findViewById(R.id.now_sky);
        //Rain Clouds Clear
        weather_iamge=(ImageView)view.findViewById(R.id.weather_image);
        now_sky_set(sky_array[time_index]);
        now_up=(TextView)view.findViewById(R.id.now_up);
        now_down=(TextView)view.findViewById(R.id.now_down);
        now_up.setText(String.valueOf((int)high_temp-273)+"도");
        now_up.setTextColor(Color.RED);
        now_down.setText(String.valueOf((int)low_temp-273)+"도");
        now_down.setTextColor(Color.BLUE);
        dress_text=(TextView)view.findViewById(R.id.dress_text);
        dress_image=(ImageView)view.findViewById(R.id.dress_image);
        dress_set(just);

        remake_button1=(Button) view.findViewById(R.id.remake_button1);
        logout_button=(Button)view.findViewById(R.id.logout_button);
        sw=(Switch)view.findViewById(R.id.sw);
        sw1=(Switch)view.findViewById(R.id.sw1);
        alarm_switch_off=(TextView)view.findViewById(R.id.alarm_switch_off);
        alarm_switch_on=(TextView)view.findViewById(R.id.alarm_switch_on);
        jindong_switch_on=(TextView)view.findViewById(R.id.jindong_switch_on);
        jindong_switch_off=(TextView)view.findViewById(R.id.jindong_switch_off);
        my_image=(ImageView)view.findViewById(R.id.my_image);
        my_nickname=(TextView)view.findViewById(R.id.my_nickname);
        my_id_number=(TextView)view.findViewById(R.id.my_id_number);
        set_nickname_id_number();   //데이터에있는 이미지와 닉네임 아이디 전화번호 해주기
        set_switch_default();//스위치 디폴드로 저장
        //Toast.makeText(getContext(),String.valueOf(sw.isChecked()),Toast.LENGTH_SHORT).show();
        //날씨파싱하는함수들어가야함
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw.isChecked()==true){
                    alarm_switch_off.setTextColor(Color.parseColor("#6667AB"));
                    alarm_switch_on.setTextColor(Color.parseColor("#000000"));
                    Global.getInstance().setAlarm_switch_value(0);
                }
                else{
                    alarm_switch_on.setTextColor(Color.parseColor("#6667AB"));
                    alarm_switch_off.setTextColor(Color.parseColor("#000000"));
                    Global.getInstance().setAlarm_switch_value(1);
                }
            }
        });
        sw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw1.isChecked()==true){
                    jindong_switch_off.setTextColor(Color.parseColor("#6667AB"));
                    jindong_switch_on.setTextColor(Color.parseColor("#000000"));
                    Global.getInstance().setJindong_switch_value(0);
                }
                else{
                    jindong_switch_on.setTextColor(Color.parseColor("#6667AB"));
                    jindong_switch_off.setTextColor(Color.parseColor("#000000"));
                    Global.getInstance().setJindong_switch_value(0);
                }
            }
        });
        remake_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment_eightteen=new Fragment_EightTeen();
                transaction.replace(R.id.fragment_main, fragment_eightteen);
                transaction.commit();
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.clear((SecondActivity)SecondActivity.mContext);
                fragment_main_activity.onShutDown1();
            }
        });

        return view;
    }


}
