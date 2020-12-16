package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.getsetgo.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    Context context;

    public NotificationAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_notification_row, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView txtDetails, txtTitle;
        RoundedImageView imgNotification;


        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDetails = itemView.findViewById(R.id.txtNotifyDetails);
            txtTitle = itemView.findViewById(R.id.txtNotifyTitle);
            imgNotification = itemView.findViewById(R.id.imvNotification);

        }
    }
}

