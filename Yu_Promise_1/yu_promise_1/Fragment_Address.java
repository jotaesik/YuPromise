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
import android.webkit.WebView;
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
//사용안하는 파일
public class Fragment_Address extends Fragment implements OnBackPressedListener { //인터페이스 내포 프레그먼트 포함
    private View view;
    private WebView webView;
    Fragment_Main_Activity fragment_main_activity;

    @Override
    public void onBackPressed() {
        if(Global.getInstance().getPage_check2()==1){
            fragment_main_activity.setFragment_address1();
            Global.getInstance().setPage_check2(0);
        }
        else{
            fragment_main_activity.setFragment_address();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        fragment_main_activity.setOnBackPressedListener(this);  //프레그먼트의 포함하는 액티비티에서의 리스너 적용
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
    @Nullable   //null이 될수 있는 매개변수
    @Override
    //화면을 구성할때 호출되는 부분
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_address,container,false);
        //부모 뷰그룹에 자식으로 안들어감. 객체화
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        webView=(WebView)view.findViewById(R.id.webView);
        //Toast_Make("성공도착");
        return view;
    }



}