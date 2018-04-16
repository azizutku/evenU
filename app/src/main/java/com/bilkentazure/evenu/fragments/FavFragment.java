package com.bilkentazure.evenu.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bilkentazure.evenu.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment {


	public FavFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {


		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_fav, container, false);
		return view;
	}

}
