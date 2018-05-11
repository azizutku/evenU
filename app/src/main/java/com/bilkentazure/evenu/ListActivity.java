package com.bilkentazure.evenu;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bilkentazure.evenu.fragments.HomeFragment;
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

public class ListActivity extends AppCompatActivity {

	//Firebase
	private FirebaseFirestore db;
	private FirebaseAuth mAuth;
	private FirebaseUser mUser;
	private FirestoreRecyclerAdapter mFirestoreRecyclerAdapter;

	private Toolbar mToolbar;
	private static final int REQUEST_CALENDAR = 0;
	private RecyclerView mRecyclerEvent;

	private int counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		mToolbar = findViewById(R.id.list_toolbar);

		db = FirebaseFirestore.getInstance();
		mAuth = FirebaseAuth.getInstance();
		mUser = mAuth.getCurrentUser();

		Intent intent = getIntent();
		String name = intent.getExtras().getString("name","List Activity");
		int type =  intent.getExtras().getInt("type",0);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle( name);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		counter = 0;

		mRecyclerEvent = findViewById(R.id.list_recycler_list);
		final TextView txtInfo = findViewById(R.id.list_txt_info);

		mRecyclerEvent.setHasFixedSize(true);
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
		mRecyclerEvent.setLayoutManager(layoutManager);
		mRecyclerEvent.setItemAnimator(new DefaultItemAnimator());
		mRecyclerEvent.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


		//addEvent();
		Query query;
		switch(type) {

			case 0:
				query = db.collection("_events")
						.orderBy("from", Query.Direction.ASCENDING).whereEqualTo("target_department", name);
				break;

			case 1:
				query = db.collection("_events")
						.orderBy("from", Query.Direction.ASCENDING).whereEqualTo("club_name", name);
				break;

			case 2:
				query = db.collection("_events")
						.orderBy("from", Query.Direction.ASCENDING).whereEqualTo("target_interest", name);
				break;

			default:
				query = db.collection("_events")
						.orderBy("from", Query.Direction.ASCENDING).whereEqualTo("target_department", name);

		}

		FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
				.setQuery(query, Event.class)
				.build();

		mFirestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Event, HomeFragment.EventHolder>(options) {
			@Override
			public void onBindViewHolder(final HomeFragment.EventHolder holder, int position, final Event event) {

				txtInfo.setVisibility(View.GONE);

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


				if (MainActivity.userModel != null) {

					ArrayList<String> favoriteEvents = MainActivity.userModel.getFavoriteEvents();

					for (String event_id : favoriteEvents) {

						if (event_id.equals(id)) {
							holder.btnFav.setImageResource(R.drawable.ic_favorite_selected);
						}
					}

				}


				holder.btnFav.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						if (MainActivity.userModel != null) {
							ArrayList<String> favoriteEvents = MainActivity.userModel.getFavoriteEvents();

							boolean isPresent = false;

							for (Iterator<String> iterator = favoriteEvents.iterator(); iterator.hasNext(); ) {
								String value = iterator.next();

								if (value.equals(id)) {

									//FIXME add success listener
									iterator.remove();
									MainActivity.userModel.setFavoriteEvents(favoriteEvents);
									isPresent = true;
									holder.btnFav.setImageResource(R.drawable.ic_favorite_border);

									db.collection("users").document(mAuth.getCurrentUser().getUid()).update("favoriteEvents", favoriteEvents);

								}


							}

							if (!isPresent) {
								favoriteEvents.add(id);
								MainActivity.userModel.setFavoriteEvents(favoriteEvents);
								holder.btnFav.setImageResource(R.drawable.ic_favorite_selected);
								db.collection("users").document(mAuth.getCurrentUser().getUid()).update("favoriteEvents", favoriteEvents);
							}


						}

					}
				});

				holder.btnShare.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy\n 'Time: ' H:m");
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


				holder.mainRlt.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ListActivity.this , EventView.class);
						intent.putExtra("event", event);
						startActivity(intent);
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
						ContentResolver cr =getApplicationContext().getContentResolver();
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
						if (ActivityCompat.checkSelfPermission(ListActivity.this, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
								&& ActivityCompat.checkSelfPermission(ListActivity.this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

							ActivityCompat.requestPermissions(ListActivity.this , new String[]{android.Manifest.permission.WRITE_CALENDAR, android.Manifest.permission.READ_CALENDAR} , REQUEST_CALENDAR );

						} else if (ActivityCompat.checkSelfPermission(ListActivity.this, android.Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
								&& ActivityCompat.checkSelfPermission(ListActivity.this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {

							//Insert event
							Uri event = cr.insert(CalendarContract.Events.CONTENT_URI, values);

							//Set reminder for given event
							long notifyID = Long.parseLong( event.getLastPathSegment());
							ContentValues reminders = new ContentValues();
							reminders.put(CalendarContract.Reminders.EVENT_ID, notifyID);
							reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
							reminders.put(CalendarContract.Reminders.MINUTES,60);
							Uri uri2 = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);

							//Need a collection for notification events in user collection to retrieve set events
							holder.btnNotify.setImageResource(R.drawable.ic_notifications_active);

							//Code to remove event if button is pressed again, notifyID needs to be saved in Database however
							cr = getApplicationContext().getContentResolver();
							values = new ContentValues();
							//	Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, notifyID);

							Toast.makeText(getApplicationContext(), "You will receive notifications for this event",
									Toast.LENGTH_LONG).show();
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
	}

	@Override
	public void onStart() {
		super.onStart();
		mFirestoreRecyclerAdapter.startListening();
	}

	@Override
	public void onStop() {
		super.onStop();
		mFirestoreRecyclerAdapter.stopListening();
	}
}
