package com.example.wade.inme.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wade on 2017/6/15.
 */

public class DownloadService extends Service {
    ThreadPoolExecutor threadPoolExecutor;
    OkHttpClient client = new OkHttpClient();
    String filep;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        filep= Environment.getExternalStorageDirectory().toString()+ File.separator+"InMe/Music";
        threadPoolExecutor=new ThreadPoolExecutor(5, 5, 1, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(120));
    }
    public void ListAdd(String id,String name){
        downmusic(id,name);

    }
    public class Therad1 extends Thread{
        String songGc;
        public String url;
        String name;
        public void chushi(String url,String gc,String name){
            this.name=name;
            this.url=url;
            songGc=gc;

        }
        @Override
        public void run() {
            Log.e("url",url);
            downLoadFromUrl(url,name);
            downLoadLyric(songGc,name);
        }

    }
    public void downmusic(String id, String name) {
        String url1;
        url1 =" http://ting.baidu.com/data/music/links?songIds="+id;
        Log.e("kk", url1);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest sr = new JsonObjectRequest(url1, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("json", jsonObject.toString());
                JSONObject jsonArray = null;
                JSONArray jsonArray1 = null;
                try {

                    jsonArray=jsonObject.getJSONObject("data");
                    JSONArray j3=jsonArray.getJSONArray("songList");
                    JSONObject j2=j3.getJSONObject(0);
                    String songname=j2.getString("songName");
                    String songbit=j2.getString("songPicRadio");
                    String songlrc=j2.getString("lrcLink");
                    String songLink=j2.getString("songLink");
                    Therad1 therad1=new Therad1();
                    therad1.chushi(songLink.replaceAll("////",""),songlrc.replaceAll("////",""),songname);
                    //therad1.start();
                    threadPoolExecutor.execute(therad1);
                } catch (JSONException e) {
                    Log.e(";",e.toString());
                    e.printStackTrace();
                }
                String ss=jsonObject.toString().replaceAll("////","");
                Log.e("id1", jsonObject.toString());

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(sr);
    }
    public  void downLoadFromUrl(String urlStr,String name) {
        if(urlStr==null){
            return;
        }
        URL url = null;
        try {
            url = new URL(urlStr);
            Log.e("music",urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = new byte[1024];
            //文件保存位置
            File saveDir = new File(filep+"/music");
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }

            File file = new File(saveDir + "/"+ name+".mp3");
            FileOutputStream fos = new FileOutputStream(file);
            int i=0;
            int count=0;
            while ((count=inputStream.read(getData))!=-1) {
                fos.write(getData,0,count);
            }
            fos.flush();
            if (fos != null) {
                fos.close();
                //Toast.makeText(mContext, savePath, Toast.LENGTH_LONG).show();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (MalformedURLException e) {
            //Log.e("json10",e.toString());
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            //Log.e("json11",e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            //Log.e("json12",e.toString());
            e.printStackTrace();
        }

        System.out.println("info:" + url + " download success");
    }
    public void downLoadLyric(String lyric,String name){

        if(lyric==null){
            return;
        }
        URL url = null;
        try {
            Log.e("m",lyric);
            url = new URL(lyric);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = new byte[1024];
            //文件保存位置
            File saveDir = new File(filep+"/lyric");
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }

            File file = new File(saveDir + "/"+ name+".txt");
            FileOutputStream fos = new FileOutputStream(file);
            int i=0;
            int count=0;
            while ((count=inputStream.read(getData))!=-1) {
                fos.write(getData,0,count);
            }
            fos.flush();
            if (fos != null) {
                fos.close();
                //Toast.makeText(mContext, savePath, Toast.LENGTH_LONG).show();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (MalformedURLException e) {
            Log.e("json10",e.toString());
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Log.e("json11",e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("json12",e.toString());
            e.printStackTrace();
        }

        //System.out.println("info:" + url + " download success");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sname=null;
        String sid=null;
        if(intent!=null){
            sid=intent.getStringExtra("songid");
            sname=intent.getStringExtra("songname");
            ListAdd(sid,sname);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
