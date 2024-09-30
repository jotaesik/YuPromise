package com.example.yupromise;

public class AlarmItem {
    private Long code;
    private String content;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String min;

    AlarmItem(Long code, String content, String year, String month, String day, String hour, String min) {
        this.code = code;
        this.content = content;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
    }
    public Long getCode() {
        return code;
    }
    public String getContent() {
        return content;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }
    public String getMin() {
        return min;
    }
}
