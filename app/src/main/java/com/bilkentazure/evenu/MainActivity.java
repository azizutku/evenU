package com.bilkentazure.evenu;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bilkentazure.evenu.adapters.PagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {


	//Firebase
	private FirebaseAuth mAuth;
	private FirebaseFirestore db;
	private FirebaseUser mCurrentUser;

	private PagerAdapter pagerAdapter;
	private TabLayout tabLayout;
	private ViewPager viewPager;

	private Toolbar mToolbar;

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

		mToolbar = findViewById(R.id.main_toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle(" Newsfeed");
		getSupportActionBar().setIcon(R.drawable.ic_icon);

		//Firebase
		mAuth = FirebaseAuth.getInstance();
		db = FirebaseFirestore.getInstance();
		mCurrentUser = mAuth.getCurrentUser();

		updateState(mCurrentUser);

		pagerAdapter = new PagerAdapter(getSupportFragmentManager());
		tabLayout = findViewById(R.id.main_tab_layout);
		viewPager = findViewById(R.id.main_view_pager);

		viewPager.setAdapter(pagerAdapter);
		tabLayout.setupWithViewPager(viewPager);

		for(int i = 0; i < tabLayout.getTabCount(); i++) {
			tabLayout.getTabAt(i).setIcon(ICONS_OF_TABS[i]);
		}


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

	private void updateState(FirebaseUser user){

		if( user == null){
			sendToStart();
		}
		else {
			if(!user.isEmailVerified()){
				sendToVerify();
			}
		}

	}

	private void sendToStart(){
		Intent intent = new Intent(MainActivity.this,StartActivity.class);
		startActivity(intent);
		finish();
	}

	private void sendToVerify(){
		Intent intent = new Intent(MainActivity.this,VerifyActivity.class);
		startActivity(intent);
		finish();
	}

	private void sendToAbout(){
		Intent intent = new Intent(MainActivity.this,AboutActivity.class);
		startActivity(intent);
		//finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main_menu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch(item.getItemId()){
			case R.id.menu_main_help:
				break;
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
