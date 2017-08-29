package com.example.irene.geoatencion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import Model.Variables;


public class ProfileFragment extends Fragment {

    View mView;
    Context c;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static class Profile
    {
        TextView name;
        TextView phone;
        TextView email;
        TextView address;
        TextView latitude;
        TextView longitude;
        ImageView profileImage;
        //ImageView icon;
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
        String mPhone = settings.getString("phone", null);
        String mEmail = settings.getString("email", null);
        String mAddress = settings.getString("address", null);
        String mLatitude = settings.getString("latitude", null);
        String mLongitude = settings.getString("longitude", null);
        String mProfileImage = settings.getString("profileImage", null);

        Profile view= new Profile();

        view.name = (TextView) mView.findViewById(R.id.textView2);
        view.phone = (TextView) mView.findViewById(R.id.textView3);
        view.email = (TextView) mView.findViewById(R.id.textView5);
        view.address = (TextView) mView.findViewById(R.id.textView4);
        view.profileImage = (ImageView) mView.findViewById(R.id.imageView2);

        view.name.setText(mName);
        view.phone.setText(mPhone);
        view.email.setText(mEmail);
        view.address.setText("\nMi dirección actual: "+mAddress +"\nMi ubicación actual: "+mLatitude+", "+mLongitude);
        Glide
                .with(this.c)
                .load(Variables.getUrl()+mProfileImage)
                .error(R.drawable.logo_icono)
                .into(view.profileImage);
        return mView;
    }


}
