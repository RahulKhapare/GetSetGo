package com.getsetgo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.getsetgo.Model.NotificationModel;
import com.getsetgo.Model.ResponseMessage;
import com.getsetgo.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter {

    Context context;
    List<NotificationModel> notificationList;
    private static final int VIEW_TYPE_SPECIAL_OFFER = 3;
    private static final int VIEW_TYPE_CONGRATULATIONS = 2;
    private static final int VIEW_TYPE_NOTIFICATION = 1;

    public NotificationAdapter(Context context, List<NotificationModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_NOTIFICATION) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_notification_row, parent, false);
            return new NotificationHolder(view);
        } else if (viewType == VIEW_TYPE_SPECIAL_OFFER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_special_notification_row, parent, false);
            return new SpecailOfferHolder(view);
        } else if (viewType == VIEW_TYPE_CONGRATULATIONS) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_congrats_notification_row, parent, false);
            return new CongratulationsHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        NotificationModel notifi = notificationList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_SPECIAL_OFFER:
                ((NotificationAdapter.SpecailOfferHolder) holder).bind(notifi);
                break;
            case VIEW_TYPE_CONGRATULATIONS:
                ((NotificationAdapter.CongratulationsHolder) holder).bind(notifi);
                break;
            case VIEW_TYPE_NOTIFICATION:
                ((NotificationAdapter.NotificationHolder) holder).bind(notifi);
        }
    }


    @Override
    public int getItemViewType(int position) {
        NotificationModel notification = (NotificationModel) notificationList.get(position);
        if (notification.getViewType() == 1) {
            return VIEW_TYPE_NOTIFICATION;
        } else if (notification.getViewType() == 2) {
            return VIEW_TYPE_CONGRATULATIONS;
        } else {
            return VIEW_TYPE_SPECIAL_OFFER;
        }

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        TextView txtDetails, txtTitle;
        ImageView imgNotification;


        public NotificationHolder(@NonNull View itemView) {
            super(itemView);

            txtDetails = itemView.findViewById(R.id.txtNotifyDetails);
            txtTitle = itemView.findViewById(R.id.txtNotifyTitle);
            imgNotification = itemView.findViewById(R.id.imvNotification);

        }

        void bind(NotificationModel messageModel) {

        }
    }

    private class SpecailOfferHolder extends RecyclerView.ViewHolder {

        TextView title, details;


        public SpecailOfferHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txtSpecialTitle);
            details = (TextView) itemView.findViewById(R.id.txtSpecialDetails);

        }

        void bind(NotificationModel messageModel) {

        }

    }

    private class CongratulationsHolder extends RecyclerView.ViewHolder {

        TextView title, details;
        TextView time;


        public CongratulationsHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txttCongratsTitle);
            details = (TextView) itemView.findViewById(R.id.txtCongratsDetails);

        }

        void bind(NotificationModel messageModel) {

        }

    }
}

