package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.getsetgo.Adapter.ActiveCourseAdapter;
import com.getsetgo.Adapter.MyCourseAdapter;
import com.getsetgo.R;

public class MyCourseActivity extends AppCompatActivity {
    Context context;
    public RecyclerView recyclerViewCourse;
    MyCourseAdapter myCourseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);
        context = MyCourseActivity.this;
        init();
    }
    private void init(){
        recyclerViewCourse = findViewById(R.id.recyclerViewCourse);
        setupRecyclerViewForMyCourse();
    }
    private void setupRecyclerViewForMyCourse() {
        recyclerViewCourse.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myCourseAdapter = new MyCourseAdapter(this);
        recyclerViewCourse.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCourse.setAdapter(myCourseAdapter);
        myCourseAdapter.notifyDataSetChanged();
    }
}