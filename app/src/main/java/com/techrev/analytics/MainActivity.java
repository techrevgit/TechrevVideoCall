package com.techrev.analytics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.techrev.videocall.TechrevVideoCallManager;

public class MainActivity extends AppCompatActivity {

    // DEV SERVER (Has to be used during development)
    //private final String SERVER_BASE_URL = "https://cnapidev.digitalnotarize.com/";
    // INTERMEDIATE TEST SERVER (Has to be used when need to release particial test release)
    //private final String SERVER_BASE_URL = "https://cnapiv1.digitalnotarize.com/";
    // FINAL TEST SERVER (Has to be used for test release)
    private final String SERVER_BASE_URL = "https://custtestmobileapi.digitalnotarize.com/";
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
                                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzljZWQ2ZjE3N2UwNWEwNmE4ZDM2MmE2MzRmZTA3MzE4LTE3Mjg5MDgwNzAiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJwYXJ0aWNpcGFudC1SdXBlc2ggSmVuYS04ODQiLCJ2aWRlbyI6eyJyb29tIjoiUkVRWlRHUUwifX0sImlhdCI6MTcyODkwODA3MCwiZXhwIjoxNzI4OTExNjcwLCJpc3MiOiJTSzljZWQ2ZjE3N2UwNWEwNmE4ZDM2MmE2MzRmZTA3MzE4Iiwic3ViIjoiQUM5ZTU4YTJjODA1MmEzODZhOTRmODVhYzcxZmFiMzNhYiJ9.ypjoa78geEw7YjyNS3UMkMsZDrHLVvfrMTV6Wj0s_4s",
                                "REQZTGQL",
                                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOnsidXNlcklkIjo1MCwidXNlck5hbWUiOiJ1c3JraWU0aWo5ODNsb24wOWZ3ZCIsImZpcnN0TmFtZSI6IlJ1cGVzaCIsIk1pZGRsZU5hbWUiOiJLdW1hciIsImxhc3ROYW1lIjoiSmVuYSIsImVtYWlsIjoicmt1bWFyQHRlY2hyZXYudXMiLCJkaXNwbGF5TmFtZSI6bnVsbCwiaXNBY3RpdmUiOnRydWUsInByb2ZpbGVQaWNNaW1lVHlwZSI6bnVsbCwiUGhvbmUiOiI4MDkzODMxNDM2Iiwic29ja2V0SWQiOm51bGwsInBob25lUHJlZml4Q29kZSI6Iis5MSIsImlzVHdvU3RlcFZlcmlmaWNhdGlvbiI6MCwibGFzdFNvY2tldFVwZGF0ZWREYXRlVGltZSI6IjIwMjQtMTAtMTRUMTI6MDE6MDUuMjkzWiIsImlzVmVyaWZpY2F0aW9uQ2hhcmdlc0RlYml0ZWQiOjEsImlzUGVybWFuZW50bHlEZWxldGVkIjpmYWxzZSwiaXNDdXN0b21lclByb2ZpbGVDb21wbGV0ZWQiOjEsImlzUGxhdGZvcm1SZWdpc3RlcmVkVXNlciI6MSwiaXNPYXV0aFVzZXIiOjAsImlzV2hhdHNBcHBOb3RpZmljYXRpb25FbmFibGVkIjoxLCJpc0NlbGxQaG9uZU51bWJlclZlcmlmaWVkIjoxLCJpc05vdGFyeSI6MCwiZmluaXhDdXN0b21lclByb2ZpbGVJZCI6IklENXBQd2NhdEZpNmhFUFVaODNKUGdvNSIsImFwcFR5cGUiOiJjdXN0b21lciJ9LCJpYXQiOjE3Mjg5MDgwMzksImV4cCI6MTczMTUwMzYzOX0.hivraWe9NgM7kkV04SsWvmzRf0LSpfC7E9F0MTLoOjQ",
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