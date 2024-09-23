package com.example.last;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class AssignPost {
    public String id;
    public String pw;
    public String nickname;
    public String phoneNo;

    public AssignPost(){

    }

    public AssignPost(String pw, String nickname, String phoneNo) {
        this.pw = pw;
        this.nickname = nickname;
        this.phoneNo = phoneNo;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("pw","" );
        result.put("nickname", "");
        result.put("phoneNo", "");
        return result;
    }
}
