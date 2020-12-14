package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getsetgo.R;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    public TextView txtName, txtEmail, txtUserId, txtStatus, txtAffliateLink,
            txtAffliateCode, txtTotalUser, txtTotalDirectUser, txtFranchiseConatct,
            txtAvisorContact, txtMasterContact, txtAdviserName, txtFranchiseName, txtMasterName;
    CircleImageView imvViewProfile;
    ImageView imvCopyCode, imvCopyLink;
    RoundedHorizontalProgressBar progressBarCompleteProfile;
    RelativeLayout rlBusKit, rlCourseBoard, rlFranc, rlAdvisor, rlInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        context = DashBoardActivity.this;
        init();
    }

    private void init() {
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtUserId = findViewById(R.id.txtUserId);
        txtTotalUser = findViewById(R.id.txtTotalUser);
        txtTotalDirectUser = findViewById(R.id.txtTotalDirectUser);
        txtAdviserName = findViewById(R.id.txtAdviserName);
        txtFranchiseName = findViewById(R.id.txtFranchiseName);
        txtMasterName = findViewById(R.id.txtMasterName);
        txtAvisorContact = findViewById(R.id.txtAvisorContact);
        txtMasterContact = findViewById(R.id.txtMasterContact);
        txtFranchiseConatct = findViewById(R.id.txtFranchiseConatct);
        imvViewProfile = findViewById(R.id.imvViewProfile);
        progressBarCompleteProfile = findViewById(R.id.progressBarDash);
        txtStatus = findViewById(R.id.txtStatus);
        txtAffliateLink = findViewById(R.id.txtAffLink);
        txtAffliateCode = findViewById(R.id.txtAffCode);
        imvCopyCode = findViewById(R.id.imvCopyCode);
        imvCopyLink = findViewById(R.id.imvCopyLink);
        rlBusKit = findViewById(R.id.rlBusKit);
        rlCourseBoard = findViewById(R.id.rlCourseBoard);
        rlFranc = findViewById(R.id.rlFranc);
        rlAdvisor = findViewById(R.id.rlAdvisor);
        rlInfo = findViewById(R.id.rlInfo);

        rlAdvisor.setOnClickListener(this);
        rlBusKit.setOnClickListener(this);
        rlCourseBoard.setOnClickListener(this);
        rlFranc.setOnClickListener(this);
        rlInfo.setOnClickListener(this);

        imvCopyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = txtAffliateCode.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context,clipboard.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        imvCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = txtAffliateLink.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label link", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context,clipboard.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rlAdvisor:
                break;
            case R.id.rlFranc:
                break;
            case R.id.rlCourseBoard:
                break;
            case R.id.rlInfo:
                break;
            case R.id.rlBusKit:
                break;
        }

    }
}