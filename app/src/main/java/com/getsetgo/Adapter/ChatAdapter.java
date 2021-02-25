package com.getsetgo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Model.ResponseMessage;
import com.getsetgo.R;
import com.getsetgo.util.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context;
    List<ResponseMessage> responseMessages;

    public ChatAdapter(Context context, List<ResponseMessage> responseMessages) {
        this.context = context;
        this.responseMessages = responseMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_message_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResponseMessage model = responseMessages.get(position);

        String TIME = "";

        if (!model.getDatetime().contains("AM") || !model.getDatetime().contains("PM")){

            StringTokenizer tk = new StringTokenizer(model.getDatetime());
            String date = tk.nextToken();
            String time = tk.nextToken();

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss",Locale.US);
            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm aa", Locale.US);

            Date dt;
            try {
                dt = sdf.parse(time);
                TIME = sdfs.format(dt);
            } catch (ParseException e) {
                e.printStackTrace();
                TIME = model.getDatetime();
            }

        }else {
            TIME = model.getDatetime();
        }

        if (model.getMsg_from().contains(Config.user)){
            holder.lnrUser.setVisibility(View.VISIBLE);
            holder.lnrAdmin.setVisibility(View.GONE);
            holder.txtUserMessage.setText(model.getMessage());
            holder.txtUserTime.setText(TIME);
        }else if (model.getMsg_from().contains(Config.admin)){
            holder.lnrAdmin.setVisibility(View.VISIBLE);
            holder.lnrUser.setVisibility(View.GONE);
            holder.txtAdminMessage.setText(model.getMessage());
            holder.txtAdminTime.setText(TIME);
        }

    }

    @Override
    public int getItemCount() {
        return responseMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout lnrAdmin;
        TextView txtAdminMessage;
        TextView txtAdminTime;

        RelativeLayout lnrUser;
        TextView txtUserMessage;
        TextView txtUserTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lnrAdmin = itemView.findViewById(R.id.lnrAdmin);
            lnrUser = itemView.findViewById(R.id.lnrUser);
            txtAdminMessage = itemView.findViewById(R.id.txtAdminMessage);
            txtAdminTime = itemView.findViewById(R.id.txtAdminTime);
            txtUserMessage = itemView.findViewById(R.id.txtUserMessage);
            txtUserTime = itemView.findViewById(R.id.txtUserTime);

        }
    }

    //layout_me_bubble_row
    //layout_bot_bubble_row


}

