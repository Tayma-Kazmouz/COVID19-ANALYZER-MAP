package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;

public class ViewPagerAdapter2 extends PagerAdapter {

    Context context;

    int images[] = {

            R.raw.vactest,
            R.raw.stay_at_home,
            R.raw.wash_hands,
            R.raw.use_a_mask,
            R.raw.use_hand_sanitizer,
            R.raw.touch_face,
            R.raw.avoid_contacts,
            R.raw.avoid_crowd_places,
            R.raw.socialdistance


    };

    int headings[] = {

            R.string.pre_one,
            R.string.pre_two,
            R.string.pre_third,
            R.string.pre_fourth,
            R.string.pre_fifth,
            R.string.pre_sixth,
            R.string.pre_seventh,
            R.string.pre_eight,
            R.string.pre_nine
    };

    int description[] = {

            R.string.predesc_one,
            R.string.predesc_two,
            R.string.predesc_third,
            R.string.predesc_fourth,
            R.string.predesc_fifth,
            R.string.predesc_sixth,
            R.string.predesc_seventh,
            R.string.predesc_eight,
            R.string.predesc_nine
    };

    public ViewPagerAdapter2(Context context){

        this.context = context;

    }

    @Override
    public int getCount() {
        return  headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ScrollView) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        LottieAnimationView slidetitleimage = (LottieAnimationView) view.findViewById(R.id.covid_id);
        TextView slideHeading = (TextView) view.findViewById(R.id.texttitle);
        TextView slideDesciption = (TextView) view.findViewById(R.id.textdeccription);

        slidetitleimage.setAnimation(images[position]);
        slideHeading.setText(headings[position]);
        slideDesciption.setText(description[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ScrollView)object);

    }
}