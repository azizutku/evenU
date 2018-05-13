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

import com.bilkentazure.evenu.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

/**
 * Created by Aziz Utku Kağıtcı on 17/04/2018
 * This activity is for registering.
 * It checks database and do registration processing.
 * @author Aziz Utku Kağıtcı
 * @version 17/04/2018
 */
public class RegisterActivity extends AppCompatActivity {

	private static final String TAG = "RegisterActivity";

	//Firebase
	private FirebaseAuth mAuth;
	private FirebaseFirestore db;

	private Toolbar mToolbar;
	private ProgressDialog mProgress;

	private Button btnSignUp;
	private RelativeLayout rlt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		mToolbar = findViewById(R.id.register_toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Create Account");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//Firebase
		mAuth = FirebaseAuth.getInstance();
		db = FirebaseFirestore.getInstance();

		rlt = findViewById(R.id.reg_rlt);
		btnSignUp = findViewById(R.id.reg_btn_create_account);
		final EditText edtName = findViewById(R.id.reg_edt_name);
		final EditText edtEmail = findViewById(R.id.reg_edt_email);
		final EditText edtID = findViewById(R.id.reg_edt_id);
		final EditText edtPass = findViewById(R.id.reg_edt_password);

		mProgress = new ProgressDialog(this);

		btnSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				btnSignUp.setEnabled(false);

				View view = RegisterActivity.this.getCurrentFocus();
				if (view != null) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}

				String email = edtEmail.getText().toString();
				String password = edtPass.getText().toString();
				String schoolID = edtID.getText().toString();
				String name = edtName.getText().toString();


				if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){

					if(email.toString().contains("bilkent.edu.tr")){

						mProgress.setTitle("Registering...");
						mProgress.setMessage("Please wait!");
						mProgress.setCanceledOnTouchOutside(false);
						mProgress.show();

						registerUser(email, password, schoolID, name);

					}

					else {
						btnSignUp.setEnabled(true);
						Snackbar snackbar = Snackbar.make(rlt,"Only students on Bilkent University can register!", Snackbar.LENGTH_LONG);
						snackbar.show();

					}
				} else {
					btnSignUp.setEnabled(true);
					Snackbar snackbar = Snackbar.make(rlt,"Please fill all fields!", Snackbar.LENGTH_LONG);
					snackbar.show();
				}


			}
		});


	}

	private void registerUser(final String email, final String password, final String schoolID, final String name){
		mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
			@Override
			public void onSuccess(AuthResult authResult) {

				/*private String schoolId;
				private String email;
				private String name;
				private String department;
				private String image;
				private String thumbImage;
				private String tokenID;
				private int geTotal;
				private boolean takeGe250;
				private boolean takeGe251;
				ArrayList<String> attendedEvents;
				ArrayList<String> favoriteEvents;
				ArrayList<String> subscribedDepartments;
				ArrayList<String> subscribeInterests;
				ArrayList<String> subscribedClubs;*/


				User user = new User(schoolID,
						email,
						name,
						"Computer Science",
						"default",
						"default",
						FirebaseInstanceId.getInstance().getToken(),
						0,
						true,
						true,
						new ArrayList<String>(),
						new ArrayList<String>(),
						new ArrayList<String>(),
						new ArrayList<String>(),
						new ArrayList<String>());

				db.collection("users")
						.document(mAuth.getCurrentUser().getUid())
						.set(user)
						.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {

						if (task.isSuccessful()) {
							Log.d(TAG, "Adding document success");
							mProgress.dismiss();
							btnSignUp.setEnabled(true);
							Intent intent = new Intent(RegisterActivity.this,VerifyActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intent);
							finish();
						}

						else {
							Log.w(TAG, "Error adding document", task.getException());
							mProgress.hide();
							btnSignUp.setEnabled(true);
							Snackbar snackbar = Snackbar.make(rlt, "Registration failed!", Snackbar.LENGTH_LONG);
							snackbar.show();
						}

					}
				});

			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {

				btnSignUp.setEnabled(true);
				Log.w(TAG, "Error registering user", e);
				mProgress.hide();
				btnSignUp.setEnabled(true);
				Snackbar snackbar = Snackbar.make(rlt, "Registration failed!", Snackbar.LENGTH_LONG);
				snackbar.show();
			}
		});
	}
}
