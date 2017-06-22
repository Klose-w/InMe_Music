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
import com.example.wade.inme.Adapter.FindMusicAdapter;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class IntoMusicFragment extends Fragment {
    ListView Lv_music_into;
    List<MusicInfor> inforList;
    List<MusicInfor> diaplaylist;
    DisplayAdapter displayAdapter;
    Button Bt_gs_into;
    TextView Tv_title_into;
    ImageButton Ib_backmain_into;
    InMeApplicacation inMeApplicacation;
    Context mContext;
    int biaoji;
    String Key;
    int musicnum=0;
    public IntoMusicFragment() {

    }
    public IntoMusicFragment(int i,String key) {
        biaoji=i;
        Key=key;
    }
    public void Findmusic(int bj,String key){
        int num=inforList.size();
        diaplaylist=new ArrayList<MusicInfor>();
        switch (bj){
            case 0:
                for(int i=0;i<num;i++){
                    if(inforList.get(i).getSongArtist().equals(key)){
                        musicnum++;
                        diaplaylist.add(inforList.get(i));
                    }
                }
                Tv_title_into.setText(key);
                break;
            case 1:
                for(int i=0;i<num;i++){
                    if(inforList.get(i).getSongAlbum().equals(key)){
                        musicnum++;
                        diaplaylist.add(inforList.get(i));
                    }
                }
                Tv_title_into.setText(key);
                Bt_gs_into.setText("");
                break;
            case 2:
                for(int i=0;i<num;i++){
                    File file=new File(inforList.get(i).getSongUrl());
                    if(file.getParent().equals(key)){
                        musicnum++;
                        diaplaylist.add(inforList.get(i));
                    }
                }
                File f=new File(key);
                Tv_title_into.setText(f.getName());
                Bt_gs_into.setText("");
                break;
        }

    }
    public static IntoMusicFragment newInstance(String param1, String param2) {
        IntoMusicFragment fragment = new IntoMusicFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_into_music, container, false);
        mContext=getContext();
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
        inforList=inMeApplicacation.getLocalmusicList();
        View viewHeaderView=LayoutInflater.from(getActivity()).inflate(R.layout.localplaylisthead,null);
        Lv_music_into=(ListView)view.findViewById(R.id.lv_music_into);
        Bt_gs_into=(Button)view.findViewById(R.id.bt_gs_into);
        Tv_title_into=(TextView)view.findViewById(R.id.tv_title_into);
        Ib_backmain_into=(ImageButton)view.findViewById(R.id.ib_backmain_into);
        Ib_backmain_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fm_main, new LocalMusicFragment());
                transaction.commit();
            }
        });
        Findmusic(biaoji,Key);
        TextView tt=(TextView)viewHeaderView.findViewById(R.id.tv_playall);
        tt.setText(+musicnum+"首)");
        displayAdapter=new DisplayAdapter();
        Lv_music_into.addHeaderView(viewHeaderView);
        Lv_music_into.setAdapter(displayAdapter);
        Lv_music_into.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inMeApplicacation.setPlaymusicList(diaplaylist);
                if(position==0){
                    inMeApplicacation.setNowplay(0);
                }
                else if(inMeApplicacation.getNowmusic().getSongId()==inMeApplicacation.getPlaymusicList().get(position-1).getSongId()){
                    Intent intent=new Intent(getActivity(), PlayNowMusic_Main.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("nowmusic",inMeApplicacation.getNowmusic());
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
                else {
                    inMeApplicacation.setNowplay(position-1);
                }

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
    public class DisplayAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return diaplaylist.size();
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
                holder=new ViewHolder();
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
            musicInfor=diaplaylist.get(position);
            holder.Tv_musictitle.setText(musicInfor.getSongName());
            holder.Tv_musicatr.setText(musicInfor.getSongArtist());
            holder.Tv_musiczj.setText(musicInfor.getSongAlbum());
            return convertView;
        }
    }
}
