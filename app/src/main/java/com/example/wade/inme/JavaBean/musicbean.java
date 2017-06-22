package com.example.wade.inme.JavaBean;

/**
 * Created by wade on 2017/6/15.
 */

public class musicbean{
    public String getSingid() {
        return singid;
    }

    public void setSingid(String singid) {
        this.singid = singid;
    }

    public String getSname() {
        return songname;
    }

    public void setSname(String sname) {
        this.songname = sname;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    String singid;
    String songname;
    String artistname;
    public musicbean(){

    }
    public musicbean(String id,String name,String art){
        singid=id;
        songname=name;
        artistname=art;
    }

}
