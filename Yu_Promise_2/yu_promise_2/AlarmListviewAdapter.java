package com.example.yupromise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AlarmListviewAdapter extends BaseAdapter {
    public ArrayList<AlarmItem> alarmItems;

    AlarmListviewAdapter(ArrayList<AlarmItem> alarmItems) {
        this.alarmItems = alarmItems;
    }

    @Override
    public int getCount() {
        return alarmItems.size();
    }

    @Override
    public Object getItem(int position) {
        return alarmItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final AlarmItem alarmItem = alarmItems.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.alarm_item, parent, false);
        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        TextView date = convertView.findViewById(R.id.date);
        date.setText(alarmItem.getYear() + "년 " + alarmItem.getMonth() + "월 " + alarmItem.getDay() + "일");
        TextView time = convertView.findViewById(R.id.time);
        time.setText(alarmItem.getHour() + "시 " + alarmItem.getMin() + "분");
        TextView content = convertView.findViewById(R.id.content);
        content.setText(alarmItem.getContent());
        return convertView;
    }
}
