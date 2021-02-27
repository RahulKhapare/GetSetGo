package com.getsetgo.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgo.Adapter.GenderAdapter;
import com.getsetgo.Adapter.SpinnerSelectionAdapter;
import com.getsetgo.Model.GenderModel;
import com.getsetgo.Model.SpinnerModel;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentEditProfileBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.JumpToLogin;
import com.getsetgo.util.P;
import com.getsetgo.util.ProgressView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditProfileFragment extends Fragment implements GenderAdapter.click {

    private FragmentEditProfileBinding binding;
    boolean isFromBottom;
    private DatePickerDialog mDatePickerDialog;
    LoadingDialog loadingDialog;
    String gender = "";
    String occupation = "";
    String maritalStatus = "";
    String profilePic = "";
    String file_path = "";
    private List<SpinnerModel> occupationList;
    private List<SpinnerModel> maritalStatusList;
    SpinnerSelectionAdapter statusAdapter;
    SpinnerSelectionAdapter occupationAdapter;

    List<GenderModel> genderModelList;
    GenderAdapter genderAdapter;

    private static final int REQUEST_GALLARY = 9;
    private static final int REQUEST_CAMERA = 10;
    private static final int READ_WRIRE = 11;
    private Uri cameraURI;
    private int click;
    private int cameraClick = 0;
    private int galleryClick = 1;
    private String base64Image = "";
    Session session;

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

        session = new Session(getActivity());

        strictMode();
        loadingDialog = new LoadingDialog(getActivity());

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Edit Profile");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        isFromBottom = getArguments().getBoolean("isFromBottom");

        occupationList = new ArrayList<>();
        SpinnerModel occupationModel = new SpinnerModel();
        occupationModel.setId("");
        occupationModel.setName("Select Occupation");
        occupationList.add(occupationModel);
        occupationAdapter = new SpinnerSelectionAdapter(getActivity(), occupationList);
        binding.spinnerOccupation.setAdapter(occupationAdapter);

        maritalStatusList = new ArrayList<>();
        SpinnerModel statusModel = new SpinnerModel();
        statusModel.setId("");
        statusModel.setName("Select Marital Status");
        maritalStatusList.add(statusModel);
        statusAdapter = new SpinnerSelectionAdapter(getActivity(), maritalStatusList);
        binding.spinnerMaritalStatus.setAdapter(statusAdapter);

        genderModelList = new ArrayList<>();
        genderAdapter = new GenderAdapter(getActivity(),genderModelList,EditProfileFragment.this,true);
        binding.recyclerGender.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerGender.setNestedScrollingEnabled(false);
        binding.recyclerGender.setAdapter(genderAdapter);

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
        setProfileData();

        setupOccupationData(BaseScreenActivity.occupation_list);
        setupStatusData(BaseScreenActivity.marital_status_list);
        setupGenderData(BaseScreenActivity.gender_list);

        return rootView;
    }

    private void setProfileData(){
        binding.etxDate.setText(session.getString(P.dob));
        String profile_picture = session.getString(P.profile_picture);
        if (!TextUtils.isEmpty(profile_picture)){
            Picasso.get().load(profile_picture).placeholder(R.drawable.ic_profile_imag).error(R.drawable.ic_profile_imag).into(binding.imgProfileImage);
        }
    }

    @Override
    public void genderClick(String id) {
        gender = id;
    }

    private void onClick(){

        binding.spinnerOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerModel model = occupationList.get(position);
                if (TextUtils.isEmpty(model.getId())){
                    occupation = "";
                }else {
                    occupation = model.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerMaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerModel model = maritalStatusList.get(position);
                if (TextUtils.isEmpty(model.getId())){
                    maritalStatus = "";
                }else {
                    maritalStatus = model.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });

        binding.lnrUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                onUploadClick();
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

    private void setupOccupationData(JsonList jsonList){
        String id = session.getString(P.occupation_id);
        int adapterPosition = 0;
        if (jsonList!=null && jsonList.size()!=0){
            for (int i=0; i<jsonList.size(); i++){
                Json json = jsonList.get(i);
                SpinnerModel model = new SpinnerModel();
                model.setId(json.getString(P.id));
                model.setName(json.getString(P.occupation_name));
                occupationList.add(model);
                if (!TextUtils.isEmpty(id) && model.getId().equals(id)){
                    adapterPosition = i+1;
                }
            }
            occupationAdapter.notifyDataSetChanged();
            binding.spinnerOccupation.setSelection(adapterPosition);
        }
    }

    private void setupStatusData(JsonList jsonList){
        String id = session.getString(P.marital_status_id);
        int adapterPosition = 0;
        if (jsonList!=null && jsonList.size()!=0){
            for (int i=0; i<jsonList.size(); i++){
                Json json = jsonList.get(i);
                SpinnerModel model = new SpinnerModel();
                model.setId(json.getString(P.id));
                model.setName(json.getString(P.title));
                maritalStatusList.add(model);
                if (!TextUtils.isEmpty(id) && model.getId().equals(id)){
                    adapterPosition = i+1;
                }
            }
            statusAdapter.notifyDataSetChanged();
            binding.spinnerMaritalStatus.setSelection(adapterPosition);
        }
    }

    private void setupGenderData(JsonList jsonList){
        if (jsonList!=null && jsonList.size()!=0){
            for (int i=0; i<jsonList.size(); i++){
                Json json = jsonList.get(i);
                GenderModel model = new GenderModel();
                model.setName(json.getString(P.name));
                model.setValue(json.getString(P.value));
                genderModelList.add(model);
            }
            genderAdapter.notifyDataSetChanged();
        }
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
                        session.addString(P.dob,binding.etxDate.getText().toString().trim());
                        session.addString(P.occupation_id,occupation);
                        session.addString(P.marital_status_id,maritalStatus);
                        session.addString(P.gender,gender);
                        if (!TextUtils.isEmpty(file_path)){
                            session.addString(P.profile_picture,file_path);
                        }
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getFragmentManager().popBackStackImmediate();
                            }
                        }, 500);
                    }
                })
                .run("hitUpdateProfile");
    }

    private void hitUploadImage(Context context,String base64Image) {
        ProgressView.show(context,loadingDialog);

        Json j = new Json();
        j.addString(P.type,"profile_picture");
        j.addString(P.content,"data:image/jpeg;base64,"+base64Image);

        Api.newApi(context, P.baseUrl + "upload").addJson(j)
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
                        Json jsonData = json.getJson(P.data);
                        profilePic = jsonData.getString(P.file_name);
                        file_path = jsonData.getString(P.file_path);
                        Picasso.get().load(file_path).placeholder(R.drawable.ic_profile_imag).error(R.drawable.ic_profile_imag).into(binding.imgProfileImage);
                        binding.txtUpload.setText("Re-Upload Profile Image");
                        H.showMessage(context,"Image Uploaded successfully");
                    }
                })
                .run("hitUploadImage");
    }


    private void strictMode(){
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }catch (Exception e){

        }
    }

    private void onUploadClick() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_upload_dialog);
        dialog.findViewById(R.id.txtCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.cancel();
                click = cameraClick;
                getPermission();
            }
        });
        dialog.findViewById(R.id.txtGallary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.cancel();
                click = galleryClick;
                getPermission();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }


    private void getPermission() {

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    READ_WRIRE);
        } else {
            //Do your work
            if (click == cameraClick) {
                openCamera();
            } else if (click == galleryClick) {
                openGallery();
            }
        }
    }


    private void jumpToSetting() {
        H.showMessage(getActivity(), "Please allow permission from setting.");
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    private void openCamera() {
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(Environment.getExternalStorageDirectory(),
                    fileName);
            cameraURI = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraURI);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception e) {
        }
    }

    private void openGallery() {
        try {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, REQUEST_GALLARY);
        } catch (Exception e) {
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_WRIRE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (click == cameraClick) {
                        openCamera();
                    } else if (click == galleryClick) {
                        openGallery();
                    }
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    jumpToSetting();
                } else {
                    getPermission();
                }
                return;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GALLARY:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    try {
                        Uri selectedImage = data.getData();
                        setImageData(selectedImage);
                    } catch (Exception e) {
                    }
                }
                break;
            case REQUEST_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        setImageData(cameraURI);
                    } catch (Exception e) {
                    }
                }
                break;
        }
    }


    private void setImageData(Uri uri) {
        base64Image = "";
        try {
            InputStream imageStream = getContext().getContentResolver().openInputStream(uri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            base64Image = encodeImage(selectedImage);
            hitUploadImage(getActivity(),base64Image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("TAG", "setImageDataEE: "+ e.getMessage() );
            H.showMessage(getActivity(), "Unable to get image, try again.");
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
