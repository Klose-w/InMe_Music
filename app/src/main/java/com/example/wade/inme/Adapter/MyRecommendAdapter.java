package com.example.wade.inme.Adapter;

import java.util.List;



import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wade.inme.Entity.RecommendDujia;
import com.example.wade.inme.Entity.RecommendNew;
import com.example.wade.inme.Entity.RecommendTop;
import com.example.wade.inme.Entity.RecommendTuijian;
import com.example.wade.inme.Entity.RecommendZhuanlan;
import com.example.wade.inme.Fragment.GdDiaplayFragment;
import com.example.wade.inme.Fragment.MainFragment;
import com.example.wade.inme.JavaBean.GdJsonArray;
import com.example.wade.inme.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MyRecommendAdapter extends BaseAdapter implements View.OnClickListener {

	private Context myContext;
	private RecommendTop myRecommendTop;
	private List<RecommendTuijian> myRecommendTuijian;
	private RecommendDujia myRecommendDujia;
	private RecommendNew myRecommendNew;
	private RecommendZhuanlan myRecommendZhuanlan;

	private final int TYPE_COUNT = 5;
	private final int FIRST_TYPE = 0;
	private final int SECOND_TYPE = 1;
	private final int THIRD_TYPE = 2;
	private final int FOURTH_TYPE = 3;
	private final int FIFTH_TYPE = 4;
	private int currentType;
	GdJsonArray jsarray;
	FragmentManager fragmentManager;
	public MyRecommendAdapter(Context myContext) {
		super();
		this.myContext = myContext;
		jsarray=new GdJsonArray();
	}

	public MyRecommendAdapter(FragmentManager fragmentManager,Context myContext, RecommendTop myRecommendTop,
			List<RecommendTuijian> myRecommendTuijian,
			RecommendDujia myRecommendDujia, RecommendNew myRecommendNew,
			RecommendZhuanlan myRecommendZhuanlan) {
		super();
		this.fragmentManager=fragmentManager;
		this.myContext = myContext;
		this.myRecommendTop = myRecommendTop;
		this.myRecommendTuijian = myRecommendTuijian;
		this.myRecommendDujia = myRecommendDujia;
		this.myRecommendNew = myRecommendNew;
		this.myRecommendZhuanlan = myRecommendZhuanlan;
		jsarray=new GdJsonArray();
	}

	@Override
	public int getCount() {

		return 5;
	}

	@Override
	public Object getItem(int position) {
		if (position == 0) {
			return myRecommendTop;
		} else if (position == 1) {
			return myRecommendTuijian.get(0);
		} else if (position == 2) {
			return myRecommendNew;
		} else if (position == 4) {
			return myRecommendZhuanlan;
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return FIRST_TYPE;
		} else if (position == 1) {
			return SECOND_TYPE;
		} else if (position == 2) {
			return THIRD_TYPE;
		} else if (position == 3) {
			return FOURTH_TYPE;
		} else {
			return FIFTH_TYPE;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View firstItemView = null;
		View secondItemView = null;
		View thirdItemView = null;
		View fourthItemView = null;
		View fifthItemView = null;
		currentType = getItemViewType(position);
		if (currentType == FIRST_TYPE) {
			firstItemView = convertView;
			FirstItemViewHolder firstItemViewHolder = null;
			if (firstItemView == null) {
				firstItemView = View.inflate(myContext, R.layout.list_item_recommend_top, null);
				firstItemViewHolder = new FirstItemViewHolder();
				firstItemViewHolder.imageView = (ImageView) firstItemView.findViewById(R.id.recommend_top_iv1);
				firstItemView.setTag(firstItemViewHolder);
			} else {
				firstItemViewHolder = (FirstItemViewHolder) firstItemView
						.getTag();
			}
			JSONObject jj=jsarray.getGdlistjson()[0];
			Glide.with(myContext).load(R.drawable.vs).into(firstItemViewHolder.imageView);
			convertView = firstItemView;
		}
		if (currentType == SECOND_TYPE) {
			secondItemView = convertView;
			SecondItemViewHolder secondItemViewHolder = null;
			if (secondItemView == null) {
				secondItemView = View.inflate(myContext,
						R.layout.list_item_recommend_tuijian, null);
				secondItemViewHolder = new SecondItemViewHolder();
				secondItemViewHolder.tv_title = (TextView) secondItemView.findViewById(R.id.recommend_tv_tuijian_title);
				secondItemViewHolder.imageView1 = (ImageView) secondItemView.findViewById(R.id.recommend_iv_tuijian1);
				secondItemViewHolder.imageView2 = (ImageView) secondItemView.findViewById(R.id.recommend_iv_tuijian2);
				secondItemViewHolder.imageView3 = (ImageView) secondItemView.findViewById(R.id.recommend_iv_tuijian3);
				secondItemViewHolder.imageView4 = (ImageView) secondItemView.findViewById(R.id.recommend_iv_tuijian4);
				secondItemViewHolder.imageView5 = (ImageView) secondItemView.findViewById(R.id.recommend_iv_tuijian5);
				secondItemViewHolder.imageView6 = (ImageView) secondItemView.findViewById(R.id.recommend_iv_tuijian6);
				secondItemViewHolder.tv1 = (TextView) secondItemView.findViewById(R.id.recommend_tv_tuijian1);
				secondItemViewHolder.tv2 = (TextView) secondItemView.findViewById(R.id.recommend_tv_tuijian2);
				secondItemViewHolder.tv3 = (TextView) secondItemView.findViewById(R.id.recommend_tv_tuijian3);
				secondItemViewHolder.tv4 = (TextView) secondItemView.findViewById(R.id.recommend_tv_tuijian4);
				secondItemViewHolder.tv5 = (TextView) secondItemView.findViewById(R.id.recommend_tv_tuijian5);
				secondItemViewHolder.tv6 = (TextView) secondItemView.findViewById(R.id.recommend_tv_tuijian6);
				secondItemView.setTag(secondItemViewHolder);
			} else {
				secondItemViewHolder = (SecondItemViewHolder) secondItemView
						.getTag();
			}
			RecommendTuijian rt = null;
			/**��������Դ*/
			if(position==1){
				rt = myRecommendTuijian.get(position-1);
			}else if(position==5){
				//rt = myRecommendTuijian.get(position-4);
			}
				try {
					secondItemViewHolder.tv_title.setText("推荐歌单");
					secondItemViewHolder.tv1.setText(jsarray.getGdlistjson()[0].getString("name"));
					secondItemViewHolder.tv2.setText(jsarray.getGdlistjson()[1].getString("name"));
					secondItemViewHolder.tv3.setText(jsarray.getGdlistjson()[2].getString("name"));
					secondItemViewHolder.tv4.setText(jsarray.getGdlistjson()[3].getString("name"));
					secondItemViewHolder.tv5.setText(jsarray.getGdlistjson()[4].getString("name"));
					secondItemViewHolder.tv6.setText(jsarray.getGdlistjson()[5].getString("name"));
					secondItemViewHolder.imageView1.setOnClickListener(this);
					secondItemViewHolder.imageView2.setOnClickListener(this);
					secondItemViewHolder.imageView3.setOnClickListener(this);
					secondItemViewHolder.imageView4.setOnClickListener(this);
					secondItemViewHolder.imageView5.setOnClickListener(this);
					secondItemViewHolder.imageView6.setOnClickListener(this);

					Glide.with(myContext).load(jsarray.getGdlistjson()[0].getString("cover_img")).into(secondItemViewHolder.imageView1);
					Glide.with(myContext).load(jsarray.getGdlistjson()[1].getString("cover_img")).into(secondItemViewHolder.imageView2);
					Glide.with(myContext).load(jsarray.getGdlistjson()[2].getString("cover_img")).into(secondItemViewHolder.imageView3);
					Glide.with(myContext).load(jsarray.getGdlistjson()[3].getString("cover_img")).into(secondItemViewHolder.imageView4);
					Glide.with(myContext).load(jsarray.getGdlistjson()[4].getString("cover_img")).into(secondItemViewHolder.imageView5);
					Glide.with(myContext).load(jsarray.getGdlistjson()[5].getString("cover_img")).into(secondItemViewHolder.imageView6);

				} catch (JSONException e) {
					//Glide.with(myContext).load(R.drawable.placeholder_disk_play_song).into(firstItemViewHolder.imageView);
					e.printStackTrace();
				}
			convertView = secondItemView;
		}
		if (currentType == THIRD_TYPE) {
			thirdItemView = convertView;
			ThirdItemViewHolder thirdItemViewHolder = null;
			if (thirdItemView == null) {
				thirdItemView = View.inflate(myContext,
						R.layout.list_item_recommend_dujia, null);
				thirdItemViewHolder = new ThirdItemViewHolder();
				thirdItemViewHolder.imageView1 = (ImageView) thirdItemView.findViewById(R.id.recommend_iv_dujia1);
				thirdItemViewHolder.imageView2 = (ImageView) thirdItemView.findViewById(R.id.recommend_iv_dujia2);
				thirdItemViewHolder.imageView3 = (ImageView) thirdItemView.findViewById(R.id.recommend_iv_dujia3);
				thirdItemViewHolder.tv1 = (TextView) thirdItemView.findViewById(R.id.recommend_tv_dujia1);
				thirdItemViewHolder.tv2 = (TextView) thirdItemView.findViewById(R.id.recommend_tv_dujia2);
				thirdItemViewHolder.tv3 = (TextView) thirdItemView.findViewById(R.id.recommend_tv_dujia3);
				thirdItemView.setTag(thirdItemViewHolder);
			} else {
				thirdItemViewHolder = (ThirdItemViewHolder) thirdItemView
						.getTag();
			}
			/**��������Դ*/
			thirdItemViewHolder.tv2.setText(myRecommendDujia.getTv_context2());
			thirdItemViewHolder.tv3.setText(myRecommendDujia.getTv_context3());
			convertView = thirdItemView;
		}
		if (currentType == FOURTH_TYPE) {
			fourthItemView = convertView;
			FourthItemViewHolder fourthItemViewHolder = null;
			if (fourthItemView == null) {
				fourthItemView = View.inflate(myContext,
						R.layout.list_item_recommend_new, null);
				fourthItemViewHolder = new FourthItemViewHolder();
				fourthItemViewHolder.imageView1 = (ImageView) fourthItemView.findViewById(R.id.recommend_iv_new1);
				fourthItemViewHolder.imageView2 = (ImageView) fourthItemView.findViewById(R.id.recommend_iv_new2);
				fourthItemViewHolder.imageView3 = (ImageView) fourthItemView.findViewById(R.id.recommend_iv_new3);
				fourthItemViewHolder.imageView4 = (ImageView) fourthItemView.findViewById(R.id.recommend_iv_new4);
				fourthItemViewHolder.imageView5 = (ImageView) fourthItemView.findViewById(R.id.recommend_iv_new5);
				fourthItemViewHolder.imageView6 = (ImageView) fourthItemView.findViewById(R.id.recommend_iv_new6);
				fourthItemViewHolder.imageView6.setOnClickListener(this);
				fourthItemViewHolder.imageView5.setOnClickListener(this);
				fourthItemViewHolder.imageView4.setOnClickListener(this);
				fourthItemViewHolder.imageView3.setOnClickListener(this);
				fourthItemViewHolder.imageView2.setOnClickListener(this);
				fourthItemViewHolder.imageView1.setOnClickListener(this);
				fourthItemViewHolder.tv_title1 = (TextView) fourthItemView.findViewById(R.id.recommend_tv_new_name1);
				fourthItemViewHolder.tv_title2 = (TextView) fourthItemView.findViewById(R.id.recommend_tv_new_name2);
				fourthItemViewHolder.tv_title3 = (TextView) fourthItemView.findViewById(R.id.recommend_tv_new_name3);
				fourthItemViewHolder.tv_title4 = (TextView) fourthItemView.findViewById(R.id.recommend_tv_new_name4);
				fourthItemViewHolder.tv_title5 = (TextView) fourthItemView.findViewById(R.id.recommend_tv_new_name5);
				fourthItemViewHolder.tv_title6 = (TextView) fourthItemView.findViewById(R.id.recommend_tv_new_name6);
				fourthItemViewHolder.tv_context1 = (TextView) fourthItemView.findViewById(R.id.recommend_tv_new_context1);
				fourthItemViewHolder.tv_context2 = (TextView) fourthItemView.findViewById(R.id.recommend_tv_new_context2);
				fourthItemViewHolder.tv_context3 = (TextView) fourthItemView.findViewById(R.id.recommend_tv_new_context3);
				fourthItemViewHolder.tv_context4 = (TextView) fourthItemView.findViewById(R.id.recommend_tv_new_context4);
				fourthItemViewHolder.tv_context5 = (TextView) fourthItemView.findViewById(R.id.recommend_tv_new_context5);
				fourthItemViewHolder.tv_context6 = (TextView) fourthItemView.findViewById(R.id.recommend_tv_new_context6);
				fourthItemView.setTag(fourthItemViewHolder);
			} else {
				fourthItemViewHolder = (FourthItemViewHolder) fourthItemView
						.getTag();
			}
			/**��������Դ*/
			try {
				fourthItemViewHolder.tv_title1.setText(jsarray.getGdlistjson()[9].getString("name"));
				fourthItemViewHolder.tv_title2.setText(jsarray.getGdlistjson()[10].getString("name"));
				fourthItemViewHolder.tv_title3.setText(jsarray.getGdlistjson()[11].getString("name"));
				fourthItemViewHolder.tv_title4.setText(jsarray.getGdlistjson()[12].getString("name"));
				fourthItemViewHolder.tv_title5.setText(jsarray.getGdlistjson()[13].getString("name"));
				fourthItemViewHolder.tv_title6.setText(jsarray.getGdlistjson()[14].getString("name"));
				Glide.with(myContext).load(jsarray.getGdlistjson()[9].getString("cover_img")).into(fourthItemViewHolder.imageView1);
				Glide.with(myContext).load(jsarray.getGdlistjson()[10].getString("cover_img")).into(fourthItemViewHolder.imageView2);
				Glide.with(myContext).load(jsarray.getGdlistjson()[11].getString("cover_img")).into(fourthItemViewHolder.imageView3);
				Glide.with(myContext).load(jsarray.getGdlistjson()[12].getString("cover_img")).into(fourthItemViewHolder.imageView4);
				Glide.with(myContext).load(jsarray.getGdlistjson()[13].getString("cover_img")).into(fourthItemViewHolder.imageView5);
				Glide.with(myContext).load(jsarray.getGdlistjson()[14].getString("cover_img")).into(fourthItemViewHolder.imageView6);
				fourthItemViewHolder.tv_context1.setText(jsarray.getGdlistjson()[9].getString("topics"));
				fourthItemViewHolder.tv_context2.setText(jsarray.getGdlistjson()[10].getString("topics"));
				fourthItemViewHolder.tv_context3.setText(jsarray.getGdlistjson()[11].getString("topics"));
				fourthItemViewHolder.tv_context4.setText(jsarray.getGdlistjson()[12].getString("topics"));
				fourthItemViewHolder.tv_context5.setText(jsarray.getGdlistjson()[13].getString("topics"));
				fourthItemViewHolder.tv_context6.setText(jsarray.getGdlistjson()[14].getString("topics"));



			} catch (JSONException e) {
				//Glide.with(myContext).load(R.drawable.placeholder_disk_play_song).into(firstItemViewHolder.imageView);
				e.printStackTrace();
			}
			convertView = fourthItemView;
		}
		if (currentType == FIFTH_TYPE) {
			fifthItemView = convertView;
			FifthItemViewHolder fifthItemViewHolder = null;
			if (fifthItemView == null) {
				fifthItemView = View.inflate(myContext,
						R.layout.list_item_recommend_zhuanlan, null);
				fifthItemViewHolder = new FifthItemViewHolder();
				fifthItemViewHolder.imageView1 = (ImageView) fifthItemView.findViewById(R.id.recommend_iv_zhuanlan1);
				fifthItemViewHolder.imageView2 = (ImageView) fifthItemView.findViewById(R.id.recommend_iv_zhuanlan2);
				fifthItemViewHolder.imageView3 = (ImageView) fifthItemView.findViewById(R.id.recommend_iv_zhuanlan3);
				fifthItemViewHolder.tv_name1 = (TextView) fifthItemView.findViewById(R.id.recommend_tv_zhuanlan_name1);
				fifthItemViewHolder.tv_name2 = (TextView) fifthItemView.findViewById(R.id.recommend_tv_zhuanlan_name2);
				fifthItemViewHolder.tv_name3 = (TextView) fifthItemView.findViewById(R.id.recommend_tv_zhuanlan_name3);
				fifthItemViewHolder.imageView1.setOnClickListener(this);
				fifthItemViewHolder.imageView2.setOnClickListener(this);
				fifthItemViewHolder.imageView3.setOnClickListener(this);
				fifthItemViewHolder.tv_name1.setOnClickListener(this);
				fifthItemViewHolder.tv_name2.setOnClickListener(this);
				fifthItemViewHolder.tv_name3.setOnClickListener(this);
				fifthItemViewHolder.tv_number1 = (TextView) fifthItemView.findViewById(R.id.recommend_tv_zhuanlan_number1);
				fifthItemViewHolder.tv_number2 = (TextView) fifthItemView.findViewById(R.id.recommend_tv_zhuanlan_number2);
				fifthItemViewHolder.tv_number3 = (TextView) fifthItemView.findViewById(R.id.recommend_tv_zhuanlan_number3);
				fifthItemView.setTag(fifthItemViewHolder);
			} else {
				fifthItemViewHolder = (FifthItemViewHolder) fifthItemView.getTag();
			}
			try {
				fifthItemViewHolder.tv_name1.setText(jsarray.getGdlistjson()[6].getString("name")+"--"+jsarray.getGdlistjson()[6].getString("author"));
				fifthItemViewHolder.tv_name2.setText(jsarray.getGdlistjson()[7].getString("name")+"--"+jsarray.getGdlistjson()[7].getString("author"));
				fifthItemViewHolder.tv_name3.setText(jsarray.getGdlistjson()[8].getString("name")+"--"+jsarray.getGdlistjson()[8].getString("author"));
				fifthItemViewHolder.tv_number1.setText("播放数量("+jsarray.getGdlistjson()[6].getString("play_count")+")");
				fifthItemViewHolder.tv_number2.setText("播放数量("+jsarray.getGdlistjson()[7].getString("play_count")+")");
				fifthItemViewHolder.tv_number3.setText("播放数量("+jsarray.getGdlistjson()[8].getString("play_count")+")");
				Glide.with(myContext).load(jsarray.getGdlistjson()[6].getString("cover_img")).into(fifthItemViewHolder.imageView1);
				Glide.with(myContext).load(jsarray.getGdlistjson()[7].getString("cover_img")).into(fifthItemViewHolder.imageView2);
				Glide.with(myContext).load(jsarray.getGdlistjson()[8].getString("cover_img")).into(fifthItemViewHolder.imageView3);

			} catch (JSONException e) {
				//Glide.with(myContext).load(R.drawable.placeholder_disk_play_song).into(firstItemViewHolder.imageView);
				e.printStackTrace();
			}
			/**��������Դ*/
			/*fifthItemViewHolder.tv_name2.setText(myRecommendNew.getTv_title2());
			fifthItemViewHolder.tv_number1.setText(myRecommendNew.getTv_context2());*/
			convertView = fifthItemView;
		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		FragmentManager fm = fragmentManager;
		FragmentTransaction transaction = fm.beginTransaction();
		switch (v.getId()){
			case R.id.recommend_iv_tuijian1:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(0,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_tuijian2:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(1,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_tuijian3:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(2,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_tuijian4:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(3,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_tuijian5:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(4,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_new1:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(5,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_new2:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(9,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_new3:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(10,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_new4:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(11,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_new5:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(12,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_new6:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(13,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_zhuanlan1:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(6,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_zhuanlan2:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(7,1));
				transaction.commit();
				break;
			case R.id.recommend_iv_zhuanlan3:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(8,1));
				transaction.commit();
				break;
			case R.id.recommend_tv_zhuanlan_name1:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(6,1));
				transaction.commit();
				break;
			case R.id.recommend_tv_zhuanlan_name2:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(7,1));
				transaction.commit();
				break;
			case R.id.recommend_tv_zhuanlan_name3:
				transaction.replace(R.id.fm_main,new GdDiaplayFragment(8,1));
				transaction.commit();
				break;
		}
	}

	class FirstItemViewHolder {
		ImageView imageView; 
	}
	class SecondItemViewHolder {
		
		TextView tv_title;
		ImageView imageView1;
		ImageView imageView2;
		ImageView imageView3;
		ImageView imageView4;
		ImageView imageView5;
		ImageView imageView6;
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		TextView tv5;
		TextView tv6;
		
	}
	class ThirdItemViewHolder {
		
		ImageView imageView1;
		ImageView imageView2;
		ImageView imageView3;
		TextView tv1;
		TextView tv2;
		TextView tv3;
	}
	class FourthItemViewHolder {
		
		ImageView imageView1;
		ImageView imageView2;
		ImageView imageView3;
		ImageView imageView4;
		ImageView imageView5;
		ImageView imageView6;
		TextView tv_title1;
		TextView tv_title2;
		TextView tv_title3;
		TextView tv_title4;
		TextView tv_title5;
		TextView tv_title6;
		TextView tv_context1;
		TextView tv_context2;
		TextView tv_context3;
		TextView tv_context4;
		TextView tv_context5;
		TextView tv_context6;
		
	}
	class FifthItemViewHolder {
		
		ImageView imageView1;
		ImageView imageView2;
		ImageView imageView3;
		TextView tv_name1;
		TextView tv_name2;
		TextView tv_name3;
		TextView tv_number1;
		TextView tv_number2;
		TextView tv_number3;
	}

}
