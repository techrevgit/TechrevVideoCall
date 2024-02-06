package com.techrev.videocall;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.techrev.videocall.ui.MainActivity;

public class TechrevVideoCallManager {

    public static void connectToRoom(Context context, String baseURL, String token, String roomName, String authToken,
                                     String passcode, int memberType, String userMeetingIdentifier, String maxImageCaptureLimit,
                                     String requestID, int isRecordingEnabled, String endDate, String startDate, String duration,
                                     String userId, boolean isCoSigner, String userDetails, String requestParticipantId, String clientId) {
        //Toast.makeText(context, "You are now connected to room successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.d("===Args","baseURL:"+baseURL);
        Log.d("===Args","token:"+token);
        Log.d("===Args","roomName:"+roomName);
        Log.d("===Args","authToken:"+authToken);
        Log.d("===Args","baseURL:"+baseURL);
        Log.d("===Args","requestID:"+requestID);
        Log.d("===Args","passcode:"+passcode);

        Log.d("===Args","memberType:"+memberType);
        Log.d("===Args","userMeetingIdentifier:"+userMeetingIdentifier);
        Log.d("===Args","maxImageCaptureLimit:"+maxImageCaptureLimit);
        Log.d("===Args","clientId:"+clientId);
        Log.d("===Args","user details:"+userDetails);
        Log.d("===Args","request participant id: "+requestParticipantId);
        //Log.d("===Args","isRecordingEnabled:"+isRecordingEnabled);
        //Log.d("===Args","maxImageCaptureLimit:"+maxImageCaptureLimit);
        if(userMeetingIdentifier.endsWith("-screen"))
        {
            userMeetingIdentifier=userMeetingIdentifier.replace("-screen","");
        }
        intent.putExtra("API_BASE_URL", baseURL);
        intent.putExtra("TOKEN", token);
        intent.putExtra("ROOM_NAME", roomName);
        intent.putExtra("AUTHORIZATION_TOKEN", authToken);
        intent.putExtra("REQUEST_ID", requestID);
        intent.putExtra("BASE_URL",baseURL);
        intent.putExtra("PASSCODE",passcode);
        //MaxImageCaptureLimit
        intent.putExtra("MAX_IMAGE_CAPTURE_LIMIT",maxImageCaptureLimit);
        intent.putExtra("MEMBER_TYPE",memberType);
        intent.putExtra("USER_MEETING_IDENTIFIER",userMeetingIdentifier);
        intent.putExtra("CLIENT_ID",clientId);
        // For RECORDING_ENABLED
        intent.putExtra("RECORDING_ENABLED",isRecordingEnabled);
        intent.putExtra("MEETING_START_DATE",startDate);
        intent.putExtra("MEETING_END_DATE",endDate);
        intent.putExtra("MEETING_DURATION",duration);
        intent.putExtra("USER_ID",userId);
        intent.putExtra("IS_CO_SIGNER",isCoSigner);
        intent.putExtra("USER_DETAILS",userDetails);
        intent.putExtra("REQUEST_PARTICIPANT_ID",requestParticipantId);

        context.startActivity(intent);
    }

}
