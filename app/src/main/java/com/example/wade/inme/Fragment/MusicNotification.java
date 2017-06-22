package com.example.wade.inme.Fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.example.wade.inme.Activity.PlayNowMusic_Main;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.R;
import com.example.wade.inme.Service.PentIntentService;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

/**
 * Created by wade on 2017/6/20.
 */

public class MusicNotification {
    boolean Isplay=false;
    private Context mContext;
    private static  final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
    RemoteViews rv;
    private NotificationManager manager;
    InMeApplicacation inMeApplicacation;
    MusicInfor nowMusic;
    NotificationCompat.Builder builder;
    Broadgenxin broadgenxin;
    Broadgenxinplay broadgenxin1;
    Notification notification;
    public MusicNotification(Context context){
        mContext=context;
        manager=(NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
        broadgenxin=new Broadgenxin();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.changelistview");
        mContext.registerReceiver(broadgenxin, filter);
        broadgenxin1=new Broadgenxinplay();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("android.intent.changeplay");
        mContext.registerReceiver(broadgenxin1, filter1);
    }
    public void initview(){
        nowMusic=inMeApplicacation.getNowmusic();
        rv.setTextViewText(R.id.tv_noti_name,nowMusic.getSongName());
        rv.setTextViewText(R.id.tv_noti_songger,nowMusic.getSongArtist()+"-"+nowMusic.getSongAlbum());
        //if(nowMusic.getBiaoji()==0){
            rv.setImageViewBitmap(R.id.iv_noti_play,getArtwork(nowMusic.getSongId()+""));
       // }else {
            //Glide.with(mContext).load(nowMusic.getSongbitmap()).into();
       // }



    }
    public void initpending(){
        Intent PlayIntent=new Intent(mContext, PentIntentService.class);
        PlayIntent.putExtra("play",1);
        PlayIntent.putExtra("Isplay",Isplay);
        PendingIntent playPending=PendingIntent.getService(mContext,2,PlayIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.ib_play_play_,playPending);
    }
    public void sendNotification(){
        builder=new NotificationCompat.Builder(mContext);
        /*Notification notification=new Notification();
        notification.flags=Notification.FLAG_NO_CLEAR;
        notification.icon=R.drawable.a3_;*/

        rv=new RemoteViews(mContext.getPackageName(), R.layout.musicplaynotification);
        initview();
        builder.setCustomBigContentView(rv).setSmallIcon(R.drawable.a3i).setAutoCancel(false);
        Intent OnclickIntent=new Intent(mContext, PlayNowMusic_Main.class);
        PendingIntent pIntent=PendingIntent.getActivity(mContext,0,OnclickIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification= builder.getNotification();
        //notification.contentView=rv;
        notification.contentIntent=pIntent;
        initpending();

        Intent LastIntent=new Intent(mContext, PentIntentService.class);
        LastIntent.putExtra("play",0);
        PendingIntent lastPending=PendingIntent.getService(mContext,1,LastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.ib_play_last_,lastPending);

        Intent NextIntent=new Intent(mContext, PentIntentService.class);
        NextIntent.putExtra("play",2);
        PendingIntent nextPending=PendingIntent.getService(mContext,3,NextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.ib_play_next_,nextPending);
        manager.notify(0,notification);
    }

    public class Broadgenxin extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
           // Bundle bundle=intent.getExtras();
           // nowMusic= (MusicInfor) bundle.getSerializable("music");
            initview();
            manager.notify(0,notification);
        }
    }
    public class Broadgenxinplay extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            if(Isplay!=bundle.getBoolean("Isplay")){
                Isplay=  bundle.getBoolean("Isplay");
                if(Isplay){
                    rv.setImageViewBitmap(R.id.ib_play_play_,BitmapFactory.decodeResource(mContext.getResources(),R.drawable.a8l));
                }
                else {
                    rv.setImageViewBitmap(R.id.ib_play_play_,BitmapFactory.decodeResource(mContext.getResources(),R.drawable.a8n));
                }
                initpending();
                manager.notify(0,notification);
            }
        }
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
                ParcelFileDescriptor pfd = mContext.getContentResolver().openFileDescriptor(uri, "r");
                if(pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            } else {
                Uri uri = ContentUris.withAppendedId(albumArtUri, albumId);
                ParcelFileDescriptor pfd = mContext.getContentResolver().openFileDescriptor(uri, "r");
                if(pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            }
            options.inSampleSize = 1;
            // 只进行大小判断
            options.inJustDecodeBounds = true;
            // 调用此方法得到options得到图片大小
            if(fd==null){
                Bitmap bmm=BitmapFactory.decodeResource(mContext.getResources(),R.drawable.placeholder_disk_210);
                //b=new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_210));
                return Bitmap.createScaledBitmap(bmm,200,200,true);
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
                Bitmap bmm=BitmapFactory.decodeResource(mContext.getResources(),R.drawable.placeholder_disk_play_song);
                //b=new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_210));
                return Bitmap.createScaledBitmap(bmm,200,200,true);
            }
            bmp1=Bitmap.createScaledBitmap(bm,200,200,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp1;
    }
}
