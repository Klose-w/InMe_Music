package com.example.wade.inme.JavaBean;

/**
 * Created by wade on 2017/6/13.
 */

public class LyricInfo{
    long time;
    String text;
    public LyricInfo(String s,long time){
        text=s;
        this.time=time;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
