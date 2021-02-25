package com.getsetgo.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
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
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentChangePasswordBinding;
import com.getsetgo.databinding.FragmentEditProfileBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.JumpToLogin;
import com.getsetgo.util.P;
import com.getsetgo.util.ProgressView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    boolean isFromBottom;
    private DatePickerDialog mDatePickerDialog;
    LoadingDialog loadingDialog;
    String gender = "";
    String occupation = "";
    String maritalStatus = "";
    String profilePic = "";

    public EditProfileFragment() {
    }

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                onBackClick();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);
        View rootView = binding.getRoot();

        loadingDialog = new LoadingDialog(getActivity());
        hitInitApi(getActivity());

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Edit Profile");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        isFromBottom = getArguments().getBoolean("isFromBottom");

        setDateTimeField(binding.etxDate);
        binding.etxDate.setFocusable(false);
        binding.etxDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_date_range_24, 0);
        binding.etxDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDatePickerDialog.show();
                return false;
            }
        });

        onClick();

        return rootView;
    }

    private void onClick(){

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });

        binding.radioMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radioMale.setChecked(true);
                binding.radioFeMale.setChecked(false);
                gender = "1";
            }
        });

        binding.radioFeMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radioMale.setChecked(false);
                binding.radioFeMale.setChecked(true);
                gender = "2";
            }
        });

        binding.lnrUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

            }
        });

        binding.txtSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (TextUtils.isEmpty(occupation)){
                    H.showMessage(getActivity(),"Please select occupation");
                }else if (TextUtils.isEmpty(binding.etxDate.getText().toString().trim())){
                    H.showMessage(getActivity(),"Please select date of birth");
                }else if (TextUtils.isEmpty(maritalStatus)){
                    H.showMessage(getActivity(),"Please select marital status");
                }else if (TextUtils.isEmpty(gender)){
                    H.showMessage(getActivity(),"Please select gender");
                }else {
                    hitUpdateProfile(getActivity());
                }
            }
        });

    }

    private void onBackClick(){
        if(getFragmentManager().getBackStackEntryCount() > 0){
            if(isFromBottom){
                getFragmentManager().popBackStackImmediate();
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.VISIBLE);
                BaseScreenActivity.binding.bottomNavigation.setSelectedItemId(R.id.menu_Account);
            }else{
                getFragmentManager().popBackStackImmediate();
            }
        }
    }

    private void setDateTimeField(EditText editText) {

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);

                editText.setText(fdate);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private void hitInitApi(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "init")
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
                        JumpToLogin.call(Json1,context);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            Json1 = Json1.getJson(P.data);
                            JsonList occupation_list = Json1.getJsonList(P.occupation_list);
                            JsonList marital_status_list = Json1.getJsonList(P.marital_status_list);
                        }
                    }

                }).run("hitInitApi");
    }


    private void hitUpdateProfile(Context context) {
        ProgressView.show(context,loadingDialog);

        Json j = new Json();
        j.addString(P.occupation_id,occupation);
        j.addString(P.dob,binding.etxDate.getText().toString().trim());
        j.addString(P.marital_status_id,maritalStatus);
        j.addString(P.gender,gender);
        j.addString(P.profile_picture,profilePic);

        Api.newApi(context, P.baseUrl + "edit_profile").addJson(j)
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
                        H.showMessage(context,"Profile save successfully");
                        getFragmentManager().popBackStackImmediate();
                    }
                })
                .run("hitUpdateProfile");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
