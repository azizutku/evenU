package com.bilkentazure.evenu;

/**
 * Created by Zeyad Khaled on 4/17/2018.
 */
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class AboutActivity extends AppCompatActivity {

    //Properties
    private Toolbar mToolbar;

    //Code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mToolbar = findViewById(R.id.about_toolbar);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("About page");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("evenU brings you all Bilkent events in a stylish and accessible way!")
                .setImage(R.drawable.about_logo)
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Made with love by")
                .addItem(new Element().setTitle("Aysegul ♥ Zeyad ♥ Utku ♥ Rana ♥ Endri ♥"))
                .addGroup("Connect with us")
                .addEmail("bilkentazure@gmail.com")
                .addWebsite("https://github.com/AzizUtku/evenU/")
                .addTwitter("zeyadk99")
                .addPlayStore("com.ideashower.readitlater.pro")
                .addGitHub("https://github.com/AzizUtku/evenU")
                .create();

        setContentView(aboutPage);
    }
}
