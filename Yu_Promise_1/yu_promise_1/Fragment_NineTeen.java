package com.example.last;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class Fragment_NineTeen extends Fragment implements OnBackPressedListener, OnMapReadyCallback {

    private View view;
    private MapView mapView;
    private static NaverMap naverMap;
    Fragment_Main_Activity fragment_main_activity;
    @Override
    public void onBackPressed() {
        fragment_main_activity.setFragment_twelve();
    }
    @Override
    public void onResume() {
        super.onResume();
        fragment_main_activity.setOnBackPressedListener(this);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_nineteen,container,false);
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        mapView=(MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        //toast=Toast.makeText(getContext(),"한번더누르면종료",Toast.LENGTH_SHORT);
        ArrayList<Test5> test5s;
        ListView test5_list_view;
        Test5Adapter test5Adapter;
        test5s=new ArrayList<>();
        test5_list_view=(ListView)view.findViewById(R.id.hot_place_listview);
        test5Adapter=new Test5Adapter(getContext(),test5s);
        test5_list_view.setAdapter(test5Adapter);
        for(int i=0;i<10;i++){
            //여기에 주변검색 추천 db들어가야할듯
            //그리고 마커찍는함수도
            test5s.add(new Test5(i+"happy","몇점"));
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

    class Test5Adapter extends ArrayAdapter implements AdapterView.OnItemClickListener {

        private Context context;
        private List list;

        public Test5Adapter(@NonNull Context context, int resource) {
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

        public Test5Adapter(Context context, ArrayList list){
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

            viewHolder = new Test5Adapter.ViewHolder();
            viewHolder.item_hot_place_name_textview=  (TextView)  convertView.findViewById(R.id.item_hot_place_name_textview);
            viewHolder.item_hot_place_grade_textview = (TextView) convertView.findViewById(R.id.item_hot_place_grade_textview);


            final Test5 test5 = (Test5) list.get(position);
            viewHolder.item_hot_place_name_textview.setText(test5.getFirst());
            viewHolder.item_hot_place_grade_textview.setText(test5.getSecond());

            return convertView;
        }

    }
    class Test5 {
        private String first;
        private String second;

        public Test5(String first, String second) {
            this.first=first;
            this.second=second;
        }
        public String getFirst() {
            return first;
        }
        public String getSecond(){return second;}
    }

}