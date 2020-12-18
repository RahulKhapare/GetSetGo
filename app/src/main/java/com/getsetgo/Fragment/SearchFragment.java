package com.getsetgo.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.getsetgo.R;
import com.getsetgo.databinding.FragmentComposeBinding;
import com.getsetgo.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    TextView t[];
    TextView tC[];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        View rootView = binding.getRoot();

        dynamicTextView();


        return rootView;
    }

    private void dynamicTextView() {

        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.nunito_sans_regular);
        LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dim.setMargins(0, 20, 0, 20);

        String[] arr = getActivity().getResources().getStringArray(R.array.listArray);
        t = new TextView[arr.length];
        for (int i = 0; i < arr.length; i++) {
            t[i] = new TextView(getActivity());
            t[i].setLayoutParams(dim);
            t[i].setText("\u25CF   " + arr[i]);
            t[i].setTextSize(14);
            t[i].setTypeface(typeface);
            t[i].setTextColor(getActivity().getResources().getColor(R.color.colorTextHint60));
            binding.llDynamicSearch.addView(t[i]);
        }


        LinearLayout.LayoutParams llm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llm.setMargins(0, 20, 0, 20);

        String[] arrCat = getActivity().getResources().getStringArray(R.array.categoryArray);

        tC = new TextView[arrCat.length];

        for (int i = 0; i < arrCat.length; i++) {
            tC[i] = new TextView(getActivity());
            tC[i].setLayoutParams(dim);
            tC[i].setText(arrCat[i]);
            tC[i].setTextSize(14);
            tC[i].setTypeface(typeface);
            tC[i].setTextColor(getActivity().getResources().getColor(R.color.colorTextHint60));
            binding.llDynamicCategory.addView(tC[i]);
            int finalI = i;
            tC[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), arrCat[finalI], Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}