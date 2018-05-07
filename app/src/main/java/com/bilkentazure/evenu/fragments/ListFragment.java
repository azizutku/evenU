package com.bilkentazure.evenu.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilkentazure.evenu.R;
import com.bilkentazure.evenu.adapters.ListPagerAdapter;

/**
 * Created by Aziz Utku Kağıtcı on 17/04/2018
 * Fragment for Categorized Events
 * @author Aziz Utku Kağıtcı
 * @version 06/05/2018
 */
public class ListFragment extends Fragment {

	private View mView;
	private ViewPager mViewPager;
	private static TabLayout mTabLayout;

	private TextView txtDepartments;
	private TextView txtClubs;
	private TextView txtInterests;

	private ListPagerAdapter mPagerAdapter;

	public ListFragment() {
		// Required empty public constructor
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		mView =  inflater.inflate(R.layout.fragment_list, container, false);

		txtDepartments = mView.findViewById(R.id.list_fragment_txt_departments);
		txtClubs = mView.findViewById(R.id.list_fragment_txt_clubs);
		txtInterests = mView.findViewById(R.id.list_fragment_txt_interests);


		mViewPager = mView.findViewById(R.id.list_fragment_view_pager);
		mPagerAdapter = new ListPagerAdapter(getActivity().getSupportFragmentManager());


		mViewPager.setAdapter(mPagerAdapter);

		txtDepartments.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(0, true);
				setTextViewColor(0);
			}
		});

		txtClubs.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(1, true);
				setTextViewColor(1);
			}
		});

		txtInterests.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(2, true);
				setTextViewColor(2);
			}
		});

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				setTextViewColor(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});


		return mView;
	}

	private void setTextViewColor(int position){
		if(position == 0){
			txtDepartments.setTextColor(Color.parseColor("#C22B1F"));
			txtClubs.setTextColor(Color.parseColor("#A2A2A2"));
			txtInterests.setTextColor(Color.parseColor("#A2A2A2"));
		} else if(position == 1) {
			txtDepartments.setTextColor(Color.parseColor("#A2A2A2"));
			txtClubs.setTextColor(Color.parseColor("#C22B1F"));
			txtInterests.setTextColor(Color.parseColor("#A2A2A2"));
		} else {
			txtDepartments.setTextColor(Color.parseColor("#A2A2A2"));
			txtClubs.setTextColor(Color.parseColor("#A2A2A2"));
			txtInterests.setTextColor(Color.parseColor("#C22B1F"));
		}
	}

}
