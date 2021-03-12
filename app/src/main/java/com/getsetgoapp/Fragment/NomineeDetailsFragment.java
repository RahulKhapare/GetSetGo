package com.getsetgoapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentNomineeDocumentBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.getsetgoapp.util.ProgressView;

public class NomineeDetailsFragment extends Fragment {

    private FragmentNomineeDocumentBinding binding;

    LoadingDialog loadingDialog;
    String relationOne = "";
    String relationTwo = "";
    String status = "";



    public NomineeDetailsFragment() {
    }

    public static NomineeDetailsFragment newInstance() {
        NomineeDetailsFragment fragment = new NomineeDetailsFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                onBackPressClick();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nominee_document, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Nominee Documents");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        init();

        return rootView;
    }

    private void init(){

        loadingDialog = new LoadingDialog(getActivity());
        hitGetNomineeData(getActivity());

        onClick();
    }


    private void onClick(){

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressClick();
            }
        });


        binding.txtSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkValidation()){
                    if (status.equals("1")){
                        H.showMessage(getActivity(),"Nominee details already uploaded");
                    }else {
                        hitSaveNomineeData(getActivity());
                    }

                }
            }
        });

    }


    private boolean checkValidation(){
        boolean value = true;

        if (TextUtils.isEmpty(binding.etxNomineeOne.getText().toString().trim())){
            H.showMessage(getActivity(),"Please enter nominee one name");
            value = false;
        }else if (TextUtils.isEmpty(binding.etxNomineeOneRelation.getText().toString().trim())){
            H.showMessage(getActivity(),"Please enter nominee one relation");
            value = false;
        }else if (TextUtils.isEmpty(binding.etxNomineeOne.getText().toString().trim())){
            H.showMessage(getActivity(),"Please enter nominee two name");
            value = false;
        }else if (TextUtils.isEmpty(binding.etxNomineeTwoRelation.getText().toString().trim())){
            H.showMessage(getActivity(),"Please enter nominee two relation");
            value = false;
        }
        return value;
    }

    private void hitSaveNomineeData(Context context) {
        ProgressView.show(context,loadingDialog);

        Json j = new Json();
        j.addString(P.nominee1,binding.etxNomineeOne.getText().toString().trim());
        j.addString(P.nominee1_relation,binding.etxNomineeOneRelation.getText().toString().trim());
        j.addString(P.nominee2,binding.etxNomineeTwo.getText().toString().trim());
        j.addString(P.nominee2_relation,binding.etxNomineeTwoRelation.getText().toString().trim());

        Api.newApi(context, P.baseUrl + "nominee_details").addJson(j)
                .setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    MessageBox.showOkMessage(context, "Message", "Failed to login. Please try again", () -> {
                    });
                })
                .onSuccess(json ->
                {
                    JumpToLogin.call(json,context);
                    ProgressView.dismiss(loadingDialog);
                    if (json.getInt(P.status) == 1) {
                        H.showMessage(context,"Details save successfully");
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                               onBackPressClick();
                            }
                        }, 500);
                    }
                })
                .run("hitSaveNomineeData");
    }

    private void hitGetNomineeData(Context context) {
        ProgressView.show(context,loadingDialog);
        Api.newApi(context, P.baseUrl + "nominee_details")
                .setMethod(Api.GET)
                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    MessageBox.showOkMessage(context, "Message", "Failed to login. Please try again", () -> {
                    });
                })
                .onSuccess(json ->
                {
                    JumpToLogin.call(json,context);
                    ProgressView.dismiss(loadingDialog);
                    if (json.getInt(P.status) == 1) {
                        Json jsonData = json.getJson(P.data);
                        Json nomineeJson = jsonData.getJson(P.nominee_details);
                        binding.etxNomineeOne.setText(checkString(nomineeJson.getString(P.nominee1)));
                        binding.etxNomineeOneRelation.setText(checkString(nomineeJson.getString(P.nominee1_relation)));
                        binding.etxNomineeTwo.setText(checkString(nomineeJson.getString(P.nominee2)));
                        binding.etxNomineeTwoRelation.setText(checkString(nomineeJson.getString(P.nominee2_relation)));
                        status = "1";
                        disableData(status);
                    }else {
                        H.showMessage(context,json.getString(P.err));
                    }
                })
                .run("hitGetNomineeData");
    }

    private void disableData(String status){

        if (status.equals("1")){
            noEditable(binding.etxNomineeOne);
            noEditable(binding.etxNomineeOneRelation);
            noEditable(binding.etxNomineeTwo);
            noEditable(binding.etxNomineeTwoRelation);
            binding.txtSaveNext.setVisibility(View.GONE);
        }
    }

    private void noEditable(EditText editText){
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        editText.setClickable(false);
    }

    private String checkString(String string){
        String value = "";
        if (!TextUtils.isEmpty(string) || !string.equals("null")){
            value = string;
        }
        return value;
    }

    private void onBackPressClick(){
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            BaseScreenActivity.callBack();
        }
    }

}
