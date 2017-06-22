package com.example.wade.inme.Fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.wade.inme.Adapter.CharstopAdapter;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CharsItem_top100 extends Fragment {
    TextView Tv_title_chars;
    ListView listView;
    CharstopAdapter charstopAdapter;
    LinearLayout linearLayout;
    ImageButton Ib_backmain;
    InMeApplicacation inMeApplicacation;
    int Num;
    String[] title=new String[]{"云音乐飙升榜","云音乐新歌榜","云音乐原创榜","云音乐热歌榜","云音乐K歌榜","内地火热榜","最美港台音",
            "欧美最前线","日本榜","韩语榜","网络歌曲榜"};
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==11){
                charstopAdapter.notifyDataSetChanged();
            }
        }
    };
    public CharsItem_top100(int Num) {
        // Required empty public constructor
        this.Num=Num;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Window window=getActivity().getWindow();
        //window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        View view=inflater.inflate(R.layout.activity_scrolling, container, false);
        Window window=getActivity().getWindow();
        inMeApplicacation=(InMeApplicacation)this.getContext().getApplicationContext();
        Tv_title_chars=(TextView)view.findViewById(R.id.tv_title_chars);
        linearLayout=(LinearLayout)view.findViewById(R.id.ll_top_chars);
        View viewheader=LayoutInflater.from(getContext()).inflate(R.layout.charstop_lan,null);
        Ib_backmain=(ImageButton)view.findViewById(R.id.ib_backmain);
        Ib_backmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fm_main,new MainFragment());
                transaction.commit();
            }
        });
        charstopAdapter=new CharstopAdapter(getContext(),Num,handler,window);
        listView=(ListView)view.findViewById(R.id.lv_chars_top100);
        listView.addHeaderView(viewheader);
        View viewHeaderView=LayoutInflater.from(getActivity()).inflate(R.layout.localplaylisthead,null);
        TextView textView=(TextView)viewHeaderView.findViewById(R.id.tv_playall);
        textView.setText("(共100首)");
        listView.addHeaderView(viewHeaderView);
        listView.setAdapter(charstopAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e("rrr",firstVisibleItem+" "+visibleItemCount+" "+totalItemCount+" ");
                View c=listView.getChildAt(0);
                if(visibleItemCount>9){
                    linearLayout.setBackgroundColor(Color.argb(255,255,0,0));
                    Tv_title_chars.setText(title[Num-1]);

                }else {
                    linearLayout.setBackgroundColor(Color.argb(0,255,0,0));
                    Tv_title_chars.setText("");

                }


            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>1&&inMeApplicacation.getNowplay()==(position-2)){

                }else {
                    inMeApplicacation.setPlaymusicList(charstopAdapter.getMusiclist());
                    inMeApplicacation.setNowplay(position-2);
                }

            }
        });

        return view;
    }

}
