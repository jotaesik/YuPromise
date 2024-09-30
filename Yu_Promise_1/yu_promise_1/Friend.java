package com.example.last;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class Friend {
    private int id;
    private String name;
    private int image_url;
    public boolean checked;

    public Friend(int id,String name,int image_url, boolean checked){
        this.id=id;
        this.name=name;
        this.image_url=image_url;
        this.checked=checked;
    }

    public boolean getChecked() {
        return checked;
    }
    public void setChecked(boolean checked){
        this.checked=checked;
    }
    public String getName() {
        return name;
    }
    public int getImage_url() {
        return image_url;
    }


    public boolean isChecked() {
        return checked;
    }
}