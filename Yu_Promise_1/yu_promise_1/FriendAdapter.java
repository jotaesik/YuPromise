package com.example.last;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends BaseAdapter {
    private List<Friend> friends;
    private View.OnClickListener onButtonDeleteClicked;
    public FriendAdapter(List<Friend> friends,View.OnClickListener onButtonDeleteClicked) { //생성자 선언
        this.friends=friends;
        this.onButtonDeleteClicked = onButtonDeleteClicked;
    }
    @Override
    public int getCount() {
        return friends.size();
    }
    @Override
    public Object getItem(int i) {
        return  friends.get(i);
    }
    @Override
    public long getItemId(int i) {
        return  friends.get(i).getId();
    }



    static class ViewHolder {
        public TextView name;
        public ImageView image_url;
        public Button item_friend_delete;
        public CheckBox checkbox;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_friend_textview);
            viewHolder.image_url = (ImageView) convertView.findViewById(R.id.item_friend_imageview);
            viewHolder.item_friend_delete=(Button) convertView.findViewById(R.id.item_friend_delete);
            viewHolder.item_friend_delete.setOnClickListener(onButtonDeleteClicked);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag(); //일일이 findviewid하지않고 뷰홀더로 통제
        }

        Friend friend = friends.get(position);
        viewHolder.item_friend_delete.setTag(position);
        //viewHolder.image_url.setImageResource(friend.getImage_url());
        viewHolder.image_url.setImageResource(R.drawable.circle);
        viewHolder.name.setText(friend.getName());
        return convertView;
    }

}