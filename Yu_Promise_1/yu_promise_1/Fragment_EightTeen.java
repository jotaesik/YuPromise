package com.example.last;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.regex.Pattern;

public class Fragment_EightTeen extends Fragment implements OnBackPressedListener{
    private View view;
    private final int GET_GALLERY_IMAGE=200;
    private Dialog dialog;
    private Button remake_button;
    private int can_password_change;
    private TextView my_id, text_result_id,check_password0,check_password1,result_number;
    //private CheckBox checkbox_password;
    private EditText remake_edit_password0,remake_edit_password1,remake_edit_number,text_nickname;
    int can_go_password,can_go_password1,can_go_number,can_go_next_image;
    int exist_promise;
    private ImageView my_image;
    private String user_id;
    Uri selectedImageUri;
    private Fragment_SevenTeen fragment_seventeen;
    Fragment_Main_Activity fragment_main_activity;
    public void Toast_Make(String message){
        LayoutInflater inflater=getLayoutInflater();    //레이아웃 인플레이터 객체 참조 뷰 사용하기위함
        View layout=inflater.inflate(R.layout.toast_design_layout,(ViewGroup) getActivity().findViewById(R.id.toast_design_layout_main));
        //토스트를위한 레이아웃 인플레이트
        Toast toast=Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
        TextView textView=layout.findViewById(R.id.toast_textview);
        textView.setText(message);
        toast.setGravity(Gravity.BOTTOM,0,500);
        toast.setView(layout);  //토스트 뷰 설정
        toast.show();
    }
    public void set_can_go_next(){  //초기화시키기
        can_go_password =2;
        can_go_number=2;
        can_go_next_image=0;
        can_password_change=0;
    }

    public void my_profile_set(){   //첫화면 띄웠을때 비밀번호 변경과 비밀번호 변경확인 포커스 꺼놓기
        //데이터에서 불러오기 -->코드추가하기
        /*example
         my_id.setText("");
         text_nickname.setText("");
         my_image.setImageURI();//이거는 이미지상황에 맞게
         */
        remake_edit_password0.setFocusableInTouchMode(false);
        remake_edit_password1.setFocusableInTouchMode(false);
        remake_edit_password0.setFocusable(false);
        remake_edit_password1.setFocusable(false);
    }
    @Override
    public void onBackPressed() {
        fragment_main_activity.setFragment_eightteen();
    }   //메인프레그먼트의 함수 호출 백버튼호출시
    @Override
    public void onResume() {
        super.onResume();
        fragment_main_activity.setOnBackPressedListener(this);
    }
    public boolean check_user_password(String input_second_password){   //내가입력한 비밀번호2차가 내 비밀번호와 맞는지->코드추가하기
        //맞으면
        return true;
        //틀리면 return false;
    }
    public boolean check_user_number(String input_number){  //내가 변경한 전화번호가 존재하는지 안하는지
        //맞으면
        return true;
        //틀리면 return false;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_eightteen,container,false);
        my_id=(TextView)view.findViewById(R.id.my_id);
        //checkbox_password=(CheckBox)view.findViewById(R.id.checkbox_password);
        text_nickname=(EditText) view.findViewById(R.id.text_nickname);
        remake_edit_password0=(EditText)view.findViewById(R.id.remake_edit_password0);
        remake_edit_password1=(EditText)view.findViewById(R.id.remake_edit_password1);
        remake_edit_number=(EditText)view.findViewById(R.id.remake_edit_number);
        text_result_id=(TextView)view.findViewById(R.id.text_result_id);
        check_password0=(TextView)view.findViewById(R.id.check_password0);
        check_password1=(TextView)view.findViewById(R.id.check_password1);
        result_number=(TextView)view.findViewById(R.id.result_number);
        my_image=(ImageView)view.findViewById(R.id.my_image);

        remake_button=(Button)view.findViewById(R.id.remake_button);
        user_id = Global.getInstance().getUser_id();
        my_id.setText(user_id);
        remake_edit_number.setText(Global.getInstance().getUser_phoneNo());

        result_number.setText("");
        check_password0.setText("");
        check_password1.setText("");
        my_profile_set();   //나의 이미지와 아이디와 닉네임 set 그리고 추가로 체크박스가 되지않으면 비밀번호변경과 비밀변호변경확인 입력x
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        set_can_go_next();
        //toast=Toast.makeText(getContext(),"한번더누르면종료",Toast.LENGTH_SHORT);

        FirebaseStorage.getInstance().getReference().child("profile_picture/" + user_id +".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Fragment_EightTeen.this).load(uri).into(my_image);
            }
        });
        my_image.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,GET_GALLERY_IMAGE);
            }
        });

        remake_edit_password0.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {   //View를 Checkbox형으로 변환한다음 함수호출 체크여부 확인
                if(can_password_change==0){
                    dialog=new Dialog(getActivity());
                    dialog.setContentView(R.layout.custom_layout);
                    dialog.setCancelable(false);    //팝업창이 켜지면 팝업창바깥을 눌러도 꺼지지 않도록
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //팝업창 배경 투명하게하기
                    dialog.show();
                    Button yes_button=dialog.findViewById(R.id.yes_button);
                    EditText first_edit=dialog.findViewById(R.id.first_edit);
                    yes_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String input_second_password=first_edit.getText().toString();
                            if(input_second_password.equals(Global.getInstance().getUser_pw())){   //여기에 본인 비밀번호 잇어야할듯
                                //check_user_password(input_second_password); <--이 함수호출하기 추가로 작성해서
                                Toast_Make("변경할 비밀번호를 입력해주세요.");
                                remake_edit_password0.setHint("비밀번호 입력");

                                check_password0.setText("비밀번호는 대,소문자와 숫자 그리고 특수문자로만 이루어져야하고 6~10자리이어야 합니다");
                                check_password1.setText("");
                                //이제 작성가능하게 하기
                                remake_edit_password0.setFocusableInTouchMode(true);
                                remake_edit_password1.setFocusableInTouchMode(true);
                                remake_edit_password0.setFocusable(true);
                                remake_edit_password1.setFocusable(true);
                                dialog.dismiss();
                                can_password_change=1;  //화면을 벗어가기전까진 여러번 수정가능
                            }
                            else if(input_second_password.equals("")){
                                Toast_Make("비밀번호가 입력되지 않았습니다.");
                            }
                            else{
                                Toast_Make("비밀번호가 틀립니다.");
                            }
                        }
                    });
                    Button no_button=dialog.findViewById(R.id.no_button);
                    no_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //비밀번호 입력확인이 안되었으니 여전히 잠굼
                            remake_edit_password0.setFocusableInTouchMode(false);
                            remake_edit_password1.setFocusableInTouchMode(false);
                            remake_edit_password0.setFocusable(false);
                            remake_edit_password1.setFocusable(false);
                            dialog.dismiss();
                            //checkbox_password.setChecked(false);
                        }
                    });
                }
                else{   //변경하려고 눌렀지만 다른곳부터 수정하고자 할때 다시 비밀번호 확인 받지않기위해서
                    remake_edit_password0.setFocusableInTouchMode(true);
                    remake_edit_password1.setFocusableInTouchMode(true);
                    remake_edit_password0.setFocusable(true);
                    remake_edit_password1.setFocusable(true);
                }



            }
        });
        /*text_nickname.addTextChangedListener(new TextWatcher() {  //필요없는 함수
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //주소가 존재하는지 확인하기
            }
        });*/
        text_nickname.setFocusable(false);
        text_nickname.setOnClickListener(new View.OnClickListener() {   //내주소 입력란
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SearchActivity.class);
                getSearchResult.launch(intent);
            }
        });
        remake_edit_password0.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                can_go_password =0;
                String temp=remake_edit_password0.getText().toString();
                String password_need= "^[A-Za-z0-9.,_%+~`!@#$^&*()_+-]{6,10}";
                //정규표현식 영어대소문자와숫자그리고 특수문자로 이루어지고 6자리에서 10자리
                boolean check_password= Pattern.matches(password_need,temp);
                if(check_password==true){
                    if(remake_edit_password0.getText().toString().equals("")){
                        check_password0.setText("비밀번호는 대,소문자와 숫자 그리고 특수문자로만 이루어져야하고 6~10자리이어야 합니다");
                        check_password1.setText("");
                    }

                    else if(remake_edit_password1.getText().toString().equals("")){
                        check_password0.setText("사용 가능한 비밀번호입니다");
                        check_password1.setText("비밀번호를 한번 더 입력해주세요.");
                    }

                    else if(remake_edit_password0.getText().toString().equals(remake_edit_password1.getText().toString())){
                        check_password0.setText("사용 가능한 비밀번호입니다");
                        check_password1.setText("비밀번호가 일치합니다");
                        can_go_password =1;
                    }

                    else{
                        check_password0.setText("사용 가능한 비밀번호입니다");
                        check_password1.setText("비밀번호가 일치하지 않습니다");
                    }

                }
                else{
                    check_password0.setText("비밀번호는 대,소문자와 숫자 그리고 특수문자로만 이루어져야하고 6~10자리이어야 합니다");
                    check_password1.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        remake_edit_password1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                can_go_password =0;
                String temp=remake_edit_password0.getText().toString();
                String password_need= "^[A-Za-z0-9.,_%+~`!@#$^&*()_+-]{6,10}";
                //정규표현식 영어대소문자와숫자그리고 특수문자로 이루어지고 6자리에서 10자리
                boolean check_password= Pattern.matches(password_need,temp);
                if(check_password==true){
                    if(remake_edit_password0.getText().toString().equals("")){
                        check_password0.setText("비밀번호는 대,소문자와 숫자 그리고 특수문자로만 이루어져야하고 6~10자리이어야 합니다");
                        check_password1.setText("");
                    }

                    else if(remake_edit_password1.getText().toString().equals("")){
                        check_password0.setText("사용 가능한 비밀번호입니다");
                        check_password1.setText("비밀번호를 한번 더 입력해주세요.");
                    }

                    else if(remake_edit_password0.getText().toString().equals(remake_edit_password1.getText().toString())){
                        check_password0.setText("사용 가능한 비밀번호입니다");
                        check_password1.setText("비밀번호가 일치합니다");
                        can_go_password =1;
                    }

                    else{
                        check_password0.setText("사용 가능한 비밀번호입니다");
                        check_password1.setText("비밀번호가 일치하지 않습니다");
                    }

                }
                else{
                    check_password0.setText("비밀번호는 대,소문자와 숫자 그리고 특수문자로만 이루어져야하고 6~10자리이어야 합니다");
                    check_password1.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        remake_edit_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!remake_edit_number.getText().toString().equals("")&&remake_edit_number.getText().toString().length()!=11){
                    result_number.setText("11자리의 올바른 전화번호를 적어주세요");
                    can_go_number=0;
                }

                else{
                    result_number.setText("");
                    can_go_number=1;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    
        remake_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(can_go_password == 0 || can_go_number == 0){
                    Toast_Make("수정할 사항을 정확히 입력해주세요.");
                }

                else if(can_go_password == 2 && can_go_number == 2 && can_go_next_image == 0 && can_password_change == 0){
                    Toast_Make("수정된 사항이 없습니다");
                }
                else{
                    if (can_go_next_image == 1){
                        FirebaseStorage.getInstance().getReference().child(
                                "profile_picture/" + Global.getInstance().getUser_id() +".png").putFile(selectedImageUri);
                    }
                    if (can_go_password == 1){
                        Global.getInstance().setUser_pw(remake_edit_password0.getText().toString());
                        PreferenceManager.clear((SecondActivity)SecondActivity.mContext);
                    }
                    if (can_go_number == 1){
                        Global.getInstance().setUser_phoneNo(remake_edit_number.getText().toString());
                    }
                    Toast_Make("수정이 완료되었습니다.");
                    Global.getInstance().updateDB();
                    fragment_main_activity.setFragment_eightteen();
                }
            }
        });
        return view;
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if(result.getData() != null){
                    String data = result.getData().getStringExtra("data");
                    text_nickname.setText(data);
                }

            }
    );

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {   //startacitivityforresult에서 결과값 받는곳
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            //리퀘스트코드가 일치하는지 리절트코드가 성공값인지 데이터가 빈값이 아닌지
            selectedImageUri = data.getData();  //선택한 데이터의 경로 구한 다음 저장
            my_image.setImageURI(selectedImageUri);
            can_go_next_image=1;
            //Toast.makeText(getActivity(),selectedImageUri.toString(),Toast.LENGTH_SHORT).show();
        }
        else{

            //Toast.makeText(getActivity(),"NULL",Toast.LENGTH_SHORT).show();
        }
    }
}