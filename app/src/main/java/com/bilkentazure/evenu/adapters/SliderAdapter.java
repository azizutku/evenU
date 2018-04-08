package com.bilkentazure.evenu.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bilkentazure.evenu.R;

/**
 * Created by azizutku on 20.03.2018.
 */
public class SliderAdapter extends PagerAdapter {

	//properties
	Context context;
	LayoutInflater layoutInflater;

	//constructor
	public SliderAdapter(Context context){

		this.context = context;

	}

	//Arrays
	public String[] slide_headings = {
			"Welcome",
			"GE Points",
			"List events"
	};

	public String[] slide_descriptions = {
			"To our event organizing application.",
			"Keep track of your GE points",
			"View events in an organized way"
	};

	public int[] slide_images = {
			R.drawable.onboarding_hand,
			R.drawable.onboarding_progress,
			R.drawable.onboarding_list
	};


	@Override
	public int getCount() {
		return slide_headings.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (RelativeLayout)object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

		ImageView slideImage = view.findViewById(R.id.slide_image);
		TextView slideHeading = view.findViewById(R.id.slide_heading);
		TextView slideDescription = view.findViewById(R.id.slide_description);

		slideImage.setImageResource(slide_images[position]);
		slideHeading.setText(slide_headings[position]);
		slideDescription.setText(slide_descriptions[position]);

		container.addView(view);


		return view;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((RelativeLayout)object);
	}
}
