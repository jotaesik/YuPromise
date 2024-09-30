package com.example.last;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NineActivity extends AppCompatActivity {
    private int real_message_number;
    public TextView how_many_message;
    private List<Message> messages;
    private MessageAdapter messageAdapter;
    @Override
    public void onBackPressed() {
        finish();
    }
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nine);
        Toolbar toolbar1=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        messages=Global.getInstance().getMessages();
        //messages = Global.getInstance().getMessages();
        real_message_number= Global.getInstance().getMessages().size();
        how_many_message=findViewById(R.id.how_many_message);
        how_many_message.setText("메시지가 "+String.valueOf(Global.getInstance().getMessages().size())+"개 있습니다");
        ListView listViewMessage = findViewById(R.id.message_listview);
        listViewMessage.setAdapter(new MessageAdapter(messages, view -> {
                    final int index = (int) view.getTag();
                    //Toast_Make("흠"+String.valueOf(index));
                    messages.remove(index);
                    finish();
                    overridePendingTransition(0, 0);
                    Intent intent = getIntent(); //인텐트
                    startActivity(intent); //액티비티 열기
                    overridePendingTransition(0, 0);

                })
        );

        listViewMessage.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent(this, Message_Detail_Activity.class);
            String message_detail = messages.get(position).getTitle();
            intent.putExtra("message_detail", message_detail);
            //intent.putExtra("promise_where",messages.get(position).getMessage_form());
            intent.putExtra("message_form",messages.get(position).getMessage_form());
            //intent.putExtra("message_position",position);
            Global.getInstance().setMessage_position(position);
            startActivity(intent);
        });
        /*Toast_Make(String.valueOf(Global.getInstance().getMessage_check())+
                " "+String.valueOf(Global.getInstance().getMessage_position()));*/
        if(Global.getInstance().getMessage_check()==true&&Global.getInstance().getMessage_position()!=-1){
            //수락이나 거절눌렀으면 자동 삭제
            int temp=Global.getInstance().getMessage_position();
            //Toast_Make("성공!!");
            messages.remove(temp);
            //Toast_Make(String.valueOf(Global.getInstance().getMessages().size()));
            how_many_message.setText("메시지가 "+String.valueOf(Global.getInstance().getMessages().size())+"개 있습니다");
            //초기화
            Global.getInstance().setMessage_check(false);
            Global.getInstance().setMessage_position(-1);
            //messageAdapter.notifyDataSetChanged();
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