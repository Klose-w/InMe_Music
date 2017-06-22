package com.example.wade.inme.Fragment;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ServiceCompat;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**

 */
public class Local_zj_fragment extends Fragment {
    private static  final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
    ListView Lv_localmusic_zj;
    Context mContext;
    InMeApplicacation inMeApplicacation;
    List<MusicInfor> listsong;
    ZjAdapter zjAdapter;
    List<Jilu> list;
    private LruCache<String,Bitmap> mMemoryCache;
    class Jilu{
        private String Album;
        private int num;
        private String Name;
        private long SongId;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public long getSongId() {
            return SongId;
        }

        public void setSongId(long songId) {
            SongId = songId;
        }

        public Jilu(String A, int N, String name, long SongId){
            Album=A;
            num=N;
            Name=name;
            this.SongId=SongId;

        }

        public String getAlbum() {
            return Album;
        }

        public void setAlbum(String album) {
            Album = album;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

    public Local_zj_fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_local_zj_fragment, container, false);
        mContext=getContext();
        initCache();
        inMeApplicacation=(InMeApplicacation)getContext().getApplicationContext();
        listsong=inMeApplicacation.getLocalmusicList();
        list=new ArrayList<Jilu>();
        Tongji();
        Lv_localmusic_zj=(ListView)view.findViewById(R.id.lv_localmusic_zj);
        zjAdapter=new ZjAdapter();
        Lv_localmusic_zj.setAdapter(zjAdapter);
        Lv_localmusic_zj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fm_main, new IntoMusicFragment(1,list.get(position).getAlbum()));
                transaction.commit();
            }
        });
        return view;
    }
    public void initCache(){
        int maxMenroy=(int)Runtime.getRuntime().maxMemory();
        int cachesize=maxMenroy/16;
        mMemoryCache=new LruCache<String,Bitmap>(cachesize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }
    public void Tongji(){
        Collections.sort(listsong, new Comparator<MusicInfor>() {
            @Override
            public int compare(MusicInfor o1, MusicInfor o2) {
                return o1.getSongAlbum().compareTo(o2.getSongAlbum());
            }
        });
        String name=null;
        String n=null;
        int num=0;
        long songid=0;
        int size=listsong.size();
        for(int i=0;i<size;i++){
            if(!listsong.get(i).getSongAlbum().equals(name)){
                if(name!=null){
                    Jilu j=new Jilu(name,num,n,songid);
                    list.add(j);
                }
                name=listsong.get(i).getSongAlbum();
                n=listsong.get(i).getSongName();
                songid=listsong.get(i).getSongId();
                num=1;
            }else {
                num++;
            }
            if(i==size-1){
                Jilu j=new Jilu(name,num,n,songid);
                list.add(j);
            }
        }
    }
    public class ViewHolder{
        TextView Tv_art_zj;
        TextView Tv_num_zj;
        ImageView Iv_tx_zj;
    }
    class ZjAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView==null){
                convertView=LayoutInflater.from(getContext()).inflate(R.layout.zj_local_item,null);
                holder=new ViewHolder();
                holder.Tv_art_zj=(TextView)convertView.findViewById(R.id.tv_art_zj);
                holder.Tv_num_zj=(TextView)convertView.findViewById(R.id.tv_num_zj);
                holder.Iv_tx_zj=(ImageView)convertView.findViewById(R.id.iv_tx_zj);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }
            holder.Tv_art_zj.setText(list.get(position).getAlbum());
            holder.Tv_num_zj.setText(list.get(position).getNum()+"首"+" "+list.get(position).getName());
            holder.Iv_tx_zj.setTag(list.get(position).getSongId()+"");
            //holder.Iv_tx_gs=(ImageView)convertView.findViewById(R.id.iv_tx_gs);
            //final ImageView iv=(ImageView)convertView.findViewById(R.id.iv_tx_gs);
            // if(list.get(position).getName().equals("林俊杰")){
            //holder.Iv_tx_zj.setImageBitmap(getArtwork(list.get(position).getSongId()));
            loadBitmaps(holder.Iv_tx_zj,list.get(position).getSongId()+"");
            // }else {
            //holder.Iv_tx_gs.setBackgroundResource(R.drawable.x9);
            //}
            return convertView;
        }
    }
    public void loadBitmaps(ImageView imageView,String key) {
        Bitmap bitmap=mMemoryCache.get(key+"");
        if(bitmap==null){
            BitmapWorkerTask bitmapWorkerTask=new BitmapWorkerTask();
           bitmapWorkerTask.execute(key);
        } else{
            imageView.setImageBitmap(bitmap);
        }
    }
    class BitmapWorkerTask extends AsyncTask<String,Void,Bitmap>{
        String key;
        @Override
        protected Bitmap doInBackground(String... params) {
            key=params[0];
            Bitmap bitmap=getArtwork(key);
            mMemoryCache.put(key+"",bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView=(ImageView)Lv_localmusic_zj.findViewWithTag(key);
            if(imageView!=null&&bitmap!=null){
                imageView.setImageBitmap(bitmap);
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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
