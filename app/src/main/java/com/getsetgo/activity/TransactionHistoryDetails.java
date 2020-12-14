package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.getsetgo.R;

public class TransactionHistoryDetails extends AppCompatActivity {

    TextView txtName,txtAmount,txtDateTime,txtAction,txtParent,txtCourse,txtIncomeType,txtDescription;
    Button btnGoBack;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history_details);
        context = TransactionHistoryDetails.this;
        init();
    }
    private void init(){
        txtName = findViewById(R.id.txtTitleName);
        txtAmount = findViewById(R.id.txtAmount);
        txtDateTime = findViewById(R.id.txtDateTime);
        txtAction = findViewById(R.id.txtAction);
        txtParent = findViewById(R.id.txtParent);
        txtCourse = findViewById(R.id.txtCourse);
        txtIncomeType = findViewById(R.id.txtIncomeType);
        txtDescription = findViewById(R.id.txtDescription);
        btnGoBack = findViewById(R.id.btnGoBack);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}