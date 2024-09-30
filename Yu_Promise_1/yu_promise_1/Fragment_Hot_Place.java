package com.example.last;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Hot_Place extends Fragment implements OnBackPressedListener, OnMapReadyCallback {
    private View view;
    private MapView mapView;
    private static NaverMap naverMap;
    long backKeyPressedTime;
    private Button first_button,second_button,more_button;
    private TextView second_text;
    Toast toast;
    Fragment_Main_Activity fragment_main_activity;  //프레그먼트 메인 선언
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
    @Override
    public void onBackPressed() {       //오버라이딩 fragment_main_activity
            if(Global.getInstance().getPage_check()==1){    //약속하기탭을 통해 핫플레이스로 이동햇을때에
                //Toast_Make("성공");
                fragment_main_activity.setFragment_hot_place();
                Global.getInstance().setPage_check(0);  //돌아갈때는 다시 리셋
            }
            else {  //하단바를 통해 핫플레이스탭을 들어갔을때에


                //Toast_Make("실패");
                if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                    backKeyPressedTime = System.currentTimeMillis();
                    toast.show();
                    return;
                }
                if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                    getActivity().finish();
                    fragment_main_activity.onShutDown();
                    //getActivity().finish();
                    toast.cancel();
                }
            }

    }
    @Override
    public void onResume() {
        super.onResume();
        fragment_main_activity.setOnBackPressedListener(this);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_hot_place,container,false);
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        mapView=(MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        toast=Toast.makeText(getContext(),"한번더 뒤로가기 누르면 종료",Toast.LENGTH_SHORT);
        //Toast_Make(String.valueOf("머지"+Global.getInstance().getPage_check()));
        LinearLayout layout=(LinearLayout) view.findViewById(R.id.layout_first);    //리니어정의
        layout.setVisibility(View.INVISIBLE);  //주변검색을 누르기전까진 안보이게하기
        second_text=(TextView)view.findViewById(R.id.second_text);
        second_text.setText((Global.getInstance().getUser_id() + "님 위치에 따라 장소를 찾아줘요!!"));
        first_button=(Button) view.findViewById(R.id.first_button);
        first_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.getInstance().setAddress_number(0);
                Bundle bundle=new Bundle();
                bundle.putString("before_exist1","yes");
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                //getacitvity로 framgent_main_Acitvity받고 프래그먼트매니저 참조확득하고 트랜스픽션시작
                Fragment_Twelve fragment_twelve=new Fragment_Twelve();
                fragment_twelve.setArguments(bundle);
                Global.getInstance().setMe_check(0);
                transaction.replace(R.id.fragment_main, fragment_twelve); //프래그먼트 화면대체
                transaction.commit();   //작업완료
            }
        });
        second_button=(Button) view.findViewById(R.id.second_button);
        second_button.setOnClickListener(new View.OnClickListener() {   //껏다킬수잇도록
            @Override
            public void onClick(View v) {
                if(layout.getVisibility()==View.VISIBLE){
                    layout.setVisibility(View.INVISIBLE);
                    //second_button.setBackgroundResource(R.drawable.rectangle_7);
                    //second_button.setText("주변 검색 off");
                }
                else{
                    //여기에 이제 주변검색버튼 클릭시 사용자 주위 지도와 핫프레이스를 받아와야함.
                    layout.setVisibility(View.VISIBLE);
                    //second_button.setBackgroundResource(R.drawable.rectangle_25);
                    //second_button.setText("주변 검색 on");
                }

            }
        });
        more_button=(Button)view.findViewById(R.id.more_button);
        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_NineTeen fragment_nineteen=new Fragment_NineTeen();
                transaction.replace(R.id.fragment_main, fragment_nineteen);
                transaction.commit();
            }
        });
        ArrayList<Place> places;
        ListView hot_place_listview=(ListView) view.findViewById(R.id.hot_place_listview);
        PlaceAdapter placeAdapter;
        places=new ArrayList<>();
        placeAdapter=new PlaceAdapter(getContext(),places);
        hot_place_listview.setAdapter(placeAdapter);
        //내위치에 맞는 핫플레이스가졍괴
        //여기에 이제 내 주위를 받아오고 내 주위에있는 추천장소를 받아서 넣기
        for(int i=0;i<10;i++){
            places.add(new Place(i+"happy",""));
        }
        return view;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap=naverMap;
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(Global.getInstance().getLatitude(), Global.getInstance().getLongitude()),  // 위치 지정
                15                           // 줌 레벨
        );
        naverMap.setCameraPosition(cameraPosition);
        Marker marker=new Marker();
        marker.setPosition(new LatLng(Global.getInstance().getLatitude(), Global.getInstance().getLongitude()));
        marker.setMap(naverMap);
    }

    class PlaceAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener{
        private Context context;
        private List list;

        public PlaceAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
        }

        class ViewHolder {
            public TextView item_hot_place_name_textview;
            public TextView item_hot_place_grade_textview;

        }
        public PlaceAdapter(Context context, ArrayList list){
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.item_hot_place, parent, false);
            }

            viewHolder = new PlaceAdapter.ViewHolder();
            viewHolder.item_hot_place_name_textview=  (TextView)  convertView.findViewById(R.id.item_hot_place_name_textview);
            viewHolder.item_hot_place_grade_textview = (TextView) convertView.findViewById(R.id.item_hot_place_grade_textview);


            final Place place = (Place) list.get(position);
            viewHolder.item_hot_place_name_textview.setText(place.getFirst());
            viewHolder.item_hot_place_grade_textview.setText(place.getSecond());

            return convertView;
        }
    }

    class Place {
        private String first;
        private String second;

        public Place(String first, String second) {
            this.first=first;
            this.second=second;
        }
        public String getFirst() {
            return first;
        }
        public String getSecond(){return second;}
    }


}
