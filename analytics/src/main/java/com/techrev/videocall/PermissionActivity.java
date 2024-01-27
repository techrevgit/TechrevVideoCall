package com.techrev.videocall;

import static android.os.Build.VERSION.SDK_INT;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class PermissionActivity extends AppCompatActivity {

    /*
     * Created by Rupesh to handle all app permissions at one place and
     * Modified by Tuneer to ask Permission According to Android API like API level 31,32,33.
     */

    private static final String TAG = "PermissionActivity";
    private final AppPermission appPermission = new AppPermission();
    int ACTIVITY_SETTING_RESULT_CODE = 191;
    boolean allAppPermissionsGranted = true;
    AlertDialog.Builder  lmdCommonDialog;
    Dialog alertDialogExits;
    String loginBy ="";
    private AlertDialog alertDialog;
    private int permissionsCount = 0;
    private String DENIED_PERMISSIONS = "";
    private Map<String, Boolean> permissionResults;
    private TextView button_continue, button_goToNext;

    private LinearLayout notification_layout;
    private ArrayList<String> permissionsList;
    private CheckBox checkFile, checkLocation, checkNotification, checkMicrophone, checkCamera, checkPhone;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);*/
        setContentView(R.layout.activity_permission);

        /*Handle Permissions*/
        permissionsList = new ArrayList<String>();
        if (SDK_INT >= Build.VERSION_CODES.M) {
            if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                //API level 33 and above
                permissionsList.add(Manifest.permission.POST_NOTIFICATIONS);
                permissionsList.add(Manifest.permission.BLUETOOTH);
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                permissionsList.add(Manifest.permission.BLUETOOTH_SCAN);
                permissionsList.add(Manifest.permission.BLUETOOTH_CONNECT);
                permissionsList.add(Manifest.permission.READ_MEDIA_AUDIO);
                permissionsList.add(Manifest.permission.READ_MEDIA_IMAGES);
                permissionsList.add(Manifest.permission.READ_MEDIA_VIDEO);
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
                permissionsList.add(Manifest.permission.CAMERA);
                permissionsList.add(Manifest.permission.CALL_PHONE);
            }
            else if (SDK_INT >= Build.VERSION_CODES.S) {
                //API Level 31(12), 32
                // permissionsList.add(Manifest.permission.BLUETOOTH);
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                permissionsList.add(Manifest.permission.BLUETOOTH_SCAN);
                permissionsList.add(Manifest.permission.BLUETOOTH_CONNECT);
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
                permissionsList.add(Manifest.permission.CAMERA);
                permissionsList.add(Manifest.permission.CALL_PHONE);
            }
            else if (SDK_INT >= Build.VERSION_CODES.R) {
                //API Level 30(11)
                permissionsList.add(Manifest.permission.BLUETOOTH);
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
                permissionsList.add(Manifest.permission.CAMERA);
                permissionsList.add(Manifest.permission.CALL_PHONE);
            } else if (SDK_INT >= Build.VERSION_CODES.O) {
                //API level 26,27,28,29
                permissionsList.add(Manifest.permission.BLUETOOTH);
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
                permissionsList.add(Manifest.permission.CAMERA);
                permissionsList.add(Manifest.permission.CALL_PHONE);
            } else {
                permissionsList.add(Manifest.permission.BLUETOOTH);
                permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
                permissionsList.add(Manifest.permission.CAMERA);
                permissionsList.add(Manifest.permission.CALL_PHONE);
            }
        }
        else {
            permissionsList.add(Manifest.permission.BLUETOOTH);
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionsList.add(Manifest.permission.RECORD_AUDIO);
            permissionsList.add(Manifest.permission.CAMERA);
            permissionsList.add(Manifest.permission.CALL_PHONE);
        }
        /*Handle Permissions*/

        notification_layout = findViewById(R.id.notification_layout);
        button_continue = findViewById(R.id.button_continue);

        button_goToNext = findViewById(R.id.button_goToNext);

        checkFile = findViewById(R.id.checkFile);
        checkLocation = findViewById(R.id.checkLocation);
        checkNotification = findViewById(R.id.checkNotification);
        checkMicrophone = findViewById(R.id.checkMicrophone);
        checkCamera = findViewById(R.id.checkCamera);
        checkPhone = findViewById(R.id.checkPhone);

        checkMicrophone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    permissionsList.add(Manifest.permission.RECORD_AUDIO);
                } else {
                    permissionsList.remove(Manifest.permission.RECORD_AUDIO);
                }
                appPermission.setPermissionsList(permissionsList);
            }
        });

        checkCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    permissionsList.add(Manifest.permission.CAMERA);
                } else {
                    permissionsList.remove(Manifest.permission.CAMERA);
                }
                appPermission.setPermissionsList(permissionsList);
            }
        });

        checkPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    permissionsList.add(Manifest.permission.CALL_PHONE);
                } else {
                    permissionsList.remove(Manifest.permission.CALL_PHONE);
                }
                appPermission.setPermissionsList(permissionsList);
            }
        });

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                askForPermissions(appPermission.getPermissionsList());
            }
        });

        button_goToNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                goToNext();
            }
        });

//        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            notification_layout.setVisibility(View.VISIBLE);
//        }
        // Log.d(TAG, "onCreate: -----------------fristtime Permission---------: " + new Gson().toJson(appPermission.getPermissionsList()));

        // adding onbackpressed callback listener.
        // This callback will only be called when MyFragment is at least Started.

    }

    @Override
    protected void onResume() {

        for (int i = 0; i < appPermission.getMandatoryPermissionsList().size(); i++) {
            // Log.d(TAG, "onResume: -------------------------" + new Gson().toJson(appPermission.getMandatoryPermissionsList().get(i)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!hasPermission(PermissionActivity.this, appPermission.getMandatoryPermissionsStr()[i])) {
                    Log.d(TAG, "onResume: ------NOT GRANTED----- " + appPermission.getMandatoryPermissionsStr()[i]);
                    allAppPermissionsGranted = false;
                    break;
                }
            }
        }
        if (allAppPermissionsGranted) {
            goToNext();
        }
        super.onResume();
    }

    private boolean hasPermission(Context context, String permissionStr) {
        return ContextCompat.checkSelfPermission(context, permissionStr) == PackageManager.PERMISSION_GRANTED;
    }

    private void askForPermissions(ArrayList<String> permissionsList) {
        String[] newPermissionStr = new String[permissionsList.size()];
        for (int i = 0; i < newPermissionStr.length; i++) {
            newPermissionStr[i] = permissionsList.get(i);
        }
        if (newPermissionStr.length > 0) {
            permissionsLauncher.launch(newPermissionStr);
        } else {
        /* User has pressed 'Deny & Don't ask again' so we have to show the enable permissions dialog
        which will lead them to app details page to enable permissions from there. */
            showPermissionDialog();
        }
    }

    private void goToNext() {
        Intent it = getIntent();
        if (it != null && it.hasExtra("API_BASE_URL")) {
            Log.d(TAG , "BASE_URL_VAL: "+ it.getStringExtra("API_BASE_URL"));
            Constants.API_BASE_URL = it.getStringExtra("API_BASE_URL");
        }
        Intent intent = new Intent(PermissionActivity.this, VideoActivity.class);
        intent.putExtras(it);
        startActivity(intent);
        finish();

    }

    private void showPermissionDialog() {
        String PERMISSION_ALERT_MESSAGE_COMMON = "To use features of video call please allow all required permissions to  app from your phone settings.\n" +
                "\n" +
                "Go to settings, find and select Permissions, choose " + DENIED_PERMISSIONS + " permission(s) and allow them respectively. Choose following options only \"Allow only while using the app\", \"Allow\", \"Allow access to media only\".";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission required")
                // .setMessage(getPermissionString(getString(R.string.permission_KYC)))
                .setMessage(PERMISSION_ALERT_MESSAGE_COMMON)
                .setPositiveButton("Settings", (dialog, which) -> {
                    dialog.dismiss();
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        startActivityForResult(intent, ACTIVITY_SETTING_RESULT_CODE);
                    } catch (Exception e) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        startActivityForResult(intent, ACTIVITY_SETTING_RESULT_CODE);
                    }
                });
        if (alertDialog == null) {
            alertDialog = builder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        } else {
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }


    public String getPermissionString(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            return String.valueOf(Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY));
        } else {
            return String.valueOf(Html.fromHtml(source));
        }
    }

    private void askPermissionToInstallUnknownApps() {
        //Handling installation permission
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission required")
                .setMessage("Please click on the Settings button below and grant the permission to install updates automatically.")
                .setPositiveButton("Settings", (dialog, which) -> {
                    dialog.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (!getPackageManager().canRequestPackageInstalls()) {
                            startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                                    .setData(Uri.parse(String.format("package:%s", getPackageName()))), 1);
                        }
                    }
                });
        if (alertDialog == null) {
            alertDialog = builder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }

    ActivityResultLauncher<String[]> permissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            permissionResults = result;
            ArrayList<Boolean> list = new ArrayList<>(result.values());
            ArrayList permissionsList = new ArrayList<>();
            permissionsCount = 0;
            for (int i = 0; i < appPermission.getMandatoryPermissionsList().size(); i++) {
                if (shouldShowRequestPermissionRationale(appPermission.getMandatoryPermissionsStr()[i])) {
                    Log.d(TAG, "onActivityResult: shouldshowRequest: " + appPermission.getMandatoryPermissionsStr()[i]);
                    permissionsList.add(appPermission.getMandatoryPermissionsStr()[i]);
                } else if (!hasPermission(PermissionActivity.this, appPermission.getMandatoryPermissionsStr()[i])) {
                    try {
                        String permission_name = "";
                        if (appPermission.getMandatoryPermissionsStr()[i].contains("ACCESS_FINE_LOCATION") ||
                                appPermission.getMandatoryPermissionsStr()[i].contains("ACCESS_COARSE_LOCATION")) {
                            permission_name = "Location";
                        } else if (appPermission.getMandatoryPermissionsStr()[i].contains("READ_MEDIA_AUDIO") ||
                                appPermission.getMandatoryPermissionsStr()[i].contains("READ_MEDIA_IMAGES") ||
                                appPermission.getMandatoryPermissionsStr()[i].contains("READ_MEDIA_VIDEO") ||
                                appPermission.getMandatoryPermissionsStr()[i].contains("READ_EXTERNAL_STORAGE") ||
                                appPermission.getMandatoryPermissionsStr()[i].contains("WRITE_EXTERNAL_STORAGE")) {
                            permission_name = "Files and Media";
                        } else if (appPermission.getMandatoryPermissionsStr()[i].contains("POST_NOTIFICATIONS")) {
                            permission_name = "Notification";
                        }

                        if (DENIED_PERMISSIONS.equals("")) {
                            if (!DENIED_PERMISSIONS.contains(permission_name)) {
                                DENIED_PERMISSIONS = permission_name;
                            }
                        } else {
                            if (!DENIED_PERMISSIONS.contains(permission_name)) {
                                DENIED_PERMISSIONS += ", " + permission_name;
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    permissionsCount++;
                }
            }
            if (permissionsList.size() > 0) {
                //Some permissions are denied and can be asked again.
                lmdCommonDialog = new AlertDialog.Builder(PermissionActivity.this);
                lmdCommonDialog.setTitle("Alert")
                        .setMessage("You have denied some permissions. To use application further you need to allow all permission asked on this page")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked Yes
                                // Handle positive response here
                                dialog.dismiss();
                                askForPermissions(permissionsList);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked No
                                // Handle negative response here
                                dialog.dismiss();
                            }
                        })
                        .show();


            }
            else if (permissionsCount > 0) {
                //Show alert dialog
                showPermissionDialog();
            } else {
                //All permissions granted. We are good to go ðŸ¤ž
                //askPermissionToInstallUnknownApps();
                goToNext();
            }
        }
    });




}

