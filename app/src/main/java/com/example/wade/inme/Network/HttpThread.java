package com.example.wade.inme.Network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Lenovo on 2017/6/8.
 */

public class HttpThread extends Thread{

    private String url;
    private String json;
    private Handler handler;
    private RequestBody body;
    private Request request;
    private Message msg=new Message();
    private Bundle bundler = new Bundle();// 存放数据
    private Response response;
    private OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public HttpThread(Handler handler,String url,String json){
        this.handler = handler;
        this.url = url;
        this.json = json;
    }

    @Override
    public void run() {
        super.run();
        if(!json.equals("null")){
            body = RequestBody.create(JSON, json);
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Log.i("TTT","+++++++++++++++++++++1"+json);
        }else{
            request = new Request.Builder()
                    .url(url)
                    .build();
            Log.i("TTT","+++++++++++++++++++++2"+json);
        }

        try {
            response= client.newCall(request).execute();

            //Log.i("TTT","+++++++++++++++++++++1"+response);

            //Log.i("TTT","+++++++++++++++++++++2"+response.body().string());
            bundler.putString("json",response.body().string());
            msg.setData(bundler);
            msg.arg1=12;
            handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
