package com.example.last;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter3 extends BaseAdapter {

    private Context context;
    private List<Spinner1> spinnerList2;


    public SpinnerAdapter3(Context context, List<Spinner1> spinnerList1) {
        this.context = context;
        this.spinnerList2 = spinnerList1;
    }

    @Override
    public int getCount() {

        return spinnerList2 !=  null ? spinnerList2.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return spinnerList2.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.spinner_item, viewGroup, false);

        TextView spinner_text = rootView.findViewById(R.id.spinner_text);
        spinner_text.setText(spinnerList2.get(i).getName());
        return rootView;
    }
}