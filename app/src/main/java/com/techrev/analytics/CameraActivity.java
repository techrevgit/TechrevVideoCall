package com.techrev.analytics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    private Activity mActivity = null;
    private ImageView iv_cross, iv_preview;
    private LinearLayout ll_capture_section;
    private TextView tv_edit , tv_save;
    private final int REQUEST_CAMERA_CODE = 100;
    private final int CAMERA_REQUEST = 101;
    static RetrofitNetworkClass networkClass = new RetrofitNetworkClass();
    static Retrofit retrofitLocal = networkClass.callingURL();
    static NetworkInterface serviceLocal = retrofitLocal.create(NetworkInterface.class);
    private static String authToken = "";
    private static String requestID = "";
    private static String userId = "";
    private String isSignature = "";
    private String userMeetingIdentifier = "";
    private VideoCallModel videoCallModel;
    private Bitmap mBitmap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Added By Ruepesh*/
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        /*Added By Ruepesh*/
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_camera);
        initViews();
    }
    
    private void initViews() {

        if (getIntent() != null) {
            requestID = getIntent().getStringExtra("REQUEST_ID");
            authToken = getIntent().getStringExtra("AUTH_TOKEN");
            isSignature = getIntent().getStringExtra("TYPE");
            userId = getIntent().getStringExtra("USER_ID");
            userMeetingIdentifier = getIntent().getStringExtra("USER_MEETING_IDENTIFIER");
            /*videoCallModel = (VideoCallModel) getIntent().getSerializableExtra("VIDEO_CALL_MODEL_OBJ");*/
            /*isSignature*/
            // 1 > Signature
            // 0 > Initial
            /*isSignature*/
        }

        iv_cross = findViewById(R.id.iv_cross);
        iv_preview = findViewById(R.id.iv_preview);
        ll_capture_section = findViewById(R.id.ll_capture_section);
        tv_edit = findViewById(R.id.tv_edit);
        tv_save = findViewById(R.id.tv_save);

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*mActivity.getFragmentManager().popBackStack();*/
                finish();
            }
        });

        ll_capture_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    captureImageThroughCamera();
                } else {
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
                }
            }
        });

        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mBitmap != null) {
                    try {
                        removeImageBackgroundAndAutoCrop(mBitmap);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(CameraActivity.this, "No image found!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(CameraActivity.this, "Camera permission granted", Toast.LENGTH_LONG).show();
                captureImageThroughCamera();
            } else {
                //Toast.makeText(CameraActivity.this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mBitmap = photo;
            ll_capture_section.setVisibility(View.GONE);
            iv_preview.setVisibility(View.VISIBLE);
            iv_preview.setImageBitmap(photo);
        }
    }

    private void captureImageThroughCamera () {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void removeImageBackgroundAndAutoCrop(Bitmap bitMapSignature) throws JSONException, IOException {

        /*Preparing file to send to server*/
        //create a file to write bitmap data
        File f = new File(getCacheDir(), "SignerSignature");
        f.createNewFile();
        //Convert bitmap to byte array
        Bitmap bitmap = bitMapSignature;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", f.getName(), reqFile);

        /*Preparing file to send to server*/

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        /*Preparing retrofit for empty base URL*/
        Retrofit retrofitLocalTemp = networkClass.businessApiCallingURL();
        NetworkInterface serviceLocalTemp = retrofitLocalTemp.create(NetworkInterface.class);
        /*Preparing retrofit for empty base URL*/

        Call<ResponseBody> responseBodyCall = serviceLocalTemp.removeImageBackgroundAndAutoCrop(authToken, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                if (response.body() != null){
                    Log.d(TAG , "Response data:\n"+new Gson().toJson(response.body()));
                    Log.d(TAG , "Processed image data:\n"+response.body().byteStream());
                }
                try {
                    uploadNotarizationImage();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                Log.d("====onResponse", "Failure" + t.toString());
            }
        });
    }

    public void uploadNotarizationImage() throws JSONException {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
        JSONObject obj = new JSONObject();
        obj.put("isDewDoc" , false );
        obj.put("signatureUploadld" , "");
        obj.put("isSignature" , isSignature);
        obj.put("isEditedlmage" , 0);
        obj.put("guid" , "");
        obj.put("requestId" , requestID);
        obj.put("userid" , userId);
        String data = obj.toString();
        Call<ResponseBody> responseBodyCall = serviceLocal.uploadCustomerImages(authToken, data);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(dialog!=null){
                    dialog.dismiss();
                }

                if (response.body() != null){
                    Log.d(TAG , "Response: "+new Gson().toJson(response.body()));
                }

                Intent it = new Intent();
                it.putExtra("RESULT" , true);
                setResult(RESULT_OK , it);
                finish();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                Log.d("====onResponse", "Failure" + t.toString());
            }
        });
    }
    
}