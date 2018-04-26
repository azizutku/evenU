package com.bilkentazure.evenu.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.bilkentazure.evenu.LoginActivity;
import com.bilkentazure.evenu.MainActivity;
import com.bilkentazure.evenu.ScannerActivity;
import com.bilkentazure.evenu.R;
import com.bilkentazure.evenu.VerifyActivity;
import com.bilkentazure.evenu.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import javax.net.ssl.HttpsURLConnection;
import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Zeyad Khaled on 4/21/2018.
 * Scanner fragment where Android permission are being checked, and actions are taken
 * accordingly.
 */
public class ScannerFragment extends Fragment {

    // Properties
    private static final int REQUEST_CAMERA = 0;
    private ImageButton qrScan;
    private TextView resultView;
    private String android_id;
    private String android_id_db;
    private String intentResult;
    private String userEmail;
    private String userName;
    private String userID;
    private String spreadsheetURL;
    private String uid;
    private FirebaseFirestore db;
    private boolean is_ge;
    private double ge;
    private double ge_total;
    private Date from_time;
    private Date to_time;
    private ProgressDialog processProgress;


    /**
     *                          #@Checks Flow Chart@#
     *                                                          |--> Check GE -> Update GE
     * Check EVENT_ID -> Check Timestamp --> Check Android_id_db --> Retrieve Attendee's details -> SendRequest
     *                                                         |--> Add Android_id to Database
     */

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        //Initializing  necessary properties
        processProgress = new ProgressDialog(getContext());
        db = FirebaseFirestore.getInstance();
        android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        //Initialize Firebase userID
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid();

        //Initialize view
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        resultView = view.findViewById(R.id.textViewResult);

        //Opening intent when scan button is being pressed and making necessary permission checks.
        qrScan = view.findViewById(R.id.scan_qr);
        qrScan.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //Android version check
                    if (ContextCompat.checkSelfPermission(getActivity(), CAMERA) != PackageManager.PERMISSION_GRANTED) { //Camera permission check
                        ActivityCompat.requestPermissions(getActivity(), new String[]{ CAMERA}, REQUEST_CAMERA);
                    }
                    else {
                        // Already grant Camera permission. Now call your QR scan Activity
                        Intent intent = new Intent(getActivity(),ScannerActivity.class);
                        startActivityForResult(intent, 1);
                    }
                } else {
                    Intent intent = new Intent(getActivity(),ScannerActivity.class);
                    startActivityForResult(intent, 1);
                }

            }
        });
        return view;
	}

    /**
     * Asking Android for permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getActivity(),ScannerActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(getContext(), "Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Processing of the QR decoded data and taking actions accordingly
     * @param requestCode intent code
     * @param resultCode intent result code
     * @param data intent data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if( resultCode == RESULT_OK){
                intentResult  = data.getStringExtra("result");

                /**
                 * Checks flow
                 * ProcessQRResult() method checks if eventID "intentResult" is valid or not
                 * If it's valid > Call isTimeValid() if time of scan if between event time
                 * Retrieve android_id_db  >  calls isScannedBefore() method
                 * isScannedBefore() checks if android_id_db from database equals the current android_id
                 * If it doesn't > isScannedBefore() is called
                 * If not scanned before > call attendeeDetails() which returns all user's details
                 * If all user Details aren't null > it gets gePoints and updates them
                 * Then > it call's sendRequest().execute()
                 * SendRequest class checks if Http request was correct
                 * If Http request was correct > Changes the result view to success
                 * isScannedBefore() finally calls addAndroidToDatabase() method.
                 * ***After each check, success or fail message is displayed and proper textView is displayed,
                 * also progress bar disappear after result of progress***
                 */

                if ( isConnectedToInternet()) {
                    processQRResult(intentResult);
                } else {
                    resultView.setTextColor(Color.RED);
                    resultView.setText("Please connect to the internet");
                }

                }
            }
            if (resultCode == RESULT_CANCELED) {
                resultView.setTextColor(Color.RED);
                resultView.setText("Scan was cancelled!");
            }
    }

    /**
     * Checks if eventID is valid or not
     * @param eventID is the event ID
     */
    public void processQRResult ( String eventID) {
        showProgress(); // Display progress

        db.collection("events").document(eventID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snap = task.getResult();
                    if (snap.exists() && snap != null) {

                        from_time = snap.getDate("from");
                        to_time = snap.getDate("to");

                        if ( isTimeValid() ) {
                            android_id_db = snap.getString("android_id");
                            spreadsheetURL = snap.getString("spreadsheet");
                            /**
                             * Could put a check here if event won't have a permanent GE property
                             */
                            ge =  snap.getDouble("ge");
                            isScannedBefore();
                        }

                    } else {
                        processProgress.dismiss();
                        resultView.setTextColor(Color.RED);
                        resultView.setText("Event doesn't exist in database!");
                    }
                } else {
                    processProgress.dismiss();
                    resultView.setTextColor(Color.RED);
                    resultView.setText("Event doesn't exist in database!");
                }
            }
        });
    }

    /**
     * Check validity of Time range of the event by  comparing it to current Time of scanning
     * @return true or false
     */
    public boolean isTimeValid() {
        Date current_time = new Date(); // Create date object with current time
        if ( current_time.after(from_time) && current_time.before(to_time)) {
            return true;

        } else if ( current_time.before(from_time) )  {
            resultView.setTextColor(Color.RED);
            SimpleDateFormat df = new SimpleDateFormat("k:mm");
            resultView.setText("Event will start at " + df.format(to_time));
            processProgress.dismiss();
            return false;

        } else {
            resultView.setTextColor(Color.RED);
            resultView.setText("Event has expired!");
            processProgress.dismiss();
            return false;
        }
    }

    /**
     * Checks if android_id_db is equal to current android_id  or not, if not it executes a chain of
     * methods.
     */
    public void isScannedBefore() {
        if ( !android_id_db.equals(android_id)) {

            attendeeDetails(); // Retrieve attendeeDetails > Spreadsheet > SendRequest
            addAndroidIDToDatabase(); // Add current android ID in this event details
            resultView.setTextColor(Color.GREEN);
            resultView.setText("Scan success!");
            processProgress.dismiss();

        } else {
            resultView.setTextColor(Color.RED);
            resultView.setText("THIS EVENT HAS BEEN SCANNED BEFORE");
            processProgress.dismiss();

        }
    }

    /**
     * Call Firestore and retrieve data from users collection
     */
    public void attendeeDetails()  {
        // Entering users collection to retrieve the user details
        db.collection("users").document( uid ).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snap = task.getResult();
                    if (snap.exists() && snap != null) {
                        userID = snap.getString("schoolID");
                        userName = snap.getString("name");
                        userEmail = snap.getString("email");
                        ge_total = snap.getDouble("ge_total");
                        is_ge = snap.getBoolean("is_ge");

                        isTakingGE();
                        new SendRequest().execute();
                    }
                } else {
                    resultView.setTextColor(Color.RED);
                    resultView.setText("Database Error, Try Again");
                }
            }
        });
    }



    /**
     * Add current user android phone to this event
     */
    public void addAndroidIDToDatabase() {
        db.collection("events").document(intentResult).update("android_id" , android_id);
    }


    /**
     * Check if user is taking GE or not and update accordingly if taking
     */
    public void isTakingGE() {
        if ( is_ge ) {
            updateGE();
        }
    }

    /**
     * Update GE points by adding current GE points user have and new amount that event gave
     */
    public void updateGE() {
        db.collection("users").document(uid).update("ge_total" , ge_total + ge);
    }


    /**
     * Showing QR Processing progress
     */
    public void showProgress() {
        processProgress.setTitle("Processing QR");
        processProgress.setMessage("Please wait!");
        processProgress.setCanceledOnTouchOutside(false);
        processProgress.show();
    }

    /**
     * Check if application is connected to internet or not
     * @return
     */
    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }


    /**
     * Sending Scanned Result to Google Sheets through an inner class
     */
    private class SendRequest extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... arg0) {

            try{

                //App Script URL here
                String scriptURL = "https://script.google.com/macros/s/AKfycbzCBqbQY_YX3GpXF8vVD8obru0DrUOUrbis4wIFTjwOTjbUIChA/exec";
                URL url = new URL(scriptURL);
                JSONObject postDataParams = new JSONObject();


                //Spreadsheet details and required data
                postDataParams.put("name", userName);
                postDataParams.put("email", userEmail);
                postDataParams.put("id", userID);
                postDataParams.put("url", spreadsheetURL);


                // Initiating request connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //Writing the parameters to the request
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write( getPostDataString( postDataParams));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader( new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();
                }
                else {
                    return new String("false : " + responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), result,
                    Toast.LENGTH_LONG).show();

        }
    }

    /**
     * Creating post request
     */
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    /**
     * @Deprecated
     * This is for resetting the database retrieved parameters after sending the data
     */
    public void resetData() {
        userEmail = "";
        userName = "";
        userID = "";
        spreadsheetURL = "";
        android_id_db = "";
        ge = 0;
        ge_total = 0;
    }

    /**
     * @Deprecated
     * Retrieve android_id_db from the database > events collection
     * @param eventID is the event ID
     */
    public void retrieveAndroidIDFromDatabase( String eventID) {
        db.collection("events").document(eventID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot snap = task.getResult();
                    if (snap.exists() && snap != null) {
                        android_id_db = snap.getString("android_id");
                        if ( android_id_db != null)
                            isScannedBefore();
                        else {
                            resultView.setTextColor(Color.RED);
                            resultView.setText("Database ERROR");
                        }
                    }
                }
            }
        });
    }

    /**
     * @Deprecated
     * Call Firestore and retrieve data from events collection
     */
    public void spreadsheetDetails( String eventID) {
        db.collection("events").document(eventID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot snap = task.getResult();
                    if (snap.exists() && snap != null) {
                        spreadsheetURL = snap.getString("spreadsheet");
                        ge =  snap.getDouble("ge"); // Testing
                        isTakingGE(); //Check is taking GE and update it
                        if ( spreadsheetURL != null ) {

                            resultView.setTextColor(Color.GREEN);
                            resultView.setText("Scan Success!" + intentResult);
                            new SendRequest().execute();

                        } else {
                            Toast.makeText(getContext(), "ERROR",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }
        });
    }
}

