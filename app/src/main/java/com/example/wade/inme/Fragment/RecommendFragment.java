package com.example.wade.inme.Fragment;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.wade.inme.Adapter.MyRecommendAdapter;
import com.example.wade.inme.Entity.RecommendDujia;
import com.example.wade.inme.Entity.RecommendNew;
import com.example.wade.inme.Entity.RecommendTop;
import com.example.wade.inme.Entity.RecommendTuijian;
import com.example.wade.inme.Entity.RecommendZhuanlan;
import com.example.wade.inme.R;

public class RecommendFragment extends Fragment {
	
	private ListView lv;
	
	private MyRecommendAdapter myRecommendAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View.inflate(getActivity(), R.layout.fragment_recommend, null);
		//��ʼ���ؼ�
		initViews(v);
		//��ʼ���¼�����
		initListeners();
		
	    RecommendTop myRecommendTop=getDataTop();
		List<RecommendTuijian> myRecommendTuijian=getDataTuijian();
		RecommendDujia myRecommendDujia=getDataDujia();
		RecommendNew myRecommendNew=getDataNew();
		RecommendZhuanlan myRecommendZhuanlan=getDataZhuanlan();
	    myRecommendAdapter = new MyRecommendAdapter(getActivity().getSupportFragmentManager(),getActivity(), myRecommendTop,myRecommendTuijian,myRecommendDujia,myRecommendNew,myRecommendZhuanlan);
        //����������
        lv.setAdapter(myRecommendAdapter);
		return v;
	}

	private void initViews(View v) {
		lv = (ListView) v.findViewById(R.id.fragment_recommend_lv);
		
	}

	private void initListeners() {
		/***����ListView���¼�����*/
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
	}
	
	//�������
	public RecommendTop getDataTop() {
		RecommendTop list = new RecommendTop();
		/*ImageView imageView1 = new ImageView(getActivity());
		imageView1.setImageDrawable();
		list.setImageview1(imageView1);*/
		return list;
	}
    public List<RecommendTuijian> getDataTuijian() {
        List<RecommendTuijian> list = new ArrayList<RecommendTuijian>();
        RecommendTuijian recommendTuijian1 = new RecommendTuijian();
        recommendTuijian1.setTv_title("�Ƽ��赥");
        recommendTuijian1.setTv_context2("����2");
        recommendTuijian1.setTv_context3("����3");
        recommendTuijian1.setTv_context5("����5");
        recommendTuijian1.setTv_context6("����6");
        list.add(recommendTuijian1);
        RecommendTuijian recommendTuijian2 = new RecommendTuijian();
        recommendTuijian2.setTv_title("������̨");
        recommendTuijian2.setTv_context2("��̨����2");
        recommendTuijian2.setTv_context3("��̨����3");
        recommendTuijian2.setTv_context5("��̨����5");
        recommendTuijian2.setTv_context6("��̨����6");
        list.add(recommendTuijian2);
        return list;
    }
    public RecommendDujia getDataDujia() {
    	RecommendDujia list = new RecommendDujia();
    	list.setTv_context2("����2");
    	list.setTv_context3("����3");
    	return list;
    }
    public RecommendNew getDataNew() {
    	RecommendNew list = new RecommendNew();
    	list.setTv_context2("����2");
    	list.setTv_title2("����2");
    	return list;
    }
    public RecommendZhuanlan getDataZhuanlan() {
    	RecommendZhuanlan list = new RecommendZhuanlan();
    	/*list.setTv_context2("����2");
    	list.setTv_title2("����2");*/
    	return list;
    }

}
