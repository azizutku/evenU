package com.bilkentazure.evenu;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bilkentazure.evenu.adapters.SliderAdapter;


/**
 * Created by azizutku on 20.03.2018.
 */
public class MainActivity extends AppCompatActivity {

	private ViewPager mViewPager;
	private LinearLayout mLinear;
	private SliderAdapter mSliderAdapter;
	private TextView[] mTxtDots;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		Button btnLogin = findViewById(R.id.main_btn_sign);
		TextView txtForgot = findViewById(R.id.main_txt_forgot_password);
		TextView txtRegister = findViewById(R.id.main_txt_register);

		txtRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});

		txtForgot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ForgotPassActivity.class);
				startActivity(intent);
			}
		});

		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});

		mViewPager = findViewById(R.id.main_viewpager_slide);
		mLinear = findViewById(R.id.main_lnr_dots);

		mSliderAdapter = new SliderAdapter(this);
		mViewPager.setAdapter(mSliderAdapter);

		addDotsIndicator(0);

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				addDotsIndicator(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

	}

	public void addDotsIndicator(int position){

		mTxtDots = new TextView[mSliderAdapter.getCount()];
		mLinear.removeAllViews();

		for( int i = 0; i < mTxtDots.length; i++){

			mTxtDots[i] = new TextView( this);
			mTxtDots[i].setText("•");
			mTxtDots[i].setTextSize(35);

			if(i == position){
				mTxtDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
				mTxtDots[position].setTextSize(45);
			} else {
				mTxtDots[i].setTextColor(getResources().getColor(R.color.colorUnselectedDot));
			}

			mLinear.addView(mTxtDots[i]);

		}

	}


}
