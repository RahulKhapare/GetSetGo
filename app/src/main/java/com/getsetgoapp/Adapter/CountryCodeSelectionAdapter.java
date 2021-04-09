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
    private int status = 0;

    public CountryCodeSelectionAdapter(Context context, List<CountryCodeModel> itemListModelList,int status) {
        this.context = context;
        this.itemListModels = itemListModelList;
        this.status = status;
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
        if (i == 0){
            txtName.setText(model.getCountry_shortname());
            txtName.setTextColor(context.getResources().getColor(R.color.colorTextHint));
        }else {
            if (status==1){
                txtName.setText("(+"+model.getCountry_code().trim()+") " + model.getCountry_name().trim());
            }else if (status==2){
                txtName.setText(model.getCountry_shortname());
            }

        }

        return view;
    }

}