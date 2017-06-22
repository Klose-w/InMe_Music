package com.example.wade.inme.JavaBean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by wade on 2017/6/1.
 */

public class MusicInfor implements Serializable {
    String SongName;
    String SongArtist;
    String SongAlbum;
    String SongYear;
    String SongComment;
    String SongUrl;
    long SongId;
    long SongDuration;
    float SongSize;
    String SonggcUrl;
    String Songid="0";
    String SongAlubmid;
    String Songbitmap;
    String Songdownurl;
    String Songerid;


    int Songsecond;
    int biaoji;
    public MusicInfor(String son, String soi,int soc,String sori,String sob,String sod,String su,String sa,String soa){
        biaoji=1;
        SongName=son;
        Songid=soi;
        Songsecond=soc;
        Songerid=sori;
        Songbitmap=sob;
        Songdownurl=sod;
        SongUrl=su;
        SongArtist=sa;
        SongAlubmid=soa;
        SongDuration=Songsecond*1000;
    }
    public MusicInfor(String sn, String sa, String sa1,String su,long si,long sd,float ss){
        biaoji=0;
        SongName=sn;
        SongAlbum=sa1;
        SongArtist=sa;
        SongUrl=su;
        SongId=si;
        SongDuration=sd;
        SongSize=ss;
    }
    public void setSonggcUrl(String songgcUrl) {
        SonggcUrl = songgcUrl;
    }
    public String getSonggcUrl() {
        return SonggcUrl;
    }
    public String getSongUrl() {
        return SongUrl;
    }

    public void setSongUrl(String songUrl) {
        SongUrl = songUrl;
    }

    public long getSongId() {
        return SongId;
    }

    public void setSongId(long songId) {
        SongId = songId;
    }
    public void setSongSize(float songSize) {
        SongSize = songSize;
    }

    public String getSongid() {
        return Songid;
    }

    public void setSongid(String songid) {
        Songid = songid;
    }

    public String getSongAlubmid() {
        return SongAlubmid;
    }

    public void setSongAlubmid(String songAlubmid) {
        SongAlubmid = songAlubmid;
    }

    public String getSongbitmap() {
        return Songbitmap;
    }

    public void setSongbitmap(String songbitmap) {
        Songbitmap = songbitmap;
    }

    public String getSongdownurl() {
        return Songdownurl;
    }

    public void setSongdownurl(String songdownurl) {
        Songdownurl = songdownurl;
    }

    public String getSongerid() {
        return Songerid;
    }

    public void setSongerid(String songerid) {
        Songerid = songerid;
    }

    public int getSongsecond() {
        return Songsecond;
    }

    public void setSongsecond(int songsecond) {
        Songsecond = songsecond;
    }

    public int getBiaoji() {
        return biaoji;
    }

    public void setBiaoji(int biaoji) {
        this.biaoji = biaoji;
    }

    public long getSongDuration() {
        return SongDuration;
    }

    public void setSongDuration(long songDuration) {
        SongDuration = songDuration;
    }

    public float getSongSize() {
        return SongSize;
    }

    public void setSongSize(long songSize) {
        SongSize = songSize;
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getSongArtist() {
        return SongArtist;
    }

    public void setSongArtist(String songArtist) {
        SongArtist = songArtist;
    }

    public String getSongAlbum() {
        return SongAlbum;
    }

    public void setSongAlbum(String songAlbum) {
        SongAlbum = songAlbum;
    }

    public String getSongYear() {
        return SongYear;
    }

    public void setSongYear(String songYear) {
        SongYear = songYear;
    }

    public String getSongComment() {
        return SongComment;
    }

    public void setSongComment(String songComment) {
        SongComment = songComment;
    }
}
