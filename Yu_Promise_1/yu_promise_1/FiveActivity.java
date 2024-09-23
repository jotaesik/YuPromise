package com.example.last;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.Iterator;

public class FiveActivity extends AppCompatActivity {
    /*@Override //필요없음
    public void onBackPressed() {
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(),FourActivity.class);
        startActivity(intent);
    }*/
    private final int GET_GALLERY_IMAGE=200;    //리퀘스트 값 비교용
    private ImageView imageView;
    private TextView result_nickname;
    private EditText edit_nickname;
    private Uri selectedImageUri;       //uri 리소스에 접근할수있는 식별자 선언 image 가져오기 위해서
    int can_go_next_nickname;//다음패이지로넘어갈수있는지?
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
                if (edit_nickname.getText().toString().equals(child.next().child("nickname").getValue().toString())) {
                    user_list.removeEventListener(this);
                    result_nickname.setText("같은 닉네임이 존재합니다.");
                    return;
                }
            }
            result_nickname.setText("사용 가능한 닉네임입니다.");
            can_go_next_nickname=1;
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {  //savedInstanceState는 activity이전상태 저장
        can_go_next_nickname=0;
        super.onCreate(savedInstanceState); //super class호출
        setContentView(R.layout.five);
        selectedImageUri = Uri.parse("android.resource://"+R.class.getPackage().getName()+"/"+R.drawable.p_person);
        result_nickname=findViewById(R.id.result_nickname);
        edit_nickname=findViewById(R.id.edit_nickname);
        edit_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {    //입력하기전에
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {   //입력하는곳에 변화있을때 발생
                //닉내임 중복 코드 추가 nickname_exist_check(String input_nickname)이 함수 호출
                can_go_next_nickname=0;
                if(edit_nickname.getText().toString().equals("")){
                    result_nickname.setText("");
                }
                else{
                    user_list.addListenerForSingleValueEvent(checkRegister);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {  //입력이끝낫을때
            }
        });
        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(can_go_next_nickname == 1){
                    FirebaseStorage.getInstance().getReference().child(
                            "profile_picture/" + getIntent().getExtras().getString("user_id") +".png").putFile(selectedImageUri);
                    Intent intent = new Intent(getApplicationContext(), SixActivity.class);
                    intent.putExtra("user_id", getIntent().getExtras().getString("user_id"));
                    intent.putExtra("user_pw", getIntent().getExtras().getString("user_pw"));
                    intent.putExtra("user_nickname", edit_nickname.getText().toString());
                    startActivity(intent);
                }
                else{
                    Toast_Make("닉네임을 확인해주세요.");
                    return;
                }
            }
        });
        imageView=(ImageView) findViewById(R.id.profile_image);
        imageView.setClipToOutline(true);
        imageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(Intent.ACTION_PICK);   //앨범호출 인텐트
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");  //데이터 uri설정과 mime유형 설정
                //외부사진  //데이터타입 image 모든 image타입가능
                startActivityForResult(intent,GET_GALLERY_IMAGE);   //액티비를 열어주고 결과값 전달
                //acitivty 식별값 Gallery_image
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {   //startacitivityforresult에서 결과값 받는곳
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //리퀘스트코드가 일치하는지 리절트코드가 성공값인지 데이터가 빈값이 아닌지
            selectedImageUri = data.getData();  //선택한 데이터의 경로 구한 다음 저장
            imageView.setImageURI(selectedImageUri);
        }
        else{

        }
    }

}
