package com.getsetgo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.getsetgo.Adapter.CategoriesCommonAdapter;
import com.getsetgo.Adapter.CurriculumLectureAdapter;
import com.getsetgo.Adapter.StudentsFeedbackAdapter;
import com.getsetgo.R;
import com.getsetgo.util.BulletTextUtil;

import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {

    TextView txtCourseTitle, txtCourseIncludes, txtLearn, txtViewMore,txtMoreSections;
    ImageView icCourseImage;
    ReadMoreTextView readMoreTextView;
    Context context;
    RecyclerView recyclerLecture,recyclerViewFeedback;
    CurriculumLectureAdapter curriculumLectureAdapter;
    StudentsFeedbackAdapter studentsFeedbackAdapter;
    LinearLayoutManager layoutManager;
    LinearLayoutManager mLayoutManagerStudentFeedback;

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
        txtMoreSections = findViewById(R.id.txtMoreSections);
        recyclerLecture = findViewById(R.id.recyclerViewLecture);
        recyclerViewFeedback = findViewById(R.id.recyclerViewFeedback);
        icCourseImage = findViewById(R.id.ivCourseImage);

        readMoreTextView.setText(R.string.dummy_text);

        CharSequence bulletedList = BulletTextUtil.makeBulletList(10, "First line", "Second line", "third line", "First line", "Second line", "third line");
        txtCourseIncludes.setText(bulletedList);


        CharSequence bulletedList2 = BulletTextUtil.makeBulletList(10, "First line", "Second line", "third line", "First line", "Second line", "third line");
        txtLearn.setText(bulletedList2);

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mLayoutManagerStudentFeedback = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);


        txtViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setupRecyclerViewCurriculumLecture();
        setupRecyclerViewStudenrFeedback();

        txtMoreSections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch(5);
            }
        });
    }

    private void setupRecyclerViewCurriculumLecture() {
        recyclerLecture.setLayoutManager(layoutManager);
        curriculumLectureAdapter = new CurriculumLectureAdapter(this,5);
        recyclerLecture.setItemAnimator(new DefaultItemAnimator());
        recyclerLecture.setAdapter(curriculumLectureAdapter);
        curriculumLectureAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewStudenrFeedback() {
        recyclerViewFeedback.setLayoutManager(mLayoutManagerStudentFeedback);
        studentsFeedbackAdapter = new StudentsFeedbackAdapter(this);
        recyclerViewFeedback.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFeedback.setAdapter(studentsFeedbackAdapter);
        studentsFeedbackAdapter.notifyDataSetChanged();
    }

    void fetch(int count){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                curriculumLectureAdapter = new CurriculumLectureAdapter(context,count);
                recyclerLecture.setItemAnimator(new DefaultItemAnimator());
                recyclerLecture.setAdapter(curriculumLectureAdapter);
            }
        },3000);
    }
}