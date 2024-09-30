package com.example.last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.microedition.khronos.opengles.GL;

public class Fragment_Main_Activity extends AppCompatActivity{
    private Fragment_Eight fragment_eight;
    private Fragment_SevenTeen fragment_seventeen;
    private Fragment_SixTeen fragment_sixteen;
    private String str_url;
    //private Fragment_Eleven fragment_eleven;
    //private Fragment_FifthTeen fragment_fifthteen;
    private Fragment_Twelve fragment_twelve;
    private Fragment_Hot_Place fragment_hot_place=new Fragment_Hot_Place();
    private Fragment_Ten fragment_ten;
    private Fragment_FourTeen1 fragment_fourteen1;
    private Fragment_FourTeen fragment_fourteen;
    private Fragment_NineTeen fragment_nineteen;
    private Fragment_Twelve1 fragment_twelve1;
    //private AllOfActivity allOfActivity;
    //private long lastTimeBackPressed;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    OnBackPressedListener listener; //프레그먼트의 부모 activity이므로 객체  선언
    private int promise_exist;
    private double lon=Global.getInstance().getLongitude();
    private double lat=Global.getInstance().getLatitude();
    /*@Override
    public void onBackPressed(){
        backKeyClose.onBackPressed();
    }*/
    public void setOnBackPressedListener(OnBackPressedListener listener){   //만들어진 인터페이스 적용
        this.listener = listener; //설정
    }
    public void setFragment_sixteen(){
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_main,fragment_twelve);
        fragmentTransaction.commit();
    }
    public void setFragment_hot_place(){
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main,fragment_twelve1);
        BottomNavigationView bottom_menu=findViewById(R.id.bottom_menu);
        bottom_menu.setSelectedItemId(R.id.second_tab);
        fragmentTransaction.commit();
    }
    public void setFragment_eightteen(){    //각각의 백버튼이 눌럿을때
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_main,fragment_seventeen);
        fragmentTransaction.commit();
    }

    public void setFragment_fourteen(){
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_main,fragment_twelve);
        fragmentTransaction.commit();
    }
    public void setFragment_fourteen1(){
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_main, fragment_fourteen);
        fragmentTransaction.commit();
    }
    public void setFragment_twelve(){
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        //Bundle bundle=new Bundle();
        //bundle.putString("before_exist1","no");
        //fragment_hot_place.setArguments(bundle);
        fragmentTransaction.add(R.id.fragment_main,fragment_hot_place);
        fragmentTransaction.commit();
    }
    /*public void setFragment_twelve1(){
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main,fragment_eleven);
        fragmentTransaction.commit();
    }*/
    public void setFragment_address(){
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main,fragment_twelve1);
        fragmentTransaction.commit();
    }
    public void setFragment_address1(){
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main,fragment_twelve);
        fragmentTransaction.commit();
    }
    public void setFragment_nineteen(){
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_main,fragment_seventeen);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        if(listener!=null){     //다른프레그먼트에서 백버튼 실행시
            listener.onBackPressed();
        }else{
            super.onBackPressed();  //액티비티에서 백버튼 실행시
        }
    }
    public void onShutDown(){
        moveTaskToBack(true);   //다른 액티비티들이 뜨는거 막는 역할
        finish();   //현재 액티비티 종료
        android.os.Process.killProcess(android.os.Process.myPid()); //안드로이드 프로세스 kill
    }
    public void onShutDown1(){
        moveTaskToBack(true);   //다른 액티비티들이 뜨는거 막는 역할
        finish();   //현재 액티비티 종료
        finishAffinity(); //모든 액티비티 종료
        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        startActivity(intent);

    }
    public void Toast_Make(String message){
        LayoutInflater inflater=getLayoutInflater();    //레이아웃 인플레이터 객체 참조 뷰 사용하기위함
        View layout=inflater.inflate(R.layout.toast_design_layout,(ViewGroup) findViewById(R.id.toast_design_layout_main));
        //토스트를위한 레이아웃 인플레이트
        Toast toast=Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
        TextView textView=layout.findViewById(R.id.toast_textview);
        textView.setText(message);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(layout);  //토스트 뷰 설정
        toast.show();
    }
    public int onPromiseExist(){
        return promise_exist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        Global.getInstance().setUser_id(getIntent().getExtras().getString("user_id"));
        Global.getInstance().setAll();
        //getMyWhere();
        //allOfActivity=(AllOfActivity)getApplicationContext();
        //allOfActivity.setPromise_exist(0);  //1이나 0이거나
        //promise_exist=((AllOfActivity)getApplicationContext()).getPromise_exist();
        //Toast.makeText(getApplicationContext(),String.valueOf(promise_exist),Toast.LENGTH_SHORT).show();
        //Global.getInstance().setPromise_exist(0);//일단 0으로 초기화 하지만 받아와야함.
        //promise_exist=Global.getInstance().getPromise_exist();
        //Toast.makeText(getApplicationContext(),String.valueOf(promise_exist),Toast.LENGTH_SHORT).show();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //키보드가 화면 안가리게하기
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //제목 안보이게하기
        fragment_eight = new Fragment_Eight();
        fragment_seventeen=new Fragment_SevenTeen();
        fragment_twelve1=new Fragment_Twelve1();
        Fragment_Hot_Place fragment_hot_place=new Fragment_Hot_Place();
        fragment_sixteen=new Fragment_SixTeen();
        //fragment_eleven=new Fragment_Eleven();
        //fragment_fifthteen=new Fragment_FifthTeen();
        fragment_ten=new Fragment_Ten();
        fragment_twelve=new Fragment_Twelve();
        fragment_fourteen1=new Fragment_FourTeen1();
        fragment_fourteen=new Fragment_FourTeen();
        fragment_nineteen=new Fragment_NineTeen();
        fragmentManager =getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        //뭔가 db에서 유저의 약속개수를 받아와야함

        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        bottom_menu.setItemIconTintList(null);

        promise_exist=Global.getInstance().getPromise_exist();
        findViewById(R.id.bottom_menu);
        bottom_menu.setSelectedItemId(R.id.fifth_tab);
        fragmentTransaction.add(R.id.fragment_main,fragment_hot_place);
        fragmentTransaction.commit();

        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {  //아이템 클릭시
                    case R.id.first_tab:    //핫플레이스탭클릭시
                        Global.getInstance().setPage_check3(0); //초기화
                        ArrayList<Friend>friends1=Global.getInstance().getFriends();
                        for(int i=0;i<friends1.size();i++){
                            friends1.get(i).setChecked(false);  //새롭게 시작하므로 모든 친구체크해제
                            Global.getInstance().setFriends(friends1);  //그걸 전역변수로 저장
                            //Toast.makeText(getApplicationContext(),String.valueOf(friends1.get(i).getChecked()),Toast.LENGTH_SHORT).show();
                        }
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_main,fragment_sixteen).commitAllowingStateLoss();

                        return true;
                    case R.id.second_tab:
                        //Global.getInstance().setPromise_time("");
                        //Global.getInstance().setPromise_data("");
                        ArrayList<Friend_no> friend_nos=Global.getInstance().getFriend_nos();
                        friend_nos.clear();
                        Global.getInstance().setFriend_nos(friend_nos);
                        Global.getInstance().setAddress_number(0);
                        Global.getInstance().setMe_check(0);
                        Global.getInstance().setPage_check(1);  //핫플레이스탭에서 뒤로가기눌럿을때 구별법
                        promise_exist=Global.getInstance().getPromise_exist();
                        for(int i=0;i<Global.getInstance().getFriends().size();i++){    //모든 친구들 체크해제


                                Global.getInstance().getFriends().get(i).setChecked(false);

                        }
                        Global.getInstance().setFriend_choose_number(0);    //약속같이할친구들도 초기화
                        //Global.getInstance().setFriend_choose_number(0);
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_main,fragment_twelve1).commitAllowingStateLoss();
                        //Toast.makeText(getApplicationContext(),String.valueOf(promise_exist),Toast.LENGTH_SHORT).show();
                        /*if(promise_exist==0){
                            getSupportFragmentManager().beginTransaction().
                                    replace(R.id.fragment_main,fragment_eleven).commitAllowingStateLoss();

                        }
                        else{
                            getSupportFragmentManager().beginTransaction().
                                    replace(R.id.fragment_main,fragment_fifthteen).commitAllowingStateLoss();

                        }*/

                        return true;
                    case R.id.third_tab:
                        //약속없으면 8
                        //약속있으면 10
                        promise_exist=Global.getInstance().getPromise_exist();
                        //Toast.makeText(getApplicationContext(),String.valueOf(promise_exist),Toast.LENGTH_SHORT).show();
                        if(promise_exist==0){
                            getSupportFragmentManager().beginTransaction().
                                    replace(R.id.fragment_main,fragment_eight).commitAllowingStateLoss();
                        }
                        else{
                            getSupportFragmentManager().beginTransaction().
                                    replace(R.id.fragment_main,fragment_ten).commitAllowingStateLoss();
                        }

                        return true;
                    case R.id.fourth_tab:
                        Date now= Calendar.getInstance().getTime();
                        SimpleDateFormat year=new SimpleDateFormat("yyyy", Locale.KOREA);
                        SimpleDateFormat month=new SimpleDateFormat("MM",Locale.KOREA);
                        SimpleDateFormat day=new SimpleDateFormat("dd",Locale.KOREA);
                        SimpleDateFormat hour=new SimpleDateFormat("HH",Locale.KOREA);
                        SimpleDateFormat minute=new SimpleDateFormat("mm",Locale.KOREA);
                        String now_year=year.format(now);
                        String now_month=month.format(now);
                        String now_day=day.format(now);
                        String now_hour=hour.format(now);
                        String now_minute=hour.format(now);
                        Global.getInstance().setNow_year(now_year);
                        Global.getInstance().setNow_month(now_month);
                        Global.getInstance().setNow_day(now_day);
                        Global.getInstance().setNow_hour(now_hour);
                        Global.getInstance().setNow_minute(now_minute);
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_main,fragment_seventeen).commitAllowingStateLoss();
                        return true;
                    case R.id.fifth_tab:    //핫플레이스
                        Global.getInstance().setMe_check(0);    //초기화
                        Global.getInstance().setPage_check(0);
                        Global.getInstance().setAddress_number(0);
                        ArrayList<Friend_no> friend_nos1=Global.getInstance().getFriend_nos();
                        friend_nos1.clear();;
                        Global.getInstance().setFriend_nos(friend_nos1);
                        Global.getInstance().setFriend_choose_number(0);
                        ArrayList<Friend>friends=Global.getInstance().getFriends();
                        //Toast.makeText(getApplicationContext(),String.valueOf(friends.size()),Toast.LENGTH_SHORT).show();
                        for(int i=0;i<friends.size();i++){
                            friends.get(i).setChecked(false);
                        }
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_main,fragment_hot_place).commitAllowingStateLoss();
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)    //선택된 객체 이동
    {
        switch(item.getItemId()){
            case R.id.item_alarm:
                Intent intent = new Intent(getApplicationContext(), NineActivity.class);    //현재 액티비티에서 이동
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}