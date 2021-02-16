package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class CurriculumLectureAdapter extends RecyclerView.Adapter<CurriculumLectureAdapter.CurriculumLecturViewHolder> {

    Context context;
    JsonList module;
    Animation animUp, animDown;

    public CurriculumLectureAdapter(Context context, JsonList module) {
        this.context = context;
        this.module = module;

        animUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        animDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

    }

    @NonNull
    @Override
    public CurriculumLectureAdapter.CurriculumLecturViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_lecture, parent, false);
        return new CurriculumLecturViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurriculumLectureAdapter.CurriculumLecturViewHolder holder, int position) {

        final Json json = module.get(position);
        try {


//            JSONObject jsonObject = json.getJsonObject("course_videos");
            JSONObject element = null;
            Iterator<?> keys = json.keys();

            while (keys.hasNext()) {

                String key = (String) keys.next();
                if (json.get(key) instanceof JSONObject) {

                }
                JSONArray arr = json.getJSONArray(key);

                for (int i = 0; i < arr.length(); i++) {
                    element = arr.getJSONObject(i);
                    holder.txtLectureTitle.setText(element.getString("video_name"));
                    holder.txtVideoDetails.setText("Duration: " + element.getString("video_duration"));
                }
            }


            holder.txtTitle.setText(json.names().getString(position));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.chkExpClp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    holder.llCollapse.setVisibility(View.VISIBLE);
                    holder.llCollapse.startAnimation(animDown);


//                    holder.recyclerCollapsed.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//                    LecturePreviewAdapter lecturePreviewAdapter = new LecturePreviewAdapter(context);
//                    holder.recyclerCollapsed.setItemAnimator(new DefaultItemAnimator());
//                    holder.recyclerCollapsed.setAdapter(lecturePreviewAdapter);
//                    lecturePreviewAdapter.notifyDataSetChanged();
                } else {
                    holder.llCollapse.startAnimation(animUp);
                    holder.llCollapse.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return module.size();
    }

    public class CurriculumLecturViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtLectureTitle, txtVideoDetails, txtPreview;
        CheckBox chkExpClp;
        RelativeLayout rlCollapse;
        LinearLayout llCollapse;
        RecyclerView recyclerCollapsed;


        public CurriculumLecturViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtLectureTitle = itemView.findViewById(R.id.txtLectureTitle);
            txtVideoDetails = itemView.findViewById(R.id.txtVideoDetails);
            txtPreview = itemView.findViewById(R.id.txtPreview);
            chkExpClp = itemView.findViewById(R.id.chkExpClp);
            llCollapse = itemView.findViewById(R.id.llCollapse);
            recyclerCollapsed = itemView.findViewById(R.id.recyclerCollapsed);

        }
    }

}

