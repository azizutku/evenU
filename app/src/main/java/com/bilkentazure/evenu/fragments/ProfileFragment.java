package com.bilkentazure.evenu.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilkentazure.evenu.GeActivity;
import com.bilkentazure.evenu.R;
import com.bilkentazure.evenu.SettingsActivity;

/**
 * Created by Aziz Utku Kağıtcı on 17/04/2018
 * Fragment for Categorized Events
 * @author Aziz Utku Kağıtcı
 * @version 17/04/2018
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{


	private ImageView profileImage;
	private TextView ge;
	private TextView sett;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_profile, container, false);

		profileImage = view.findViewById(R.id.profile_image);
		ge = view.findViewById(R.id.ge_bridge);
		sett = view.findViewById(R.id.settings_bridge);



		ge.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), GeActivity.class);
				startActivity(intent);
			}
		});
		sett.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SettingsActivity.class);
				startActivity(intent);
			}
		});
		//ge.setOnClickListener(this);
		//sett.setOnClickListener(this);



		return view;
	}

	@Override
	public void onClick(View view) {

		/**if( view == ge){
			startActivity(new Intent(getActivity(), GeActivity.class));

		}
		if( view == sett){
			startActivity(new Intent(getActivity(), SettingsActivity.class));
			startActivity(intent);

		}

		 */
	}
}
