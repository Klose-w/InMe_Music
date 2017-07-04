package com.example.wade.inme.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wade.inme.Activity.NewsActivity;
import com.example.wade.inme.Adapter.NewsAdapter;
import com.example.wade.inme.JavaBean.NewsBean;
import com.example.wade.inme.JavaBean.musicbean;
import com.example.wade.inme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsFragment extends Fragment {


    private RecyclerView recyclerView;
    private String showapi_appid = "39401";
    private String showapi_sign = "3e47b57274ff414f81e76f624a6a1231";
    NewsAdapter newsAdapter;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout Sr_news;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==11){
                if(Sr_news.isRefreshing()==true){
                    Sr_news.setRefreshing(false);
                }
                if(msg.arg2==17){
                    Toast.makeText(getContext(),"刷新失败,请检查网络",Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    public NewsFragment() {

    }


    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.rv_news_list);
        layoutManager=new GridLayoutManager(recyclerView.getContext(),2,GridLayoutManager.VERTICAL,false);
        initView();
        Sr_news=(SwipeRefreshLayout)view.findViewById(R.id.sr_news);
        Sr_news.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if(position>0){
                    NewsBean mm=newsAdapter.getBean(position-1);
                    Intent intent=new Intent(getActivity(), NewsActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("news",mm);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
        return view;
    }
    public void initView(){
        newsAdapter=new NewsAdapter(getContext(),handler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newsAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}
