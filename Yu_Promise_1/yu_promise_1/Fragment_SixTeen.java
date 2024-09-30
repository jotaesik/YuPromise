package com.example.last;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class Fragment_SixTeen extends Fragment implements OnBackPressedListener {
    private View view;
    private Button friend_add_button;

    private EditText edit_nickname;
    //private int friend_last_id;
    private Dialog dialog;
    private List<Friend> friends;
    ListView friend_list_view;
    FriendAdapter friendAdapter;
    FragmentTransaction ft;
    Fragment_Main_Activity fragment_main_activity;
    long backKeyPressedTime;
    Toast toast;
    @Override
    public void onBackPressed() {
        /*if (getArguments() != null)
        {
            String temp = getArguments().getString("before_exist"); // 약속잡기화면에서 누른 친구추가창인건가?
            fragment_main_activity.setFragment_sixteen();
            //Toast_Make("과거에서왓군요");
        }*/
        if(Global.getInstance().getPage_check3()==0){   //하단바를 통해 들어온케이스
            //Toast_Make("하단바 클릭해서 들어오셨군요");
            /*FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment_SixTeen fragment_sixteen=new Fragment_SixTeen();
            //fragment_sixteen.setArguments(bundle);
            transaction.replace(R.id.fragment_main, fragment_sixteen);
            transaction.commit();*/
            if(System.currentTimeMillis() > backKeyPressedTime + 2000){
                backKeyPressedTime = System.currentTimeMillis();
                toast.show();
                return;
            }
            if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
                getActivity().finish();
                fragment_main_activity.onShutDown();
                //getActivity().finish();
                toast.cancel();
            }
        }

        else if(Global.getInstance().getPage_check3()==1){//친구추가버튼을 통해 들어온케이스
            fragment_main_activity.setFragment_sixteen();
            Global.getInstance().setPage_check3(0);
            Global.getInstance().setMe_check(0);
            //Toast_Make(String.valueOf(Global.getInstance().getMe_check()));
            /*if(Global.getInstance().getMe_check()==0){
                //그냥 나를 터치를 한번도 안누름 default상태일때
                Global.getInstance().setMe_check(3);
            }
            else{
                Global.getInstance().setMe_check(-1);
            }*/
            /*else{
                Global.getInstance().setMe_check(-1);
            }*/


        }
        else{

        }

    }
    @Override
    public void onResume() {
        super.onResume();
        fragment_main_activity.setOnBackPressedListener(this);
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_sixteen,container,false);
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        toast=Toast.makeText(getContext(),"한번더 뒤로가기 누르면 종료",Toast.LENGTH_SHORT);
        edit_nickname=(EditText)view.findViewById(R.id.edit_nickname);
        friend_add_button=(Button) view.findViewById(R.id.friend_add_button);
        //friend_last_id=0;
        //Global.getInstance().setFriend_choose_number(-1);
        //Toast_Make(String.valueOf(Global.getInstance().getFriend_choose_number()));
        friends=Global.getInstance().getFriends();
        friend_list_view=(ListView)view.findViewById(R.id.listview);
        /*for(int i=0;i<friends.size();i++){
            Toast_Make(i+String.valueOf(friends.get(i).getChecked()));
        }*/
        //Toast_Make("전 화면 선택"+String.valueOf(Global.getInstance().getFriend_choose_number()));
        //
        //
        //
        // Toast_Make(String.valueOf(Global.getInstance().getFriend_choose_number()));
        friend_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"친구추가가되엇씁니다."+friend_last_id,Toast.LENGTH_SHORT).show();
                if(edit_nickname.getText().toString().equals("")){
                    Toast_Make("친구추가할 닉네임을 입력해주세요!!!");
                }
                else{
                    //닉네임이 존재하지 않을 경우도 넣어야함.
                    //Toast_Make(String.valueOf(friends.size()));
                    int add_fine=0;
                    //중복존재하는지 확인

                    for(int i=0;i<friends.size();i++){
                        if(edit_nickname.getText().toString().equals(friends.get(i).getName())){
                            Toast_Make("이미 같은 닉네임을 가진 친구가 존재합니다");
                            edit_nickname.setText("");
                            add_fine++;
                        }
                    }
                    if(add_fine==0){
                        int temp=Global.getInstance().getFriend_number();
                        //Toast_Make(String.valueOf(temp));
                        temp++;
                        //여기서 친구추가할사람의 이미지를 받아와야하는 코드 넣어야함
                        Friend friend=new Friend(temp,edit_nickname.getText().toString(),0,false);
                        Global.getInstance().friend_add(friend);
                        //그친구의 사진가져와야함
                        //friendAdapter.notifyDataSetChanged();
                        Toast_Make("추가되었습니다");
                        edit_nickname.setText("");
                        friend_list_view.setAdapter(new FriendAdapter(friends,view->{
                            final int index=(int)view.getTag();
                            //Toast_Make("흠"+String.valueOf(index));
                            friends.remove(index);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            Fragment_SixTeen fragment_sixteen=new Fragment_SixTeen();
                            transaction.replace(R.id.fragment_main, fragment_sixteen);
                            transaction.commit();
                        }));

                    }

                }
            }
        });
        friendAdapter=new FriendAdapter(friends,view->{ //그냥 적용

        });
        friend_list_view.setAdapter(new FriendAdapter(friends,view->{
            final int index=(int)view.getTag();
            //Toast_Make(String.valueOf(friends.get(index).getChecked()));
            boolean temp=friends.get(index).getChecked();
            if(temp==true){
                Toast_Make("약속할 친구를 삭제할순 없습니다!!");
            }
            else{
                Dialog dialog=new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_layout1);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                TextView second_title=dialog.findViewById(R.id.second_title);
                second_title.setText(friends.get(index).getName()+"를 삭제할까요?");
                Button yes_button=dialog.findViewById(R.id.yes_button);
                Button no_button=dialog.findViewById(R.id.no_button);
                yes_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friends.remove(index);
                        dialog.dismiss();
                        //Bundle bundle=new Bundle();
                        //bundle.putString("before_exist","yes");
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        Fragment_SixTeen fragment_sixteen=new Fragment_SixTeen();
                        //fragment_sixteen.setArguments(bundle);
                        transaction.replace(R.id.fragment_main, fragment_sixteen);
                        transaction.commit();
                        //뭔가 약속의 개수를 담당해주는 것이 잇어야할듯 그래야 취소를 눌럿을때 남아있는 약속의 개수가 0이면
                        //하나도 없는 약속메인으로가게디고 약속이 남아잇으면 약속이 있는 메인화면으로가니

                        //transaction.replace(R.id.fragment_main,fragment_fifthteen);
                        //transaction.commit();
                        //Toast.makeText(getContext(),"성공",Toast.LENGTH_SHORT).show();
                    }
                });
                no_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
            //Toast_Make("흠"+String.valueOf(index));


            //fragment_main_activity.setFragment_sixteen();


            //약속하기에서 친구추가 갔다가 뒤로누르면 다시 약속잡기로 가야지 꺼지는거 방지하가위한 데이터전달

        }));

        return view;
    }


}
