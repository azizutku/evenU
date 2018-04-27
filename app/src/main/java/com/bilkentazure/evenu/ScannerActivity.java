package com.bilkentazure.evenu;
/**
 * Created by Zeyad Khaled on 27/4/2018.
 * This is QR scanning class where QR data is being decoded and sent to the ScannerFragment
 * for processing.
 * @author Zeyad Khaled
 * @version 27/4/2018
 */

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class ScannerActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private Toolbar mToolbar;
    private TextView qrResultView;
    private QRCodeReaderView qrCodeReaderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        displayTutorial();

        mToolbar = findViewById(R.id.qr_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Scan QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        qrCodeReaderView = findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setBackCamera();

    }

    /**
     * Second decoded QR result to another Class
     * @param text the retrieved information after decoding
     * @param points the virtual 3D points found while decoding
     */
    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        onPause(); // Stop camera!
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", text);
        setResult(RESULT_OK, returnIntent);
        finish();

    }

    /**
     * Return Cancel code if back button is pressed and show scan fragment
     */
    public boolean onOptionsItemSelected(MenuItem item){
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    /**
     * Display a 5 second instruction about scanning QR
     */
    public void displayTutorial() {
        qrResultView = findViewById(R.id.qr_result);

        CountDownTimer timer = new CountDownTimer(4000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                qrResultView.setVisibility(View.INVISIBLE);
            }
        }.start();
    }
}
