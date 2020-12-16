package com.getsetgo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.getsetgo.R;
import com.getsetgo.databinding.ActivitySearchUserBinding;
import com.getsetgo.databinding.ActivitySearchUseridBinding;
import com.getsetgo.util.WindowView;

import java.util.ArrayList;

public class SearchUserIdActivity extends AppCompatActivity {

    private SearchUserIdActivity activity = this;
    private ActivitySearchUseridBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_userid);
        init();
    }

    private void init() {
    }

}