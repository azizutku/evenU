package com.bilkentazure.evenu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.bilkentazure.evenu.models.Event;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Zeyad Khaled on 1/5/2018.
 * Event view activity that is opened using an intent with an event object.
 * Event object is then used to display relevant information.
 * @author Zeyad Khaled
 * @version 1/5/2018
 */

public class EventView extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView txtTitle;
    private TextView txtInfo;
    private TextView txtDate;
    private TextView txtLocation;
    private TextView txtGE;
    private TextView txtClub;
    private Button going;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        Event event = (Event)getIntent().getSerializableExtra("event");

        mToolbar = findViewById(R.id.event_view_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(event.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTitle = findViewById(R.id.event_view_txt_title);
        txtInfo =findViewById(R.id.event_view_txt_info);
        txtDate = findViewById(R.id.event_view_txt_date);
        txtLocation = findViewById(R.id.event_view_txt_location);
        txtGE = findViewById(R.id.event_view_txt_ge);
        txtClub = findViewById(R.id.event_view_txt_club);

        txtTitle.setText(event.getName());
        txtInfo.setText(event.getDescription());
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy\n'Time: ' H:m");
		String date = dateFormat.format(event.getFrom());
        txtDate.setText(date);
        txtLocation.setText(event.getLocation());
        txtGE.setText(event.getGe_point() + " Points");
        txtClub.setText(event.getClub_id());


        going = findViewById(R.id.going_event);
        going.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Process going here and save to database

                //Return back to home activity
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                Toast.makeText(getApplicationContext(), "Going to event is successful!",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}
