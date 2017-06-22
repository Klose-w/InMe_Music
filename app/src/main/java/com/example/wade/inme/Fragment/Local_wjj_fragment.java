package com.example.wade.inme.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Local_wjj_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Local_wjj_fragment extends Fragment {
    List<MusicInfor> inforList;
    InMeApplicacation inMeApplicacation;
    Context mContext;
    ListView Lv_local_wjj;
    List<jilu> list;
    WjjAdapter wjjAdapter;
    public Local_wjj_fragment() {

    }

    public static Local_wjj_fragment newInstance(String param1, String param2) {
        Local_wjj_fragment fragment = new Local_wjj_fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_local_wjj_fragment, container, false);
        mContext=getContext();
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
        inforList=inMeApplicacation.getLocalmusicList();
        list=new ArrayList<jilu>();
        Tongji();
        wjjAdapter=new WjjAdapter(list,mContext);
        Lv_local_wjj=(ListView)view.findViewById(R.id.lv_local_wjj);
        Lv_local_wjj.setAdapter(wjjAdapter);
        Lv_local_wjj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fm_main, new IntoMusicFragment(2,list.get(position).getUrl()));
                transaction.commit();
            }
        });
        return view;
    }
    class jilu{
        private int num=0;
        private String url;
        public jilu(int num,String url){
            this.num=num;
            this.url=url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }


    }
    public void Tongji(){
        Collections.sort(inforList, new Comparator<MusicInfor>() {
            @Override
            public int compare(MusicInfor o1, MusicInfor o2) {
                return o1.getSongUrl().compareTo(o2.getSongUrl());
            }
        });
        String url=null;
        int num=0;
        int size=inforList.size();
        for(int i=0;i<size;i++){
            File file=new File(inforList.get(i).getSongUrl());
            Log.e("file",file.toString());
            if(!file.getParent().equals(url)){
                if(url!=null){
                    jilu j=new jilu(num,url);
                    list.add(j);
                }
                url=file.getParent();
                num=1;
            }else {
                num++;
            }
            if(i==size-1){
                jilu j=new jilu(num,url);
                list.add(j);
            }
        }
    }
    public class WjjAdapter extends BaseAdapter {
        List<jilu> list;
        Context mContext;
        public WjjAdapter(List<jilu> list, Context mContext) {
            this.list=list;
            this.mContext=mContext;
        }


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
        public class ViewHolder{
            TextView Tv_art_wjj;
            TextView Tv_num_wjj;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.e("adapter","addd");
            ViewHolder holder = null;
            if(convertView==null){
                convertView= LayoutInflater.from(mContext).inflate(R.layout.wjj_local_item,null);
                holder=new ViewHolder();
                holder.Tv_art_wjj=(TextView)convertView.findViewById(R.id.tv_art_wjj);
                holder.Tv_num_wjj=(TextView)convertView.findViewById(R.id.tv_num_wjj);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }
            String uu=list.get(position).getUrl();
            File file=new File(uu);
            uu=file.getParent();
            holder.Tv_art_wjj.setText(file.getName());
            holder.Tv_num_wjj.setText(list.get(position).getNum()+"首"+uu);
            return convertView;
        }
    }


}
