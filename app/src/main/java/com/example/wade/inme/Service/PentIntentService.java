package com.example.wade.inme.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.wade.inme.Activity.PlayNowMusic_Main;
import com.example.wade.inme.InMeApplicacation;

/**
 * Created by wade on 2017/6/20.
 */

public class PentIntentService extends Service {
    InMeApplicacation inMeApplicacation;
    int ZT=-1;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        inMeApplicacation=(InMeApplicacation)getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ZT=intent.getIntExtra("play",-1);
        Log.e("ZT",ZT+"");
        switch (ZT){
            case 0:
                inMeApplicacation.nextorlast(0);
                break;
            case 1:
                boolean Isplay=intent.getBooleanExtra("Isplay",true);
                if(Isplay){
                    Log.e("lll","1");
                }else {
                    Log.e("lll","0");
                }

                Intent intent1=new Intent(getApplicationContext(), MusicPlayService.class);
                intent1.putExtra("url",inMeApplicacation.getNowmusic().getSongUrl());
                if(Isplay){
                    intent1.putExtra("isplay",0);
                }else{
                    intent1.putExtra("isplay",1);
                }
                startService(intent1);
                break;
            case 2:
                inMeApplicacation.nextorlast(2);
                break;

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
