package com.example.wade.inme.Activity;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wade.inme.Adapter.LyricAdapter;
import com.example.wade.inme.Fragment.MusicNotification;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.LyricInfo;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.Network.DiskLruCache;
import com.example.wade.inme.Network.DiskLruCacheEx;
import com.example.wade.inme.R;
import com.example.wade.inme.Service.MusicPlayService;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ScreenActivity extends AppCompatActivity implements View.OnClickListener{
    private Animation translateAn=null;
    private VelocityTracker velocityTracker;
    MusicInfor musicInfor;
    TextView Tv_scr_name;
    TextView Tv_scr_art;
    ImageButton Ib_scr_last;
    ImageButton Ib_scr_play;
    ImageButton Ib_scr_next;
    ImageButton Ib_scr_love;
    boolean Isplay=false;
    private static  final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
    ImageView Iv_screen_all;
    InMeApplicacation inMeApplicacation;
    Context mContext;
    Broadgenxinplay  broadgenxin;
    Broadgenxin broadgenxin1;
    RelativeLayout Rl_scr_hh;
    TextView Tv_scr_lyric;
    float mScrollx=0;
    LyricAdapter lyricAdapter;
    List<LyricInfo> lyricInfoList;
    DiskLruCacheEx diskLruCacheEx;
    DiskLruCache diskLruCache;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==2){
                try {
                    DiskLruCache.Snapshot snapshot=diskLruCache.get(musicInfor.getSongid());
                    lyricAdapter=new LyricAdapter(snapshot,getApplicationContext());
                    lyricInfoList=lyricAdapter.getLyricInfos();
                    //Lv_lyric.setAdapter(lyricAdapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    |WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_screen);
        mContext=this;
        broadgenxin=new Broadgenxinplay();
        Rl_scr_hh=(RelativeLayout)findViewById(R.id.rl_scr_hh);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.changeplay");
        mContext.registerReceiver(broadgenxin, filter);
        broadgenxin1=new Broadgenxin();
        IntentFilter filter1=new IntentFilter();
        filter1.addAction("android.intent.changelistview");
        mContext.registerReceiver(broadgenxin1,filter1);
        Tv_scr_name=(TextView)findViewById(R.id.tv_scr_name);
        Tv_scr_art=(TextView)findViewById(R.id.tv_scr_art);
        Tv_scr_lyric=(TextView)findViewById(R.id.tv_scr_lyric);
        Ib_scr_last=(ImageButton)findViewById(R.id.ib_scr_last);
        Ib_scr_play=(ImageButton)findViewById(R.id.ib_scr_play);
        Ib_scr_next=(ImageButton)findViewById(R.id.ib_scr_next);
        Ib_scr_love=(ImageButton)findViewById(R.id.ib_scr_love);
        Ib_scr_last.setOnClickListener(this);
        Ib_scr_play.setOnClickListener(this);
        Ib_scr_next.setOnClickListener(this);
        Ib_scr_love.setOnClickListener(this);
        inMeApplicacation=(InMeApplicacation)getApplicationContext();
        diskLruCache=inMeApplicacation.getDiskLruCache();
        Iv_screen_all=(ImageView)findViewById(R.id.iv_screen_all);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(broadgenxin);
        mContext.unregisterReceiver(broadgenxin1);
    }

    public void initView(){
        musicInfor=inMeApplicacation.getNowmusic();
        lyricInfoList=new ArrayList<LyricInfo>();
        if(musicInfor.getBiaoji()==0){
            Iv_screen_all.setBackgroundDrawable(new BitmapDrawable(getArtwork(musicInfor.getSongId()+"")));
            String lyricurl= Environment.getExternalStorageDirectory()+"/InMe/Music/lyric/"+musicInfor.getSongName()+".txt";
            // Log.e("bbbbbbbbbbbb",lyricurl);
             lyricAdapter=new LyricAdapter(lyricurl,this);
            lyricInfoList=lyricAdapter.getLyricInfos();
        }else{
            Glide.with(mContext).load(musicInfor.getSongbitmap()).into(Iv_screen_all);
            diskLruCacheEx=new DiskLruCacheEx(musicInfor.getSongid(),0,this,handler);
            diskLruCacheEx.start();
        }

        Tv_scr_art.setText(musicInfor.getSongArtist());
        Tv_scr_name.setText(musicInfor.getSongName());
    }
    public Bitmap getArtwork(String Id){
        String songId=null;
        long albumId = -1;
        Bitmap bmp1 = null;
        songId=Id;
        Bitmap bm = null;
        if(albumId < 0 && songId == null) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            FileDescriptor fd = null;
            if(albumId < 0){
                Uri uri = Uri.parse("content://media/external/audio/media/"
                        + songId + "/albumart");
                ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(uri, "r");
                if(pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            } else {
                Uri uri = ContentUris.withAppendedId(albumArtUri, albumId);
                ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(uri, "r");
                if(pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            }
            options.inSampleSize = 1;
            // 只进行大小判断
            options.inJustDecodeBounds = true;
            // 调用此方法得到options得到图片大小
            if(fd==null){
                Bitmap bmm=BitmapFactory.decodeResource(this.getResources(),R.drawable.playnowback);
                //b=new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_210));
                return bmm;
            }
            // BitmapFactory.decodeFileDescriptor(fd, null, options);
            // 我们的目标是在800pixel的画面上显示
            // 所以需要调用computeSampleSize得到图片缩放的比例
            options.inSampleSize = 1;
            // 我们得到了缩放的比例，现在开始正式读入Bitmap数据
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            //根据options参数，减少所需要的内存

            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
            if(bm==null){
                Bitmap bmm=BitmapFactory.decodeResource(this.getResources(),R.drawable.playnowback);
                //b=new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_210));
                return bmm;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bm;
    }
    public class Broadgenxinplay extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            long nowtime=bundle.getLong("playprogress");
            String  hh=null;
            String aa=null;
            if(lyricInfoList!=null){
                if(lyricInfoList.size()>0){
                    aa=getIndex(nowtime);
                    if(aa!=null&&hh!=aa){
                        Tv_scr_lyric.setText(aa);
                        hh=aa;
                    }
                }else {
                    Tv_scr_lyric.setText("无歌词");
                }
            }

            if(Isplay!=bundle.getBoolean("Isplay")){
                Isplay=  bundle.getBoolean("Isplay");
                if(Isplay){
                   Ib_scr_play.setBackgroundResource(R.drawable.a2q);
                }
                else {
                    Ib_scr_play.setBackgroundResource(R.drawable.a2s);
                }

            }
        }
    }
    public String getIndex(long lo){
        lo=lo/1000;
        //Log.e("l",lo+"");
        int i;int size=lyricInfoList.size();
        for(i=0;i<size;i++){
            if(lyricInfoList.get(i).getTime()==lo){
                // Log.e("iii",i+"");
                return lyricInfoList.get(i).getText();

            }
        }
        return null;
    }
    public class Broadgenxin extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            // Bundle bundle=intent.getExtras();
            // nowMusic= (MusicInfor) bundle.getSerializable("music");
            initView();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_scr_last:
                inMeApplicacation.nextorlast(0);
                initView();
                break;
            case R.id.ib_scr_play:
                Intent intent=new Intent(ScreenActivity.this, MusicPlayService.class);
                intent.putExtra("url",inMeApplicacation.getNowmusic().getSongUrl());
                if(Isplay){
                    intent.putExtra("isplay",0);
                }else{
                    intent.putExtra("isplay",1);
                }
                startService(intent);
                break;
            case R.id.ib_scr_next:
                inMeApplicacation.nextorlast(2);
                initView();
                break;
            case R.id.ib_scr_love:
                break;
        }
    }
    float mstartX=0.0f;
    float mchange=0.0f;
    float mstart=0.0f;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(velocityTracker==null){
            velocityTracker= VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_DOWN:
                mstart=event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                 mchange=event.getX();
                //Log.e("move","move"+changeY);
                yido(mchange);
                break;
            case MotionEvent.ACTION_UP:
                yido1(mstartX);
                break;

        }
        return false;
    }

    public void yido(float mchang){
        if(translateAn!=null){
            translateAn.cancel();
        }
        Log.e("ms,mc",mstart+" "+mchang);
        if((mstartX+mchang-mstart)<0){
            return;
        }
        translateAn=new TranslateAnimation(mstartX,mchang-mstart,0,0);
        translateAn.setDuration(300);
        translateAn.setFillAfter(true);
        Rl_scr_hh.startAnimation(translateAn);

        mstartX=mchang-mstart;
       // mstart=mchang;
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(320);
                    translateAn=null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

    }
    public void yido1(float mm){

        if(mm>520){
            translateAn=new TranslateAnimation(mstartX,1200,0,0);
            translateAn.setDuration(300);
            translateAn.setFillAfter(true);
            Rl_scr_hh.startAnimation(translateAn);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);
                        translateAn=null;
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }else {
            translateAn=new TranslateAnimation(mstartX,0,0,0);
            mstart=0;
            translateAn.setDuration(500);
            translateAn.setFillAfter(true);
            Rl_scr_hh.startAnimation(translateAn);
        }


    }
}
