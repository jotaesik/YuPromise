package com.example.last;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class Message {
    private int id;
    private String title;
    private int message_form;
    private int check_read;

    public Message(int id, String title, int message_form, int check_read) {
        this.id=id;
        this.title=title;
        this.message_form=message_form;
        this.check_read=check_read;
    }

    public int getMessage_form() {
        return message_form;
    }
    public void setCheck_read(int check_read){
        this.check_read=check_read;
    }
    public int getCheck_read(){
        return check_read;
    }
}