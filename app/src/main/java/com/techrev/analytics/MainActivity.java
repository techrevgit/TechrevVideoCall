package com.techrev.analytics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.techrev.videocall.TechrevVideoCallManager;

public class MainActivity extends AppCompatActivity {

    // DEV SERVER (Has to be used during development)
    private final String SERVER_BASE_URL = "https://cnapidev.digitalnotarize.com/";
    // INTERMEDIATE TEST SERVER (Has to be used when need to release particial test release)
    //private final String SERVER_BASE_URL = "https://cnapiv1.digitalnotarize.com/";
    // FINAL TEST SERVER (Has to be used for test release)
    //private final String SERVER_BASE_URL = "https://custtestmobileapi.digitalnotarize.com/";
    // TRAINING SERVER
    //private final String SERVER_BASE_URL = "https://custtrainingmobileapi.enotaryoncall.com/";
    // PROD SERVER (Has to be used while releasing the app to the playstore)
    //private final String SERVER_BASE_URL = "https://cnapi.enotaryoncall.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnConnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TechrevVideoCallManager.connectToRoom
                        (MainActivity.this,
                                SERVER_BASE_URL,
                                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzljZWQ2ZjE3N2UwNWEwNmE4ZDM2MmE2MzRmZTA3MzE4LTE3MDkyODI5ODAiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJwYXJ0aWNpcGFudC1SdXBlc2ggSmVuYS04ODQiLCJ2aWRlbyI6eyJyb29tIjoiUkVRWlRHUUwifX0sImlhdCI6MTcwOTI4Mjk4MCwiZXhwIjoxNzA5Mjg2NTgwLCJpc3MiOiJTSzljZWQ2ZjE3N2UwNWEwNmE4ZDM2MmE2MzRmZTA3MzE4Iiwic3ViIjoiQUM5ZTU4YTJjODA1MmEzODZhOTRmODVhYzcxZmFiMzNhYiJ9.kuQqagb0-qjidAojAXmTQb44vjCB6o8EcT6hg04tm9E",
                                //"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzljZWQ2ZjE3N2UwNWEwNmE4ZDM2MmE2MzRmZTA3MzE4LTE3MDkxMjUyNzciLCJncmFudHMiOnsiaWRlbnRpdHkiOiJwYXJ0aWNpcGFudC1SdXBlc2ggSmVuYS04ODQiLCJ2aWRlbyI6eyJyb29tIjoiUkVRWlRHUUwifX0sImlhdCI6MTcwOTEyNTI3NywiZXhwIjoxNzA5MTI4ODc3LCJpc3MiOiJTSzljZWQ2ZjE3N2UwNWEwNmE4ZDM2MmE2MzRmZTA3MzE4Iiwic3ViIjoiQUM5ZTU4YTJjODA1MmEzODZhOTRmODVhYzcxZmFiMzNhYiJ9.lUmrSrs3q5jt0LXI6gUVjuFwdotJZyeS9Kybd_1MIBw",
                                "REQZTGQL",
                               // "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOnsidXNlcklkIjo1MCwidXNlck5hbWUiOiJ1c3JraWU0aWo5ODNsb24wOWZ3ZCIsImZpcnN0TmFtZSI6IlJ1cGVzaCIsImxhc3ROYW1lIjoiSmVuYSIsImVtYWlsIjoicmt1bWFyQHRlY2hyZXYudXMiLCJkaXNwbGF5TmFtZSI6bnVsbCwiaGFzQWRkcmVzcyI6bnVsbCwiaXNBY3RpdmUiOnRydWUsIkRvY0lkIjpudWxsLCJvcmlnaW5hbFByb2ZpbGVQaWNGaWxlTmFtZSI6bnVsbCwicHJvZmlsZVBpY01pbWVUeXBlIjpudWxsLCJjcmVhdGVkX2F0IjoiMjAyMy0xMS0wNlQxNDozNjoyNC44NjNaIiwidXBkYXRlZF9hdCI6IjIwMjMtMTEtMDZUMTQ6MzY6MjQuODYwWiIsIlNpZ25hdHVyZURvY0lkIjpudWxsLCJJZFByb29mRnJvbnREb2NJZCI6bnVsbCwiSWRQcm9vZkJhY2tEb2NJZCI6bnVsbCwiUGhvbmUiOiI4MDkzODMxNDM2IiwiTWlkZGxlTmFtZSI6Ikt1bWFyIiwiTm90YXJ5U2VhbERvY0lkIjpudWxsLCJpc05vdGFyeSI6MCwiTm90YXJ5T25Cb2FyZGluZ1N0YXR1cyI6MCwiaXNBZG1pbiI6MCwiTm90YXJ5Q2VydGlmaWNhdGVEb2NJZCI6bnVsbCwic29ja2V0SWQiOiJkc1l5WUF4SlQ5NWs5elIwQUJzNiIsInJlcXVlc3RJZCI6bnVsbCwiUHJvZmlsZVBpY3R1cmVEb2NJZCI6bnVsbCwiZG9jUHJvb2ZUeXBlIjpudWxsLCJhZGRyZXNzSWQiOm51bGwsImlkUHJvb2ZOdW1iZXIiOm51bGwsImRvYiI6IjE5OTctMDctMDlUMTg6MzA6MDAuMDAwWiIsInBob25lUHJlZml4Q29kZSI6Iis5MSIsImd1aWQiOm51bGwsIlBhc3N3b3JkUmVzZXRUaW1lIjpudWxsLCJsYXN0Rm91ckRpZ2l0U1NOIjoiMTIzNCIsImlzVHdvU3RlcFZlcmlmaWNhdGlvbiI6MCwiQ3VzdG9tZXJQcm9maWxlSWQiOm51bGwsInVwZGF0ZWRCeSI6IjUwIiwibGFzdFNvY2tldFVwZGF0ZWREYXRlVGltZSI6IjIwMjQtMDItMjhUMTE6Mjk6MDcuNzAwWiIsImlwYWRkcmVzcyI6bnVsbCwibGF0aXR1ZGUiOm51bGwsImxvbmdpdHVkZSI6bnVsbCwiaXNWZXJpZmljYXRpb25DaGFyZ2VzRGViaXRlZCI6MSwibGFzdFVwZGF0ZWRQYXltZW50UHJvZmlsZUlkIjoiUElkZktGZmFHUk52ZlNjRmNWQUNiMlJuIiwiYXZhaWxhYmxlRGlzY291bnQiOm51bGwsImF1dGhvcml6ZWRDYXJkU3RhdHVzIjowLCJ2ZXJpZmljYXRpb25NYWlsQ291bnQiOjAsImF1dGhvcml6ZU5ldEN1c3RvbWVyUHJvZmlsZUlkIjpudWxsLCJ1c2VyR2V0SWQiOiI4OTEwN2RhOS05ODVlLTQxMzItYjE3Mi1lY2FkOTdkNDZlNWYiLCJvbGRQaG9uZSI6bnVsbCwib2xkRW1haWwiOm51bGwsImlzRGVsZXRlZCI6bnVsbCwiY3VzdG9tZXJUeXBlIjpudWxsLCJmaW5peEN1c3RvbWVyUHJvZmlsZUlkIjoiSUQ1cFB3Y2F0Rmk2aEVQVVo4M0pQZ281IiwiZmluaXhDYXJkU3RhdHVzIjoxLCJpbnZpdGF0aW9uSWQiOm51bGwsImlzQ2VsbFBob25lTnVtYmVyVmVyaWZpZWQiOjEsImN1c3RvbWVyU2lnbnVwU291cmNlIjoiSW5zdGFncmFtIiwiaXNXaGF0c0FwcE5vdGlmaWNhdGlvbkVuYWJsZWQiOjEsImlzU2VsZWN0Q2FyZFN0ZXBCeVBhc3NlZCI6bnVsbCwiY2FyZEJ5cGFzc1JlYXNvbiI6bnVsbCwidXBkYXRlZEJ5VXNlclR5cGUiOjMsImlzUGVybWFuZW50bHlEZWxldGVkIjpmYWxzZSwiYXBwVHlwZSI6ImN1c3RvbWVyIn0sImlhdCI6MTcwOTExOTc5OCwiZXhwIjoxNzE0MzAwMTk4fQ.RhVjMCJplv_w8zMNlT-3-62cgZKZsbQpe9H05nWyKIY",
                                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOnsidXNlcklkIjo1MCwidXNlck5hbWUiOiJ1c3JraWU0aWo5ODNsb24wOWZ3ZCIsImZpcnN0TmFtZSI6IlJ1cGVzaCIsImxhc3ROYW1lIjoiSmVuYSIsImVtYWlsIjoicmt1bWFyQHRlY2hyZXYudXMiLCJkaXNwbGF5TmFtZSI6bnVsbCwiaGFzQWRkcmVzcyI6bnVsbCwiaXNBY3RpdmUiOnRydWUsIkRvY0lkIjpudWxsLCJvcmlnaW5hbFByb2ZpbGVQaWNGaWxlTmFtZSI6bnVsbCwicHJvZmlsZVBpY01pbWVUeXBlIjpudWxsLCJjcmVhdGVkX2F0IjoiMjAyMy0xMS0wNlQxNDozNjoyNC44NjNaIiwidXBkYXRlZF9hdCI6IjIwMjMtMTEtMDZUMTQ6MzY6MjQuODYwWiIsIlNpZ25hdHVyZURvY0lkIjpudWxsLCJJZFByb29mRnJvbnREb2NJZCI6bnVsbCwiSWRQcm9vZkJhY2tEb2NJZCI6bnVsbCwiUGhvbmUiOiI4MDkzODMxNDM2IiwiTWlkZGxlTmFtZSI6Ikt1bWFyIiwiTm90YXJ5U2VhbERvY0lkIjpudWxsLCJpc05vdGFyeSI6MCwiTm90YXJ5T25Cb2FyZGluZ1N0YXR1cyI6MCwiaXNBZG1pbiI6MCwiTm90YXJ5Q2VydGlmaWNhdGVEb2NJZCI6bnVsbCwic29ja2V0SWQiOm51bGwsInJlcXVlc3RJZCI6bnVsbCwiUHJvZmlsZVBpY3R1cmVEb2NJZCI6bnVsbCwiZG9jUHJvb2ZUeXBlIjpudWxsLCJhZGRyZXNzSWQiOm51bGwsImlkUHJvb2ZOdW1iZXIiOm51bGwsImRvYiI6IjE5OTctMDctMDlUMTg6MzA6MDAuMDAwWiIsInBob25lUHJlZml4Q29kZSI6Iis5MSIsImd1aWQiOm51bGwsIlBhc3N3b3JkUmVzZXRUaW1lIjpudWxsLCJsYXN0Rm91ckRpZ2l0U1NOIjoiMTIzNCIsImlzVHdvU3RlcFZlcmlmaWNhdGlvbiI6MCwiQ3VzdG9tZXJQcm9maWxlSWQiOm51bGwsInVwZGF0ZWRCeSI6IjUwIiwibGFzdFNvY2tldFVwZGF0ZWREYXRlVGltZSI6IjIwMjQtMDMtMDFUMDg6NDc6MDYuMTM2WiIsImlwYWRkcmVzcyI6bnVsbCwibGF0aXR1ZGUiOm51bGwsImxvbmdpdHVkZSI6bnVsbCwiaXNWZXJpZmljYXRpb25DaGFyZ2VzRGViaXRlZCI6MSwibGFzdFVwZGF0ZWRQYXltZW50UHJvZmlsZUlkIjoiUElkZktGZmFHUk52ZlNjRmNWQUNiMlJuIiwiYXZhaWxhYmxlRGlzY291bnQiOm51bGwsImF1dGhvcml6ZWRDYXJkU3RhdHVzIjowLCJ2ZXJpZmljYXRpb25NYWlsQ291bnQiOjAsImF1dGhvcml6ZU5ldEN1c3RvbWVyUHJvZmlsZUlkIjpudWxsLCJ1c2VyR2V0SWQiOiI4OTEwN2RhOS05ODVlLTQxMzItYjE3Mi1lY2FkOTdkNDZlNWYiLCJvbGRQaG9uZSI6bnVsbCwib2xkRW1haWwiOm51bGwsImlzRGVsZXRlZCI6bnVsbCwiY3VzdG9tZXJUeXBlIjpudWxsLCJmaW5peEN1c3RvbWVyUHJvZmlsZUlkIjoiSUQ1cFB3Y2F0Rmk2aEVQVVo4M0pQZ281IiwiZmluaXhDYXJkU3RhdHVzIjoxLCJpbnZpdGF0aW9uSWQiOm51bGwsImlzQ2VsbFBob25lTnVtYmVyVmVyaWZpZWQiOjEsImN1c3RvbWVyU2lnbnVwU291cmNlIjoiSW5zdGFncmFtIiwiaXNXaGF0c0FwcE5vdGlmaWNhdGlvbkVuYWJsZWQiOjEsImlzU2VsZWN0Q2FyZFN0ZXBCeVBhc3NlZCI6bnVsbCwiY2FyZEJ5cGFzc1JlYXNvbiI6bnVsbCwidXBkYXRlZEJ5VXNlclR5cGUiOjMsImlzUGVybWFuZW50bHlEZWxldGVkIjpmYWxzZSwiYXBwVHlwZSI6ImN1c3RvbWVyIn0sImlhdCI6MTcwOTI4MjgzMywiZXhwIjoxNzExODcxMjMzfQ.N3OzW8HQP_6igcvBtHBqsnAnOShsYpjOV5KSR8lJGd4",
                                "0",
                                0,
                                "participant-Rupesh Jena-884",
                                "0",
                                "REQZTGQL",
                                0,
                                "0",
                                "0",
                                "0",
                                "50",
                                false,
                                "{\"userId\":50,\"userName\":\"usrkie4ij983lon09fwd\",\"firstName\":\"Rupesh\",\"lastName\":\"Jena\",\"email\":\"rkumar@techrev.us\",\"displayName\":null,\"hasAddress\":null,\"isActive\":true,\"DocId\":null,\"originalProfilePicFileName\":null,\"profilePicMimeType\":null,\"created_at\":\"2023-11-06T14:36:24.863Z\",\"updated_at\":\"2023-11-06T14:36:24.860Z\",\"SignatureDocId\":null,\"IdProofFrontDocId\":null,\"IdProofBackDocId\":null,\"Phone\":\"8093831436\",\"MiddleName\":\"Kumar\",\"NotarySealDocId\":null,\"isNotary\":0,\"NotaryOnBoardingStatus\":0,\"isAdmin\":0,\"NotaryCertificateDocId\":null,\"socketId\":\"OQ8BvcMBDxytAN5BAARo\",\"requestId\":null,\"ProfilePictureDocId\":null,\"docProofType\":null,\"addressId\":null,\"idProofNumber\":null,\"dob\":\"1997-07-09T18:30:00.000Z\",\"phonePrefixCode\":\"+91\",\"guid\":null,\"PasswordResetTime\":null,\"lastFourDigitSSN\":\"1234\",\"isTwoStepVerification\":0,\"CustomerProfileId\":null,\"updatedBy\":\"50\",\"lastSocketUpdatedDateTime\":\"2024-01-24T15:22:35.446Z\",\"ipaddress\":null,\"latitude\":null,\"longitude\":null,\"isVerificationChargesDebited\":1,\"lastUpdatedPaymentProfileId\":\"PIdfKFfaGRNvfScFcVACb2Rn\",\"availableDiscount\":null,\"authorizedCardStatus\":0,\"verificationMailCount\":0,\"authorizeNetCustomerProfileId\":null,\"userGetId\":\"89107da9-985e-4132-b172-ecad97d46e5f\",\"oldPhone\":null,\"oldEmail\":null,\"isDeleted\":null,\"customerType\":null,\"finixCustomerProfileId\":\"ID5pPwcatFi6hEPUZ83JPgo5\",\"finixCardStatus\":1,\"invitationId\":null,\"isCellPhoneNumberVerified\":1,\"customerSignupSource\":\"Instagram\",\"isWhatsAppNotificationEnabled\":1,\"isSelectCardStepByPassed\":null,\"cardBypassReason\":null,\"updatedByUserType\":3,\"isPermanentlyDeleted\":null,\"appType\":\"customer\",\"stayLoggedIn\":false}",
                                "1022",
                                "null"
                                );
            }
        });
    }
}