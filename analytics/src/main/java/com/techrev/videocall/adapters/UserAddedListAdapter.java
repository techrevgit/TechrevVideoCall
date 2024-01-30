package com.techrev.videocall.adapters;

import static com.techrev.videocall.ui.videocallroom.VideoActivity.BASE_URL_VAL;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.techrev.videocall.R;
import com.techrev.videocall.models.SearchUserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;



public class UserAddedListAdapter extends RecyclerView.Adapter<UserAddedListAdapter.ViewHolder> {
    private List<SearchUserResponse.SearchUser> results = new ArrayList<>();
    private Context context;
    private String authToken;
    private UserRemoveListener userRemoveListener;

    public UserAddedListAdapter(Context context,String authToken,UserRemoveListener userRemoveListener) {
        this.context = context;
        this.authToken = authToken;
        this.userRemoveListener = userRemoveListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_cosigner_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchUserResponse.SearchUser searchUser = results.get(position);
        holder.tvUserName.setText(searchUser.getFirstName()+" "+searchUser.getLastName());
        if(TextUtils.isEmpty(searchUser.getProfilePictureDocId())){
            setColorAndText(searchUser.getFirstName()+" "+searchUser.getLastName(),holder.tvInitial);
            holder.tvInitial.setVisibility(View.VISIBLE);
            holder.ivImage.setVisibility(View.INVISIBLE);
        }else{
            GlideUrl glideUrl = new GlideUrl(BASE_URL_VAL + "downloadDewFile/?DocId="+searchUser.getProfilePictureDocId(),
                    new LazyHeaders.Builder()
                            .addHeader("Authorization", authToken)
                            .build());
            Glide.with(context)
                    .load(glideUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.ivImage);
            holder.tvInitial.setVisibility(View.INVISIBLE);
            holder.ivImage.setVisibility(View.VISIBLE);
        }
        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRemoveListener.onUserRemoved(searchUser);
            }
        });
    }

    public void removeUser(SearchUserResponse.SearchUser searchUser){
        results.remove(searchUser);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setData(SearchUserResponse.SearchUser user) {
        this.results.add(user);
        notifyDataSetChanged();
    }

    public boolean isUserExist(SearchUserResponse.SearchUser user){
        for(SearchUserResponse.SearchUser data: results){
            if(data.getUserId().equalsIgnoreCase(user.getUserId())){
                return  true;
            }
        }
        return  false;
    }

    public void clearData() {
        this.results.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName,tvInitial;
        ImageView ivImage, ivRemove;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivRemove = itemView.findViewById(R.id.ivRemove);
            tvInitial = itemView.findViewById(R.id.tvInitial);
        }
    }

    public List<SearchUserResponse.SearchUser> getResults() {
        return results;
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

    private void setColorAndText(String name,TextView textView) {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(100), rnd.nextInt(100), rnd.nextInt(100));
        textView.setBackgroundColor(color);
        textView.setText(getInitial(name));
    }
    public interface  UserRemoveListener{
        void onUserRemoved(SearchUserResponse.SearchUser searchUser);
    }
}
