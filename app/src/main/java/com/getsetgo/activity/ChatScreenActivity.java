package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;


import com.getsetgo.Adapter.ChatAdapter;
import com.getsetgo.Model.ResponseMessage;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityChatScreenBinding;
import com.getsetgo.util.Utilities;
import com.getsetgo.util.WindowView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatScreenActivity extends AppCompatActivity {

    private ChatScreenActivity activity = this;
    private ActivityChatScreenBinding binding;
    List<ResponseMessage> responseMessages = new ArrayList<>();
    ChatAdapter chatAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_screen);
        init();
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setStackFromEnd(true);
        binding.recyclerViewChats.setLayoutManager(mLayoutManager);
        binding.recyclerViewChats.setItemAnimator(new DefaultItemAnimator());

        setupRecyclerViewForChats();
        binding.rlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etMessage.getText().toString().trim().isEmpty() &&
                        !binding.etMessage.getText().toString().trim().equals("")) {
                    String msg = binding.etMessage.getText().toString().trim();
                    ResponseMessage responseMessage = new ResponseMessage();
                    responseMessage.setMessage(msg);
                    responseMessage.setTime(Utilities.getCurrentDateTime());
                    responseMessage.setViewType(0);
                    responseMessages.add(responseMessage);
                    chatAdapter.notifyDataSetChanged();
                    binding.recyclerViewChats.smoothScrollToPosition(responseMessages.lastIndexOf(responseMessage));
                    binding.etMessage.setText("");

                }
            }
        });

    }


    private void setupRecyclerViewForChats() {
        ResponseMessage responseMessage5 = new ResponseMessage();
        responseMessage5.setMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, tempor.");
        responseMessage5.setTime("6:06 PM");
        responseMessage5.setViewType(1);
        responseMessages.add(responseMessage5);

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Lorem ipsum dolor sit amet, consetetur, sed diam nonumy eirmod tempor.");
        responseMessage.setTime("6:06 PM");
        responseMessage.setViewType(0);
        responseMessages.add(responseMessage);

        ResponseMessage responseMessage2 = new ResponseMessage();
        responseMessage2.setMessage("Lorem ipsum dolor sit amet, elitr, sed diam nonumy eirmod tempor.");
        responseMessage2.setTime("6:09 PM");
        responseMessage2.setViewType(1);
        responseMessages.add(responseMessage2);

        ResponseMessage responseMessage3 = new ResponseMessage();
        responseMessage3.setMessage("Lorem ipsum dolor sit sadipscing elitr, sed diam nonumy eirmod tempor.");
        responseMessage3.setTime("7:06 PM");
        responseMessage3.setViewType(0);
        responseMessages.add(responseMessage3);

        ResponseMessage responseMessage4 = new ResponseMessage();
        responseMessage4.setMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, tempor.");
        responseMessage4.setTime("8:06 PM");
        responseMessage4.setViewType(1);
        responseMessages.add(responseMessage4);

        responseMessages.add(responseMessage);
        responseMessages.add(responseMessage2);
        responseMessages.add(responseMessage3);
        responseMessages.add(responseMessage4);

        chatAdapter = new ChatAdapter(activity, responseMessages);
        binding.recyclerViewChats.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        binding.recyclerViewChats.smoothScrollToPosition(responseMessages.lastIndexOf(responseMessage));

    }
}