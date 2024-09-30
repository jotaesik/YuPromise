package com.example.last;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SevenActivity extends AppCompatActivity {
    private DatabaseReference myPostReference;
    String user_id;
    String user_pw;
    String user_nickname;
    String user_phoneNo;

    public void Toast_Make(String message){
        LayoutInflater inflater=getLayoutInflater();    //레이아웃 인플레이터 객체 참조 뷰 사용하기위함
        View layout=inflater.inflate(R.layout.toast_design_layout,(ViewGroup) findViewById(R.id.toast_design_layout_main));
        //토스트를위한 레이아웃 인플레이트
        Toast toast=Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
        TextView textView=layout.findViewById(R.id.toast_textview);
        textView.setText(message);
        toast.setGravity(Gravity.BOTTOM,0,500);
        toast.setView(layout);  //토스트 뷰 설정
        toast.show();
    }

    public void postFirebaseDatabase(){
        myPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        AssignPost post = new AssignPost(user_pw, user_nickname, user_phoneNo);
        postValues = post.toMap();
        childUpdates.put("/User_list/" + user_id, postValues);
        myPostReference.updateChildren(childUpdates);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //finishAffinity();
        //Toast_Make("뒤로가실수 없습니다");

        //finishAffinity();
        //Intent intent = new Intent(getApplicationContext(),SixActivity.class);
        //startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seven);
        //가정 약속이 있다 1->ten 약속이없다 0->eight
        int exist=0;
        user_id = getIntent().getExtras().getString("user_id");
        user_pw = getIntent().getExtras().getString("user_pw");
        user_nickname = getIntent().getExtras().getString("user_nickname");
        user_phoneNo = getIntent().getExtras().getString("user_phoneNo");

        postFirebaseDatabase();
        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
                finish();

            }
        });






    }
}
