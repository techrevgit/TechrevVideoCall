package com.techrev.analytics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.techrev.videocall.TechrevVideoCallManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        TechrevVideoCallManager.connectToRoom
                (MainActivity.this,
                        "https://custtestmobileapi",
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzljZWQ2ZjE3N2UwNWEwNmE4ZDM2MmE2MzRmZTA3MzE4LTE3MDE4NjYzOTQiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJwYXJ0aWNpcGFudC1SdXBlc2ggQ3VzdG9tZXItNDI3IiwidmlkZW8iOnsicm9vbSI6IlJFUUNDTUQyIn19LCJpYXQiOjE3MDE4NjYzOTQsImV4cCI6MTcwMTg2OTk5NCwiaXNzIjoiU0s5Y2VkNmYxNzdlMDVhMDZhOGQzNjJhNjM0ZmUwNzMxOCIsInN1YiI6IkFDOWU1OGEyYzgwNTJhMzg2YTk0Zjg1YWM3MWZhYjMzYWIifQ.dijAf-OmCDV8A1IDAEib41ROOZ4GThaQ79UdPs04Qw4",
                        "REQCCMD2",
                        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOnsidXNlcklkIjo1MCwidXNlck5hbWUiOiJ1c3JraWU0aWo5ODNsb24wOWZ3ZCIsImZpcnN0TmFtZSI6IlJ1cGVzaCIsImxhc3ROYW1lIjoiQ3VzdG9tZXIiLCJlbWFpbCI6InJrdW1hckB0ZWNocmV2LnVzIiwiZGlzcGxheU5hbWUiOm51bGwsImhhc0FkZHJlc3MiOm51bGwsImlzQWN0aXZlIjp0cnVlLCJEb2NJZCI6bnVsbCwib3JpZ2luYWxQcm9maWxlUGljRmlsZU5hbWUiOm51bGwsInByb2ZpbGVQaWNNaW1lVHlwZSI6bnVsbCwiY3JlYXRlZF9hdCI6IjIwMjMtMTEtMDZUMTQ6MzY6MjQuODYzWiIsInVwZGF0ZWRfYXQiOiIyMDIzLTExLTA2VDE0OjM2OjI0Ljg2MFoiLCJTaWduYXR1cmVEb2NJZCI6bnVsbCwiSWRQcm9vZkZyb250RG9jSWQiOm51bGwsIklkUHJvb2ZCYWNrRG9jSWQiOm51bGwsIlBob25lIjoiODA5MzgzMTQzNiIsIk1pZGRsZU5hbWUiOm51bGwsIk5vdGFyeVNlYWxEb2NJZCI6bnVsbCwiaXNOb3RhcnkiOjAsIk5vdGFyeU9uQm9hcmRpbmdTdGF0dXMiOjAsImlzQWRtaW4iOjAsIk5vdGFyeUNlcnRpZmljYXRlRG9jSWQiOm51bGwsInNvY2tldElkIjpudWxsLCJyZXF1ZXN0SWQiOm51bGwsIlByb2ZpbGVQaWN0dXJlRG9jSWQiOm51bGwsImRvY1Byb29mVHlwZSI6bnVsbCwiYWRkcmVzc0lkIjpudWxsLCJpZFByb29mTnVtYmVyIjpudWxsLCJkb2IiOiIxOTk3LTA3LTA5VDE4OjMwOjAwLjAwMFoiLCJwaG9uZVByZWZpeENvZGUiOiIrOTEiLCJndWlkIjpudWxsLCJQYXNzd29yZFJlc2V0VGltZSI6bnVsbCwibGFzdEZvdXJEaWdpdFNTTiI6bnVsbCwiaXNUd29TdGVwVmVyaWZpY2F0aW9uIjoxLCJDdXN0b21lclByb2ZpbGVJZCI6bnVsbCwidXBkYXRlQnkiOm51bGwsInVwZGF0ZWRCeSI6bnVsbCwibGFzdFNvY2tldFVwZGF0ZWREYXRlVGltZSI6IjIwMjMtMTEtMDhUMDk6NTA6NTAuMTg2WiIsImlwYWRkcmVzcyI6bnVsbCwibGF0aXR1ZGUiOm51bGwsImxvbmdpdHVkZSI6bnVsbCwiaXNWZXJpZmljYXRpb25DaGFyZ2VzRGViaXRlZCI6MSwibGFzdFVwZGF0ZWRQYXltZW50UHJvZmlsZUlkIjoiUElkZktGZmFHUk52ZlNjRmNWQUNiMlJuIiwiYXZhaWxhYmxlRGlzY291bnQiOm51bGwsImF1dGhvcml6ZWRDYXJkU3RhdHVzIjowLCJ2ZXJpZmljYXRpb25NYWlsQ291bnQiOjAsImF1dGhvcml6ZU5ldEN1c3RvbWVyUHJvZmlsZUlkIjpudWxsLCJ1c2VyR2V0SWQiOiI4OTEwN2RhOS05ODVlLTQxMzItYjE3Mi1lY2FkOTdkNDZlNWYiLCJvbGRQaG9uZSI6bnVsbCwib2xkRW1haWwiOm51bGwsImlzRGVsZXRlZCI6bnVsbCwiY3VzdG9tZXJUeXBlIjpudWxsLCJmaW5peEN1c3RvbWVyUHJvZmlsZUlkIjoiSUQ1cFB3Y2F0Rmk2aEVQVVo4M0pQZ281IiwiZmluaXhDYXJkU3RhdHVzIjoxLCJpbnZpdGF0aW9uSWQiOm51bGwsImlzQ2VsbFBob25lTnVtYmVyVmVyaWZpZWQiOjEsImN1c3RvbWVyU2lnbnVwU291cmNlIjoiSW5zdGFncmFtIiwiaXNXaGF0c0FwcE5vdGlmaWNhdGlvbkVuYWJsZWQiOjEsImlzU2VsZWN0Q2FyZFN0ZXBCeVBhc3NlZCI6bnVsbCwiY2FyZEJ5cGFzc1JlYXNvbiI6bnVsbCwiYXBwVHlwZSI6ImN1c3RvbWVyIn0sImlhdCI6MTY5OTQzNzI0MiwiZXhwIjoxNzAyMDI5MjQyfQ.LfJkDugpSH7uwggdWrQ5LGC_FwSvrarsVmIomjN184A",
                        "0",
                        0,
                        "participant-Rupesh Customer-427",
                        "0",
                        "REQCCMD2",
                        0,
                        "0",
                        "0",
                        "0",
                        "50",
                        false,
                        "{\"userId\":50,\"userName\":\"usrkie4ij983lon09fwd\",\"firstName\":\"Rupesh\",\"lastName\":\"Customer\",\"email\":\"rkumar@techrev.us\",\"displayName\":null,\"hasAddress\":null,\"isActive\":true,\"DocId\":null,\"originalProfilePicFileName\":null,\"profilePicMimeType\":null,\"created_at\":\"2023-11-06T14:36:24.863Z\",\"updated_at\":\"2023-11-06T14:36:24.860Z\",\"SignatureDocId\":null,\"IdProofFrontDocId\":null,\"IdProofBackDocId\":null,\"Phone\":\"8093831436\",\"MiddleName\":null,\"NotarySealDocId\":null,\"isNotary\":0,\"NotaryOnBoardingStatus\":0,\"isAdmin\":0,\"NotaryCertificateDocId\":null,\"socketId\":null,\"requestId\":null,\"ProfilePictureDocId\":null,\"docProofType\":null,\"addressId\":null,\"idProofNumber\":null,\"dob\":\"1997-07-09T18:30:00.000Z\",\"phonePrefixCode\":\"+91\",\"guid\":null,\"PasswordResetTime\":null,\"lastFourDigitSSN\":null,\"isTwoStepVerification\":1,\"CustomerProfileId\":null,\"updateBy\":null,\"updatedBy\":null,\"lastSocketUpdatedDateTime\":\"2023-11-08T09:50:50.186Z\",\"ipaddress\":null,\"latitude\":null,\"longitude\":null,\"isVerificationChargesDebited\":1,\"lastUpdatedPaymentProfileId\":\"PIdfKFfaGRNvfScFcVACb2Rn\",\"availableDiscount\":null,\"authorizedCardStatus\":0,\"verificationMailCount\":0,\"authorizeNetCustomerProfileId\":null,\"userGetId\":\"89107da9-985e-4132-b172-ecad97d46e5f\",\"oldPhone\":null,\"oldEmail\":null,\"isDeleted\":null,\"customerType\":null,\"finixCustomerProfileId\":\"ID5pPwcatFi6hEPUZ83JPgo5\",\"finixCardStatus\":1,\"invitationId\":null,\"isCellPhoneNumberVerified\":1,\"customerSignupSource\":\"Instagram\",\"isWhatsAppNotificationEnabled\":1,\"isSelectCardStepByPassed\":null,\"cardBypassReason\":null,\"appType\":\"customer\",\"stayLoggedIn\":false}",
                        "",
                        "null");
    }
}