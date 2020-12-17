package com.getsetgo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.R;
import com.getsetgo.activity.ChatScreenActivity;
import com.getsetgo.databinding.FragmentInboxOutboxBinding;
import com.getsetgo.databinding.LayoutInboxOutboxRowBinding;
import com.makeramen.roundedimageview.RoundedImageView;

public class InboxOutboxAdapter extends RecyclerView.Adapter<InboxOutboxAdapter.InboxOutViewHolder> {

    LayoutInboxOutboxRowBinding binding;
    Context context;

    public InboxOutboxAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public InboxOutboxAdapter.InboxOutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.layout_inbox_outbox_row,parent,false);
        return new InboxOutViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull InboxOutboxAdapter.InboxOutViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatScreenActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class InboxOutViewHolder extends RecyclerView.ViewHolder {


        public InboxOutViewHolder(View view) {
            super(view);


        }
    }
}

