package com.example.wade.inme.Network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lenovo on 2017/6/8.
 */

public class OkHttpRequest extends Thread{
    private String showapi_appid = "39401";
    private String showapi_sign = "3e47b57274ff414f81e76f624a6a1231";
    private String url;
    int[] ints = {4,27,32,26,36};
    private Handler handler;
    private Message msg=new Message();
    Bundle bundler = new Bundle();// 存放数据
    OkHttpClient client = new OkHttpClient();

    public OkHttpRequest(Handler handler){
        this.handler = handler;
        Log.e("json","111");
    }

    @Override
    public void run() {
        super.run();
        try {
            for(int i=0;i<5;i++){
                url = "http://route.showapi.com/213-4?showapi_appid="+showapi_appid+"&topid="+ints[i]+"&showapi_sign="+showapi_sign;
                Log.e("kk",url);
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                bundler.putString("message"+i, jsonObject.toString());
                Log.e("json",jsonObject.toString());

            }
            msg.setData(bundler);
            handler.sendMessage(msg);
        }
        catch (Exception e) {
            Log.e("json",e.toString());
            e.printStackTrace();
        }
    }
}
