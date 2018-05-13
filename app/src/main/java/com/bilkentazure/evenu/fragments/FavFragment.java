package com.bilkentazure.evenu.fragments;


import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bilkentazure.evenu.MainActivity;
import com.bilkentazure.evenu.R;
import com.bilkentazure.evenu.models.Event;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Aziz Utku Kağıtcı on 17/04/2018
 * Fragment for Favorite Events
 * @author Aziz Utku Kağıtcı
 * @version 28/04/2018
 */
public class FavFragment extends Fragment {


	//Firebase
	private FirebaseFirestore db;
	private FirebaseAuth mAuth;
	private FirebaseUser mUser;
	private FirestoreRecyclerAdapter mFirestoreRecyclerAdapter;
	private static final int REQUEST_CALENDAR = 0;
	private RecyclerView mRecyclerEvent;

	public FavFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {


		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_fav, container, false);

		//Firebase
		db = FirebaseFirestore.getInstance();
		mAuth = FirebaseAuth.getInstance();
		mUser = mAuth.getCurrentUser();

		mRecyclerEvent = view.findViewById(R.id.fragment_fav_recycler);

		//Set properties of RecyclerView
		mRecyclerEvent.setHasFixedSize(true);
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());
		mRecyclerEvent.setLayoutManager(layoutManager);
		mRecyclerEvent.setItemAnimator(new DefaultItemAnimator());
		mRecyclerEvent.addItemDecoration(new DividerItemDecoration(container.getContext(), LinearLayoutManager.VERTICAL));

		//Set query
		Query query = db.collection("_events")
				.orderBy("from",Query.Direction.ASCENDING);

		//Set options
		FirestoreRecyclerOptions<Event> options =  new FirestoreRecyclerOptions.Builder<Event>()
				.setQuery(query, Event.class)
				.build();

		mFirestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Event, HomeFragment.EventHolder>(options) {
			@Override
			public void onBindViewHolder(final HomeFragment.EventHolder holder, int position, final Event event) {

				//Set view
				final String id = event.getId();
				String club_id = event.getClub_id();
				String name = event.getName();
				String image = event.getImage();
				String description = event.getDescription();
				String location = event.getLocation();
				Date from = event.getFrom();
				Date to = event.getTo();
				int ge_point = event.getGe_point();
				ArrayList<String> tags = event.getTags();
				ArrayList<String> keywords = event.getKeywords();
				String qr_id = event.getQr_id();
				String spreadsheet = event.getSpreadsheet();
				String security_check = event.getSecurity_check();

				holder.setTitle(name);
				holder.setInfo(description);
				holder.setImage(image);
				holder.setLocation(location);
				holder.setDate(from);


				if(MainActivity.userModel != null){

					//Get favorite events.
					ArrayList<String> favoriteEvents = MainActivity.userModel.getFavoriteEvents();
					boolean isPresent = false;

					for(String event_id: favoriteEvents){
						if(event_id.equals(id)){
							isPresent = true;
							//Set their icon selected
							holder.btnFav.setImageResource(R.drawable.ic_favorite_selected);
						}
					}

					if(!isPresent){

						holder.hide();
					}

				}


				//Remove it from favorite events
				holder.btnFav.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						if(MainActivity.userModel != null){
							ArrayList<String> favoriteEvents = MainActivity.userModel.getFavoriteEvents();

							for (Iterator<String> iterator = favoriteEvents.iterator(); iterator.hasNext(); ) {
								String value = iterator.next();

								if(value.equals(id)){

									iterator.remove();
									MainActivity.userModel.setFavoriteEvents(favoriteEvents);
									//Hide item
									holder.hide();

									db.collection("users").document(mAuth.getCurrentUser().getUid()).update("favoriteEvents", favoriteEvents);

								}

							}

						}

					}
				});

				holder.btnShare.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						holder.btnShare.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								//Share it
								DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy\n 'Time: 'H:m");
								String date = dateFormat.format(event.getFrom());
								Intent intent = new Intent(android.content.Intent.ACTION_SEND);
								intent.setType("text/plain");
								intent.putExtra(android.content.Intent.EXTRA_SUBJECT, event.getName());
								intent.putExtra(android.content.Intent.EXTRA_TEXT, "We want to see you among us! Do you want to attend \""
										+ event.getName()
										+ "\" event?\n\nEvent Description: "
										+ event.getDescription()
										+ "\n\nDate: " + date
										+ "\n\nLocation: "
										+ event.getLocation());
								startActivity(Intent.createChooser(intent, "Share event!"));
							}
						});
					}
				});


				// Added by Zeyad on 30/4/18
				holder.btnNotify.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//Properties
						long calID = 1;
						long startMillis = 0;
						long endMillis = 0;

						//Initiate properties
						startMillis = event.getFrom().getTime();
						endMillis = event.getTo().getTime();
						ContentResolver cr = getContext().getContentResolver();
						ContentValues values = new ContentValues();

						//Populate the event with needed information
						values.put(CalendarContract.Events.DTSTART, startMillis);
						values.put(CalendarContract.Events.DTEND, endMillis);
						values.put(CalendarContract.Events.TITLE, event.getName());
						values.put(CalendarContract.Events.DESCRIPTION, event.getDescription());
						values.put(CalendarContract.Events.CALENDAR_ID, calID);
						values.put(CalendarContract.Events.EVENT_LOCATION, event.getLocation());
						values.put(CalendarContract.Events.ALL_DAY, false);
						values.put(CalendarContract.Events.HAS_ALARM, 1);
						values.put(CalendarContract.Events.EVENT_TIMEZONE, "Turkey");

						//Check and ask for permissions
						if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
								&& ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

							ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR} , REQUEST_CALENDAR );

						} else if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
								&& ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {

							SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
							//Insert event
							Uri event = cr.insert(CalendarContract.Events.CONTENT_URI, values);

							//Set reminder for given event
							long notifyID = Long.parseLong( event.getLastPathSegment());

							if(!sharedPreferences.getBoolean(id,false)) {

								ContentValues reminders = new ContentValues();
								reminders.put(CalendarContract.Reminders.EVENT_ID, notifyID);
								reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
								reminders.put(CalendarContract.Reminders.MINUTES,60);
								Uri uri2 = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);

								//Need a collection for notification events in user collection to retrieve set events
								holder.btnNotify.setImageResource(R.drawable.ic_notifications_active);

								//Code to remove event if button is pressed again, notifyID needs to be saved in Database however
								cr = getContext().getContentResolver();
								values = new ContentValues();

								SharedPreferences.Editor editor = sharedPreferences.edit();
								editor.putLong( id + " ", notifyID);
								editor.putBoolean(id, true);
								editor.commit();
								//	Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, notifyID);

								Toast.makeText(getContext(), "You will receive notifications for this event",
										Toast.LENGTH_LONG).show();

							} else {

								notifyID = sharedPreferences.getLong(id + " ",2);



								Uri eventsUri;
								int osVersion = android.os.Build.VERSION.SDK_INT;
								if (osVersion <= 7) { //up-to Android 2.1
									eventsUri = Uri.parse("content://calendar/events");
								} else { //8 is Android 2.2 (Froyo) (http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)
									eventsUri = Uri.parse("content://com.android.calendar/events");
								}
								ContentResolver resolver = getActivity().getContentResolver();


								resolver.delete(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, notifyID), null, null);


								//		ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, notifyID);
								holder.btnNotify.setImageResource(R.drawable.ic_notifications_none);
								SharedPreferences.Editor editor = sharedPreferences.edit();
								editor.putLong( id, notifyID);
								editor.putBoolean(id, false);
								editor.commit();

							}


						}


					}
				});


			}

			@Override
			public HomeFragment.EventHolder onCreateViewHolder(ViewGroup group, int i) {
				// Create a new instance of the ViewHolder, in this case we are using a custom
				// layout called R.layout.message for each item
				View view = LayoutInflater.from(group.getContext())
						.inflate(R.layout.event_list_item, group, false);

				return new HomeFragment.EventHolder(view);
			}
		};

		mRecyclerEvent.setAdapter(mFirestoreRecyclerAdapter);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		//Start listening
		mFirestoreRecyclerAdapter.startListening();
	}

	@Override
	public void onStop() {
		super.onStop();
		//Stop listening
		mFirestoreRecyclerAdapter.stopListening();
	}

}
