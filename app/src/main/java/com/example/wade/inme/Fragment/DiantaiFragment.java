package com.example.wade.inme.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wade.inme.Adapter.DiantaiAdapter;
import com.example.wade.inme.R;


public class DiantaiFragment extends Fragment {
    DiantaiAdapter adapter;
    RecyclerView Rv_dt_;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout Sr_dt;
    public DiantaiFragment() {
        // Required empty public constructor
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==11){
                if(Sr_dt.isRefreshing()==true){
                    Sr_dt.setRefreshing(false);
                }
                if(msg.arg2==17){
                    Toast.makeText(getContext(),"刷新失败,请检查网络",Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    public static DiantaiFragment newInstance(String param1, String param2) {
        DiantaiFragment fragment = new DiantaiFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_diantai, container, false);
        Rv_dt_=(RecyclerView)view.findViewById(R.id.rv_dt_);
        Sr_dt=(SwipeRefreshLayout)view.findViewById(R.id.sr_dt);
        Sr_dt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg=new Message();
                        msg.arg1=11;
                        msg.arg2=17;
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });
        layoutManager=new GridLayoutManager(Rv_dt_.getContext(),3,GridLayoutManager.VERTICAL,false);
        initView();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
   public void initView(){
       adapter=new DiantaiAdapter(getContext(),getActivity().getSupportFragmentManager(),handler);
       Rv_dt_.setLayoutManager(layoutManager);
       Rv_dt_.setAdapter(adapter);
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
