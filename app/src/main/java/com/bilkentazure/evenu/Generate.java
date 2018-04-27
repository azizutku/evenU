package com.bilkentazure.evenu;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

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
    private int security_check;
    final private String eventID = "event1"; // Shall be retrieved from calling intent in evenU club class
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

        doActions();
        //Start a timer with given interval and delay
        generationTimer = new CountDownTimer(5000,10) {

            @Override
            public void onTick(long millisUntilFinished) {}

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
            generatedQR = generateQR(security_check + ":" + eventID, 500,500);
            Drawable d = new BitmapDrawable(getResources(), generatedQR);
            viewQR.setImageDrawable(d);
            Toast.makeText( Generate.this , "QR Updated!", Toast.LENGTH_LONG).show();

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
     * @param text is the String to be generated
     * @param width is width
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
                pixels[offset + x] = bitMatrix.get( x , y) ? colorBlack : colorWhite;
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
        security_check = (int) (Math.random() * 1000000);
        db.collection("events").document(eventID).update("security_check", security_check);
    }

}
