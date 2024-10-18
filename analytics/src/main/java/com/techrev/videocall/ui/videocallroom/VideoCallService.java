package com.techrev.videocall.ui.videocallroom;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.techrev.videocall.models.DataModel;
import com.techrev.videocall.models.EventModel;
import com.techrev.videocall.models.VideoCallModel;
import com.techrev.videocall.ui.camera.CameraCapturerCompat;
import com.techrev.videocall.utils.Constants;
import com.techrev.videocall.utils.MySharedPreference;
import com.twilio.video.AudioCodec;
import com.twilio.video.CameraCapturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.DataTrackPublication;
import com.twilio.video.EncodingParameters;
import com.twilio.video.G722Codec;
import com.twilio.video.H264Codec;
import com.twilio.video.IsacCodec;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalAudioTrackPublication;
import com.twilio.video.LocalDataTrack;
import com.twilio.video.LocalDataTrackPublication;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.LocalVideoTrackPublication;
import com.twilio.video.NetworkQualityLevel;
import com.twilio.video.OpusCodec;
import com.twilio.video.PcmaCodec;
import com.twilio.video.PcmuCodec;
import com.twilio.video.RemoteAudioTrack;
import com.twilio.video.RemoteAudioTrackPublication;
import com.twilio.video.RemoteDataTrack;
import com.twilio.video.RemoteDataTrackPublication;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrack;
import com.twilio.video.RemoteVideoTrackPublication;
import com.twilio.video.Room;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoCodec;
import com.twilio.video.VideoTrack;
import com.twilio.video.Vp8Codec;
import com.twilio.video.Vp9Codec;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tvi.webrtc.voiceengine.WebRtcAudioUtils;


@TargetApi(29)
public class VideoCallService extends Service {

    public int APP_STATUS;
    private static final String CHANNEL_ID = "video_call";
    private static final String CHANNEL_NAME = "video_call";
    // Added for DataTrack



    private Room room;

    private static final String LOCAL_AUDIO_TRACK_NAME = "mic";
    private static final String LOCAL_VIDEO_TRACK_NAME = "camera";
    private ArrayList<TechrevRemoteParticipant> remoteParticipantList = new ArrayList<TechrevRemoteParticipant>();
    private String token=null;
    private String roomName=null;
    private String userMeetingIdentifier=null;

    private boolean enableAutomaticSubscription;

    private int previousAudioMode;
    private boolean previousMicrophoneMute;
    private SharedPreferences preferences;
    private VideoCallModel videoCallModel=new VideoCallModel();
    static boolean IS_BLUETOOTH_CONNECTED = false;


    //Start
    private static final String DATA_TRACK_MESSAGE_THREAD_NAME = "DataTrackMessages";

    // Dedicated thread and handler for messages received from a RemoteDataTrack
    private final HandlerThread dataTrackMessageThread =
            new HandlerThread(DATA_TRACK_MESSAGE_THREAD_NAME);
    private Handler dataTrackMessageThreadHandler;

    // Map used to map remote data tracks to remote participants
    private final Map<RemoteDataTrack, RemoteParticipant> dataTrackRemoteParticipantMap =
            new HashMap<>();


    private static final String TAG = "VideoCallService";
    /*
     * AudioCodec and VideoCodec represent the preferred codec for encoding and decoding audio and
     * video.
     */
    private AudioCodec audioCodec;
    private VideoCodec videoCodec;

    /*
     * Encoding parameters represent the sender side bandwidth constraints.
     */
    private EncodingParameters encodingParameters;

    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    private MySharedPreference sharedPreference;

    /*Added by Rupesh*/
    // Introduce an executor for background tasks
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Use a Handler to post tasks to the main thread
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    /*Added by Rupesh*/

    /**
     * Class used for the client Binder.  We know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public VideoCallService getService() {
            // Return this instance of ScreenCapturerService so clients can call public methods
            return VideoCallService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start tasks on a background thread
        executorService.execute(() -> {
            // Perform background tasks
            if (intent.hasExtra("token")) {
                token = intent.getStringExtra("token");
            }
            if (intent.hasExtra("room")) {
                roomName = intent.getStringExtra("room");
            }
            if (intent.hasExtra("userMeetingIdentifier")) {
                userMeetingIdentifier = intent.getStringExtra("userMeetingIdentifier");
            }

            // Use mainHandler to post tasks to the main thread if needed
            mainHandler.post(() -> {
                preferences = PreferenceManager.getDefaultSharedPreferences(this);
                startForeground();
                sharedPreference = new MySharedPreference(this);
            });
        });

        return START_NOT_STICKY;
    }

    private CameraCapturer.CameraSource getAvailableCameraSource() {
        return (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA)) ?
                (CameraCapturer.CameraSource.FRONT_CAMERA) :
                (CameraCapturer.CameraSource.BACK_CAMERA);
    }
    /*
     * Get the preferred audio codec from shared preferences
     */
    private AudioCodec getAudioCodecPreference(String key, String defaultValue) {
        final String audioCodecName = preferences.getString(key, defaultValue);

        switch (audioCodecName) {
            case IsacCodec.NAME:
                return new IsacCodec();
            case OpusCodec.NAME:
                return new OpusCodec();
            case PcmaCodec.NAME:
                return new PcmaCodec();
            case PcmuCodec.NAME:
                return new PcmuCodec();
            case G722Codec.NAME:
                return new G722Codec();
            default:
                return new OpusCodec();
        }
    }

    /*
     * Get the preferred video codec from shared preferences
     */
    private VideoCodec getVideoCodecPreference(String key, String defaultValue) {
        final String videoCodecName = preferences.getString(key, defaultValue);

        switch (videoCodecName) {
            case Vp8Codec.NAME:
                boolean simulcast = false; //preferences.getboolean(SettingsActivity.PREF_VP8_SIMULCAST,
                //SettingsActivity.PREF_VP8_SIMULCAST_DEFAULT);
                return new Vp8Codec(simulcast);
            case H264Codec.NAME:
                return new H264Codec();
            case Vp9Codec.NAME:
                return new Vp9Codec();
            default:
                return new Vp8Codec();
        }
    }
    private boolean getAutomaticSubscriptionPreference(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    private EncodingParameters getEncodingParameters() {
        final int maxAudioBitrate = 0; // Integer.parseInt(
        //preferences.getString(SettingsActivity.PREF_SENDER_MAX_AUDIO_BITRATE,
        //      SettingsActivity.PREF_SENDER_MAX_AUDIO_BITRATE_DEFAULT));
        final int maxVideoBitrate = 0; //Integer.parseInt(
        //preferences.getString(SettingsActivity.PREF_SENDER_MAX_VIDEO_BITRATE,
        //      SettingsActivity.PREF_SENDER_MAX_VIDEO_BITRATE_DEFAULT));

        return new EncodingParameters(maxAudioBitrate, maxVideoBitrate);
    }

    private void createAudioAndVideoTracks() {
        if(token!=null && roomName!=null) {
            if (dataTrackMessageThread == null || !dataTrackMessageThread.isAlive()) {
                // Create the local data track
                if(videoCallModel!=null) {
                    videoCallModel.setLocalDataTrack(LocalDataTrack.create(this));

                    // Start the thread where data messages are received
                    dataTrackMessageThread.start();
                    dataTrackMessageThreadHandler = new Handler(dataTrackMessageThread.getLooper());
                    // Share your microphone

                    videoCallModel.setLocalAudioTrack(LocalAudioTrack.create(this, true, LOCAL_AUDIO_TRACK_NAME));

                    // Share your camera
                    videoCallModel.setCameraCapturerCompat(new CameraCapturerCompat(this, getAvailableCameraSource()));
                    videoCallModel.setLocalVideoTrack(LocalVideoTrack.create(this,
                            true,
                            videoCallModel.getCameraCapturerCompat().getVideoCapturer(),
                            LOCAL_VIDEO_TRACK_NAME));
                    CameraCapturer.CameraSource cameraSource = videoCallModel.getCameraCapturerCompat().getCameraSource();
                    videoCallModel.setCameraSource(cameraSource);
                    /*
                     * Update preferred audio and video codec in case changed in settings
                     */

                    audioCodec = getAudioCodecPreference("audio_codec",
                            OpusCodec.NAME);
                    videoCodec = getVideoCodecPreference("video_codec",
                            Vp8Codec.NAME);
                    enableAutomaticSubscription = getAutomaticSubscriptionPreference("enable_automatic_subscription",
                            true);


                    /*
                     * Get latest encoding parameters
                     */
                    final EncodingParameters newEncodingParameters = getEncodingParameters();

                    /*
                     * If the local video track was released when the app was put in the background, recreate.
                     */
//            if (localVideoTrack == null ) {
//                localVideoTrack = LocalVideoTrack.create(this,
//                        true,
//                        cameraCapturerCompat.getVideoCapturer(),
//                        LOCAL_VIDEO_TRACK_NAME);
//                localVideoTrack.addRenderer(localVideoView);
//
//                /*
//                 * If connected to a Room then share the local video track.
//                 */
//                if (localParticipant != null) {
//                    localParticipant.publishTrack(localVideoTrack);
//
//                    /*
//                     * Update encoding parameters if they have changed.
//                     */
//                    if (!newEncodingParameters.equals(encodingParameters)) {
//                        localParticipant.setEncodingParameters(newEncodingParameters);
//                    }
//                }
//            }

                    /*
                     * Update encoding parameters
                     */
                    encodingParameters = newEncodingParameters;
                }
            }

            /*
             * Update reconnecting UI
             */
            if (room != null) {
//            reconnectingProgressBar.setVisibility((room.getState() != Room.State.RECONNECTING) ?
//                    View.GONE :
//                    View.VISIBLE);
            } else {
                Log.d("===Handler", "Called");
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("===Handler", "Inside");
                        connectToRoom(roomName);
                    }
                }, 100);

            }
        }

    }

    private void connectToRoom(String roomName) {
        if (roomName != null && token != null) {
            Log.d("====connectToRoom", "IF part");
            Log.d("====connectToRoom", "roomName:" + roomName);
            Log.d("====connectToRoom", "accessToken:" + token);

            /*
             * Enable changing the volume using the up/down keys during a conversation
             */

            /*
             * Needed for setting/abandoning audio focus during call
             */

            /*ADDED BY RUPESH*/
            // Use software AEC
            WebRtcAudioUtils.setWebRtcBasedAcousticEchoCanceler(true);

            // Use sofware NS
            WebRtcAudioUtils.setWebRtcBasedNoiseSuppressor(true);

            // Use software AGC
            WebRtcAudioUtils.setWebRtcBasedAutomaticGainControl(true);
            /*ADDED BY RUPESH*/

            //audioDeviceSelector.activate();
            ConnectOptions.Builder connectOptionsBuilder = new ConnectOptions.Builder(token)
                    .roomName(roomName)
                    .enableDominantSpeaker(true);


            // Newly Added for data Track
            if (videoCallModel!=null && videoCallModel.getLocalDataTrack() != null) {
                connectOptionsBuilder
                        .dataTracks(Collections.singletonList(videoCallModel.getLocalDataTrack()));
            }

            /*
             * Add local audio track to connect options to share with participants.
             */
            if (videoCallModel!=null && videoCallModel.getLocalAudioTrack() != null) {
                connectOptionsBuilder
                        .audioTracks(Collections.singletonList(videoCallModel.getLocalAudioTrack()));
            }

            /*
             * Add local video track to connect options to share with participants.
             */
            if (videoCallModel!=null &&  videoCallModel.getLocalVideoTrack() != null) {
                connectOptionsBuilder.videoTracks(Collections.singletonList(videoCallModel.getLocalVideoTrack()));
            }

            /*
             * Set the preferred audio and video codec for media.
             */
            connectOptionsBuilder.preferAudioCodecs(Collections.singletonList(audioCodec));
            connectOptionsBuilder.preferVideoCodecs(Collections.singletonList(videoCodec));

            /*
             * Set the sender side encoding parameters.
             */
            connectOptionsBuilder.encodingParameters(encodingParameters);

            /*
             * Toggles automatic track subscription. If set to false, the LocalParticipant will receive
             * notifications of track publish events, but will not automatically subscribe to them. If
             * set to true, the LocalParticipant will automatically subscribe to tracks as they are
             * published. If unset, the default is true. Note: This feature is only available for Group
             * Rooms. Toggling the flag in a P2P room does not modify subscription behavior.
             */
            connectOptionsBuilder.enableAutomaticSubscription(enableAutomaticSubscription);
            Log.d(TAG , "Before Video.connect()");
            if (room == null) {
                Log.d(TAG , "room = null");
                room = Video.connect(this, connectOptionsBuilder.build(), roomListener());
                Log.d(TAG , "ROOM_CHECK: ROOM CONNECTED SUCCESSFULLY!");
            } else {
                Log.d(TAG , "room != null");
            }



            //End

        } else {
            Log.d("====connectToRoom", "Else part");
        }

    }
    /*
     * Room events listener
     */
    @SuppressLint("SetTextI18n")
    private Room.Listener roomListener() {
        return new Room.Listener() {
            @Override
            public void onConnected(Room room) {


                if(videoCallModel!=null ) {
                    Log.d("===Inside", "ProcessRequest onConnected" + videoCallModel.getLocalDataTrack().getName());
                    videoCallModel.setLocalParticipant(room.getLocalParticipant());
                    videoCallModel.publishAllTrack();

                    videoCallModel.getLocalParticipant().setListener(localParticipantListener());

                    if (videoCallModel.getLocalParticipant().getDataTracks().size() > 0) {

                        try {
                            DataTrackPublication localDataTrackPublication = videoCallModel.getLocalParticipant().getDataTracks().get(0);
                            if (localDataTrackPublication.isTrackEnabled()) {
                                Log.d("===Inside", "ProcessRequest On");
                                videoCallModel.setLocalDataTrackPublicationGlobal((LocalDataTrackPublication) localDataTrackPublication);

                            } else {
                                //
                                Log.d("===Inside", "ProcessRequest Off");

                            }
                        } catch (Exception e) {
                            Log.d("====Exception", "ProcessRequest Exception:" + e.toString());
                        }
                    } else {
                        Log.d("===Inside", "ProcessRequest DataTrack Size Zero");
                    }

                    remoteParticipantList.clear();
                    TechrevRemoteParticipant techrevRemoteParticipant = null;
                    for (RemoteParticipant remoteParticipant : room.getRemoteParticipants()) {
                        // Have to check all Room participant before adding into list
                        techrevRemoteParticipant = new TechrevRemoteParticipant();
                        techrevRemoteParticipant.checkAudioVideo(remoteParticipant);
                        remoteParticipantList.add(techrevRemoteParticipant);
                        addRemoteParticipant(techrevRemoteParticipant);
                    }
                    videoCallModel.setConnectionStatus(true);
                    if (APP_STATUS == Constants.APP_STATUS_FOREGROUND) {
                        EventModel eventModel = new EventModel(videoCallModel, null, Constants.EVENTTYPE_CONNECTED_IN_ROOM);
                        eventModel.setRemoteParticipantList(remoteParticipantList);
                        EventBus.getDefault().post(eventModel);
                    }
                }
            }

            @Override
            public void onReconnecting(@NonNull Room room, @NonNull TwilioException twilioException) {




                if(APP_STATUS==Constants.APP_STATUS_FOREGROUND) {
                    EventBus.getDefault().post(new EventModel(null, null, Constants.EVENTTYPE_RECONNECTING));
                }
            }

            @Override
            public void onReconnected(@NonNull Room room) {


                if(videoCallModel != null){
                    videoCallModel.setConnectionStatus(true);
                }
                if(APP_STATUS== Constants.APP_STATUS_FOREGROUND) {
                    EventBus.getDefault().post(new EventModel(null, null, Constants.EVENTTYPE_RECONNECTED));
                }

            }

            @Override
            public void onConnectFailure(Room room, TwilioException e) {

                if (room.getRemoteParticipants().size() == 0) {

                    Log.d(TAG, "onConnectFailure: " + e);
                    // Toast.makeText(VideoActivity.this, "Unable to Connect Please Try Again! "+e, Toast.LENGTH_SHORT).show();

                }
                if(videoCallModel!=null) {
                    videoCallModel.setConnectionStatus(false);
                }
                if(APP_STATUS== Constants.APP_STATUS_FOREGROUND) {
                    EventBus.getDefault().post(new EventModel(null, null, Constants.EVENTTYPE_FAILURE));
                }

            }

            @Override
            public void onDisconnected(Room room, TwilioException e) {

                // Added for Bluetooth Disconnect



                if(videoCallModel!=null) {
                    videoCallModel.setConnectionStatus(false);
                }
                if(APP_STATUS== Constants.APP_STATUS_FOREGROUND) {
                    EventBus.getDefault().post(new EventModel(null, null, Constants.EVENTTYPE_DISCONNECTED_FROM_ROOM));
                }
            }

            @Override
            public void onParticipantConnected(Room room, RemoteParticipant remoteParticipant) {
                Log.d("===VAlidate", "onParticipantConnected");
                for (int index = 0; index < remoteParticipant.getVideoTracks().size(); index++) {
                    VideoTrack remoteVideoTrack = (VideoTrack) remoteParticipant.getVideoTracks().get(index).getVideoTrack();
                    if(remoteVideoTrack!=null)
                    {
                        Log.d("remoteVideoTrack","remoteVideoTrack :"+ remoteVideoTrack.getName());
                    }
                }
                TechrevRemoteParticipant techrevRemoteParticipant = new TechrevRemoteParticipant();
                techrevRemoteParticipant.checkAudioVideo(remoteParticipant);

                //Toast.makeText(VideoActivity.this,"onParticipantConnected",Toast.LENGTH_SHORT).show();




                remoteParticipantList.add(techrevRemoteParticipant);
                addRemoteParticipant(techrevRemoteParticipant);
                if(APP_STATUS== Constants.APP_STATUS_FOREGROUND) {
                    EventModel eventModel = new EventModel(null, null, Constants.EVENTTYPE_PARTICIPANT_CONNECTED_IN_ROOM);
                    eventModel.setTechrevRemoteParticipant(techrevRemoteParticipant);
                    EventBus.getDefault().post(eventModel);
                }
            }

            @Override
            public void onParticipantDisconnected(Room room, RemoteParticipant remoteParticipant) {

                Log.d("===VAlidate", "onParticipantDisconnected");
                /*Added By Rupesh*/
                String name = remoteParticipant.getIdentity();
                if (name.contains("hoster-")){
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                            .getInstance(VideoCallService.this);
                    localBroadcastManager.sendBroadcast(new Intent("com.enotaryoncall.customer.action.close"));

                    /*Added by Rupesh to close foreground screens*/
                    localBroadcastManager.sendBroadcast(new Intent("com.enotaryoncall.customer.action.close.notary_ended_call"));
                    /*Added by Rupesh to close foreground screens*/

                }
                /*Added By Rupesh*/
                if(APP_STATUS== Constants.APP_STATUS_FOREGROUND) {
                    TechrevRemoteParticipant techrevRemoteParticipant = new TechrevRemoteParticipant();
                    techrevRemoteParticipant.checkAudioVideo(remoteParticipant);
                    EventModel eventModel = new EventModel(null, null, Constants.EVENTTYPE_PARTICIPANT_DISCONNECTED_FROM_ROOM);
                    eventModel.setTechrevRemoteParticipant(techrevRemoteParticipant);
                    EventBus.getDefault().post(eventModel);
                }
                removeRemoteParticipant(remoteParticipant);

            }

            /*
             * Called when remote participant leaves the room
             */
            @SuppressLint("SetTextI18n")
            private void removeRemoteParticipant(RemoteParticipant remoteParticipant) {
                //  if (!remoteParticipant.getIdentity().equals(selectedRemoteParticipant.getIdentity())) {
                //      return;
                //  }

                for (int i = 0; i < remoteParticipantList.size(); i++) {
                    TechrevRemoteParticipant participant = (TechrevRemoteParticipant) remoteParticipantList.get(i);
                    if (remoteParticipant.getIdentity().equalsIgnoreCase(participant.getRemoteParticipant().getIdentity())) {
                        remoteParticipantList.remove(i);
                        break;
                    }
                }


            }

            @Override
            public void onRecordingStarted(Room room) {
                /*
                 * Indicates when media shared to a Room is being recorded. Note that
                 * recording is only available in our Group Rooms developer preview.
                 */
                Log.d(TAG, "onRecordingStarted");
            }

            @Override
            public void onRecordingStopped(Room room) {
                /*
                 * Indicates when media shared to a Room is no longer being recorded. Note that
                 * recording is only available in our Group Rooms developer preview.
                 */
                Log.d(TAG, "onRecordingStopped");
            }

            @Override
            public void onDominantSpeakerChanged(@NonNull Room room, @Nullable RemoteParticipant remoteParticipant) {
                for(int i = 0; i < remoteParticipantList.size() ; i++){
                    Log.e(TAG , "Remote participantList value in set speaker: "+remoteParticipantList);
                    Log.e(TAG , "Remote participant value in set speaker: "+remoteParticipantList.get(i).remoteParticipant.getIdentity());
                    if(remoteParticipant != null){
                        String identity = remoteParticipantList.get(i).remoteParticipant.getIdentity();
                        if(identity.equalsIgnoreCase(remoteParticipant.getIdentity())){
                            remoteParticipantList.get(i).setSpeaking(true);
                        }else {
                            remoteParticipantList.get(i).setSpeaking(false);
                        }
                    }
                }
                if(APP_STATUS== Constants.APP_STATUS_FOREGROUND) {
                    TechrevRemoteParticipant techrevRemoteParticipant = new TechrevRemoteParticipant();
                    techrevRemoteParticipant.setSpeaking(true);
                    EventModel eventModel = new EventModel(null, null, Constants.EVENTTYPE_DOMINANT_SPEAKER);
                    eventModel.setTechrevRemoteParticipant(techrevRemoteParticipant);
                    EventBus.getDefault().post(eventModel);
                }
            }
        };
    }
    private LocalParticipant.Listener localParticipantListener() {
        return new LocalParticipant.Listener() {

            @Override
            public void onAudioTrackPublished(@NonNull LocalParticipant localParticipant, @NonNull LocalAudioTrackPublication localAudioTrackPublication) {

            }

            @Override
            public void onAudioTrackPublicationFailed(@NonNull LocalParticipant localParticipant, @NonNull LocalAudioTrack localAudioTrack, @NonNull TwilioException twilioException) {
                Log.d(TAG , "*Inside onAudioTrackPublicationFailed*");
            }

            @Override
            public void onVideoTrackPublished(@NonNull LocalParticipant localParticipant, @NonNull LocalVideoTrackPublication localVideoTrackPublication) {
                Log.d(TAG , "*Inside onVideoTrackPublished*");
            }

            @Override
            public void onVideoTrackPublicationFailed(@NonNull LocalParticipant localParticipant, @NonNull LocalVideoTrack localVideoTrack, @NonNull TwilioException twilioException) {
                Log.d(TAG , "*Inside onVideoTrackPublicationFailed*");
            }

            @Override
            public void onDataTrackPublished(@NonNull LocalParticipant localParticipant, @NonNull LocalDataTrackPublication localDataTrackPublication) {
                // The data track has been published and is ready for use
//                String message = "hello DataTrack!";
//                localDataTrackPublication.getLocalDataTrack().send(message);

                if (videoCallModel != null){
                    videoCallModel.setLocalDataTrackPublicationGlobal(localDataTrackPublication);
                }
                Log.d("===Inside", "ProcessRequest localDataTrackPublicationGlobal" + videoCallModel.getLocalDataTrackPublicationGlobal().getTrackSid());

//                ByteBuffer messageBuffer = ByteBuffer.wrap(new byte[]{ 0xf, 0xe });
//                localDataTrackPublication.getLocalDataTrack().send(messageBuffer);
            }

            @Override
            public void onDataTrackPublicationFailed(@NonNull LocalParticipant localParticipant, @NonNull LocalDataTrack localDataTrack, @NonNull TwilioException twilioException) {
                if(videoCallModel!=null){
                    videoCallModel.setLocalDataTrackPublicationGlobal(null);
                }
                Log.d("===Inside", "ProcessRequest localDataTrackPublicationGlobal failed");

            }

            @Override
            public void onNetworkQualityLevelChanged(@NonNull LocalParticipant localParticipant, @NonNull NetworkQualityLevel networkQualityLevel) {

            }
        };
    }
    /*
     * Called when remote participant joins the room
     */
    @SuppressLint("SetTextI18n")
    private void addRemoteParticipant(TechrevRemoteParticipant remoteParticipant) {
        Log.d("====Inside", "addRemoteParticipant");

        RemoteParticipant newRemoteParticipant = remoteParticipant.getRemoteParticipant();
        newRemoteParticipant.setListener(remoteParticipantListener());

        for (final RemoteDataTrackPublication remoteDataTrackPublication :
                newRemoteParticipant.getRemoteDataTracks()) {
            /*
             * Data track messages are received on the thread that calls setListener. Post the
             * invocation of setting the listener onto our dedicated data track message thread.
             */
            if (remoteDataTrackPublication.isTrackSubscribed()) {
                Log.d("===Thread", "2");
                dataTrackMessageThreadHandler.post(() -> addRemoteDataTrack(newRemoteParticipant,
                        remoteDataTrackPublication.getRemoteDataTrack()));
            }
        }

    }
    @SuppressLint("SetTextI18n")
    private RemoteParticipant.Listener remoteParticipantListener() {
        return new RemoteParticipant.Listener() {
            @Override
            public void onAudioTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.i(TAG, String.format("onAudioTrackPublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrackPublication.getTrackSid(),
                        remoteAudioTrackPublication.isTrackEnabled(),
                        remoteAudioTrackPublication.isTrackSubscribed(),
                        remoteAudioTrackPublication.getTrackName()));
                //Toast.makeText(VideoActivity.this,"onAudioTrackPublished",Toast.LENGTH_SHORT).show();
                //checkPublishAudioTrack(remoteParticipant);


            }

            @Override
            public void onAudioTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.i(TAG, String.format("onAudioTrackUnpublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrackPublication.getTrackSid(),
                        remoteAudioTrackPublication.isTrackEnabled(),
                        remoteAudioTrackPublication.isTrackSubscribed(),
                        remoteAudioTrackPublication.getTrackName()));
                // Toast.makeText(VideoActivity.this,"onAudioTrackUnpublished",Toast.LENGTH_SHORT).show();
                //checkUnPublishAudioTrack(remoteParticipant);

            }

            @Override
            public void onDataTrackPublished(RemoteParticipant remoteParticipant,
                                             RemoteDataTrackPublication remoteDataTrackPublication) {
                Log.i(TAG, String.format("onDataTrackPublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrackPublication.getTrackSid(),
                        remoteDataTrackPublication.isTrackEnabled(),
                        remoteDataTrackPublication.isTrackSubscribed(),
                        remoteDataTrackPublication.getTrackName()));
                //Toast.makeText(VideoActivity.this,"onDataTrackPublished",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDataTrackUnpublished(RemoteParticipant remoteParticipant,
                                               RemoteDataTrackPublication remoteDataTrackPublication) {
                Log.i(TAG, String.format("onDataTrackUnpublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrackPublication.getTrackSid(),
                        remoteDataTrackPublication.isTrackEnabled(),
                        remoteDataTrackPublication.isTrackSubscribed(),
                        remoteDataTrackPublication.getTrackName()));
                //Toast.makeText(VideoActivity.this,"onDataTrackUnpublished",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onVideoTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.i(TAG, String.format("onVideoTrackPublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrackPublication.getTrackSid(),
                        remoteVideoTrackPublication.isTrackEnabled(),
                        remoteVideoTrackPublication.isTrackSubscribed(),
                        remoteVideoTrackPublication.getTrackName()));
                //Toast.makeText(VideoActivity.this,"onVideoTrackPublished",Toast.LENGTH_SHORT).show();

                // checkPublishVideoTrack(remoteParticipant);


            }

            @Override
            public void onVideoTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.i(TAG, String.format("onVideoTrackUnpublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrackPublication.getTrackSid(),
                        remoteVideoTrackPublication.isTrackEnabled(),
                        remoteVideoTrackPublication.isTrackSubscribed(),
                        remoteVideoTrackPublication.getTrackName()));
                //Toast.makeText(VideoActivity.this,"onVideoTrackUnpublished",Toast.LENGTH_SHORT).show();
                // checkUnPublishVideoTrack(remoteParticipant);

            }

            @Override
            public void onAudioTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteAudioTrackPublication remoteAudioTrackPublication,
                                               RemoteAudioTrack remoteAudioTrack) {
                Log.i(TAG, String.format("onAudioTrackSubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrack: enabled=%b, playbackEnabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrack.isEnabled(),
                        remoteAudioTrack.isPlaybackEnabled(),
                        remoteAudioTrack.getName()));
                //Toast.makeText(VideoActivity.this,"onAudioTrackSubscribed",Toast.LENGTH_SHORT).show();

                Log.d("===onAudioTrackSub", "" + remoteAudioTrack.isEnabled());
            }

            @Override
            public void onAudioTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                 RemoteAudioTrack remoteAudioTrack) {
                Log.i(TAG, String.format("onAudioTrackUnsubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrack: enabled=%b, playbackEnabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrack.isEnabled(),
                        remoteAudioTrack.isPlaybackEnabled(),
                        remoteAudioTrack.getName()));
                //Toast.makeText(VideoActivity.this,"onAudioTrackUnsubscribed",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAudioTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                       RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                       TwilioException twilioException) {
                Log.i(TAG, String.format("onAudioTrackSubscriptionFailed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrackPublication: sid=%b, name=%s]" +
                                "[TwilioException: code=%d, message=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrackPublication.getTrackSid(),
                        remoteAudioTrackPublication.getTrackName(),
                        twilioException.getCode(),
                        twilioException.getMessage()));
                //Toast.makeText(VideoActivity.this,"onAudioTrackSubscriptionFailed",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDataTrackSubscribed(RemoteParticipant remoteParticipant,
                                              RemoteDataTrackPublication remoteDataTrackPublication,
                                              RemoteDataTrack remoteDataTrack) {
                Log.i(TAG, String.format("onDataTrackSubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrack.isEnabled(),
                        remoteDataTrack.getName()));
                //Toast.makeText(VideoActivity.this,"onDataTrackSubscribed",Toast.LENGTH_SHORT).show();


                /*
                 * Data track messages are received on the thread that calls setListener. Post the
                 * invocation of setting the listener onto our dedicated data track message thread.
                 */
                Log.d("===Thread", "1");
                dataTrackMessageThreadHandler.post(() -> addRemoteDataTrack(remoteParticipant, remoteDataTrack));

            }

            @Override
            public void onDataTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                RemoteDataTrackPublication remoteDataTrackPublication,
                                                RemoteDataTrack remoteDataTrack) {
                Log.i(TAG, String.format("onDataTrackUnsubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrack.isEnabled(),
                        remoteDataTrack.getName()));

                // Toast.makeText(VideoActivity.this,"onDataTrackSubscribed",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDataTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                      RemoteDataTrackPublication remoteDataTrackPublication,
                                                      TwilioException twilioException) {
                Log.i(TAG, String.format("onDataTrackSubscriptionFailed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrackPublication: sid=%b, name=%s]" +
                                "[TwilioException: code=%d, message=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrackPublication.getTrackSid(),
                        remoteDataTrackPublication.getTrackName(),
                        twilioException.getCode(),
                        twilioException.getMessage()));
                //Toast.makeText(VideoActivity.this,"onDataTrackSubscriptionFailed",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVideoTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteVideoTrackPublication remoteVideoTrackPublication,
                                               RemoteVideoTrack remoteVideoTrack) {
                Log.i(TAG, String.format("onVideoTrackSubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrack.isEnabled(),
                        remoteVideoTrack.getName()));

//                addRemoteParticipantVideo();

                for (int i = 0; i < remoteParticipantList.size(); i++) {
                    if (remoteVideoTrack.getName().equalsIgnoreCase("screen") && remoteParticipant != null && (remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipantList.get(i).getRemoteParticipant().getIdentity()))) {
                        remoteParticipantList.get(i).setTechRevScreenEnable(true);
                        /*Added By Rupesh*/
                        sharedPreference.setBoolean(Constants.SCREEN_SUBSCRIBED_IN_BACKGROUND , true);
                        /*Added By Rupesh*/
                        if(APP_STATUS== Constants.APP_STATUS_FOREGROUND) {
                            TechrevRemoteParticipant techrevRemoteParticipant = new TechrevRemoteParticipant();
                            techrevRemoteParticipant.checkAudioVideo(remoteParticipant);
                            EventModel eventModel = new EventModel(null, null, Constants.EVENTTYPE_SCREEN_SUBSCRIBED);
                            eventModel.setTechrevRemoteParticipant(techrevRemoteParticipant);
                            eventModel.setRemoteVideoTrack(remoteVideoTrack);
                            EventBus.getDefault().post(eventModel);
                        }
                        break;
                    }
                    else  if (remoteParticipant != null && (remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipantList.get(i).getRemoteParticipant().getIdentity()))) {

                        if(APP_STATUS== Constants.APP_STATUS_FOREGROUND) {
                            EventModel eventModel = new EventModel(null, null, Constants.EVENTTYPE_VIDEO_SUBSCRIBED);
                            EventBus.getDefault().post(eventModel);
                        }
                        break;
                    }
                }




                // Toast.makeText(VideoActivity.this,"onVideoTrackSubscribed",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVideoTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                 RemoteVideoTrack remoteVideoTrack) {
                Log.i(TAG, String.format("onVideoTrackUnsubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrack.isEnabled(),
                        remoteVideoTrack.getName()));


                for (int i = 0; i < remoteParticipantList.size(); i++) {
                    if (remoteVideoTrack.getName().equalsIgnoreCase("screen") && remoteParticipant != null && (remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipantList.get(i).getRemoteParticipant().getIdentity()))) {
                        remoteParticipantList.get(i).setTechRevScreenEnable(false);
                        /*Added By Rupesh*/
                        sharedPreference.setBoolean(Constants.SCREEN_UNSUBSCRIBED_IN_BACKGROUND , true);
                        /*Added By Rupesh*/
                        if(APP_STATUS== Constants.APP_STATUS_FOREGROUND) {
                            TechrevRemoteParticipant techrevRemoteParticipant = new TechrevRemoteParticipant();
                            techrevRemoteParticipant.checkAudioVideo(remoteParticipant);
                            EventModel eventModel = new EventModel(null, null, Constants.EVENTTYPE_SCREEN_UNSUBSCRIBED);
                            eventModel.setTechrevRemoteParticipant(techrevRemoteParticipant);
                            eventModel.setRemoteVideoTrack(remoteVideoTrack);
                            EventBus.getDefault().post(eventModel);
                        }
                        break;
                    }
                    else  if (remoteParticipant != null && (remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipantList.get(i).getRemoteParticipant().getIdentity()))) {

                        if(APP_STATUS== Constants.APP_STATUS_FOREGROUND) {
                            EventModel eventModel = new EventModel(null, null, Constants.EVENTTYPE_VIDEO_UNSUBSCRIBED);
                            EventBus.getDefault().post(eventModel);
                        }
                        break;
                    }
                }
//                removeParticipantVideo(remoteVideoTrack);



                //Toast.makeText(VideoActivity.this,"onVideoTrackUnsubscribed",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onVideoTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                       RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                       TwilioException twilioException) {
                Log.i(TAG, String.format("onVideoTrackSubscriptionFailed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrackPublication: sid=%b, name=%s]" +
                                "[TwilioException: code=%d, message=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrackPublication.getTrackSid(),
                        remoteVideoTrackPublication.getTrackName(),
                        twilioException.getCode(),
                        twilioException.getMessage()));
//                Snackbar.make(connectActionFab,
//                        String.format("Failed to subscribe to %s video track",
//                                remoteParticipant.getIdentity()),
//                        Snackbar.LENGTH_LONG)
//                        .show();

                // Toast.makeText(VideoActivity.this,"onVideoTrackSubscriptionFailed",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAudioTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteAudioTrackPublication remoteAudioTrackPublication) {
                //Toast.makeText(VideoActivity.this,"onAudioTrackEnabled",Toast.LENGTH_SHORT).show();
                sendDataModel(remoteParticipant,Constants.EVENTTYPE_AUDIO_ENABLED);

            }

            @Override
            public void onAudioTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteAudioTrackPublication remoteAudioTrackPublication) {
                // Toast.makeText(VideoActivity.this,"onAudioTrackDisabled",Toast.LENGTH_SHORT).show();
                sendDataModel(remoteParticipant,Constants.EVENTTYPE_AUDIO_DISABLED);

            }

            @Override
            public void onVideoTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteVideoTrackPublication remoteVideoTrackPublication) {
                //Toast.makeText(VideoActivity.this,"onVideoTrackEnabled",Toast.LENGTH_SHORT).show();
                sendDataModel(remoteParticipant,Constants.EVENTTYPE_VIDEO_ENABLED);

            }

            @Override
            public void onVideoTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteVideoTrackPublication remoteVideoTrackPublication) {
                //Toast.makeText(VideoActivity.this,"onVideoTrackDisabled",Toast.LENGTH_SHORT).show();
                sendDataModel(remoteParticipant,Constants.EVENTTYPE_VIDEO_DISABLED);

            }
        };
    }

    public void sendDataModel(RemoteParticipant remoteParticipant,int ActionType)
    {

        if(APP_STATUS== Constants.APP_STATUS_FOREGROUND) {
            DataModel dModel =new DataModel();
            dModel.setFrom(remoteParticipant.getIdentity());
            dModel.setTo("All");
            dModel.setContent("");
            dModel.setMessageType("");

            if(ActionType==Constants.EVENTTYPE_VIDEO_DISABLED)
            {
                dModel.setMessageType("CameraOff");
            }
            else if(ActionType==Constants.EVENTTYPE_VIDEO_ENABLED)
            {
                dModel.setMessageType("CameraOn");
            }
            else if(ActionType==Constants.EVENTTYPE_AUDIO_DISABLED)
            {
                dModel.setMessageType("AudioMuted");
            }
            else if(ActionType==Constants.EVENTTYPE_AUDIO_ENABLED)
            {
                dModel.setMessageType("AudioUnMuted");
            }
            if (dModel.getTo() != null && dModel.getTo().equalsIgnoreCase("All") || dModel.getTo().equalsIgnoreCase(userMeetingIdentifier)) {
                EventBus.getDefault().post(new EventModel(null, dModel, Constants.EVENTTYPE_PARTICIPANT_MESSAGE_COMING_FROM_ROOM));
            }
        }
        else {
            for (int i = 0; i < remoteParticipantList.size(); i++) {
                if (remoteParticipant != null && (remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipantList.get(i).getRemoteParticipant().getIdentity()))) {

                    if(ActionType==Constants.EVENTTYPE_VIDEO_DISABLED)
                    {
                        remoteParticipantList.get(i).setTechRevVideoEnable(false);
                    }
                    else if(ActionType==Constants.EVENTTYPE_VIDEO_ENABLED)
                    {
                        remoteParticipantList.get(i).setTechRevVideoEnable(true);
                    }
                    else if(ActionType==Constants.EVENTTYPE_AUDIO_DISABLED)
                    {
                        remoteParticipantList.get(i).setTechRevAudioEnable(false);
                    }
                    else if(ActionType==Constants.EVENTTYPE_AUDIO_ENABLED)
                    {
                        remoteParticipantList.get(i).setTechRevAudioEnable(true);
                    }
                    break;
                }
            }
        }
    }
    private void addRemoteDataTrack(RemoteParticipant remoteParticipant,
                                    RemoteDataTrack remoteDataTrack) {
        Log.d("===Inside", "addRemoteDataTrack");
        dataTrackRemoteParticipantMap.put(remoteDataTrack, remoteParticipant);
        remoteDataTrack.setListener(remoteDataTrackListener());
//        Log.d("===Inside", "Check getTrackName:" + remoteParticipant.getRemoteVideoTracks().get(0).getTrackName());
//        Log.d("===Inside", "Check getVideoTrack:" + remoteParticipant.getRemoteVideoTracks().get(0).getVideoTrack());

    }
    //Newly Added
    private RemoteDataTrack.Listener remoteDataTrackListener() {
        return new RemoteDataTrack.Listener() {
            @Override
            public void onMessage(@NonNull RemoteDataTrack remoteDataTrack, @NonNull ByteBuffer messageBuffer) {
            }

            @Override
            public void onMessage(RemoteDataTrack remoteDataTrack, String message) {
                Log.d("===Inside", "ProcessRequest onMessage: " + message);
                Log.d("===Inside", "ProcessRequest remoteDataTrack: " + remoteDataTrack);
                String messageDetails = message;
                Gson gson = new Gson();
                try {
                    Log.e(TAG , "DATA TRACK DETAILS AT ON MESSAGE RECEIVED: "+messageDetails);
                    Log.d(TAG , "Converting the JSON data to DataModel...>");
                    JSONObject jsonObject = new JSONObject(messageDetails); // jsonString is your JSON string
                    DataModel dModel = new DataModel();
                    try {
                        Log.d(TAG , "JSON Data conversion started...>");
                        //dModel = gson.fromJson(messageDetails, DataModel.class); commented by Rupesh

                        if (jsonObject.has("from") && !jsonObject.isNull("from")) {
                            String from = jsonObject.getString("from");
                            Log.d(TAG , "from: "+from);
                            dModel.setFrom(from);
                        }

                        if (jsonObject.has("to") && !jsonObject.isNull("to")) {
                            String to = jsonObject.getString("to");
                            Log.d(TAG , "to: "+to);
                            dModel.setTo(to);
                        }

                        if (jsonObject.has("messageType") && !jsonObject.isNull("messageType")) {
                            String messageType = jsonObject.getString("messageType");
                            Log.d(TAG , "messageType: "+messageType);
                            dModel.setMessageType(messageType);
                        }

                        if (jsonObject.has("content") && !jsonObject.isNull("content")) {
                            String content = jsonObject.getString("content");
                            Log.d(TAG , "content: "+content);
                            dModel.setContent(content);
                        }

                        if (jsonObject.has("messageValue") && !jsonObject.isNull("messageValue")) {
                            String messageValue = jsonObject.getString("messageValue");
                            Log.d(TAG , "messageValue: "+messageValue);
                            dModel.setContent(messageValue);
                        }

                        if (jsonObject.has("docid") && !jsonObject.isNull("docid")) {
                            String docid = jsonObject.getString("docid");
                            Log.d(TAG , "docid: "+docid);
                            dModel.setDocid(docid);
                        }

                        if (jsonObject.has("tagKey") && !jsonObject.isNull("tagKey")) {
                            String tagKey = jsonObject.getString("tagKey");
                            Log.d(TAG , "tagKey: "+tagKey);
                            dModel.setTagKey(tagKey);
                        }

                        if (jsonObject.has("pageNumber") && !jsonObject.isNull("pageNumber")) {
                            String pageNumber = jsonObject.getString("pageNumber");
                            Log.d(TAG , "pageNumber: "+pageNumber);
                            dModel.setPageNumber(pageNumber);
                        }

                    }catch (Exception e) {
                        Log.d(TAG , "Exception while converting data track to DataModel");
                        Log.d(TAG , "Exception Details: "+e.getMessage());
                        Log.d(TAG , "Exception Details: "+e);
                    }
                    Log.d(TAG , "JSON Data conversion finished!");
                    Log.d(TAG , "from: "+dModel.getFrom());
                    Log.d(TAG , "to: "+dModel.getTo());
                    Log.d(TAG , "messageType: "+dModel.getMessageType());
                    Log.d(TAG , "content: "+dModel.getContent());
                    Log.d(TAG , "messageValue: "+dModel.getMessageValue());
                    Log.d(TAG , "docid: "+dModel.getDocid());
                    Log.d(TAG , "pageNumber: "+dModel.getPageNumber());
                    if (dModel.getTo() != null && (dModel.getTo().equalsIgnoreCase("All") || dModel.getTo().equalsIgnoreCase(userMeetingIdentifier))) {
                        Log.d("===Inside", "ProcessRequest onMessageValue: IF ");
                        processRequest(dModel);
                        sharedPreference.setString(Constants.CURRENT_SIGNATURE_INITIAL_TAG_REPLACE_DOC_ID , dModel.getDocid());
                        if(APP_STATUS== Constants.APP_STATUS_FOREGROUND) {
                            EventBus.getDefault().post(new EventModel(null, dModel, Constants.EVENTTYPE_PARTICIPANT_MESSAGE_COMING_FROM_ROOM));
                        }
                    } else {
                        Log.d(TAG , "DataModel dModel.getTo() is null / this message is not for this user");
                    }
                }catch (Exception e){
                    Log.e(TAG , "DATA TRACK DETAILS AT ON MESSAGE RECEIVED EXCEPTION: "+e.getMessage());
                    e.printStackTrace();
                }

            }
        };
    }

    private void processRequest(DataModel dModel) {

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(VideoCallService.this);

        if (dModel != null) {
            switch (dModel.getMessageType()) {

                case "MuteOrUnmuteAudio":
                    muteUnmuteAudio(dModel);
                    break;
                case "SwitchOnOrOffVideo":
                    SwitchOnOrOffVideo(dModel);
                    break;
                case "NotifySignerToCaptureSignature":
                    if (APP_STATUS != Constants.APP_STATUS_FOREGROUND) {
                        /*Added By Rupesh*/
                        sharedPreference.setBoolean(Constants.SIGNATURE_CAPTURE_REQUEST_IN_BACKGROUND , true);
                        /*Added By Rupesh*/
                    }

                    // To send signature capture signal to all screens in the app
                    //localBroadcastManager.sendBroadcast(new Intent("com.enotaryoncall.customer.action.signature_capture_request"));

                    break;
                case "NotifySignerToCaptureInitial":
                    if (APP_STATUS != Constants.APP_STATUS_FOREGROUND) {
                        /*Added By Rupesh*/
                        sharedPreference.setBoolean(Constants.INITIAL_CAPTURE_REQUEST_IN_BACKGROUND , true);
                        /*Added By Rupesh*/
                    }

                    // To send initial capture signal to all screens in the app
                    //localBroadcastManager.sendBroadcast(new Intent("com.enotaryoncall.customer.action.initial_capture_request"));

                    break;
                case "requestToReplaceSignature":
                    if (APP_STATUS != Constants.APP_STATUS_FOREGROUND) {
                        /*Added By Rupesh*/
                        sharedPreference.setBoolean(Constants.SIGNATURE_INITIAL_REPLACE_REQUEST_IN_BACKGROUND , true);
                        /*Added By Rupesh*/
                    }

                    // To send signature/initial replace signal to all screens in the app
                    //localBroadcastManager.sendBroadcast(new Intent("com.enotaryoncall.customer.action.signature_initial_replace_request"));

                    break;
                case "LeaveFromRoom":
                case "NotaryCancelsRequest":
                case "NotaryEndCallOfCustomer":
                case "RemoveCustomersFromMobile":
                    // Handle LeaveFromRoom
                    localBroadcastManager.sendBroadcast(new Intent("com.enotaryoncall.customer.action.close"));
                    break;
            }


        }
    }
    public void setLocalAudioStatusToService(boolean status) {
        if(videoCallModel!=null)
        {
            videoCallModel.setLocalAudioStatus(status);
        }
    }

    public void setLocalVideoStatusToService(boolean status) {
        if(videoCallModel!=null)
        {
            videoCallModel.setLocalVideoStatus(status);
        }
    }
    private void SwitchOnOrOffVideo(DataModel dModel) {
        boolean msgval = dModel.getMessageValue();
        Log.d("===Inside", "ProcessRequest msgval:" + msgval);
        boolean eventFire = false;

        videoCallModel.setLocalVideoStatus(msgval);

        if (msgval) {
            Log.d("===Inside", "ProcessRequest msgval:IF");

            if (videoCallModel.getLocalParticipant() != null) {
                if (videoCallModel.getLocalVideoTrack() != null) {
                    Log.d("===Inside", "ProcessRequest msgval:IF inside");
                    videoCallModel.getLocalVideoTrack().enable(false);

                    eventFire = true;
                }
            }
        } else {
            Log.d("===Inside", "ProcessRequest msgval:ELSE");


            if (videoCallModel.getLocalParticipant() != null) {
                if (videoCallModel.getLocalVideoTrack() != null) {
                    Log.d("===Inside", "ProcessRequest msgval:ELSE Inside");
                    videoCallModel.getLocalVideoTrack().enable(true);
                    eventFire = true;
                }
            }
        }

        if (eventFire == true) {
            Log.d("===Inside", "ProcessRequest eventFire:Inside");
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("from", userMeetingIdentifier);
                jsonObject.put("to", "All");
                jsonObject.put("messageType", "CameraOff");
                jsonObject.put("content", "");
                videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
            } catch (Exception e) {
                Log.d("====Exception", "ProcessRequest " + e.toString());

            }
        }
    }

    private void muteUnmuteAudio(DataModel dModel) {

        boolean msgval = dModel.getMessageValue();
        Log.d("===Inside", "ProcessRequest msgval:" + msgval);

        boolean eventFire = false;


        videoCallModel.setLocalAudioStatus(msgval);


        if (msgval) {
            Log.d("===Inside", "ProcessRequest msgval:IF");

            if (videoCallModel.getLocalParticipant() != null) {
                if (videoCallModel.getLocalAudioTrack() != null) {
                    Log.d("===Inside", "ProcessRequest msgval:IF inside");
                    videoCallModel.getLocalAudioTrack().enable(false);

                    eventFire = true;
                }
            }
        } else {
            Log.d("===Inside", "ProcessRequest msgval:ELSE");


            if (videoCallModel.getLocalParticipant() != null) {
                if (videoCallModel.getLocalAudioTrack() != null) {
                    Log.d("===Inside", "ProcessRequest msgval:ELSE Inside");
                    videoCallModel.getLocalAudioTrack().enable(true);
                    eventFire = true;
                }
            }
        }

        if (eventFire == true) {
            Log.d("===Inside", "ProcessRequest eventFire:Inside");
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("from", userMeetingIdentifier);
                jsonObject.put("to", "All");
                jsonObject.put("messageType", "AudioMuted");
                jsonObject.put("content", "");
                if(videoCallModel!=null) {
                    videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                }
            } catch (Exception e) {
                Log.d("====Exception", "ProcessRequest " + e.toString());

            }
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void startForeground() {
        NotificationChannel chan = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_NONE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        final int notificationId = (int) System.currentTimeMillis();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Video call is running")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        ServiceCompat.startForeground(this, notificationId, notification, FOREGROUND_SERVICE_TYPE_CAMERA);
        //EventBus.getDefault().register(this);
        APP_STATUS= Constants.APP_STATUS_FOREGROUND;
        executorService.execute(() -> {
            createAudioAndVideoTracks();
            // Use mainHandler to post tasks to the main thread if needed
            mainHandler.post(() -> {
                // Update UI or perform tasks on the main thread
            });
        });

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doThis(DataModel dataModel) {
        //do code here
    }
    public void appInForeground(boolean status)
    {
        try {
            APP_STATUS = Constants.APP_STATUS_FOREGROUND;
            if(APP_STATUS == Constants.APP_STATUS_FOREGROUND) {
                if(videoCallModel!=null && videoCallModel.isConnectionStatus()) {
                    EventModel eventModel;
                    /*CODE TO HANDLE CAMERA CONTROL IF USED BY ANOTHER APP AT THE SAME TIME*/
                    /*CameraCapturer.CameraSource cameraSource = videoCallModel.getCameraCapturerCompat().getCameraSource();
                    if (cameraSource == CameraCapturer.CameraSource.FRONT_CAMERA){
                        videoCallModel.getLocalParticipant().unpublishTrack(videoCallModel.getLocalVideoTrack());
                    }
                    videoCallModel.getCameraCapturerCompat().releaseCamera();
                    videoCallModel.setCameraCapturerCompat(new CameraCapturerCompat(this, videoCallModel.getCameraSource()));
                    videoCallModel.setLocalVideoTrack(LocalVideoTrack.create(this,
                            true,
                            videoCallModel.getCameraCapturerCompat().getVideoCapturer(),
                            LOCAL_VIDEO_TRACK_NAME));

                    videoCallModel.getLocalParticipant().publishTrack(videoCallModel.getLocalVideoTrack());*/
                    /*CODE TO HANDLE CAMERA CONTROL IF USED BY ANOTHER APP AT THE SAME TIME*/

                    if(status){
                        eventModel = new EventModel(videoCallModel, null, Constants.EVENTTYPE_SCREEN_SUBSCRIBED);
                    }else {
                        eventModel = new EventModel(videoCallModel, null, Constants.EVENTTYPE_CONNECTED_IN_ROOM);
                    }
                    eventModel.setRemoteParticipantList(remoteParticipantList);
                    EventBus.getDefault().post(eventModel);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void appInBackground()
    {
        APP_STATUS= Constants.APP_STATUS_BACKGROUND;
    }
    public void endForeground() {
        stopForeground(true);

        if (room != null && room.getState() != Room.State.DISCONNECTED) {
            room.disconnect();
            room = null;
            Log.d(TAG , "ROOM_CHECK: ROOM DISCONNECTED SUCCESSFULLY!");
        }
        if(videoCallModel!=null) {
            this.videoCallModel.releaseAllTracks();
        }
        this.videoCallModel=null;
        // Quit the data track message thread
        dataTrackMessageThread.quit();
        // EventBus.getDefault().unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /*Added By Rupesh*/
    public void resumeVideoTracks() {
        if(token!=null && roomName!=null) {

            // Create the local data track
            if(videoCallModel!=null) {
                videoCallModel.getCameraCapturerCompat().releaseCamera();
                // Share your camera
                if (getAvailableCameraSource()!=null) {
                    videoCallModel.setCameraCapturerCompat(new CameraCapturerCompat(this, getAvailableCameraSource()));
                    videoCallModel.setLocalVideoTrack(LocalVideoTrack.create(this, true, videoCallModel.getCameraCapturerCompat().getVideoCapturer(), LOCAL_VIDEO_TRACK_NAME));
                    CameraCapturer.CameraSource cameraSource = videoCallModel.getCameraCapturerCompat().getCameraSource();
                    videoCallModel.setCameraSource(cameraSource);
                }else{
                    videoCallModel.setCameraCapturerCompat(new CameraCapturerCompat(this, CameraCapturer.CameraSource.FRONT_CAMERA));
                    videoCallModel.setLocalVideoTrack(LocalVideoTrack.create(this, true, videoCallModel.getCameraCapturerCompat().getVideoCapturer(), LOCAL_VIDEO_TRACK_NAME));
                    CameraCapturer.CameraSource cameraSource = videoCallModel.getCameraCapturerCompat().getCameraSource();
                    videoCallModel.setCameraSource(cameraSource);
                }

            }

        }

    }

    public void resumeVideoTrackPublish()
    {
        /*
         * Update preferred audio and video codec in case changed in settings
         */

        audioCodec = getAudioCodecPreference("audio_codec",
                OpusCodec.NAME);
        videoCodec = getVideoCodecPreference("video_codec",
                Vp8Codec.NAME);
        enableAutomaticSubscription = getAutomaticSubscriptionPreference("enable_automatic_subscription",
                true);

        /*
         * Get latest encoding parameters
         */
        final EncodingParameters newEncodingParameters = getEncodingParameters();

        /*
         * If connected to a Room then share the local video track.
         */
        if (videoCallModel.getLocalParticipant() != null) {
            videoCallModel.getLocalParticipant().publishTrack(videoCallModel.getLocalVideoTrack());

            /*
             * Update encoding parameters if they have changed.
             */
            if (!newEncodingParameters.equals(encodingParameters)) {
                videoCallModel.getLocalParticipant().setEncodingParameters(newEncodingParameters);
            }
        }
    }
    /*Added By Rupesh*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release resources, disconnect from the room, etc.

        // Stop the executor service
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

}
