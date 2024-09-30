package com.example.last;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SixActivity extends AppCompatActivity {
    /*@Override
    public void onBackPressed() {
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(),FiveActivity.class);
        startActivity(intent);
    }*/

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
    TextView result_number;
    EditText edit_number;
    int can_go_next=0;//다음패이지로넘어갈수있는지?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.six);
        result_number=findViewById(R.id.result_number);
        edit_number=findViewById(R.id.edit_number);
        result_number.setText("전화번호는 11자가되야합니다");
        edit_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //전화번호 중복 코드 추가
                if(edit_number.getText().toString().equals("")){
                    result_number.setText("존재하는 전화번호가 있습니다.");
                    can_go_next=0;
                }
                else if(edit_number.getText().toString().length()!=11){
                    result_number.setText("11자가 되야합니다");
                    can_go_next=0;
                }
                else if(edit_number.getText().toString().equals("")){
                    result_number.setText("입력대기중입니다");
                    can_go_next=0;
                }
                else{
                    result_number.setText("전화번호 사용가능합니다");
                    can_go_next=1;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button finishButton = (Button) findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(can_go_next==1){
                    Intent intent = new Intent(getApplicationContext(), SevenActivity.class);
                    intent.putExtra("user_id", getIntent().getExtras().getString("user_id"));
                    intent.putExtra("user_pw", getIntent().getExtras().getString("user_pw"));
                    intent.putExtra("user_nickname", getIntent().getExtras().getString("user_nickname"));
                    intent.putExtra("user_phoneNo", edit_number.getText().toString());
                    startActivity(intent);
                    //스택다지우기
                    finishAffinity();
                }
                else{
                    Toast_Make("전화번호 입력에 오류가 있습니다");
                    //Toast.makeText(getApplicationContext(),"넘어갈수없습니다",Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });

    }
}
