package com.example.wade.inme.Fragment;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.wade.inme.Activity.WebActivity;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.GdJsonArray;
import com.example.wade.inme.JavaBean.GeDanBean;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.JavaBean.OnlineGdBean;
import com.example.wade.inme.R;

import net.qiujuer.genius.blur.StackBlur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class GdDiaplayFragment extends Fragment{
    ListView Lv_gd_list;
    TextView Tv_gd_name;
    ImageButton Ib_backmain;
    InMeApplicacation inMeApplicacation;
    Context mContext;
    List<MusicInfor> inforList;
    List<OnlineGdBean> gdBeenlist;
    RecentlyAdapter recentlyAdapter;
    GeDanBean geinto;
    JSONObject jsonObject;
    GdJsonArray gdJsonArray;
    int Biaoji=0;
    int BI=0;
    LinearLayout Ll_bar_ll;
    String name1;
    public GdDiaplayFragment() {

    }
    public GdDiaplayFragment(int i) {
        // Required empty public constructor
        BI=0;
        Biaoji=i;
    }
    public GdDiaplayFragment(int i,int k) {
        // Required empty public constructor

        Biaoji=i;
        BI=k;
        gdJsonArray=new GdJsonArray();
        jsonObject=gdJsonArray.getGdlistjson()[i];
    }
    public static GdDiaplayFragment newInstance(String param1, String param2) {
        GdDiaplayFragment fragment = new GdDiaplayFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getActivity().getWindow();
        //window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.requestFeature(Window.FEATURE_NO_TITLE);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_gd_diaplay, container, false);
        mContext=getContext();
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
        Lv_gd_list=(ListView)view.findViewById(R.id.lv_gd_list);
        Tv_gd_name=(TextView)view.findViewById(R.id.tv_gd_name);
        Ib_backmain=(ImageButton)view.findViewById(R.id.ib_backmain);
        Ll_bar_ll=(LinearLayout)view.findViewById(R.id.ll_bar_ll);
        View viewHeaderView=LayoutInflater.from(getActivity()).inflate(R.layout.localplaylisthead,null);
         if(Biaoji>-1&&BI==0){
            geinto=inMeApplicacation.getGeDanBeanList().get(Biaoji);
            inforList=geinto.getGdlist();
            TextView t=(TextView)viewHeaderView.findViewById(R.id.tv_playall);
            t.setText(" 共("+inforList.size()+")首");
        }else if(BI==1){
             initgedan();
             TextView t=(TextView)viewHeaderView.findViewById(R.id.tv_playall);
             t.setText(" 共("+gdBeenlist.size()+")首");
         }
        View viewHeaderView1=LayoutInflater.from(getActivity()).inflate(R.layout.gdheadview,null);
        initHeadview(viewHeaderView1);
        Ib_backmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fm_main, new MainFragment());
                transaction.commit();
            }
        });
        recentlyAdapter=new RecentlyAdapter();
        Lv_gd_list.addHeaderView(viewHeaderView1);
        Lv_gd_list.addHeaderView(viewHeaderView);
        Lv_gd_list.setAdapter(recentlyAdapter);
        Lv_gd_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Biaoji>-1&&BI==0){
                    inMeApplicacation.setPlaymusicList(inforList);
                    if(position==1){
                        inMeApplicacation.setNowplay(position-1);
                    }
                    else if(position>1){
                        inMeApplicacation.setNowplay(position-2);
                    }
                }else {
                    Intent intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url",gdBeenlist.get(position-1).getS_url());
                    intent.putExtra("name",gdBeenlist.get(position-1).getS_name());
                    startActivity(intent);
                }

                /*if(Biaoji==2){
                    inforList=inMeApplicacation.getLisrDowlist();
                }else {
                    inforList=inMeApplicacation.getLisrRecPaly();
                }
                recentlyAdapter.notifyDataSetChanged();*/
            }
        });
        Lv_gd_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                View c=Lv_gd_list.getChildAt(0);
                if(visibleItemCount>9){
                    Ll_bar_ll.setBackgroundColor(Color.argb(255,255,0,0));
                    Tv_gd_name.setText(name1);

                }else {
                    Ll_bar_ll.setBackgroundColor(Color.argb(0,255,0,0));
                    Tv_gd_name.setText("歌单");

                }
            }
        });
        return view;
    }

   public void initgedan(){
       try {
           gdBeenlist=new ArrayList<>();
           JSONArray j=jsonObject.getJSONArray("songs");
           int ss=j.length();
           for(int i=0;i<ss;i++){
               JSONObject jj=j.getJSONObject(i);
               OnlineGdBean n=new OnlineGdBean();
               n.setS_name(jj.getString("song_name"));
               n.setS_author(jj.getString("song_author"));
               n.setS_album(jj.getString("song_album"));
               n.setS_pic(jj.getString("song_album_pic"));
               n.setS_url(jj.getString("song_url"));
               gdBeenlist.add(n);
           }
       } catch (JSONException e) {
           e.printStackTrace();
       }
   }

   public void initHeadview(View view){
       final ImageView Iv_back_gd=(ImageView)view.findViewById(R.id.iv_back_gd);
       ImageView Iv_title_gd=(ImageView)view.findViewById(R.id.iv_title_gd);
       TextView Tv_tit_nn=(TextView)view.findViewById(R.id.tv_tit_nn) ;
       TextView Tv_user_name=(TextView)view.findViewById(R.id.tv_user_name);
       TextView Tv_sc_gdd=(TextView)view.findViewById(R.id.tv_sc_gdd) ;
       TextView Tv_pl_gdd=(TextView)view.findViewById(R.id.tv_pl_gdd);
       TextView Tv_fx_gdd=(TextView)view.findViewById(R.id.tv_fx_gdd) ;
       TextView Tv_xz_gdd=(TextView)view.findViewById(R.id.tv_xz_gdd);
       //Glide.with(mContext).load(new BitmapDrawable(  StackBlur.blurNatively(getArtwork(geinto.getGdlist()),280,false))).into(Iv_back_gd);
       //Glide.with(mContext).load(new BitmapDrawable(getArtwork(geinto.getGdlist()))).into(Iv_title_gd);
       if(BI==0){
           Iv_back_gd.setBackground(new BitmapDrawable(  StackBlur.blurNatively(getArtwork(geinto.getGdlist()),280,false)));
           Iv_title_gd.setBackground(new BitmapDrawable(getArtwork(geinto.getGdlist())));
           Tv_tit_nn.setText(geinto.getGdname());
           Tv_user_name.setText(geinto.getUsername());
           name1=geinto.getGdname();
       }else {
           //Iv_back_gd.setBackground(new BitmapDrawable(  StackBlur.blurNatively(getArtwork(geinto.getGdlist()),280,false)));
           try {
               Glide.with(mContext).load(jsonObject.getString("cover_img")).asBitmap().into(new SimpleTarget<Bitmap>(){

                   @Override
                   public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                       Bitmap bitmap= Bitmap.createScaledBitmap(resource,10,7,true);
                        Bitmap bitmap1=StackBlur.blurNatively(bitmap,380,false);
                       Drawable drawable=new BitmapDrawable(bitmap1);
                       Iv_back_gd.setBackground(drawable);
                   }
               });
               Tv_sc_gdd.setText(jsonObject.getString("collect_count"));
               Tv_pl_gdd.setText(jsonObject.getString("collect_count"));
               Tv_fx_gdd.setText(jsonObject.getString("share_count"));
               Tv_xz_gdd.setText(jsonObject.getString("comment_count"));
               Glide.with(mContext).load(jsonObject.getString("cover_img")).into(Iv_title_gd);
               Tv_tit_nn.setText(jsonObject.getString("name"));
               name1=jsonObject.getString("name");
               Tv_user_name.setText(jsonObject.getString("author"));
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }

   }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    public class RecentlyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(BI==0){
                return inforList.size();
            }else {
                return gdBeenlist.size();
            }

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
            TextView Tv_musictitle;
            ImageView Iv_musicicon;
            TextView Tv_musicatr;
            TextView Tv_musiczj;
            ImageButton Ib_music_more;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                convertView=LayoutInflater.from(mContext).inflate(R.layout.music_list_xianshi,null);
                holder= new ViewHolder();
                holder.Tv_musictitle=(TextView) convertView.findViewById(R.id.tv_musictitle);
                holder.Iv_musicicon=(ImageView) convertView.findViewById(R.id.iv_musicicon);
                holder.Tv_musicatr=(TextView)convertView.findViewById(R.id.tv_musicatr);
                holder.Tv_musiczj=(TextView)convertView.findViewById(R.id.tv_musiczj);
                holder.Ib_music_more=(ImageButton) convertView.findViewById(R.id.ib_music_more);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            if(BI==0){
                MusicInfor musicInfor;
                musicInfor=inforList.get(position);
                holder.Tv_musictitle.setText(musicInfor.getSongName());
                holder.Tv_musicatr.setText(musicInfor.getSongArtist());
                holder.Tv_musiczj.setText(musicInfor.getSongAlbum());
            }else {
                OnlineGdBean gd=gdBeenlist.get(position);
                holder.Tv_musictitle.setText(gd.getS_name());
                holder.Tv_musicatr.setText(gd.getS_author());
                holder.Tv_musiczj.setText(gd.getS_author());
            }

            return convertView;
        }
    }
    public Bitmap getArtwork(List<MusicInfor> m){
        String songId=null;
        if(m.size()==0){
            return BitmapFactory.decodeResource(mContext.getResources(),R.drawable.note_btn_love);
        }else {
            songId=m.get(0).getSongId()+"";
        }
        Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
        long albumId = -1;
        Bitmap bmp1 = null;
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
                Bitmap bmm=BitmapFactory.decodeResource(mContext.getResources(),R.drawable.playnowback);
                bmp1=Bitmap.createScaledBitmap(bmm,200,200,true);
                //b=new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_210));
                return bmp1;
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
                Bitmap bmm=BitmapFactory.decodeResource(mContext.getResources(),R.drawable.playnowback);
                bmp1=Bitmap.createScaledBitmap(bmm,200,200,true);
                //b=new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_210));
                return bmp1;
            }
            bmp1=Bitmap.createScaledBitmap(bm,200,200,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp1;
    }
}
