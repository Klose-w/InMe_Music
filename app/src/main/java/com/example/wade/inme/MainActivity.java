package com.example.wade.inme;

import android.app.ActionBar;


import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wade.inme.Activity.LoginOrRegistActivity;
import com.example.wade.inme.Activity.MeAcitvity;
import com.example.wade.inme.Activity.MessageActivity;
import com.example.wade.inme.Activity.PlayNowMusic_Main;
import com.example.wade.inme.Activity.SettingActivity;
import com.example.wade.inme.Adapter.FindMusicAdapter;
import com.example.wade.inme.Adapter.MainFragmentAdapter;
import com.example.wade.inme.Adapter.MyDatabaseHelper;
import com.example.wade.inme.Adapter.PlayViewPagerAdapter;
import com.example.wade.inme.BroadcastReceiver.PlayMusicBroad;
import com.example.wade.inme.BroadcastReceiver.PlaychangeBroad;
import com.example.wade.inme.Fragment.LocalMusicFragment;
import com.example.wade.inme.Fragment.MainFragment;
import com.example.wade.inme.Fragment.MusicList_main;
import com.example.wade.inme.Fragment.MusicNotification;
import com.example.wade.inme.Fragment.Musictop_online;
import com.example.wade.inme.JavaBean.GeDanBean;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.JavaBean.UserBean;
import com.example.wade.inme.Network.HttpThread;
import com.example.wade.inme.Service.MusicPlayService;
import com.example.wade.inme.UIwedget.CircleImageView;
import com.example.wade.inme.UIwedget.CircleSeekBar;
import com.example.wade.inme.UIwedget.LoopViewPager;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,ViewPager.OnPageChangeListener{
    MusicPlayService musicPlayService;
    ViewPagerScroller scroller;
    InMeApplicacation inMeApplicacation;
    PlayViewPagerAdapter playViewPagerAdapter;
    MainFragment mainFragment;
    LocalMusicFragment localMusicFragment;
    CircleSeekBar bt_play;
    ImageButton Iv_allmusic_main;
    boolean isplay=false;
    boolean isstart=false;
    float progress=0.0f;
    TextView Tv_musicname_main;
    TextView Tv_singername_main;
    //Theard_pre theard;
    PlayMusicBroad playMusicBroad;
    ViewPager Rv_listplay;
    PlaychangeBroad playchangeBroad;
    PopupWindow popupWindow;
    int HightWindow;
    WindowManager wm;
    Window mwindow;
    private String user_url;
    private String phone;
    private JSONObject jsonObject;
    private UserBean userBean;
    private CircleImageView head_iv;
    private TextView tv_nickname;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    private LinearLayout background_ll;
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
            else if(msg.arg1==12){
                Bundle bundle= msg.getData();
                String jsonString = bundle.getString("json");

                try {
                    jsonObject = new JSONObject(jsonString);
                    Log.i("TTT",jsonObject.toString());
                    userBean = new UserBean();
                    userBean.setId(jsonObject.getString("id"));
                    userBean.setAvatar(jsonObject.getString("avatar"));
                    userBean.setBackground(jsonObject.getString("background"));
                    userBean.setBirth(jsonObject.getString("birth"));
                    userBean.setFanscount(Integer.parseInt(jsonObject.getString("fanscount")));
                    userBean.setGender(jsonObject.getString("gender"));
                    userBean.setLocation(jsonObject.getString("location"));
                    userBean.setNickname(jsonObject.getString("nickname"));
                    userBean.setPhone(phone);
                    userBean.setFoucuscount(Integer.parseInt(jsonObject.getString("foucuscount")));
                    userBean.setSharecount(Integer.parseInt(jsonObject.getString("sharecount")));
                    userBean.setSign(jsonObject.getString("sign"));
                    inMeApplicacation.setUserBean(userBean);
                    tv_nickname.setText(userBean.getNickname());
                    Log.i("TTT",userBean.getAvatar());
                    if(userBean.getAvatar().equals("null")){
                        Glide.with(MainActivity.this).load(R.drawable.a3).into(head_iv);
                    }else {
                        Glide.with(MainActivity.this).load(inMeApplicacation.getUrl()+userBean.getAvatar()).into(head_iv);

                    }
                    //Glide.with(MainActivity.this).load(application.getUrl()+userBean.getBackground()).into()


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=this.getWindow();
        // window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    |WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        //Window window=getWindow();
        //window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences("person",MODE_WORLD_READABLE);
        ed=sp.edit();
        playMusicBroad=new PlayMusicBroad(handler);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.changelistview");
        registerReceiver(playMusicBroad, filter);
        playchangeBroad=new PlaychangeBroad(handler);
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("android.intent.changeplay");
        registerReceiver(playchangeBroad, filter1);
        inMeApplicacation=(InMeApplicacation)getApplicationContext();
        user_url=inMeApplicacation.getUrl()+"get_user.php";

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
        mwindow=this.getWindow();
        wm=(WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        HightWindow=wm.getDefaultDisplay().getHeight();

        //scroller= new ViewPagerScroller(this);
       // scroller.setScrollDuration(3000);
        //scroller.initViewPagerScroll(Rv_listplay);
        //Rv_listplay.setLayoutManager(linearLayoutManager);
        bt_play=(CircleSeekBar)findViewById(R.id.bt_play_main);
;        bt_play.setOnClickListener(this);
        bt_play.setpre(progress);
        Iv_allmusic_main=(ImageButton) findViewById(R.id.iv_allmusic_main);
        Iv_allmusic_main.setOnClickListener(this);
                // fragment=(fragment)findViewById(R.id.fm_main);
        //theard=new Theard_pre();
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       // navigationView.setNavigationItemSelectedListener(this);
        inintView2();
        setDefaultFragment();
        MusicNotification musicNotification=new MusicNotification(getApplicationContext());
        musicNotification.sendNotification();


    }

    public void inintView2(){
        phone = inMeApplicacation.getPhone();
        Map<String,String> getuser=new HashMap<String,String>();

        getuser.put("phone", phone);
        JSONObject params=new JSONObject(getuser);

        new HttpThread(handler,user_url,params.toString()).start();

        //ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //DefalltFragment
        setDefaultFragment();

        //设置侧边栏
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headview = navigationView.inflateHeaderView(R.layout.nav_header_main);

        head_iv = (CircleImageView) headview.findViewById(R.id.iv_avatar);
        background_ll=(LinearLayout) headview.findViewById(R.id.head_background);
        tv_nickname =(TextView) headview.findViewById(R.id.nickname_main);



        head_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,MeAcitvity.class);
                startActivity(intent);
            }
        });

        Button setting_btn =(Button) findViewById(R.id.setting_btn);
        Button exit_btn =(Button) findViewById(R.id.exit_btn);
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.putString("plogin","否");
                ed.commit();
                Intent intent = new Intent(MainActivity.this, LoginOrRegistActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void setDefaultFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mainFragment = new MainFragment();
        transaction.replace(R.id.fm_main, mainFragment);
        transaction.commit();
    }


    long time = 0;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - time > 1000)) {
                Toast.makeText(this, "Exittttt", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SQLiteDatabase sqLiteDatabase=inMeApplicacation.initDataBase();
        sqLiteDatabase.delete("Reclist",null,null);
        ContentValues contentValues=new ContentValues();
        List<MusicInfor> putlist=new ArrayList<MusicInfor>();
        putlist=inMeApplicacation.lisrRecPaly;
        for(MusicInfor m:putlist){
            contentValues.put("SongId",m.getSongId());
            contentValues.put("SongName",m.getSongName());
            contentValues.put("SongAlbum",m.getSongAlbum());
            contentValues.put("SongArtist",m.getSongArtist());
            contentValues.put("SongUrl",m.getSongUrl());
            contentValues.put("Songerid",m.getSongerid());
            contentValues.put("Songbitmap",m.getSongbitmap());
            contentValues.put("SongDuration",m.getSongDuration());
            contentValues.put("Biaoji ",m.getBiaoji());
            contentValues.put("SongSize",m.getSongsecond());
            sqLiteDatabase.insert("Reclist",null,contentValues);
            contentValues.clear();
        }
        sqLiteDatabase.delete("Downlist",null,null);
        List<MusicInfor> dellist=new ArrayList<MusicInfor>();
        dellist=inMeApplicacation.getLisrDowlist();
        for(MusicInfor m:dellist){
            contentValues.put("SongId",m.getSongId());
            contentValues.put("SongName",m.getSongName());
            contentValues.put("SongAlbum",m.getSongAlbum());
            contentValues.put("SongArtist",m.getSongArtist());
            contentValues.put("SongUrl",m.getSongUrl());
            contentValues.put("SongDuration",m.getSongDuration());
            contentValues.put("SongSize",m.getSongsecond());
            sqLiteDatabase.insert("Downlist",null,contentValues);
            contentValues.clear();
        }
        saveGdlist();
    }
    public void saveGdlist(){
        List<GeDanBean> listgd=new ArrayList<>();
        listgd=inMeApplicacation.geDanBeanList;
        int size=listgd.size();
        Log.e("TAG1",size+"");
        ContentValues cv=new ContentValues();
        SQLiteDatabase database = inMeApplicacation.initDataBase();
        database.delete("Gdlist",null,null);
        for(int i=0;i<size;i++){
            GeDanBean g=listgd.get(i);
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
                objectOutputStream.writeObject(g);
                objectOutputStream.flush();
                byte data[] = arrayOutputStream.toByteArray();
                objectOutputStream.close();
                arrayOutputStream.close();
                cv.put("GdId",i);
                cv.put("Gdlist",data);
                database.insert("Gdlist",null,cv);
                cv.clear();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.e("ee",e.toString());
                e.printStackTrace();
            }
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
        Intent intent=new Intent();
        int id = item.getItemId();
        if (id == R.id.item_msg) {
            intent.setClass(MainActivity.this,MessageActivity.class);
           startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.item_friends) {

        } else if (id == R.id.item_near) {

        } else if (id == R.id.item_skin) {

        } else if (id == R.id.item_time) {

        } else if (id == R.id.item_clock) {

        } else if (id == R.id.item_drive) {

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
            case R.id.iv_allmusic_main:
                popWindowplay(v);
                break;



        }
    }
    private void popWindowplay(View v){
        View view= LayoutInflater.from(this).inflate(R.layout.popplaynow,null);
        ListView Iv_pop_playnow=(ListView)view.findViewById(R.id.iv_pop_playnow);
        TextView Tv_play_zt=(TextView) view.findViewById(R.id.tv_play_zt);
        final PlayNowAdapter playNowAdapter=new PlayNowAdapter();
        Iv_pop_playnow.setAdapter(playNowAdapter);
        Tv_play_zt.setText("循环播放 ("+kk.size()+")");
        Iv_pop_playnow.setSelection(ii);
        Iv_pop_playnow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inMeApplicacation.setNowplay(position);
                playNowAdapter.popclick();
            }
        });
        initPopwindow(view);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(v,0,0,HightWindow);
        WindowManager.LayoutParams lp=mwindow.getAttributes();
        lp.alpha=0.5f;
        mwindow.setAttributes(lp);
    }
    public void initPopwindow(View view){

        popupWindow=new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(255,255,255,255)));
        popupWindow.setAnimationStyle(R.style.take_photo_anim);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=mwindow.getAttributes();
                lp.alpha=1;
                mwindow.setAttributes(lp);

            }
        });
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

    List<MusicInfor> kk;
    int ii;
    public class PlayNowAdapter extends BaseAdapter{
        public PlayNowAdapter(){
             kk=inMeApplicacation.getPlaymusicList();
            ii=inMeApplicacation.getNowplay();
        }
        public void popclick(){
            kk=inMeApplicacation.getPlaymusicList();
            ii=inMeApplicacation.getNowplay();
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return kk.size();
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
            ImageView Iv_play_xians;
            TextView TextView2;
            TextView Tv_name_playxians;
            ImageButton Iv_del_popmain;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                convertView=LayoutInflater.from(getApplication()).inflate(R.layout.playclear_item,null);
                holder=new ViewHolder();
                holder.Iv_play_xians=(ImageView) convertView.findViewById(R.id.iv_play_xians);
                holder.TextView2=(TextView)convertView.findViewById(R.id.textView2);
                holder.Tv_name_playxians=(TextView)convertView.findViewById(R.id.tv_name_playxians);
                holder.Iv_del_popmain=(ImageButton) convertView.findViewById(R.id.iv_del_popmain);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            holder.TextView2.setTextColor(Color.BLACK);
            holder.Tv_name_playxians.setTextColor(Color.GRAY);
            holder.TextView2.setText(kk.get(position).getSongName());
            holder.Tv_name_playxians.setText("-"+kk.get(position).getSongArtist());
            holder.Iv_play_xians.setImageBitmap(null);
            if(position==ii){
                //holder.Iv_play_xians.setBackgroundResource(R.id.ib_backpalynow,null);
                holder.TextView2.setTextColor(Color.RED);
                holder.Tv_name_playxians.setTextColor(Color.RED);
                holder.Iv_play_xians.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.aer));
            }

            //holder.Iv_play_xians=(ImageView) convertView.findViewById(R.id.iv_play_xians);
           // holder.Iv_del_popmain=(ImageView) convertView.findViewById(R.id.iv_del_popmain);
            return convertView;
        }
    }
}
