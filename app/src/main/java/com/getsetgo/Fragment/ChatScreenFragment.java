package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.getsetgo.Adapter.ChatAdapter;
import com.getsetgo.Model.ResponseMessage;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;

import com.getsetgo.databinding.FragmentChatScreenrBinding;
import com.getsetgo.util.Utilities;

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
        chatAdapter = new ChatAdapter(getActivity(), responseMessages);
        binding.recyclerViewChats.setAdapter(chatAdapter);

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

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if(getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStackImmediate();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    private void setupRecyclerViewForChats() {
        ResponseMessage responseMessage5 = new ResponseMessage();
        responseMessage5.setMessage("Lorem ipsum dolor sit amet, elitr, tempor.");
        responseMessage5.setTime("6:06 PM");
        responseMessage5.setViewType(1);
        responseMessages.add(responseMessage5);

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Lorem ipsum, consetetur, sed diam nonumy eirmod tempor.");
        responseMessage.setTime("6:06 PM");
        responseMessage.setViewType(0);
        responseMessages.add(responseMessage);

        ResponseMessage responseMessage2 = new ResponseMessage();
        responseMessage2.setMessage("eirmod tempor.");
        responseMessage2.setTime("6:09 PM");
        responseMessage2.setViewType(1);
        responseMessages.add(responseMessage2);

        responseMessages.add(responseMessage);
        responseMessages.add(responseMessage2);


        chatAdapter.notifyDataSetChanged();
        binding.recyclerViewChats.smoothScrollToPosition(responseMessages.lastIndexOf(responseMessage));

    }




}