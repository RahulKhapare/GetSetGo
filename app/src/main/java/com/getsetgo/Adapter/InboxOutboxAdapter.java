package com.getsetgo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Fragment.ChatScreenFragment;
import com.getsetgo.Fragment.TotalUsersFragment;
import com.getsetgo.R;
import com.getsetgo.activity.ChatScreenActivity;
import com.getsetgo.databinding.FragmentInboxOutboxBinding;
import com.getsetgo.databinding.LayoutInboxOutboxRowBinding;
import com.makeramen.roundedimageview.RoundedImageView;

public class InboxOutboxAdapter extends RecyclerView.Adapter<InboxOutboxAdapter.InboxOutViewHolder> {

    LayoutInboxOutboxRowBinding binding;
    Context context;
    ChatScreenFragment chatScreenFragment;

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
                loadTotalFragment(v);
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

    private void loadTotalFragment(View v){

        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        chatScreenFragment = new ChatScreenFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, chatScreenFragment)
                .addToBackStack(null)
                .commit();

    }
}

