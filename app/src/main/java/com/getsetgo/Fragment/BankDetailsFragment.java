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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;

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
import com.getsetgo.Adapter.SpinnerSelectionAdapter;
import com.getsetgo.Model.SpinnerModel;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentBankDetailsBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.JumpToLogin;
import com.getsetgo.util.P;
import com.getsetgo.util.ProgressView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BankDetailsFragment extends Fragment {

    FragmentBankDetailsBinding binding;
    private static final int REQUEST_GALLARY = 19;
    private static final int REQUEST_CAMERA = 110;
    private static final int READ_WRIRE = 111;
    private Uri cameraURI;
    private int click;
    private final int cameraClick = 0;
    private final int galleryClick = 1;
    private String imgString = "";
    private String accountType = "";

    private List<SpinnerModel> bankAccountList;
    SpinnerSelectionAdapter bankAccountAdapter;

    LoadingDialog loadingDialog;

    String documentPath = "";

    public BankDetailsFragment() {
    }

    public static BankDetailsFragment newInstance() {
        BankDetailsFragment fragment = new BankDetailsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bank_details, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Bank Details");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        init(rootView);
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

               onBackPressClick();

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View view) {
        loadingDialog = new LoadingDialog(getActivity());

        bankAccountList = new ArrayList<>();
        bankAccountList.add(new SpinnerModel("","Select Account"));
        bankAccountList.add(new SpinnerModel("1","SAVING"));
        bankAccountList.add(new SpinnerModel("2","CURRENT"));
        bankAccountAdapter = new SpinnerSelectionAdapter(getActivity(), bankAccountList);
        binding.spinnerAccountType.setAdapter(bankAccountAdapter);


        onClick();
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        } catch (Exception e) {

        }

        hitGetBankData(getActivity());

    }

    private void onClick() {

        binding.spinnerAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerModel model = bankAccountList.get(position);
                if (!model.getId().equals("")){
                    accountType = model.getName();
                }else {
                    accountType = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.txtSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkValidation()){
                    hitSaveBankData(getActivity());
                }
            }
        });

        binding.llUploadDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                onUploadClick();
            }
        });

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        });

    }

    private boolean checkValidation(){
        boolean value = true;

        if (TextUtils.isEmpty(binding.etAccHolderName.getText().toString().trim())){
            H.showMessage(getActivity(),"Please enter account holder ame");
            value = false;
        }else if (TextUtils.isEmpty(binding.etBankName.getText().toString().trim())){
            H.showMessage(getActivity(),"Please enter bank name");
            value = false;
        }else if (TextUtils.isEmpty(binding.etAccNumber.getText().toString().trim())){
            H.showMessage(getActivity(),"Please enter account number");
            value = false;
        }else if (TextUtils.isEmpty(binding.etIFSCCode.getText().toString().trim())){
            H.showMessage(getActivity(),"Please enter IFSC code");
            value = false;
        }else if (TextUtils.isEmpty(binding.etBranch.getText().toString().trim())){
            H.showMessage(getActivity(),"Please enter branch name");
            value = false;
        }else if (TextUtils.isEmpty(accountType)){
            H.showMessage(getActivity(),"Please select account type");
            value = false;
        }else if (TextUtils.isEmpty(documentPath)){
            H.showMessage(getActivity(),"Please choose document");
            value = false;
        }
        return value;
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
            getActivity().startActivity(intent);
        } catch (Exception e) {
        }
    }

    private void openCamera() {
        try {
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
                                           String[] permissions, int[] grantResults) {
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
        imgString = "";
        try {
            final InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imgString = encodeImage(selectedImage);
            hitUploadImage(getActivity(),imgString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            H.showMessage(getActivity(), "Unable to get image, try again.");
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    private void hitSaveBankData(Context context) {
        ProgressView.show(context,loadingDialog);

        Json j = new Json();
        j.addString(P.bank_name,binding.etBankName.getText().toString().trim());
        j.addString(P.acc_holder_name,binding.etAccHolderName.getText().toString().trim());
        j.addString(P.bank_ifsc_code,binding.etIFSCCode.getText().toString().trim());
        j.addString(P.bank_account_number,binding.etAccNumber.getText().toString().trim());
        j.addString(P.bank_account_type,accountType);
        j.addString(P.bank_branch,binding.etBranch.getText().toString().trim());
        j.addString(P.bank_document_file_name,documentPath);

        Api.newApi(context, P.baseUrl + "bank_details").addJson(j)
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
                        H.showMessage(context,"Bank details save successfully");
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                KYCDocumentFragment myFragment = new KYCDocumentFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                            }
                        }, 500);
                    }
                })
                .run("hitSaveBankData");
    }

    private void hitGetBankData(Context context) {
        ProgressView.show(context,loadingDialog);

        Api.newApi(context, P.baseUrl + "bank_details")
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
                        Json detailsJson = data.getJson(P.bank_details);
                        String acc_holder_name = detailsJson.getString(P.acc_holder_name);
                        String bank_name = detailsJson.getString(P.bank_name);
                        String bank_ifsc_code = detailsJson.getString(P.bank_ifsc_code);
                        String bank_account_number = detailsJson.getString(P.bank_account_number);
                        String bank_branch = detailsJson.getString(P.bank_branch);
                        String bank_account_type = detailsJson.getString(P.bank_account_type);
                        String bank_document_file_name = detailsJson.getString(P.bank_document_file_name);
                        String bank_document_file_image = detailsJson.getString(P.bank_document_file_image);
                        String bank_approve_status = detailsJson.getString(P.bank_approve_status);
                        String bank_approve_remark = detailsJson.getString(P.bank_approve_remark);

                        binding.etAccHolderName.setText(checkString(acc_holder_name));
                        binding.etBankName.setText(checkString(bank_name));
                        binding.etAccNumber.setText(checkString(bank_account_number));
                        binding.etIFSCCode.setText(checkString(bank_ifsc_code));
                        binding.etBranch.setText(checkString(bank_branch));

                        if (bank_account_type.contains("SAVING")){
                            binding.spinnerAccountType.setSelection(1);
                        }else if (bank_account_type.contains("CURRENT")){
                            binding.spinnerAccountType.setSelection(2);
                        }

                        documentPath = bank_document_file_name;
                        if (!TextUtils.isEmpty(documentPath) && !documentPath.equals("null")){
                            binding.txtMessage.setText("File Name : " +  documentPath);
                            binding.txtDocument.setText("Re-Upload Document");
                        }

                        if (!TextUtils.isEmpty(bank_approve_status) && !bank_approve_status.equals("null")){
                            if (bank_approve_status.equals("0")){
                                binding.txtStatus.setText("Admin Status : " + "Pending");
                            }else if (bank_approve_status.equals("1")){
                                binding.txtStatus.setText("Admin Status : " + "Approved");
                            }else if (bank_approve_status.equals("2")){
                                binding.txtStatus.setText("Admin Status : " + "Rejected");
                                binding.txtStatus.setTextColor(getResources().getColor(R.color.colorReward));
                                if (!TextUtils.isEmpty(bank_approve_remark) && !bank_approve_remark.equals("null")){
                                    binding.txtStatusMessage.setText("Remark : " + bank_approve_remark);
                                }
                            }
                        }
                    }
                })
                .run("hitGetBankData");
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
                        documentPath = file_name;
                        binding.txtMessage.setText("File Name : " +  documentPath);
                        binding.txtDocument.setText("Re-Upload Document");
                        H.showMessage(context,"Document uploaded successfully");
                    }
                })
                .run("hitUploadImage");
    }


    private void onBackPressClick(){
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            BaseScreenActivity.callBack();
        }
    }

    private String checkString(String string){
        String value = "";
        if (!TextUtils.isEmpty(string) || !string.equals("null")){
            value =  string;
        }
        return  value;
    }

}