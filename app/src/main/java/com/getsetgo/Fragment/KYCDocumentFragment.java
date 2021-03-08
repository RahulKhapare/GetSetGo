package com.getsetgo.Fragment;

import android.Manifest;
import android.app.Activity;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentKycDocumentBinding;
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

public class KYCDocumentFragment extends Fragment {

    private FragmentKycDocumentBinding binding;
    private static final int REQUEST_GALLARY = 9;
    private static final int REQUEST_CAMERA = 10;
    private static final int READ_WRIRE = 11;
    private Uri cameraURI;
    private int click;
    private int cameraClick = 0;
    private int galleryClick = 1;
    private String base64Image = "";
    private int clickFor = 0;
    private int PANClick = 1;
    private int AadharClick = 2;
    LoadingDialog loadingDialog;
    String aadharImage = "";
    String panImage = "";
    String pan_card_number = "";
    String user_pan_card_file_name = "";
    String user_pan_card_file_image = "";
    String aadhar_card_number = "";
    String user_aadhar_card_file_name = "";
    String user_aadhar_card_file_image = "";
    String document_approve_status = "";
    String document_approve_remark = "";

    public KYCDocumentFragment() {
    }

    public static KYCDocumentFragment newInstance() {
        KYCDocumentFragment fragment = new KYCDocumentFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_kyc_document, container, false);
        View rootView = binding.getRoot();


        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("KYC Documents");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        init();

        return rootView;
    }

    private void init(){
        loadingDialog = new LoadingDialog(getActivity());
        onClick();
        hitGetKYCData(getActivity());
    }


    private void onClick(){

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressClick();
            }
        });

        binding.llAdharUploadDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                clickFor = AadharClick;
                if (!document_approve_status.equals("1")){
                    onUploadClick();
                }
            }
        });

        binding.llPANUploadDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                clickFor = PANClick;
                if (!document_approve_status.equals("1")){
                    onUploadClick();
                }
            }
        });

        binding.txtSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkValidation()){
                    if (document_approve_status.equals("1")){
                        H.showMessage(getActivity(),"KYC document already uploaded");
                    }else {
                        hitSaveKYCData(getActivity());
                    }
                }
            }
        });

    }

    private boolean checkValidation(){
        boolean value = true;

        if (TextUtils.isEmpty(binding.etxPanCardNumber.getText().toString().trim())){
            H.showMessage(getActivity(),"Please enter PAN number");
            value = false;
        }else if (TextUtils.isEmpty(binding.etxAdharCardNumber.getText().toString().trim())){
            H.showMessage(getActivity(),"Please enter Aadhar number");
            value = false;
        }else if (TextUtils.isEmpty(panImage)){
            H.showMessage(getActivity(),"Please choose PAN card image");
            value = false;
        }else if (TextUtils.isEmpty(aadharImage)){
            H.showMessage(getActivity(),"Please choose Aadhar card image");
            value = false;
        }
        return value;
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

    private void hitSaveKYCData(Context context) {
        ProgressView.show(context,loadingDialog);

        Json j = new Json();
        j.addString(P.pan_card_number,binding.etxPanCardNumber.getText().toString().trim());
        j.addString(P.user_pan_card_file_name,panImage);
        j.addString(P.aadhar_card_number,binding.etxAdharCardNumber.getText().toString().trim());
        j.addString(P.user_aadhar_card_file_name,aadharImage);

        Api.newApi(context, P.baseUrl + "kyc_details").addJson(j)
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
                        H.showMessage(context,"KYC details save successfully");
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                NomineeDetailsFragment myFragment = new NomineeDetailsFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                            }
                        }, 500);
                    }else {
                        H.showMessage(context,json.getString(P.err));
                    }
                })
                .run("hitSaveKYCData");
    }


    private void hitGetKYCData(Context context) {
        ProgressView.show(context,loadingDialog);

        Api.newApi(context, P.baseUrl + "kyc_details")
                .setMethod(Api.GET)
                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                })
                .onSuccess(json ->
                {
                    JumpToLogin.call(json,context);
                    ProgressView.dismiss(loadingDialog);
                    if (json.getInt(P.status) == 1) {
                        Json data = json.getJson(P.data);
                        Json detailsJson = data.getJson(P.aadhar_pan_details);

                        pan_card_number = detailsJson.getString(P.pan_card_number);
                        user_pan_card_file_name = detailsJson.getString(P.user_pan_card_file_name);
                        user_pan_card_file_image = detailsJson.getString(P.user_pan_card_file_image);
                        aadhar_card_number = detailsJson.getString(P.aadhar_card_number);
                        user_aadhar_card_file_name = detailsJson.getString(P.user_aadhar_card_file_name);
                        user_aadhar_card_file_image = detailsJson.getString(P.user_aadhar_card_file_image);
                        document_approve_status = detailsJson.getString(P.document_approve_status);
                        document_approve_remark = detailsJson.getString(P.document_approve_remark);

                        panImage = user_pan_card_file_name;
                        aadharImage = user_aadhar_card_file_name;
                        binding.etxPanCardNumber.setText(pan_card_number);
                        binding.etxAdharCardNumber.setText(aadhar_card_number);

                        if (!TextUtils.isEmpty(panImage) && !panImage.equals("null")){
                            binding.txtPanMessage.setText("File Name : " + panImage);
                            binding.txtPAnCardUpload.setText("Re-Upload PAN Card Image");
                        }

                        if (!TextUtils.isEmpty(aadharImage) && !aadharImage.equals("null")){
                            binding.txtAadharMessage.setText("File Name : " + aadharImage);
                            binding.txtAadharCardUpload.setText("Re-Upload Aadhar Card Image");
                        }

                        if (!TextUtils.isEmpty(document_approve_status) && !document_approve_status.equals("null")){
                            if (document_approve_status.equals("0")){
                                binding.txtStatus.setText("Admin Status : " + "Pending");
                            }else if (document_approve_status.equals("1")){
                                disableData(document_approve_status);
                                binding.txtStatus.setText("Admin Status : " + "Approved");
                            }else if (document_approve_status.equals("2")){
                                binding.txtStatus.setText("Admin Status : " + "Rejected");
                                binding.txtStatus.setTextColor(getResources().getColor(R.color.colorReward));
                                if (!TextUtils.isEmpty(document_approve_remark) && !document_approve_remark.equals("null")){
                                    binding.txtStatusMessage.setText("Remark : " + document_approve_remark);
                                }
                            }
                        }

                    }
                })
                .run("hitGetKYCData");
    }

    private void hitUploadImage(Context context,String base64Image) {
        ProgressView.show(context,loadingDialog);

        Json j = new Json();
        j.addString(P.type,"kyc_docs");
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
                        String file_name = jsonData.getString(P.file_name);
                        String file_path = jsonData.getString(P.file_path);
                        if (clickFor==AadharClick){
                            aadharImage = file_name;
                            binding.txtAadharMessage.setText("File Name : " + aadharImage);
                            binding.txtAadharCardUpload.setText("Re-Upload Aadhar Card Image");
                        }else if (clickFor==PANClick){
                            panImage = file_name;
                            binding.txtPanMessage.setText("File Name : " + panImage);
                            binding.txtPAnCardUpload.setText("Re-Upload PAN Card Image");
                        }
                        H.showMessage(context,"Document uploaded successfully");
                    }
                })
                .run("hitUploadImage");
    }

    private void disableData(String status){

        if (status.equals("1")){
            noEditable(binding.etxPanCardNumber);
            noEditable(binding.etxAdharCardNumber);
            binding.txtSaveNext.setVisibility(View.GONE);
        }
    }

    private void noEditable(EditText editText){
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        editText.setClickable(false);
    }

    private void onBackPressClick(){
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            BaseScreenActivity.callBack();
        }
    }
}
