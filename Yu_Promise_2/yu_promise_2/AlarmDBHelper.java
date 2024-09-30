package com.example.yupromise;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AlarmDBHelper extends SQLiteOpenHelper {

    public AlarmDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "create table if not exists alarm (" +
                "code integer primary key, " +
                "content text," +
                "year text," +
                "month text," +
                "day text," +
                "hour text," +
                "min text)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertAlarmItem(AlarmItem alarmItem) {
        ContentValues items = new ContentValues();
        items.put("code", alarmItem.getCode());
        items.put("content", alarmItem.getContent());
        items.put("year", alarmItem.getYear());
        items.put("month", alarmItem.getMonth());
        items.put("day", alarmItem.getDay());
        items.put("hour", alarmItem.getHour());
        items.put("min", alarmItem.getMin());
        SQLiteDatabase wd = getWritableDatabase();
        wd.insert("alarm", null, items);
        wd.close();
    }

    public ArrayList<AlarmItem> selectAlarmItem() {
        ArrayList alarmList = new ArrayList<AlarmItem>();

        String selectCmd = "select * from alarm";
        SQLiteDatabase rd = getReadableDatabase();
        Cursor cursor = rd.rawQuery(selectCmd, null);

        while(cursor.moveToNext()) {
                @SuppressLint("Range") Long code = cursor.getLong(cursor.getColumnIndex("code"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String year = cursor.getString(cursor.getColumnIndex("year"));
                @SuppressLint("Range") String month = cursor.getString(cursor.getColumnIndex("month"));
                @SuppressLint("Range") String day = cursor.getString(cursor.getColumnIndex("day"));
                @SuppressLint("Range") String hour = cursor.getString(cursor.getColumnIndex("hour"));
                @SuppressLint("Range") String min = cursor.getString(cursor.getColumnIndex("min"));
                alarmList.add(new AlarmItem(code, content, year, month, day, hour, min));
        }
        cursor.close();
        rd.close();
        return alarmList;
    }

    public void updateAlarmItem(Long code, String content, String year, String month, String day, String hour, String min) {
        ContentValues items = new ContentValues();
        items.put("content", content);
        items.put("year", year);
        items.put("month", month);
        items.put("day", day);
        items.put("hour", hour);
        items.put("min", min);

        SQLiteDatabase wd = getWritableDatabase();
        wd.update("alarm", items, "code = " + code, null);
        wd.close();
    }

    public void deleteAlarmItem(AlarmItem alarmItem) {
        Long code = alarmItem.getCode();
        String deleteCmd = "delete from alarm where code=" + code;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(deleteCmd);
        db.close();
    }

}
