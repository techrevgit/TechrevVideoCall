package com.techrev.videocall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerViewParticipantAdapter extends RecyclerView.Adapter<RecyclerViewParticipantAdapter.ViewHolder> {
    Context mContext;
    ONClickInterfaceParticipant onClickInterfaceParticipant;
    ArrayList<TechrevRemoteParticipant> remoteParticipantList;

    public RecyclerViewParticipantAdapter(Context context, ArrayList<TechrevRemoteParticipant> remoteParticipantList1, ONClickInterfaceParticipant onClickInterfaceParticipant1) {
        this.remoteParticipantList = remoteParticipantList1;
        this.onClickInterfaceParticipant = onClickInterfaceParticipant1;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_participant_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("====remoteParticipant", "Size" + remoteParticipantList.size());

        try {
            Log.d("====remoteParticipant", "Try");
            TechrevRemoteParticipant remoteParticipant;
            remoteParticipant = remoteParticipantList.get(position);

            if(Constants.isCaptureImageEnable) {
                holder.imgBtnOne.setVisibility(View.VISIBLE);
                // holder.name.setText(remoteParticipantList.get(position).getIdentity());
                holder.imgBtnOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("===imgBtn1", "Capture");

                        if (!remoteParticipant.getTechRevVideoEnable()) {
                            AlertDialog.Builder alert1 = new AlertDialog.Builder(mContext);
                            alert1.setTitle("Alert");
                            alert1.setMessage("Participant's camera is turned off so you cannot capture the image.");
                            alert1.setPositiveButton("Ok", null);
                            alert1.show();

                        } else {
                            onClickInterfaceParticipant.onClickParticipantCapture(position);
                        }


                    }
                });
            }else{
                holder.imgBtnOne.setVisibility(View.GONE);
            }

            String s = remoteParticipantList.get(position).remoteParticipant.getIdentity();
            Log.d("====remoteParticipant", "s:" + s);
            if (s != null && s.length() > 0) {
                //Log.d("====INside","IF");
                //Log.d("====INside","s:"+s);
                try {
                    List<String> splitString = Arrays.asList(s.split("\\-"));
                    //Log.d("====INside","splitString:"+splitString.size());
                    if (splitString.size() > 0) {
                        holder.name.setText(splitString.get(1));
                    } else {
                        String resVal = removePrefix(s, "participant-");
                        holder.name.setText(resVal);
                    }
                } catch (Exception e) {   //Log.d("====Exception","Exception:"+e.toString());
                    holder.name.setText(s);
                }

            } else {
                //Log.d("====INside","ELse");
                holder.name.setText(remoteParticipantList.get(position).remoteParticipant.getIdentity());
            }

            if (remoteParticipant != null) {
                if (remoteParticipant.getTechRevAudioEnable()) {
                    holder.imgBtnTwo.setImageDrawable(ContextCompat.getDrawable(
                            mContext, R.drawable.new_mic_black));

                } else {

                    holder.imgBtnTwo.setImageDrawable(ContextCompat.getDrawable(
                            mContext, R.drawable.new_mic_off));


                }


                if (remoteParticipant.getTechRevVideoEnable()) {
                    holder.imgBtnThree.setImageDrawable(ContextCompat.getDrawable(
                            mContext, R.drawable.new_videocam_on));

                } else {
                    holder.imgBtnThree.setImageDrawable(ContextCompat.getDrawable(
                            mContext, R.drawable.new_videocam_off));


                }


            } else {
                Log.d("====remoteParticipant", "ELSE null");
            }


            holder.imgBtnTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("===imgBtn2", "Audio");
                    //onClickInterfaceParticipant.onClickParticipantAudio(position);
                    if (!remoteParticipant.getTechRevAudioEnable()) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                        alert.setTitle("Alert");
                        alert.setMessage("You cannot unmute this participant.");
                        alert.setPositiveButton("Ok", null);
                        alert.show();

                    } else {
                        onClickInterfaceParticipant.onClickParticipantAudio(position);
                    }

                }
            });
            holder.imgBtnThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("===imgBtn3", "Video");
                    ///onClickInterfaceParticipant.onClickParticipantVideo(position);
                    if (!remoteParticipant.getTechRevVideoEnable()) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(mContext);
                        alert1.setTitle("Alert");
                        alert1.setMessage("You cannot turn on camera of this participant.");
                        alert1.setPositiveButton("Ok", null);
                        alert1.show();

                    } else {
                        onClickInterfaceParticipant.onClickParticipantVideo(position);
                    }

                }
            });
            holder.imgBtnFour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("===imgBtn4", "End Call");

                    // your code.
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    onClickInterfaceParticipant.onClickParticipantCallEnd(position);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Do you want to remove this participant from this call?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();


                }
            });


        } catch (Exception e) {
            Log.d("====remoteParticipant", "Exc" + e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return remoteParticipantList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageButton imgBtnOne, imgBtnTwo, imgBtnThree, imgBtnFour;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
            imgBtnOne = itemView.findViewById(R.id.imgBtnOne);
            imgBtnTwo = itemView.findViewById(R.id.imgBtnTwo);
            imgBtnThree = itemView.findViewById(R.id.imgBtnThree);
            imgBtnFour = itemView.findViewById(R.id.imgBtnFour);

        }
    }

    public static String removePrefix(String s, String prefix) {
        if (s != null && prefix != null && s.startsWith(prefix)) {
            return s.substring(prefix.length());
        }
        return s;
    }

}


interface ONClickInterfaceParticipant {
    void onClickParticipantCapture(int index);

    void onClickParticipantAudio(int index);

    void onClickParticipantVideo(int index);

    void onClickParticipantCallEnd(int index);
}