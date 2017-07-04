package com.example.wade.inme.JavaBean;

import java.io.Serializable;

/**
 * Created by wade on 2017/6/26.
 */

public class NewsBean implements Serializable{
    String Title;
    String Id;
    String Link;
    String PubDate;
    String Comeform;
    String Desc;
    String[] Image;
    String ChannelName;
    String ChannelId;
    public NewsBean(){

    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getPubDate() {
        return PubDate;
    }

    public void setPubDate(String pubDate) {
        PubDate = pubDate;
    }

    public String getComeform() {
        return Comeform;
    }

    public void setComeform(String comeform) {
        Comeform = comeform;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String[] getImage() {
        return Image;
    }

    public void setImage(String[] image) {
        Image = image;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }

    public String getChannelId() {
        return ChannelId;
    }

    public void setChannelId(String channelId) {
        ChannelId = channelId;
    }
}
