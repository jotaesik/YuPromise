package com.example.last;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Array;
import java.sql.Time;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Fragment_Twelve1 extends Fragment implements OnBackPressedListener{
    private View view;
    long backKeyPressedTime;
    private TextView where_button;
    private TextView no_input_button;
    private TextView where_button1;
    private EditText input_edittext;
    String month_choose,day_choose,real_month_choose,real_day_choose;
    TextView friend_number,date_text,time_text;
    private Fragment_SixTeen fragment_sixteen;
    //private Fragment_FifthTeen fragment_fifthteen;
    private int promise_exist;
    private Button friend_add,make_finish,date_button,time_button;
    private int clear=0; //리스트뷰 클리어조건
    private EditText find_nickname;
    int can_go_next_month=0,can_go_next_day=0,can_go_next_hour=0,can_go_next_minute=0;
    int can_go_friend_choose=0;
    int can_go_next_date,can_go_next_time;
    int can_go_alarm_choose=0;
    private int friend_last_id;
    //private int friend_choose_number;
    //TextView friend_number;
    private String data;
    private String now_hour,now_minute;
    private String choose_hour,choose_minute;
    private String state;
    private SpinnerAdapter1 adapter;
    private SpinnerAdapter2 adapter1;
    private SpinnerAdapter3 adapter2;
    private SpinnerAdapter4 adapter3;
    private SpinnerAdapter5 adapter4;
    private SpinnerAdapter6 adapter5;
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;
    private Spinner spinner5;
    private Spinner spinner6;
    private String now_year,now_month,now_day;
    private String choose_year,choose_month,choose_day;
    public void set_clear(){
        clear=0;
    }
    Calendar calendar = Calendar.getInstance(); //캘린더객체생성하고
    DatePickerDialog.OnDateSetListener DatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            set_data_text();
        }
    };
    private void set_data_text() {
        SimpleDateFormat temp = new SimpleDateFormat("yyyy / MM / dd", Locale.KOREA); //한국어 사용
        Date just=calendar.getTime();
        choose_year=new SimpleDateFormat("yyyy").format(just);
        choose_month=new SimpleDateFormat("MM").format(just);
        choose_day=new SimpleDateFormat("dd").format(just);
        date_text.setText(temp.format(calendar.getTime()));
        if(now_year.equals(choose_year)&&now_month.equals(choose_month)&&now_day.equals(choose_day)){
            //오늘을 약속날짜로 잡을때
            //Toast_Make(now_day+" "+choose_day);
            if(choose_hour.equals("")&&choose_minute.equals("")){
                //시간이 정해지지 않은경우는 일단 인정
                date_text.setText(temp.format(calendar.getTime()));
            }
            else{
                //하지만 시간이 정해져있더라면?
                //Toast_Make("선택한시간은"+choose_hour+"지금시간은"+now_hour);
                if (Integer.parseInt(choose_hour) <= Integer.parseInt(now_hour)) {
                    if (Integer.parseInt(choose_minute) <= Integer.parseInt(now_minute)) {
                        //당연히 과거시간은 선택이 될수가 없다
                        time_text.setText("존재할 수 없는 시간입니다.");
                    }
                    else{
                        //오전
                        if(state.equals("오전")){
                            time_text.setText("존재할 수 없는 시간입니다.");
                        }
                        else {
                        //오후

                            time_text.setText(state + " " + choose_hour + "시 " + choose_minute + "분");
                            Global.getInstance().setPromise_time(time_text.getText().toString());
                            //Toast_Make("1");
                        }
                    }
                }
                else{
                    //시간이 이후라면
                    time_text.setText(state + " " + choose_hour + "시 " + choose_minute + "분");
                    Global.getInstance().setPromise_time(time_text.getText().toString());
                    //Toast_Make("2");
                }
            }

        }
        else{
            date_text.setText(temp.format(calendar.getTime()));
        }
        Global.getInstance().setPromise_data(date_text.getText().toString());

    }
    Fragment_Main_Activity fragment_main_activity;  //프레그먼트 메인 선언
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
    public void onBackPressed() {       //오버라이딩 fragment_main_activity
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){     //뒤로가기 버튼 제한
            backKeyPressedTime = System.currentTimeMillis();
            Toast_Make("한번더 뒤로가기 누르면 종료");
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            getActivity().finish(); //fragment_main_acitivty 호출해서 finish호출시 fragment_main_activity 종료
            fragment_main_activity.onShutDown();    //fragment_main_Acitivty 온셧다운함수 호출
            //getActivity().finish();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        fragment_main_activity.setOnBackPressedListener(this);
    }

    public void set_date_time_reset(){  //모든 값 초기화
        now_year="";
        now_month="";
        now_day="";
        choose_year="";
        choose_month="";
        choose_day="";
        choose_hour="";
        choose_minute="";
        now_hour="";
        now_minute="";
        //Toast_Make("첫번째값"+Global.getInstance().getPromise_time()+"!");
        //Toast_Make("두번째값"+Global.getInstance().getPromise_time()+"!");
        /*if(Global.getInstance().getPromise_time().equals("")){
            Toast_Make("null");
        }*/
        /*if(!Global.getInstance().getPromise_time().equals("")){

            time_text.setText(Global.getInstance().getPromise_time());
        }*/
        /*if(!Global.getInstance().getPromise_date().equals("")){
            date_text.setText(Global.getInstance().getPromise_date());
        }*/
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_twelve1,container,false);
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        set_date_time_reset();
        input_edittext=(EditText)view.findViewById(R.id.input_edittext);
        time_text=(TextView)view.findViewById(R.id.time_text);

        where_button=(TextView)view.findViewById(R.id.where_button);
        where_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                //getacitvity로 framgent_main_Acitvity받고 프래그먼트매니저 참조확득하고 트랜스픽션시작
                Fragment_Hot_Place fragment_hot_place=new Fragment_Hot_Place();
                transaction.replace(R.id.fragment_main, fragment_hot_place); //프래그먼트 화면대체
                //Global.getInstance().setPage_check(1);
                //Global.getInstance().setPage_check(1);
                //BottomNavigationView bottom_menu=getActivity().findViewById(R.id.bottom_menu);
                //bottom_menu.setSelectedItemId(R.id.fifth_tab);
                transaction.commit();   //작업완료
            }
        });
        where_button1=(TextView)view.findViewById(R.id.where_button1);
        where_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SearchActivity.class);
                getSearchResult.launch(intent);


            }
        });
        //Toast_Make(String.valueOf(Global.getInstance().getPage_check()));
        Global.getInstance().setPage_check(1);
        input_edittext.setFocusable(false);
        /*input_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_layout5);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button close_button=dialog.findViewById(R.id.close_button);
                WebView webView=dialog.findViewById(R.id.webView);
                close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });*/
        date_text=(TextView)view.findViewById(R.id.date_text);
        date_button=(Button)view.findViewById(R.id.date_button);
        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar min_date=Calendar.getInstance();   //최소날짜
                Date now=Calendar.getInstance().getTime();
                SimpleDateFormat year=new SimpleDateFormat("yyyy",Locale.KOREA);
                SimpleDateFormat month=new SimpleDateFormat("MM",Locale.KOREA);
                SimpleDateFormat day=new SimpleDateFormat("dd",Locale.KOREA);
                now_year=year.format(now);
                now_month=month.format(now);
                now_day=day.format(now);
                min_date.set(Integer.parseInt(now_year),Integer.parseInt(now_month)-1,Integer.parseInt(now_day));
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),DatePicker,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(min_date.getTime().getTime());
                datePickerDialog.show();
            }
        });
        time_button=(Button)view.findViewById(R.id.time_button);
        time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar1 = Calendar.getInstance();
                int hour = calendar1.get(Calendar.HOUR_OF_DAY); //24시간으로 표시
                int minute = calendar1.get(Calendar.MINUTE);
                TimePickerDialog TimePicker;
                TimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                        Date now=Calendar.getInstance().getTime();
                        SimpleDateFormat hour=new SimpleDateFormat("HH",Locale.KOREA);
                        SimpleDateFormat minute=new SimpleDateFormat("mm",Locale.KOREA);
                        now_hour=hour.format(now);
                        now_minute=minute.format(now);
                        //Toast_Make(now_minute);
                        state = "오전";
                        if (Hour > 12) {
                            Hour =Hour- 12;
                            state = "오후";
                        }
                        if(choose_year.equals("")&&choose_month.equals("")&&choose_day.equals("")) {
                            time_text.setText(state + " " + Hour+ "시 " + Minute + "분");
                            Global.getInstance().setPromise_time(time_text.getText().toString());
                            //Toast_Make("1");
                            if(state.equals("오후")){
                                Hour=Hour+12;
                            }
                            choose_hour=String.valueOf(Hour);
                            choose_minute=String.valueOf(Minute);
                        }
                        else{
                            if (choose_year.equals(now_year) && choose_month.equals(now_month) && choose_day.equals(now_day)) {
                                //Toast_Make(Hour+"현재시간"+now_hour);
                                if (Hour <= Integer.parseInt(now_hour)) {
                                    //Toast_Make(Minute+"현재븐"+now_minute);
                                    //Toast.makeText(getContext(),Integer.parseInt(now_minute)+" ",Toast.LENGTH_LONG).show();
                                    if (Minute <= Integer.parseInt(now_minute)) {
                                        time_text.setText("존재할 수 없는 시간입니다.");
                                        Global.getInstance().setPromise_time("");
                                    }
                                    else{
                                        if(state.equals("오전")){
                                            time_text.setText("존재할 수 없는 시간입니다");
                                            Global.getInstance().setPromise_time("");
                                        }
                                        else {
                                            //Toast_Make("여기서발생");
                                            choose_hour = String.valueOf(Hour);
                                            choose_minute = String.valueOf(Minute);
                                            time_text.setText(state + " " + Hour + "시 " + Minute + "분");
                                            Global.getInstance().setPromise_time(time_text.getText().toString());
                                            //Toast_Make("1");
                                        }
                                    }
                                }
                                else{

                                    choose_hour=String.valueOf(Hour);
                                    choose_minute=String.valueOf(Minute);
                                    time_text.setText(state + " " + Hour + "시 " + Minute + "분");
                                    Global.getInstance().setPromise_time(time_text.getText().toString());
                                    //Toast_Make("2");
                                }
                            }
                            else{

                                time_text.setText(state + " " + Hour + "시 " + Minute + "분");
                                Global.getInstance().setPromise_time(time_text.getText().toString());
                                //Toast_Make("3");
                            }
                        }
                    }
                }, hour, minute, false);
                TimePicker.show();

                //Toast_Make(Global.getInstance().getPromise_time());
            }
        });

        no_input_button=(TextView) view.findViewById(R.id.no_input_button);
        //Toast_Make(String.valueOf(Global.getInstance().getPage_check()));

        if (getArguments() != null) //전달받은 데이터가 잇으면
        {
            String bundle_string = getArguments().getString("we_where"); // 프래그먼트1에서 받아온 값 넣기
            input_edittext.setText(bundle_string);
            //we_where.setText("약속장소를 "+bundle_string+" 하시겠습니까?");
            //bundle_string1=getArguments().getString("we_where1");
            //we_where1.setText(bundle_string1);
        }
        promise_exist= Global.getInstance().getPromise_exist();
         if(!Global.getInstance().getPromise_date().equals("")){
            date_text.setText(Global.getInstance().getPromise_date());
        }
         //Toast_Make(Global.getInstance().getPromise_time());
        /*if(Global.getInstance().getPromise_time().equals("")){
            Toast_Make("오류");
        }*/
        //Toast_Make("값"+Global.getInstance().getPromise_time()+"!");
        //Toast_Make(Global.getInstance().getPromise_time());
         if(!Global.getInstance().getPromise_time().equals("")){
             //Toast_Make("머지");
             time_text.setText(Global.getInstance().getPromise_time());
         }
        make_finish=(Button) view.findViewById(R.id.make_finish);
        make_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(date_text.getText().toString().equals("아직 정해지지 않았습니다")){
                    Toast_Make("약속 날짜가 선택되지 않았습니다.");
                    return;
                }
                else if(time_text.getText().toString().equals("아직 정해지지 않았습니다")){
                    Toast_Make("약속 시간이 선택되지 않았습니다.");
                    return;
                }
                else if(time_text.getText().toString().equals("존재할 수 없는 시간입니다.")){
                    Toast_Make(("존재할 수 없는 약속시간입니다"));
                }
                else if(input_edittext.getText().toString().equals("아직 정해지지 않았습니다")){
                    Toast_Make("장소가 정해지지 않았습니다");
                }
                else{
                    promise_exist++;
                    Global.getInstance().setPromise_exist(promise_exist);

                    //Global.getInstance().setPromise_time(time_text.getText().toString());
                    //Global.getInstance().setPromise_data(date_text.getText().toString());
                    Global.getInstance().setPromise_data("");
                    Global.getInstance().setPromise_time("");
                    Global.getInstance().setAddress_number(0);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    //fragment_fifthteen=new Fragment_FifthTeen();
                    Fragment_Ten fragment_ten=new Fragment_Ten();
                    BottomNavigationView bottom_menu=getActivity().findViewById(R.id.bottom_menu);
                    bottom_menu.setSelectedItemId(R.id.third_tab);
                    transaction.replace(R.id.fragment_main, fragment_ten);
                    transaction.commit();
                }

            }
        });

        return view;
    }
    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if(result.getData() != null){
                    data = result.getData().getStringExtra("data");
                    input_edittext.setText(data);
                }

            }
    );

}
