package com.techrev.videocall.ui.utils;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;

public class AppPermission {

    private ArrayList<String> permissionsList;
    private ArrayList<String> mandatoryPermissionsList;
    private String[] permissionsStr = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE
    };
    private String[] mandatoryPermissionsStr;

    public AppPermission() {

        /*if (SDK_INT >= Build.VERSION_CODES.S){
            permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH_SCAN;
            permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH_CONNECT;
        }
        else {
            permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH;
        }

        if (SDK_INT >= Build.VERSION_CODES.M) {
            if (SDK_INT >= Build.VERSION_CODES.S) {
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.ACCESS_FINE_LOCATION;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH_SCAN;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH_CONNECT;
            }else if (SDK_INT >= Build.VERSION_CODES.O){
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.ACCESS_FINE_LOCATION;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH;
            }else{
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.ACCESS_COARSE_LOCATION;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH;
            }
        }
        else{
            permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.ACCESS_FINE_LOCATION;
            permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH;
        }*/

        // permissionsList = new ArrayList<>();
        // permissionsList.addAll(Arrays.asList(permissionsStr));

        //NEW Permission list:-
        permissionsList = new ArrayList<>();
        mandatoryPermissionsList = new ArrayList<>();
        ArrayList<String> tempPermissionsList = new ArrayList<>();
        if (SDK_INT >= Build.VERSION_CODES.M) {
            if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                //API level 33 and above
                tempPermissionsList.add(Manifest.permission.POST_NOTIFICATIONS);
                // tempPermissionsList.add(Manifest.permission.BLUETOOTH);
                tempPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                tempPermissionsList.add(Manifest.permission.BLUETOOTH_SCAN);
                tempPermissionsList.add(Manifest.permission.BLUETOOTH_CONNECT);
                tempPermissionsList.add(Manifest.permission.READ_MEDIA_AUDIO);
                tempPermissionsList.add(Manifest.permission.READ_MEDIA_IMAGES);
                tempPermissionsList.add(Manifest.permission.READ_MEDIA_VIDEO);
                tempPermissionsList.add(Manifest.permission.RECORD_AUDIO);
                tempPermissionsList.add(Manifest.permission.CAMERA);
                tempPermissionsList.add(Manifest.permission.CALL_PHONE);

                mandatoryPermissionsList.add(Manifest.permission.POST_NOTIFICATIONS);
                //mandatoryPermissionsList.add(Manifest.permission.BLUETOOTH);
                mandatoryPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                mandatoryPermissionsList.add(Manifest.permission.BLUETOOTH_SCAN);
                mandatoryPermissionsList.add(Manifest.permission.BLUETOOTH_CONNECT);
                mandatoryPermissionsList.add(Manifest.permission.READ_MEDIA_AUDIO);
                mandatoryPermissionsList.add(Manifest.permission.READ_MEDIA_IMAGES);
                mandatoryPermissionsList.add(Manifest.permission.READ_MEDIA_VIDEO);

                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.POST_NOTIFICATIONS;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.ACCESS_FINE_LOCATION;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH_SCAN;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH_CONNECT;

                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.READ_MEDIA_AUDIO;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.READ_MEDIA_IMAGES;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.READ_MEDIA_VIDEO;

                mandatoryPermissionsStr = new String[7];
                mandatoryPermissionsStr[0] = Manifest.permission.POST_NOTIFICATIONS;
                mandatoryPermissionsStr[1] = Manifest.permission.ACCESS_FINE_LOCATION;
                mandatoryPermissionsStr[2] = Manifest.permission.BLUETOOTH_SCAN;
                mandatoryPermissionsStr[3] = Manifest.permission.BLUETOOTH_CONNECT;
                mandatoryPermissionsStr[4] = Manifest.permission.READ_MEDIA_AUDIO;
                mandatoryPermissionsStr[5] = Manifest.permission.READ_MEDIA_IMAGES;
                mandatoryPermissionsStr[6] = Manifest.permission.READ_MEDIA_VIDEO;

              /*  permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.RECORD_AUDIO;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.CAMERA;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.CALL_PHONE;*/

            }
            else if (SDK_INT >= Build.VERSION_CODES.S) {
                //API Level 31(12), 32
                // tempPermissionsList.add(Manifest.permission.BLUETOOTH);
                tempPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                tempPermissionsList.add(Manifest.permission.BLUETOOTH_SCAN);
                tempPermissionsList.add(Manifest.permission.BLUETOOTH_CONNECT);
                tempPermissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                tempPermissionsList.add(Manifest.permission.RECORD_AUDIO);
                tempPermissionsList.add(Manifest.permission.CAMERA);
                tempPermissionsList.add(Manifest.permission.CALL_PHONE);

                mandatoryPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                mandatoryPermissionsList.add(Manifest.permission.BLUETOOTH_SCAN);
                mandatoryPermissionsList.add(Manifest.permission.BLUETOOTH_CONNECT);
                mandatoryPermissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

                /*permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH;*/
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.ACCESS_FINE_LOCATION;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH_SCAN;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH_CONNECT;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.READ_EXTERNAL_STORAGE;

                mandatoryPermissionsStr = new String[4];
                mandatoryPermissionsStr[0] = Manifest.permission.ACCESS_FINE_LOCATION;
                mandatoryPermissionsStr[1] = Manifest.permission.BLUETOOTH_SCAN;
                mandatoryPermissionsStr[2] = Manifest.permission.BLUETOOTH_CONNECT;
                mandatoryPermissionsStr[3] = Manifest.permission.READ_EXTERNAL_STORAGE;

               /* permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.RECORD_AUDIO;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.CAMERA;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.CALL_PHONE;*/

            }
            else if (SDK_INT >= Build.VERSION_CODES.R) {
                //API Level 30(11)
                tempPermissionsList.add(Manifest.permission.BLUETOOTH);
                tempPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                tempPermissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                tempPermissionsList.add(Manifest.permission.RECORD_AUDIO);
                tempPermissionsList.add(Manifest.permission.CAMERA);
                tempPermissionsList.add(Manifest.permission.CALL_PHONE);

                mandatoryPermissionsList.add(Manifest.permission.BLUETOOTH);
                mandatoryPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                mandatoryPermissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.ACCESS_FINE_LOCATION;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.READ_EXTERNAL_STORAGE;

                mandatoryPermissionsStr = new String[3];
                mandatoryPermissionsStr[0] = Manifest.permission.BLUETOOTH;
                mandatoryPermissionsStr[1] = Manifest.permission.ACCESS_FINE_LOCATION;
                mandatoryPermissionsStr[2] = Manifest.permission.READ_EXTERNAL_STORAGE;

               /* permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.RECORD_AUDIO;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.CAMERA;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.CALL_PHONE;*/

            }
            else if (SDK_INT >= Build.VERSION_CODES.O) {
                //API level 26,27,28,29
                tempPermissionsList.add(Manifest.permission.BLUETOOTH);
                tempPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                tempPermissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                tempPermissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                tempPermissionsList.add(Manifest.permission.RECORD_AUDIO);
                tempPermissionsList.add(Manifest.permission.CAMERA);
                tempPermissionsList.add(Manifest.permission.CALL_PHONE);

                mandatoryPermissionsList.add(Manifest.permission.BLUETOOTH);
                mandatoryPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                mandatoryPermissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                mandatoryPermissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.ACCESS_FINE_LOCATION;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.READ_EXTERNAL_STORAGE;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;

                mandatoryPermissionsStr = new String[4];
                mandatoryPermissionsStr[0] = Manifest.permission.BLUETOOTH;
                mandatoryPermissionsStr[1] = Manifest.permission.ACCESS_FINE_LOCATION;
                mandatoryPermissionsStr[2] = Manifest.permission.READ_EXTERNAL_STORAGE;
                mandatoryPermissionsStr[3] = Manifest.permission.WRITE_EXTERNAL_STORAGE;

                /*permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.RECORD_AUDIO;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.CAMERA;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.CALL_PHONE;*/
            }
            else {
                tempPermissionsList.add(Manifest.permission.BLUETOOTH);
                tempPermissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                tempPermissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                tempPermissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                tempPermissionsList.add(Manifest.permission.RECORD_AUDIO);
                tempPermissionsList.add(Manifest.permission.CAMERA);
                tempPermissionsList.add(Manifest.permission.CALL_PHONE);

                mandatoryPermissionsList.add(Manifest.permission.BLUETOOTH);
                mandatoryPermissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                mandatoryPermissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                mandatoryPermissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.ACCESS_COARSE_LOCATION;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.READ_EXTERNAL_STORAGE;
                permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;

                mandatoryPermissionsStr = new String[4];
                mandatoryPermissionsStr[0] = Manifest.permission.BLUETOOTH;
                mandatoryPermissionsStr[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
                mandatoryPermissionsStr[2] = Manifest.permission.READ_EXTERNAL_STORAGE;
                mandatoryPermissionsStr[3] = Manifest.permission.WRITE_EXTERNAL_STORAGE;

              /*  permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.RECORD_AUDIO;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.CAMERA;
                permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
                permissionsStr[permissionsStr.length - 1] = Manifest.permission.CALL_PHONE;*/
            }
        }
        else {
            tempPermissionsList.add(Manifest.permission.BLUETOOTH);
            tempPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            tempPermissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            tempPermissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            tempPermissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            tempPermissionsList.add(Manifest.permission.RECORD_AUDIO);
            tempPermissionsList.add(Manifest.permission.CAMERA);
            tempPermissionsList.add(Manifest.permission.CALL_PHONE);

            mandatoryPermissionsList.add(Manifest.permission.BLUETOOTH);
            mandatoryPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            mandatoryPermissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            mandatoryPermissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            mandatoryPermissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.BLUETOOTH;
            permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.ACCESS_FINE_LOCATION;
            permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            permissionsStr = Arrays.copyOf(permissionsStr, permissionsStr.length + 1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.READ_EXTERNAL_STORAGE;

            mandatoryPermissionsStr = new String[5];
            mandatoryPermissionsStr[0] = Manifest.permission.BLUETOOTH;
            mandatoryPermissionsStr[1] = Manifest.permission.ACCESS_FINE_LOCATION;
            mandatoryPermissionsStr[2] = Manifest.permission.ACCESS_COARSE_LOCATION;
            mandatoryPermissionsStr[3] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            mandatoryPermissionsStr[4] = Manifest.permission.READ_EXTERNAL_STORAGE;

           /* permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.RECORD_AUDIO;
            permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.CAMERA;
            permissionsStr = Arrays.copyOf(permissionsStr , permissionsStr.length+1);
            permissionsStr[permissionsStr.length - 1] = Manifest.permission.CALL_PHONE;*/
        }

        // permissionsList = new ArrayList<>();
        // permissionsList.addAll(tempPermissionsList);

        permissionsList.addAll(Arrays.asList(permissionsStr));
    }

    public ArrayList<String> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(ArrayList<String> permissionsList) {
        this.permissionsList = permissionsList;
    }

    public ArrayList<String> getMandatoryPermissionsList() {
        return mandatoryPermissionsList;
    }

    public void setMandatoryPermissionsList(ArrayList<String> mandatoryPermissionsList) {
        this.mandatoryPermissionsList = mandatoryPermissionsList;
    }

    public String[] getPermissionsStr() {
        return permissionsStr;
    }

    public void setPermissionsStr(String[] permissionsStr) {
        this.permissionsStr = permissionsStr;
    }

    public String[] getMandatoryPermissionsStr() {
        return mandatoryPermissionsStr;
    }

    public void setMandatoryPermissionsStr(String[] mandatoryPermissionsStr) {
        this.mandatoryPermissionsStr = mandatoryPermissionsStr;
    }
}
