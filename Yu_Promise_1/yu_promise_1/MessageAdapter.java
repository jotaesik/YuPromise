package com.example.last;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private List<Message> messages;
    private View.OnClickListener onButtonDeleteClicked;
    public MessageAdapter(List<Message> messages,View.OnClickListener onButtonDeleteClicked) { //생성자 선언
        this.messages = messages;
        this.onButtonDeleteClicked = onButtonDeleteClicked;
    }
    @Override
    public int getCount() {
        return messages.size();
    }
    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }
    @Override
    public long getItemId(int i) {
        return messages.get(i).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {  //처음 비어잇을때만
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.message_title = convertView.findViewById(R.id.message_title);
            viewHolder.message_delete_button = convertView.findViewById(R.id.message_delete_button );
            viewHolder.message_delete_button.setOnClickListener(onButtonDeleteClicked);
            viewHolder.image1=convertView.findViewById(R.id.image1);
            viewHolder.image2=convertView.findViewById(R.id.image2);
            convertView.setTag(viewHolder); //적용해주므로 다음부턴 else부분으로
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag(); //일일이 findviewid하지않고 뷰홀더로 통제
        }



        Message message =messages.get(position);
        viewHolder.message_title.setText(message.getTitle());
        viewHolder.message_delete_button.setTag(position);
        if(message.getTitle().equals("친구메시지")){
            viewHolder.message_delete_button.setVisibility(View.INVISIBLE);
            viewHolder.image1.setImageResource(R.drawable.friend_image);
            viewHolder.image2.setImageResource(R.drawable.friend_image);

        }
        else if(message.getTitle().equals("약속메시지")){
            viewHolder.message_delete_button.setVisibility(View.INVISIBLE);
            viewHolder.image1.setImageResource(R.drawable.hand1);
            viewHolder.image2.setImageResource(R.drawable.hand1);
        }
        else{
            viewHolder.message_delete_button.setVisibility(View.VISIBLE);
            viewHolder.image1.setImageResource(R.drawable.message_image);
            viewHolder.image2.setImageResource(R.drawable.message_image);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView message_title;
        Button message_delete_button;
        ImageView image1;
        ImageView image2;
    }
}
