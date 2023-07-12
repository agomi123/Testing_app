package com.example.testingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter3 extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter3(Context context) {
        this.context = context;
    }

    int images[] = {
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p2,
            R.drawable.p3,
            R.drawable.p5,
            R.drawable.p4,
    };
    String headings[] = {
            "Activate display over other App",
            "Activate App supervision",
            "Activate Notification Access",
            "Activate Device Administrator Permission",
            "Active Run in Background",
            "Active Location service"

    };
    String descriptions[] = {
            "Reference site about lorem ipsum,giving info",
            "Reference site about lorem ipsum,giving info",
            "Reference site about lorem ipsum,giving info",
            "Reference site about lorem ipsum,giving info",
            "Reference site about lorem ipsum,giving info",
            "Reference site about lorem ipsum,giving info",
    };
    String descp2[] = {
            "on its origins, as well as a random Lipsum generator",
            "on its origins, as well as a random Lipsum generator",
            "on its origins, as well as a random Lipsum generator",
            "on its origins, as well as a random Lipsum generator",
            "on its origins, as well as a random Lipsum generator",
            "on its origins, as well as a random Lipsum generator",
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout3, container, false);

        ImageView imageView = view.findViewById(R.id.imgx);
        TextView heading = view.findViewById(R.id.headingx);
        TextView desc = view.findViewById(R.id.descx);
        TextView desc2 = view.findViewById(R.id.descx2);
        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(descriptions[position]);
        desc2.setText(descp2[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
