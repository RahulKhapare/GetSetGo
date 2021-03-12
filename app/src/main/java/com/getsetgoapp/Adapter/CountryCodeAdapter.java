package com.getsetgoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getsetgoapp.Model.CountryCode;
import com.getsetgoapp.R;

import java.util.ArrayList;
import java.util.List;


public class CountryCodeAdapter extends ArrayAdapter<CountryCode> {
    Context context;
    int resource, textViewResourceId;
    ArrayList<CountryCode> items, tempItems, suggestions;

    public CountryCodeAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<CountryCode> objects) {
        super(context, resource, textViewResourceId);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = objects;
        tempItems = (ArrayList<CountryCode>) objects.clone();// this makes the difference.
        suggestions = new ArrayList<CountryCode>();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_auto_txt, parent, false);
            }
            CountryCode country = suggestions.get(position);
            if (country != null) {
                TextView tvCode = (TextView) view.findViewById(R.id.lbl_name);
                tvCode.setText(country.getCode());
            }

            view.setTag(country);

        }catch (Exception e){
            e.printStackTrace();
        }

        return view;

    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((CountryCode) resultValue).getDialCode();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CountryCode country : tempItems) {
                    String temp = country.getCode();
                    if (temp.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        if(suggestions.size() <= 9){
                            suggestions.add(country);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<CountryCode> filterList = (ArrayList<CountryCode>) results.values;
            try {
                if (results != null && results.count > 0) {
                    clear();
                    for (CountryCode country : filterList) {
                        add(country);
                        notifyDataSetChanged();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}
