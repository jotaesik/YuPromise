package com.example.last;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Fragment_FourTeen extends Fragment implements OnBackPressedListener, OnMapReadyCallback {
    private View view;
    private MapView mapView;
    private static NaverMap naverMap;
    private Fragment_Eleven fragment_eleven;
    //private Fragment_FourTeen1 fragment_fourteen1;
    private Fragment_Twelve fragment_twelve;
    private Button cancel_promise;
    private TextView item_where_first,item_where_second;
    private ListView test1_list_view;
    private TextView we_where0;
    private TextView we_where;
    private TextView we_where1;
    private Button ok_promise;
    private Button no_promise;
    private String text1,text2;
    private View view_20;
    private Fragment_Main_Activity fragment_main_activity;
    @Override
    public void onBackPressed() {
        fragment_main_activity.setFragment_fourteen();
    } //전화면으로 가기
    @Override
    public void onResume() {
        super.onResume();
        fragment_main_activity.setOnBackPressedListener(this);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_fourteen,container,false);
        fragment_main_activity=(Fragment_Main_Activity)getActivity();
        mapView=(MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //toast=Toast.makeText(getContext(),"한번더누르면종료",Toast.LENGTH_SHORT);
        /*Toast.makeText(getContext(),String.valueOf(Global.getInstance().getLatitude())+"  "+
                String.valueOf(Global.getInstance().getLongitude()),Toast.LENGTH_SHORT).show();*/
        cancel_promise=(Button)view.findViewById(R.id.cancel_promise);
        item_where_first=(TextView)view.findViewById(R.id.item_where_first);
        item_where_second=(TextView)view.findViewById(R.id.item_where_second);
        cancel_promise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment_twelve=new Fragment_Twelve();
                transaction.replace(R.id.fragment_main, fragment_twelve);
                transaction.commit();
            }
        });
        we_where0=(TextView)view.findViewById(R.id.we_where0);
        we_where=(TextView)view.findViewById(R.id.we_where);
        we_where1=(TextView)view.findViewById(R.id.we_where1);
        ok_promise=(Button)view.findViewById(R.id.ok_promise);
        ok_promise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //promise_exist++;
                //Global.getInstance().setPromise_exist(promise_exist);
                //Global.getInstance().setPromise_data("");
                //Global.getInstance().setPromise_time("");
                Bundle bundle=new Bundle(); //프레그먼트의 데이터 전달
                bundle.putString("we_where",text1);
                //bundle.putString("we_where1", test1.getSecond());
                //Toast.makeText(getContext(),String.valueOf(Global.getInstance().getPromise_exist()),Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_Twelve1 fragment_twelve1=new Fragment_Twelve1();
                fragment_twelve1.setArguments(bundle); //프레그먼트로 데이터전달
                transaction.replace(R.id.fragment_main, fragment_twelve1);
                transaction.commit();
                //약속과 연관된 친구들에게 메시지 보내기
                //스키마에 약속개수 만들어주는 변수추가
            }
        });
        no_promise=(Button)view.findViewById(R.id.no_promise);
        no_promise.setOnClickListener(new View.OnClickListener() {
            //다음페이지로 못넘어가므로 다시 다 리셋
            @Override
            public void onClick(View v) {
                we_where.setVisibility(View.INVISIBLE);
                we_where1.setVisibility(View.INVISIBLE);
                ok_promise.setVisibility(View.INVISIBLE);
                no_promise.setVisibility(View.INVISIBLE);
                we_where0.setVisibility(View.VISIBLE);
                test1_list_view.setVisibility(View.VISIBLE);
                cancel_promise.setVisibility(View.VISIBLE);
            }
        });
        //view_20=(View)view.findViewById(R.id.view_20);
        we_where.setVisibility(View.INVISIBLE);
        we_where1.setVisibility(View.INVISIBLE);
        ok_promise.setVisibility(View.INVISIBLE);
        no_promise.setVisibility(View.INVISIBLE);
        //view_20.setVisibility(View.INVISIBLE);
        ArrayList<Test1> test1s;    //Test1유형 배열리스트

        Test1Adapter test1Adapter;
        test1s=new ArrayList<>();
        test1_list_view=(ListView)view.findViewById(R.id.listview5);
        test1Adapter=new Test1Adapter(getContext(),test1s); //프레그먼트라 getContext()
        test1_list_view.setAdapter(test1Adapter);
        for(int i=0;i<100;i++){
            test1s.add(new Test1("happy","no happy"));  //나중에 makeData()함수로 바꿔서 약속장소 선택 추가해주면 될듯
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
    }

    class Test1Adapter extends ArrayAdapter implements AdapterView.OnItemClickListener {
        //arrayadaper 클래스 상속해서 배열리스트받아와서 리스트뷰에 표시 아이템클릭리스터 implemnets
        private Context context;//뷰를 표시해줄 곳
        private List list;  //데이터배열

        public Test1Adapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
        }

        class ViewHolder{ //계속 뷰에서 리스트뷰를 갱신하면 계속하여 inflate를 불러와야하는게 성능저하를 막기위해 뷰홀더 선언
            public TextView first;  //위치1
            public TextView second; //위치2 좀더 섬세한거
            public Button button;   //선택 버튼

        }

        public Test1Adapter(Context context, ArrayList list){
            super(context, 0, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null){   //이럴때만 새로은 inflate
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());   //parent의 getContext() 즉 fragment_main
                convertView = layoutInflater.inflate(R.layout.item_where_place, parent, false); //입히기
            }

            viewHolder = new ViewHolder();
            viewHolder.first = (TextView) convertView.findViewById(R.id.item_where_first);
            viewHolder.second = (TextView) convertView.findViewById(R.id.item_where_second);
            viewHolder.button = (Button) convertView.findViewById(R.id.item_where_choose);
            //viewHolder.button.setOnClickListener(onButtonChooseClicked);
            final Test1 test1 = (Test1) list.get(position); //test1 에 list.get(poistion)값 저장
            viewHolder.first.setText(test1.getFirst());
            viewHolder.second.setText(test1.getSecond());
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                //선택버튼클릭시
                @Override
                public void onClick(View v) {
                    //int index = (int) view.getTag();
                    //Toast.makeText(getContext(),index,Toast.LENGTH_SHORT).show();
                    /*Bundle bundle=new Bundle(); //프레그먼트의 데이터 전달
                    bundle.putString("we_where",test1.getFirst());
                    bundle.putString("we_where1", test1.getSecond());
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragment_fourteen1=new Fragment_FourTeen1();
                    fragment_fourteen1.setArguments(bundle); //프레그먼트로 데이터전달
                    transaction.replace(R.id.fragment_main, fragment_fourteen1);
                    transaction.commit();*/
                    text1=test1.getFirst();
                    text2=test1.getSecond();
                    //팝업창만변경
                    we_where.setVisibility(View.VISIBLE);
                    we_where.setText("약속장소를 "+text1+" 하시겠습니까?");
                    we_where1.setVisibility(View.VISIBLE);
                    we_where1.setText(text2);
                    ok_promise.setVisibility(View.VISIBLE);
                    no_promise.setVisibility(View.VISIBLE);
                    //view_20.setVisibility(View.VISIBLE);
                    we_where0.setVisibility(View.INVISIBLE);

                    test1_list_view.setVisibility(View.INVISIBLE);
                    cancel_promise.setVisibility(View.INVISIBLE);

                }
            });
            return convertView;
        }

    }
    class Test1 {
        private String first;
        private String second;
        public Test1(String first,String second) {
            this.first=first;
            this.second = second;

        }
        public String getFirst() {
            return first;
        }
        public String getSecond(){
            return second;
        }


    }

}
