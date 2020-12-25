package com.getsetgo.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.H;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentUploadDocsBinding;
import com.getsetgo.util.Click;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UploadDocsFragment extends Fragment {

    FragmentUploadDocsBinding binding;


    private static final int REQUEST_GALLARY = 19;
    private static final int REQUEST_CAMERA = 110;
    private static final int READ_WRIRE = 111;
    private Uri cameraURI;
    private int click;
    private final int cameraClick = 0;
    private final int galleryClick = 1;
    private String imgString = "";


    public UploadDocsFragment() {
    }

    public static UploadDocsFragment newInstance() {
        UploadDocsFragment fragment = new UploadDocsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_docs, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Upload Documents");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        init();
        return rootView;
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




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void onUploadClick() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.upload_dialog);
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

    private void init() {
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }catch (Exception e){

        }
        onClick();
    }

    private void onClick() {
        binding.txtSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.llUploadPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUploadClick();
            }
        });
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


}