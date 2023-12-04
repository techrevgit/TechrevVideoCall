package com.techrev.analytics;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import com.withpersona.sdk2.inquiry.Environment;
import com.withpersona.sdk2.inquiry.Inquiry;
import com.withpersona.sdk2.inquiry.InquiryResponse;

public class IDVerificationActivity extends AppCompatActivity {

    /*Added By Rupesh*/

    private static final String TAG = "IDVerificationActivity";
    private String personaURL = null;
    private String inquiryTemplateId = null;
    private String isUseSandBox = null;
    private String accountId = null;
    private String redirectURL = null;
    private String userId = null;
    private Inquiry inquiry = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_idverification);
        Intent it = getIntent();
        if (it != null){
            personaURL = it.getStringExtra("personaURL");
            inquiryTemplateId = it.getStringExtra("inquiryTemplateId");
            isUseSandBox = it.getStringExtra("isUseSandBox");
            accountId = it.getStringExtra("accountId");
            redirectURL = it.getStringExtra("redirectURL");
            userId = it.getStringExtra("userId");

            /*Handle Persona ID Verification Responses*/
            ActivityResultLauncher<Inquiry> getInquiryResult =
                    registerForActivityResult(new Inquiry.Contract(),
                            new ActivityResultCallback<InquiryResponse>() {
                                @Override public void onActivityResult(InquiryResponse result) {
                                    if (result instanceof InquiryResponse.Complete) {
                                        InquiryResponse.Complete complete = (InquiryResponse.Complete) result;
                                        // ... completed flow
                                        Log.d(TAG , "ID VERIFICATION COMPLETED");
                                        //Toast.makeText(IDVerificationActivity.this, "ID Verification Successful", Toast.LENGTH_SHORT).show();
                                        //new TechRevIdVerificationWrapper().getTechRevIdVerificationWrapperInstance().notifyStatusToTheWeb(1 , userId);
                                        IDVerificationActivity.this.finish();
                                    } else if (result instanceof InquiryResponse.Cancel) {
                                        InquiryResponse.Cancel cancel = (InquiryResponse.Cancel) result;
                                        // ... abandoned flow
                                        Log.d(TAG , "ID VERIFICATION CANCELED");
                                        //new TechRevIdVerificationWrapper().getTechRevIdVerificationWrapperInstance().notifyStatusToTheWeb(1 , userId);
                                        IDVerificationActivity.this.finish();
                                    } else if (result instanceof InquiryResponse.Error) {
                                        InquiryResponse.Error error = (InquiryResponse.Error) result;
                                        // ... something went wrong
                                        Log.d(TAG , "ID VERIFICATION ERROR: "+ error.toString());
                                        //new TechRevIdVerificationWrapper().getTechRevIdVerificationWrapperInstance().notifyStatusToTheWeb(0 , userId);
                                        IDVerificationActivity.this.finish();
                                    }
                                }
                            });
            /*Handle Persona ID Verification Responses*/


            /*Initiate Persona ID Verification SDK*/
            if (isUseSandBox.equalsIgnoreCase("true")){
                inquiry = Inquiry.fromTemplate(inquiryTemplateId)
                        .environment(Environment.SANDBOX)
                        .accountId(accountId)
                        .build();
            }else {
                inquiry = Inquiry.fromTemplate(inquiryTemplateId)
                        .accountId(accountId)
                        .build();
            }

            getInquiryResult.launch(inquiry);

            /*Initiate Persona ID Verification SDK*/

        }
    }
}