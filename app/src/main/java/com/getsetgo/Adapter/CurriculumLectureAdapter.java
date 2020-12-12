package com.getsetgo.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.getsetgo.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class CurriculumLectureAdapter extends RecyclerView.Adapter<CurriculumLectureAdapter.CurriculumLecturViewHolder> {

    Context context;
    int Count;
    Animation animUp,animDown;

    public CurriculumLectureAdapter(Context context, int count) {
        this.context = context;
        this.Count = count;

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

        holder.chkExpClp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.rlCollapse.setVisibility(View.VISIBLE);
                    holder.rlCollapse.startAnimation(animDown);
                    holder.recyclerCollapsed.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                    LecturePreviewAdapter lecturePreviewAdapter = new LecturePreviewAdapter(context);
                    holder.recyclerCollapsed.setItemAnimator(new DefaultItemAnimator());
                    holder.recyclerCollapsed.setAdapter(lecturePreviewAdapter);
                    lecturePreviewAdapter.notifyDataSetChanged();
                } else {
                    holder.rlCollapse.startAnimation(animUp);
                    holder.rlCollapse.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return Count;
    }

    public class CurriculumLecturViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        CheckBox chkExpClp;
        RelativeLayout rlCollapse;
        RecyclerView recyclerCollapsed;


        public CurriculumLecturViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            chkExpClp = itemView.findViewById(R.id.chkExpClp);
            rlCollapse = itemView.findViewById(R.id.rlCollapse);
            recyclerCollapsed = itemView.findViewById(R.id.recyclerCollapsed);

        }
    }

}

