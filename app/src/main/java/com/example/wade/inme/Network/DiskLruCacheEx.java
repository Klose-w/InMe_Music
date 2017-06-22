package com.example.wade.inme.Network;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.util.Log;

import com.example.wade.inme.InMeApplicacation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wade on 2017/6/13.
 */

public class DiskLruCacheEx extends Thread {
    OkHttpClient client = new OkHttpClient();
    private String showapi_appid = "39401";
    private String showapi_sign = "3e47b57274ff414f81e76f624a6a1231";
    String url;
    int level;
    String Id;
    Handler handler;
    Context mContext;
    String filesd;
    DiskLruCache diskLruCache=null;
    InMeApplicacation inMeApplicacation;
    public DiskLruCacheEx(String id, int level, Context context, Handler handler){
        Id=id;
        this.level=level;
        mContext=context;
        inMeApplicacation=(InMeApplicacation)context.getApplicationContext();
        diskLruCache=inMeApplicacation.getDiskLruCache();
        this.handler=handler;
    }

    public String zhuanyi(String s){
        String s1=s.replaceAll("&#58;",":");
        String s2=s1.replaceAll("&#46;",".");
        String s3=s2.replaceAll("&#10;","\n");
        String s4=s3.replaceAll("&#32;"," ");
        String s5=s4.replaceAll("&#45;","-");
        String s6=s5.replaceAll("&#40;","(");
        String s7=s6.replaceAll("&#41;",")");
        String rgex="[offset:0]([\\s\\S]*?),\"lyric_txt\"";
        Pattern pattern=Pattern.compile(rgex);
        Matcher m=pattern.matcher(s7);
        while (m.find()){
            s7=m.group(1);
        }
        Log.e("rr",s7);
        return s7;
    }

    @Override
    public void run() {
        super.run();
        url = "http://route.showapi.com/213-2?showapi_appid="+showapi_appid+"&musicid="+Id+"&showapi_sign="+showapi_sign;
        Log.e("kk",url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            //bundler.putString("message"+i, jsonObject.toString());

            String text;
            text=zhuanyi(jsonObject.toString());
            Log.e("json",text);
            DiskLruCache.Editor editor=diskLruCache.edit(Id);
            if(editor!=null){
                OutputStream outputStream=editor.newOutputStream(0);
                outputStream.write(text.getBytes());
                outputStream.close();
            }
            editor.commit();
            Message msg=new Message();
            msg.arg1=2;
            handler.sendMessage(msg);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("json","1");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json","2");
        }

    }
}
