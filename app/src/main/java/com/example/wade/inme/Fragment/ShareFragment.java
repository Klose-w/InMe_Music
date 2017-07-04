package com.example.wade.inme.Fragment;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wade.inme.Activity.StartShareActivity;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.Image;
import com.example.wade.inme.JavaBean.ShareBean;
import com.example.wade.inme.Network.HttpThread;
import com.example.wade.inme.R;
import com.example.wade.inme.ScreenTools;
import com.example.wade.inme.UIwedget.CircleImageView;
import com.example.wade.inme.UIwedget.CustomImageView;
import com.example.wade.inme.UIwedget.MyRefreshRecyclerView;
import com.example.wade.inme.UIwedget.NineGridlayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.b.I;


/**
 * Created by Lenovo on 2017/6/26.
 */

public class ShareFragment extends Fragment {
    InMeApplicacation inMeApplicacation;
    private ArrayList<ShareBean> list = new ArrayList<>();
    private String[] typelist = new String[]{"分享单曲："};
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyRefreshRecyclerView recyclerView;
    private String getShareUrl;
    private String addvoteup;
    //private List<String> integerList = new ArrayList<>();
    private MyAdapter myAdapter;
    private Handler mHandler = new Handler();
    private FloatingActionButton button;
    private View view;
    private JSONArray jsonArray;
    private JSONObject jsonObject;

    private List<List<Image>> imagesList;
    private String[][] images = new String[][]{{"http://t2.27270.com/uploads/tu/201606/76/32.jpg", "640", "960"}
            , {"http://t2.27270.com/uploads/tu/201606/76/32.jpg", "640", "640"}
            , {"http://t2.27270.com/uploads/tu/201606/76/32.jpg", "640", "640"}
            , {"http://t2.27270.com/uploads/tu/201606/76/32.jpg", "640", "640"}
            , {"http://t2.27270.com/uploads/tu/201606/76/32.jpg", "640", "640"}
            , {"http://t2.27270.com/uploads/tu/201606/76/32.jpg", "640", "640"}
            , {"http://t2.27270.com/uploads/tu/201606/76/32.jpg", "640", "640"}
            , {"http://t2.27270.com/uploads/tu/201606/76/32.jpg", "640", "640"}
            , {"http://t2.27270.com/uploads/tu/201606/76/32.jpg", "640", "640"}
            , {"http://t2.27270.com/uploads/tu/201606/76/32.jpg", "640", "640"}
            , {"http://t2.27270.com/uploads/tu/201606/76/32.jpg", "640", "640"}
            , {"http://t2.27270.com/uploads/tu/201606/76/32.jpg", "800", "650"}};

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String result = bundle.getString("json");
            try {
                jsonArray = new JSONArray(result);
                Log.i("TTT","+++++++++++++++++++++4"+jsonArray);
                for(int i = 0;i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    ShareBean shareBean = new ShareBean();
                    Log.i("TTT","+++++++++++++++++++++5"+jsonObject);
                    shareBean.setShareid(jsonObject.getString("share_id"));
                    shareBean.setUserid(jsonObject.getString("user_id"));
                    shareBean.setPhone(jsonObject.getString("phone"));
                    shareBean.setAvatar(jsonObject.getString("avatar"));
                    shareBean.setNickname(jsonObject.getString("nickname"));
                    shareBean.setMusicid(jsonObject.getString("music_id"));
                    shareBean.setType(jsonObject.getString("share_type"));
                    shareBean.setContent(jsonObject.getString("content_text"));
                    shareBean.setTime(jsonObject.getString("share_time"));
                    shareBean.setVoteup_num(Integer.parseInt(jsonObject.getString("voteup_num")));
                    shareBean.setComment_num(Integer.parseInt(jsonObject.getString("comment_num")));

                    list.add(shareBean);
                }
                recyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler addvotehandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            refresh();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_share, container,false);
        inMeApplicacation=(InMeApplicacation)getContext().getApplicationContext();
        getShareUrl= inMeApplicacation.getUrl()+"get_share.php";
        addvoteup =inMeApplicacation.getUrl()+"addvoteup.php";
        new HttpThread(handler,getShareUrl,"null").start();
        init();
        nine_init();
        return view;
    }

    private void nine_init() {
        imagesList = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            ArrayList<Image> itemList = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                itemList.add(new Image(images[j][0], Integer.parseInt(images[j][1]), Integer.parseInt(images[j][2])));
            }
            imagesList.add(itemList);
        }

    }

    private void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        button = (FloatingActionButton) view.findViewById(R.id.fab_startshare);
        recyclerView = (MyRefreshRecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        myAdapter = new MyAdapter();
        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.orange);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        refresh();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StartShareActivity.class));
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

    }

    private void refresh() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                new HttpThread(handler,getShareUrl,"null").start();

            }
        }, 1500);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sharemusic, parent, false);

            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            List<Image> itemList = imagesList.get(position);
            int type = Integer.parseInt(list.get(position).getType());
            holder.tv_type.setText(typelist[type-1]);
            holder.tv_name.setText(list.get(position).getNickname());
            holder.tv_time.setText(list.get(position).getTime());
            holder.tv_content.setText(list.get(position).getContent());
            holder.tv_voteup.setText(String.valueOf(list.get(position).getVoteup_num()));
            holder.tv_comment.setText(String.valueOf(list.get(position).getComment_num()));

            holder.iv_voteup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,String> vote=new HashMap<String,String>();
                    vote.put("shareid", list.get(position).getShareid());
                    vote.put("userid",inMeApplicacation.getUserBean().getId());
                    JSONObject params=new JSONObject(vote);

                    new HttpThread(addvotehandler,addvoteup,params.toString()).start();
                    holder.iv_voteup.setImageResource(R.drawable.a7t);
                }
            });

            if(list.get(position).getAvatar().equals("null")){
                Glide.with(getContext()).load(R.drawable.a3).into(holder.avatar);

            }else {
                Glide.with(getContext()).load(inMeApplicacation.getUrl()+list.get(position).getAvatar()).into(holder.avatar);

            }

            if (itemList.isEmpty() ) {
                holder.ivMore.setVisibility(View.GONE);
                holder.ivOne.setVisibility(View.GONE);
            } else if (itemList.size() == 1) {
                holder.ivMore.setVisibility(View.GONE);
                holder.ivOne.setVisibility(View.VISIBLE);

                handlerOneImage(holder, itemList.get(0));
            } else {
                holder.ivMore.setVisibility(View.VISIBLE);
                holder.ivOne.setVisibility(View.GONE);

                holder.ivMore.setImagesData(itemList);
            }


            holder.itemView.setTag(position);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private void handlerOneImage(MyViewHolder viewHolder, Image image) {
        int totalWidth;
        int imageWidth;
        int imageHeight;
        ScreenTools screentools = ScreenTools.instance(getContext());
        totalWidth = screentools.getScreenWidth() - screentools.dip2px(80);
        imageWidth = screentools.dip2px(image.getWidth());
        imageHeight = screentools.dip2px(image.getHeight());
        if (image.getWidth() <= image.getHeight()) {
            if (imageHeight > totalWidth) {
                imageHeight = totalWidth;
                imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
            }
        } else {
            if (imageWidth > totalWidth) {
                imageWidth = totalWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }
        }
        ViewGroup.LayoutParams layoutparams = viewHolder.ivOne.getLayoutParams();
        layoutparams.height = imageHeight;
        layoutparams.width = imageWidth;
        viewHolder.ivOne.setLayoutParams(layoutparams);
        viewHolder.ivOne.setClickable(true);
        viewHolder.ivOne.setScaleType(ImageView.ScaleType.FIT_XY);
        viewHolder.ivOne.setImageUrl(image.getUrl());

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        TextView tv_name,tv_type,tv_time,tv_content,tv_voteup,tv_comment;
        ImageView iv_voteup;
        NineGridlayout ivMore;
        CustomImageView ivOne;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_voteup = (ImageView) itemView.findViewById(R.id.voteup_iv);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_share_content);
            tv_voteup = (TextView) itemView.findViewById(R.id.voteup_tv);
            tv_comment = (TextView) itemView.findViewById(R.id.comment_tv);
            avatar = (CircleImageView) itemView.findViewById(R.id.iv_user);
            ivMore = (NineGridlayout) itemView.findViewById(R.id.iv_ngrid_layout);
            ivOne = (CustomImageView) itemView.findViewById(R.id.iv_oneimage);

        }
    }
}
