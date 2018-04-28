package com.bilkentazure.evenu.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bilkentazure.evenu.R;

/**
 * Created by Aziz Utku Kağıtcı on 17/04/2018
 * Fragment for Categorized Events
 * @author Aziz Utku Kağıtcı
 * @version 17/04/2018
 */
public class ProfileFragment extends Fragment {


	public ProfileFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_profile, container, false);
		return view;
	}

}
