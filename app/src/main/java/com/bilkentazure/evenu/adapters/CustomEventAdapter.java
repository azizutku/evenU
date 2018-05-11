package com.bilkentazure.evenu.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bilkentazure.evenu.EventView;
import com.bilkentazure.evenu.R;
import com.bilkentazure.evenu.fragments.HomeFragment;
import com.bilkentazure.evenu.models.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Aziz Utku Kağıtcı on 08/05/2018
 *
 * @author Aziz Utku Kağıtcı
 * @version 08/05/2018
 */
public class CustomEventAdapter extends RecyclerView.Adapter<HomeFragment.EventHolder>{

	private List<Event> eventList;
	private Context context;

	public CustomEventAdapter(Context context, List<Event> eventList){
		this.eventList = eventList;
		this.context = context;
	}

	@Override
	public HomeFragment.EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item,parent,false);
		return new HomeFragment.EventHolder(itemView);
	}

	@Override
	public void onBindViewHolder(HomeFragment.EventHolder holder, int position) {
		final Event event = eventList.get(position);

		String name = event.getName();
		String description = event.getDescription();
		String location = event.getLocation();
		Date from = event.getFrom();

		holder.setTitle(name);
		holder.setInfo(description);
		holder.setLocation(location);
		holder.setDate(from);

		holder.hideEventButtons();
		holder.hideEventImage();

		holder.mainRlt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context , EventView.class);
				intent.putExtra("event", event);
				context.startActivity(intent);
			}
		});

	}

	@Override
	public int getItemCount() {
		return eventList.size();
	}

}
