package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.getsetgo.Adapter.CategoriesCommonAdapter;
import com.getsetgo.Adapter.CurriculumLectureAdapter;
import com.getsetgo.R;
import com.getsetgo.util.BulletTextUtil;

import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {

    TextView txtCourseTitle, txtCourseIncludes, txtLearn, txtViewMore;
    ImageView icCourseImage;
    ReadMoreTextView readMoreTextView;
    Context context;
    RecyclerView recyclerLecture;
    CurriculumLectureAdapter curriculumLectureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        context = CourseDetailsActivity.this;
        init();
    }

    private void init() {
        txtCourseTitle = findViewById(R.id.txtCourseTitle);
        txtCourseIncludes = findViewById(R.id.txtCourseIncludes);
        txtLearn = findViewById(R.id.txtLearn);
        txtViewMore = findViewById(R.id.txtViewMore);
        readMoreTextView = findViewById(R.id.txtDesc);
        recyclerLecture = findViewById(R.id.recyclerViewLecture);
        icCourseImage = findViewById(R.id.ivCourseImage);

        readMoreTextView.setText(R.string.dummy_text);

        CharSequence bulletedList = BulletTextUtil.makeBulletList(10, "First line", "Second line", "third line", "First line", "Second line", "third line");
        txtCourseIncludes.setText(bulletedList);


        CharSequence bulletedList2 = BulletTextUtil.makeBulletList(10, "First line", "Second line", "third line", "First line", "Second line", "third line");
        txtLearn.setText(bulletedList2);


        txtViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setupRecyclerViewCurriculumLecture();
    }

    private void setupRecyclerViewCurriculumLecture() {
        recyclerLecture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        curriculumLectureAdapter = new CurriculumLectureAdapter(this);
        recyclerLecture.setItemAnimator(new DefaultItemAnimator());
        recyclerLecture.setAdapter(curriculumLectureAdapter);
        curriculumLectureAdapter.notifyDataSetChanged();
    }
}