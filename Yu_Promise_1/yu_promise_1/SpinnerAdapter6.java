package com.example.last;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter6 extends BaseAdapter {

    private Context context;
    private List<Spinner1> spinnerList5;


    public SpinnerAdapter6(Context context, List<Spinner1> spinnerList5) {
        this.context = context;
        this.spinnerList5 = spinnerList5;
    }

    @Override
    public int getCount() {

        return spinnerList5!=  null ? spinnerList5.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return spinnerList5.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.spinner_item, viewGroup, false);

        TextView spinner_text = rootView.findViewById(R.id.spinner_text);
        spinner_text.setText(spinnerList5.get(i).getName());
        return rootView;
    }
}