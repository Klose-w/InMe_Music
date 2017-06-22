package com.example.wade.inme.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by wade on 2017/6/1.
 */

public class PlayMusicBroad extends BroadcastReceiver {
    Handler handler;
    public PlayMusicBroad(Handler handler1){
        handler=handler1;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Message message=new Message();
        message.arg1=117;
        Bundle bundle=new Bundle();
        bundle.putSerializable("music",intent.getSerializableExtra("music"));
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
