package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgo.Fragment.ChatScreenFragment;
import com.getsetgo.R;
import com.getsetgo.databinding.LayoutInboxOutboxRowBinding;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxOutViewHolder> {

    LayoutInboxOutboxRowBinding binding;
    Context context;
    ChatScreenFragment chatScreenFragment;
    JsonList jsonList;

    public InboxAdapter(Context context,JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;

    }

    @NonNull
    @Override
    public InboxAdapter.InboxOutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.layout_inbox_outbox_row,parent,false);
        return new InboxOutViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull InboxAdapter.InboxOutViewHolder holder, int position) {

        Json json = jsonList.get(position);

        holder.txtTitle.setText(json.getString("subject"));
        holder.txtMessage.setText(json.getString("message"));
        holder.txtDate.setText(json.getString("create_date"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTotalFragment(v);
            }
        });

    }

    @Override
    public int getItemCount() {
        return jsonList.size();
    }

    public class InboxOutViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle,txtMessage,txtDate;

        public InboxOutViewHolder(View view) {
            super(view);

            txtTitle = itemView.findViewById(R.id.txtSupportTitle);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtMessage = itemView.findViewById(R.id.txtMessage);
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

