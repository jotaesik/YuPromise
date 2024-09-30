package com.example.last;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
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

import java.sql.Array;
import java.sql.Time;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Fragment_Twelve extends Fragment implements OnBackPressedListener{
    private View view;
    private int friend_choose_number;
    String month_choose,day_choose,real_month_choose,real_day_choose;
    TextView friend_number,date_text,time_text;
    private Fragment_SixTeen fragment_sixteen;
    private Fragment_FourTeen fragment_fourteen;
    private Button friend_add,make_finish,date_button,time_button,friend_add1;
    private int clear=0; //리스트뷰 클리어조건
    private EditText find_nickname,find_nickname1;
    int can_go_next_month=0,can_go_next_day=0,can_go_next_hour=0,can_go_next_minute=0;
    int can_go_friend_choose=0;
    int can_go_next_date,can_go_next_time;
    int can_go_alarm_choose=0;
    private int friend_last_id;
    //private int friend_choose_number;
    //TextView friend_number;
    private String now_hour,now_minute;
    private String choose_hour,choose_minute;
    private String state;
    private String now_year,now_month,now_day;
    private String choose_year,choose_month,choose_day;
    private ArrayList<Friend_no> friend_nos;
    private ListView listView1;
    private Friend_no_Adapter friend_no_adapter;
    int how_number=0;
    int address_number=0;
    private int me_number=0;
    private  CheckBox me_checkbox;
    public void set_clear(){
        clear=0;
    }
    public static boolean setListViewHeightBasedOnItems(ListView listView) {    //리스트뷰에 아이템 개수에 따른 조절
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        }
        else {
            return false;
        }
    }
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
    Fragment_Main_Activity fragment_main_activity;
    @Override
    public void onBackPressed() {
        if(getArguments()!=null){
            //Toast_Make("성공");
            fragment_main_activity.setFragment_twelve();
        }
        else{
            fragment_main_activity.setFragment_twelve();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        fragment_main_activity.setOnBackPressedListener(this);
    }
    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if(result.getData() != null){
                    String data = result.getData().getStringExtra("data");
                    find_nickname1.setText(data);
                }

            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_twelve,container,false);
        view.setClickable(true);
        me_checkbox=(CheckBox) view.findViewById(R.id.me_checkbox);
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        friend_number=(TextView)view.findViewById(R.id.friend_number);
        find_nickname=(EditText)view.findViewById(R.id.find_nickname);
        friend_add1=(Button)view.findViewById(R.id.friend_add1);
        find_nickname1=(EditText)view.findViewById(R.id.find_nickname1);
        listView1=(ListView)view.findViewById(R.id.listView1);
        make_finish=(Button) view.findViewById(R.id.make_finish);
        ArrayList<Friend>friends;
        ArrayList<Friend>friends1 = new ArrayList<>();
        ListView friend_list_view;
        FriendAdapter2 friendAdapter2;
        friends=Global.getInstance().getFriends();//지금까지의친구목록받아오기
        friends1.addAll(friends);   //깊은복사
        friend_list_view=(ListView)view.findViewById(R.id.listView);
        friendAdapter2=new FriendAdapter2(getContext(),friends);
        friend_list_view.setAdapter(friendAdapter2);
        friend_nos=new ArrayList<>();
        friend_nos=Global.getInstance().getFriend_nos();
        friend_no_adapter=new Friend_no_Adapter(getContext(),friend_nos);
        listView1.setAdapter(friend_no_adapter);
        setListViewHeightBasedOnItems(listView1);
        find_nickname1.setFocusable(false);
        friend_add=(Button) view.findViewById(R.id.friend_add);
        friend_choose_number=Global.getInstance().getFriend_choose_number();
        //address_number=Global.getInstance().getAddress_number();
        //Toast_Make(String.valueOf(how_number));
        /*for(int i=0;i<how_number;i++){
            friend_nos.add(new Friend_no(how_number,find_nickname1.getText().toString()));

        }*/
        how_number=friend_nos.size();
        Toast_Make(String.valueOf(friend_nos.size()));




        friend_number.setText("총 "+ (friend_choose_number+me_number+how_number)+"명입니다");

        //Toast_Make(String.valueOf(how_number));

        if(Global.getInstance().getMe_check()==0){
            Global.getInstance().setMe_go(true);
            me_checkbox.setChecked(true);
            //Toast_Make("발생"+me_checkbox.isChecked());
            me_number=1;
            friend_number.setText("총 "+ (friend_choose_number+me_number+how_number)+"명입니다");
            //Toast_Make(String.valueOf(Global.getInstance().getMe_go()));

        }
        //me_checkbox.setChecked(true);
        //Toast_Make(String.valueOf(Global.getInstance().getMe_go()));
        //Global.getInstance().setFriend_choose_number(friend_choose_number);
        //Toast_Make(String.valueOf(me_checkbox.isChecked()));
        /*if(me_checkbox.isChecked()==true){
            Toast_Make("발생");
            //friend_choose_number++;
            me_number=1;
            friend_number.setText("총 "+ (friend_choose_number+me_number)+"명입니다");
            Global.getInstance().setFriend_choose_number(friend_choose_number);
        }*/
        me_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //me_checkbox.setChecked(true);
                if(me_checkbox.isChecked()==true){

                    me_number=1;

                    me_checkbox.setChecked(true);
                    Global.getInstance().setMe_go(true);
                    //Toast_Make("온"+me_checkbox.isChecked());
                    friend_number.setText("총 "+ (friend_choose_number+me_number+how_number)+"명입니다");
                    Global.getInstance().setFriend_choose_number(friend_choose_number);
                }
                else{
                    me_number=0;
                    me_checkbox.setChecked(false);
                    Global.getInstance().setMe_go(false);

                    Toast_Make("오프"+me_checkbox.isChecked());
                    friend_number.setText("총 "+ (friend_choose_number+me_number+how_number)+"명입니다");
                    Global.getInstance().setFriend_choose_number(friend_choose_number);
                }
            }
        });
        //me_checkbox.setChecked(true);
        find_nickname1.setFocusable(false);
        find_nickname1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.getInstance().setPage_check2(1); //뒤로돌아오기위한 빌드업
                Intent intent = new Intent(getActivity(),SearchActivity.class);
                getSearchResult.launch(intent);
                //FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                //fragment_sixteen=new Fragment_SixTeen();

                //transaction.replace(R.id.fragment_main, fragment_sixteen);
                //transaction.commit();
                //Dialog dialog=new Dialog(getActivity());
                //dialog.setContentView(R.layout.custom_layout5);
                //dialog.setCancelable(true);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //dialog.show();
                //Button close_button=dialog.findViewById(R.id.close_button);
                //WebView webview=dialog.findViewById(R.id.webView);

                /*close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });*/
            }
        });

        friend_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                how_number++;
                friend_nos.add(new Friend_no(how_number,find_nickname1.getText().toString()));

                find_nickname1.setText("");
                Global.getInstance().setFriend_nos(friend_nos);
                //Toast_Make(String.valueOf(friend_nos.size()));
                Global.getInstance().setAddress_number(how_number);
                Global.getInstance().setFriend_choose_number(friend_choose_number);
                friend_number.setText("총 "+ (friend_choose_number+me_number+how_number)+"명입니다");
                listView1.setAdapter(friend_no_adapter);
                setListViewHeightBasedOnItems(listView1);
            }
        });

        friend_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //약속하기에서 친구추가 갔다가 뒤로누르면 다시 약속잡기로 가야지 꺼지는거 방지하가위한 데이터전달
                Bundle bundle=new Bundle();
                bundle.putString("before_exist","yes");
                Global.getInstance().setPage_check3(1);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment_sixteen=new Fragment_SixTeen();
                fragment_sixteen.setArguments(bundle);
                transaction.replace(R.id.fragment_main, fragment_sixteen);
                transaction.commit();
            }
        });

        make_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(friend_choose_number==0&&how_number==0){ //나를제외하고 아무도 선택이 안되었을때
                    can_go_friend_choose=0;
                    Toast_Make("약속을 함께할 친구가 한명도 선택되지 않았습니다");
                    return;
                }
                if(friend_choose_number==-1){   //나까지도 해제되었을때 즉 총0명일때
                    can_go_friend_choose=0;
                    Toast_Make("아무도 선택되지 않았습니다");
                    return;
                }
                else{
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragment_fourteen=new Fragment_FourTeen();
                    transaction.replace(R.id.fragment_main, fragment_fourteen);
                    transaction.commit();
                }

            }
        });

        find_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String temp=find_nickname.getText().toString();
                friends.clear();
                clear=0;
                if(temp.length()==0){
                    friends.addAll(friends1);
                    friend_list_view.setAdapter(friendAdapter2);
                }
                else{
                    friends.clear();
                    for(int i=0;i<friends1.size();i++){
                        if(friends1.get(i).getName().toLowerCase().contains(temp)){
                            friends.add(friends1.get(i));
                            clear++;
                            friend_list_view.setAdapter(friendAdapter2);
                        }
                    }
                    if(clear==0){
                        friends.clear();
                        friend_list_view.setAdapter(friendAdapter2);
                    }
                    else{
                    }
                }
            }
        });
        find_nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                }
                else{
                    find_nickname.setText("");
                    for(int i=0;i<friends.size();i++){
                        Toast_Make(String.valueOf(friends.size()));
                        if(friends.get(i).getChecked()==true){
                            friend_choose_number--;
                        }
                    }
                }
            }
        });
        //friend_number.setText("총 "+ (friend_choose_number+me_number+how_number)+"명입니다");
        return view;
    }


    class FriendAdapter2 extends ArrayAdapter implements AdapterView.OnItemClickListener {

        private Context context;
        private List list;

        public FriendAdapter2(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
        }

        class ViewHolder {
            public ImageView image_url;
            public TextView name;
            public CheckBox checkbox;

        }

        public FriendAdapter2(Context context, ArrayList list){
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.item_friend1, parent, false);
            }

            viewHolder = new ViewHolder();
            viewHolder.image_url = (ImageView) convertView.findViewById(R.id.item_friend_imageview);
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_friend_textview);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox_friend_choose);


            final Friend test = (Friend) list.get(position);
            viewHolder.image_url.setImageResource(test.getImage_url());
            viewHolder.name.setText(test.getName());
            viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean newState=!((Friend) list.get(position)).isChecked();  //체크박스가 눌리때마다 체크여부 변환
                    ((Friend) list.get(position)).checked=newState;
                    if(((Friend)list.get(position)).isChecked()){
                        friend_choose_number++;
                        Global.getInstance().setFriend_choose_number(friend_choose_number);
                        friend_number.setText("총 "+ (friend_choose_number+me_number+how_number)+"명입니다");

                    }
                    else{
                        if(friend_choose_number>0) {
                            friend_choose_number--;
                        }
                        Global.getInstance().setFriend_choose_number(friend_choose_number);
                        friend_number.setText("총 "+ (friend_choose_number+me_number+how_number)+"명입니다");
                    }
                }
            });
            me_checkbox.setChecked(true);
            viewHolder.checkbox.setChecked(isChecked(position));    //현재 체크박스 상황

            return convertView;
        }
        private boolean isChecked(int position) {
            return((Friend) list.get(position)).checked;
        }
    }




    class Friend_no_Adapter extends ArrayAdapter implements AdapterView.OnItemClickListener {

        private Context context;
        private List list;
        public Friend_no_Adapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
        }

        class ViewHolder {
            public TextView where;
            public Button item_friend_delete;
        }

        public Friend_no_Adapter(Context context, ArrayList list){
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.item_friend2, parent, false);
            }

            viewHolder = new ViewHolder();
            viewHolder.where= (TextView) convertView.findViewById(R.id.item_friend_textview);
            viewHolder.item_friend_delete=(Button) convertView.findViewById(R.id.item_friend_delete);
            final Friend_no test = (Friend_no) list.get(position);
            viewHolder.item_friend_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    Global.getInstance().setFriend_nos(friend_nos);
                    //Toast_Make(String.valueOf(friend_nos.size()));
                    //friend_choose_number--;
                    if(how_number>0) {
                        how_number--;
                    }
                    Global.getInstance().setAddress_number(how_number);
                    Global.getInstance().setFriend_choose_number(friend_choose_number);
                    friend_number.setText("총 "+ (friend_choose_number+me_number+how_number)+"명입니다");
                    listView1.setAdapter(friend_no_adapter);
                    setListViewHeightBasedOnItems(listView1);
                }
            });
            viewHolder.where.setText(test.getWhere());
            return convertView;
        }

    }

}
