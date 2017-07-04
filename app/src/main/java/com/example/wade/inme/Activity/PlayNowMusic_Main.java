package com.example.wade.inme.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wade.inme.Adapter.DiskAdapter;
import com.example.wade.inme.Adapter.LyricAdapter;
import com.example.wade.inme.BroadcastReceiver.PlayMusicBroad;
import com.example.wade.inme.BroadcastReceiver.PlaychangeBroad;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.MainActivity;
import com.example.wade.inme.Network.DiskLruCache;
import com.example.wade.inme.Network.DiskLruCacheEx;
import com.example.wade.inme.R;
import com.example.wade.inme.Service.MusicPlayService;
import com.example.wade.inme.UIwedget.CircleImageView;
import com.example.wade.inme.UIwedget.LyricView;

import net.qiujuer.genius.blur.StackBlur;

import java.io.IOException;

public class PlayNowMusic_Main extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    boolean Ischang=false;
    Animatable xuanzhuan;
    TextView Tv_nowpress;
    SeekBar Sb_musicpre;
    TextView Tv_musiclong;
    ImageButton Ib_play_sx;
    ImageButton Ib_play_last;
    ImageButton Ib_play_play;
    ImageButton Ib_play_next;
    ImageButton Ib_play_more;
    ImageView relativeLayout;
    ImageButton Ib_backpalynow;
    TextView Tv_title_playnow;
    TextView Tv_name_playnow;
    ImageButton Ib_fengxiang_playnow;
    MusicInfor nowmusic;
    Bitmap bitmapback;
    CircleImageView Cv_back_cir;
    int how_to_play;
    boolean Isplaying=false;
    PlaychangeBroad playchangeBroad;
    InMeApplicacation inMeApplicacation;
    CircleImageView Ci_xuanzhuan_m;
    ImageView Iv_bar_zhuan;
    ViewPager Vp_disk;
    DiskAdapter diskAdapter;
    DiskLruCache diskLruCache;
    LyricView Lv_lyric;
    boolean whatNow=false;
    View view1;
    View view2;
    boolean ib_fin=false;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1==7){
                Bundle bundle=msg.getData();
                initPlay(bundle.getBoolean("Isplay"),bundle.getLong("playprogress"),bundle.getLong("playlong"));
            }else if(msg.arg1==11){
                int i=msg.getData().getInt("id");
                //Log.e("iiiii",i+"");
                if(i==0){
                    inMeApplicacation.nextorlast(0);
                    diskAdapter.initMusic();
                    diskAdapter.changeViews();
                }else if(i==2){
                    inMeApplicacation.nextorlast(2);
                   diskAdapter.initMusic();
                    diskAdapter.changeViews();
                }
                //scroller.setScrollDuration(0);
                //scroller.initViewPagerScroll(Rv_listplay);
                Vp_disk.setCurrentItem(1);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(msg.arg1==10){
                byte[] yy;
                yy=msg.getData().getByteArray("bit");
                Log.e("dd",0+" "+"jj");
                if(yy!=null){
                    Log.e("dd",yy.length+"");
                    Bitmap bitmap1= BitmapFactory.decodeByteArray(yy,0,yy.length);
                    Bitmap bitmap=Bitmap.createBitmap(bitmap1,0,0,bitmap1.getWidth(),bitmap1.getHeight());
                    //bitmap1=Bitmap.createBitmap(bitmap,bitmap.getWidth()/2-bitmap.getWidth()/4,bitmap.getHeight()/2-bitmap.getHeight()/4,bitmap.getWidth()/2+bitmap.getWidth()/4,bitmap.getHeight()/2+bitmap.getWidth()/4);
                    bitmap=StackBlur.blurNatively(bitmap1,68,false);
                    relativeLayout.setBackground( new BitmapDrawable(bitmap));
                }

            }else if(msg.arg1==2){
                try {
                    DiskLruCache.Snapshot snapshot=diskLruCache.get(nowmusic.getSongid());
                    LyricAdapter lyricAdapter=new LyricAdapter(snapshot,getApplicationContext());
                    //Lv_lyric.setAdapter(lyricAdapter);
                    Lv_lyric.setLyricInfos(lyricAdapter.getLyricInfos());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getWindow();
        //window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    |WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_play_now_music__main);
        Lv_lyric=(LyricView) findViewById(R.id.lv_lyric);
        relativeLayout=(ImageView)findViewById(R.id.relativelayout);
        Ib_backpalynow=(ImageButton)findViewById(R.id.ib_backpalynow);
        Ib_backpalynow.setOnClickListener(this);
        Tv_title_playnow=(TextView)findViewById(R.id.tv_title_playnow);
        Tv_name_playnow=(TextView)findViewById(R.id.tv_name_playnow);
        Vp_disk=(ViewPager)findViewById(R.id.vp_disk);
        diskAdapter=new DiskAdapter(this);
        Vp_disk.setAdapter(diskAdapter);
        Vp_disk.setOnPageChangeListener(this);
        Vp_disk.setCurrentItem(1);
        Ib_fengxiang_playnow=(ImageButton) findViewById(R.id.ib_fengxiang_playnow);
        Ib_fengxiang_playnow.setOnClickListener(this);
        Tv_nowpress=(TextView)findViewById(R.id.tv_nowpress);
        Tv_musiclong=(TextView)findViewById(R.id.tv_musiclong);
        Cv_back_cir=(CircleImageView)findViewById(R.id.cv_back_cir);
        view1=(View)findViewById(R.id.include2);
        view2=(View)findViewById(R.id.include3);
        //Ci_xuanzhuan_m=(CircleImageView)findViewById(R.id.ci_xuanzhuan_m);
        Iv_bar_zhuan=(ImageView)findViewById(R.id.iv_bar_zhuan);
        Animation animation=AnimationUtils.loadAnimation(this,R.anim.tip4);
        LinearInterpolator lin=new LinearInterpolator();
        animation.setInterpolator(lin);
        animation.setFillAfter(true);
        Iv_bar_zhuan.setAnimation(animation);
        Sb_musicpre=(SeekBar)findViewById(R.id.sb_musicpre);
        Sb_musicpre.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    Intent intent=new Intent(PlayNowMusic_Main.this, MusicPlayService.class);
                    intent.putExtra("url",inMeApplicacation.getNowmusic().getSongUrl());
                    intent.putExtra("isplay",3);
                    intent.putExtra("pre",progress);
                    startService(intent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Ib_play_sx=(ImageButton)findViewById(R.id.ib_play_sx);
        Ib_play_last=(ImageButton)findViewById(R.id.ib_play_last);
        Ib_play_play=(ImageButton)findViewById(R.id.ib_play_play);
        Ib_play_next=(ImageButton)findViewById(R.id.ib_play_next);
        Ib_play_more=(ImageButton)findViewById(R.id.ib_play_more);
        Ib_play_sx.setOnClickListener(this);
        Ib_play_last.setOnClickListener(this);
        Ib_play_play.setOnClickListener(this);
        Ib_play_next.setOnClickListener(this);
        Ib_play_more.setOnClickListener(this);
        inMeApplicacation=(InMeApplicacation)this.getApplicationContext();
        diskLruCache=inMeApplicacation.getDiskLruCache();
        playchangeBroad=new PlaychangeBroad(handler);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.changeplay");
        registerReceiver(playchangeBroad, filter);
        //Ci_xuanzhuan_m.setImageBitmap(inMeApplicacation.getArtworkFromFile(this,1));
        initView();

    }
    public void initView(){
        /*if(inMeApplicacation.getArtworkFromFile(this)==null){
            Glide.with(PlayNowMusic_Main.this).load(R.drawable.placeholder_disk_play_song).into(Ci_xuanzhuan_m);
        }
        else {
            Ci_xuanzhuan_m.setImageBitmap(inMeApplicacation.getArtworkFromFile(this));
        }*/

        //Glide.with(PlayNowMusic_Main.this).load(inMeApplicacation.getArtworkFromFile(this,1)).into(Ci_xuanzhuan_m);
        ///xuanzhuan=new AnimationUtils.loadAnimation(this,R.anim.tip);
        if(nowmusic!=inMeApplicacation.getNowmusic()){
            diskAdapter.changeViews();
            nowmusic=inMeApplicacation.getNowmusic();
            try {
                DiskLruCache.Snapshot snapshot=diskLruCache.get(nowmusic.getSongid());
                if(snapshot==null&&nowmusic.getBiaoji()==1){
                    DiskLruCacheEx diskLruCacheEx=new DiskLruCacheEx(nowmusic.getSongid(),0,this,handler);
                    diskLruCacheEx.start();
                }else if(nowmusic.getBiaoji()==1){
                    LyricAdapter lyricAdapter=new LyricAdapter(snapshot,this);
                    Lv_lyric.setLyricInfos(lyricAdapter.getLyricInfos());
                   // Lv_lyric.setAdapter(lyricAdapter);
                }else if(nowmusic.getBiaoji()==0){
                    String lyricurl= Environment.getExternalStorageDirectory()+"/InMe/Music/lyric/"+nowmusic.getSongName()+".txt";
                   // Log.e("bbbbbbbbbbbb",lyricurl);
                    LyricAdapter lyricAdapter=new LyricAdapter(lyricurl,this);
                    Lv_lyric.setLyricInfos(lyricAdapter.getLyricInfos());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Tv_musiclong.setText(Longtotime(nowmusic.getSongDuration()));
            Tv_title_playnow.setText(nowmusic.getSongName());
            Tv_name_playnow.setText(nowmusic.getSongArtist());
            Drawable drawable=inMeApplicacation.getnowBitmap(this,handler);
            relativeLayout.setBackground(drawable);
            Tv_musiclong.setText(Longtotime(nowmusic.getSongDuration()));
            Ischang=true;
        }


//       Glide.with(PlayNowMusic_Main.this).load(drawable).into(relativeLayout);
        if(how_to_play!=inMeApplicacation.getHow_to_play()){
            how_to_play=inMeApplicacation.getHow_to_play();
            switch (how_to_play){
                case 0:
                    Ib_play_sx.setBackgroundResource(R.drawable.a_v);
                    break;
                case 1:
                    Ib_play_sx.setBackgroundResource(R.drawable.aag);
                    break;
                case 2:
                    Ib_play_sx.setBackgroundResource(R.drawable.aa3);
                    break;

            }
        }


    }
    public void initPlay(boolean Isplay,long pre,long time){
        initView();
        if(Isplaying!=Isplay)
        {
            diskAdapter.setRotateAnimationstart(Isplay);
           // diskAdapter=new DiskAdapter(this);
            //Vp_disk.setAdapter(diskAdapter);
            if(Ib_play_play!=null){
                if(Isplay){
                    Isplaying=true;
                    Ib_play_play.setBackgroundResource(R.drawable.a8l);
                    Animation animation=AnimationUtils.loadAnimation(this,R.anim.tip3);
                    LinearInterpolator lin=new LinearInterpolator();
                    animation.setInterpolator(lin);
                    animation.setFillAfter(true);
                    Iv_bar_zhuan.setAnimation(animation);
                }else{
                    Ib_play_play.setBackgroundResource(R.drawable.a8n);
                    Isplaying=false;
                    Animation animation=AnimationUtils.loadAnimation(this,R.anim.tip2);
                    LinearInterpolator lin=new LinearInterpolator();
                    animation.setInterpolator(lin);
                    animation.setFillAfter(true);
                    Iv_bar_zhuan.setAnimation(animation);
                }
            }
        }

        if(Sb_musicpre!=null){
            Sb_musicpre.setProgress((int)(pre*100/time));
        }
        if(Tv_musiclong!=null&&Tv_nowpress!=null){
            Tv_nowpress.setText(Longtotime(pre));
        }


    }
    public StringBuffer Longtotime(Long time1){
        time1=time1/1000;
        StringBuffer time=new StringBuffer();
        int hour=(int)(time1/3600);
        time1=time1%3600;
        int fen=(int) (time1/60);
        int miao=(int)(time1%60);
        if(hour!=0){
            time.append(hour+":");
        }
        if(fen<10){
            time.append("0"+fen+":");
        }else {
            time.append(fen+":");
        }
        if(miao<10){
            time.append("0"+miao);
        }else {
            time.append(miao+"");
        }
        return time;
    }
   

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.ib_backpalynow:
                finish();
                break;
            case R.id.ib_play_play:
                Intent intent=new Intent(PlayNowMusic_Main.this, MusicPlayService.class);
                intent.putExtra("url",inMeApplicacation.getNowmusic().getSongUrl());
                if(Isplaying){
                    intent.putExtra("isplay",0);

                }else{
                    intent.putExtra("isplay",1);

                }

                startService(intent);
                break;
            case R.id.ib_play_last:
                inMeApplicacation.nextorlast(0);
                initView();
                break;
            case R.id.ib_play_next:
                inMeApplicacation.nextorlast(2);
                initView();
                break;
            case R.id.ib_play_sx:
                how_to_play=(how_to_play+1)%3;
                switch (how_to_play){
                    case 0:
                        Ib_play_sx.setBackgroundResource(R.drawable.a_v);
                        break;
                    case 1:
                        Ib_play_sx.setBackgroundResource(R.drawable.aag);
                        break;
                    case 2:
                        Ib_play_sx.setBackgroundResource(R.drawable.aa3);
                        break;

                }
                inMeApplicacation.setHow_to_play(how_to_play);
                break;
            case R.id.ib_fengxiang_playnow:
                if(ib_fin){
                    ib_fin=false;
                    Cv_back_cir.setVisibility(View.VISIBLE);
                    Vp_disk.setVisibility(View.VISIBLE);
                    Iv_bar_zhuan.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    Ib_fengxiang_playnow.setBackgroundResource(R.drawable.a8g);
                }else {
                    ib_fin=true;
                    Ib_fengxiang_playnow.setBackgroundResource(R.drawable.a8i);
                    Cv_back_cir.setVisibility(View.GONE);
                    Vp_disk.setVisibility(View.GONE);
                    Iv_bar_zhuan.clearAnimation();
                    Iv_bar_zhuan.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(playchangeBroad);


    }
    int nowpage=0;
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {



        }




    int i;
    @Override
    public void onPageSelected(int position) {
        i=position;
        if (i != 1) {
            //Ischang=false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.arg1 = 11;
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", i);
                    message.setData(bundle);
                    handler.sendMessage(message);

                }
            }).start();


        }
    }




    @Override
    public void onPageScrollStateChanged(int state) {
       /* if(Isplaying==true){
            Animation animation1=AnimationUtils.loadAnimation(this,R.anim.tip2);
            LinearInterpolator lin1=new LinearInterpolator();
            animation1.setInterpolator(lin1);
            animation1.setFillAfter(true);
            Iv_bar_zhuan.setAnimation(animation1);
        }*/
    }
}
