package com.example.yupromise;

public class MidSkyNRainfall {
    private String skyAm;
    private String skyPm;
    private String rainfallAm;
    private String rainfallPm;

    MidSkyNRainfall(String skyAm, String skyPm, String rainfallAm, String rainfallPm) {
        this.skyAm = skyAm;
        this.skyPm = skyPm;
        this.rainfallAm = rainfallAm;
        this.rainfallPm = rainfallPm;
    }

    public String getRainfallAm() {
        return rainfallAm;
    }

    public String getRainfallPm() {
        return rainfallPm;
    }

    public String getSkyAm() {
        return skyAm;
    }

    public String getSkyPm() {
        return skyPm;
    }
}
