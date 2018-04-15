package com.bilkentazure.evenu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {


	//Firebase
	private FirebaseAuth mAuth;
	private FirebaseFirestore db;
	private FirebaseUser mCurrentUser;

	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mToolbar = findViewById(R.id.main_toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("evenU");

		//Firebase
		mAuth = FirebaseAuth.getInstance();
		db = FirebaseFirestore.getInstance();
		mCurrentUser = mAuth.getCurrentUser();

		updateState(mCurrentUser);
	}

	private void updateState(FirebaseUser user){

		if( user == null){
			sendToStart();
		}
		else {

		}

	}

	private void sendToStart(){
		Intent intent = new Intent(MainActivity.this,StartActivity.class);
		startActivity(intent);
		finish();
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
