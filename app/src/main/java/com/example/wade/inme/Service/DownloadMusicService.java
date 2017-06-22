package com.example.wade.inme.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wade.inme.Network.DiskLruCache;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wade on 2017/6/14.
 */

public class DownloadMusicService{
    OkHttpClient client = new OkHttpClient();
    String showapi_appid = "39401";
    String showapi_sign = "3e47b57274ff414f81e76f624a6a1231";
    //List<JavaBean> list;
    Context mContext;
    String sss=null;
    //ViewHodler vh=null;
    ThreadPoolExecutor threadPoolExecutor;
    String filep;
    private static DownloadMusicService downloadMusicService=null;
    public static DownloadMusicService getInstance(){
        if(downloadMusicService==null){
            downloadMusicService=new DownloadMusicService();
        }
        return downloadMusicService;
    }
    private DownloadMusicService(){
        threadPoolExecutor=new ThreadPoolExecutor(5, 5, 1, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(120));
        filep= Environment.getExternalStorageDirectory().toString()+ File.separator+"InMe/Music";
        File file=new File(filep);
        if(!file.exists()){
            Log.e("gg","不存在");
            file.mkdirs();
        }
    }
    public void addContext(Context context){

        mContext=context;
        //list=new LinkedList<JavaBean>();

    }
    public void ListAdd(String str,String gc,String name){
            downmusic(name,gc);

    }
    public class Therad1 extends Thread{
        String Gc;
        public String url;
        String name;
        public void chushi(String url,String gc,String name){
            this.name=name;
            this.url=url;
            Gc=gc;

        }
        @Override
        public void run() {
            try {
                downLoadFromUrlgc(Gc,name);
                downLoadFromUrl(url,name);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void downmusic(final String name, final String Gc) {
        String url1;
        url1 = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.catalogSug&format=json&query=" + name;
        Log.e("kk", url1);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest sr = new JsonObjectRequest(url1, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("json", jsonObject.toString());
                JSONArray jsonArray = null;
                try {
                    String songid;
                    jsonArray = jsonObject.getJSONArray("song");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    songid = jsonObject1.getString("songid");
                    String uu=getUrl(songid);
                    Therad1 therad1=new Therad1();
                    therad1.chushi(uu,Gc,name);
                    //therad1.start();
                    threadPoolExecutor.execute(therad1);
                    Log.e("id1", songid);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(sr);
    }
    public String getUrl(String songid){
        //JSONObject js=new JSONObject(url1,new)
        String url2=null;
        String link=null;
        if(songid!=null){
            url2 =" http://ting.baidu.com/data/music/links?songIds="+songid;
            Log.e("kk",url2);
            Request request1= new Request.Builder()
                    .url(url2)
                    .build();
            Response response1 = null;
            try {
                response1 = client.newCall(request1).execute();
                JSONObject jsonObject = new JSONObject(response1.body().string());
                JSONArray jsonArray=jsonObject.getJSONArray("songList");
                Log.e("json",jsonArray.toString());
                JSONObject jsonObject1=jsonArray.getJSONObject(0);
                link=jsonObject1.getString("songLink");
                Log.e("link",link);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

     return link;


    }
    public String zhuanyi2(String s){
        String s1=s.replaceAll("\\\\","");
        //strings11=s1.Replace("\"","");
        //Log.e("rr",s7);
        return s1;
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
            Log.e("json10",e.toString());
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Log.e("json11",e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("json12",e.toString());
            e.printStackTrace();
        }

        System.out.println("info:" + url + " download success");
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
        //Log.e("rr",s7);
        return s7;
    }
    public void downLoadFromUrlgc(String Id,String name) throws IOException {

        String url = "http://route.showapi.com/213-2?showapi_appid="+showapi_appid+"&musicid="+Id+"&showapi_sign="+showapi_sign;
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
           // Log.e("json",text);
            File saveDir = new File(filep+"/lyric");
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            File file = new File(saveDir+ "/"+name+".txt");
            OutputStream out=new FileOutputStream(file);
            out.write(text.getBytes());
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(mContext,e.toString(),Toast.LENGTH_LONG).show();
            Log.e("json",e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json","2");
        }

    }

}
