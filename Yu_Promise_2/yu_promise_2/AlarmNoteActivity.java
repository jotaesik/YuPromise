package com.example.yupromise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AlarmNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_note);

        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        String date = intent.getStringExtra("year") + "년 " +
                intent.getStringExtra("month") + "월 " +
                intent.getStringExtra("day") + "일";
        String time = intent.getStringExtra("hour") + "시 " +
                intent.getStringExtra("min") + "분";

        TextView dateText = findViewById(R.id.dateText); dateText.setText(date);
        TextView timeText = findViewById(R.id.timeText); timeText.setText(time);
        TextView contentText = findViewById(R.id.contentText); contentText.setText(content);

        findViewById(R.id.checkBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}