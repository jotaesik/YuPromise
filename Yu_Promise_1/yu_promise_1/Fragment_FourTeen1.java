package com.example.last;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class Fragment_FourTeen1 extends Fragment implements OnBackPressedListener{
    private View view;
    private Fragment_Eleven fragment_eleven;
    private Fragment_Twelve1 fragment_twelve1;
    private Button ok_promise,no_promise;
    private TextView we_where,we_where1;
    private String bundle_string,bundle_string1;
    private int promise_exist;
    Fragment_Main_Activity fragment_main_activity;
    @Override
    public void onBackPressed() {
        fragment_main_activity.setFragment_fourteen1();
    }
    @Override
    public void onResume() {
        super.onResume();
        fragment_main_activity.setOnBackPressedListener(this);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fourteen1, container, false);
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        //
        // promise_exist=Global.getInstance().getPromise_exist();
        //toast=Toast.makeText(getContext(),"한번더누르면종료",Toast.LENGTH_SHORT);
        ok_promise=(Button) view.findViewById(R.id.ok_promise);
        no_promise=(Button) view.findViewById(R.id.no_promise);
        we_where=(TextView)view.findViewById(R.id.we_where);
        we_where1=(TextView) view.findViewById(R.id.we_where1);
        if (getArguments() != null) //전달받은 데이터가 잇으면
        {
            bundle_string = getArguments().getString("we_where"); // 프래그먼트1에서 받아온 값 넣기
            we_where.setText("약속장소를 "+bundle_string+" 하시겠습니까?");
            bundle_string1=getArguments().getString("we_where1");
            we_where1.setText(bundle_string1);
        }
        ok_promise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //promise_exist++;
                //Global.getInstance().setPromise_exist(promise_exist);
                //Global.getInstance().setPromise_data("");
                //Global.getInstance().setPromise_time("");
                Bundle bundle=new Bundle(); //프레그먼트의 데이터 전달
                bundle.putString("we_where",bundle_string);
                //bundle.putString("we_where1", test1.getSecond());
                //Toast.makeText(getContext(),String.valueOf(Global.getInstance().getPromise_exist()),Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment_twelve1=new Fragment_Twelve1();
                fragment_twelve1.setArguments(bundle); //프레그먼트로 데이터전달
                transaction.replace(R.id.fragment_main, fragment_twelve1);
                transaction.commit();
                //약속과 연관된 친구들에게 메시지 보내기
                //스키마에 약속개수 만들어주는 변수추가
            }
        });
        no_promise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_FourTeen fragment_fourteen=new Fragment_FourTeen();
                transaction.replace(R.id.fragment_main, fragment_fourteen);
                transaction.commit();
            }
        });



        return view;
    }
}