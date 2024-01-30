package com.techrev.videocall.adapters;

import static com.techrev.videocall.ui.videocallroom.VideoActivity.BASE_URL_VAL;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.gson.Gson;
import com.techrev.videocall.R;
import com.techrev.videocall.models.SearchUserResponse;
import com.techrev.videocall.network.NetworkInterface;
import com.techrev.videocall.network.RetrofitNetworkClass;
import com.techrev.videocall.ui.cosigner.CosignerVerificationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;


import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private static final String TAG = "UserListAdapter";
    private List<SearchUserResponse.SearchUser> results = new ArrayList<>();
    private Context context;
    private UserAddListener userAddListener;
    private  String authToken;
    // Added by Rupesh
    private String userId = "";
    SearchUserResponse.SearchUser searchUser;
    static RetrofitNetworkClass networkClass = new RetrofitNetworkClass();
    static Retrofit retrofitLocal = networkClass.callingURL();
    static NetworkInterface serviceLocal = retrofitLocal.create(NetworkInterface.class);

    public UserListAdapter(Context context, UserAddListener userAddListener, String authToken, String userID) {
        this.context = context;
        this.userAddListener = userAddListener;
        this.authToken = authToken;
        this.userId = userID;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cosigner_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        searchUser = results.get(position);
        holder.tvUserName.setText(searchUser.getFirstName()+" "+searchUser.getLastName());
        if(TextUtils.isEmpty(searchUser.getProfilePictureDocId())){
            setColorAndText(searchUser.getFirstName()+" "+searchUser.getLastName(),holder.tvInitial);
            holder.tvInitial.setVisibility(View.VISIBLE);
            holder.ivUser.setVisibility(View.GONE);
        }else{
            GlideUrl glideUrl = new GlideUrl(BASE_URL_VAL + "downloadDewFile/?DocId="+searchUser.getProfilePictureDocId(),
                    new LazyHeaders.Builder()
                            .addHeader("Authorization", authToken)
                            .build());
            Glide.with(context)
                    .load(glideUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.ivUser);
            holder.tvInitial.setVisibility(View.GONE);
            holder.ivUser.setVisibility(View.VISIBLE);
        }
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkIfKBADone();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setData(List<SearchUserResponse.SearchUser> data) {
        this.results.clear();
        this.results = data;
        notifyDataSetChanged();
    }

    public void clearData() {
        this.results.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName,tvInitial;
        ImageView ivUser, ivAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            ivUser = itemView.findViewById(R.id.ivUser);
            ivAdd = itemView.findViewById(R.id.ivAdd);
            tvInitial = itemView.findViewById(R.id.tvInitial);
        }
    }
    public interface  UserAddListener{
        void onUserAdded(SearchUserResponse.SearchUser searchUser);
    }
    private void setColorAndText(String name,TextView textView) {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(100), rnd.nextInt(100), rnd.nextInt(100));
        textView.setBackgroundColor(color);
        textView.setText(getInitial(name));
    }

    private String getInitial(String name) {
        Pattern pattern = Pattern.compile(" ");
        String[] names = pattern.split(name);
        if (names.length > 1) {
            return (names[0].charAt(0) + "" + names[names.length - 1].charAt(0)).toUpperCase();
        } else {
            return (name.charAt(0) + "").toUpperCase();
        }
    }

    private void checkIfKBADone() throws JSONException {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
        Log.d(TAG , "CO-SIGNER ID : "+userId);
        JSONObject obj = new JSONObject();
        obj.put("userId" , userId);
        String data = obj.toString();
        Call<CosignerVerificationModel> responseBodyCall = serviceLocal.checkIfKBADoneOfUserByID(authToken, data);
        responseBodyCall.enqueue(new Callback<CosignerVerificationModel>() {
            @Override
            public void onResponse(Call<CosignerVerificationModel> call, Response<CosignerVerificationModel> response) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                if (response.body() != null){
                    Log.d(TAG , "*RESPONSE* "+new Gson().toJson(response.body()));
                    if (response.body().getResults().size() > 0){
                        Log.d(TAG , "isKbaVerified : "+response.body().getResults().get(0).isKbaVerified());
                        Log.d(TAG , "isDocumentVerified : "+response.body().getResults().get(0).isDocumentVerified());
                        if (response.body().getResults().get(0).isKbaVerified() == 1 &&
                                response.body().getResults().get(0).isDocumentVerified() == 1){
                            userAddListener.onUserAdded(searchUser);
                        }else {
                            Toast.makeText(context, "This user has not completed KBA & ID Verification so you can not add as Co-Signer.", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(context, "This user has not completed KBA & ID Verification so you can not add as Co-Signer.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CosignerVerificationModel> call, Throwable t) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                Log.d("====onResponse", "Failure" + t.toString());
            }
        });
    }
}
