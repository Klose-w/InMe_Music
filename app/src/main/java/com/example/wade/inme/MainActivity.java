package com.example.wade.inme;

import android.app.ActionBar;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;


import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.wade.inme.Activity.PlayNowMusic_Main;
import com.example.wade.inme.Adapter.MainFragmentAdapter;
import com.example.wade.inme.Adapter.PlayViewPagerAdapter;
import com.example.wade.inme.BroadcastReceiver.PlayMusicBroad;
import com.example.wade.inme.BroadcastReceiver.PlaychangeBroad;
import com.example.wade.inme.Fragment.LocalMusicFragment;
import com.example.wade.inme.Fragment.MainFragment;
import com.example.wade.inme.Fragment.MusicList_main;
import com.example.wade.inme.Fragment.MusicNotification;
import com.example.wade.inme.Fragment.Musictop_online;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.Service.MusicPlayService;
import com.example.wade.inme.UIwedget.CircleSeekBar;
import com.example.wade.inme.UIwedget.LoopViewPager;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,ViewPager.OnPageChangeListener{
    MusicPlayService musicPlayService;
    ViewPagerScroller scroller;
    InMeApplicacation inMeApplicacation;
    PlayViewPagerAdapter playViewPagerAdapter;
    MainFragment mainFragment;
    LocalMusicFragment localMusicFragment;
    CircleSeekBar bt_play;
    ImageButton bt_allmusic;
    boolean isplay=false;
    boolean isstart=false;
    float progress=0.0f;
    TextView Tv_musicname_main;
    TextView Tv_singername_main;
    //Theard_pre theard;
    PlayMusicBroad playMusicBroad;
    ViewPager Rv_listplay;
    PlaychangeBroad playchangeBroad;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==7){
               // Log.e("hh",progress+"");
                Bundle bundle=msg.getData();
                initPlay(bundle.getBoolean("Isplay"),bundle.getLong("playprogress"),bundle.getLong("playlong"));
            }else if(msg.arg1==117){
                long id=msg.getData().getLong("id");
                //List<MusicInfor> list=inMeApplicacation.getLocalmusicList();
                MusicInfor musicInfor = (MusicInfor) msg.getData().getSerializable("music");
                if(musicInfor!=null)
                {
                    //Log.e("sdfs",musicInfor.getSongArtist());
                    inMeApplicacation.setNowmusic(musicInfor);
                    //theard.setSonglong((int) (inMeApplicacation.getNowmusic().getSongDuration()/1000));
                    if(isplay){
                        bt_play.setBackgroundResource(R.drawable.note_btn_play_white);
                    }

                    bt_play.setpre(0);
                    progress=0;
                    //isplay=true;
                    if(playViewPagerAdapter!=null){
                        playViewPagerAdapter=null;
                    }

                    playViewPagerAdapter=new PlayViewPagerAdapter(getApplicationContext());
                    Rv_listplay.setAdapter(playViewPagerAdapter);
                    Rv_listplay.setCurrentItem(1);


                }

            }
            else if(msg.arg1==11){
                int i=msg.getData().getInt("id");
                //Log.e("iiiii",i+"");
                if(i==0){
                    inMeApplicacation.nextorlast(0);
                    playViewPagerAdapter.initMusic();
                    playViewPagerAdapter.changeViews();
                }else if(i==2){
                    inMeApplicacation.nextorlast(2);
                    playViewPagerAdapter.initMusic();
                    playViewPagerAdapter.changeViews();
                }
               // scroller.setScrollDuration(0);
                //scroller.initViewPagerScroll(Rv_listplay);
                Rv_listplay.setCurrentItem(1);
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Window window=getWindow();
        //window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        setContentView(R.layout.activity_main);
        playMusicBroad=new PlayMusicBroad(handler);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.changelistview");
        registerReceiver(playMusicBroad, filter);
        playchangeBroad=new PlaychangeBroad(handler);
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("android.intent.changeplay");
        registerReceiver(playchangeBroad, filter1);
        inMeApplicacation=(InMeApplicacation)getApplicationContext();
        musicPlayService=new MusicPlayService();
        //Intent intent=new Intent(MainActivity.this,MusicPlayService.class);
       // startService(intent);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        playViewPagerAdapter=new PlayViewPagerAdapter(this);
        Rv_listplay=(ViewPager) findViewById(R.id.rv_listplay);
        Rv_listplay.setAdapter(playViewPagerAdapter);
        Rv_listplay.setCurrentItem(1);
        Rv_listplay.setOnPageChangeListener(this);
        //scroller= new ViewPagerScroller(this);
       // scroller.setScrollDuration(3000);
        //scroller.initViewPagerScroll(Rv_listplay);
        //Rv_listplay.setLayoutManager(linearLayoutManager);
        bt_play=(CircleSeekBar)findViewById(R.id.bt_play_main);
;        bt_play.setOnClickListener(this);
        bt_play.setpre(progress);
       // fragment=(fragment)findViewById(R.id.fm_main);
        //theard=new Theard_pre();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setDefaultFragment();
        MusicNotification musicNotification=new MusicNotification(getApplicationContext());
        musicNotification.sendNotification();

    }


    private void setDefaultFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mainFragment = new MainFragment();
        transaction.replace(R.id.fm_main, mainFragment);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(playMusicBroad);
        unregisterReceiver(playchangeBroad);
    }
    public void initPlay(boolean Isplay,long pre,long time){
        if(bt_play!=null){
            if(Isplay){
                isplay=true;
                bt_play.setBackgroundResource(R.drawable.note_btn_play_white);
            }else{
                isplay=false;
                bt_play.setBackgroundResource(R.drawable.play);
            }
            bt_play.setpre((int)((pre*100/time)*3.6));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void bt_celan_mainc(View view) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.bt_play_main:
                if(isplay){
                    //bt_play.setBackgroundResource(R.drawable.play);
                    isplay=false;
                    Intent intent=new Intent(MainActivity.this, MusicPlayService.class);
                    intent.putExtra("url",inMeApplicacation.getNowmusic().getSongUrl());
                    intent.putExtra("isplay",0);
                    startService(intent);
                }else{
                    //bt_play.setBackgroundResource(R.drawable.note_btn_play_white);
                    isplay=true;
                    Intent intent=new Intent(MainActivity.this, MusicPlayService.class);
                    intent.putExtra("url",inMeApplicacation.getNowmusic().getSongUrl());
                    intent.putExtra("isplay",1);
                    startService(intent);
                    //theard.setSonglong((int) (inMeApplicacation.getNowmusic().getSongDuration()/1000));


                }
                break;



        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        final int i=position;
        if(i!=1){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(400);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message=new Message();
                    message.arg1=11;
                    Bundle bundle=new Bundle();
                    bundle.putInt("id",i);
                    message.setData(bundle);
                    handler.sendMessage(message);

                }
            }).start();
        }


        //Rv_listplay.setCurrentItem(1);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /*public class Theard_pre extends Thread{
        int Songlong=0;

        public int getSonglong() {
            return Songlong;
        }

        public void setSonglong(int songlong) {
            Songlong = songlong;
        }

        @Override
        public void run() {
            while(true){
                if(isplay){
                try {
                    float jia=(2.0f/Songlong)*360;
                    Thread.sleep(2000);
                    progress=progress+jia;
                    Log.e("hh1",progress+"");
                    Message message=new Message();
                    message.arg1=17;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }}
        }
    }*/

    public class ViewPagerScroller extends Scroller {
        private int mScrollDuration = 2000;             // 滑动速度

        /**
         * 设置速度速度
         * @param duration
         */
        public void setScrollDuration(int duration){
            this.mScrollDuration = duration;
        }

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }



        public void initViewPagerScroll(ViewPager viewPager) {
            try {
                Field mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                mScroller.set(viewPager, this);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
