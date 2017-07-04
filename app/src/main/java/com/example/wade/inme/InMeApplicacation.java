package com.example.wade.inme;

import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.wade.inme.Adapter.MyDatabaseHelper;
import com.example.wade.inme.JavaBean.GeDanBean;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.JavaBean.UserBean;
import com.example.wade.inme.Network.DiskLruCache;
import com.example.wade.inme.Service.MusicPlayService;

import net.qiujuer.genius.blur.StackBlur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade on 2017/6/1.
 */

public class InMeApplicacation extends Application {
    Context context;
    MusicInfor nowmusic;
    MusicInfor lastmusic;
    MusicInfor nextmusic;
    List<MusicInfor> localmusicList;
    List<MusicInfor> playmusicList;
    List<MusicInfor> lisrDowlist=new ArrayList<>();
    int localsize;
    int playsize;
    int nowplay=0;
    int last;
    int next;
    private int how_to_play=0;
    Bitmap bm = null;
    DiskLruCache diskLruCache=null;
    List<MusicInfor> lisrRecPaly=new ArrayList<>();
    List<GeDanBean> geDanBeanList=new ArrayList<>();
    MyDatabaseHelper databaseHelper;;
    SQLiteDatabase db;
    private UserBean userBean;
    private String url = "http://www.jumpingbear.cn/ListenerMusic/";
    private String phone;
    private String passwd;
    public int getHow_to_play() {
        return how_to_play;
    }
    public void setHow_to_play(int how_to_play) {
        this.how_to_play = how_to_play;
        setlon();
       // sentbroad();
    }
    public void setlon(){
        if(how_to_play==0){
            if(nowplay==0){
                last=playmusicList.size()-1;
            }else {
                last=nowplay-1;
            }
            if(nowplay==(playmusicList.size()-1)){
                next=0;
            }else {
                next=nowplay+1;
            }

            lastmusic=playmusicList.get(last);
            nextmusic=playmusicList.get(next);
        }else if(how_to_play==1){
            last = (int) (Math.random() * (playmusicList.size()-1));
            next = (int) (Math.random() * (playmusicList.size()-1));
            lastmusic=playmusicList.get(last);
            nextmusic=playmusicList.get(next);

        }else if(how_to_play==2){
            last=nowplay;
            next=nowplay;
            lastmusic=nowmusic;
            nextmusic=nowmusic;
        }
        Log.e("kk","3");

    }

    private static  final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper=new MyDatabaseHelper(this,"InMeDataBase.db",null,1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                lisrRecPaly=new ArrayList<MusicInfor>();
                lisrDowlist=new ArrayList<MusicInfor>();
                getLocalSongsInfo(getApplicationContext());
                initList();
                initGeDan();
            }
        }).start();

    }

    public void initList(){
        db=initDataBase();
        Cursor cursor=db.query("Reclist",null,null,null,null,null,null);
        if(cursor.moveToLast()){
            do{
                if(cursor.getInt(cursor.getColumnIndex("Biaoji"))==0){
                    MusicInfor m=new MusicInfor(cursor.getString(cursor.getColumnIndex("SongName")),
                            cursor.getString(cursor.getColumnIndex("SongArtist")),
                            cursor.getString(cursor.getColumnIndex("SongAlbum")),
                            cursor.getString(cursor.getColumnIndex("SongUrl")),
                            cursor.getLong(cursor.getColumnIndex("SongId")),
                            cursor.getLong(cursor.getColumnIndex("SongDuration")),
                            cursor.getLong(cursor.getColumnIndex("SongSize")));
                    lisrRecPaly.add(m);
                }else {
                    MusicInfor m=new MusicInfor(cursor.getString(cursor.getColumnIndex("SongName")),
                            cursor.getString(cursor.getColumnIndex("SongId")),
                            cursor.getInt(cursor.getColumnIndex("SongSize")),
                            cursor.getString(cursor.getColumnIndex("Songerid")),
                            cursor.getString(cursor.getColumnIndex("Songbitmap")),
                            null,
                            cursor.getString(cursor.getColumnIndex("SongUrl")),
                            cursor.getString(cursor.getColumnIndex("SongArtist")),
                            cursor.getString(cursor.getColumnIndex("SongAlbum"))
                            );
                    lisrRecPaly.add(m);
                }
            }while (cursor.moveToPrevious());
        }

        Cursor cursor1=db.query("Downlist",null,null,null,null,null,null);
        if(cursor1.moveToLast()){
            do{
                    MusicInfor m=new MusicInfor(cursor1.getString(cursor1.getColumnIndex("SongName")),
                            cursor1.getString(cursor1.getColumnIndex("SongArtist")),
                            cursor1.getString(cursor1.getColumnIndex("SongAlbum")),
                            cursor1.getString(cursor1.getColumnIndex("SongUrl")),
                            cursor1.getLong(cursor1.getColumnIndex("SongId")),
                            cursor1.getLong(cursor1.getColumnIndex("SongDuration")),
                            cursor1.getLong(cursor1.getColumnIndex("SongSize")));
                    lisrDowlist.add(m);

            }while (cursor1.moveToPrevious());
        }
    }

    public DiskLruCache getDiskLruCache() {
        return diskLruCache;
    }

    public void setDiskLruCache(DiskLruCache diskLruCache) {
        this.diskLruCache = diskLruCache;
    }

    public void  getLocalSongsInfo(Context context){
        localmusicList = new ArrayList<MusicInfor>();
        try {
            File cacheDir=getDiskCacheDir(context,"Lyric");
            if(!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            diskLruCache=DiskLruCache.open(cacheDir,getAppVersion(context),1,1*1024*1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        );
       // Log.e("ll",cursor.getCount()+"");
        for (int i = 0; i < cursor.getCount();i++){
            cursor.moveToNext();
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); //是否为音乐
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)); //歌曲Id
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)); //歌曲名
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));//歌手
            int albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)); //专辑ID
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)); //专辑
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)); //歌曲时长
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)); //歌曲路径
            float size = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)); //歌曲大小

            if (duration>1000*60){  //只把大于一分钟的音乐添加到集合中
                MusicInfor musicInfor=new MusicInfor(title,artist,album,url,id,duration,size);
                localmusicList.add(musicInfor);
               // Log.e("ll",7+"");
            }
        }
        localsize=localmusicList.size();
        playmusicList=localmusicList;
        playsize=playmusicList.size();
        nowmusic=playmusicList.get(nowplay);
        setlon();
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
    public  Bitmap getArtwork(Context context,int i){
        long songId=-1;
        long albumId = -1;
        Bitmap bmp1 = null;
        switch (i){
            case 0:
                songId=lastmusic.getSongId();
                break;
            case 1:
                songId=nowmusic.getSongId();
                break;
            case 2:
                songId=nextmusic.getSongId();
                break;
        }
        Bitmap bm = null;
        if(albumId < 0 && songId < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            FileDescriptor fd = null;
            if(albumId < 0){
                Uri uri = Uri.parse("content://media/external/audio/media/"
                        + songId + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if(pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            } else {
                Uri uri = ContentUris.withAppendedId(albumArtUri, albumId);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if(pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            }
            options.inSampleSize = 1;
            // 只进行大小判断
            options.inJustDecodeBounds = true;
            // 调用此方法得到options得到图片大小
            if(fd==null){
                Bitmap bmm=BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_play_song);
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
                Bitmap bmm=BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_play_song);
                //b=new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_210));
                return Bitmap.createScaledBitmap(bmm,200,200,true);
            }
            bmp1=Bitmap.createScaledBitmap(bm,200,200,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp1;
    }
    public  Bitmap getArtworkFromFile(Context context,int i){
        long songId=-1;
        long albumId = -1;
        Bitmap bmp1 = null;
        switch (i){
            case 0:
                songId=lastmusic.getSongId();
                break;
            case 1:
                songId=nowmusic.getSongId();
                break;
            case 2:
                songId=nextmusic.getSongId();
                break;
        }
        Bitmap bm = null;
        if(albumId < 0 && songId < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            FileDescriptor fd = null;
            if(albumId < 0){
                Uri uri = Uri.parse("content://media/external/audio/media/"
                        + songId + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if(pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            } else {
                Uri uri = ContentUris.withAppendedId(albumArtUri, albumId);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if(pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            }
            options.inSampleSize = 1;
            // 只进行大小判断
            options.inJustDecodeBounds = true;
            // 调用此方法得到options得到图片大小
            if(fd==null){
                //b=new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_210));
                return BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_210);
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
                return BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_210);
            }
            bmp1=Bitmap.createScaledBitmap(bm,50,50,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp1;
    }
    public Drawable getnowBitmap(Context context, final Handler handler){
        this.context=context;
        Drawable drawable;
        long songId=-1;
        long albumId = -1;

        if(nowmusic.getBiaoji()==1){
            Glide.with(context).load(nowmusic.getSongbitmap()).asBitmap().into(new SimpleTarget<Bitmap>(){

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    bm=resource;
                    ByteArrayOutputStream out=new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG,100,out);
                    byte[] bytes=out.toByteArray();
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putByteArray("bit",bytes);
                    message.setData(bundle);
                    message.arg1=10;
                    handler.sendMessage(message);
                }
            });
            //while (bm==null){
            drawable = new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.playnowback));
            return drawable;
            //}
        }else {
            songId = nowmusic.getSongId();
            if (albumId < 0 && songId < 0) {
                throw new IllegalArgumentException("Must specify an album or a song id");
            }
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                FileDescriptor fd = null;
                if (albumId < 0) {
                    Uri uri = Uri.parse("content://media/external/audio/media/"
                            + songId + "/albumart");
                    ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                    if (pfd != null) {
                        fd = pfd.getFileDescriptor();
                    }
                } else {
                    Uri uri = ContentUris.withAppendedId(albumArtUri, albumId);
                    ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                    if (pfd != null) {
                        fd = pfd.getFileDescriptor();
                    }
                }
                options.inSampleSize = 1;
                // 只进行大小判断
                options.inJustDecodeBounds = true;
                // 调用此方法得到options得到图片大小
                if (fd == null) {
                    drawable = new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.playnowback));
                    return drawable;
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
            if(bm==null){
                drawable=new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.playnowback));
                return drawable;
            }
        drawable=new BitmapDrawable(jianqie(Bitmap.createScaledBitmap(bm,100,100,true)));
        return drawable;
    }
    public Bitmap jianqie(Bitmap bitmap){
        Bitmap bitmap1;
        bitmap1=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight());
       //bitmap1=Bitmap.createBitmap(bitmap,bitmap.getWidth()/2-bitmap.getWidth()/4,bitmap.getHeight()/2-bitmap.getHeight()/4,bitmap.getWidth()/2+bitmap.getWidth()/4,bitmap.getHeight()/2+bitmap.getWidth()/4);
        bitmap=StackBlur.blurNatively(bitmap1,28,false);
        return  bitmap;
    }
    public void nextorlast(int num){
        if(num==0){
            nextmusic=nowmusic;
            nowmusic=lastmusic;
            next=nowplay;
            nowplay=last;
            switch (how_to_play){
                case 0:
                    if(nowplay-1<0){
                        last=playmusicList.size()-1;
                    }else {
                        last=nowplay-1;
                    }
                    break;
                case 1:
                    last = (int) (Math.random() * (playmusicList.size()-1));
                    break;
                case 2:
                    last=last;
                    break;

            }
            lastmusic=playmusicList.get(last);
        }else if(num==2){
            lastmusic=nowmusic;
            nowmusic=nextmusic;
            last=nowplay;
            nowplay=next;
            switch (how_to_play){
                case 0:
                    if((nowplay+1)==playmusicList.size()){
                        next=0;
                    }else {
                        next=next+1;
                    }
                    break;
                case 1:
                    next = (int) (Math.random() * (playmusicList.size()-1));
                    break;
                case 2:
                    next=next;
                    break;

            }
            nextmusic=playmusicList.get(next);
        }
        Log.e("kk","4");
        sentbroad();

    }
    public void sentbroad(){
        Log.e("kk","5");
        Intent intent1=new Intent("android.intent.changelistview");
        Bundle bundle=new Bundle();
        bundle.putSerializable("music",nowmusic);
        intent1.putExtras(bundle);
        getApplicationContext().sendBroadcast(intent1);
        Intent intent=new Intent(getApplicationContext(), MusicPlayService.class);
        intent.putExtra("url",nowmusic.getSongUrl());
        intent.putExtra("isplay",2);
        getApplicationContext().startService(intent);
    }

    public void initGeDan(){
        Cursor cursor=db.query("Gdlist",null,null,null,null,null,null);
        if(!cursor.moveToFirst()){
            List<MusicInfor> kl=new ArrayList<>();
            GeDanBean geDanBean=new GeDanBean(0,kl,"我喜欢的音乐",BitmapFactory.decodeResource(getResources(),R.drawable.note_btn_love),"侠客孤");
            geDanBeanList.add(geDanBean);
        }
        if(cursor.moveToFirst()){
            do{
                //Toast.makeText(getApplicationContext(),"1111",Toast.LENGTH_LONG).show();
                byte data[] = cursor.getBlob(cursor.getColumnIndex("Gdlist"));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    GeDanBean g = (GeDanBean) inputStream.readObject();
                    geDanBeanList.add(g);
                    inputStream.close();
                    arrayInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }while (cursor.moveToNext());
        }
    }
    public MusicInfor getLastmusic() {
        return lastmusic;
    }

    public void setLastmusic(MusicInfor lastmusic) {
        this.lastmusic = lastmusic;
    }

    public MusicInfor getNextmusic() {
        return nextmusic;
    }

    public void setNextmusic(MusicInfor nextmusic) {
        this.nextmusic = nextmusic;
    }

    public List<MusicInfor> getPlaymusicList() {
        return playmusicList;
    }

    public void setPlaymusicList(List<MusicInfor> playmusicList) {
        this.playmusicList = playmusicList;
        Log.e("kk","1");
    }

    public int getPlaysize() {
        return playsize;
    }

    public void setPlaysize(int playsize) {
        this.playsize = playsize;
    }

    public MusicInfor getNowmusic() {
        return nowmusic;
    }

    public void setNowmusic(MusicInfor nowmusic) {
        this.nowmusic = nowmusic;
    }

    public int getNowplay() {
        return nowplay;
    }

    public void setNowplay(int nowplay) {
        this.nowplay = nowplay;
        nowmusic=playmusicList.get(nowplay);
        Log.e("kk","2");
        setlon();
        nextorlast(1);

    }

    public int getLocalsize() {
        return localsize;
    }

    public void setLocalsize(int localsize) {
        this.localsize = localsize;
    }

    public List<MusicInfor> getLocalmusicList() {
        return localmusicList;
    }

    public void setLocalmusicList(List<MusicInfor> localmusicList) {
        this.localmusicList = localmusicList;
    }

    public List<MusicInfor> getLisrRecPaly() {
        return lisrRecPaly;
    }

    public void setLisrRecPaly(List<MusicInfor> lisrRecPaly) {
        this.lisrRecPaly = lisrRecPaly;
    }

    public void addListRecPlay(){
        for(int i=0;i<lisrRecPaly.size();i++){
            if(lisrRecPaly.get(i).getSongId()==nowmusic.getSongId()){
                lisrRecPaly.remove(i);
                break;
            }
        }
        lisrRecPaly.add(nowmusic);

    }

    public List<GeDanBean> getGeDanBeanList() {
        return this.geDanBeanList;
    }

    public void setGeDanBeanList(List<GeDanBean> geDanBeanList) {
        this.geDanBeanList = geDanBeanList;
        Toast.makeText(getApplicationContext(),this.geDanBeanList.size()+"lkl",Toast.LENGTH_LONG).show();
    }

    public List<MusicInfor> getLisrDowlist() {
        return lisrDowlist;
    }
    public void addListdown(MusicInfor m){
        lisrDowlist.add(m);
    }
    public void setLisrDowlist(List<MusicInfor> lisrDowlist) {
        this.lisrDowlist = lisrDowlist;
    }
    public SQLiteDatabase initDataBase(){
        return  databaseHelper.getWritableDatabase();
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

}
