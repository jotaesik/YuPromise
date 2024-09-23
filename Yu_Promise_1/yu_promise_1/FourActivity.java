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

import java.util.regex.Pattern;

public class FourActivity extends AppCompatActivity {
    private TextView result_password,result_password_check;
    private EditText edit_password,edit_password_check;
    int can_go_next_password;//다음패이지로넘어갈수있는지?
    /*@Override
    public void onBackPressed() {
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(),ThreeActivity.class);
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

    /*public void password_number_english_count(String edit_password){
        english_count=0;
        number_count=0;
        int length=edit_password.length();
        for(int i=0;i<length;i++){
            if('a'<=edit_password.charAt(i)&&edit_password.charAt(i)<='z'){
                english_count++;
            }
            else if('0'<=edit_password.charAt(i)&&edit_password.charAt(i)<='9'){
                number_count++;
            }
        }
        if(0==english_count){
            //Toast.makeText(getApplicationContext(),"숫자를포함해야합니다",Toast.LENGTH_SHORT).show();
            can_go_next_password=0;
            result_password.setText("소문자를 포함해야합니다.");
            //edit_password.setText("");
        }
        else if(0==number_count){
            //Toast.makeText(getApplicationContext(),"소문자를포함해야합니다",Toast.LENGTH_SHORT).show();
            result_password.setText("숫자를 포함해야합니다.");
            can_go_next_password=0;
        }
        else{
            result_password.setText("사용가능합니다");
            can_go_next_password=1;
        }
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four);
        result_password=findViewById(R.id.result_password);
        edit_password=findViewById(R.id.edit_password);
        result_password_check=findViewById(R.id.result_password_check);
        edit_password_check=findViewById(R.id.edit_password_check);
        result_password.setText("비밀번호는 대,소문자와 숫자 그리고 특수문자로만 이루어져야하고 6~10자리이어야 합니다");
        result_password_check.setText("");
        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                can_go_next_password = 0;
                /*if(edit_password.getText().toString().length()<6){
                    result_password.setText("비밀번호는 최소 6자리 이상이 되어야합니다");
                    english_count=0;
                    number_count=0;
                    can_go_next_password=0;
                }*/
                //else{
                String temp=edit_password.getText().toString();
                String password_need= "^[A-Za-z0-9.,_%+~`!@#$^&*()_+-]{6,10}";
                //정규표현식 영어대소문자와숫자그리고 특수문자로 이루어지고 6자리에서 10자리
                boolean check_password= Pattern.matches(password_need,temp);
                if(check_password==true){
                    if(edit_password.getText().toString().equals("")){
                        result_password.setText("비밀번호는 대,소문자와 숫자 그리고 특수문자로만 이루어져야하고 6~10자리이어야 합니다");
                        result_password_check.setText("");
                    }

                    else if(edit_password_check.getText().toString().equals("")){
                        result_password.setText("사용 가능한 비밀번호입니다");
                        result_password_check.setText("비밀번호를 한번 더 입력해주세요.");
                    }

                    else if(edit_password.getText().toString().equals(edit_password_check.getText().toString())){
                        result_password.setText("사용 가능한 비밀번호입니다");
                        result_password_check.setText("비밀번호가 일치합니다");
                        can_go_next_password=1;
                    }

                    else{
                        result_password.setText("사용 가능한 비밀번호입니다");
                        result_password_check.setText("비밀번호가 일치하지 않습니다");
                    }

                }
                else{
                    result_password.setText("비밀번호는 대,소문자와 숫자 그리고 특수문자로만 이루어져야하고 6~10자리이어야 합니다");
                    result_password_check.setText("");
                }
                //english_count=0;
                //number_count=0;
                //password_number_english_count(temp);
                // }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edit_password_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                can_go_next_password = 0;
                /*if(edit_password.getText().toString().length()<6){
                    result_password.setText("비밀번호는 최소 6자리 이상이 되어야합니다");
                    english_count=0;
                    number_count=0;
                    can_go_next_password=0;
                }*/
                //else{
                String temp=edit_password.getText().toString();
                String password_need= "^[A-Za-z0-9.,_%+~`!@#$^&*()_+-]{6,10}";
                //정규표현식 영어대소문자와숫자그리고 특수문자로 이루어지고 6자리에서 10자리
                boolean check_password= Pattern.matches(password_need,temp);
                if(check_password==true){
                    if(edit_password.getText().toString().equals("")){
                        result_password.setText("비밀번호는 대,소문자와 숫자 그리고 특수문자로만 이루어져야하고 6~10자리이어야 합니다");
                        result_password_check.setText("");
                    }

                    else if(edit_password_check.getText().toString().equals("")){
                        result_password.setText("사용 가능한 비밀번호입니다");
                        result_password_check.setText("비밀번호를 한번 더 입력해주세요.");
                    }

                    else if(edit_password.getText().toString().equals(edit_password_check.getText().toString())){
                        result_password.setText("사용 가능한 비밀번호입니다");
                        result_password_check.setText("비밀번호가 일치합니다");
                        can_go_next_password=1;
                    }

                    else{
                        result_password.setText("사용 가능한 비밀번호입니다");
                        result_password_check.setText("비밀번호가 일치하지 않습니다");
                    }

                }
                else{
                    result_password.setText("비밀번호는 대,소문자와 숫자 그리고 특수문자로만 이루어져야하고 6~10자리이어야 합니다");
                    result_password_check.setText("");
                }
                //english_count=0;
                //number_count=0;
                //password_number_english_count(temp);
                // }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button backButton = (Button) findViewById(R.id.back_button1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button nextButton = (Button) findViewById(R.id.next_button1);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(can_go_next_password==1){
                    Intent intent = new Intent(getApplicationContext(), FiveActivity.class);
                    intent.putExtra("user_id", getIntent().getExtras().getString("user_id"));
                    intent.putExtra("user_pw", edit_password.getText().toString());
                    startActivity(intent);
                }
                else{
                    Toast_Make("비밀번호를 확인해주세요.");
                }

            }
        });

    }
}
