package com.getsetgoapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgoapp.Model.CommentModel;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivityCommentListBgBinding;

import java.util.List;
import java.util.Random;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context context;
    List<CommentModel> commentModelList;

    public CommentAdapter(Context context, List<CommentModel> commentModelList) {
        this.context = context;
        this.commentModelList = commentModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCommentListBgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_comment_list_bg, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentModel model = commentModelList.get(position);

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        GradientDrawable tvBackground = (GradientDrawable) holder.binding.txtName.getBackground();
        tvBackground.setColor(color);

        holder.binding.txtFullName.setText(model.getName() + " "+ model.getLastname());
        holder.binding.txtDate.setText(model.getAdd_date());
        holder.binding.txtName.setText(model.getInitials());
        holder.binding.txtComment.setText(model.getComment());

    }

    @Override
    public int getItemCount() {
        return commentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ActivityCommentListBgBinding binding;

        public ViewHolder(@NonNull ActivityCommentListBgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}