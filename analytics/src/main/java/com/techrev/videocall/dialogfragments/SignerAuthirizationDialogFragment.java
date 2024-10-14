package com.techrev.videocall.dialogfragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.techrev.videocall.R;


public class SignerAuthirizationDialogFragment extends DialogFragment {

    private Activity mActivity = null;
    private OnAuthorizationActionPerformed mActionPerformed;
    private CheckBox cb_authorization;
    private TextView tv_yes, tv_no;

    public interface OnAuthorizationActionPerformed {
        void onAuthorizationGiven();
        void onAuthorizationDenied();
    }

    public SignerAuthirizationDialogFragment() {
        // Required empty public constructor
    }

    public SignerAuthirizationDialogFragment(Activity activity, OnAuthorizationActionPerformed actionPerformed) {
        this.mActivity = activity;
        this.mActionPerformed = actionPerformed;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.dialog_fragment_signer_authorization, container, false);
        cb_authorization = mView.findViewById(R.id.cb_allow);
        tv_yes = mView.findViewById(R.id.tv_yes);
        tv_no = mView.findViewById(R.id.tv_no);
        return mView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cb_authorization.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tv_yes.setBackground(mActivity.getDrawable(R.drawable.primary_colored_round_corner_background));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tv_yes.setTextColor(mActivity.getColor(R.color.white));
                    }
                } else {
                    tv_yes.setBackground(mActivity.getDrawable(R.drawable.gray_colored_round_corner_background));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tv_yes.setTextColor(mActivity.getColor(R.color.black));
                    }
                }
            }
        });

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_authorization.isChecked()) {
                    mActionPerformed.onAuthorizationGiven();
                    dismiss();
                } else {
                    Toast.makeText(mActivity, "Please accept the consent", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionPerformed.onAuthorizationDenied();
                dismiss();
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