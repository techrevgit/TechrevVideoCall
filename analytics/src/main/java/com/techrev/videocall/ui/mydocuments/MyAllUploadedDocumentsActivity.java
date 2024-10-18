package com.techrev.videocall.ui.mydocuments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.techrev.videocall.R;
import com.techrev.videocall.dialogfragments.CaptureSignerInitialDialogFragment;
import com.techrev.videocall.dialogfragments.CaptureSignerSignatureDialogFragment;
import com.techrev.videocall.models.AttachedFileUploadResponseModel;
import com.techrev.videocall.models.CommonModel;
import com.techrev.videocall.models.DataModel;
import com.techrev.videocall.models.MyAllDocListModel;
import com.techrev.videocall.models.VideoCallModel;
import com.techrev.videocall.network.NetworkInterface;
import com.techrev.videocall.network.RetrofitNetworkClass;
import com.techrev.videocall.ui.camera.CameraActivity;
import com.techrev.videocall.ui.cosigner.AddCoSignerActivity;
import com.techrev.videocall.ui.videocallroom.VideoActivity;
import com.techrev.videocall.ui.whiteboard.WhiteBoardActivity;
import com.techrev.videocall.utils.Constants;
import com.techrev.videocall.utils.DataManager;
import com.techrev.videocall.utils.MySharedPreference;
import com.techrev.videocall.utils.NotarizationActionUpdateManger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyAllUploadedDocumentsActivity extends AppCompatActivity {

    private static final String TAG = "MyAllUploadedDocumentsA";
    private ImageView iv_back;
    private CheckBox cb_selectAll;
    private TextView tv_add_document;
    private RecyclerView rv_uploaded_documents;
    private LinearLayout ll_upload_Section;
    ActivityResultLauncher<Intent> resultLauncher;
    private String authToken, requestID;
    static RetrofitNetworkClass networkClass = new RetrofitNetworkClass();
    static Retrofit retrofitLocal = networkClass.callingURL();
    static NetworkInterface serviceLocal = retrofitLocal.create(NetworkInterface.class);
    File directory;
    File mypath;
    private static String userId = "";
    private String isPrimarySigner = "";
    List<String> selectedDocIdList = new ArrayList<>();
    private MyAllDocListAdapter adapter = null;
    LocalBroadcastManager mLocalBroadcastManager;
    private String pathDoc="";
    private Uri uri;
    BroadcastReceiver mBroadcastReceiver;

    /*Signature/Initial Capture And Replace Implementation*/
    private static final int SIGNATURE_INITIAL_CAPTURE_CODE = 9999;
    private final int CAMERA_REQUEST_FOR_SIGNATURE = 10191;
    private final int CAMERA_REQUEST_FOR_INITIAL = 10192;
    private final int REQUEST_CAMERA_CODE_FOR_SIGNATURE = 100981;
    private final int REQUEST_CAMERA_CODE_FOR_INITIAL = 100982;
    private boolean IS_REPLACE_SIGNATURE_INITIAL_DIALOG_SHOWN = false;
    private boolean IS_AUTHORIZATION_DIALOG_SHOWN_ALREADY = false;
    private boolean IS_SIGNATURE_CAPTURE_IN_PROGRESS = false;
    private boolean IS_INITIAL_CAPTURE_IN_PROGRESS = false;
    private String isWitness, customerType;
    private boolean isCoSigner = false;
    private List<DataModel> dataModelList = new ArrayList<DataModel>();
    private VideoCallModel videoCallModel;
    private String userMeetingIdentifier = "";
    private MySharedPreference sharedPreference;
    /*Signature/Initial Capture And Replace Implementation*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Added By Ruepsh*/
        /*Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);*/

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        //getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        /*Added By Ruepsh*/

        setContentView(R.layout.activity_my_all_uploaded_documents);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sharedPreference!=null && sharedPreference.getBoolean(Constants.SIGNATURE_CAPTURE_REQUEST_IN_BACKGROUND)){
            try {
                showSignatureCaptureDialog();
                sharedPreference.setBoolean(Constants.SIGNATURE_CAPTURE_REQUEST_IN_BACKGROUND , false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (sharedPreference!=null && sharedPreference.getBoolean(Constants.INITIAL_CAPTURE_REQUEST_IN_BACKGROUND)){
            try {
                showInitialCaptureDialog();
                sharedPreference.setBoolean(Constants.INITIAL_CAPTURE_REQUEST_IN_BACKGROUND , false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (sharedPreference!=null && sharedPreference.getBoolean(Constants.SIGNATURE_INITIAL_REPLACE_REQUEST_IN_BACKGROUND)){
            showReplaceSignatureInitialDialog();
            sharedPreference.setBoolean(Constants.SIGNATURE_INITIAL_REPLACE_REQUEST_IN_BACKGROUND , false);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.clearSelectedDocIdList();
        }
        selectedDocIdList.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String uriString = uri.toString();
            Log.d(TAG, "onActivityResult: Selected File: " + uriString);

            // Resolve the actual file path from the Uri
            String path = getPathFromUri(uri);
            Log.d(TAG, "onActivityResult: Selected File Real Path: " + path);

            try {
                if (path != null) {
                    uploadAttachedFile(path);
                } else {
                    Log.e(TAG, "File path is null");
                    // Handle case where file path is null
                    // Display an error message or take appropriate action
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (resultCode == RESULT_OK && requestCode == SIGNATURE_INITIAL_CAPTURE_CODE) {

            if (data.getBooleanExtra("RESULT", false)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    sendUpdateToServer();
                }
            }
        }

        if (requestCode == CAMERA_REQUEST_FOR_SIGNATURE && resultCode == Activity.RESULT_OK) {
            byte[] imageInBytes;
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap resizedImage=getResizedBitmap(photo,500);
            /*Converting bitmap to byte array*/
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageInBytes = stream.toByteArray();
            /*Converting bitmap to byte array*/
            Intent it = new Intent(MyAllUploadedDocumentsActivity.this, CameraActivity.class);
            it.putExtra("REQUEST_ID" , requestID);
            it.putExtra("AUTH_TOKEN" , authToken);
            it.putExtra("USER_ID" , userId);
            it.putExtra("CUSTOMER_TYPE" , customerType);
            it.putExtra("TYPE" , "1");
            it.putExtra("USER_MEETING_IDENTIFIER" , userMeetingIdentifier);
            it.putExtra("bitmap", photo);
            it.putExtra("resizedImageBytes", imageInBytes);
            /*it.putExtra("VIDEO_CALL_MODEL_OBJ" , videoCallModel);*/
            MyAllUploadedDocumentsActivity.this.startActivityForResult(it , SIGNATURE_INITIAL_CAPTURE_CODE);
        }

        if (requestCode == CAMERA_REQUEST_FOR_INITIAL && resultCode == Activity.RESULT_OK) {
            byte[] imageInBytes;
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap resizedImage=getResizedBitmap(photo,500);
            /*Converting bitmap to byte array*/
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageInBytes = stream.toByteArray();
            /*Converting bitmap to byte array*/
            Intent it = new Intent(MyAllUploadedDocumentsActivity.this, CameraActivity.class);
            it.putExtra("REQUEST_ID" , requestID);
            it.putExtra("AUTH_TOKEN" , authToken);
            it.putExtra("USER_ID" , userId);
            it.putExtra("CUSTOMER_TYPE" , customerType);
            it.putExtra("TYPE" , "0");
            it.putExtra("USER_MEETING_IDENTIFIER" , userMeetingIdentifier);
            it.putExtra("bitmap", photo);
            it.putExtra("resizedImageBytes", imageInBytes);
            /*it.putExtra("VIDEO_CALL_MODEL_OBJ" , videoCallModel);*/
            MyAllUploadedDocumentsActivity.this.startActivityForResult(it , SIGNATURE_INITIAL_CAPTURE_CODE);
        }
    }


    private void initViews() {
        if (getIntent() != null) {
            authToken = getIntent().getStringExtra("AUTH_TOKEN");
            requestID = getIntent().getStringExtra("REQUEST_ID");
            userId = getIntent().getStringExtra("USER_ID");
            isPrimarySigner = getIntent().getStringExtra("IS_PRIMARY_SIGNER");
            isWitness = getIntent().getStringExtra("IS_WITNESS");
            isCoSigner = getIntent().getBooleanExtra("IS_COSIGNER", false);
            customerType = getIntent().getStringExtra("CUSTOMER_TYPE");
            userMeetingIdentifier = getIntent().getStringExtra("USER_MEETING_IDENTIFIER");
        }
        iv_back = findViewById(R.id.iv_back);
        tv_add_document = findViewById(R.id.tv_add_document);
        cb_selectAll = findViewById(R.id.cb_selectAll);
        rv_uploaded_documents = findViewById(R.id.rv_uploaded_documents);
        ll_upload_Section = findViewById(R.id.ll_upload_Section);

        sharedPreference = new MySharedPreference(MyAllUploadedDocumentsActivity.this);
        videoCallModel = DataManager.getInstance().getVideoCallModel();
        dataModelList = DataManager.getInstance().getDataModelList();

        registerBroadcastReceivers();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cb_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (adapter != null){
                    adapter.isAllDocsSelected(b);
                }
            }
        });

        tv_add_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Add Document selected items size: "+selectedDocIdList.size());
                try {
                    if (selectedDocIdList.size() > 0) {
                        updateExistingRequestDocument();
                    } else {
                        Toast.makeText(MyAllUploadedDocumentsActivity.this, "Please select any document to add.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Initialize result launcher
        /*resultLauncher = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(
                            ActivityResult result)
                    {
                        // Initialize result data
                        Intent data = result.getData();
                        Log.d(TAG , "Chosen file data in onActivityResult: "+new Gson().toJson(data));
                        if(data != null){
                            pathDoc=data.getStringExtra("path");
                            Log.d(TAG , "Selected document path: "+pathDoc);
                            if (pathDoc != null) {
                                uri=Uri.fromFile(new File(pathDoc));
                                File file = new File(uri.getPath());
                                *//*View myView = getLayoutInflater().inflate(R.layout.fragment_file_chooser, null);
                                TextView tv_file_name = myView.findViewById(R.id.file_name);
                                tv_file_name.setText(name);*//*
                                try {
                                    uploadAttachedFile(file);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                });*/

        ll_upload_Section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check condition
                /*if (ActivityCompat.checkSelfPermission(
                        MyAllUploadedDocumentsActivity.this,
                        Manifest.permission
                                .READ_EXTERNAL_STORAGE)
                        != PackageManager
                        .PERMISSION_GRANTED) {
                    // When permission is not granted
                    // Result permission
                    ActivityCompat.requestPermissions(
                            MyAllUploadedDocumentsActivity.this,
                            new String[] {
                                    Manifest.permission
                                            .READ_EXTERNAL_STORAGE },
                            1);
                } else {
                    // When permission is granted
                    selectPDF();
                }*/
                selectPDF();
            }
        });

        try {
            getMyAllUploadedDocuments();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private void registerBroadcastReceivers() {
        /*Code to handle signals from service*/
        mBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (action == null) return;

                switch (action) {
                    case "com.enotaryoncall.customer.action.close":
                        finish();
                        break;

                    case "com.enotaryoncall.customer.action.signature_capture_request":
                        if (IS_SIGNATURE_CAPTURE_IN_PROGRESS) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("from", userMeetingIdentifier);
                                jsonObject.put("to", "All");
                                jsonObject.put("messageType", "SignatureDialogAlreadyOpen");
                                jsonObject.put("content", "SignatureDialogAlreadyOpen");
                            } catch (JSONException e) {
                                e.printStackTrace(); // Handle the exception
                            }
                            videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());

                        } else if (IS_INITIAL_CAPTURE_IN_PROGRESS) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("from", userMeetingIdentifier);
                                jsonObject.put("to", "All");
                                jsonObject.put("messageType", "InitialDialogAlreadyOpen");
                                jsonObject.put("content", "InitialDialogAlreadyOpen");
                            } catch (JSONException e) {
                                e.printStackTrace(); // Handle the exception
                            }
                            videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());

                        } else {
                            try {
                                showSignatureCaptureDialog();
                            } catch (JSONException e) {
                                Log.d(TAG, "Error showing signature capture dialog: " + e.getMessage());
                            }
                        }
                        break;

                    case "com.enotaryoncall.customer.action.initial_capture_request":
                        if (IS_SIGNATURE_CAPTURE_IN_PROGRESS) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("from", userMeetingIdentifier);
                                jsonObject.put("to", "All");
                                jsonObject.put("messageType", "SignatureDialogAlreadyOpen");
                                jsonObject.put("content", "SignatureDialogAlreadyOpen");
                            } catch (JSONException e) {
                                e.printStackTrace(); // Handle the exception
                            }
                            videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());

                        } else if (IS_INITIAL_CAPTURE_IN_PROGRESS) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("from", userMeetingIdentifier);
                                jsonObject.put("to", "All");
                                jsonObject.put("messageType", "InitialDialogAlreadyOpen");
                                jsonObject.put("content", "InitialDialogAlreadyOpen");
                            } catch (JSONException e) {
                                e.printStackTrace(); // Handle the exception
                            }
                            videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());

                        } else {
                            try {
                                showInitialCaptureDialog();
                            } catch (JSONException e) {
                                Log.d(TAG, "Error showing initial capture dialog: " + e.getMessage());
                            }
                        }
                        break;

                    case "com.enotaryoncall.customer.action.signature_initial_replace_request":
                        showReplaceSignatureInitialDialog();
                        break;

                    default:
                        // Handle any other actions if needed
                        break;
                }
            }
        };
        /*Code to handle signals from service*/

        /*Added by Rupesh to register signals to service*/
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.enotaryoncall.customer.action.close");
        mIntentFilter.addAction("com.enotaryoncall.customer.action.signature_capture_request");
        mIntentFilter.addAction("com.enotaryoncall.customer.action.initial_capture_request");
        mIntentFilter.addAction("com.enotaryoncall.customer.action.signature_initial_replace_request");
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
        /*Added by Rupesh to register signals to service*/
    }

    public String getMimeType(Uri uri) {
        String mimeType = "*/*";
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = null;
        try {
            ContentResolver resolver = getContentResolver();
            cursor = resolver.query(uri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                String fileName = cursor.getString(nameIndex);

                if (fileName != null) {
                    InputStream inputStream = resolver.openInputStream(uri);

                    if (inputStream != null) {
                        File tempFile = new File(getCacheDir(), fileName);
                        FileOutputStream outputStream = new FileOutputStream(tempFile);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }

                        outputStream.close();
                        inputStream.close();

                        return tempFile.getAbsolutePath();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;
    }



    public void uploadAttachedFile(String fileUri) throws IOException {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        DateFormat dateFormat = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)");
        Calendar cal = Calendar.getInstance();
        String dateAndTime = dateFormat.format(cal.getTime());
        // Resolve the actual file path from the Uri
        Log.d(TAG , "Selected File: "+fileUri);
        File file = new File(fileUri);
        // Create RequestBody for file
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/pdf"), file);
        // Create MultipartBody.Part from file
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);
        RequestBody rb_userType = RequestBody.create(MediaType.parse("text/plain"), "1");
        //RequestBody rb_RequestId = RequestBody.create(MediaType.parse("text/plain"), requestID);
        RequestBody rb_documentName = RequestBody.create(MediaType.parse("text/plain"), file.getName().replace(".pdf",""));
        RequestBody rb_userId = RequestBody.create(MediaType.parse("text/plain"), userId);
        //RequestBody rb_isTempRequest = RequestBody.create(MediaType.parse("text/plain"), "true");
        RequestBody rb_dateTime = RequestBody.create(MediaType.parse("text/plain"), dateAndTime);
        //RequestBody rb_tempRequestId = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody rb_isDewDoc = RequestBody.create(MediaType.parse("text/plain"), "false");
        RequestBody rb_uploadedBy = RequestBody.create(MediaType.parse("text/plain"), userId);

        Call<AttachedFileUploadResponseModel> call = serviceLocal.uploadRequesterDocument(
                authToken,
                filePart,
                rb_userType,
                rb_documentName,
                rb_userId,
                rb_dateTime,
                rb_isDewDoc,
                rb_uploadedBy);
        call.enqueue(new Callback<AttachedFileUploadResponseModel>() {
            @Override
            public void onResponse(Call<AttachedFileUploadResponseModel> call, Response<AttachedFileUploadResponseModel> response) {
                dialog.dismiss();
                Log.d("====Inside1", "Success");
                Log.d("====Inside1", "Response:" + response.code());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(TAG, "UPLOADED DOC ID:" + response.body().getDocId());
                        Toast.makeText(MyAllUploadedDocumentsActivity.this, "File uploaded successfully!", Toast.LENGTH_SHORT).show();
                        try {
                            getMyAllUploadedDocuments();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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

    }

    private void updateExistingRequestDocument() throws JSONException {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
        JsonObject object = new JsonObject();
        object.addProperty("requestId" , requestID);
        object.addProperty("DocIds" , new Gson().toJson(selectedDocIdList));
        Log.d(TAG, "updateExistingRequestDocument: requestId: "+requestID);
        Log.d(TAG, "updateExistingRequestDocument: DocIds: "+new Gson().toJson(selectedDocIdList));

        //List<RequesterDocumentModel.DocIdObject> docIdObjects = new ArrayList<>();
        //docIdObjects.add(new RequesterDocumentModel.DocIdObject(docId));
        /*for (String docId : selectedDocIdList) {
            docIdObjects.add(new RequesterDocumentModel.DocIdObject(docId));
        }*/
        String selectedDocIds = listToString(selectedDocIdList);
        List<String> docIdObjects = new ArrayList<>(selectedDocIdList);
        Log.d(TAG , "Selected docIdObjects data for API: "+new Gson().toJson(docIdObjects));
        //RequesterDocumentModel request = new RequesterDocumentModel(requestID, docIdObjects);
        RequesterDocumentModel request = new RequesterDocumentModel();
        JSONObject requestObject = request.buildRequestObject(docIdObjects, requestID);
        Log.d(TAG , "Selected request data for API: "+requestObject.toString());
        Call<CommonModel> responseBodyCall = serviceLocal.updateExistingRequestDocument(authToken, requestObject.toString());
        responseBodyCall.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                if (adapter != null) {
                    adapter.clearSelectedDocIdList();
                }
                selectedDocIdList.clear();
                if(response != null){
                    Log.d(TAG , "Updated document response: \n"+new Gson().toJson(response.body()));
                    if (response.body() != null && response.body().getStatus().equalsIgnoreCase("1")) {
                        Toast.makeText(MyAllUploadedDocumentsActivity.this, "Document(s) added successfully!", Toast.LENGTH_SHORT).show();
                        NotarizationActionUpdateManger.updateNotarizationAction(
                                MyAllUploadedDocumentsActivity.this, authToken,
                                requestID, "", userId, isPrimarySigner,
                                "1", "1", selectedDocIds, "");
                    } else {
                        Toast.makeText(MyAllUploadedDocumentsActivity.this, "Something went wrong, please try again later!", Toast.LENGTH_SHORT).show();
                    }
                    if (adapter != null) {
                        adapter.clearSelectedDocIdList();
                    }
                    selectedDocIdList.clear();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                Log.d("====onResponse", "Failure" + t.toString());
            }
        });

    }

    public static String listToString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String element : list) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(","); // Add comma and space separator
            }
            stringBuilder.append(element);
        }
        return stringBuilder.toString();
    }

    private void selectPDF()
    {
        Intent intent = null;
        // Initialize intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            //intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        //intent = new Intent(Intent.ACTION_GET_CONTENT);
        // set type
        intent.setType("application/pdf");
        // Launch intent
        //resultLauncher.launch(intent);
        startActivityForResult(intent , 1001);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);

        // check condition
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            selectPDF();
        } else {
            // When permission is denied
            // Display toast
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMyAllUploadedDocuments() throws JSONException {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
        Call<MyAllDocListModel> responseBodyCall = serviceLocal.getAllCustomerUploadedDocs(authToken);
        responseBodyCall.enqueue(new Callback<MyAllDocListModel>() {
            @Override
            public void onResponse(Call<MyAllDocListModel> call, Response<MyAllDocListModel> response) {
                if(dialog!=null){
                    dialog.dismiss();
                }

                if(response != null){
                    Log.d(TAG , "My Uploaded Documents Data: \n"+new Gson().toJson(response.body()));
                    adapter = new MyAllDocListAdapter(MyAllUploadedDocumentsActivity.this, authToken,
                            response.body().getAllDocs(), docIdList -> {
                                selectedDocIdList = docIdList;
                                Log.d(TAG, "onDocumentsSelected: selected item size: "+selectedDocIdList.size());
                                Log.d(TAG , "Selected Doc IDs List Data: "+new Gson().toJson(selectedDocIdList));
                            });
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(MyAllUploadedDocumentsActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv_uploaded_documents.setLayoutManager(manager);
                    rv_uploaded_documents.setAdapter(adapter);
                    rv_uploaded_documents.addItemDecoration(new DividerItemDecoration(MyAllUploadedDocumentsActivity.this,
                            DividerItemDecoration.VERTICAL));
                }
            }

            @Override
            public void onFailure(Call<MyAllDocListModel> call, Throwable t) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                Log.d("====onResponse", "Failure" + t.toString());
            }
        });

    }

    /*Signature/Initial Capture and Replace Implementation*/

    private void showSignatureCaptureDialog() throws JSONException {
        //Toast.makeText(this, "Request to capture initial image", Toast.LENGTH_SHORT).show();
        // Show the capture signature dialog fragment
        CaptureSignerSignatureDialogFragment captureSignerSignatureDialogFragment = new CaptureSignerSignatureDialogFragment(MyAllUploadedDocumentsActivity.this, userMeetingIdentifier, videoCallModel, authToken, requestID, userId, customerType, new CaptureSignerSignatureDialogFragment.OptionSelectionInterface() {
            @Override
            public void onOptionSelected(int selectedOption) {
                //1 = Capture through camera & 2 = Draw through whiteboard
                if (selectedOption == 1) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MyAllUploadedDocumentsActivity.this);
                    AlertDialog dialog = builder.setTitle("Capture Signature")
                            .setMessage("Are you sure, you want to capture your signature?")
                            .setCancelable(false)
                            .setPositiveButton("Ok, Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Toast.makeText(MyAllUploadedDocumentsActivity.this, "Opening camera", Toast.LENGTH_SHORT).show();
                                    if (ContextCompat.checkSelfPermission(MyAllUploadedDocumentsActivity.this, Manifest.permission.CAMERA)
                                            == PackageManager.PERMISSION_DENIED) {
                                        captureImageThroughCamera("Signature");
                                    } else {
                                        ActivityCompat.requestPermissions(MyAllUploadedDocumentsActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE_FOR_SIGNATURE);
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MyAllUploadedDocumentsActivity.this.getColor(R.color.color_primary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MyAllUploadedDocumentsActivity.this.getColor(R.color.red));
                    }

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MyAllUploadedDocumentsActivity.this);
                    AlertDialog dialog = builder.setTitle("Draw Signature")
                            .setMessage("Are you sure, you want to draw your signature?")
                            .setCancelable(false)
                            .setPositiveButton("Ok, Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Toast.makeText(MyAllUploadedDocumentsActivity.this, "Opening whiteboard", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(MyAllUploadedDocumentsActivity.this, WhiteBoardActivity.class);
                                    it.putExtra("REQUEST_ID", requestID);
                                    it.putExtra("AUTH_TOKEN", authToken);
                                    it.putExtra("USER_ID", userId);
                                    it.putExtra("CUSTOMER_TYPE", customerType);
                                    it.putExtra("TYPE", "1");
                                    it.putExtra("USER_MEETING_IDENTIFIER", userMeetingIdentifier);
                                    /*it.putExtra("VIDEO_CALL_MODEL_OBJ" , videoCallModel);*/
                                    MyAllUploadedDocumentsActivity.this.startActivityForResult(it, SIGNATURE_INITIAL_CAPTURE_CODE);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setCancelable(false)
                            .show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MyAllUploadedDocumentsActivity.this.getColor(R.color.color_primary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MyAllUploadedDocumentsActivity.this.getColor(R.color.red));
                    }

                }
            }
        }, new CaptureSignerSignatureDialogFragment.DialogStateInterface() {
            @Override
            public void onDialogClosed(boolean isClosed) {
                IS_SIGNATURE_CAPTURE_IN_PROGRESS = false;
            }
        });
        FragmentTransaction ft = MyAllUploadedDocumentsActivity.this.getFragmentManager().beginTransaction();
        Fragment prev = MyAllUploadedDocumentsActivity.this.getFragmentManager().findFragmentByTag("signature_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        captureSignerSignatureDialogFragment.setCancelable(false);
        captureSignerSignatureDialogFragment.show(ft,"signature_dialog");
        IS_SIGNATURE_CAPTURE_IN_PROGRESS = true;

    }

    private void showInitialCaptureDialog() throws JSONException {
        //Toast.makeText(this, "Request to capture signature image", Toast.LENGTH_SHORT).show();
        // Show the capture initial dialog fragment
        CaptureSignerInitialDialogFragment captureSignerInitialDialogFragment = new CaptureSignerInitialDialogFragment(MyAllUploadedDocumentsActivity.this, userMeetingIdentifier, videoCallModel, authToken, requestID, userId, customerType, new CaptureSignerInitialDialogFragment.OptionSelectionInterface() {
            @Override
            public void onOptionSelected(int selectedOption) {
                //1 = Capture through camera & 2 = Draw through whiteboard
                if (selectedOption == 1) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Toast.makeText(MyAllUploadedDocumentsActivity.this, "Opening camera", Toast.LENGTH_SHORT).show();
                                    if (ContextCompat.checkSelfPermission(MyAllUploadedDocumentsActivity.this, Manifest.permission.CAMERA)
                                            == PackageManager.PERMISSION_DENIED) {
                                        captureImageThroughCamera("Initial");
                                    } else {
                                        ActivityCompat.requestPermissions(MyAllUploadedDocumentsActivity.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE_FOR_INITIAL);
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MyAllUploadedDocumentsActivity.this);
                    AlertDialog dialog = builder.setTitle("Capture Initial")
                            .setMessage("Are you sure, you want to capture your initial?")
                            .setPositiveButton("Ok, Proceed", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener)
                            .setCancelable(false)
                            .show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MyAllUploadedDocumentsActivity.this.getColor(R.color.color_primary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MyAllUploadedDocumentsActivity.this.getColor(R.color.red));
                    }

                } else {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Toast.makeText(MyAllUploadedDocumentsActivity.this, "Opening camera", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(MyAllUploadedDocumentsActivity.this, WhiteBoardActivity.class);
                                    it.putExtra("REQUEST_ID" , requestID);
                                    it.putExtra("AUTH_TOKEN" , authToken);
                                    it.putExtra("USER_ID" , userId);
                                    it.putExtra("CUSTOMER_TYPE" , customerType);
                                    it.putExtra("TYPE" , "0");
                                    it.putExtra("USER_MEETING_IDENTIFIER" , userMeetingIdentifier);
                                    /*it.putExtra("VIDEO_CALL_MODEL_OBJ" , videoCallModel);*/
                                    MyAllUploadedDocumentsActivity.this.startActivityForResult(it , SIGNATURE_INITIAL_CAPTURE_CODE);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MyAllUploadedDocumentsActivity.this);
                    AlertDialog dialog = builder.setTitle("Draw Initial")
                            .setMessage("Are you sure, you want to draw your initial?")
                            .setPositiveButton("Ok, Proceed", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener)
                            .setCancelable(false)
                            .show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MyAllUploadedDocumentsActivity.this.getColor(R.color.color_primary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MyAllUploadedDocumentsActivity.this.getColor(R.color.red));
                    }

                }
            }
        }, new CaptureSignerInitialDialogFragment.DialogStateInterface() {
            @Override
            public void onDialogClosed(boolean isClosed) {
                IS_INITIAL_CAPTURE_IN_PROGRESS = false;
            }
        });
        FragmentTransaction ft1 = MyAllUploadedDocumentsActivity.this.getFragmentManager().beginTransaction();
        Fragment prev1 = MyAllUploadedDocumentsActivity.this.getFragmentManager().findFragmentByTag("initial_dialog");
        if (prev1 != null) {
            ft1.remove(prev1);
        }
        ft1.addToBackStack(null);
        captureSignerInitialDialogFragment.setCancelable(false);
        captureSignerInitialDialogFragment.show(ft1,"initial_dialog");
        IS_INITIAL_CAPTURE_IN_PROGRESS = true;

    }

    private void showReplaceSignatureInitialDialog() {
        if (!IS_REPLACE_SIGNATURE_INITIAL_DIALOG_SHOWN) {
            IS_REPLACE_SIGNATURE_INITIAL_DIALOG_SHOWN = true;
            String signatureActionID = "";
            String initialActionID = "";
            String deniedActionID = "";
            if (isWitness != null && !isWitness.equals("")) {
                signatureActionID = "75";
                initialActionID = "76";
                deniedActionID = "74";
            } else if (!isCoSigner) {
                signatureActionID = "27";
                initialActionID = "28";
                deniedActionID = "34";
            } else {
                signatureActionID = "62";
                initialActionID = "63";
                deniedActionID = "35";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(MyAllUploadedDocumentsActivity.this);
            String finalSignatureActionID = signatureActionID;
            String finalInitialActionID = initialActionID;
            String finalDeniedActionID = deniedActionID;
            AlertDialog dialog = builder.setTitle("Confirmation to Add My Signature & Initial")
                    .setMessage("I agree to replace Signature & Initial Tag with my Signature & Initial.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            IS_REPLACE_SIGNATURE_INITIAL_DIALOG_SHOWN = false;
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("from", userMeetingIdentifier);
                                jsonObject.put("to", "All");
                                jsonObject.put("messageType", "AcceptedToReplaceMySignatureAndIntial");
                                jsonObject.put("content", "AcceptedToReplaceMySignatureAndIntial");
                                videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());

                                String currentDocID = sharedPreference.getString(Constants.CURRENT_SIGNATURE_INITIAL_TAG_REPLACE_DOC_ID);
                                Log.d(TAG , "Current DataModel List Data: "+new Gson().toJson(dataModelList));

                                for (int position = 0; position < dataModelList.size() ; position++) {
                                    if (dataModelList.get(position).getTagKey().equalsIgnoreCase("52")) {
                                        // For Signature
                                        NotarizationActionUpdateManger.updateNotarizationAction(
                                                MyAllUploadedDocumentsActivity.this, authToken,
                                                requestID, "", userId, customerType,
                                                finalSignatureActionID, "1", dataModelList.get(position).getDocid(),
                                                dataModelList.get(position).getPageNumber());
                                    } else {
                                        // For Initial
                                        NotarizationActionUpdateManger.updateNotarizationAction(
                                                MyAllUploadedDocumentsActivity.this, authToken,
                                                requestID, "", userId, customerType,
                                                finalInitialActionID, "1", dataModelList.get(position).getDocid(),
                                                dataModelList.get(position).getPageNumber());
                                    }
                                }
                                dataModelList.clear();

                            } catch (Exception e) {
                                Log.d("====Exception", "" + e.toString());
                            }

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            IS_REPLACE_SIGNATURE_INITIAL_DIALOG_SHOWN = false;
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("from", userMeetingIdentifier);
                                jsonObject.put("to", "All");
                                jsonObject.put("messageType", "DeniedToReplaceMySignatureAndIntial");
                                jsonObject.put("content", "DeniedToReplaceMySignatureAndIntial");
                                videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                                NotarizationActionUpdateManger.updateNotarizationAction(
                                        MyAllUploadedDocumentsActivity.this, authToken,
                                        requestID, "", userId, customerType,
                                        finalDeniedActionID, "1", "", "");

                                dataModelList.clear();

                            } catch (Exception e) {
                                Log.d("====Exception", "" + e.toString());
                            }

                        }
                    })
                    .setCancelable(false)
                    .show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MyAllUploadedDocumentsActivity.this.getColor(R.color.color_primary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MyAllUploadedDocumentsActivity.this.getColor(R.color.red));
            }
        }
    }

    private void captureImageThroughCamera (String type) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (type.equalsIgnoreCase("Signature")) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_FOR_SIGNATURE);
        } else {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_FOR_INITIAL);
        }
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

    @SuppressLint("StaticFieldLeak")
    private void sendUpdateToServer() {
        Log.d(TAG , "Thread Name in sendUpdateToServer: "+Thread.currentThread().getName());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(VideoActivity.this, "Sending update to server...", Toast.LENGTH_SHORT).show();
            }
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("from", userMeetingIdentifier);
                    jsonObject.put("to", "All");
                    jsonObject.put("messageType", "RefreshMeetingImages");
                    jsonObject.put("content", "RefreshMeetingImages");
                    videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                } catch (Exception e) {
                    Log.d("====Exception", "" + e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Handle UI updates after the background task if needed
            }
        }.execute();
    }

    /*Signature/Initial Capture and Replace Implementation*/

}