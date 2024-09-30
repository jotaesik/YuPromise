package com.example.yupromise;

import java.util.ArrayList;

public class ResultPathData {
    ResultDistance summary;
    ArrayList<ArrayList<Double>> path;

    ResultPathData(ResultDistance summary, ArrayList<ArrayList<Double>> path) {
        this.summary = summary;
        this.path = path;
    }
}
