package com.getsetgo.adapterview;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.getsetgo.Model.SliderModel;
import com.getsetgo.R;
import com.getsetgo.util.Click;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SliderImageAdapter extends PagerAdapter {


    private List<SliderModel> imageModelList;
    private LayoutInflater inflater;
    private Context context;


    public SliderImageAdapter(Context context, List<SliderModel> imageModelList) {
        this.context = context;
        this.imageModelList = imageModelList;
        inflater = LayoutInflater.from(context);
        notifyDataSetChanged();
    }

    public void addData(@NonNull List<SliderModel> data) {
        imageModelList.clear();
        imageModelList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.activity_slider_view, view, false);
        assert imageLayout != null;
        final ImageView imageView = imageLayout
                .findViewById(R.id.imgView);
        SliderModel model = imageModelList.get(position);
        Picasso.get().load(model.getBanner_image()).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        view.addView(imageLayout, 0);

        return imageLayout;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}