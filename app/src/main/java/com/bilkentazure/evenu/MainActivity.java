package com.bilkentazure.evenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bilkentazure.evenu.adapters.PagerAdapter;
import com.bilkentazure.evenu.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Created by Aziz Utku Kağıtcı on 16/04/2018
 * This is main activitiy of app.
 * It includes all fragments.
 * It checks whether there is current user who already signed in.
 * If there is no user, it opens StartActivity.
 * If there is user but user's emails not verified, it opens VerifyActivity.
 * @author Aziz Utku Kağıtcı
 * @version 17/04/2018
 */
public class MainActivity extends AppCompatActivity {


	//Firebase
	private FirebaseAuth mAuth;
	private FirebaseFirestore db;
	private FirebaseUser mCurrentUser;

	private PagerAdapter pagerAdapter;
	private TabLayout tabLayout;
	private ViewPager viewPager;

	private Toolbar mToolbar;
	private ProgressDialog mProgress;


	public static User userModel;

	//IDs of tab icons
	final int[] ICONS_OF_TABS = new int[]{
			R.drawable.ic_tab_home,
			R.drawable.ic_tab_camera,
			R.drawable.ic_tab_profile,
			R.drawable.ic_tab_list,
			R.drawable.ic_tab_favorite
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Set toolbar
		mToolbar = findViewById(R.id.main_toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle(" Newsfeed");
		getSupportActionBar().setIcon(R.drawable.ic_icon);

		mProgress = new ProgressDialog(this);

		//Firebase
		mAuth = FirebaseAuth.getInstance();
		db = FirebaseFirestore.getInstance();
		mCurrentUser = mAuth.getCurrentUser();

		mProgress.setTitle("Loading");
		mProgress.setMessage("Please wait...");
		mProgress.setCanceledOnTouchOutside(false);
		mProgress.show();

		//Check current user state
		updateState(mCurrentUser);




	}


	private void updateState(FirebaseUser user){

		if( user == null){
			mProgress.dismiss();
			//If user is null send to start
			sendToStart();
		}
		else {
			if(!user.isEmailVerified()){
				mProgress.dismiss();
				//If email is not verified send user to verify page
				sendToVerify();
			} else {


				//get user info
				db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
					@Override
					public void onComplete(@NonNull Task<DocumentSnapshot> task) {
						if (task.isSuccessful()){

							DocumentSnapshot documentSnapshot = task.getResult();

							if(documentSnapshot != null){

								//set data to User object
								userModel = documentSnapshot.toObject(User.class);


							}

						}

						mProgress.dismiss();

						//Set PagerAdapter
						pagerAdapter = new PagerAdapter(getSupportFragmentManager());
						tabLayout = findViewById(R.id.main_tab_layout);
						viewPager = findViewById(R.id.main_view_pager);

						viewPager.setAdapter(pagerAdapter);
						tabLayout.setupWithViewPager(viewPager);

						//Set icons
						for(int i = 0; i < tabLayout.getTabCount(); i++) {
							tabLayout.getTabAt(i).setIcon(ICONS_OF_TABS[i]);
						}

						//Change title by respect to tab
						tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
							@Override
							public void onTabSelected(TabLayout.Tab tab) {

								switch (tabLayout.getSelectedTabPosition()){

									case 0:
										getSupportActionBar().setTitle(" Newsfeed");
										break;
									case 1:
										getSupportActionBar().setTitle(" QR Scanner");
										break;
									case 2:
										getSupportActionBar().setTitle(" My Account");
										break;
									case 3:
										getSupportActionBar().setTitle(" Categorizes");
										break;
									case 4:
										getSupportActionBar().setTitle(" My Events");
										break;
									default:
										getSupportActionBar().setTitle(" evenU");


								}
							}

							@Override
							public void onTabUnselected(TabLayout.Tab tab) {

							}

							@Override
							public void onTabReselected(TabLayout.Tab tab) {

							}
						});


					}
				});



						/*.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
							@Override
							public void onSuccess(DocumentSnapshot documentSnapshot) {
								if(documentSnapshot != null){

									userModel = documentSnapshot.toObject(User.class);

								}
							}
						});*/

			}


		}

	}

	//Send to StartActivity
	private void sendToStart(){
		Intent intent = new Intent(MainActivity.this,StartActivity.class);
		startActivity(intent);
		finish();
	}

	//Send to VerifyActivity
	private void sendToVerify(){
		Intent intent = new Intent(MainActivity.this,VerifyActivity.class);
		startActivity(intent);
		finish();
	}

	//Send to About Page
	private void sendToAbout(){
		Intent intent = new Intent(MainActivity.this,AboutActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//Inflate menu
		getMenuInflater().inflate(R.menu.main_menu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		//Set menu
		switch(item.getItemId()){
			case R.id.menu_main_about:
				sendToAbout();
				break;
			case R.id.menu_main_signout:
				FirebaseAuth.getInstance().signOut();
				sendToStart();
				break;
			default:
				break;
		}

		return true;
	}
}
