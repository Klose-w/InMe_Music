package com.example.wade.inme.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wade.inme.R;

/**
 * Created by wade on 2017/6/21.
 */

public class SearchCardAdapter extends RecyclerView.Adapter {
    String[] title=new String[]{"剑魂","李一桐","向天再借","真正的快乐","周杰伦","小小天涯","再见了朋友","何处不古风","知了电台","射雕英雄传"};
    String[] search=new String[]{"洛阳夜雨","再逢明月照九州","猪八戒","射雕英雄传","李一桐"};
    Context mContext;
    public SearchCardAdapter(Context context){
        mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0){
            View view= LayoutInflater.from(mContext).inflate(R.layout.cardview_text_item,null);
            return new MycardviewHolder(view);
        }else {
            View view= LayoutInflater.from(mContext).inflate(R.layout.searchlistjilu,null);
            return new MycardviewHolder1(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position>9){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager=recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager=((GridLayoutManager)manager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
                @Override
                public int getSpanSize(int position) {
                    int type=getItemViewType(position);
                    switch (type){
                        case 0:
                            return 1;
                        case 1:
                            return 3;
                        default:
                            return 3;
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MycardviewHolder){
            MycardviewHolder mycardviewHolder=(MycardviewHolder)holder;
            mycardviewHolder.Tv_card_name.setText(title[position]);
        }else {
            MycardviewHolder1 mycardviewHolder1=(MycardviewHolder1)holder;
            mycardviewHolder1.Tv_sear_dell.setText(search[position-10]);
        }

    }

    @Override
    public int getItemCount() {
        return title.length+search.length;
    }
    public class MycardviewHolder extends RecyclerView.ViewHolder{
        TextView Tv_card_name;
        public MycardviewHolder(View itemView) {
            super(itemView);
            Tv_card_name=(TextView)itemView.findViewById(R.id.tv_card_name);
        }
    }
    public class MycardviewHolder1 extends RecyclerView.ViewHolder{
        TextView Tv_sear_dell;
        public MycardviewHolder1(View itemView) {
            super(itemView);
            Tv_sear_dell=(TextView)itemView.findViewById(R.id.tv_sear_dell);
        }
    }
}
