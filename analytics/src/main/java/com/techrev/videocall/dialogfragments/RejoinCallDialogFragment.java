package com.techrev.videocall.dialogfragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.techrev.videocall.R;

/**
 * Created by Rupesh Kumar Jena on 11-10-2024.
 */
public class RejoinCallDialogFragment extends DialogFragment {

    private final String TAG = "RetryLoginDialogFragmen";
    private Activity mActivity;
    private DialogStateInterface dialogStateInterface;
    private ImageView iv_cross;
    private TextView tvMessage1, tvMessage2, tvBtnLeaveCall;
    private String message1, message2;

    public interface DialogStateInterface {
        void onDialogClosed(boolean isClosed);
    }

    public RejoinCallDialogFragment() {

    }

    @SuppressLint("ValidFragment")
    public RejoinCallDialogFragment(Activity activity, DialogStateInterface dialogStateInterface) {
        this.mActivity = activity;
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
        setCancelable(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.MATCH_PARENT);
        View mView = inflater.inflate(R.layout.retry_login_dialog_layout, container, false);
        iv_cross = mView.findViewById(R.id.iv_cross);
        tvMessage1 = mView.findViewById(R.id.tvMessage1);
        tvMessage2 = mView.findViewById(R.id.tvMessage2);
        tvBtnLeaveCall = mView.findViewById(R.id.tvBtnLeaveCall);

        return mView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        message1 = "Click on Leave this call button below";
        message2 = "After leaving the call, click on Join Call Now button of your ongoing request to rejoin the Notarization";

        // First string and part to make bold
        String message1 = "Click on Leave this call button below";
        SpannableString spannableString1 = new SpannableString(message1);
        int start1 = message1.indexOf("Leave this call");
        int end1 = start1 + "Leave this call".length();
        spannableString1.setSpan(new StyleSpan(Typeface.BOLD), start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Second string and part to make bold
        String message2 = "After leaving the call, click on Join Call Now button of your ongoing request to rejoin the Notarization";
        SpannableString spannableString2 = new SpannableString(message2);
        int start2 = message2.indexOf("Join Call Now");
        int end2 = start2 + "Join Call Now".length();
        spannableString2.setSpan(new StyleSpan(Typeface.BOLD), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvMessage1.setText(spannableString1);
        tvMessage2.setText(spannableString2);

        iv_cross.setOnClickListener(view1 -> {
            mActivity.getFragmentManager().popBackStack();
            dialogStateInterface.onDialogClosed(true);
        });

        tvBtnLeaveCall.setOnClickListener(view1 -> {
            mActivity.getFragmentManager().popBackStack();
            dialogStateInterface.onDialogClosed(true);
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
