package com.bilkentazure.evenu;

/**
 * Created by Zeyad Khaled on 4/17/2018.
 */
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import android.widget.RelativeLayout;

public class AboutActivity extends AppCompatActivity {

    //Properties
    private Toolbar mToolbar;

    String[] names = {
    		"-Ayşegül Gökçe",
			"-Aziz Utku Kağıtcı",
			"-Endri Suknaj",
			"-Rana Elbatanony",
			"-Zeyad Khaled"
	};

    String[] mails = {
			"aysegul.gokce@ug.bilkent.edu.tr",
			"azizutku@gmail.com",
			"endrisuknaj@icloud.com",
			"rmelbatanony002@gmail.com",
			"monosec15@gmail.com"
	};

    private Element[] devs = new Element[5];

    //Code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		mToolbar = findViewById(R.id.about_toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("About");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		RelativeLayout rlt = findViewById(R.id.about_rlt_content);

		setMailtoDev();

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("evenU brings you all Bilkent events in a stylish and accessible way!")
                .setImage(R.drawable.about_logo)
                .addGroup("Made with by")
                .addItem(devs[0])
				.addItem(devs[1])
				.addItem(devs[2])
				.addItem(devs[3])
				.addItem(devs[4])
                .addGroup("Connect with us")
                .addEmail("bilkentazure@gmail.com")
                .addGitHub("AzizUtku/evenU/")
                .addPlayStore("com.bilkentazure.evenu")
				.addItem(new Element().setTitle("Version 1.0"))
                .create();

		rlt.addView(aboutPage);


    }

	private void setMailtoDev(){
    	for(int i = 0; i < names.length; i++){
    		devs[i] = new Element();
    		devs[i].setTitle(names[i]);
			final int finalI = i;
			devs[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mails[finalI]));
					emailIntent.putExtra(Intent.EXTRA_SUBJECT, "evenU");
					emailIntent.putExtra(Intent.EXTRA_TEXT, "");
					startActivity(Intent.createChooser(emailIntent, "Select one of them"));
				}
			});
		}
	}
}
