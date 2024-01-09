package com.techrev.videocall;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * This class echoes a string called from JavaScript.
 */
public class TechRevVideoConferenceWrapper {

    /*public static CallbackContext callbackContext = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }else if (action.equals("startVideoConferenceRoom")) {
            this.startVideoConferenceRoom(args, callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void startVideoConferenceRoom(JSONArray args, CallbackContext callbackContext) throws JSONException {

        TechRevVideoConferenceWrapper.callbackContext=callbackContext;

        String token = args.getString(0);
        String roomName = args.getString(2);
        String authToken = args.getString(3);
        String baseURL = args.getString(4);
        String passcode=args.getString(5);
        Integer memberType=args.getInt(6);
        String userMeetingIdentifier=args.getString(7);

        String maxImageCaptureLimit=args.getString(9);
        String requestID = args.getString(11);
        int isRecordingEnabled=0;
        try{
            if(args.getString(12)!=null&&args.getString(12).length()>0){
                isRecordingEnabled= args.getInt(12);
            }
        }catch (Exception e)
        {
            Log.d("====Exc",""+e.toString());
            isRecordingEnabled=0;

        }
        String endDate = args.getString(13);
        String startDate = args.getString(14);
        String duration = args.getString(15);
        String userId = args.getString(16);
        boolean isCoSigner = args.getBoolean(17);
        String userDetails = args.getString(18);
        String requestParticipantId = args.getString(19);


        //Newly Added
        //String maxImageCaptureLimit=args.getString(8);
        Constants.API_BASE_URL = baseURL;
        Context context = cordova.getActivity().getApplicationContext();
        // Intent intent = new Intent(context, cordova_plugin_techrev_videoconference.VideoActivity.class);
        Intent intent = new Intent(context, cordova_plugin_techrev_videoconference.PermissionActivity.class);
        Log.d("===Args","Values:"+args.toString());
        Log.d("===Args","token:"+token);
        Log.d("===Args","roomName:"+roomName);
        Log.d("===Args","authToken:"+authToken);
        Log.d("===Args","baseURL:"+baseURL);
        Log.d("===Args","requestID:"+requestID);
        Log.d("===Args","passcode:"+passcode);

        Log.d("===Args","memberType:"+memberType);
        Log.d("===Args","userMeetingIdentifier:"+userMeetingIdentifier);
        Log.d("===Args","maxImageCaptureLimit:"+maxImageCaptureLimit);
        String clientId=args.getString(8);
        Log.d("===Args","clientId:"+clientId);
        Log.d("===Args","user details:"+userDetails);
        Log.d("===Args","request participant id: "+requestParticipantId);
        //Log.d("===Args","isRecordingEnabled:"+isRecordingEnabled);
        //Log.d("===Args","maxImageCaptureLimit:"+maxImageCaptureLimit);
        if(userMeetingIdentifier.endsWith("-screen"))
        {
            userMeetingIdentifier=userMeetingIdentifier.replace("-screen","");  
        }
        intent.putExtra("TOKEN", token);
        intent.putExtra("ROOM_NAME", roomName);
        intent.putExtra("AUTHORIZATION_TOKEN", authToken);
        intent.putExtra("REQUEST_ID", requestID);
        intent.putExtra("BASE_URL",baseURL);
        intent.putExtra("PASSCODE",passcode);
        //MaxImageCaptureLimit
        intent.putExtra("MAX_IMAGE_CAPTURE_LIMIT",maxImageCaptureLimit);
        if(memberType==0) {
            memberType=2;
        }
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


        this.cordova.getActivity().startActivity(intent);
    }*/
}