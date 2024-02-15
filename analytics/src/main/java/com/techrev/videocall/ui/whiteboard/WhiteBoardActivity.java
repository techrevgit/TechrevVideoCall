package com.techrev.videocall.ui.whiteboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suyati.telvin.drawingboard.DrawingBoard;
import com.techrev.videocall.R;
import com.techrev.videocall.models.VideoCallModel;
import com.techrev.videocall.network.NetworkInterface;
import com.techrev.videocall.network.RetrofitNetworkClass;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

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
    private Activity mActivity = this;
    /*private DrawingBoard drawingBoard;*/
    private ImageView iv_cross;
    private TextView tv_clear, tv_save, tv_document_title;
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
    private byte[] imageInBytes;
    public interface OnCapturedDrawing {
        public void onFinishDrawing(Bitmap bitMapSignature);
    }

    LocalBroadcastManager mLocalBroadcastManager;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.enotaryoncall.customer.action.close")){
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /*Added by Rupesh to close activity from service*/
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.enotaryoncall.customer.action.close");
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
        /*Added by Rupesh to close activity from service*/
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
            //this.getSupportActionBar().hide();
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
        tv_document_title = findViewById(R.id.tv_document_title);

        if (isSignature.equalsIgnoreCase("1")) {
            tv_document_title.setText("Draw Your Signature");
        } else {
            tv_document_title.setText("Draw Your Initial");
        }

        drawingBoard.setCanvasColor(android.R.color.white);
        drawingBoard.setPenColor(R.color.color_primary);
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

        // For testing
        //ImageView iv_test = findViewById(R.id.testCapturedSignature);
        //iv_test.setImageBitmap(drawingBoard.getBitMapSignature());
        // For testing
        if (drawingBoard.isDraw()) {
            removeImageBackgroundAndAutoCrop(drawingBoard.getBitMapSignature());
            clearBoard();
        } else {
            Toast.makeText(this, "Please draw your signature/initial", Toast.LENGTH_SHORT).show();
        }

    }

    public void clearBoard() {
        drawingBoard.clearBoard();
    }

    public void removeImageBackgroundAndAutoCrop(Bitmap bitMapSignature) throws JSONException, IOException {

        /*Preparing file to send to server*/
        Bitmap resizedImage=getResizedBitmap(bitMapSignature,500);
        /*Converting bitmap to byte array*/
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageInBytes = stream.toByteArray();
        /*Converting bitmap to byte array*/
        RequestBody rFile = RequestBody.create(MediaType.parse("image/jpeg"), imageInBytes);
        MultipartBody.Part file = MultipartBody.Part.createFormData("file", Calendar.getInstance().getTimeInMillis()+"_image.jpg", rFile);
        /*Preparing file to send to server*/

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        /*Preparing retrofit for empty base URL*/
         Retrofit retrofitLocalTemp = networkClass.businessApiCallingURL();
         NetworkInterface serviceLocalTemp = retrofitLocalTemp.create(NetworkInterface.class);
        /*Preparing retrofit for empty base URL*/

        Call<ResponseBody> responseBodyCall = serviceLocalTemp.removeImageBackgroundAndAutoCrop(authToken, file);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                if (response.isSuccessful() && response.body() != null){
                    Log.d(TAG , "Response data:\n"+new Gson().toJson(response.body()));
                    Log.d(TAG , "Processed image data:\n"+response.body().byteStream());
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    try {
                        uploadNotarizationImage(bitmap);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
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

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
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

    public void uploadNotarizationImage(Bitmap bitmap) throws JSONException {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        /*Preparing file to send to server*/
        Bitmap resizedImage=getResizedBitmap(bitmap,500);
        /*Converting bitmap to byte array*/
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageInBytes = stream.toByteArray();
        RequestBody rFile = RequestBody.create(MediaType.parse("image/jpeg"), imageInBytes);
        MultipartBody.Part file = MultipartBody.Part.createFormData("file", Calendar.getInstance().getTimeInMillis()+"_image.jpg", rFile);
        /*Preparing file to send to server*/

        /*JSONObject obj = new JSONObject();
        obj.put("isDewDoc" , false );
        obj.put("signatureUploadId" , "");
        obj.put("isSignature" , isSignature);
        obj.put("isEditedlmage" , 0);
        obj.put("guid" , "");
        obj.put("requestId" , requestID);
        obj.put("userid" , userId);
        String data = obj.toString();*/
        RequestBody rb_isDewDoc = RequestBody.create(MediaType.parse("text/plain"), "false");
        RequestBody rb_signatureUploadld = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody rb_isSignature = RequestBody.create(MediaType.parse("text/plain"), isSignature);
        RequestBody rb_isEditedlmage = RequestBody.create(MediaType.parse("text/plain"), "0");
        RequestBody rb_guid = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody rb_requestId = RequestBody.create(MediaType.parse("text/plain"), requestID);
        RequestBody rb_userid = RequestBody.create(MediaType.parse("text/plain"), userId);

        Call<ResponseBody> responseBodyCall = serviceLocal.uploadCustomerImages(authToken,file, rb_isDewDoc, rb_signatureUploadld,
                rb_isSignature, rb_isEditedlmage, rb_guid, rb_requestId, rb_userid);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                if (isSignature.equalsIgnoreCase("1")) {
                    Toast.makeText(WhiteBoardActivity.this, "Signature has been uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WhiteBoardActivity.this, "Initial has been uploaded successfully", Toast.LENGTH_SHORT).show();
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