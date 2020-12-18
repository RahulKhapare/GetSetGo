package com.getsetgo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.getsetgo.Adapter.CategoriesCommonAdapter;
import com.getsetgo.Adapter.CurriculumLectureAdapter;
import com.getsetgo.Adapter.StudentsFeedbackAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityCourseDetailsBinding;
import com.getsetgo.util.BulletTextUtil;
import com.getsetgo.util.WindowView;

import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {

    CurriculumLectureAdapter curriculumLectureAdapter;
    StudentsFeedbackAdapter studentsFeedbackAdapter;
    LinearLayoutManager layoutManager;
    LinearLayoutManager mLayoutManagerStudentFeedback;

    TextView t[];
    TextView tC[];
    private CourseDetailsActivity activity = this;
    ActivityCourseDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_details);
        init();
    }

    private void init() {

        binding.txtDesc.setText(R.string.dummy_text);

        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        mLayoutManagerStudentFeedback = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);


        binding.txtViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setupRecyclerViewCurriculumLecture();
        setupRecyclerViewStudenrFeedback();

        binding.txtMoreSections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch(5);
            }
        });
        dynamicTextView();
    }

    private void dynamicTextView(){
        Typeface typeface = ResourcesCompat.getFont(activity, R.font.nunito_sans_regular);
        LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String[] arr = activity.getResources().getStringArray(R.array.listArray);
        t = new TextView[arr.length];
        for (int i = 0; i < arr.length; i++) {
            t[i] = new TextView(activity);
            t[i].setLayoutParams(dim);
            t[i].setText("\u25CF   " + arr[i]);
            t[i].setTextSize(14);
            t[i].setTypeface(typeface);
            t[i].setTextColor(activity.getResources().getColor(R.color.colorTextDark));
            binding.llCourseIncludes.addView(t[i]);
        }


        LinearLayout.LayoutParams llm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        String[] arrCat = activity.getResources().getStringArray(R.array.categoryArray);
        tC = new TextView[arrCat.length];
        textViewMore(llm, typeface, arrCat, arrCat.length);
    }

    private void textViewMore(LinearLayout.LayoutParams dim, Typeface typeface, String[] arrCat, int length) {
        for (int i = 0; i < length; i++) {
            tC[i] = new TextView(activity);
            tC[i].setLayoutParams(dim);
            tC[i].setText("\u25CF   " + arrCat[i]);
            tC[i].setTextSize(14);
            tC[i].setTypeface(typeface);
            tC[i].setTextColor(activity.getResources().getColor(R.color.colorTextDark));
            binding.llLearn.addView(tC[i]);
            int finalI = i;
            tC[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity, arrCat[finalI], Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupRecyclerViewCurriculumLecture() {
        binding.recyclerViewLecture.setLayoutManager(layoutManager);
        curriculumLectureAdapter = new CurriculumLectureAdapter(this, 5);
        binding.recyclerViewLecture.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewLecture.setAdapter(curriculumLectureAdapter);
        curriculumLectureAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewStudenrFeedback() {
        binding.recyclerViewFeedback.setLayoutManager(mLayoutManagerStudentFeedback);
        studentsFeedbackAdapter = new StudentsFeedbackAdapter(this);
        binding.recyclerViewFeedback.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewFeedback.setAdapter(studentsFeedbackAdapter);
        studentsFeedbackAdapter.notifyDataSetChanged();
    }

    void fetch(int count) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                curriculumLectureAdapter = new CurriculumLectureAdapter(activity, count);
                binding.recyclerViewLecture.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerViewLecture.setAdapter(curriculumLectureAdapter);
            }
        }, 3000);
    }
}