package com.bilkentazure.evenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class RegisterActivity extends AppCompatActivity {

	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		mToolbar = findViewById(R.id.register_toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Create Account");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
