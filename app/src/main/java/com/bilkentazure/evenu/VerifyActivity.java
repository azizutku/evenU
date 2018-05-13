package com.bilkentazure.evenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Aziz Utku Kağıtcı on 16/04/2018
 * This activity does verification processing.
 * Unless user verify his/her email, don't go forward.
 * @author Aziz Utku Kağıtcı
 * @version 17/04/2018
 */
public class VerifyActivity extends AppCompatActivity {

	private static final String TAG = "VerifyActivity";

	//Firebase
	private FirebaseAuth mAuth;

	private ProgressDialog mProgress;
	private Button btnSend, btnContinue;
	private RelativeLayout rlt;

	private boolean verified = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify);

		//Firebase
		mAuth = FirebaseAuth.getInstance();
		final FirebaseUser user = mAuth.getCurrentUser();

		btnSend = findViewById(R.id.verify_btn_send);
		btnContinue = findViewById(R.id.verify_btn_continue);
		rlt = findViewById(R.id.verify_rlt);

		mProgress = new ProgressDialog(this);

		//Reload user to get current state
		user.reload();
		if (!user.isEmailVerified()) {
			mProgress.setTitle("Sending...");
			mProgress.setMessage("Please wait!");
			mProgress.show();
			//Send email verification if user's mail is not verified
			sendEmailVerification();
		} else {
			//Else open MainActivity
			Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			finish();
		}

		btnContinue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						//Check verification
						if (!user.isEmailVerified()) {
							Snackbar snackbar = Snackbar.make(rlt, "Your email address has not been verified yet!", Snackbar.LENGTH_LONG);
							snackbar.show();
						} else {
							Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intent);
							finish();
						}
					}
				});
			}
		});

		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Send verification
				btnSend.setEnabled(false);
				mProgress.setTitle("Sending...");
				mProgress.setMessage("Please wait!");
				mProgress.show();

				sendEmailVerification();
			}
		});


	}

	private void sendEmailVerification() {
		FirebaseUser user = mAuth.getCurrentUser();
		user.reload();
		if (user != null) {
			//Send verification
			user.sendEmailVerification()
					.addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if (task.isSuccessful()) {
								Log.d(TAG, "Email sent.");
								mProgress.hide();
								btnSend.setEnabled(true);
								Snackbar snackbar = Snackbar.make(rlt, "We have sent you an email for account verification!", Snackbar.LENGTH_LONG);
								snackbar.show();
							} else {
								Log.w(TAG,"emailSent:failure");
								mProgress.hide();
								btnSend.setEnabled(true);
							}
						}
					});
		}
	}
}
