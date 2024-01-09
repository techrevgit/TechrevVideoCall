package com.techrev.videocall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    List< ParticipantDetailsModel.Result> resultValue=new ArrayList<>();
    Context mContext;

    public RecyclerViewAdapter(Context context, List< ParticipantDetailsModel.Result> results) {
//        mNames = names;
        this.resultValue = results;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(resultValue.get(position).getParticipantName()!=null)
        {
            if(resultValue.get(position).getParticipantName().length()>0)
            {
                holder.name.setText(resultValue.get(position).getParticipantName());
            }else {
                holder.name.setText("-");
            }
        }else {
            holder.name.setText("-");
        }

        if(resultValue.get(position).getPhone()!=null)
        {
            if(resultValue.get(position).getPhone().length()>0)
            {
                holder.phone.setText(resultValue.get(position).getPhone());
            }else {
                holder.phone.setText("-");
            }
        }else {
            holder.phone.setText("-");
        }

        if(resultValue.get(position).getEmail()!=null)
        {
            if(resultValue.get(position).getEmail().length()>0)
            {
                holder.email.setText(resultValue.get(position).getEmail());
            }else {
                holder.email.setText("-");
            }
        }else {
            holder.email.setText("-");
        }


        if(resultValue.get(position).getIpaddress()!=null)
        {
            if(resultValue.get(position).getIpaddress().length()>0)
            {
                holder.ip.setText(resultValue.get(position).getIpaddress());
            }else {
                holder.ip.setText("-");
            }
        }else {
            holder.ip.setText("-");
        }

        if(resultValue.get(position).getLatitude()!=null){
            if(resultValue.get(position).getLatitude().length()>0)
            {
                holder.latitude.setText(resultValue.get(position).getLatitude());
            }else {
                holder.latitude.setText("-");
            }
        }else {
            holder.latitude.setText("-");
        }


        if(resultValue.get(position).getLongitude()!=null){
            if(resultValue.get(position).getLongitude().length()>0)
            {
                holder.longtitude.setText(resultValue.get(position).getLongitude());
            }else {
                holder.longtitude.setText("-");
            }
        }else {
            holder.longtitude.setText("-");
        }

    }

    @Override
    public int getItemCount() {
        return resultValue.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,phone,email,ip,latitude,longtitude;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameValue);
            phone = itemView.findViewById(R.id.phoneValue);
            email = itemView.findViewById(R.id.emailValue);
            ip = itemView.findViewById(R.id.ipValue);
            latitude = itemView.findViewById(R.id.latitudeValue);
            longtitude = itemView.findViewById(R.id.longtitudeValue);
        }
    }
}
