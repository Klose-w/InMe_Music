package com.example.wade.inme.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wade.inme.Activity.PlayNowMusic_Main;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.GeDanBean;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.R;

import java.util.List;


public class Recently extends Fragment {

    ListView Lv_music_rec;
    Button Bt_gs_rec;
    TextView Tv_title_rec;
    ImageButton Ib_backmain_into;
    InMeApplicacation inMeApplicacation;
    Context mContext;
    List<MusicInfor> inforList;
    int Biaoji=0;
    RecentlyAdapter recentlyAdapter;
    GeDanBean geinto;
    public Recently() {
        // Required empty public constructor
    }
    public Recently(int i) {
        // Required empty public constructor
       Biaoji=i;
    }

    public static Recently newInstance(String param1, String param2) {
        Recently fragment = new Recently();
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
        View view=inflater.inflate(R.layout.fragment_recently, container, false);
        mContext=getContext();
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
        View viewHeaderView=LayoutInflater.from(getActivity()).inflate(R.layout.localplaylisthead,null);
        Lv_music_rec=(ListView)view.findViewById(R.id.lv_music_rec);
        Bt_gs_rec=(Button)view.findViewById(R.id.bt_clean_rec);
        Tv_title_rec=(TextView)view.findViewById(R.id.tv_title_rec);
        Ib_backmain_into=(ImageButton)view.findViewById(R.id.ib_backmain_rec);
        if(Biaoji==1){
            Tv_title_rec.setText("最近播放");
            inforList=inMeApplicacation.getLisrRecPaly();
            TextView t=(TextView)viewHeaderView.findViewById(R.id.tv_playall);
            t.setText(inforList.size()+"");
        }else if(Biaoji==2){
            Tv_title_rec.setText("最近下载");
            inforList=inMeApplicacation.getLisrDowlist();
            TextView t=(TextView)viewHeaderView.findViewById(R.id.tv_playall);
            t.setText(inforList.size()+"");
        }else if(Biaoji>5){
            geinto=inMeApplicacation.getGeDanBeanList().get(Biaoji-6);
            Tv_title_rec.setText(geinto.getGdname());
            inforList=geinto.getGdlist();
            TextView t=(TextView)viewHeaderView.findViewById(R.id.tv_playall);
            t.setText(inforList.size()+"");
            Bt_gs_rec.setText("我的歌单");
        }
        Ib_backmain_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fm_main, new MainFragment());
                transaction.commit();
            }
        });
        recentlyAdapter=new RecentlyAdapter();
        Lv_music_rec.addHeaderView(viewHeaderView);
        Lv_music_rec.setAdapter(recentlyAdapter);
        Lv_music_rec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              if(Biaoji>5){
                    inMeApplicacation.setPlaymusicList(inforList);
                  int p=inforList.size()-1;
                  if(position==0){
                      inMeApplicacation.setNowplay(p);
                  }
                  else {
                      inMeApplicacation.setNowplay(p-position+1);
                  }
                }

                /*if(Biaoji==2){
                    inforList=inMeApplicacation.getLisrDowlist();
                }else {
                    inforList=inMeApplicacation.getLisrRecPaly();
                }
                recentlyAdapter.notifyDataSetChanged();*/
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    public class RecentlyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return inforList.size();
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
            position=inforList.size()-position-1;
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
            MusicInfor musicInfor;
            musicInfor=inforList.get(position);
            holder.Tv_musictitle.setText(musicInfor.getSongName());
            holder.Tv_musicatr.setText(musicInfor.getSongArtist());
            holder.Tv_musiczj.setText(musicInfor.getSongAlbum());
            return convertView;
        }
    }

}
