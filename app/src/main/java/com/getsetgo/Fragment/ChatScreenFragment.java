package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.H;
import com.getsetgo.Adapter.ChatAdapter;
import com.getsetgo.Adapter.CountryCodeAdapter;
import com.getsetgo.Model.CountryCode;
import com.getsetgo.Model.ResponseMessage;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.activity.ChatScreenActivity;
import com.getsetgo.databinding.ActivityChatScreenBinding;
import com.getsetgo.databinding.FragmentAddNewUserBinding;
import com.getsetgo.databinding.FragmentChatScreenrBinding;
import com.getsetgo.util.Click;
import com.getsetgo.util.Utilities;
import com.getsetgo.util.Validation;

import java.util.ArrayList;
import java.util.List;

public class ChatScreenFragment extends Fragment {

    private FragmentChatScreenrBinding binding;
    List<ResponseMessage> responseMessages = new ArrayList<>();
    ChatAdapter chatAdapter;
    LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chat_screenr, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View rootView = binding.getRoot();
        init(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View view) {
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Support/Help");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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

        chatAdapter = new ChatAdapter(getActivity(), responseMessages);
        binding.recyclerViewChats.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        binding.recyclerViewChats.smoothScrollToPosition(responseMessages.lastIndexOf(responseMessage));

    }




}