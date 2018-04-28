package com.bilkentazure.evenu.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bilkentazure.evenu.fragments.FavFragment;
import com.bilkentazure.evenu.fragments.HomeFragment;
import com.bilkentazure.evenu.fragments.ListFragment;
import com.bilkentazure.evenu.fragments.ProfileFragment;
import com.bilkentazure.evenu.fragments.ScannerFragment;

/**
 * Created by Aziz Utku Kağıtcı on 17/04/2018
 * It is adapter for fragments.
 * @author Aziz Utku Kağıtcı
 * @version 17/04/2018
 */
public class PagerAdapter extends FragmentPagerAdapter {

	public PagerAdapter(FragmentManager fragmentManager){
		super(fragmentManager);
	}

	@Override
	public Fragment getItem(int position) {

		switch (position){
			case 0:
				HomeFragment homeFragment = new HomeFragment();
				return homeFragment;
			case 1:
				ScannerFragment scannerFragment = new ScannerFragment();
				return scannerFragment;
			case 2:
				ProfileFragment profileFragment = new ProfileFragment();
				return profileFragment;
			case 3:
				ListFragment listFragment = new ListFragment();
				return listFragment;
			case 4:
				FavFragment favFragment = new FavFragment();
				return favFragment;
			default:
				return null;
		}

	}

	@Override
	public int getCount() {
		return 5;
	}




}
