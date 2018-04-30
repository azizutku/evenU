package com.bilkentazure.evenu;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Zeyad Khaled on 27/4/2018.
 * This is QR generating class with securtiy_check implemented in place.
 * QR is being generated based on a given interval Default 15secs. and Security_check
 * code is being changed after the given interval.
 * Old QR codes are unusable.
 * @author Zeyad Khaled
 * @version 27/4/2018
 */

public class Generate extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView viewQR;
    private FirebaseFirestore db;
    private String security_check;
    final private String eventID = "BFnxKhJMM5wqo2dMmF2l"; // Shall be retrieved from calling intent in evenU club class
    private Bitmap generatedQR;
    private CountDownTimer generationTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        db = FirebaseFirestore.getInstance();
        mToolbar = findViewById(R.id.generate_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Generate QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewQR = findViewById(R.id.qr_view);

        new MakeRequestTask().execute();

        doActions();
        //Start a timer with given interval and delay
        generationTimer = new CountDownTimer(5000, 10) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            //Execute actions here
            @Override
            public void onFinish() {
                doActions();
                start();
            }
        }.start();
    }

    /**
     * (1) Update Security check code
     * (2) Generate a QR with new security_check code
     * (3) Draw QR and transfer it to Drawable
     * (4) Change imageview view to the new QR
     */
    public void doActions() {

        try {
            updateSecurityCheck();
            generatedQR = generateQR(security_check + ":" + eventID, 500, 500);
            Drawable d = new BitmapDrawable(getResources(), generatedQR);
            viewQR.setImageDrawable(d);
            Toast.makeText(Generate.this, "QR Updated!", Toast.LENGTH_LONG).show();

        } catch (WriterException e) {
            e.printStackTrace();
        }
        Log.e("QR", "Generated");
    }

    /**
     * Stops the timer when activity is closed
     */
    public void onPause() {
        super.onPause();
        generationTimer.cancel();
    }

    /**
     * generate a QR with given text param
     *
     * @param text   is the String to be generated
     * @param width  is width
     * @param height is height
     * @return a QR with given text
     */
    private Bitmap generateQR(String text, int width, int height) throws WriterException, NullPointerException {

        BitMatrix bitMatrix;

        try {
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.DATA_MATRIX.QR_CODE,
                    width, height, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        int colorWhite = 0xFFFFFFFF;
        int colorBlack = 0xFF000000;

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }


    /**
     * Generate a security check code
     * Update security_check field in database
     */
    public void updateSecurityCheck() {
        security_check = (int) (Math.random() * 1000000) + "";
        db.collection("_events").document(eventID).update("security_check", security_check);
    }

     class MakeRequestTask extends AsyncTask<Void, Void, Spreadsheet> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;


        MakeRequestTask() {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    getApplicationContext(), Arrays.asList(SheetsScopes.SPREADSHEETS))
                    .setBackOff(new ExponentialBackOff());
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Sheets API Android Quickstart")
                    .build();
            Log.e("test", "first");
        }

        /**
         * Background task to call Google Sheets API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected Spreadsheet doInBackground(Void... params) {
            try {
                Log.e("test", "second");
                return getDataFromApi();

            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         *
         * @return List of names and majors
         * @throws IOException
         */
        private Spreadsheet getDataFromApi() throws IOException {

            Spreadsheet requestBody = new Spreadsheet();
            SpreadsheetProperties properties = new SpreadsheetProperties();
            properties.setTitle("event Name");
            requestBody.setProperties(properties);

            Sheets.Spreadsheets.Create request = mService.spreadsheets().create(requestBody);
            Log.e("test", "last");
            return request.execute();
        }

    }
}
