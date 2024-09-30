package com.example.yupromise;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {
    private AlarmDBHelper alarmDBHelper = new AlarmDBHelper(this, "alarm", null, 1);
    private ArrayList<AlarmItem> alarmItems = new ArrayList<AlarmItem>();
    private Long code = Long.valueOf(0);
    private String curYear = "";
    private String curMonth = "";
    private String curDay = "";
    private String curHour = "";
    private String curMin = "";
    private String year = "";
    private String month = "";
    private String day = "";
    private String min = "";
    private String hour = "";
    private String content = "";
    int btnClickCheck = 0;
    private ArrayList<MidTmp> midTmpArray = new ArrayList<MidTmp>();
    private ArrayList<MidSkyNRainfall> midSkyNRainfallArray = new ArrayList<MidSkyNRainfall>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmItems = alarmDBHelper.selectAlarmItem();
        ListView alarmListView = findViewById(R.id.alarmList);
        TimePicker alarmTime = findViewById(R.id.alarmTime);
        alarmTime.setIs24HourView(false);
        AlarmListviewAdapter alarmAdapter = new AlarmListviewAdapter(alarmItems);
        alarmListView.setAdapter(alarmAdapter);
        CalendarView alarmCal = findViewById(R.id.alarmCal);
        year = new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis()));
        curYear = new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis()));
        month = new SimpleDateFormat("M").format(new Date(System.currentTimeMillis()));
        curMonth = new SimpleDateFormat("MM").format(new Date(System.currentTimeMillis()));
        day = new SimpleDateFormat("dd").format(new Date(System.currentTimeMillis()));
        curDay = new SimpleDateFormat("dd").format(new Date(System.currentTimeMillis()));
        hour = new SimpleDateFormat("hh").format(new Date(System.currentTimeMillis()));
        curHour = new SimpleDateFormat("HH").format(new Date(System.currentTimeMillis()));
        min = new SimpleDateFormat("mm").format(new Date(System.currentTimeMillis()));
        code = Long.parseLong(year+month+day+hour+min);
        Log.d("로그", String.valueOf(code));

        //스레드 처리
        Thread weatherThread = new Thread(() -> {
            curHour = new SimpleDateFormat("HH").format(new Date(System.currentTimeMillis()));
            if(Integer.parseInt(curHour) < 6) {
               //
            } else {
                curYear = new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis()));
                curMonth = new SimpleDateFormat("MM").format(new Date(System.currentTimeMillis()));
                curDay = new SimpleDateFormat("dd").format(new Date(System.currentTimeMillis()));
                String date = curYear+curMonth+curDay+"0600";
                String local = "11B10101"; //서울
                MidWeatherTemp midWeatherTemp = new MidWeatherTemp(date, local, midTmpArray);
                midWeatherTemp.getMidTmpArray();
                MidWeatherSkyNRainfall midWeatherSkyNRainfall = new MidWeatherSkyNRainfall(date,local, midSkyNRainfallArray);
                midWeatherSkyNRainfall.getMidSkyNRainfall();
                Log.d("로그", "sky rainfall array : " + midSkyNRainfallArray.toString());


            }
            /*
            if(Integer.parseInt(curHour) < 6) {
                //
            } else if (Integer.parseInt(curHour) >= 6 && Integer.parseInt(curHour) < 18) {
                String date = curYear+curMonth+curDay+"0600";
                String local = "11B10101"; //서울
                MidWeatherSkyNRainfall midWeatherSkyNRainfall = new MidWeatherSkyNRainfall(date,local, midSkyNRainfallArray);
                midWeatherSkyNRainfall.getMidSkyNRainfall();
                Log.d("로그", "sky rainfall array : " + midSkyNRainfallArray.toString());
            } else {
                String date = curYear+curMonth+curDay+"1800";
                String local = "11B10101"; //서울
                MidWeatherSkyNRainfall midWeatherSkyNRainfall = new MidWeatherSkyNRainfall(date,local, midSkyNRainfallArray);
                midWeatherSkyNRainfall.getMidSkyNRainfall();
                Log.d("로그", "sky rainfall array : " + midSkyNRainfallArray.toString());
            }
             */
        });

        weatherThread.start();
        try {
            weatherThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("로그", "mid weather thread : " + e.getMessage());
        }

        //달력, 날씨 처리
        ArrayList vsDate = new ArrayList<String>();
        vsDate.add(new SimpleDateFormat("yyyyMd").format(new Date(System.currentTimeMillis())));
        vsDate.add(new SimpleDateFormat("yyyyMd").format(new Date(System.currentTimeMillis()+ 86400000 * 1)));
        vsDate.add(new SimpleDateFormat("yyyyMd").format(new Date(System.currentTimeMillis()+ 86400000 * 2)));
        vsDate.add(new SimpleDateFormat("yyyyMd").format(new Date(System.currentTimeMillis()+ 86400000 * 3)));
        vsDate.add(new SimpleDateFormat("yyyyMd").format(new Date(System.currentTimeMillis()+ 86400000 * 4)));
        vsDate.add(new SimpleDateFormat("yyyyMd").format(new Date(System.currentTimeMillis()+ 86400000 * 5)));
        vsDate.add(new SimpleDateFormat("yyyyMd").format(new Date(System.currentTimeMillis()+ 86400000 * 6)));
        vsDate.add(new SimpleDateFormat("yyyyMd").format(new Date(System.currentTimeMillis()+ 86400000 * 7)));
        vsDate.add(new SimpleDateFormat("yyyyMd").format(new Date(System.currentTimeMillis()+ 86400000 * 8)));
        vsDate.add(new SimpleDateFormat("yyyyMd").format(new Date(System.currentTimeMillis()+ 86400000 * 9)));
        vsDate.add(new SimpleDateFormat("yyyyMd").format(new Date(System.currentTimeMillis()+ 86400000 * 10)));

        TextView weatherDate = findViewById(R.id.weather_date);
        TextView weatherSkyNRainfallAm = findViewById(R.id.weather_am);
        TextView weatherSkyNRainfallPm = findViewById(R.id.weather_pm);
        TextView weatherTemp = findViewById(R.id.weather_temp);
        weatherDate.setText(curYear+"년 "+curMonth+"월 "+curDay+"일 날씨정보");
        weatherSkyNRainfallAm.setText("정보 없음");
        weatherSkyNRainfallPm.setText("정보 없음");
        weatherTemp.setText("정보 없음");

        alarmCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int calYear, int calMonth, int calDay) {
                year = String.valueOf(calYear);
                month = String.valueOf(calMonth + 1);
                day = String.valueOf(calDay);
                weatherDate.setText(calYear+"년 "+(calMonth+1)+"월 "+calDay+"일 날씨정보");
                weatherSkyNRainfallAm.setText("정보 없음");
                weatherSkyNRainfallPm.setText("정보 없음");
                weatherTemp.setText("정보 없음");

                if(Integer.parseInt(curHour) <= 6) {
                    //
                } else if(calYear == Integer.parseInt(curYear)) {
                    String pickedDate = String.valueOf(calYear) + String.valueOf(calMonth+1) + String.valueOf(calDay);
                    if(pickedDate.equals(vsDate.get(0).toString())) {
                        //당일
                    }
                    if(pickedDate.equals(vsDate.get(1).toString())) {
                        //1일 후
                    }
                    if(pickedDate.equals(vsDate.get(2).toString())) {
                        //2일 후
                    }
                    if(pickedDate.equals(vsDate.get(3).toString())) {
                        //3일 후
                        String tmpText= midTmpArray.get(0).getTaMin()+"C(최저기온) ~ " + midTmpArray.get(0).getTaMax()+"C(최고기온)";
                        weatherTemp.setText(tmpText);
                        String skyNRainfallAmText = "강수 확률 " +  midSkyNRainfallArray.get(0).getRainfallAm() + "%";
                        weatherSkyNRainfallAm.setText(skyNRainfallAmText);
                        String skyNRainfallPmText = "강수 확률 " +  midSkyNRainfallArray.get(0).getRainfallPm() + "%";
                        weatherSkyNRainfallPm.setText(skyNRainfallPmText);
                    }
                    if(pickedDate.equals(vsDate.get(4).toString())) {
                        //4일 후
                        String tmpText= midTmpArray.get(1).getTaMin()+"C(최저기온) ~ " + midTmpArray.get(1).getTaMax()+"C(최고기온)";
                        weatherTemp.setText(tmpText);
                        String skyNRainfallAmText = "강수 확률 " +  midSkyNRainfallArray.get(1).getRainfallAm() + "%";
                        weatherSkyNRainfallAm.setText(skyNRainfallAmText);
                        String skyNRainfallPmText = "강수 확률 " +  midSkyNRainfallArray.get(1).getRainfallPm() + "%";
                        weatherSkyNRainfallPm.setText(skyNRainfallPmText);
                    }
                    if(pickedDate.equals(vsDate.get(5).toString())) {
                        //5일 후
                        String tmpText= midTmpArray.get(2).getTaMin()+"C(최저기온) ~ " + midTmpArray.get(2).getTaMax()+"C(최고기온)";
                        weatherTemp.setText(tmpText);
                        String skyNRainfallAmText = "강수 확률 " +  midSkyNRainfallArray.get(2).getRainfallAm() + "%";
                        weatherSkyNRainfallAm.setText(skyNRainfallAmText);
                        String skyNRainfallPmText = "강수 확률 " +  midSkyNRainfallArray.get(2).getRainfallPm() + "%";
                        weatherSkyNRainfallPm.setText(skyNRainfallPmText);
                    }
                    if(pickedDate.equals(vsDate.get(6).toString())) {
                        //6일 후
                        String tmpText= midTmpArray.get(3).getTaMin()+"C(최저기온) ~ " + midTmpArray.get(3).getTaMax()+"C(최고기온)";
                        weatherTemp.setText(tmpText);
                        String skyNRainfallAmText = "강수 확률 " +  midSkyNRainfallArray.get(3).getRainfallAm() + "%";
                        weatherSkyNRainfallAm.setText(skyNRainfallAmText);
                        String skyNRainfallPmText = "강수 확률 " +  midSkyNRainfallArray.get(3).getRainfallPm() + "%";
                        weatherSkyNRainfallPm.setText(skyNRainfallPmText);
                    }
                    if(pickedDate.equals(vsDate.get(7).toString())) {
                        //7일 후
                        String tmpText= midTmpArray.get(4).getTaMin()+"C(최저기온) ~ " + midTmpArray.get(4).getTaMax()+"C(최고기온)";
                        weatherTemp.setText(tmpText);
                        String skyNRainfallAmText = "강수 확률 " +  midSkyNRainfallArray.get(4).getRainfallAm() + "%";
                        weatherSkyNRainfallAm.setText(skyNRainfallAmText);
                        String skyNRainfallPmText = "강수 확률 " +  midSkyNRainfallArray.get(4).getRainfallPm() + "%";
                        weatherSkyNRainfallPm.setText(skyNRainfallPmText);
                    }
                    if(pickedDate.equals(vsDate.get(8).toString())) {
                        //8일 후
                        String tmpText= midTmpArray.get(5).getTaMin()+"C(최저기온) ~ " + midTmpArray.get(5).getTaMax()+"C(최고기온)";
                        weatherTemp.setText(tmpText);
                        String skyNRainfallAmText = "강수 확률 " +  midSkyNRainfallArray.get(5).getRainfallAm() + "%";
                        weatherSkyNRainfallAm.setText(skyNRainfallAmText);
                        String skyNRainfallPmText = "강수 확률 " +  midSkyNRainfallArray.get(5).getRainfallPm() + "%";
                        weatherSkyNRainfallPm.setText(skyNRainfallPmText);
                    }
                    if(pickedDate.equals(vsDate.get(9).toString())) {
                        //9일 후
                        String tmpText= midTmpArray.get(6).getTaMin()+"C(최저기온) ~ " + midTmpArray.get(6).getTaMax()+"C(최고기온)";
                        weatherTemp.setText(tmpText);
                        String skyNRainfallAmText = "강수 확률 " +  midSkyNRainfallArray.get(6).getRainfallAm() + "%";
                        weatherSkyNRainfallAm.setText(skyNRainfallAmText);
                        String skyNRainfallPmText = "강수 확률 " +  midSkyNRainfallArray.get(6).getRainfallPm() + "%";
                        weatherSkyNRainfallPm.setText(skyNRainfallPmText);
                    }
                    if(pickedDate.equals(vsDate.get(10).toString())) {
                        //10일 후
                        String tmpText= midTmpArray.get(7).getTaMin()+"C(최저기온) ~ " + midTmpArray.get(7).getTaMax()+"C(최고기온)";
                        weatherTemp.setText(tmpText);
                        String skyNRainfallAmText = "강수 확률 " +  midSkyNRainfallArray.get(7).getRainfallAm() + "%";
                        weatherSkyNRainfallAm.setText(skyNRainfallAmText);
                        String skyNRainfallPmText = "강수 확률 " +  midSkyNRainfallArray.get(7).getRainfallPm() + "%";
                        weatherSkyNRainfallPm.setText(skyNRainfallPmText);
                    }
                }
            }
        });

        //일청 처리
        Button addAlarmBtn = findViewById(R.id.addAlarmBtn);
        addAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = String.valueOf(alarmTime.getHour());
                min = String.valueOf(alarmTime.getMinute());
                code = Long.parseLong(year)+Long.parseLong(month)+Long.parseLong(day)+Long.parseLong(hour)+Long.parseLong(min);
                Intent sendIntent = new Intent(getApplicationContext(), AlarmcontentActivity.class);
                startActivityForResult(sendIntent, 99);
            }
        });

        alarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                btnClickCheck = 1;
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(AlarmActivity.this);
                deleteDialog.setTitle("일정을 삭제하시겠습니까?");
                deleteDialog.setCancelable(true);
                deleteDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alarmDBHelper.deleteAlarmItem(alarmItems.get(position));
                        alarmItems = alarmDBHelper.selectAlarmItem();
                        alarmAdapter.alarmItems = alarmItems;
                        alarmListView.setAdapter(alarmAdapter);
                    }
                });
                deleteDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                deleteDialog.show();
                return false;
            }
        });

        alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(btnClickCheck == 0) {
                    alarmItems = alarmDBHelper.selectAlarmItem();
                    Intent intent = new Intent(AlarmActivity.this, AlarmNoteActivity.class);
                    intent.putExtra("content", alarmItems.get(position).getContent());
                    intent.putExtra("year", alarmItems.get(position).getYear());
                    intent.putExtra("month", alarmItems.get(position).getMonth());
                    intent.putExtra("day", alarmItems.get(position).getDay());
                    intent.putExtra("hour", alarmItems.get(position).getHour());
                    intent.putExtra("min", alarmItems.get(position).getMin());
                    startActivity(intent);
                } else btnClickCheck = 0;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 99 && resultCode == RESULT_OK && data != null) {
            content = data.getStringExtra("alarmContent");
            Log.d("로그", content);
            alarmItems = alarmDBHelper.selectAlarmItem();
            if(alarmItems.size() == 0) {
                alarmDBHelper.insertAlarmItem(new AlarmItem(code, content, year,month, day, hour, min));
            } else {
                int itemCheck = 0;
                for(int i = 0; i < alarmItems.size(); i++) {
                    if(alarmItems.get(i).getCode() == code) {
                        alarmDBHelper.updateAlarmItem(code, content, year, month, day, hour, min);
                    } else {
                        alarmDBHelper.insertAlarmItem(new AlarmItem(code, content, year,month, day, hour, min));
                    }
                }
            }
            alarmItems = alarmDBHelper.selectAlarmItem();
            ListView alarmListView = findViewById(R.id.alarmList);
            AlarmListviewAdapter alarmAdapter = new AlarmListviewAdapter(alarmItems);
            alarmListView.setAdapter(alarmAdapter);
        }
    }
}