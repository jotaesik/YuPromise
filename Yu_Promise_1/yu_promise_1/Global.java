package com.example.last;

import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Global {
    private String user_id;
    private String user_pw;
    private String user_nickname;
    private String user_phoneNo;
    private int promise_exist;  //약속개수
    private int friend_number;  //친구수
    private int friend_choose_number=0; //약속을 함께할 친구수
    private int alarm_switch_value=0;
    private int jindong_switch_value=0;
    private int address_number=0;
    private boolean me_go=true;
    private double longitude;
    private String now_year,now_month,now_day,now_hour,now_minute;
    public String getNow_year(){
        return now_year;
    }
    public String getNow_month(){
        return now_month;
    }
    public String getNow_day(){
        return now_day;
    }
    public String getNow_hour(){
        return now_hour;
    }
    public String getNow_minute(){
        return now_minute;
    }
    public void setNow_year(String now_year){
        this.now_year=now_year;
    }
    public void setNow_month(String now_month){
        this.now_month=now_month;
    }
    public void setNow_day(String now_day){
        this.now_day=now_day;
    }
    public void setNow_hour(String now_hour){
        this.now_hour=now_hour;
    }
    public void setNow_minute(String now_minute){
        this.now_minute=now_minute;
    }
    public double getLongitude(){
        return longitude;
    }
    public void setLongitude(double longitude){
        this.longitude=longitude;
    }
    public double getLatitude(){
        return latitude;
    }
    public void setLatitude(double latitude){
        this.latitude=latitude;
    }
    private double latitude;
    //페이지이동할때 백버튼 사용시
    private int page_check=0;
    private int page_check1=0;
    private int page_check2=0;
    private int page_check3=0;
    private int me_check=0; //나의 체크여부
    private List<Message> messages;
    private ArrayList<Friend>friends;
    private ArrayList<Friend_no> friend_nos;
    private List<String> promise_list = new ArrayList<>();
    private List<String> friends_list = new ArrayList<>();
    private boolean message_check;
    private int message_position=-1;
    private DatabaseReference myPostReference;
    private Global(){
        messages=new ArrayList<>();
        for(int i=1;i<10;i++){
            if(i%3==0){
                Message message=new Message(i,"메시지",0,0);
                messages.add(message);
            }
            else if(i%3==1){
                Message message=new Message(i,"약속메시지",1,0);
                messages.add(message);
            }
            else{
                Message message=new Message(i,"친구메시지",2,0);
                messages.add(message);
            }
        }
        friends=new ArrayList<>();
        for(int i=0;i<5;i++){
                Friend friend=new Friend(i,i+"친구",0,false);
                friends.add(friend);
                friend_number++;
        }
        friend_nos=new ArrayList<>();
    }
    public void setMe_go(boolean me_go){
        this.me_go=me_go;
    }
    public boolean getMe_go(){
        return me_go;
    }
    public void setAddress_number(int address_number){
        this.address_number=address_number;
    }
    public int getAddress_number(){
        return address_number;
    }
    public void setAlarm_switch_value(int alarm_switch_value){
        this.alarm_switch_value=alarm_switch_value;
    }
    public int getAlarm_switch_value(){
        return alarm_switch_value;
    }
    public void setJindong_switch_value(int jindong_switch_value){
        this.jindong_switch_value=jindong_switch_value;
    }
    public String getUser_id() {return user_id;}
    public void setUser_id(String user_id) {this.user_id = user_id;}
    public String getUser_pw() {return user_pw;}
    public void setUser_pw(String user_pw) {this.user_pw = user_pw;}
    public String getUser_nickname() {return user_nickname;}
    public void setUser_nickname(String user_nickname) {this.user_nickname = user_nickname;}
    public String getUser_phoneNo() {return user_phoneNo;}
    public void setUser_phoneNo(String user_phoneNo) {this.user_phoneNo = user_phoneNo;}

    private FirebaseDatabase mydatabase = FirebaseDatabase.getInstance();
    private DatabaseReference user_list = mydatabase.getReference("User_list");
    private ValueEventListener checkRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            while (child.hasNext()) {
                DataSnapshot current_child = child.next();
                if (user_id.equals(current_child.getKey())) {
                    user_pw = current_child.child("pw").getValue().toString();
                    user_nickname = current_child.child("nickname").getValue().toString();
                    user_phoneNo = current_child.child("phoneNo").getValue().toString();
                    Iterator<DataSnapshot> promises = current_child.child("promises").getChildren().iterator();
                    Iterator<DataSnapshot> friends = current_child.child("friends").getChildren().iterator();
                    while (promises.hasNext()) {
                        promise_list.add(promises.next().getKey());
                    }
                    while (promises.hasNext()) {
                        friends_list.add(friends.next().getKey());
                    }
                    setPromise_exist();
                    user_list.removeEventListener(this);
                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
    public void setAll() {
        user_list.addListenerForSingleValueEvent(checkRegister);
    }
    public void updateDB() {
        myPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        AssignPost post = new AssignPost(user_pw, user_nickname, user_phoneNo);
        postValues = post.toMap();
        childUpdates.put("/User_list/" + user_id, postValues);
        myPostReference.updateChildren(childUpdates);
    }
    public int getJindong_switch_value(){
        return jindong_switch_value;
    }
    public void setMe_check(int me_check){
        this.me_check=me_check;
    }
    public int getMe_check(){
        return me_check;
    }
    public void setPage_check(int page_check){
        this.page_check=page_check;
    }
    public int getPage_check(){
        return page_check;
    }
    public void setPage_check1(int page_check1){
        this.page_check1=page_check1;
    }
    public int getPage_check1(){
        return page_check1;
    }
    public void setPage_check2(int page_check2){
        this.page_check2=page_check2;
    }
    public int getPage_check2(){
        return page_check2;
    }
    public void setPage_check3(int page_check3){
        this.page_check3=page_check3;
    }
    public int getPage_check3(){
        return page_check3;
    }
    public void friend_add(Friend friend){
        friends.add(friend);
    }
    public void friend_no_add(Friend_no friend_no){
        friend_nos.add(friend_no);
    }
    public List<Message> getMessages(){
        return messages;
    }
    public ArrayList<Friend> getFriends(){
        return friends;
    }
    public ArrayList<Friend_no>getFriend_nos(){return friend_nos;}
    public void setMessages(List <Message>messages){
        this.messages=messages;
    }
    public void setFriends(ArrayList<Friend>friends){
        this.friends=friends;
    }
    public void setFriend_nos(ArrayList<Friend_no>friend_nos){this.friend_nos=friend_nos;}
    private String promise_time="";
    private String promise_date="";
    public void setPromise_exist(int promise_exist){
        this.promise_exist=promise_exist;
    }
    public void setMessage_check(boolean message_check){
        this.message_check=message_check;
    }
    public boolean getMessage_check(){
        return message_check;
    }
    public void setMessage_position(int message_position){
        this.message_position=message_position;
    }
    public void setFriend_choose_number(int friend_choose_number){
        this.friend_choose_number=friend_choose_number;
    }
    public void setFriend_number(int friend_number) {
        this.friend_number = friend_number;
    }
    public int getFriend_number(){
        return friend_number;
    }
    public int getMessage_position(){
        return message_position;
    }
    public void setPromise_data(String promise_date){
        this.promise_date=promise_date;
    }
    public void setPromise_time(String promise_time){
        this.promise_time=promise_time;
    }
    public String getPromise_time(){
        return promise_time;
    }
    public String getPromise_date(){
        return promise_date;
    }
    public void setPromise_exist(){
        promise_exist = promise_list.size();
    }
    public int getPromise_exist(){
        return promise_exist;
    }
    public int getFriend_choose_number(){

        return friend_choose_number;
    }

    private static Global instance=null;
    public static synchronized Global getInstance(){
        if(instance==null){
            instance=new Global();
        }
        return instance;
    }
}
