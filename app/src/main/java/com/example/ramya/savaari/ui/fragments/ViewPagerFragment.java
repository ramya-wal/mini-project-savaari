package com.example.ramya.savaari.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.adapters.SlidingImageAdapter;
import com.example.ramya.savaari.util.Constants;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class ViewPagerFragment extends Fragment {

    private  ViewPager vpVehicleImages;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    TextView textView;

//    private static final Integer[] IMAGES = {R.drawable.car1, R.drawable.car2, R.drawable.car3};
    private ArrayList<String> ImagesArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

//        Collections.addAll(ImagesArray, IMAGES);

        ImagesArray = getArguments().getStringArrayList(Constants.KEY_VEHICLE_OBJECT);
        textView = view.findViewById(R.id.textview);

        vpVehicleImages = view.findViewById(R.id.fragment_view_pager_vpVehicleImages);
        final PagerAdapter adapter = new SlidingImageAdapter(getContext(),ImagesArray);
        vpVehicleImages.setAdapter(adapter);


        final PageIndicatorView indicator =  view.findViewById(R.id.fragment_view_pager_pageIndicator);

        indicator.setViewPager(vpVehicleImages);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        assert ImagesArray != null;
        NUM_PAGES =ImagesArray.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                vpVehicleImages.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        vpVehicleImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                indicator.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });

        return view;
    }

}
