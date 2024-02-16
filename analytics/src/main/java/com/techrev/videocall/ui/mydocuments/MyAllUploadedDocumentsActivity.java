package com.techrev.videocall.ui.mydocuments;

import android.content.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.techrev.videocall.models.AttachedFileUploadResponseModel;
import com.techrev.videocall.models.CommonModel;
import com.techrev.videocall.models.MyAllDocListModel;
import com.techrev.videocall.network.NetworkInterface;
import com.techrev.videocall.R;
import com.techrev.videocall.network.RetrofitNetworkClass;

import org.json.JSONException;
import org.json.JSONObject;

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
    List<String> selectedDocIdList = new ArrayList<>();
    private MyAllDocListAdapter adapter = null;
    LocalBroadcastManager mLocalBroadcastManager;
    private String pathDoc="";
    private Uri uri;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.enotaryoncall.customer.action.close.notary_ended_call")){
                finish();
            }
        }
    };


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
        /*Added by Rupesh to close activity from service*/
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.enotaryoncall.customer.action.close.notary_ended_call");
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
        /*Added by Rupesh to close activity from service*/
        setContentView(R.layout.activity_my_all_uploaded_documents);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
    }


    private void initViews() {
        if (getIntent() != null) {
            authToken = getIntent().getStringExtra("AUTH_TOKEN");
            requestID = getIntent().getStringExtra("REQUEST_ID");
            userId = getIntent().getStringExtra("USER_ID");
        }
        iv_back = findViewById(R.id.iv_back);
        tv_add_document = findViewById(R.id.tv_add_document);
        cb_selectAll = findViewById(R.id.cb_selectAll);
        rv_uploaded_documents = findViewById(R.id.rv_uploaded_documents);
        ll_upload_Section = findViewById(R.id.ll_upload_Section);

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
                if (ActivityCompat.checkSelfPermission(
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
                }
                else {
                    // When permission is granted
                    // Create method
                    selectPDF();
                }
            }
        });

        try {
            getMyAllUploadedDocuments();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

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

    private void selectPDF()
    {
        // Initialize intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
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
        }
        else {
            // When permission is denied
            // Display toast
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
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
                            response.body().getAllDocs(), new MyAllDocListAdapter.OnDocsSelected() {
                        @Override
                        public void onDocumentsSelected(List<String> docIdList) {
                            selectedDocIdList = docIdList;
                            Log.d(TAG, "onDocumentsSelected: selected item size: "+selectedDocIdList.size());
                            Log.d(TAG , "Selected Doc IDs List Data: "+new Gson().toJson(selectedDocIdList));
                        }
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

}