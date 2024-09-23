package com.example.last;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class AddressActivity extends AppCompatActivity {
    private WebView webView;
    public void Toast_Make(String message){
        LayoutInflater inflater=getLayoutInflater();    //레이아웃 인플레이터 객체 참조 뷰 사용하기위함
        View layout=inflater.inflate(R.layout.toast_design_layout,(ViewGroup) findViewById(R.id.toast_design_layout_main));
        //layout의 이름과 그 보여줄 layout의 root view의 타입 아이디
        //토스트를위한 레이아웃 인플레이트
        Toast toast=Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
        //앱의 생명주기 영향 앱이 start to end까지 동일한 객체
        TextView textView=layout.findViewById(R.id.toast_textview);
        //dfs방식
        textView.setText(message);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(layout);  //토스트 뷰 설정
        toast.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout5);
        webView=(WebView)findViewById(R.id.webView);


    }
}
