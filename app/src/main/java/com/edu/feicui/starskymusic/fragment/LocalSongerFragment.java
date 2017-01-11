package com.edu.feicui.starskymusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edu.feicui.starskymusic.R;

import butterknife.Unbinder;

/**
 * Created by user on 2017/1/11.
 */

public class LocalSongerFragment extends Fragment {

    View view;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.local_songer_fragment,container,false);
        }
        return view;
    }
}
