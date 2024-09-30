package com.example.yupromise;

public class MidTmp {
    private String taMin;
    private String taMax;

    MidTmp(String taMin, String taMax) {
        this.taMin = taMin;
        this.taMax = taMax;
    }

    public void setTaMax(String taMax) {
        this.taMax = taMax;
    }

    public void setTaMin(String taMin) {
        this.taMin = taMin;
    }

    public String getTaMax() {
        return taMax;
    }
    public String getTaMin() {
        return taMin;
    }
}
