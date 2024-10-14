package com.techrev.videocall.ui.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.techrev.videocall.models.NotarizationActionModel;
import com.techrev.videocall.network.NetworkInterface;
import com.techrev.videocall.network.RetrofitNetworkClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotarizationActionUpdateManger {

    private static final String TAG = NotarizationActionUpdateManger.class.getName();
    private static RetrofitNetworkClass networkClass = new RetrofitNetworkClass();
    private static Retrofit retrofitLocal = networkClass.callingURL();
    private static NetworkInterface serviceLocal = retrofitLocal.create(NetworkInterface.class);

    private NotarizationActionUpdateManger() {
        // Private constructor to prevent instantiation
    }

    public static void updateNotarizationAction(Context context,
                                                 String authToken,
                                                 String requestId,
                                                 String serviceProviderId,
                                                 String applicationUserId,
                                                 String isPrimary,
                                                 String actionId,
                                                 String type,
                                                 String docid,
                                                 String pageNumber) {
        // Show loading indicator
        /*ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();*/
        try {
            Log.e(TAG , "Request ID: "+requestId);
            //String actionId = getActionIdByDescription(actionName);
            Log.d(TAG , "ACTION ID: "+actionId);
            Log.d(TAG , "isPrimary: "+isPrimary);
            JSONObject obj = new JSONObject();
            obj.put("requestId" , requestId);
            obj.put("serviceProviderId" , serviceProviderId);
            obj.put("applicationUserId" , applicationUserId);
            obj.put("isPrimary" , isPrimary);
            obj.put("actionId" , actionId);
            obj.put("type" , type);
            obj.put("docid" , docid);
            obj.put("pageNumber" , pageNumber);

            String data = obj.toString();
            Call<ResponseBody> responseBodyCall = serviceLocal.insertNotarizationAuditTrial(authToken, data);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    /*if(dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }*/
                    if(response.isSuccessful() && response.body() != null){
                        Log.d(TAG , "Notarization action updated successfully");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    /*if(dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }*/
                    Log.d(TAG, "Failure in Notarization action update response:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Method to get actionId based on actionName or actionDescription
    private static String getActionIdByDescription(String description) {
        List<NotarizationActionModel.NotarizationActions> actions = NotarizationActionModel.getInstance().getNotarizationActions();
        Log.d(TAG , "Notarization Actions: "+new Gson().toJson(actions));
        if (actions != null) {
            for (NotarizationActionModel.NotarizationActions action : actions) {
                if (action.getActionDescription() != null && action.getActionDescription().equals(description)) {
                    Log.d(TAG , "Action ID for Action Name: "+action+" is "+action.getActionId());
                    return action.getActionId();
                }
            }
        }
        return ""; // Return null if no action with the provided description is found
    }
}