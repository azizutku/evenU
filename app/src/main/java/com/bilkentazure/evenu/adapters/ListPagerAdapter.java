package com.bilkentazure.evenu.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.bilkentazure.evenu.fragments.ClubsFragment;
import com.bilkentazure.evenu.fragments.DepartmentsFragment;
import com.bilkentazure.evenu.fragments.InterestsFragment;

/**
 * Created by Aziz Utku Kağıtcı on 06/05/2018
 *
 * @author Aziz Utku Kağıtcı
 * @version 06/05/2018
 */
public class ListPagerAdapter extends FragmentPagerAdapter {

	public ListPagerAdapter(FragmentManager fragmentManager){
		super(fragmentManager);
	}


	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				DepartmentsFragment departmentsFragment = new DepartmentsFragment();
				return departmentsFragment;
			case 1:
				ClubsFragment clubsFragment = new ClubsFragment();
				return clubsFragment;
			case 2:
				InterestsFragment interestsFragment = new InterestsFragment();
				return interestsFragment;
			default:
				return null;
		}
	}

	@Override
	public int getCount() { return 3; }

}
