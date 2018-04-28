package com.bilkentazure.evenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Aziz Utku Kağıtcı on 17/04/2018
 * This activity is for forgetting password.
 * It checks database and do forget password processing.
 * @author Aziz Utku Kağıtcı
 * @version 17/04/2018
 */
public class ForgotPassActivity extends AppCompatActivity {

	private Toolbar mToolbar;
	private ProgressDialog mProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_pass);

		mToolbar = findViewById(R.id.forgot_toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Forgot password");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		final EditText edtEmail = findViewById(R.id.forgot_edt_email);
		final Button btnSend = findViewById(R.id.forgot_btn_send);
		final RelativeLayout rlt = findViewById(R.id.forgot_rlt);

		mProgress = new ProgressDialog(this);

		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				btnSend.setEnabled(false);

				View view = ForgotPassActivity.this.getCurrentFocus();

				if(view != null) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(),0);
				}

				String email = edtEmail.getText().toString();

				mProgress.setTitle("Sending...");
				mProgress.setMessage("Please wait!");
				mProgress.setCanceledOnTouchOutside(false);
				mProgress.show();

				FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						mProgress.hide();
						Snackbar snackbar = Snackbar.make(rlt,"Email sent", Snackbar.LENGTH_LONG);
						snackbar.show();
						btnSend.setEnabled(true);
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						mProgress.hide();
						Snackbar snackbar = Snackbar.make(rlt,"There is no user using this email!", Snackbar.LENGTH_LONG);
						snackbar.show();
						btnSend.setEnabled(true);
					}
				});
			}
		});

	}
}
