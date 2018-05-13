package com.bilkentazure.evenu.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bilkentazure.evenu.MainActivity;
import com.bilkentazure.evenu.R;
import com.bilkentazure.evenu.adapters.ListAdapter;
import com.bilkentazure.evenu.models.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aziz Utku Kağıtcı on 06/05/2018
 *
 * @author Aziz Utku Kağıtcı
 * @version 06/05/2018
 */
public class InterestsFragment extends Fragment {

	private View mView;
	private RecyclerView recyclerView;
	private ListAdapter mAdapter;
	private List<Item> items;


	public InterestsFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mView = inflater.inflate(R.layout.fragment_interests, container, false);

		recyclerView = mView.findViewById(R.id.interests_fragment_recycler);

		//Get all clubs name and add them to RecyclerView
		items = new ArrayList<>();
		String[] departments = getContext().getResources().getStringArray(R.array.interests);
		ArrayList<String> subscribed = MainActivity.userModel.getSubscribeInterests();

		//Check followed or not
		for ( String department: departments){

			boolean followed = subscribed.contains(department);
			Item item = new Item(department,Item.ITEM_INTEREST,followed);
			items.add(item);

		}

		//Set adapter
		mAdapter = new ListAdapter(items, container.getContext(), 2);

		//Set properties of RecyclerView
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), LinearLayoutManager.VERTICAL));
		recyclerView.setAdapter(mAdapter);



		return mView;
	}

}
