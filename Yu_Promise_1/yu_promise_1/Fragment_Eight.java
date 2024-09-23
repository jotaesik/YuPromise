package com.example.last;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Fragment_Eight extends Fragment implements OnBackPressedListener { //인터페이스 내포 프레그먼트 포함
    private View view;
    private Button make_promise,make_friend;
    private Fragment_Twelve1 fragment_twelve1;    //프레그먼트 리플레이스 위하여 선언
    private Fragment_SixTeen fragment_sixteen;
    //private AllOfActivity allOfActivity;
    Fragment_Main_Activity fragment_main_activity;  //프레그먼트 메인 선언
    long backKeyPressedTime;    //뒤로가기 눌렀을때 종료 확인함수
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
    @Override
    public void onBackPressed() {       //오버라이딩 fragment_main_activity
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){     //뒤로가기 버튼 제한
            backKeyPressedTime = System.currentTimeMillis();
            Toast_Make("한번더 뒤로가기 누르면 종료");
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            getActivity().finish(); //fragment_main_acitivty 호출해서 finish호출시 fragment_main_activity 종료
            fragment_main_activity.onShutDown();    //fragment_main_Acitivty 온셧다운함수 호출
            //getActivity().finish();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        fragment_main_activity.setOnBackPressedListener(this);
    }
    @Nullable   //null이 될수 있는 매개변수
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //oncreate 후에 화면 구성 nonnull null이 될수없는 매개변수
        view=inflater.inflate(R.layout.fragment_eight,container,false); //화면올리기 fragment_eight view의 부모는 container
        // view자식추가 false
        //allOfActivity=(AllOfActivity)getContext();
        //allOfActivity.setPromise_exist(1);
        //Toast_Make(String.valueOf(allOfActivity.getPromise_exist()));
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        make_promise=(Button) view.findViewById(R.id.make_promise);
        make_friend=(Button)view.findViewById(R.id.make_friend);
        make_promise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                //getacitvity로 framgent_main_Acitvity받고 프래그먼트매니저 참조확득하고 트랜스픽션시작
                fragment_twelve1=new Fragment_Twelve1();
                Global.getInstance().setPage_check(1);
                transaction.replace(R.id.fragment_main, fragment_twelve1); //프래그먼트 화면대체
                BottomNavigationView bottom_menu=getActivity().findViewById(R.id.bottom_menu);
                bottom_menu.setSelectedItemId(R.id.second_tab); //약속정하기를 눌렀을때 하단바 변경
                transaction.commit();   //작업완료
            }
        });
        make_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment_sixteen=new Fragment_SixTeen();
                transaction.replace(R.id.fragment_main, fragment_sixteen);
                BottomNavigationView bottom_menu=getActivity().findViewById(R.id.bottom_menu);
                bottom_menu.setSelectedItemId(R.id.first_tab);
                transaction.commit();
            }
        });

        return view;
    }


}