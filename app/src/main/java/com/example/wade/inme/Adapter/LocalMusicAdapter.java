package com.example.wade.inme.Adapter;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.GeDanBean;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.Network.DiskLruCache;
import com.example.wade.inme.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by wade on 2017/5/31.
 */

public class LocalMusicAdapter extends BaseAdapter{
    boolean Iszhankai=true;
    Context mContext;
    List<Map<String, Object>> listItems;
    List<Map<String, Object>> listItems2;
    Map<String, Object> listItem;
    Map<String, Object> listItem2;
    String[] lable;
    Window mwindow;
    int HightWindow;
    WindowManager wm;
    InMeApplicacation inMeApplicacation;
    int[] num;
    int[] icon;
    List<String> lable2=new ArrayList<String>();
    List<Integer> num2=new ArrayList<Integer>();
    List<Bitmap> icon2=new ArrayList<Bitmap>();
    List<Integer> back2=new ArrayList<Integer>();
    List<Integer> more2=new ArrayList<Integer>();
    PopupWindow popupWindow;
    LayoutInflater mInflater;
    List<GeDanBean> listgedanBean;
    public LocalMusicAdapter(Context context,Window window){
        mContext=context;
        mInflater=LayoutInflater.from(mContext);
        listItems = new ArrayList<Map<String, Object>>();
        lable=new String[]
                {
                        "本地音乐",
                        "最近播放",
                        "下载管理",
                        "爱上电台",
                        "我的收藏",
                };
        wm=(WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        HightWindow=wm.getDefaultDisplay().getHeight();
        mwindow=window;
        num=new int[5];
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
        listgedanBean=inMeApplicacation.getGeDanBeanList();
        num[0]=inMeApplicacation.getLocalsize();
        num[1]=inMeApplicacation.getLisrRecPaly().size();
        num[2]=inMeApplicacation.getLisrDowlist().size();
        icon=new int[]{R.drawable.lay_icn_artist,R.drawable.lay_icn_upquality,R.drawable.lay_icn_dld,R.drawable.lay_icn_gb,R.drawable.lay_icn_manage};
        for (int i = 0; i <5; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("Icon",icon[i]);
            listItem.put("Lable", lable[i]);
            listItem.put("Num", "("+num[i]+")");
            listItems.add(listItem);
        }
        listItems2 = new ArrayList<Map<String, Object>>();
        for(int k=0;k<listgedanBean.size();k++){
            lable2.add(listgedanBean.get(k).getGdname());
            num2.add(0);
            icon2.add(listgedanBean.get(k).getBitmap());
            if(k==0){
                back2.add(R.color.colorLove);
            }else {
                back2.add(R.color.colorpitong);
            }
        }
        more2.add(R.drawable.list_icn_more);
        for (int i = 0; i <lable2.size(); i++) {
            listItem2 = new HashMap<String, Object>();
            listItem2.put("Icon",icon2.get(i));
            listItem2.put("Lable", lable2.get(i));
            listItem2.put("Num", num2.get(i)+"首");
            listItem2.put("Back",back2.get(i));
            listItem2.put("More", more2.get(0));
            listItems2.add(listItem2);
        }
        //inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
    }

    public class ViewHolder1{
        public ImageView Iv_list_icon_local;
        public TextView Tv_list_label_local;
        public TextView Tv_list_num_local;
    }
    public class ViewHolder2{
        public ImageButton Ib_xianshi_list;
        public TextView Tv_listnum_pager;
        public ImageButton Ib_shezhi_list;
    }
    public class ViewHolder3{
        public ImageView Iv_list_back_pager;
        public ImageView Iv_list_icon_pager;
        public TextView Tv_list_label_pager;
        public TextView Tv_list_num_pager;
        public ImageButton Ib_list_more;
    }
    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        int p=position;
        if(p<5){
            return 1;
        }
        else if(p==5){
            return 2;
        }else{
            return 3;
        }
    }
    public void isZhanKai(){
        if(Iszhankai){
            Iszhankai=false;
        }else{
            Iszhankai=true;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(Iszhankai)
        {
            return listgedanBean.size()+listItems.size()+1;
        }else{
            return listItems.size()+1;
        }
    }
    public void changelist2(int id,String name) {
        List<MusicInfor> ll=new ArrayList<>();
        GeDanBean m=new GeDanBean(listgedanBean.size(),ll,name,BitmapFactory.decodeResource(mContext.getResources(),R.drawable.note_btn_love),"侠客孤");
        listgedanBean.add(m);
        inMeApplicacation.setGeDanBeanList(listgedanBean);
        Log.e("Tianjia",listgedanBean.size()+"");
        //listItems2 = new ArrayList<Map<String, Object>>();
        listItem2 = new HashMap<String, Object>();
        listItem2.put("Icon",m.getBitmap());
        listItem2.put("Lable", m.getGdname());
        listItem2.put("Num", m.getGdlist().size()+"首");
        listItem2.put("Back",back2.get(0));
        listItem2.put("More", more2.get(0));
        listItems2.add(listItem2);
        notifyDataSetChanged();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1=null;
        ViewHolder2 holder2=null;
        ViewHolder3 holder3=null;
        int type=getItemViewType(position);
        //Log.e("dd",type+" "+position);
        if(convertView==null||position==5||position==6){
            switch (type){
                case 1:
                    convertView=mInflater.inflate(R.layout.main_top_local_listitem,null);
                    holder1=new ViewHolder1();
                    holder1.Iv_list_icon_local=(ImageView)convertView.findViewById(R.id.iv_list_icon_local);
                    holder1.Tv_list_label_local=(TextView)convertView.findViewById(R.id.tv_list_label_local);
                    holder1.Tv_list_num_local=(TextView)convertView.findViewById(R.id.tv_list_num_local);
                    convertView.setTag(holder1);
                    break;
                case 2:
                    convertView=mInflater.inflate(R.layout.music_shezhi_list,null);
                    holder2=new ViewHolder2();
                    holder2.Ib_xianshi_list=(ImageButton)convertView.findViewById(R.id.ib_xianshi_list);
                    holder2.Tv_listnum_pager=(TextView)convertView.findViewById(R.id.tv_listnum_pager);
                    holder2.Ib_shezhi_list=(ImageButton) convertView.findViewById(R.id.ib_shezhi_list);
                    convertView.setTag(holder2);
                    break;
                case 3:
                    convertView=mInflater.inflate(R.layout.musicpager_list_local,null);
                    holder3=new ViewHolder3();
                    holder3.Iv_list_icon_pager=(ImageView)convertView.findViewById(R.id.iv_list_icon_pager);
                    holder3.Iv_list_back_pager=(ImageView)convertView.findViewById(R.id.iv_list_back_pager);
                    holder3.Ib_list_more=(ImageButton) convertView.findViewById(R.id.ib_list_more);
                    holder3.Tv_list_label_pager=(TextView)convertView.findViewById(R.id.tv_list_label_pager);
                    holder3.Tv_list_num_pager=(TextView)convertView.findViewById(R.id.tv_list_num_pager);
                    convertView.setTag(holder3);
                    break;
            }
        }else{
            switch (type){
                case 1:
                    holder1=(ViewHolder1)convertView.getTag();
                    break;
                case 2:
                    holder2=(ViewHolder2)convertView.getTag();
                    break;
                case 3:
                    holder3=(ViewHolder3)convertView.getTag();
                    break;
            }
        }
        switch (type){
            case 1:
                holder1.Iv_list_icon_local.setBackgroundResource((int)listItems.get(position).get("Icon"));
                holder1.Tv_list_label_local.setText((String)listItems.get(position).get("Lable"));
                holder1.Tv_list_num_local.setText((String)listItems.get(position).get("Num"));
                break;
            case 2:
                holder2.Tv_listnum_pager.setText("("+listgedanBean.size()+")");
                holder2.Ib_shezhi_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //changelist2(-1,"古风");
                        //Toast.makeText(mContext,""+getCount(),Toast.LENGTH_SHORT).show();
                        View view=LayoutInflater.from(mContext).inflate(R.layout.gdpopwindow,null);
                        TextView Tv_title_pop=(TextView)view.findViewById(R.id.tv_title_pop);
                        TextView Tv_one_pop=(TextView)view.findViewById(R.id.tv_one_pop);
                        ImageView Iv_one_pop=(ImageView)view.findViewById(R.id.iv_one_pop);
                        TextView Tv_two_pop=(TextView)view.findViewById(R.id.tv_one_pop);
                        ImageView Iv_two_pop=(ImageView)view.findViewById(R.id.iv_two_pop);
                        Tv_one_pop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                                popDialog();
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
                });
                if(Iszhankai){
                    holder2.Ib_xianshi_list.setBackgroundResource(R.drawable.list_icn_arr_down);
                }else{
                    holder2.Ib_xianshi_list.setBackgroundResource(R.drawable.list_icn_arr_right);
                }
                holder2.Ib_xianshi_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       isZhanKai();
                    }
                });

                break;
            case 3:
                holder3.Iv_list_icon_pager.setImageBitmap(getArtwork(listgedanBean.get(position-lable.length-1).getGdlist()));
                holder3.Iv_list_back_pager.setBackgroundResource((int)listItems2.get(position-lable.length-1).get("Back"));
                holder3.Tv_list_label_pager.setText((String)listgedanBean.get(position-lable.length-1).getGdname());
                //holder3.Tv_list_num_pager.setText(listgedanBean.get(position-lable.length-1).getGdlist().size()+"首");
                holder3.Tv_list_num_pager.setText(listgedanBean.get(position-lable.length-1).getGdlist().size()+"首");
                holder3.Ib_list_more.setBackgroundResource((int)listItems2.get(0).get("More"));
                holder3.Ib_list_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext,"dianji",Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
       // Log.e("dd",getCount()+"");
        return convertView;
    }
    public void popDialog(){
        View layout=LayoutInflater.from(mContext).inflate(R.layout.gddialog,null);
        final EditText Et_get_dia=(EditText)layout.findViewById(R.id.et_get_dia);
        TextView Tv_btu_del=(TextView)layout.findViewById(R.id.tv_btu_del);
        TextView Tv_bt_sure=(TextView)layout.findViewById(R.id.tv_bt_sure);
       final AlertDialog dialog= new AlertDialog.Builder(mContext).setView(layout).create();
        dialog.show();
        WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
        params.width=900;
        params.height=700;
        dialog.getWindow().setAttributes(params);
        Tv_bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changelist2(-1,Et_get_dia.getText().toString());
                dialog.dismiss();
            }
        });
        Tv_btu_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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
                //b=new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_210));
                return bmm;
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
                //b=new BitmapDrawable(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.placeholder_disk_210));
                return bmm;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bm;
    }
}
