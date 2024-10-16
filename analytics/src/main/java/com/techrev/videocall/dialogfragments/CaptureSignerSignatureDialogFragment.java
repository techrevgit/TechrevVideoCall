package com.techrev.videocall.dialogfragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.techrev.videocall.R;
import com.techrev.videocall.models.CommonModel;
import com.techrev.videocall.models.VideoCallModel;
import com.techrev.videocall.network.NetworkInterface;
import com.techrev.videocall.network.RetrofitNetworkClass;
import com.techrev.videocall.utils.NotarizationActionUpdateManger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CaptureSignerSignatureDialogFragment extends DialogFragment {

    private static final String TAG = "CaptureSignerSignature";
    private String authToken, requestID, userID, isPrimarySigner;
    private Activity mActivity;
    private ImageView iv_cross, iv_camera, iv_whiteboard;
    private LinearLayout ll_capture_through_camera, ll_capture_through_whiteboard;
    private TextView tv_click_here;
    private String userMeetingIdentifier = "";
    private VideoCallModel videoCallModel;
    private OptionSelectionInterface optionSelectionInterface;
    private DialogStateInterface dialogStateInterface;
    static RetrofitNetworkClass networkClass = new RetrofitNetworkClass();
    static Retrofit retrofitLocal = networkClass.callingURL();
    static NetworkInterface serviceLocal = retrofitLocal.create(NetworkInterface.class);

    public interface OptionSelectionInterface {
        public void onOptionSelected(int selectedOption);
    }

    public interface DialogStateInterface {
        void onDialogClosed(boolean isClosed);
    }

    public CaptureSignerSignatureDialogFragment() {

    }

    @SuppressLint("ValidFragment")
    public CaptureSignerSignatureDialogFragment(Activity activity, String meetingIdetifier, VideoCallModel  model, String authToken, String requestId, String userId, String isPrimarySigner, OptionSelectionInterface selectionInterface, DialogStateInterface dialogStateInterface) {
        this.mActivity = activity;
        this.userMeetingIdentifier = meetingIdetifier;
        this.videoCallModel = model;
        this.authToken = authToken;
        this.requestID = requestId;
        this.userID = userId;
        this.isPrimarySigner = isPrimarySigner;
        this.optionSelectionInterface = selectionInterface;
        this.dialogStateInterface = dialogStateInterface;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.MATCH_PARENT);
        View mView = inflater.inflate(R.layout.dialog_fragment_signer_signature, container, false);
        iv_cross = mView.findViewById(R.id.iv_cross);
        ll_capture_through_camera = mView.findViewById(R.id.ll_capture_through_camera);
        ll_capture_through_whiteboard = mView.findViewById(R.id.ll_capture_through_whiteboard);
        tv_click_here = mView.findViewById(R.id.tv_click_here_signature);

        return mView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.getFragmentManager().popBackStack();
                dialogStateInterface.onDialogClosed(true);
            }
        });

        ll_capture_through_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                optionSelectionInterface.onOptionSelected(1);

                /*AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                AlertDialog dialog = builder.setTitle("Capture Signature")
                        .setMessage("Are you sure, you want to capture your signature?")
                        .setPositiveButton("Ok, Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(mActivity, "Opening camera", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(mActivity, CameraActivity.class);
                                it.putExtra("REQUEST_ID" , requestId);
                                it.putExtra("AUTH_TOKEN" , authToken);
                                mActivity.startActivity(it);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mActivity.getColor(R.color.color_primary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mActivity.getColor(R.color.red));*/

                /*new KAlertDialog(mActivity, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Capture Signature")
                        .setContentText("Are you sure, you want to capture your signature?")
                        .confirmButtonColor(R.color.color_primary)
                        .setConfirmClickListener("Ok, Proceed",new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                Toast.makeText(mActivity, "Opening camera", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(mActivity, CameraActivity.class);
                                mActivity.startActivity(it);
                            }
                        })
                        .setCancelClickListener("Cancel", new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();*/

            }
        });

        ll_capture_through_whiteboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                optionSelectionInterface.onOptionSelected(2);

                /*AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                AlertDialog dialog = builder.setTitle("Draw Signature")
                        .setMessage("Are you sure, you want to draw your signature?")
                        .setPositiveButton("Ok, Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(mActivity, "Opening whiteboard", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(mActivity, WhiteBoardActivity.class);
                                it.putExtra("REQUEST_ID" , requestId);
                                it.putExtra("AUTH_TOKEN" , authToken);
                                mActivity.startActivity(it);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mActivity.getColor(R.color.color_primary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mActivity.getColor(R.color.red));*/

                /*new KAlertDialog(mActivity, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Draw Signature")
                        .setContentText("Are you sure, you want to draw your signature?")
                        .setConfirmClickListener("Ok, Proceed",new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                Toast.makeText(mActivity, "Opening whiteboard", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(mActivity, WhiteBoardActivity.class);
                                mActivity.startActivity(it);
                            }
                        })
                        .setCancelClickListener("Cancel", new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();*/

            }
        });

        tv_click_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Authorize Notary to Capture Signature");
                builder.setCancelable(false);
                builder.setMessage("I am unable to capture my signature so I authorize Notary of eNotary On Call to capture on my behalf.");
                builder.setPositiveButton("I Authorize", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            updateRequestParticipantCapture(requestID, userID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                          dialogInterface.cancel();
                      }
                });
                builder.show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean setFullScreen = false;
        if (getArguments() != null) {
            setFullScreen = getArguments().getBoolean("fullScreen");
        }

        if (setFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @SuppressLint("StaticFieldLeak")
    private void updateRequestParticipantCapture(String meetingId, String userId) throws JSONException {
        Log.d(TAG , "Thread Name in updateRequestParticipantCapture: "+Thread.currentThread().getName());
        ProgressDialog dialog = new ProgressDialog(mActivity);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();
            }
        });
        Log.e(TAG, "Meeting ID: " + meetingId);
        JSONObject obj = new JSONObject();
        obj.put("requestId", meetingId);
        obj.put("userId", userId);
        /*obj.put("updateCapture", "1");*/
        obj.put("isSignature", "1");
        String data = obj.toString();

        // Wrap your network operations in an AsyncTask
        new AsyncTask<String, Void, CommonModel>() {
            @Override
            protected CommonModel doInBackground(String... params) {
                try {
                    Call<CommonModel> responseBodyCall = serviceLocal.updateRequestParticipantCapture(authToken, params[0]);
                    Response<CommonModel> response = responseBodyCall.execute();
                    if (response.isSuccessful()) {
                        return response.body();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(CommonModel result) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("from", userMeetingIdentifier);
                    jsonObject.put("to", "All");
                    jsonObject.put("messageType", "AcceptedToAuthorizeCaptureMySignature");
                    jsonObject.put("content", "AcceptedToAuthorizeCaptureMySignature");
                    videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());

                    NotarizationActionUpdateManger.updateNotarizationAction(
                            mActivity, authToken,
                            requestID, "", userId, isPrimarySigner,
                            "23", "1", "", "");

                } catch (Exception e) {
                    Log.d("====Exception", "" + e.toString());
                }finally {
                    dismiss();
                }

                if (result != null) {
                    Log.e(TAG, "Signer Signature/Initial Authorization Update Response Data: \n" + new Gson().toJson(result));
                }
            }
        }.execute(data);
    }

}
