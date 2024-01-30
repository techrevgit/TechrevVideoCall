package com.techrev.videocall.ui.mydocuments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.techrev.videocall.network.NetworkInterface;
import com.techrev.videocall.R;
import com.techrev.videocall.models.RequestDocModel;
import com.techrev.videocall.network.RetrofitNetworkClass;

import org.json.JSONException;
import org.json.JSONObject;

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
    }

    private void initViews() {
        if (getIntent() != null) {
            authToken = getIntent().getStringExtra("AUTH_TOKEN");
            requestID = getIntent().getStringExtra("REQUEST_ID");
            userId = getIntent().getStringExtra("USER_ID");
        }
        iv_back = findViewById(R.id.iv_back);
        tv_add_document = findViewById(R.id.tv_add_document);
        rv_uploaded_documents = findViewById(R.id.rv_uploaded_documents);

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
                startActivity(it);
            }
        });
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
                            response.body().getRequestDocuments(), authToken, new RequestDocListAdapter.OnDeleteDocument() {
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

}