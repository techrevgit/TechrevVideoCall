package com.techrev.videocall;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class PermissionActivity extends Activity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 10001;
    private static final int MIC_PERMISSION_REQUEST_CODE = 10002;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10003;
    private static final int FILE_PERMISSION_REQUEST_CODE = 10004;
    private static final int REQUEST_PERMISSION_SETTING = 100025;
    private ViewGroup layoutFirst, layoutSecond;
    private Button btnAllow, btnCancel, btnSetting;
    private int memberType= 0;
    private TextView tvTitleMsg,tvBottomMsg;
    private AlertDialog askPermissionAlertDialog;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_permission);
        memberType= getIntent().getIntExtra("MEMBER_TYPE",0);
        if(Constants.isHostDisable && memberType==1){
            finish();
            return;
        }
        layoutFirst = findViewById(R.id.layoutFirst);
        layoutSecond = findViewById(R.id.layoutSecond);
        btnAllow = findViewById(R.id.btnAllow);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSetting = findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                }
            }
        });

        btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndOpenVideoActivity();
            }
        });
        tvTitleMsg=findViewById(R.id.tvTitleMsg);
        tvBottomMsg=findViewById(R.id.tvBottomMsg);
        TextView tvLocation=findViewById(R.id.tvLocation);
        if(memberType==2 && Constants.isLocationEnable){
            tvLocation.setVisibility(View.VISIBLE);
            tvTitleMsg.setText("Please allow Microphone, Camera & Location permissions from the phone settings to enable video call.");
            tvBottomMsg.setText("Please allow Microphone, Camera & Location permissions to join the video call.");
        }else{
            tvTitleMsg.setText("Please allow Microphone & Camera permissions from the phone settings to enable video call.");
            tvBottomMsg.setText("Please allow Microphone & Camera permissions to join the video call.");
        }

        builder = new AlertDialog.Builder(PermissionActivity.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        askPermissionAlertDialog = builder.create();

        if(!checkPermission(PermissionActivity.this, Manifest.permission.CAMERA)
                || !checkPermission(PermissionActivity.this, Manifest.permission.RECORD_AUDIO)
                || !checkPermission(PermissionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || !checkPermission(PermissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){

            checkAndOpenVideoActivity();

        }else {
            goToNext();
        }

    }

    private void checkAndOpenVideoActivity() {
        /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
         *//*LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_permission_layout, null);
            askPermissionAlertDialog.setView(dialogView);
            askPermissionAlertDialog.show();
            TextView tv_allow_access = dialogView.findViewById(R.id.allow_access);
            tv_allow_access.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askPermissionAlertDialog.dismiss();

                }
            });*//*

            try {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                startActivityForResult(intent, 1);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                startActivityForResult(intent, 1);
            }

        }else {

        }*/

        if (checkPermission(PermissionActivity.this, Manifest.permission.CAMERA)) {
            layoutFirst.setVisibility(View.VISIBLE);
            layoutSecond.setVisibility(View.GONE);
            if (checkPermission(PermissionActivity.this, Manifest.permission.RECORD_AUDIO)) {
                if(checkPermission(PermissionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    if(memberType == 2 && Constants.isLocationEnable){
                        if (checkPermission(PermissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            goToNext();
                        } else {
                            requestPermission(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    LOCATION_PERMISSION_REQUEST_CODE
                            );
                            /*if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                requestPermission(
                                        Manifest.permission.POST_NOTIFICATIONS,
                                        LOCATION_PERMISSION_REQUEST_CODE
                                );
                                requestPermission(
                                        Manifest.permission.BLUETOOTH,
                                        LOCATION_PERMISSION_REQUEST_CODE
                                );
                            }*/
                        }
                    }else{
                        goToNext();
                    }
                }else{
                    requestPermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            FILE_PERMISSION_REQUEST_CODE
                    );
                }
            } else {
                requestPermission(
                        Manifest.permission.RECORD_AUDIO,
                        MIC_PERMISSION_REQUEST_CODE
                );
            }
        } else {
            requestPermission(
                    Manifest.permission.CAMERA,
                    CAMERA_PERMISSION_REQUEST_CODE
            );
        }
    }

    private void goToNext() {
        Intent intent = new Intent(PermissionActivity.this, VideoActivity.class);
        intent.putExtras(getIntent().getExtras());
        startActivity(intent);
        finish();

    }


    private boolean checkPermission(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(
                PermissionActivity.this, new String[]{permission}, requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE
                || requestCode == MIC_PERMISSION_REQUEST_CODE
                || requestCode == LOCATION_PERMISSION_REQUEST_CODE
                || requestCode == FILE_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkAndOpenVideoActivity();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && !shouldShowRequestPermissionRationale(permissions[0])) {
                layoutFirst.setVisibility(View.GONE);
                layoutSecond.setVisibility(View.VISIBLE);
                btnSetting.setVisibility(View.VISIBLE);
                btnAllow.setVisibility(View.GONE);
                if(memberType==2 && Constants.isLocationEnable){
                    tvTitleMsg.setText("Please allow Microphone, Camera, File & Location permissions from the phone settings to enable video call.");
                }else{
                    tvTitleMsg.setText("Please allow Microphone & Camera permissions from the phone settings to enable video call.");
                }
            } else {
                layoutFirst.setVisibility(View.GONE);
                layoutSecond.setVisibility(View.VISIBLE);
                btnSetting.setVisibility(View.GONE);
                btnAllow.setVisibility(View.VISIBLE);
                if(memberType==2 && Constants.isLocationEnable){
                    tvTitleMsg.setText("Please allow Microphone, Camera, File & Location permissions to join the video call.");
                }else{
                    tvTitleMsg.setText("Please allow Microphone & Camera permissions to join the video call.");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_PERMISSION_SETTING == requestCode) {
            checkAndOpenVideoActivity();
        }
    }
}
