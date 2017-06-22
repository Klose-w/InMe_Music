package com.example.wade.inme.Adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.R;

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
    int[] num;
    int[] icon;
    List<String> lable2=new ArrayList<String>();
    List<Integer> num2=new ArrayList<Integer>();
    List<Integer> icon2=new ArrayList<Integer>();
    List<Integer> back2=new ArrayList<Integer>();
    List<Integer> more2=new ArrayList<Integer>();
    LayoutInflater mInflater;
    public LocalMusicAdapter(Context context){
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
        num=new int[5];
        InMeApplicacation inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
        num[0]=inMeApplicacation.getLocalsize();
        icon=new int[]{R.drawable.lay_icn_artist,R.drawable.lay_icn_upquality,R.drawable.lay_icn_dld,R.drawable.lay_icn_gb,R.drawable.lay_icn_manage};
        for (int i = 0; i <5; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("Icon",icon[i]);
            listItem.put("Lable", lable[i]);
            listItem.put("Num", "("+num[i]+")");
            listItems.add(listItem);
        }
        listItems2 = new ArrayList<Map<String, Object>>();
        lable2.add("我喜欢的音乐");
        lable2.add("其他");
        num2.add(3);
        num2.add(12);
        icon2.add(R.drawable.note_btn_love);
        icon2.add(R.drawable.placeholder_disk_play_program);
        back2.add(R.color.colorLove);
        back2.add(R.color.colorpitong);
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
            return listItems2.size()+listItems.size()+1;
        }else{
            return listItems.size()+1;
        }
    }
    public void changelist2(int id,String name) {
        if (id == -1) {
            HashMap<String,Object> addlist = new HashMap<String, Object>();
            addlist.put("Icon",R.drawable.placeholder_disk_play_program);
            addlist.put("Lable", name);
            addlist.put("Num", 0+"首");
            addlist.put("Back",R.color.colorpitong);
            addlist.put("More", more2.get(0));
            listItems2.add(addlist);
        }
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
                holder2.Tv_listnum_pager.setText("("+listItems2.size()+")");
                holder2.Ib_shezhi_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changelist2(-1,"古风");
                        Toast.makeText(mContext,""+getCount(),Toast.LENGTH_SHORT).show();
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
                holder3.Iv_list_icon_pager.setBackgroundResource((int)listItems2.get(position-lable.length-1).get("Icon"));
                holder3.Iv_list_back_pager.setBackgroundResource((int)listItems2.get(position-lable.length-1).get("Back"));
                holder3.Tv_list_label_pager.setText((String)listItems2.get(position-lable.length-1).get("Lable"));
                holder3.Tv_list_num_pager.setText((String)listItems2.get(position-lable.length-1).get("Num"));
                holder3.Ib_list_more.setBackgroundResource((int)listItems2.get(position-lable.length-1).get("More"));
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
}
