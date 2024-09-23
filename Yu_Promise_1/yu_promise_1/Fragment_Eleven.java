package com.example.last;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Fragment_Eleven extends Fragment implements OnBackPressedListener {
    private View view;
    private Fragment_Twelve fragment_twelve;
    private Button make_promise;
    Fragment_Main_Activity fragment_main_activity;
    long backKeyPressedTime;
    Toast toast;
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
        view=inflater.inflate(R.layout.fragment_eleven,container,false);
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        toast=Toast.makeText(getContext(),"한번더 뒤로가기 누르면 종료",Toast.LENGTH_SHORT);
        make_promise=(Button)view.findViewById(R.id.make_promise);
        make_promise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_Twelve1 fragment_twelve1=new Fragment_Twelve1();
                transaction.replace(R.id.fragment_main, fragment_twelve1);
                transaction.commit();
            }
        });
        return view;
    }
}