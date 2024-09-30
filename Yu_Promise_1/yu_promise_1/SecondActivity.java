package com.example.last;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class SecondActivity extends AppCompatActivity {
    private CheckBox login_checkbox;
    private Button not_login_user;
    private EditText some_id,some_pw;
    public static Context mContext;

    public void Toast_Make(String message){
        LayoutInflater inflater=getLayoutInflater();    //레이아웃 인플레이터 객체 참조 뷰 사용하기위함
        View layout=inflater.inflate(R.layout.toast_design_layout,(ViewGroup) findViewById(R.id.toast_design_layout_main));
        //토스트를위한 레이아웃 인플레이트
        Toast toast=Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
        TextView textView=layout.findViewById(R.id.toast_textview);
        textView.setText(message);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(layout);  //토스트 뷰 설정
        toast.show();
    }

    private FirebaseDatabase mydatabase = FirebaseDatabase.getInstance();
    private DatabaseReference user_list = mydatabase.getReference("User_list");
    private ValueEventListener checkRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            while (child.hasNext()) {
                DataSnapshot current_child = child.next();
                if (some_id.getText().toString().equals(current_child.getKey())) {
                    if(some_pw.getText().toString().equals(current_child.child("pw").getValue().toString())){
                        Intent intent = new Intent(getApplicationContext(), Fragment_Main_Activity.class);
                        intent.putExtra("user_id", some_id.getText().toString());
                        startActivity(intent);
                    }
                    else{
                        Toast_Make("비밀번호가 틀렸습니다.");
                    }
                    user_list.removeEventListener(this);
                    return;
                }
            }
            Toast_Make("아이디가 존재하지 않습니다.");
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    public void login(){
        PreferenceManager.setString(mContext, "id", some_id.getText().toString());
        PreferenceManager.setString(mContext, "pw", some_pw.getText().toString());
        user_list.addListenerForSingleValueEvent(checkRegister);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        mContext = this;

        login_checkbox=(CheckBox) findViewById(R.id.login_checkbox);
        some_id=(EditText)findViewById(R.id.some_id);
        some_pw=(EditText)findViewById(R.id.some_pw);
        some_id.setText("");
        some_pw.setText("");

        login_checkbox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    PreferenceManager.setString(mContext, "id", some_id.getText().toString());
                    PreferenceManager.setString(mContext, "pw", some_pw.getText().toString());
                    PreferenceManager.setBoolean(mContext, "check", login_checkbox.isChecked());
                }
                else {
                    PreferenceManager.setBoolean(mContext, "check", login_checkbox.isChecked());
                    PreferenceManager.clear(mContext); //로그인 정보 삭제
                }
            }
        }) ;

        boolean isChecked = PreferenceManager.getBoolean(mContext,"check");
        if(isChecked){ //
            some_id.setText(PreferenceManager.getString(mContext, "id"));
            some_pw.setText(PreferenceManager.getString(mContext, "pw"));
            login_checkbox.setChecked(true);
            login();
        }

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( SecondActivity.this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
        }
        else{
            // 가장최근 위치정보 가져오기
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double altitude = location.getAltitude();
                Global.getInstance().setLatitude(latitude);
                Global.getInstance().setLongitude(longitude);
            }

            // 위치정보를 원하는 시간, 거리마다 갱신해준다.
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }

        //자동 로그인 체크박스 구현들어가기
        SharedPreferences appData = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
        String loginId = appData.getString("inputID", null);
        String loginPwd = appData.getString("inputPW", null);

        not_login_user=(Button) findViewById(R.id.not_login_user);
        not_login_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Not_Login_User_Activity.class);
                startActivity(intent);
            }
        });
        int promise_exist=0; //약속이 있다 1 약속이 없다 0
        Button imageButton = (Button) findViewById(R.id.first_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThreeActivity.class);
                startActivity(intent);
            }
        });
        Button login_Button = (Button) findViewById(R.id.login_button);
        login_Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {    //여기에다가 사용자 아이디,비밀번호 db에 존재하는지 확인하기

                if (some_id.getText().toString().equals("") || some_pw.getText().toString().equals("")){
                    Toast_Make("아이디/비밀번호를 입력해주세요");
                }
                else {
                    login();
                }
            }
        });
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
            String provider = location.getProvider();  // 위치정보
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도
            double altitude = location.getAltitude(); // 고도
            Global.getInstance().setLatitude(latitude);
            Global.getInstance().setLongitude(longitude);
        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };

}