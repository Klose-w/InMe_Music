package com.example.wade.inme.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.wade.inme.Activity.PlayNowMusic_Main;
import com.example.wade.inme.Activity.ScreenActivity;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade on 2017/6/1.
 */

public class MusicPlayService extends Service {
    private MediaPlayer mediaPlayer;
    String path;
    boolean ip=false;
    int Isplay=0;
    int progress;
    boolean Isbroadcast=false;
    InMeApplicacation inMeApplicacation;
    broadcastTheard broadcast;
    List<MusicInfor> recentlyPlay=new ArrayList<MusicInfor>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public MusicPlayService(){
    }
    public MusicPlayService(String url){
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        inMeApplicacation=(InMeApplicacation)getApplicationContext();
        if(broadcast==null){
            broadcast=new broadcastTheard();
            broadcast.start();
        }
        IntentFilter screenOnFilter=new IntentFilter();
        screenOnFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenOnFilter.addAction(Intent.ACTION_SCREEN_OFF);
        MusicPlayService.this.registerReceiver(broadcastReceiver,screenOnFilter);

    }

    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        inMeApplicacation.addListRecPlay();
        try {
            Isbroadcast=false;
            path=intent.getStringExtra("url");
            if(path==""){
                return super.onStartCommand(intent, flags, startId);
            }
            Isplay=intent.getIntExtra("isplay",0);
        }catch (Exception e){
            return super.onStartCommand(intent, flags, startId);
        }
        if(mediaPlayer==null){
            mediaPlayer=new MediaPlayer();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                //mediaPlayer.start();
                Toast.makeText(getApplicationContext(),path,Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        switch (Isplay){
            case 0:
                mediaPlayer.pause();
                ip=false;
                break;
            case 1:
                mediaPlayer.start();
                ip=true;
                break;
            case 2:
                if(mediaPlayer!=null){
                    mediaPlayer.release();
                    mediaPlayer=new MediaPlayer();

                    try {
                        mediaPlayer.setDataSource(path);
                        mediaPlayer.prepare();
                        if(ip){
                            mediaPlayer.start();
                        }
                        // Toast.makeText(getApplicationContext(),path,Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 3:
                if(mediaPlayer!=null){
                    mediaPlayer.seekTo(mediaPlayer.getDuration()/100*intent.getIntExtra("pre",0));
                }

        }
        //Log.e("ty",path+"onStr");
        Isbroadcast=true;

         Log.e("ly",inMeApplicacation.getNowmusic().getSongId()+"");
        return super.onStartCommand(intent, flags, startId);
    }
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Log.e("锁屏",action);
            if(action.equals(Intent.ACTION_SCREEN_OFF)){
                Intent intent1=new Intent(MusicPlayService.this, ScreenActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }else {

            }
        }
    };
    public class broadcastTheard extends Thread{
        @Override
        public void run() {
            while (true){
                if(mediaPlayer!=null&&Isbroadcast){
                    if((mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition())<1000){
                        inMeApplicacation.nextorlast(2);
                    }
                    //Log.e("ip",mediaPlayer.isPlaying()+"");
                    //Log.e("ip",mediaPlayer.getCurrentPosition()+"");
                    //Log.e("ip",mediaPlayer.getDuration()+"");
                    Intent intent=new Intent("android.intent.changeplay");
                    Bundle bundle=new Bundle();
                    bundle.putLong("playprogress",mediaPlayer.getCurrentPosition());
                    bundle.putBoolean("Isplay",mediaPlayer.isPlaying());
                    bundle.putLong("playlong",mediaPlayer.getDuration());
                    intent.putExtras(bundle);
                    getApplicationContext().sendBroadcast(intent);
                }

                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        MusicPlayService.this.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
