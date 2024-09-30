package com.example.last;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class Message_Detail_Activity extends AppCompatActivity {
    Intent intent;
    @Override
    public void onBackPressed() {
        //finishAffinity();
        //Intent intent = new Intent(getApplicationContext(),NineActivity.class);
        //String message_detail = messages.get(position).getTitle();
        //intent.putExtra("message_detail", message_detail);
        //intent.putExtra("promise_where",messages.get(position).getMessage_form());
        //intent.putExtra("message_form",messages.get(position).getMessage_form());
        //intent.putExtra("choose_result","0");
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(intent);
        Global.getInstance().setMessage_position(-1);
        Global.getInstance().setMessage_check(false);
        finish();

    }
    public void Toast_Make(String message){
        LayoutInflater inflater=getLayoutInflater();    //레이아웃 인플레이터 객체 참조 뷰 사용하기위함
        View layout=inflater.inflate(R.layout.toast_design_layout,(ViewGroup) findViewById(R.id.toast_design_layout_main));
        //토스트를위한 레이아웃 인플레이트
        Toast toast=Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
        TextView textView=layout.findViewById(R.id.toast_textview);
        //NineActivity nineActivity=new NineActivity();
        //TextView textView1=(TextView)nineActivity.findViewById()
        textView.setText(message);
        toast.setGravity(Gravity.BOTTOM,0,500);
        toast.setView(layout);  //토스트 뷰 설정
        toast.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_detail);
        Toolbar toolbar1=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        RelativeLayout relativeLayout=findViewById(R.id.message_form);
        Button promise_yes_button=findViewById(R.id.promise_yes_button);
        Button promise_no_button=findViewById(R.id.promise_no_button);
        //메시지 메인 타이틀 전달
        String message_detail = getIntent().getStringExtra("message_detail");
        int message_position=Global.getInstance().getMessage_position();
        //int message_position=getIntent().getIntExtra("message_position",-1);
        //Toast_Make(String.valueOf(message_position));
        TextView message_detail1 = findViewById(R.id.message_detail);
        TextView promise_detail=findViewById(R.id.promise_detail);
        TextView promise_detail_one=findViewById(R.id.promise_detail_one);
        int message_form=getIntent().getIntExtra("message_form",0);
        if(message_form==0){
            //그냥 일반 메시지면
            //Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_SHORT).show();
            relativeLayout.setVisibility(View.INVISIBLE);
            message_detail1.setText(message_detail);
            message_detail1.setOnClickListener(view -> {
                finish();
            });
        }
        else if(message_form==1){
            //약속메시지면
            relativeLayout.setVisibility(View.VISIBLE);
            promise_detail.setText("xxx님이 약속을 잡았습니다.");
            promise_detail_one.setText("약속장소는 "+"약속시간은 ");
            //여기서 약속을 어디인지 받아오는 해야할듯 시간과
            //어떻게 날짜와 시간이 잡히는지 모르겟지만
            //날짜는 yyyy / MM / dd
            //시간은 hh // mm
            //String date_promise; 에 값 저장
            //int year,month,day; 에 date_promise 쪼개서 넣기
            //String time_promise; 에 값 저장
            //int hour,minute; 에 time_promise 쪼개서 넣기
            promise_yes_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast_Make("약속을 수락하셨습니다");
                    Intent intent = new Intent(getApplicationContext(),NineActivity.class);
                    int temp=Global.getInstance().getPromise_exist();
                    temp++;
                    Global.getInstance().setPromise_exist(temp);
                    //String message_detail = messages.get(position).getTitle();
                    //intent.putExtra("message_detail", message_detail);
                    //intent.putExtra("promise_where",messages.get(position).getMessage_form());
                    //intent.putExtra("message_form",messages.get(position).getMessage_form());
                    intent.putExtra("choose_result",1); //수락했으므로 1 보내기
                    //intent.putExtra("message_position",message_position);
                    //intent.putExtra("date_promise",year);
                    //intent.putExtra("date_promise",month);
                    //intent.putExtra("date_promise",day);
                    //intent.putExtra("date_promise",hour);
                    //intent.putExtra("date_promise",minute);
                    Global.getInstance().setMessage_position(message_position);
                    Global.getInstance().setMessage_check(true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //finish();
                }
            });
            promise_no_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast_Make("약속을 거절하셨습니다");
                    //finish();
                    Intent intent = new Intent(getApplicationContext(),NineActivity.class);
                    intent.putExtra("choose_result",1); //거절했으므로 1 보내기기                    //intent.putExtra("message_position",message_position);
                    Global.getInstance().setMessage_position(message_position);
                    Global.getInstance().setMessage_check(true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //finish();
                }
            });
            message_detail1.setText(message_detail);
            message_detail1.setOnClickListener(view -> {
                //수락이나 거절안누르고 나갈경우
                Global.getInstance().setMessage_check(false);
                Global.getInstance().setMessage_position(-1);
                finish();
            });
        }
        else{
            relativeLayout.setVisibility(View.VISIBLE);
            promise_detail.setText("xxx님이 친구 추가를 요청했습니다.");
            promise_detail_one.setText("");
            //여기서 약속을 어디인지 받아오는 해야할듯 시간과
            promise_yes_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast_Make("친구추가를 수락하셨습니다");
                    Intent intent = new Intent(getApplicationContext(),NineActivity.class);
                    //String message_detail = messages.get(position).getTitle();
                    //intent.putExtra("message_detail", message_detail);
                    //intent.putExtra("promise_where",messages.get(position).getMessage_form());
                    //intent.putExtra("message_form",messages.get(position).getMessage_form());
                    intent.putExtra("choose_result",1);
                    //intent.putExtra("message_position",message_position);
                    Global.getInstance().setMessage_position(message_position);
                    Global.getInstance().setMessage_check(true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //finish();
                }
            });
            promise_no_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast_Make("친구추가를 거절하셨습니다");
                    //finish();
                    Intent intent = new Intent(getApplicationContext(),NineActivity.class);
                    intent.putExtra("choose_result",1);
                    //intent.putExtra("message_position",message_position);
                    Global.getInstance().setMessage_position(message_position);
                    Global.getInstance().setMessage_check(true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //finish();
                }
            });
            message_detail1.setText(message_detail);
            message_detail1.setOnClickListener(view -> {
                Global.getInstance().setMessage_check(false);
                Global.getInstance().setMessage_position(-1);
                finish();
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top1, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId()){
            case R.id.item_alarm:
                //Intent intent = new Intent(getApplicationContext(), NineActivity.class);
                //startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
