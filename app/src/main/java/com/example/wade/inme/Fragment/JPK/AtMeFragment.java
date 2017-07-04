package com.example.wade.inme.Fragment.JPK;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.wade.inme.Adapter.MyMessageAdapter;
import com.example.wade.inme.JavaBean.MyMessage;
import com.example.wade.inme.R;


public class AtMeFragment extends Fragment {

	private ListView lv;
	private MyMessageAdapter myMessageAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = View.inflate(getActivity(), R.layout.fragment_atme, null);
		//初始化控件
		initViews(v);
		//初始化事件监听
		initListeners();
		List<MyMessage> list=getData();
		if(list==null){
			View vno = View.inflate(getActivity(), R.layout.fragment_no_message, null);
			return vno;
		}else{
			myMessageAdapter = new MyMessageAdapter(getActivity(), list);
			//设置适配器
			lv.setAdapter(myMessageAdapter);
		}
		return v;
	}

	private void initViews(View v) {
		lv = (ListView) v.findViewById(R.id.fragment_mymessage_atme_lv);

	}

	private void initListeners() {
		/***设置ListView的事件监听*/
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {

			}
		});
	}
	//填充数据
	public List<MyMessage> getData() {
		List<MyMessage> list = new ArrayList<MyMessage>();
		list=null;
		return list;
	}

}
