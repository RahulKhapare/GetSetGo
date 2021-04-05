package com.getsetgoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.getsetgoapp.Model.CountryCodeModel;
import com.getsetgoapp.R;

import java.util.List;

public class CountryCodeSelectionAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflter;
    private List<CountryCodeModel> itemListModels;

    public CountryCodeSelectionAdapter(Context context, List<CountryCodeModel> itemListModelList) {
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
        view = inflter.inflate(R.layout.activity_countr_code_bg, null);
        TextView txtName = view.findViewById(R.id.txtCode);
        CountryCodeModel model = itemListModels.get(i);
        txtName.setText("+"+model.getCountry_code());
        return view;
    }

}