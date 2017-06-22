package com.example.wade.inme.Fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wade.inme.Adapter.LyricAdapter;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.JavaBean.musicbean;
import com.example.wade.inme.Network.DiskLruCache;
import com.example.wade.inme.R;
import com.example.wade.inme.Service.DownloadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link Local_gs_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Local_gs_fragment extends Fragment {
    Context mContext;
    ListView Lv_gs_local;
    GsAdapter gsAdapter;
    InMeApplicacation inMeApplicacation;
    private LruCache<String,Bitmap> mMemoryCache;
    List<MusicInfor> listsong;
    List<jilu> list=new ArrayList<jilu>();
    RequestQueue queue;
    private DiskLruCache diskLruCache;
    public Local_gs_fragment() {
        // Required empty public constructor
    }
    class jilu{
        private int num=0;
        private String name;
        private String songgerid;

        public String getSonggerid() {
            return songgerid;
        }

        public void setSonggerid(String songgerid) {
            this.songgerid = songgerid;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static Local_gs_fragment newInstance(String param1, String param2) {
        Local_gs_fragment fragment = new Local_gs_fragment();
        return fragment;
    }
    public void initCache(){
        int maxMenroy=(int)Runtime.getRuntime().maxMemory();
        int cachesize=maxMenroy/16;
        mMemoryCache=new LruCache<String,Bitmap>(cachesize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }
    public File getDiskCacheDir(Context context,String lev){
        String lj=null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||!Environment.isExternalStorageRemovable()){
            lj=context.getExternalCacheDir().getPath();
        }else {
            lj=context.getCacheDir().getPath();
        }
        String filesd=lj+File.separator+lev;
        Log.e("file",filesd);
        return  new File(filesd);
    }
    public int getAppVersion(Context context){
        try {
            PackageInfo info=context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return  info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
    public void initDiskCache(){
        try {
            File cacheDir=getDiskCacheDir(mContext,"Zjbitmap");
            if(!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            diskLruCache=DiskLruCache.open(cacheDir,getAppVersion(mContext),1,1*1024*1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_local_gs_fragment, container, false);
        mContext=getContext();
        initCache();
        initDiskCache();
        queue = Volley.newRequestQueue(mContext);
        inMeApplicacation=(InMeApplicacation)getContext().getApplicationContext();
        listsong=inMeApplicacation.getLocalmusicList();
        Lv_gs_local=(ListView)view.findViewById(R.id.lv_gs_local);
        Tongji();
        gsAdapter=new GsAdapter();
        Lv_gs_local.setAdapter(gsAdapter);
        Lv_gs_local.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fm_main, new IntoMusicFragment(0,list.get(position).getName()));
                transaction.commit();
            }
        });
        return view;
    }

    public void Tongji(){
        Collections.sort(listsong, new Comparator<MusicInfor>() {
            @Override
            public int compare(MusicInfor o1, MusicInfor o2) {
                return o1.getSongArtist().compareTo(o2.getSongArtist());
            }
        });
        String name=null;
        int num=0;
        String id=null;
        int size=listsong.size();
        for(int i=0;i<size;i++){
           if(!listsong.get(i).getSongArtist().equals(name)){
               if(name!=null){
                  jilu j=new jilu();
                   j.setNum(num);
                   j.setName(name);
                   j.setSonggerid(id);
                   list.add(j);
                   Log.e(""+i,name);
               }
               name=listsong.get(i).getSongArtist();
               num=1;
               id=listsong.get(i).getSongId()+"";
           }else {
               num++;
           }
            if(i==size-1){
                jilu j=new jilu();
                j.setNum(num);
                j.setName(name);
                j.setSonggerid(id);
                list.add(j);
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
   public class GsAdapter extends BaseAdapter{

       @Override
       public int getCount() {
           return list.size();
       }

       @Override
       public Object getItem(int position) {
           return null;
       }

       @Override
       public long getItemId(int position) {
           return position;
       }
       public class ViewHolder{
           TextView Tv_art_gs;
           TextView Tv_num_gs;
           ImageView Iv_tx_gs;
       }
       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder holder = null;
           if(convertView==null){
               convertView=LayoutInflater.from(getContext()).inflate(R.layout.gs_local_item,null);
               holder=new ViewHolder();
               holder.Tv_art_gs=(TextView)convertView.findViewById(R.id.tv_art_gs);
               holder.Tv_num_gs=(TextView)convertView.findViewById(R.id.tv_num_gs);
               holder.Iv_tx_gs=(ImageView)convertView.findViewById(R.id.iv_tx_gs);
               convertView.setTag(holder);
           }else {
               holder=(ViewHolder) convertView.getTag();
           }
           holder.Tv_art_gs.setText(list.get(position).getName());
           holder.Tv_num_gs.setText(list.get(position).getNum()+"首");
           holder.Iv_tx_gs.setTag(list.get(position).getSonggerid());
           //holder.Iv_tx_gs=(ImageView)convertView.findViewById(R.id.iv_tx_gs);
          // if(list.get(position).getName().equals("林俊杰")){
           Imageloading(holder.Iv_tx_gs,list.get(position).getName(),list.get(position).getSonggerid());
          // }else {
               //holder.Iv_tx_gs.setBackgroundResource(R.drawable.x9);
           //}
           return convertView;
       }
   }
    public void Imageloading(ImageView imageView,String Name,String sId){
        Bitmap bitmap=mMemoryCache.get(sId);
        if(bitmap==null){
           BitmapWorkerTask bitmapWorkerTask=new BitmapWorkerTask();
            bitmapWorkerTask.execute(sId,Name);
        } else{
            imageView.setImageBitmap(bitmap);
        }
    }
    class  BitmapWorkerTask extends AsyncTask<String,Void,Bitmap>{
        String name;
        String Sid;
        @Override
        protected Bitmap doInBackground(String... params) {
            Sid=params[0];
            name=params[1].replaceAll(" ","");
            FileDescriptor fileDescriptor=null;
            FileInputStream fileInputStream=null;
            DiskLruCache.Snapshot snapshot=null;
            try {
                snapshot=diskLruCache.get(Sid);
                if(snapshot==null){
                    DiskLruCache.Editor editor=diskLruCache.edit(Sid);
                    if(editor!=null){
                        OutputStream outputStream=editor.newOutputStream(0);
                        if(downloadUrlToStream(name,outputStream)){
                            Log.e("jjjjj","kkk");
                            editor.commit();
                        }else {
                            editor.abort();
                        }
                    }
                    snapshot=diskLruCache.get(Sid);
                }
                if(snapshot!=null){
                    Log.e("jjjjj","kkk1");
                    fileInputStream=(FileInputStream)snapshot.getInputStream(0);
                    fileDescriptor=fileInputStream.getFD();
                }
                Bitmap bitmap=null;
                if(fileDescriptor!=null){
                    Log.e("jjjjj","kkk2");
                    bitmap= BitmapFactory.decodeFileDescriptor(fileDescriptor);
                }
                if(bitmap!=null){
                    Log.e("jjjjj","kkk3");
                    mMemoryCache.put(Sid,bitmap);
                }
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView=(ImageView)Lv_gs_local.findViewWithTag(Sid);
            Log.e("jjjjj","kkk4");
            if(imageView!=null&&bitmap!=null){
                Log.e("jjjjj","kkk5");
                imageView.setImageBitmap(bitmap);
            }else if(imageView!=null){
                imageView.setBackgroundResource(R.drawable.x9);
            }
        }
    }

    private boolean downloadUrlToStream(String name, OutputStream outputStream) {
        URLConnection urlcon=null;
        BufferedOutputStream out=null;
        BufferedInputStream in=null;
       String sod=GetSongId(name);
        String url=null;
        if(sod!=null){
           String Songer=GetSongerId(sod);
            if(Songer!=null){
                url=GetBItmapUrl(Songer);
            }
        }
        if(url!=null){
            Log.e("sssssss",url);
            try {
                URL url1=new URL(url);
                urlcon=(URLConnection)url1.openConnection();
                in =new BufferedInputStream(urlcon.getInputStream(),8*1024);
                out=new BufferedOutputStream(outputStream,8*1024);
                int b;
                while((b=in.read())!=-1){
                    out.write(b);
                }
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {

                    try {
                        if(out!=null) {
                            out.close();
                        }
                        if(in!=null){
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        return false;
    }
    public  String GetSongId(String name) {
        String songid=null;
        StringBuilder sb = new StringBuilder();
        if(name==null){
            return null;
        }
        URL url = null;
         String urlStr = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.catalogSug&format=json&query=" + name;
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
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            String line=null;
            while ((line=reader.readLine())!=null){
                sb=sb.append(line);
            }
            JSONObject jsonObject=new JSONObject(sb.toString());
            JSONArray jsonArray = null;
            jsonArray = jsonObject.getJSONArray("song");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            songid=jsonObject1.getString("songid");
            Log.e("kkkkkkk",sb.toString());
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songid;
    }
    public  String GetSongerId(String songid) {
        String songerid=null;
        StringBuilder sb = new StringBuilder();
        if(songid==null){
            return null;
        }
        URL url = null;
        String urlStr = "http://ting.baidu.com/data/music/links?songIds=" + songid;
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
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            String line=null;
            while ((line=reader.readLine())!=null){
                sb=sb.append(line);
            }
            Log.e("kkkkkkk1",sb.toString());
            JSONObject jsonObject=new JSONObject(sb.toString());
            JSONObject jsonArray = null;
            jsonArray=jsonObject.getJSONObject("data");
            JSONArray j3=jsonArray.getJSONArray("songList");
            JSONObject j2=j3.getJSONObject(0);
            songerid=j2.getString("artistId");
            if (inputStream != null) {
                inputStream.close();
            }
            return songerid;
        } catch (MalformedURLException e) {
            Log.e("json10",e.toString());
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Log.e("json11",e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("json12",e.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  String GetBItmapUrl(String songerid) {
        String Burl=null;
        StringBuilder sb = new StringBuilder();
        if(songerid==null){
            return null;
        }
        URL url = null;
        String urlStr =  "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.artist.getinfo&format=json&tinguid=" + songerid;
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
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            String line=null;
            while ((line=reader.readLine())!=null){
                sb=sb.append(line);
            }
            Log.e("kkkkkkk1",sb.toString());
            JSONObject jsonObject=new JSONObject(sb.toString());
            String songgerbmp=jsonObject.getString("avatar_small");
            songgerbmp=songgerbmp.replaceAll("////","");
            if (inputStream != null) {
                inputStream.close();
            }
            return songgerbmp;
        } catch (MalformedURLException e) {
            Log.e("json10",e.toString());
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Log.e("json11",e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("json12",e.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void downmusic(final String name, final ImageView imageView) {
        String url1;
        url1 = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.catalogSug&format=json&query=" + name;
        // Log.e("kk", url1);

        JsonObjectRequest sr = new JsonObjectRequest(url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //Log.e("json", jsonObject.toString());
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray("song");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String songid=jsonObject1.getString("songid");
                    getArtistID(songid,imageView);

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
    public void getArtistID(final String songId, final ImageView imageView) {
        String url1;
        url1 = "http://ting.baidu.com/data/music/links?songIds=" + songId;
        // Log.e("kk", url1);

        JsonObjectRequest sr = new JsonObjectRequest(url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //Log.e("json", jsonObject.toString());
                JSONObject jsonArray = null;
                try {
                    jsonArray=jsonObject.getJSONObject("data");
                    JSONArray j3=jsonArray.getJSONArray("songList");
                    JSONObject j2=j3.getJSONObject(0);
                    String songgerId=j2.getString("artistId");
                    getArtistbmp(songgerId,imageView);
                    Log.e("IIIIIIII",songgerId);
                } catch (JSONException e) {
                    Log.e("IIIIIIII",e.toString());
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
    public void getArtistbmp(final String songgerId,final ImageView imageView) {
        String url1;
        url1 = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.artist.getinfo&format=json&tinguid=" + songgerId;
        // Log.e("kk", url1);
        JsonObjectRequest sr = new JsonObjectRequest(url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //Log.e("json", jsonObject.toString());
                try {
                    String songgerbmp=jsonObject.getString("avatar_small");
                    songgerbmp=songgerbmp.replaceAll("////","");
                    Log.e("IIIIIIII",songgerbmp);
                    //Glide.with(mContext).load(songgerbmp).into(imageView);

                } catch (JSONException e) {
                    Log.e("IIIIIIII",e.toString());
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
}
