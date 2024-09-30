package com.example.yupromise;

public class ResultPath {
    ResultTrackoption route;
    String message;
    Integer code;

    ResultPath(ResultTrackoption route, String message, Integer code) {
        this.route = route;
        this.message = message;
        this.code = code;
    }
}
