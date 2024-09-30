package com.example.last;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Not_Login_User_Activity extends AppCompatActivity {
    private Button friend_add,find_where;
    private TextView text_find_where;
    private ArrayList<Friend> friend;
    private ListView no_where_input;
    private FriendAdapter friendAdapter;
    private String data;
    int friend_add_number;
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
    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_login_user);
        friend_add=(Button) findViewById(R.id.friend_add);
        find_where=(Button)findViewById(R.id.find_where);
        text_find_where=(TextView)findViewById(R.id.text_find_where);
        friend=new ArrayList<>();
        no_where_input=(ListView) findViewById(R.id.no_where_input);
        friendAdapter=new FriendAdapter(getApplicationContext(),friend);
        no_where_input.setAdapter(friendAdapter);
        friend_add_number = 0;
        //기본베이스 2명
        for(int i=0;i<2;i++){
            friend.add(new Friend(friend_add_number++));
        }
        friend_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                friend.add(new Friend(friend_add_number++));
                no_where_input.setAdapter(friendAdapter);
            }
        });
        find_where.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //중간지점찾아주는 코드나 함수 추가

                text_find_where.setText("이곳에다가 장소를 표시해줍니다");
            }
        });

    }
    class FriendAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener {

        private Context context;
        private List list;
        public ArrayList<Friend> listview=new ArrayList<Friend>();
        public FriendAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();

        }

        class ViewHolder{
            public TextView friend_number;
            public EditText friend_where;
            public Button friend_delete_button;
        }
        public FriendAdapter(Context context, ArrayList list){
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.no_login_where, parent, false);
            }
            viewHolder = new FriendAdapter.ViewHolder();
            viewHolder.friend_number = (TextView) convertView.findViewById(R.id.friend_number);
            viewHolder.friend_where = (EditText) convertView.findViewById(R.id.friend_where);
            viewHolder.friend_where.setFocusable(false);
            viewHolder.friend_where.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Not_Login_User_Activity.this, SearchActivity.class);
                    getSearchResult.launch(intent);
                    viewHolder.friend_where.setText(data);
                    data="";
                    //Toast_Make("click");
                    /*Intent intent=new Intent(getApplicationContext(),AddressActivity.class);
                    overridePendingTransition(0, 0);
                    startActivity(intent);*/
                    /*Dialog dialog=new Dialog(Not_Login_User_Activity.this);
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
                    });*/
                }

            });

            viewHolder.friend_delete_button=(Button) convertView.findViewById(R.id.friend_delete_button);
            if(position==list.size()-1){
                //최근추가된 사람만 삭제가능
                viewHolder.friend_delete_button.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.friend_delete_button.setVisibility(View.INVISIBLE);
            }
            viewHolder.friend_delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position==list.size()-1){
                        friend_add_number--;
                        list.remove(position);
                        no_where_input.setAdapter(friendAdapter);
                    }
                    else{
                        Toast_Make("최근추가된 친구들부터 지울수있습니다.");
                    }

                }
            });


            //viewHolder.button.setOnClickListener(onButtonChooseClicked);

            final Friend friend = (Friend) list.get(position);
            viewHolder.friend_number.setText(String.valueOf(friend.getFriend_number()));
            viewHolder.friend_where.setText(friend.getFriend_where());
            viewHolder.friend_where.addTextChangedListener(friend.textWatcher);
            return convertView;
        }
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        data = result.getData().getStringExtra("data");

                    }
                }
            }
    );
    public class Friend {
        public int friend_number;
        public String friend_where;
        public TextWatcher textWatcher;
        public Friend(int friend_number) {
            this.friend_number=friend_number;
            friend_where="";
            textWatcher=new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    friend_where=s.toString();
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            };
        }
        public int getFriend_number(){
            return friend_number;
        }
        public String getFriend_where() {
            return friend_where;
        }
    }
}
