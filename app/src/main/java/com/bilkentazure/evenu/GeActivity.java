package com.bilkentazure.evenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
 * Created by Ayşegül Gökçe on 2.05.2018.
 */

public class GeActivity  extends AppCompatActivity {
    private static final String TAG = "GeActivity";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Toolbar mToolbar;
    private ProgressDialog mProgress;

    private RelativeLayout rlt;

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

        mProgress = new ProgressDialog(this);


    }

}
