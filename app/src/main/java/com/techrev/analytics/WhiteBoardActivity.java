package com.techrev.analytics;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import com.suyati.telvin.drawingboard.DrawingBoard;
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

public class WhiteBoardActivity extends AppCompatActivity {

    private static final String TAG = "WhiteBoardActivity";
    private Activity mActivity = null;
    /*private DrawingBoard drawingBoard;*/
    private ImageView iv_cross;
    private TextView tv_clear, tv_save;
    private DrawingBoard drawingBoard;
    private OnCapturedDrawing onCapturedDrawing;

    static RetrofitNetworkClass networkClass = new RetrofitNetworkClass();
    static Retrofit retrofitLocal = networkClass.callingURL();
    static NetworkInterface serviceLocal = retrofitLocal.create(NetworkInterface.class);
    private static String authToken = "";
    private static String requestID = "";
    private static String userId = "";
    private String isSignature = "";
    private String userMeetingIdentifier = "";
    private VideoCallModel videoCallModel;


    public interface OnCapturedDrawing {
        public void onFinishDrawing(Bitmap bitMapSignature);
    }

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
        setContentView(R.layout.activity_white_board);
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
        drawingBoard = findViewById(R.id.drawingboard);
        tv_clear = findViewById(R.id.tv_clear);
        tv_save = findViewById(R.id.tv_save);

        drawingBoard.setCanvasColor(android.R.color.white);
        drawingBoard.setPenColor(R.color.colorPrimary);
        drawingBoard.setPenWidth(6f);

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*mActivity.getFragmentManager().popBackStack();*/
                finish();
            }
        });

        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBoard();
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveBoard();
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        
    }

    public void saveBoard() throws JSONException, IOException {

        /*String baseFileName = "/DrawSample/Images";
        String fileName = "drawBoard" + Calendar.getInstance().getTimeInMillis() + ".png";
        Boolean isShownInGallery = true;

        drawingBoard.setBaseFilePath(baseFileName);
        drawingBoard.saveAsImageFile(fileName, isShownInGallery);*/

        /*Send data from where it was invoked*/
        //onCapturedDrawing.onFinishDrawing(drawingBoard.getBitMapSignature());
        //clearBoard();
        /*Send data from where it was invoked*/

        clearBoard();
        removeImageBackgroundAndAutoCrop(drawingBoard.getBitMapSignature());

    }

    public void clearBoard() {
        drawingBoard.clearBoard();
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

    /*public void uploadAttachedFile(Bitmap bitmap , String file_name , String mime_type) {

        ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        String pathDetails = saveToInternalStorageFotAttachment(bitmap , file_name);
        String name = mypath.getName().replace("jpg",mime_type.split("/")[1]);
        Log.e("===pathDetails", "" + pathDetails);
        Log.e("===pathDetails", "Image Path:" + mypath);
        Log.e("===pathDetails", "Image Name:" + name);
        Log.e("===pathDetails", "Image Mime Type: "+mime_type);
        String extension = mypath.getName().split("\\.")[1];

        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse(mime_type), mypath);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", name , requestBody1);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), name);
        Call<AttachedFileUploadResponseModel> call = serviceLocal.uploadAttachedFile(authToken, fileToUpload, filename, name, false, userId);
        call.enqueue(new Callback<AttachedFileUploadResponseModel>() {
            @Override
            public void onResponse(Call<AttachedFileUploadResponseModel> call, Response<AttachedFileUploadResponseModel> response) {
                dialog.dismiss();
                Log.d("====Inside1", "Success");
                Log.d("====Inside1", "Response:" + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.d(TAG, "UPLOADED DOC ID:" + response.body().getDocId());
                        uploaded_chat_file_docID = response.body().getDocId();
                        ll_attach_file.setVisibility(View.GONE);
                        ll_send_file.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<AttachedFileUploadResponseModel> call, Throwable t) {
                Log.d("====Inside1", "Failure");
                Log.d("====Inside1", "Throwable:" + t.toString());
                dialog.dismiss();

            }
        });

        // File Upload Work Ends


    }*/

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