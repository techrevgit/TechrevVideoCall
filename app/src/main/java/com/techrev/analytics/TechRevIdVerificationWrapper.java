package com.techrev.analytics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 * Created by Rupesh
 */
public class TechRevIdVerificationWrapper {

    /*public static CallbackContext callbackContext = null;
    private static final String TAG = "TechRevIdVerificationWr";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolIdVerificationMethod")) {
            String message = args.getString(0);
            this.coolIdVerificationMethod(message, callbackContext);
            return true;
        }else if (action.equals("startIdVerification")) {
            this.startIdVerification(args, callbackContext);
            return true;
        }
        return false;
    }

    private void coolIdVerificationMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void startIdVerification(JSONArray args, CallbackContext callbackContext) throws JSONException {

        TechRevIdVerificationWrapper.callbackContext=callbackContext;
        try {
            Log.d(TAG , "ID VERIFICATION PARAMS: "+args);
            JSONObject obj = new JSONObject((String) args.get(0));
            String personaURL = obj.getString("personaURL");
            String inquiryTemplateId = obj.getString("inquiryTemplateId");
            String isUseSandBox = obj.getString("isUseSandBox");
            String accountId = obj.getString("accountId");
            String redirectURL = obj.getString("redirectURL");
            String userId = obj.getString("userId");

            Log.d(TAG , "*personaURL* "+personaURL);
            Log.d(TAG , "*inquiryTemplateId* "+inquiryTemplateId);
            Log.d(TAG , "*isUseSandBox* "+isUseSandBox);
            Log.d(TAG , "*accountId* "+accountId);
            Log.d(TAG , "*redirectURL* "+redirectURL);
            Log.d(TAG , "*userId* "+userId);

            Context context = cordova.getActivity().getApplicationContext();

            Intent it = new Intent(context , IDVerificationActivity.class);
            it.putExtra("personaURL" , personaURL);
            it.putExtra("inquiryTemplateId" , inquiryTemplateId);
            it.putExtra("isUseSandBox" , isUseSandBox);
            it.putExtra("accountId" , accountId);
            it.putExtra("redirectURL" , redirectURL);
            it.putExtra("userId" , userId);
            this.cordova.getActivity().startActivity(it);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public TechRevIdVerificationWrapper getTechRevIdVerificationWrapperInstance(){
        return new TechRevIdVerificationWrapper();
    }

    public void notifyStatusToTheWeb(int status, String userId){
        if (status == 1){
            callbackContext.success(userId);
        }else if (status == 0){
            callbackContext.error("Something went wrong!");
        }
    }*/
}