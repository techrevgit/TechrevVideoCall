package com.techrev.videocall.ui.mydocuments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.techrev.videocall.R;
import com.techrev.videocall.dialogfragments.CaptureSignerInitialDialogFragment;
import com.techrev.videocall.dialogfragments.CaptureSignerSignatureDialogFragment;
import com.techrev.videocall.models.DataModel;
import com.techrev.videocall.models.RequestDocModel;
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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyCurrentUploadedDocumentsActivity extends AppCompatActivity {

    private static final String TAG = "MyCurrentUploadedDocume";
    private ImageView iv_back;
    private TextView tv_add_document;
    private RecyclerView rv_uploaded_documents;
    private String authToken, requestID;
    static RetrofitNetworkClass networkClass = new RetrofitNetworkClass();
    static Retrofit retrofitLocal = networkClass.callingURL();
    static NetworkInterface serviceLocal = retrofitLocal.create(NetworkInterface.class);
    private String userId = "";
    private boolean IS_REQUEST_CREATED_BY_CUSTOMER = false;
    private String isPrimarySigner = "";
    LocalBroadcastManager mLocalBroadcastManager;
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

        setContentView(R.layout.activity_my_current_uploaded_documents);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            getRequestDocumentsById();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

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

    private void initViews() {
        if (getIntent() != null) {
            authToken = getIntent().getStringExtra("AUTH_TOKEN");
            requestID = getIntent().getStringExtra("REQUEST_ID");
            userId = getIntent().getStringExtra("USER_ID");
            isPrimarySigner = getIntent().getStringExtra("IS_PRIMARY_SIGNER");
            IS_REQUEST_CREATED_BY_CUSTOMER = getIntent().getBooleanExtra("IS_REQUEST_CREATED_BY_CUSTOMER" , false);
            isWitness = getIntent().getStringExtra("IS_WITNESS");
            isCoSigner = getIntent().getBooleanExtra("IS_COSIGNER", false);
            customerType = getIntent().getStringExtra("CUSTOMER_TYPE");
            userMeetingIdentifier = getIntent().getStringExtra("USER_MEETING_IDENTIFIER");
        }
        iv_back = findViewById(R.id.iv_back);
        tv_add_document = findViewById(R.id.tv_add_document);
        rv_uploaded_documents = findViewById(R.id.rv_uploaded_documents);

        sharedPreference = new MySharedPreference(MyCurrentUploadedDocumentsActivity.this);
        videoCallModel = DataManager.getInstance().getVideoCallModel();
        dataModelList = DataManager.getInstance().getDataModelList();

        registerBroadcastReceivers();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_add_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MyCurrentUploadedDocumentsActivity.this , MyAllUploadedDocumentsActivity.class);
                it.putExtra("REQUEST_ID" , requestID);
                it.putExtra("AUTH_TOKEN" , authToken);
                it.putExtra("USER_ID" , userId);
                it.putExtra("IS_PRIMARY_SIGNER", isPrimarySigner);
                it.putExtra("IS_WITNESS", isWitness);
                it.putExtra("CUSTOMER_TYPE", customerType);
                it.putExtra("IS_COSIGNER", isCoSigner);
                it.putExtra("USER_MEETING_IDENTIFIER" , userMeetingIdentifier);
                startActivity(it);
            }
        });
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

    private void getRequestDocumentsById() throws JSONException {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
        Log.e(TAG , "Meeting ID: "+requestID);
        JSONObject obj = new JSONObject();
        obj.put("requestId" , requestID);
        String data = obj.toString();
        Call<RequestDocModel> responseBodyCall = serviceLocal.getRequestDocumentsById(authToken, data);
        responseBodyCall.enqueue(new Callback<RequestDocModel>() {
            @Override
            public void onResponse(Call<RequestDocModel> call, Response<RequestDocModel> response) {
                if(dialog!=null){
                    dialog.dismiss();
                }

                if(response != null){
                    Log.d(TAG , "Added Documents Data: \n"+new Gson().toJson(response.body()));
                    RequestDocListAdapter adapter = new RequestDocListAdapter(MyCurrentUploadedDocumentsActivity.this,
                            response.body().getRequestDocuments(), authToken, IS_REQUEST_CREATED_BY_CUSTOMER, userId, isPrimarySigner, requestID, new RequestDocListAdapter.OnDeleteDocument() {
                        @Override
                        public void onDeleteCompleted() {
                            try {
                                getRequestDocumentsById();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(MyCurrentUploadedDocumentsActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv_uploaded_documents.setLayoutManager(manager);
                    rv_uploaded_documents.setAdapter(adapter);
                    rv_uploaded_documents.addItemDecoration(new DividerItemDecoration(MyCurrentUploadedDocumentsActivity.this,
                            DividerItemDecoration.VERTICAL));
                }
            }

            @Override
            public void onFailure(Call<RequestDocModel> call, Throwable t) {
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
        CaptureSignerSignatureDialogFragment captureSignerSignatureDialogFragment = new CaptureSignerSignatureDialogFragment(MyCurrentUploadedDocumentsActivity.this, userMeetingIdentifier, videoCallModel, authToken, requestID, userId, customerType, new CaptureSignerSignatureDialogFragment.OptionSelectionInterface() {
            @Override
            public void onOptionSelected(int selectedOption) {
                //1 = Capture through camera & 2 = Draw through whiteboard
                if (selectedOption == 1) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MyCurrentUploadedDocumentsActivity.this);
                    AlertDialog dialog = builder.setTitle("Capture Signature")
                            .setMessage("Are you sure, you want to capture your signature?")
                            .setCancelable(false)
                            .setPositiveButton("Ok, Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Toast.makeText(MyCurrentUploadedDocumentsActivity.this, "Opening camera", Toast.LENGTH_SHORT).show();
                                    if (ContextCompat.checkSelfPermission(MyCurrentUploadedDocumentsActivity.this, Manifest.permission.CAMERA)
                                            == PackageManager.PERMISSION_DENIED) {
                                        captureImageThroughCamera("Signature");
                                    } else {
                                        ActivityCompat.requestPermissions(MyCurrentUploadedDocumentsActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE_FOR_SIGNATURE);
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
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MyCurrentUploadedDocumentsActivity.this.getColor(R.color.color_primary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MyCurrentUploadedDocumentsActivity.this.getColor(R.color.red));
                    }

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MyCurrentUploadedDocumentsActivity.this);
                    AlertDialog dialog = builder.setTitle("Draw Signature")
                            .setMessage("Are you sure, you want to draw your signature?")
                            .setCancelable(false)
                            .setPositiveButton("Ok, Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Toast.makeText(MyCurrentUploadedDocumentsActivity.this, "Opening whiteboard", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(MyCurrentUploadedDocumentsActivity.this, WhiteBoardActivity.class);
                                    it.putExtra("REQUEST_ID", requestID);
                                    it.putExtra("AUTH_TOKEN", authToken);
                                    it.putExtra("USER_ID", userId);
                                    it.putExtra("CUSTOMER_TYPE", customerType);
                                    it.putExtra("TYPE", "1");
                                    it.putExtra("USER_MEETING_IDENTIFIER", userMeetingIdentifier);
                                    /*it.putExtra("VIDEO_CALL_MODEL_OBJ" , videoCallModel);*/
                                    MyCurrentUploadedDocumentsActivity.this.startActivityForResult(it, SIGNATURE_INITIAL_CAPTURE_CODE);
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
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MyCurrentUploadedDocumentsActivity.this.getColor(R.color.color_primary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MyCurrentUploadedDocumentsActivity.this.getColor(R.color.red));
                    }

                }
            }
        }, new CaptureSignerSignatureDialogFragment.DialogStateInterface() {
            @Override
            public void onDialogClosed(boolean isClosed) {
                IS_SIGNATURE_CAPTURE_IN_PROGRESS = false;
            }
        });
        FragmentTransaction ft = MyCurrentUploadedDocumentsActivity.this.getFragmentManager().beginTransaction();
        Fragment prev = MyCurrentUploadedDocumentsActivity.this.getFragmentManager().findFragmentByTag("signature_dialog");
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
        CaptureSignerInitialDialogFragment captureSignerInitialDialogFragment = new CaptureSignerInitialDialogFragment(MyCurrentUploadedDocumentsActivity.this, userMeetingIdentifier, videoCallModel, authToken, requestID, userId, customerType, new CaptureSignerInitialDialogFragment.OptionSelectionInterface() {
            @Override
            public void onOptionSelected(int selectedOption) {
                //1 = Capture through camera & 2 = Draw through whiteboard
                if (selectedOption == 1) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Toast.makeText(MyCurrentUploadedDocumentsActivity.this, "Opening camera", Toast.LENGTH_SHORT).show();
                                    if (ContextCompat.checkSelfPermission(MyCurrentUploadedDocumentsActivity.this, Manifest.permission.CAMERA)
                                            == PackageManager.PERMISSION_DENIED) {
                                        captureImageThroughCamera("Initial");
                                    } else {
                                        ActivityCompat.requestPermissions(MyCurrentUploadedDocumentsActivity.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE_FOR_INITIAL);
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MyCurrentUploadedDocumentsActivity.this);
                    AlertDialog dialog = builder.setTitle("Capture Initial")
                            .setMessage("Are you sure, you want to capture your initial?")
                            .setPositiveButton("Ok, Proceed", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener)
                            .setCancelable(false)
                            .show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MyCurrentUploadedDocumentsActivity.this.getColor(R.color.color_primary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MyCurrentUploadedDocumentsActivity.this.getColor(R.color.red));
                    }

                } else {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Toast.makeText(MyCurrentUploadedDocumentsActivity.this, "Opening camera", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(MyCurrentUploadedDocumentsActivity.this, WhiteBoardActivity.class);
                                    it.putExtra("REQUEST_ID" , requestID);
                                    it.putExtra("AUTH_TOKEN" , authToken);
                                    it.putExtra("USER_ID" , userId);
                                    it.putExtra("CUSTOMER_TYPE" , customerType);
                                    it.putExtra("TYPE" , "0");
                                    it.putExtra("USER_MEETING_IDENTIFIER" , userMeetingIdentifier);
                                    /*it.putExtra("VIDEO_CALL_MODEL_OBJ" , videoCallModel);*/
                                    MyCurrentUploadedDocumentsActivity.this.startActivityForResult(it , SIGNATURE_INITIAL_CAPTURE_CODE);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MyCurrentUploadedDocumentsActivity.this);
                    AlertDialog dialog = builder.setTitle("Draw Initial")
                            .setMessage("Are you sure, you want to draw your initial?")
                            .setPositiveButton("Ok, Proceed", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener)
                            .setCancelable(false)
                            .show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MyCurrentUploadedDocumentsActivity.this.getColor(R.color.color_primary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MyCurrentUploadedDocumentsActivity.this.getColor(R.color.red));
                    }

                }
            }
        }, new CaptureSignerInitialDialogFragment.DialogStateInterface() {
            @Override
            public void onDialogClosed(boolean isClosed) {
                IS_INITIAL_CAPTURE_IN_PROGRESS = false;
            }
        });
        FragmentTransaction ft1 = MyCurrentUploadedDocumentsActivity.this.getFragmentManager().beginTransaction();
        Fragment prev1 = MyCurrentUploadedDocumentsActivity.this.getFragmentManager().findFragmentByTag("initial_dialog");
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

            AlertDialog.Builder builder = new AlertDialog.Builder(MyCurrentUploadedDocumentsActivity.this);
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
                                                MyCurrentUploadedDocumentsActivity.this, authToken,
                                                requestID, "", userId, customerType,
                                                finalSignatureActionID, "1", dataModelList.get(position).getDocid(),
                                                dataModelList.get(position).getPageNumber());
                                    } else {
                                        // For Initial
                                        NotarizationActionUpdateManger.updateNotarizationAction(
                                                MyCurrentUploadedDocumentsActivity.this, authToken,
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
                                        MyCurrentUploadedDocumentsActivity.this, authToken,
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
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MyCurrentUploadedDocumentsActivity.this.getColor(R.color.color_primary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MyCurrentUploadedDocumentsActivity.this.getColor(R.color.red));
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

    @SuppressLint("StaticFieldLeak")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "Inside onActivityResult");

        new AsyncTask<Object, Void, Void>() {

            @Override
            protected Void doInBackground(Object... params) {

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
                    Intent it = new Intent(MyCurrentUploadedDocumentsActivity.this, CameraActivity.class);
                    it.putExtra("REQUEST_ID" , requestID);
                    it.putExtra("AUTH_TOKEN" , authToken);
                    it.putExtra("USER_ID" , userId);
                    it.putExtra("CUSTOMER_TYPE" , customerType);
                    it.putExtra("TYPE" , "1");
                    it.putExtra("USER_MEETING_IDENTIFIER" , userMeetingIdentifier);
                    it.putExtra("bitmap", photo);
                    it.putExtra("resizedImageBytes", imageInBytes);
                    /*it.putExtra("VIDEO_CALL_MODEL_OBJ" , videoCallModel);*/
                    MyCurrentUploadedDocumentsActivity.this.startActivityForResult(it , SIGNATURE_INITIAL_CAPTURE_CODE);
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
                    Intent it = new Intent(MyCurrentUploadedDocumentsActivity.this, CameraActivity.class);
                    it.putExtra("REQUEST_ID" , requestID);
                    it.putExtra("AUTH_TOKEN" , authToken);
                    it.putExtra("USER_ID" , userId);
                    it.putExtra("CUSTOMER_TYPE" , customerType);
                    it.putExtra("TYPE" , "0");
                    it.putExtra("USER_MEETING_IDENTIFIER" , userMeetingIdentifier);
                    it.putExtra("bitmap", photo);
                    it.putExtra("resizedImageBytes", imageInBytes);
                    /*it.putExtra("VIDEO_CALL_MODEL_OBJ" , videoCallModel);*/
                    MyCurrentUploadedDocumentsActivity.this.startActivityForResult(it , SIGNATURE_INITIAL_CAPTURE_CODE);
                }

                return null;
            }

        }.execute();
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