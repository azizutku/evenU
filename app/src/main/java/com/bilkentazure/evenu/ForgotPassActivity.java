package com.bilkentazure.evenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ForgotPassActivity extends AppCompatActivity {

	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_pass);

		mToolbar = findViewById(R.id.forgot_toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Forgot password");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}
}
