package com.example.wade.inme.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wade.inme.Adapter.SearchCardAdapter;
import com.example.wade.inme.Adapter.SearchMusicAdapter;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.JavaBean.musicbean;
import com.example.wade.inme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class SearchFragment extends Fragment {
    EditText Et_sear_put;
    ImageButton Ib_back_sear;
    ImageButton Ib_sear_del;
    RecyclerView Rv_sear_hot;
    SearchCardAdapter searchCardAdapter;
    RecyclerView.LayoutManager layoutManager;
    ListView Lv_sear_diaplay;
    RelativeLayout Rv_list_search;
    SearchMusicAdapter searchMusicAdapter;
    InMeApplicacation inMeApplicacation;
    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_search, container, false);
        inMeApplicacation=(InMeApplicacation)getContext().getApplicationContext();
        Et_sear_put=(EditText) view.findViewById(R.id.et_sear_put);
        Ib_back_sear=(ImageButton)view.findViewById(R.id.ib_back_sear);
        Ib_back_sear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fm_main, new MainFragment());
                transaction.commit();
            }
        });
        Ib_sear_del=(ImageButton)view.findViewById(R.id.ib_sear_del);
        Ib_sear_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Et_sear_put.setText("");
            }
        });
        Rv_sear_hot=(RecyclerView)view.findViewById(R.id.rv_sear_hot);
        Lv_sear_diaplay=(ListView)view.findViewById(R.id.lv_sear_diaplay);
        layoutManager=new GridLayoutManager(Rv_sear_hot.getContext(),3,GridLayoutManager.VERTICAL,false);
        searchCardAdapter=new SearchCardAdapter(getContext());
        Rv_sear_hot.setLayoutManager(layoutManager);
        Rv_sear_hot.setAdapter(searchCardAdapter);
        searchCardAdapter.setOnItemClickListener(new SearchCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String text;
                TextView y;
                if(position<10){
                    y=(TextView)v.findViewById(R.id.tv_card_name);
                }else {
                    y=(TextView)v.findViewById(R.id.tv_sear_dell);
                }
                text= y.getText().toString();
                searchMusicAdapter=new SearchMusicAdapter(Et_sear_put.getText().toString(),getContext(),getActivity().getWindow());
                Lv_sear_diaplay.setAdapter(searchMusicAdapter);
                Rv_list_search.setVisibility(View.VISIBLE);
                Et_sear_put.setText(text);
            }
        });
        Rv_list_search=(RelativeLayout)view.findViewById(R.id.rv_list_search);
        Et_sear_put.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()>0){
                        Ib_sear_del.setVisibility(View.VISIBLE);
                    }else {
                        Ib_sear_del.setVisibility(View.GONE);
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    Ib_sear_del.setVisibility(View.VISIBLE);
                }else {
                    Ib_sear_del.setVisibility(View.GONE);
                }
            }
        });
        Et_sear_put.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    searchMusicAdapter=new SearchMusicAdapter(Et_sear_put.getText().toString(),getContext(),getActivity().getWindow());
                    Lv_sear_diaplay.setAdapter(searchMusicAdapter);
                    Rv_list_search.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        Lv_sear_diaplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<MusicInfor> list=searchMusicAdapter.getSondid();
                inMeApplicacation.setPlaymusicList(list);
                inMeApplicacation.setNowplay(position);
            }
        });
        return view;
    }
    public void playmusci(String songid){
        String url="http://ting.baidu.com/data/music/links?songIds="+songid;
    }
    // TODO: Rename method, update argument and hook method into UI event

    public void findMusic(String url){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest sr = new JsonObjectRequest(url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("jjjj",jsonObject.toString());
                //Log.e("json", jsonObject.toString());
                JSONArray jsonArray = null;
                /*try {
                    jsonObject=jsonObject.getJSONObject("data");
                    jsonArray=jsonObject.getJSONArray("songList");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    MusicInfor m=new MusicInfor(jsonObject1.getString("songName"),jsonObject1.getString("songId"),
                            jsonObject1.getString("time"),jsonObject1.getString("artistId"),jsonObject1.getString("songPicSmall").replaceAll("////",""),
                            jsonObject1.getString("songId"),);
                   list.add(m);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(sr);
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
