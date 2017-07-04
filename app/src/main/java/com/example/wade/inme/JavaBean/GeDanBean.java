package com.example.wade.inme.JavaBean;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.example.wade.inme.Activity.ScreenActivity;
import com.example.wade.inme.R;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by wade on 2017/6/23.
 */

public class GeDanBean implements Serializable {
    int Id;
    List<MusicInfor> gdlist;
    String gdname;
    byte[] bitmap;
    String username;
    public GeDanBean(int id, List<MusicInfor> list, String gdname, Bitmap bitmap1, String username){
        Id=id;
        gdlist=list;
        this.gdname=gdname;
        this.username=username;
        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
        bitmap1.recycle();//自由选择是否进行回收
        this.bitmap = output.toByteArray();//转换成功了
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public List<MusicInfor> getGdlist() {
        return gdlist;
    }

    public void setGdlist(List<MusicInfor> gdlist) {
        this.gdlist = gdlist;
    }

    public String getGdname() {
        return gdname;
    }

    public void setGdname(String gdname) {
        this.gdname = gdname;
    }

    public Bitmap getBitmap() {
            Bitmap bit= BitmapFactory.decodeByteArray(this.bitmap,0, this.bitmap.length);
            return bit;
    }

    public void setBitmap(Bitmap bitmap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
        bitmap.recycle();//自由选择是否进行回收
        this.bitmap = output.toByteArray();//转换成功了
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
