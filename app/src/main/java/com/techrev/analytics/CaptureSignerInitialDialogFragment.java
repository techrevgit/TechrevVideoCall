package com.techrev.analytics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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


import org.json.JSONObject;

public class CaptureSignerInitialDialogFragment extends DialogFragment {

    private String authToken, requestId;
    private Activity mActivity;
    private ImageView iv_cross, iv_camera, iv_whiteboard;
    private LinearLayout ll_capture_through_camera, ll_capture_through_whiteboard;
    private TextView tv_click_here;
    private String userMeetingIdentifier = "";
    private VideoCallModel videoCallModel;
    private OptionSelectionInterface optionSelectionInterface;

    public interface OptionSelectionInterface {
        public void onOptionSelected(int selectedOption);
    }

    public CaptureSignerInitialDialogFragment() {

    }

    @SuppressLint("ValidFragment")
    public CaptureSignerInitialDialogFragment(Activity activity, String meetingIdetifier, VideoCallModel  model, String authToken, String requestID, OptionSelectionInterface selectionInterface) {
        this.mActivity = activity;
        this.userMeetingIdentifier = meetingIdetifier;
        this.videoCallModel = model;
        this.authToken = authToken;
        this.requestId = requestID;
        this.optionSelectionInterface = selectionInterface;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.MATCH_PARENT);
        View mView = inflater.inflate(R.layout.dialog_fragment_signer_initial, container, false);
        iv_cross = mView.findViewById(R.id.iv_cross);
        ll_capture_through_camera = mView.findViewById(R.id.ll_capture_through_camera);
        ll_capture_through_whiteboard = mView.findViewById(R.id.ll_capture_through_whiteboard);
        tv_click_here = mView.findViewById(R.id.tv_click_here);

        return mView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.getFragmentManager().popBackStack();
            }
        });

        ll_capture_through_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionSelectionInterface.onOptionSelected(1);
                /*DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Toast.makeText(mActivity, "Opening camera", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(mActivity, CameraActivity.class);
                                it.putExtra("REQUEST_ID" , requestId);
                                it.putExtra("AUTH_TOKEN" , authToken);
                                mActivity.startActivity(it);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                AlertDialog dialog = builder.setTitle("Capture Initial")
                        .setMessage("Are you sure, you want to capture your initial?")
                        .setPositiveButton("Ok, Proceed", dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener)
                        .show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mActivity.getColor(R.color.color_primary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mActivity.getColor(R.color.red));*/
            }
        });

        ll_capture_through_whiteboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionSelectionInterface.onOptionSelected(2);
                /*DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Toast.makeText(mActivity, "Opening camera", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(mActivity, WhiteBoardActivity.class);
                                it.putExtra("REQUEST_ID" , requestId);
                                it.putExtra("AUTH_TOKEN" , authToken);
                                mActivity.startActivity(it);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                AlertDialog dialog = builder.setTitle("Draw Initial")
                        .setMessage("Are you sure, you want to draw your initial?")
                        .setPositiveButton("Ok, Proceed", dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener)
                        .show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mActivity.getColor(R.color.color_primary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mActivity.getColor(R.color.red));*/
            }
        });

        tv_click_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog dialog = new ProgressDialog(mActivity);
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("from", userMeetingIdentifier);
                    jsonObject.put("to", "All");
                    jsonObject.put("messageType", "AcceptedToAuthorizeCaptureMyInitial");
                    jsonObject.put("content", "AcceptedToAuthorizeCaptureMyInitial");
                    videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                } catch (Exception e) {
                    Log.d("====Exception", "" + e.toString());
                }finally {
                    dialog.dismiss();
                    dismiss();
                }

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

}
