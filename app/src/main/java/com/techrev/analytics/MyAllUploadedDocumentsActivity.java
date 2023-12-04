package com.techrev.analytics;

import android.content.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    List<JsonObject> selectedDocIdList = new ArrayList<>();
    private MyAllDocListAdapter adapter = null;
    LocalBroadcastManager mLocalBroadcastManager;
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
        getSupportActionBar().hide(); // hide the title bar
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
        try {
            getMyAllUploadedDocuments();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyAllDocListAdapter.isSelectAll = false;
        selectedDocIdList.clear();
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
                if (b) {
                    MyAllDocListAdapter.isSelectAll = true;
                } else {
                    MyAllDocListAdapter.isSelectAll = false;
                }
                if (adapter != null){
                    adapter.notifyDataSetChanged();
                }
            }
        });

        tv_add_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Add Document selected items size: "+selectedDocIdList.size());
                try {
                    updateExistingRequestDocument();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Initialize result launcher
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(
                            ActivityResult result)
                    {
                        // Initialize result data
                        Intent data = result.getData();
                        // check condition
                        if (data != null) {
                            // When data is not equal to empty
                            // Get PDf uri
                            Uri uri = data.getData();
                            String src = uri.getPath();
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                            cursor.moveToFirst();

                            String name = cursor.getString(nameIndex);
                            String size = Long.toString(cursor.getLong(sizeIndex));
                            String mime_type = getMimeType(uri);

                            //Toast.makeText(this, "Chosen file path: "+src, Toast.LENGTH_SHORT).show();
                            Log.e(TAG , "Chosen file path: "+src);
                            Log.e(TAG , "Chosen file name: "+name);
                            Log.e(TAG , "Chosen File Mime Type: "+getMimeType(uri));
                            View myView = getLayoutInflater().inflate(R.layout.fragment_file_chooser, null);
                            TextView tv_file_name = myView.findViewById(R.id.file_name);
                            tv_file_name.setText(name);
                            try {
                                uploadAttachedFile(bitmap , name , mime_type);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                });

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

    public void uploadAttachedFile(Bitmap bitmap , String file_name , String mime_type) throws IOException {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        String pathDetails = saveToInternalStorageFotAttachment(bitmap , file_name);
        String name = mypath.getName().replace("pdf",mime_type.split("/")[1]);
        Log.e("===pathDetails", "" + pathDetails);
        Log.e("===pathDetails", "Image Path:" + mypath);
        Log.e("===pathDetails", "Image Name:" + name);
        Log.e("===pathDetails", "Image Mime Type: "+mime_type);
        String extension = mypath.getName().split("\\.")[1];

        DateFormat dateFormat = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)");
        Calendar cal = Calendar.getInstance();
        String dateAndTime = dateFormat.format(cal.getTime());
        /*Converting bitmap to file*/
        File f = new File(getCacheDir(), name);
        f.createNewFile();

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
       // bitmap.compress(CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
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
        Log.d(TAG , "File Location: "+f);
        /*Converting bitmap to file*/

        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse(mime_type), f);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", name , requestBody1);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), name);
        UploadRequesterDocumentRequestModel model = new UploadRequesterDocumentRequestModel(
                                                                                "3",
                                                                                requestID,
                                                                                filename,
                                                                                userId,
                                                                                false,
                                                                                dateAndTime,
                                                                                "" ,
                                                                                false);
        Call<AttachedFileUploadResponseModel> call = serviceLocal.uploadRequesterDocument(
                                                                    authToken,
                                                                    fileToUpload,
                                                                    "3",
                                                                    requestID,
                                                                    name,
                                                                    userId,
                                                                    false,
                                                                    dateAndTime,
                                                                    "" ,
                                                                    false);
        call.enqueue(new Callback<AttachedFileUploadResponseModel>() {
            @Override
            public void onResponse(Call<AttachedFileUploadResponseModel> call, Response<AttachedFileUploadResponseModel> response) {
                dialog.dismiss();
                Log.d("====Inside1", "Success");
                Log.d("====Inside1", "Response:" + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.d(TAG, "UPLOADED DOC ID:" + response.body().getDocId());
                        Toast.makeText(MyAllUploadedDocumentsActivity.this, "File uploaded successfully!", Toast.LENGTH_SHORT).show();
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
        Call<CommonModel> responseBodyCall = serviceLocal.updateExistingRequestDocument(authToken, object.toString());
        responseBodyCall.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                selectedDocIdList.clear();
                if(response != null){
                    Log.d(TAG , "Updated document response: \n"+new Gson().toJson(response.body()));
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Toast.makeText(MyAllUploadedDocumentsActivity.this, "Document(s) added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyAllUploadedDocumentsActivity.this, "Something went wrong, please try again later!", Toast.LENGTH_SHORT).show();
                    }
                    MyAllDocListAdapter.isSelectAll = false;
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

    private String saveToInternalStorageFotAttachment(Bitmap bitmapImage , String file_name) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        mypath = new File(directory, file_name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void selectPDF()
    {
        // Initialize intent
        Intent intent
                = new Intent(Intent.ACTION_GET_CONTENT);
        // set type
        intent.setType("application/pdf");
        // Launch intent
        resultLauncher.launch(intent);
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
                    adapter = new MyAllDocListAdapter(MyAllUploadedDocumentsActivity.this,
                            response.body().getAllDocs(), new MyAllDocListAdapter.OnDocsSelected() {
                        @Override
                        public void onDocumentsSelected(List<JsonObject> docIdList) {
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