package com.bilkentazure.evenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bilkentazure.evenu.adapters.CustomEventAdapter;
import com.bilkentazure.evenu.adapters.ListAdapter;
import com.bilkentazure.evenu.fragments.HomeFragment;
import com.bilkentazure.evenu.models.Event;
import com.bilkentazure.evenu.models.Item;
import com.bilkentazure.evenu.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ayşegül Gökçe on 2.05.2018.
 */

public class GeActivity  extends AppCompatActivity {

    private static final String TAG = "GeActivity";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Toolbar mToolbar;
    private ProgressDialog mProgress;
    private SeekBar mSeekBar;
    private TextView txtCounter;
    private RecyclerView recyclerView;

    private RelativeLayout rlt;
	private CustomEventAdapter mAdapter;
	private List<Event> eventList;
	private User mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ge_points);

        mToolbar = findViewById(R.id.ge_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("GE Progress");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rlt = findViewById(R.id.reg_rlt);
        txtCounter = findViewById(R.id.ge_counter);
        mSeekBar = findViewById(R.id.ge_seekBar);
        recyclerView = findViewById(R.id.ge_recycler);


        mProgress = new ProgressDialog(this);




        updateData();






    }

    private class getEvents extends AsyncTask<String,Void,String> {

		@Override
		protected String doInBackground(String... strings) {

			final String[] data = strings;
			for(int i = 0; i < data.length; i++){
				String event_id = data[i];
				final int finalI = i;
				db.collection("_events").document(event_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
					@Override
					public void onSuccess(DocumentSnapshot documentSnapshot) {
						Event event = documentSnapshot.toObject(Event.class);
						eventList.add(event);
						if(finalI == data.length - 1){

							mAdapter = new CustomEventAdapter(GeActivity.this,eventList);
							mAdapter.notifyDataSetChanged();
							recyclerView.setAdapter(mAdapter);
							synchronized (recyclerView){
								recyclerView.notifyAll();
							}

						}
					}
				});

			}

			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);

		}
	}

	private void updateData(){

    	mProgress.setTitle("Getting Data");
    	mProgress.setMessage("Please wait...");
    	mProgress.setCanceledOnTouchOutside(false);
    	mProgress.show();

    	db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if(task.isComplete()){

					DocumentSnapshot documentSnapshot = task.getResult();

					if(documentSnapshot != null){


						MainActivity.userModel = documentSnapshot.toObject(User.class);
						mProgress.hide();

						mCurrentUser = MainActivity.userModel;

						//updating ge progress bar according to user's points

						int ge_point = mCurrentUser.getGeTotal();

						txtCounter.setText(ge_point + "/200");

						mSeekBar.setMax(200);
						mSeekBar.setEnabled(false);
						mSeekBar.setProgress(ge_point);

						//past events scroll bar infos

						String[] data = new String[ mCurrentUser.getAttendedEvents().size()];
						data = mCurrentUser.getAttendedEvents().toArray(data);


						eventList = new ArrayList<>();
						mAdapter = new CustomEventAdapter(GeActivity.this,eventList);

						RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
						recyclerView.setLayoutManager(layoutManager);
						recyclerView.setItemAnimator(new DefaultItemAnimator());
						recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
						recyclerView.setAdapter(mAdapter);
						new getEvents().execute(data);


					}

					else {
						mProgress.hide();
					}

				} else {

					mProgress.hide();

				}
			}
		});


	}

}
