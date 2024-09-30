package com.example.last;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter4 extends BaseAdapter {

    private Context context;
    private List<Spinner1> spinnerList3;


    public SpinnerAdapter4(Context context, List<Spinner1> spinnerList3) {
        this.context = context;
        this.spinnerList3 = spinnerList3;
    }

    @Override
    public int getCount() {

        return spinnerList3 !=  null ? spinnerList3.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return spinnerList3.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.spinner_item, viewGroup, false);

        TextView spinner_text = rootView.findViewById(R.id.spinner_text);
        spinner_text.setText(spinnerList3.get(i).getName());
        return rootView;
    }
}