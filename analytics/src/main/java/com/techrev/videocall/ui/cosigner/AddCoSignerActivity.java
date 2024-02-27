package com.techrev.videocall.ui.cosigner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.techrev.videocall.ui.internet.NoInternetActivity;
import com.techrev.videocall.R;
import com.techrev.videocall.models.SearchUserResponse;
import com.techrev.videocall.adapters.UserAddedListAdapter;
import com.techrev.videocall.adapters.UserListAdapter;
import com.techrev.videocall.ui.videocallroom.VideoCallManager;
import com.techrev.videocall.network.NetworkInterface;
import com.techrev.videocall.network.RetrofitNetworkClass;
import com.techrev.videocall.ui.videocallroom.VideoActivity;
import com.techrev.videocall.utils.Constants;
import com.techrev.videocall.utils.MySharedPreference;
import com.techrev.videocall.utils.NotarizationActionUpdateManger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddCoSignerActivity extends Activity {
    private RetrofitNetworkClass networkClass;
    private Retrofit retrofitLocal;
    private NetworkInterface serviceLocal;
    private String authToken = "";
    private String requestID = "";
    private String userId = "";
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView rvAddedList;
    private UserListAdapter userListAdapter;
    private UserAddedListAdapter userAddedListAdapter;
    private TextView tvEmpty;
    private TextView tvBtnAdd;

    //Added by Rupesh
    private ArrayList<CosignerDetailsModel.Cosigners> cosignersList;
    private int cosignerCount = 0;
    private static final String TAG = "AddCoSignerActivity";
    private String cosignerID = "";
    private MySharedPreference sharedPreference;
    private static Activity mActivity;
    private ConnectivityManager manager;
    private boolean isConnectedInRoom=false;
    private String userMeetingIdentifier = "";
    private ImageView ivBack;
    private VideoCallManager videoCallManager;
    private String isPrimarySigner = "";

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
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager.registerDefaultNetworkCallback(networkCallback);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*Added By Ruepsh*/
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        /*Added By Ruepsh*/
        setContentView(R.layout.activity_add_cosigner);
        if (getIntent() != null){
            authToken = getIntent().getStringExtra("AUTHORIZATION_TOKEN");
            requestID = getIntent().getStringExtra("REQUEST_ID");
            userId = getIntent().getStringExtra("USER_ID");
            userMeetingIdentifier = getIntent().getStringExtra("USER_MEETING_IDENTIFIER");
            isPrimarySigner = getIntent().getStringExtra("IS_PRIMARY_SIGNER");
        }
        networkClass = new RetrofitNetworkClass();
        retrofitLocal = networkClass.callingURL();
        serviceLocal = retrofitLocal.create(NetworkInterface.class);
        ivBack = findViewById(R.id.ivBack);
        EditText edSearch = findViewById(R.id.edSearch);
        TextView tvBtnSearch = findViewById(R.id.tvBtnSearch);
        tvBtnAdd = findViewById(R.id.tvBtnAdd);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        rvAddedList = findViewById(R.id.rvAddedList);
        tvEmpty = findViewById(R.id.tvEmpty);
        mActivity = this;
        sharedPreference = new MySharedPreference(this);

        try {
            getAllCoSignerDetailsByMeetingId(requestID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tvBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((cosignerCount + userAddedListAdapter.getItemCount()) < 5) {
                    String searchTxt = edSearch.getText().toString().trim();
                    if (TextUtils.isEmpty(searchTxt)) {
                        Toast.makeText(AddCoSignerActivity.this, "Please enter email or phone number", Toast.LENGTH_LONG).show();
                        edSearch.requestFocus();
                    } else {
                        try {
                            Long phone = Long.parseLong(searchTxt);
                            if (searchTxt.length() < 9 || searchTxt.length() > 10) {
                                Toast.makeText(AddCoSignerActivity.this, "Please enter valid phone number", Toast.LENGTH_LONG).show();
                                edSearch.requestFocus();
                            } else {
                                searchUser(searchTxt);
                            }
                        } catch (Exception e) {
                            if (!searchTxt.contains("@")) {
                                Toast.makeText(AddCoSignerActivity.this, "Please enter valid email address", Toast.LENGTH_LONG).show();
                                edSearch.requestFocus();
                            } else {
                                searchUser(searchTxt);
                            }
                        }

                    }
                }else {
                    Toast.makeText(AddCoSignerActivity.this, "You can not add more than 5 co-signers.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivBack.setOnClickListener(v -> finish());
        tvBtnAdd.setOnClickListener(v -> {
            if (userAddedListAdapter.getResults().size() > 0) {
                JSONObject requestJson = new JSONObject();
                try {
                    requestJson.put("requestId", requestID);
                    JSONArray jsonArray = new JSONArray();
                    for (SearchUserResponse.SearchUser user : userAddedListAdapter.getResults()) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("MiddleName", user.getMiddleName());
                        jsonObject.put("Phone", user.getPhone());
                        jsonObject.put("ProfilePictureDocId", user.getProfilePictureDocId());
                        jsonObject.put("addressId", user.getAddressId());
                        jsonObject.put("addressline1", user.getAddressline1());
                        jsonObject.put("addressline2", user.getAddressline2());
                        jsonObject.put("city", user.getCity());
                        jsonObject.put("country", user.getCountry());
                        jsonObject.put("displayName", user.getDisplayName());
                        jsonObject.put("email", user.getEmail());
                        jsonObject.put("firstName", user.getFirstName());
                        jsonObject.put("lastName", user.getLastName());
                        jsonObject.put("phonePrefixCode", user.getPhonePrefixCode());
                        jsonObject.put("pincode", user.getPincode());
                        jsonObject.put("state", user.getState());
                        jsonObject.put("userId", user.getUserId());
//                        jsonObject.put("profilepic","http://localhost:8080/api/downloadDewFile?DocId=02600ba512528b4313069f803df0b123.jpeg");
                        jsonArray.put(jsonObject);
                    }
                    requestJson.put("CosignerList", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addCoSigner(requestJson);
            }
        });
        rvAddedList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new LinearLayoutManager(AddCoSignerActivity.this));
        userAddedListAdapter = new UserAddedListAdapter(this, authToken, new UserAddedListAdapter.UserRemoveListener() {
            @Override
            public void onUserRemoved(SearchUserResponse.SearchUser searchUser) {
                userAddedListAdapter.removeUser(searchUser);
                enableDisableAddButton();
            }
        });
        /*userListAdapter = new UserListAdapter(AddCoSignerActivity.this, new UserListAdapter.UserAddListener() {
            @Override
            public void onUserAdded(SearchUserResponse.SearchUser searchUser) {
                if (userId.equalsIgnoreCase(searchUser.getUserId())) {
                    Toast.makeText(AddCoSignerActivity.this, "You can't add your self as a Co-Signer", Toast.LENGTH_LONG).show();
                } else if (userAddedListAdapter.isUserExist(searchUser)) {
                    Toast.makeText(AddCoSignerActivity.this, "User already added in the list", Toast.LENGTH_LONG).show();
                } else {
                    checkCosignerExist(searchUser);
                }
            }
        }, authToken, cosignerID);*/
        rvAddedList.setAdapter(userAddedListAdapter);

    }

    private void enableDisableAddButton() {
        if (userAddedListAdapter.getItemCount() > 0) {
            tvBtnAdd.setEnabled(true);
            tvBtnAdd.setBackgroundResource(R.drawable.bg_buttom);
            tvBtnAdd.setTextColor(Color.WHITE);
        } else {
            tvBtnAdd.setEnabled(false);
            tvBtnAdd.setBackgroundResource(R.drawable.bg_search_box);
            tvBtnAdd.setTextColor(Color.GRAY);
        }
    }

    private void searchUser(String searchTxt) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("emailOrPhone", searchTxt);
//            jsonObject.put("email", searchTxt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<SearchUserResponse> responseBodyCall1 = serviceLocal.searchUser(authToken, jsonObject.toString());
        progressBar.setVisibility(View.VISIBLE);
        responseBodyCall1.enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d("====onResponse", "Success");
                Log.d("====onResponse", "Response Code: " + response.code());
                Log.d("====onResponse", "Response Body: " + new Gson().toJson(response.body()));
                Log.d("====onResponse", "Response Body Results: " + new Gson().toJson(response.body().getResults()));
                SearchUserResponse searchUserResponse = response.body();
                Log.d(TAG , "CO-SIGNER ID : "+cosignerID);
                userListAdapter = new UserListAdapter(AddCoSignerActivity.this, new UserListAdapter.UserAddListener() {
                    @Override
                    public void onUserAdded(SearchUserResponse.SearchUser searchUser) {
                        if (userId.equalsIgnoreCase(searchUser.getUserId())) {
                            Toast.makeText(AddCoSignerActivity.this, "You can't add your self as a Co-Signer", Toast.LENGTH_LONG).show();
                        } else if (userAddedListAdapter.isUserExist(searchUser)) {
                            Toast.makeText(AddCoSignerActivity.this, "User already added in the list", Toast.LENGTH_LONG).show();
                        } else {
                            checkCosignerExist(searchUser);
                        }
                    }
                }, authToken, cosignerID);
                if (response.body().getResults() != null && response.body().getResults().size() > 0){
                    recyclerView.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                    cosignerID = response.body().getResults().get(0).getUserId();
                }else {
                    recyclerView.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                    userListAdapter.clearData();
                    return;
                }
                if (searchUserResponse != null && searchUserResponse.getResults() != null && !searchUserResponse.getResults().isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                    userListAdapter.setData(searchUserResponse.getResults());
                    recyclerView.setAdapter(userListAdapter);
                    userListAdapter.notifyDataSetChanged();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                    userListAdapter.clearData();
                }
                hideKeyboardFrom(recyclerView);
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("====onResponse", "Failure" + t.toString());
                recyclerView.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
                userListAdapter.clearData();
                hideKeyboardFrom(recyclerView);
                Toast.makeText(AddCoSignerActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void getAllCoSignerDetailsByMeetingId(String meeting_id) throws JSONException {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
        Log.e(TAG , "Meeting ID: "+meeting_id);

        JSONObject obj = new JSONObject();
        obj.put("requestId" , meeting_id);

        String data = obj.toString();
        Call<CosignerDetailsModel> responseBodyCall = serviceLocal.getAllCoSignerDetailsByMeetingId(authToken, data);
        responseBodyCall.enqueue(new Callback<CosignerDetailsModel>() {
            @Override
            public void onResponse(Call<CosignerDetailsModel> call, Response<CosignerDetailsModel> response) {
                if(dialog!=null){
                    dialog.dismiss();
                }

                if(response != null && response.body() != null){
                    cosignerCount = response.body().getCosigners().size();
                    if (cosignerCount > 0){
                        Log.d(TAG , "Co-Signer Details: "+new Gson().toJson(response.body().getCosigners().get(0)));
                        cosignerID = response.body().getCosigners().get(0).getUserId();
                        Log.d(TAG , "CO-SIGNER ID : "+cosignerID);
                        userListAdapter = new UserListAdapter(AddCoSignerActivity.this, new UserListAdapter.UserAddListener() {
                            @Override
                            public void onUserAdded(SearchUserResponse.SearchUser searchUser) {
                                if (userId.equalsIgnoreCase(searchUser.getUserId())) {
                                    Toast.makeText(AddCoSignerActivity.this, "You can't add your self as a Co-Signer", Toast.LENGTH_LONG).show();
                                } else if (userAddedListAdapter.isUserExist(searchUser)) {
                                    Toast.makeText(AddCoSignerActivity.this, "User already added in the list", Toast.LENGTH_LONG).show();
                                } else {
                                    checkCosignerExist(searchUser);
                                }
                            }
                        }, authToken, cosignerID);
                        // added one new parameter
                        recyclerView.setLayoutManager(new LinearLayoutManager(AddCoSignerActivity.this));
                        recyclerView.setAdapter(userListAdapter);
                        userListAdapter.notifyDataSetChanged();
                    }
                    Log.e(TAG , "Co-signers response size: "+response.body().getCosigners().size());
                }
            }

            @Override
            public void onFailure(Call<CosignerDetailsModel> call, Throwable t) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                Log.d("====onResponse", "Failure" + t.toString());
            }
        });
    }

    private void addCoSigner(JSONObject requestJson) {

        Call<ResponseBody> responseBodyCall1 = serviceLocal.addCosignerDuringMeeting(authToken, requestJson.toString());
        progressBar.setVisibility(View.VISIBLE);
        responseBodyCall1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null){
                    Log.d("====onResponse", "success" + response.code());
                    Log.d("====onResponse", "success" + response.body().toString());
                    progressBar.setVisibility(View.GONE);
                    cosignerCount++;
                    Toast.makeText(AddCoSignerActivity.this, "Invitation email sent to Co-Signer", Toast.LENGTH_LONG).show();
                    NotarizationActionUpdateManger.updateNotarizationAction(
                            AddCoSignerActivity.this, authToken,
                            requestID, "", userId, isPrimarySigner,
                            "42", "1", "");
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("====onResponse", "Failure" + t.toString());
                Toast.makeText(AddCoSignerActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void checkCosignerExist(SearchUserResponse.SearchUser selectedUser) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("requestId", requestID);
            jsonObject.put("userId", selectedUser.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<SearchUserResponse> responseBodyCall1 = serviceLocal.checkCosignerExist(authToken, jsonObject.toString());
        progressBar.setVisibility(View.VISIBLE);
        responseBodyCall1.enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                Log.d("====onResponse", "success" + response.code());
                Log.d("====onResponse", "success" + response.body().toString());
                progressBar.setVisibility(View.GONE);
                SearchUserResponse searchUserResponse = response.body();
                if (searchUserResponse == null || searchUserResponse.getResults() == null || searchUserResponse.getResults().isEmpty()) {
                    userAddedListAdapter.setData(selectedUser);
                    rvAddedList.setAdapter(userAddedListAdapter);
                    userAddedListAdapter.notifyDataSetChanged();
                    enableDisableAddButton();
                } else {
                    Toast.makeText(AddCoSignerActivity.this, "User already added as a co-signer.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("====onResponse", "Failure" + t.toString());
                Toast.makeText(AddCoSignerActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*Added By Rupesh*/

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreference.getBoolean(Constants.CALL_ENDED_IN_BACKGROUND)){
            exitFromTheRoom();
        }

        if (!isInternetAvailable()){
            openNoInternetScreen();
        }else {
            closeNoInternetScreen();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreference.setBoolean(Constants.COSIGNER_ACTIVITY_IN_FOREGROUND , false);
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }

    //Added By Rupesh
    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            // this ternary operation is not quite true, because non-metered doesn't yet mean, that it's wifi
            // nevertheless, for simplicity let's assume that's true
            if (NoInternetActivity.activity != null) {
                NoInternetActivity.activity.finish();
            }
            Log.i("vvv", "connected to " + (manager.isActiveNetworkMetered() ? "LTE" : "WIFI"));
        }

        @Override
        public void onLost(Network network) {
            super.onLost(network);
            Log.i("vvv", "losing active connection");
            if (mActivity != null){
                Log.d("BaseApplication" , "Current Activity: "+mActivity.getClass().getSimpleName());
                if (mActivity.getClass().getSimpleName().equals(VideoActivity.class.getSimpleName())){
                    openNoInternetScreen();
                }
            }
        }
    };

    public void openNoInternetScreen() {
        if (!NoInternetActivity.isInstanceExists()) {
            Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void closeNoInternetScreen(){
        if (NoInternetActivity.activity != null) {
            NoInternetActivity.activity.finish();
        }
    }

    public boolean isInternetAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else {
            return false;
        }
    }

    public void exitFromTheRoom() {
        AddCoSignerActivity.this.finish();
    }

    public static String removePrefix(String s, String prefix) {
        if (s != null && prefix != null && s.startsWith(prefix)) {
            return s.substring(prefix.length());
        }
        return s;
    }

    public static AddCoSignerActivity getAddCoSignerActivityContext(){
        return new AddCoSignerActivity();
    }

    /*Added By Rupesh*/
}




