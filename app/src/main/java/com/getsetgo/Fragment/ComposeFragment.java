package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.R;
import com.getsetgo.databinding.FragmentComposeBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.P;
import com.getsetgo.util.Validation;

public class ComposeFragment extends Fragment {

    FragmentComposeBinding binding;
    Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_compose, container, false);
        View rootView = binding.getRoot();
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view){

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidation()){
                    Json json = new Json();
                    json.addString("subject",binding.etSubject.getText().toString());
                    json.addString("message",binding.etMessage.getText().toString());
                    callComposeMessageAPI(context,json);
                }
            }
        });


    }

    private boolean checkValidation() {
        boolean value = true;

        if (TextUtils.isEmpty(binding.etSubject.getText().toString().trim())) {
            H.showMessage(context, "Enter subject");
            value = false;
        }  else if (TextUtils.isEmpty(binding.etMessage.getText().toString().trim())) {
            H.showMessage(context, "Enter message");
            value = false;
        }

        return value;
    }

    private void callComposeMessageAPI(Context context, Json json) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        Api.newApi(context, P.baseUrl + "compose").addJson(json)
                .setMethod(Api.POST)
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
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            Json1 = Json1.getJson(P.data);

                        }
                    }

                }).run("compose");
    }


}