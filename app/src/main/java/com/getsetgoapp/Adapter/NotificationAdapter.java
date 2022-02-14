package com.getsetgoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.getsetgoapp.Fragment.CourseDetailFragment;
import com.getsetgoapp.Fragment.NotificationsDetailsFragment;
import com.getsetgoapp.Model.NotificationModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.util.CheckConnection;
import com.getsetgoapp.util.Config;
import com.getsetgoapp.util.RemoveHtml;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    List<NotificationModel> notificationList;
    CourseDetailFragment courseDetailFragment;
    NotificationsDetailsFragment notificationsDetailsFragment;

    public NotificationAdapter(Context context, List<NotificationModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel model = notificationList.get(position);

        String image = model.getImage();
        if (!image.equals("") && !image.equals("null")) {
            Picasso.get().load(image).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.imgNotification);
        } else {
            Picasso.get().load(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.imgNotification);
        }

        holder.txtTitle.setText(model.getTitle());
        holder.txtDescription.setText(RemoveHtml.html2text(model.getDescription()));

        holder.lnrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(model.getAction_data()) && !model.getAction_data().equals("null")
                        && !TextUtils.isEmpty(model.getAction()) && !model.getAction().equals("null")) {
                    if (model.getAction().equals(Config.ACTION_COURSE)) {
                        if (CheckConnection.isVailable(context)) {
                            loadFragment(v, model.getTitle(), model.getAction_data());
                        } else {
                            H.showMessage(context, "No internet connection available");
                        }
                    } else {
                        jumpToNotificationDetails(v, model);
                    }
                } else {
                    jumpToNotificationDetails(v, model);
                }
            }
        });
    }

    private void jumpToNotificationDetails(View v, NotificationModel model) {
        Config.NOTIFICATION_IMAGE = model.getImage();
        Config.NOTIFICATION_TEXT = RemoveHtml.html2text(model.getDescription());
        loadDetailsFragment(v, model.getTitle(), model.getAction_data());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDescription;
        ImageView imgNotification;
        LinearLayout lnrView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imgNotification = itemView.findViewById(R.id.imgNotification);
            lnrView = itemView.findViewById(R.id.lnrView);
        }
    }

    private void loadFragment(View v, String title, String slug) {
        Bundle bundle = new Bundle();
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        bundle.putString("title", title);
        bundle.putString("slug", slug);
        bundle.putBoolean("isFromHome", false);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        courseDetailFragment = new CourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, courseDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadDetailsFragment(View v, String title, String slug) {

        Bundle bundle = new Bundle();
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        bundle.putString("title", title);
        bundle.putString("slug", slug);
        bundle.putBoolean("isFromHome", true);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        notificationsDetailsFragment = new NotificationsDetailsFragment();
        notificationsDetailsFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, notificationsDetailsFragment)
                .addToBackStack(null)
                .commit();
    }
}

