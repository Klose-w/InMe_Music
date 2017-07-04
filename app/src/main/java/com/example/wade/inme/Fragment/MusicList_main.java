package com.example.wade.inme.Fragment;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wade.inme.Adapter.LocalMusicAdapter;
import com.example.wade.inme.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MusicList_main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicList_main extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ListView toplistview;
    private ListView pagerlistview;
    List<Map<String, Object>> listItems;
    List<Map<String, Object>> listItems2;
    Map<String, Object> listItem2;
    String[] lable;
    int[] num;
    int[] icon;
    List<String> lable2=new ArrayList<String>();
    List<Integer> num2=new ArrayList<Integer>();
    List<Integer> icon2=new ArrayList<Integer>();
    List<Integer> back2=new ArrayList<Integer>();
    List<Integer> more2=new ArrayList<Integer>();
    boolean isxianshi=true;
    ImageButton ib_shezilist;
    ImageButton ib_xianshilist;
    TextView tv_num;
    LocalMusicAdapter localMusicAdapter;

    public MusicList_main() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicList_main.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicList_main newInstance(String param1, String param2) {
        MusicList_main fragment = new MusicList_main();
        Log.e("2","In");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        Log.e("2","onResume");
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        localMusicAdapter=new LocalMusicAdapter(getActivity(),getActivity().getWindow());
        View view=inflater.inflate(R.layout.fragment_music_list_main, container, false);
        toplistview=(ListView)view.findViewById(R.id.lv_mymusic_mm_fm);
        toplistview.setAdapter(localMusicAdapter);
        toplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                //Toast.makeText(getActivity(),position+"",Toast.LENGTH_SHORT).show();
                switch(position){
                    case 5:
                        localMusicAdapter.isZhanKai();
                        break;
                    case 0:
                        transaction.replace(R.id.fm_main, new LocalMusicFragment());
                        transaction.commit();
                        break;
                    case 1:
                        transaction.replace(R.id.fm_main, new Recently(1));
                        transaction.commit();
                        break;
                    case 2:
                        transaction.replace(R.id.fm_main, new Recently(2));
                        transaction.commit();
                        break;
                    case 3:
                        transaction.replace(R.id.fm_main, new LocalMusicFragment());
                        transaction.commit();
                        break;
                    case 4:
                        transaction.replace(R.id.fm_main, new Recently(1));
                        transaction.commit();
                        break;
                    default:
                        transaction.replace(R.id.fm_main, new GdDiaplayFragment(position-6));
                        transaction.commit();
                        break;
                }
            }
        });
        Log.e("2","onCreateView");
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}

