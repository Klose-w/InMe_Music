package com.example.wade.inme.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.wade.inme.Adapter.MusicChartsAdapter;
import com.example.wade.inme.R;

import java.util.ArrayList;


public class MusicChartsFragment extends Fragment {
    GridView gridView;
    ListView listView ;
    ArrayList<String> strings = new ArrayList<>();
    LinearLayout frameLayout;
    LayoutInflater mInflater;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1==11){
                frameLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    public MusicChartsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chars, container, false);
        mInflater=LayoutInflater.from(getContext());
        listView = (ListView)  view.findViewById(R.id.lv_music_charts);
        frameLayout=(LinearLayout)view.findViewById(R.id.fl_all);
        View viewHeaderView=LayoutInflater.from(getActivity()).inflate(R.layout.charshead,null);
        View viewFooterView=LayoutInflater.from(getActivity()).inflate(R.layout.charsworld,null);
        gridView=(GridView)viewFooterView.findViewById(R.id.gv_wrold_sex);
        gridView.setAdapter(new Wrold_Chars());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("yyy","cil"+position);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fm_main, new CharsItem_top100(position+6));
                transaction.commit();
            }
        });
        listView.setAdapter(new MusicChartsAdapter(getContext(),handler));
        listView.addFooterView(viewFooterView);
        listView.addHeaderView(viewHeaderView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0&&position<6){
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.fm_main, new CharsItem_top100(position));
                    transaction.commit();
                }
                //Intent intent = new Intent(getActivity(), ChartsDetailAty.class);
                //intent.putExtra("MusicChartsId",ints[position]);
                //startActivity(intent);
            }
        });
        return view;
    }
    class Wrold_Chars extends BaseAdapter{
        int Imaall[]={R.drawable.china_chars,R.drawable.gt_chars,R.drawable.america,R.drawable.japan_chars,R.drawable.korea,R.drawable.online_chars};
        String worldString[]={"内地火热榜","最美港台音","欧美最前线","日本榜","韩语榜","网络流行歌曲"};
        Wrold_Chars(){

        }
        @Override
        public int getCount() {
            return 6;
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
            TextView Tv_wrold_item;
            ImageView Iv_wrold_item;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                convertView=mInflater.inflate(R.layout.item_wrold_sex,null);
                holder=new ViewHolder();
                holder.Tv_wrold_item=(TextView)convertView.findViewById(R.id.tv_wrold_item);
                holder.Iv_wrold_item=(ImageView)convertView.findViewById(R.id.iv_wrold_item);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            Glide.with(getContext()).load(Imaall[position]).into(holder.Iv_wrold_item);
            holder.Tv_wrold_item.setText(worldString[position]);
            return convertView;
        }
    }

}
