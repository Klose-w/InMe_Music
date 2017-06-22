package com.example.wade.inme.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by wade on 2017/6/6.
 */

public class PlaychangeBroad extends BroadcastReceiver {
    Handler handler;
    boolean isPlaying;
    long CurrentPosition;
    long Duration;
    //int Duration;
    public PlaychangeBroad(Handler handler1){
        handler=handler1;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        //isPlaying=intent.getBooleanExtra("Isplay",true);
       // CurrentPosition=intent.getLongExtra("playprogress",0);
        //Duration=intent.getLongExtra("playlong",0);
        Message message=new Message();
        Bundle bundle=new Bundle();
        bundle=intent.getExtras();
        message.setData(bundle);
        message.arg1=7;
        handler.sendMessage(message);
        //Duration=intent.getIntExtra("playlong",0);
       // Log.e("isp",isPlaying+"");
        //Log.e("isp",CurrentPosition+"");
        //Log.e("isp",Duration+"");

    }
}
