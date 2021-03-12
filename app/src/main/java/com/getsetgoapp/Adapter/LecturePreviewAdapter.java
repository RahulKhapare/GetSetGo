package com.getsetgoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgoapp.R;

public class LecturePreviewAdapter extends RecyclerView.Adapter<LecturePreviewAdapter.LecturePreviewViewHolder> {

    Context context;


    public LecturePreviewAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public LecturePreviewAdapter.LecturePreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_course_list, parent, false);
        return new LecturePreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LecturePreviewAdapter.LecturePreviewViewHolder holder, int position) {

        holder.txtPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*Json json = new Json();
                String string = P.baseUrl + "series_check/" + json.getString(P.series_slug) + "/" + json.getString(P.video_slug);
                int i = json.getInt(P.time);
                i *= 1000;

                App.app.startVideoActivity(context, string, i);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class LecturePreviewViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle,txtCount,txtPreview,txtVideoDetails;


        public LecturePreviewViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtCount = itemView.findViewById(R.id.txtCount);
            txtPreview = itemView.findViewById(R.id.txtPreview);
            txtVideoDetails = itemView.findViewById(R.id.txtVideoDetails);


        }
    }

}

