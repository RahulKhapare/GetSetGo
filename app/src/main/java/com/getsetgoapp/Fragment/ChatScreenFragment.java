package com.getsetgoapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgoapp.Adapter.ChatAdapter;
import com.getsetgoapp.Model.ResponseMessage;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;

import com.getsetgoapp.databinding.FragmentChatScreenrBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.CheckConnection;
import com.getsetgoapp.util.Config;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.getsetgoapp.util.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ChatScreenFragment extends Fragment {

    private FragmentChatScreenrBinding binding;
    List<ResponseMessage> responseMessages = new ArrayList<>();
    ChatAdapter chatAdapter;
    LinearLayoutManager mLayoutManager;
    LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_screenr, container, false);
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
        loadingDialog = new LoadingDialog(getActivity());
        hitMessageThreadApi(getActivity(), Config.subjectID);
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Support/Help");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setStackFromEnd(true);
        binding.recyclerViewChats.setLayoutManager(mLayoutManager);
        binding.recyclerViewChats.setItemAnimator(new DefaultItemAnimator());
        chatAdapter = new ChatAdapter(getActivity(), responseMessages);
        binding.recyclerViewChats.setAdapter(chatAdapter);

        binding.rlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(binding.etMessage.getText().toString().trim())) {
                    String msg = binding.etMessage.getText().toString().trim();

                    ResponseMessage responseMessage = new ResponseMessage();
                    responseMessage.setMessage(msg);
                    responseMessage.setDatetime(Utilities.getCurrentDateTime());
                    responseMessage.setMsg_from(Config.user);

                    if (CheckConnection.isVailable(getActivity())){
                        responseMessages.add(responseMessage);
                        chatAdapter.notifyDataSetChanged();
                        binding.recyclerViewChats.smoothScrollToPosition(responseMessages.size());
                        binding.etMessage.setText("");
                        hitReplyMessageThreadApi(getActivity(),Config.subjectID,responseMessage);
                    }else {
                        H.showMessage(getActivity(),"Please check internet connection");
                    }

                }
            }
        });

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
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

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    private void hitMessageThreadApi(Context context, String subject_id) {

        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "message_thread" + "/" + subject_id)
                .setMethod(Api.GET)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (isLoading)
                        loadingDialog.show("loading...");
                    else
                        loadingDialog.hide();
                })
                .onError(() ->
                        MessageBox.showOkMessage(context, "Message", "Failed to login. Please try again", () -> {
                            loadingDialog.dismiss();
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        JumpToLogin.call(Json1, context);
                        loadingDialog.dismiss();
                        Json jsonData = Json1.getJson(P.data);
                        JsonList list = jsonData.getJsonList(P.list);
                        for (Json listJson : list) {
                            ResponseMessage model = new ResponseMessage();
                            String message = listJson.getString(P.message);
                            String datetime = listJson.getString(P.datetime);
                            String msg_from = listJson.getString(P.msg_from);
                            model.setMessage(message);
                            model.setDatetime(datetime);
                            model.setMsg_from(msg_from);
                            responseMessages.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                        binding.recyclerViewChats.smoothScrollToPosition(responseMessages.size());
                    }

                }).run("hitMessageThreadApi");

    }

    private void hitReplyMessageThreadApi(Context context, String subjectId,ResponseMessage model) {

//        ProgressView.show(context,loadingDialog);

        Json j = new Json();
        j.addString(P.subject_id, subjectId);
        j.addString(P.message, model.getMessage());

        Api.newApi(context, P.baseUrl + "reply_message_thread").addJson(j)
                .setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
//                    ProgressView.dismiss(loadingDialog);
                })
                .onSuccess(json ->
                {
                    JumpToLogin.call(json, context);
                    if (json.getInt(P.status) == 1) {

//                        responseMessages.add(model);
//                        chatAdapter.notifyDataSetChanged();
//                        binding.recyclerViewChats.smoothScrollToPosition(responseMessages.size());
//                        binding.etMessage.setText("");

                    }
//                    ProgressView.dismiss(loadingDialog);
                })
                .run("hitReplyMessageThreadApi");
    }

}