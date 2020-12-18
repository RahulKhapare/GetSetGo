package com.getsetgo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Model.ResponseMessage;
import com.getsetgo.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    Context context;
    List<ResponseMessage> responseMessages;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public ChatAdapter(Context context, List<ResponseMessage> responseMessages) {
        this.context = context;
        this.responseMessages = responseMessages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_me_bubble_row, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_bot_bubble_row, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ResponseMessage message = responseMessages.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }

    }

    @Override
    public int getItemViewType(int position) {
        ResponseMessage message = (ResponseMessage) responseMessages.get(position);
        if (message.getViewType() == 0) {
            // If the current user is the sender of the message
            Log.e("getItemViewType", "0");
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            Log.e("getItemViewType", "1");
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }

    }

    @Override
    public int getItemCount() {
        return responseMessages.size();
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder{

        TextView message;
        TextView time;


        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView)itemView.findViewById(R.id.txtMessage);
            time = (TextView)itemView.findViewById(R.id.txtTime);

        }

        void bind(ResponseMessage messageModel) {
            message.setText(messageModel.getMessage());
            time.setText(messageModel.getTime());

        }

    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder{
        TextView message;
        TextView time;
        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView)itemView.findViewById(R.id.txtMessage);
            time = (TextView)itemView.findViewById(R.id.txtTime);
        }

        void bind(ResponseMessage messageModel){
            message.setText(messageModel.getMessage());
            time.setText(messageModel.getTime());
        }
    }
}

