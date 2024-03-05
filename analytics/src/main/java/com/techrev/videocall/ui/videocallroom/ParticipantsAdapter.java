package com.techrev.videocall.ui.videocallroom;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.techrev.videocall.R;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class ParticipantsAdapter extends RecyclerView.Adapter {

    List<TechrevRemoteParticipant> remoteParticipantList;
    private VideoActivity videoActivity;
    onClickInterface onClickInterface;
    private int rowIndex = 0;
    private boolean isMyViewActive;

    public ParticipantsAdapter(ArrayList<TechrevRemoteParticipant> remoteParticipantList, VideoActivity videoActivity, onClickInterface onClickInterface, boolean viewActive) {
        this.remoteParticipantList = remoteParticipantList;
        this.videoActivity = videoActivity;
        this.onClickInterface = onClickInterface;
        this.isMyViewActive = viewActive;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(this.videoActivity.getApplication().getResources().getIdentifier("participant_list_row", "layout", this.videoActivity.package_name), viewGroup, false);
        return new MyViewHolder(itemView, this.videoActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int i) {

        MyViewHolder viewHolder = (MyViewHolder) holder;
        holder.setIsRecyclable(false);
        TechrevRemoteParticipant participant = remoteParticipantList.get(i);
        Random rnd = new Random();
        viewHolder.imgAudio.bringToFront();
        viewHolder.imgVideo.bringToFront();

        //remoteParticipantList.get(i).remoteParticipant.getAudioTracks().get(i).getAudioTrack().isEnabled();

        if (participant != null) {
            if (participant.getTechRevAudioEnable()) {
                ((MyViewHolder) holder).imgAudio.setImageDrawable(ContextCompat.getDrawable(
                        this.videoActivity, R.drawable.new_mic_white));

            } else {

                ((MyViewHolder) holder).imgAudio.setImageDrawable(ContextCompat.getDrawable(
                        this.videoActivity, R.drawable.new_mic_off_white));

//                if(remoteParticipantList.get(i).remoteParticipant.getAudioTracks().get(0).getAudioTrack().isEnabled())
//                {
//                    ((MyViewHolder) holder).imgAudio.setImageDrawable(ContextCompat.getDrawable(
//                            this.videoActivity, R.drawable.new_mic_white));
//
//                }else {
//                    ((MyViewHolder) holder).imgAudio.setImageDrawable(ContextCompat.getDrawable(
//                            this.videoActivity, R.drawable.new_mic_off_white));
//
//                }

            }


            if (participant.getTechRevVideoEnable()) {
               viewHolder.imgVideo.setImageDrawable(ContextCompat.getDrawable(
                        this.videoActivity, R.drawable.new_videocam_white));
                viewHolder.participantVideoView.setVisibility(View.VISIBLE);
                viewHolder.participant_initial.setVisibility(View.GONE);

            } else {
                //Log.d("===participant","SIze:"+participant);
                viewHolder.imgVideo.setImageDrawable(ContextCompat.getDrawable(
                        this.videoActivity, R.drawable.new_videocam_off_white));
                viewHolder.participantVideoView.setVisibility(View.GONE);
                viewHolder.participant_initial.setVisibility(View.VISIBLE);

//                if(remoteParticipantList.get(i).remoteParticipant.getVideoTracks().get(0).getVideoTrack().isEnabled())
//                {
//                    ((MyViewHolder) holder).imgVideo.setImageDrawable(ContextCompat.getDrawable(
//                            this.videoActivity, R.drawable.new_videocam_white));
//
//                }else {
//
//                    ((MyViewHolder) holder).imgAudio.setImageDrawable(ContextCompat.getDrawable(
//                            this.videoActivity, R.drawable.new_mic_off_white));
//                }

            }

        }

        viewHolder.participantVideoView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rowIndex = i;
                        notifyDataSetChanged();
                        //((MyViewHolder) holder).parentLayoutSection.setBackground(videoActivity.getDrawable(R.drawable.selected_participant_background));
                        onClickInterface.onClickVideo(i , viewHolder.name.getText().toString());
                    }
                }
        );
        viewHolder.participant_initial.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rowIndex = i;
                        notifyDataSetChanged();
                        //((MyViewHolder) holder).parentLayoutSection.setBackground(videoActivity.getDrawable(R.drawable.selected_participant_background));
                        onClickInterface.onClickVideo(i , viewHolder.name.getText().toString());
                    }
                }
        );


        viewHolder.participantScreenView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rowIndex = i;
                        notifyDataSetChanged();
                        //((MyViewHolder) holder).parentLayoutSection.setBackground(videoActivity.getDrawable(R.drawable.selected_participant_background));
                        onClickInterface.onClickScreen(i , viewHolder.name.getText().toString());
                    }
                }
        );
        String s = participant.remoteParticipant.getIdentity();
        if (s != null && s.length() > 0) {
            //Log.d("====INside","IF");

            //Log.d("====INside","s:"+s);
            try {
                List<String> splitString = Arrays.asList(s.split("\\-"));
                //Log.d("====INside","splitString:"+splitString.size());
                if (splitString.size() > 0) {
                    viewHolder.name.setText(splitString.get(1));
                    setColorAndText(viewHolder.participant_initial,splitString.get(1));
                } else {
                    String resVal = removePrefix(s, "participant-");
                    viewHolder.name.setText(resVal);
                    setColorAndText(viewHolder.participant_initial,resVal);
                }
            } catch (Exception e) {
                //Log.d("====Exception","Exception:"+e.toString());
                viewHolder.name.setText(participant.remoteParticipant.getIdentity());
                setColorAndText(viewHolder.participant_initial,participant.remoteParticipant.getIdentity());

            }

        } else {
            //Log.d("====INside","ELse");
            //Log.d("====INside","s:"+s);
            viewHolder.name.setText(participant.remoteParticipant.getIdentity());
            setColorAndText(viewHolder.participant_initial,participant.remoteParticipant.getIdentity());
        }
        //viewHolder.name.setText(participant.getIdentity());
        viewHolder.participantVideoView.setMirror(false);
        viewHolder.participantScreenView.setMirror(false);
        viewHolder.participantScreenView.setVisibility(View.GONE);
        if (participant.remoteParticipant.getVideoTracks().size() > 0) {
            for (int index = 0; index < participant.remoteParticipant.getVideoTracks().size(); index++) {
                VideoTrack remoteVideoTrack = (VideoTrack) participant.remoteParticipant.getVideoTracks().get(index).getVideoTrack();
                if (remoteVideoTrack != null) {
                    if (remoteVideoTrack.getName().equals("screen")) {
                        viewHolder.participantScreenView.setVisibility(View.VISIBLE);
                        //remoteVideoTrack.getRenderers().clear();
                        if(viewHolder.participantScreenView!=null) {
                            remoteVideoTrack.removeRenderer(viewHolder.participantScreenView);
                        }
                        remoteVideoTrack.addRenderer(viewHolder.participantScreenView);
                    } else {
                        //remoteVideoTrack.getRenderers().clear(); //comments by Pankaj Khare
                        if(viewHolder.participantVideoView!=null) {
                            remoteVideoTrack.removeRenderer(viewHolder.participantVideoView);
                        }
                        remoteVideoTrack.addRenderer(viewHolder.participantVideoView);
                    }

                }
            }

            /*Added By Rupesh*/
            if (rowIndex == i && !isMyViewActive){
                ((MyViewHolder) holder).parentLayoutSection.setBackground(this.videoActivity.getDrawable(R.drawable.selected_participant_background));
            }else {
                ((MyViewHolder) holder).parentLayoutSection.setBackground(this.videoActivity.getDrawable(R.drawable.unselected_participant_background));
            }
            /*Added By Rupesh*/

        }
    }

    @Override
    public int getItemCount() {
        return remoteParticipantList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,participant_initial;
        LinearLayout parent;
        VideoView participantVideoView;
        VideoView participantScreenView;
        ImageView imgAudio, imgVideo;
        RelativeLayout main_view;
        CardView card_view;
        FrameLayout parentLayoutSection;

        public MyViewHolder(View itemView, VideoActivity videoActivity) {
            super(itemView);
            parentLayoutSection = itemView.findViewById(videoActivity.getApplication().getResources().getIdentifier("parentLayoutSection", "id", videoActivity.package_name));
            card_view = itemView.findViewById(videoActivity.getApplication().getResources().getIdentifier("card_view", "id", videoActivity.package_name));
            main_view = itemView.findViewById(videoActivity.getApplication().getResources().getIdentifier("main_view", "id", videoActivity.package_name));
            parent = itemView.findViewById(videoActivity.getApplication().getResources().getIdentifier("parent", "id", videoActivity.package_name));
            name = itemView.findViewById(videoActivity.getApplication().getResources().getIdentifier("name", "id", videoActivity.package_name));
            participantVideoView = itemView.findViewById(videoActivity.getApplication().getResources().getIdentifier("participant_video_view", "id", videoActivity.package_name));
            participantScreenView = itemView.findViewById(videoActivity.getApplication().getResources().getIdentifier("participant_screen_view", "id", videoActivity.package_name));
            imgAudio = itemView.findViewById(videoActivity.getApplication().getResources().getIdentifier("imgAudio", "id", videoActivity.package_name));
            imgVideo = itemView.findViewById(videoActivity.getApplication().getResources().getIdentifier("imgVideo", "id", videoActivity.package_name));
            participant_initial = itemView.findViewById(videoActivity.getApplication().getResources().getIdentifier("participant_initial", "id", videoActivity.package_name));
        }
    }


    public static String removePrefix(String s, String prefix) {
        if (s != null && prefix != null && s.startsWith(prefix)) {
            return s.substring(prefix.length());
        }
        return s;
    }

    private String getInitial(String name){
        Pattern pattern=Pattern.compile(" ");
        String[]names=pattern.split(name);
        if(names.length>1){
            return (names[0].charAt(0)+""+names[names.length-1].charAt(0)).toUpperCase();
        }else{
            return (name.charAt(0)+"").toUpperCase();
        }
    }
    private void setColorAndText(TextView textView,String name){
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(100), rnd.nextInt(100), rnd.nextInt(100));
        textView.setBackgroundColor(color);
        textView.setText(getInitial(name));
    }

    public void refreshParticipants(int position, RecyclerView recyclerView, List<TechrevRemoteParticipant> newParticipantList){
        // Update the dataset
        /*remoteParticipantList = newParticipantList;
        rowIndex = position;
        notifyDataSetChanged();*/
        /*if (position >= 0) {
            rowIndex = position;
            notifyDataSetChanged();
        } else {
            *//*rowIndex = 0;
            notifyDataSetChanged();*//*
            // Update the background of all participants directly
            if (recyclerView != null) {
                for (int i = 0; i < getItemCount(); i++) {
                    MyViewHolder viewHolder = (MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    if (viewHolder != null) {
                        // Update the background of the parent layout directly
                        viewHolder.parentLayoutSection.setBackground(this.videoActivity.getDrawable(R.drawable.unselected_participant_background));
                    }
                }
            }
        }*/

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return remoteParticipantList.size();
            }

            @Override
            public int getNewListSize() {
                return newParticipantList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                TechrevRemoteParticipant oldParticipant = remoteParticipantList.get(oldItemPosition);
                TechrevRemoteParticipant newParticipant = newParticipantList.get(newItemPosition);
                return oldParticipant.equals(newParticipant);
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                TechrevRemoteParticipant oldParticipant = remoteParticipantList.get(oldItemPosition);
                TechrevRemoteParticipant newParticipant = newParticipantList.get(newItemPosition);
                // Compare fields to determine if the content has changed
                return oldParticipant.equals(newParticipant);
            }
        });

        remoteParticipantList = newParticipantList;
        rowIndex = position;
        diffResult.dispatchUpdatesTo(this);
    }


}

interface onClickInterface {
    void onClickVideo(int index , String participantName);

    void onClickScreen(int index , String participantName);
}
