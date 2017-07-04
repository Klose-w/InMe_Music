package com.example.wade.inme.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.wade.inme.Adapter.MusicChartsAdapter;
import com.example.wade.inme.JavaBean.Image;
import com.example.wade.inme.R;

import java.util.ArrayList;


public class MusicChartsFragment extends Fragment {
    GridView gridView;
    ListView listView ;
    ArrayList<String> strings = new ArrayList<>();
    LinearLayout frameLayout;
    View in_pop1;
    LayoutInflater mInflater;
    ProgressDialog progressDialog;
    PopupWindow popupWindow;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1==11){
                in_pop1.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }else if(msg.arg1==15){
                if(in_pop1.VISIBLE==View.VISIBLE){
                    ImageView iv=(ImageView)in_pop1.findViewById(R.id.iv_dh_dia);
                    iv.setVisibility(View.GONE);
                    TextView tv=(TextView) in_pop1.findViewById(R.id.tv_wz_dia);
                    tv.setText("点击屏幕重新加载");
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initView();
                        }
                    });
                }
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
       // View EmptyView=LayoutInflater.from(getActivity()).inflate(R.layout.charshead,null);
        //listView.setEmptyView(EmptyView);
        frameLayout=(LinearLayout)view.findViewById(R.id.fl_all);
        View viewHeaderView=LayoutInflater.from(getActivity()).inflate(R.layout.charshead,null);
        View viewFooterView=LayoutInflater.from(getActivity()).inflate(R.layout.charsworld,null);
        in_pop1=(View)view.findViewById(R.id.in_pop1);
        gridView=(GridView)viewFooterView.findViewById(R.id.gv_wrold_sex);
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
        //initPopwindow();
        initView();

        return view;
    }

    public void initPopwindow(){
        View view= LayoutInflater.from(getContext()).inflate(R.layout.popdialog,null);
        ImageView iv=(ImageView)view.findViewById(R.id.iv_dh_dia);
        iv.setVisibility(View.VISIBLE);
        iv.setImageResource(R.drawable.dohuang_jiazai);
        AnimationDrawable animationDrawable=(AnimationDrawable)iv.getDrawable();
        animationDrawable.start();
        popupWindow=new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(0,255,255,255)));
    }
    public void initView(){
       // progressDialog = ProgressDialog.show(getActivity(), "正在加载", "Please wait...", true, false);
       // popupWindow.setFocusable(true);
      //  popupWindow.setOutsideTouchable(true);

       // popupWindow.showAtLocation(view, Gravity.TOP|Gravity.LEFT,700,400);
        ImageView iv=(ImageView)in_pop1.findViewById(R.id.iv_dh_dia);
        iv.setVisibility(View.VISIBLE);
        iv.setImageResource(R.drawable.dohuang_jiazai);
        AnimationDrawable animationDrawable=(AnimationDrawable)iv.getDrawable();
        animationDrawable.start();
        TextView tv=(TextView) in_pop1.findViewById(R.id.tv_wz_dia);
        tv.setText("正在加载....");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg=new Message();
                msg.arg1=15;
                handler.sendMessage(msg);
            }
        }).start();
        gridView.setAdapter(new Wrold_Chars());
        listView.setAdapter(new MusicChartsAdapter(getContext(),handler));

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
