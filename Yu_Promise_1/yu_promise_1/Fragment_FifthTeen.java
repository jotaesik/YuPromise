package com.example.last;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;
//사용안하는 자바
public class Fragment_FifthTeen extends Fragment implements OnBackPressedListener{

    private View view;
    private Button cancel_promise,make_promise;
    private Fragment_Twelve1 fragment_twelve1;
    private Fragment_Eleven fragment_eleven;
    private SpinnerAdapter1 adapter;
    private Spinner spinner1;
    Fragment_Main_Activity fragment_main_activity;
    long backKeyPressedTime;
    Toast toast;
    private int promise_exist;
    @Override
    public void onBackPressed() {
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
    @Override
    public void onResume() {
        super.onResume();
        fragment_main_activity.setOnBackPressedListener(this);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_fifthteen,container,false);
        //default는 가장 가까운 약속으로 맵 설정
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment_FifthTeen fragment_fifthteen=new Fragment_FifthTeen();
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        toast=Toast.makeText(getContext(),"한번더 뒤로가기 누르면 종료",Toast.LENGTH_SHORT);
        cancel_promise=(Button) view.findViewById(R.id.cancel_promise);
        make_promise=(Button)view.findViewById(R.id.make_promise);
        List<Spinner1> spinnerList = new ArrayList<>();
        String []promise_title=new String[]{"첫번째","두번째","세번째","네번째","다섯번째"};
        for(int i=0;i< Global.getInstance().getPromise_exist();i++){
            Spinner1 test=new Spinner1();
            test.setName(promise_title[i]);
            spinnerList.add(test);
        }
        spinner1=(Spinner) view.findViewById(R.id.promise_title);
        adapter=new SpinnerAdapter1(getActivity(),spinnerList);
        spinner1.setAdapter(adapter);
        long a=spinner1.getSelectedItemPosition();
        //a따른 프레그먼트 변경 넣어야함
        fragment_twelve1=new Fragment_Twelve1();
        fragment_eleven=new Fragment_Eleven();
        promise_exist=Global.getInstance().getPromise_exist();
        make_promise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment_twelve1=new Fragment_Twelve1();
                transaction.replace(R.id.fragment_main, fragment_twelve1);
                transaction.commit();
            }
        });
        cancel_promise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_layout4);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button yes_button=dialog.findViewById(R.id.yes_button);
                Button no_button=dialog.findViewById(R.id.no_button);
                yes_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        //뭔가 약속의 개수를 담당해주는 것이 잇어야할듯 그래야 취소를 눌럿을때 남아있는 약속의 개수가 0이면
                        //하나도 없는 약속메인으로가게디고 약속이 남아잇으면 약속이 있는 메인화면으로가니
                        promise_exist--;
                        Toast.makeText(getContext(),String.valueOf(promise_exist),Toast.LENGTH_SHORT).show();
                        Global.getInstance().setPromise_exist(promise_exist);
                        if(0==Global.getInstance().getPromise_exist()){
                            fragment_eleven=new Fragment_Eleven();
                            transaction.replace(R.id.fragment_main, fragment_eleven);
                        }
                        else{
                            transaction.replace(R.id.fragment_main, fragment_fifthteen);
                        }
                        transaction.commit();
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
        });

        return view;
    }
}