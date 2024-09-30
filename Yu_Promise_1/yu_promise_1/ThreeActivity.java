package com.example.last;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class ThreeActivity extends AppCompatActivity {
    TextView result_id;
    EditText edit_id;
    int can_go_next=0;//다음패이지로넘어갈수있는지?

    /*@Override
    public void onBackPressed() {
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
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

    private FirebaseDatabase mydatabase = FirebaseDatabase.getInstance();
    private DatabaseReference user_list = mydatabase.getReference("User_list");
    private ValueEventListener checkRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            while (child.hasNext()) {
                if (edit_id.getText().toString().equals(child.next().getKey())) {
                    user_list.removeEventListener(this);
                    result_id.setText("같은 아이디가 존재합니다.");
                    return;
                }
            }
            result_id.setText("사용 가능한 아이디입니다.");
            can_go_next=1;
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.three);
        result_id=findViewById(R.id.result_id);
        edit_id=findViewById(R.id.edit_id);
        result_id.setText("아이디는 영어 또는 숫자로 이루어지며, 6자리 이상이어야합니다");

        edit_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                can_go_next=0;
                if(edit_id.getText().toString().length()<6){
                    result_id.setText("아이디는 영어 또는 숫자로 이루어지며, 6자리 이상이어야합니다");
                }
                else{
                    user_list.addListenerForSingleValueEvent(checkRegister);
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
        Button nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(can_go_next==1){
                    Intent intent = new Intent(getApplicationContext(), FourActivity.class);
                    intent.putExtra("user_id", edit_id.getText().toString());
                    startActivity(intent);
                }
                else{
                    Toast_Make("아이디를 확인해주세요.");
                    //Toast.makeText(getApplicationContext(),"넘어갈수없습니다",Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }
}