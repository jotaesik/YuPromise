package com.example.yupromise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AlarmcontentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmcontent);

        findViewById(R.id.addAlarmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText alarmContent = findViewById(R.id.alarmContent);
                String content = alarmContent.getText().toString();
                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                intent.putExtra("alarmContent", content);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}