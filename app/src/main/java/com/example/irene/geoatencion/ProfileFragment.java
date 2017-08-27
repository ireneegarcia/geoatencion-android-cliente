package com.example.irene.geoatencion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ProfileFragment extends Fragment {

    View mView;
    Context c;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        c = (Context)getActivity();

        SharedPreferences settings = getActivity().getSharedPreferences("perfil", c.MODE_PRIVATE);
        String mId = settings.getString("id", null);
        String mName = settings.getString("name", null);

        Log.d("my tag", mId+" "+mName);
        return mView;
    }


}
