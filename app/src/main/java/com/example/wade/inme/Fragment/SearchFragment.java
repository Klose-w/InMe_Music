package com.example.wade.inme.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wade.inme.Adapter.SearchCardAdapter;
import com.example.wade.inme.R;


public class SearchFragment extends Fragment {
    EditText Et_sear_put;
    ImageButton Ib_back_sear;
    ImageButton Ib_sear_del;
    RecyclerView Rv_sear_hot;
    SearchCardAdapter searchCardAdapter;
    RecyclerView.LayoutManager layoutManager;
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
        Et_sear_put=(EditText) view.findViewById(R.id.et_sear_put);
        Ib_back_sear=(ImageButton)view.findViewById(R.id.ib_back_sear);
        Ib_sear_del=(ImageButton)view.findViewById(R.id.ib_sear_del);
        Rv_sear_hot=(RecyclerView)view.findViewById(R.id.rv_sear_hot);
        layoutManager=new GridLayoutManager(Rv_sear_hot.getContext(),3,GridLayoutManager.VERTICAL,false);
        searchCardAdapter=new SearchCardAdapter(getContext());
        Rv_sear_hot.setLayoutManager(layoutManager);
        Rv_sear_hot.setAdapter(searchCardAdapter);
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
                    Toast.makeText(getContext(),"卧槽",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        return view;
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
