package com.example.last;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class Friend_no {
    private int id;
    private String where;


    public Friend_no(int id, String where){
        this.id=id;
        this.where=where;

    }

    public String getWhere() {
        return where;
    }




}