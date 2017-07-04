package com.example.wade.inme.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wade.inme.Activity.PlayNowMusic_Main;
import com.example.wade.inme.Adapter.FindMusicAdapter;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.R;
import com.example.wade.inme.Service.MusicPlayService;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link Local_dq_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Local_dq_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    ListView Lv_localmusic_dq;
    FindMusicAdapter findMusicAdapter;
    InMeApplicacation inMeApplicacation;
    Handler handler;

    public Local_dq_fragment() {

        // Required empty public constructor
    }
    public static Local_dq_fragment newInstance(String param1, String param2) {
        Local_dq_fragment fragment = new Local_dq_fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_local_dq_fragment, container, false);
        inMeApplicacation=(InMeApplicacation)getActivity().getApplicationContext();
        Lv_localmusic_dq=(ListView)view.findViewById(R.id.lv_localmusic_dq);
        //View viewHeaderView=inflater.inflate(R.layout.localplaylisthead,container,false);
        View viewHeaderView=LayoutInflater.from(getActivity()).inflate(R.layout.localplaylisthead,null);
        TextView textView=(TextView)viewHeaderView.findViewById(R.id.tv_playall);
        textView.setText("(共"+inMeApplicacation.getLocalsize()+"首)");
        Lv_localmusic_dq.addHeaderView(viewHeaderView);
        findMusicAdapter=new FindMusicAdapter(getActivity(), getActivity().getWindow());
        Lv_localmusic_dq.setAdapter(findMusicAdapter);
        Lv_localmusic_dq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inMeApplicacation.setPlaymusicList(findMusicAdapter.getSong());
                if(position==0){
                    inMeApplicacation.setNowplay(0);
                }
                else if(inMeApplicacation.getNowmusic().getSongId()==inMeApplicacation.getLocalmusicList().get(position-1).getSongId()){
                    Intent intent=new Intent(getActivity(), PlayNowMusic_Main.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("nowmusic",inMeApplicacation.getNowmusic());
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
                else {
                    inMeApplicacation.setNowplay(position-1);
                }

               // String str=findMusicAdapter.getSong().get(position).getSongUrl();
                //Log.e("t1y",str);
                /*Intent intent=new Intent(getActivity(), MusicPlayService.class);
                intent.putExtra("url",str);
                intent.putExtra("isplay",2);
                getActivity().startService(intent);
                Intent intent1=new Intent("android.intent.changelistview");
                Bundle bundle=new Bundle();
                bundle.putSerializable("music",findMusicAdapter.getSong().get(position));
                intent1.putExtras(bundle);
                getActivity().sendBroadcast(intent1);*/

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
