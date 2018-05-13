package com.bilkentazure.evenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Aziz Utku Kağıtcı on 17/04/2018
 * This activity is for logging.
 * It checks database and do Login processing.
 * @author Aziz Utku Kağıtcı
 * @version 17/04/2018
 */
public class LoginActivity extends AppCompatActivity {

	private static final String TAG = "LoginActivity";

	//Firebase
	private FirebaseAuth mAuth;
	private FirebaseFirestore db;

	private Toolbar mToolbar;

	private ProgressDialog mProgress;
	private Button btnSignIn;
	private RelativeLayout rlt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//Set toolbar
		mToolbar = findViewById(R.id.login_toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Login");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//Firebase
		mAuth = FirebaseAuth.getInstance();
		db = FirebaseFirestore.getInstance();

		btnSignIn = findViewById(R.id.login_btn_login);
		rlt = findViewById(R.id.login_rlt);
		TextView txtForgot = findViewById(R.id.login_txt_forgot_pass);
		final EditText edtEmail = findViewById(R.id.login_edt_email);
		final EditText edtPass = findViewById(R.id.login_edt_password);

		mProgress = new ProgressDialog(this);

		txtForgot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Open ForgatPassActivity
				Intent intent = new Intent(LoginActivity.this,ForgotPassActivity.class);
				startActivity(intent);
			}
		});

		btnSignIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				btnSignIn.setEnabled(false);

				//Hide keyboard
				View view = LoginActivity.this.getCurrentFocus();
				if (view != null) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}

				String email = edtEmail.getText().toString();
				String password = edtPass.getText().toString();


				if(!email.isEmpty() && !password.isEmpty()){
					mProgress.setTitle("Logging...");
					mProgress.setMessage("Please wait!");
					mProgress.setCanceledOnTouchOutside(false);
					mProgress.show();

					login(email, password);
				} else {
					Snackbar snackbar = Snackbar.make(rlt,"Please fill all fields!", Snackbar.LENGTH_LONG);
					snackbar.show();
				}

			}
		});


	}

	private void login(String email, String password){
		//Login with email and password
		mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {

				if(task.isSuccessful()){
					Log.d(TAG,"Signing successfully");

					//get user id and token id
					String userID = FirebaseAuth.getInstance().getUid();
					String tokenID = FirebaseInstanceId.getInstance().getToken();

					//Set new token id
					db.collection("users").document(userID).update("tokenID",tokenID).addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {

							if(task.isSuccessful()) {
								Log.d(TAG,"Update document successfully");

								mProgress.dismiss();
								btnSignIn.setEnabled(true);

								//If email is verified start MainActivity
								if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){

									Intent intent = new Intent(LoginActivity.this, MainActivity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
									startActivity(intent);
									finish();

								}

								//Else start VerifyActivity
								else {

									Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
									startActivity(intent);
									finish();

								}



							}

							else{
								Log.w(TAG,"Update document failure",task.getException());
								mProgress.hide();
								btnSignIn.setEnabled(true);
								Snackbar snackbar = Snackbar.make(rlt,"Authentication failed",Snackbar.LENGTH_LONG);
								snackbar.show();
							}
						}
					});


				}

				else {
					Log.w(TAG,"Signing failure", task.getException());
					mProgress.hide();
					btnSignIn.setEnabled(true);
					Snackbar snackbar = Snackbar.make(rlt,"Authentication failed",Snackbar.LENGTH_LONG);
					snackbar.show();
				}

			}
		});
	}
}
