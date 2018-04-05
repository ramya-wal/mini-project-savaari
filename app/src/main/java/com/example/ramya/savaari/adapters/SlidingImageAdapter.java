package com.example.ramya.savaari.adapters;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ramya.savaari.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SlidingImageAdapter extends PagerAdapter implements Parcelable {
    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapter(Context context, ArrayList<String> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout
                .findViewById(R.id.image);


        Picasso.with(context).load(IMAGES.get(position)).into(imageView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.IMAGES);
        dest.writeParcelable((Parcelable) this.inflater, flags);
        dest.writeParcelable((Parcelable) this.context, flags);
    }

    private SlidingImageAdapter(Parcel in) {
        this.IMAGES = new ArrayList<>();
        in.readList(this.IMAGES, Integer.class.getClassLoader());
        this.inflater = in.readParcelable(LayoutInflater.class.getClassLoader());
        this.context = in.readParcelable(Context.class.getClassLoader());
    }

    public static final Parcelable.Creator<SlidingImageAdapter> CREATOR = new Parcelable.Creator<SlidingImageAdapter>() {
        @Override
        public SlidingImageAdapter createFromParcel(Parcel source) {
            return new SlidingImageAdapter(source);
        }

        @Override
        public SlidingImageAdapter[] newArray(int size) {
            return new SlidingImageAdapter[size];
        }
    };
}
