package com.getsetgoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.getsetgoapp.Model.SpinnerModel;
import com.getsetgoapp.R;

import java.util.List;


public class SpinnerSelectionAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflter;
    private List<SpinnerModel> itemListModels;

    public SpinnerSelectionAdapter(Context context, List<SpinnerModel> itemListModelList) {
        this.context = context;
        this.itemListModels = itemListModelList;
        inflter = (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return itemListModels.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_spinner_bg, null);
        TextView txtName = view.findViewById(R.id.txtName);
        SpinnerModel model = itemListModels.get(i);
        txtName.setText(model.getName());
        if (i==0){
            txtName.setTextColor(context.getResources().getColor(R.color.colorTextHint60));
        }else {
            txtName.setTextColor(context.getResources().getColor(R.color.colorTextDark));
        }
        return view;
    }

}