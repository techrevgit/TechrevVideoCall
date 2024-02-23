package com.techrev.videocall.ui.videocallroom;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.projection.MediaProjectionManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;


import com.techrev.videocall.dialogfragments.CaptureSignerInitialDialogFragment;
import com.techrev.videocall.dialogfragments.CaptureSignerSignatureDialogFragment;
import com.techrev.videocall.models.NotarizationActionModel;
import com.techrev.videocall.models.RequestDetailsModel;
import com.techrev.videocall.services.APictureCapturingService;
import com.techrev.videocall.ui.camera.CameraActivity;
import com.techrev.videocall.ui.cosigner.AddCoSignerActivity;
import com.techrev.videocall.models.AttachedFileUploadResponseModel;
import com.techrev.videocall.ui.camera.CameraCapturerCompat;
import com.techrev.videocall.models.ClientActivePlanDetailsByClientIdModel;
import com.techrev.videocall.models.CommonModel;
import com.techrev.videocall.ui.cosigner.CosignerDetailsModel;
import com.techrev.videocall.models.CustomerDisagreeCountModel;
import com.techrev.videocall.models.DataModel;
import com.techrev.videocall.models.DeleteMessageResponseModel;
import com.techrev.videocall.models.EventModel;
import com.techrev.videocall.models.MeetingDetailsModel;
import com.techrev.videocall.ui.internet.NoInternetActivity;
import com.techrev.videocall.ui.whiteboard.WhiteBoardActivity;
import com.techrev.videocall.utils.NotarizationActionUpdateManger;
import com.techrev.videocall.utils.PictureCapturingListener;
import com.techrev.videocall.services.PictureCapturingServiceImpl;
import com.techrev.videocall.R;
import com.techrev.videocall.models.RequestForMeetingRoomModel;
import com.techrev.videocall.dialogfragments.SignerAuthirizationDialogFragment;
import com.techrev.videocall.models.SignerSignatureInitialAuthorizationModel;
import com.techrev.videocall.models.UpdateRequestStatusResponse;
import com.techrev.videocall.models.UploadImageModel;
import com.techrev.videocall.models.VideoCallModel;
import com.techrev.videocall.network.NetworkInterface;
import com.techrev.videocall.network.RetrofitNetworkClass;
import com.techrev.videocall.ui.chat.ChatDataModel;
import com.techrev.videocall.ui.chat.ChatMessageInterface;
import com.techrev.videocall.ui.chat.ChatViewAdapter;
import com.techrev.videocall.ui.mydocuments.DocumentsByRequestIdModel;
import com.techrev.videocall.ui.mydocuments.MyCurrentUploadedDocumentsActivity;
import com.techrev.videocall.utils.Constants;
import com.techrev.videocall.utils.MySharedPreference;
import com.twilio.video.CameraCapturer;
import com.twilio.video.CameraCapturer.CameraSource;
import com.twilio.video.LocalDataTrack;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.RemoteAudioTrack;
import com.twilio.video.RemoteAudioTrackPublication;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrack;
import com.twilio.video.RemoteVideoTrackPublication;
import com.twilio.video.ScreenCapturer;
import com.twilio.video.VideoRenderer;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class VideoActivity extends Activity implements View.OnTouchListener , ChatMessageInterface, PictureCapturingListener {


    private LMDAudioSwitch lmdAudioSwitch=null;
    private static final int REQUEST_CODE = 101;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback = null;
    private long UPDATE_INTERVAL = 10 * 18000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    boolean isTryingLocationg = false;
    //For Video Image Capture Starts
    private CameraCapturer cameraCapturer;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    LinearLayout linearView;


    private LinearLayout capturedView;
    private ImageView pictureImage;
    //private SnapshotVideoRenderer snapshotVideoRenderer;

    //Ends

    Dialog dialogView, dialogViewHost;
    ;
    static RetrofitNetworkClass networkClass = new RetrofitNetworkClass();
    static Retrofit retrofitLocal = networkClass.callingURL();
    static NetworkInterface serviceLocal = retrofitLocal.create(NetworkInterface.class);

    private static final int CAMERA_MIC_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "VideoActivity";
    /*public static String BASE_URL_VAL = "https://notarytest.enotaryoncall.com/api/";*/
    public static String BASE_URL_VAL = "https://apias.digitalnotarize.com/api/";


    //Newly Added for Camera

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri image_uri;
    Dialog dialogImage;

    /*
     * Audio and video tracks can be created with names. This feature is useful for categorizing
     * tracks of participants. For example, if one participant publishes a video track with
     * ScreenCapturer and CameraCapturer with the names "screen" and "camera" respectively then
     * other participants can use RemoteVideoTrack#getName to determine which video track is
     * produced from the other participant's screen or camera.
     */
    private static final String LOCAL_AUDIO_TRACK_NAME = "mic";
    private static final String LOCAL_VIDEO_TRACK_NAME = "camera";

    /*
     * You must provide a Twilio Access Token to connect to the Video service
     */
    private String TWILIO_ACCESS_TOKEN = "";
    private String TWILIO_ROOM_NAME = "";
    private static final String ACCESS_TOKEN_SERVER = "";

    // Newly Added for gettting Other Details
    private String BaseURL = "";
    private static String authToken = "";
    private static String requestID = "";
    private int member_Type = Constants.member_type_banker;
    private String passcode = "";
    private String maxImageCaptureLimit = "";
    private String userMeetingIdentifier = "";
    FrameLayout video_view;
    private RelativeLayout participantsview;
    private String clientId = "";
    private int isRecordingEnabled = 0;
    private String meetingEndTime;
    private String meetingStartTime;
    private String meetingDuration;
    //FrameLayout video_view;
    private static String userId = "";
    private boolean isCoSigner = false;
    private String userName = "";
    private String reqeustParticipantId = "";
    private static String firstName = "", lastName = "";

    ImageView imgRecording;
    private TextView tv_expire_time;
    private final int FLAG_ACTIVITY_ADD_CO_SIGNER = 5002;


    /*
     * Access token used to connect. This field will be set either from the console generated token
     * or the request to the token server.
     */
    private String accessToken;


    /*
     * A VideoView receives frames from a local or remote video track and renders them
     * to an associated view.
     */
    private LinearLayout nootherparticipant;
    private VideoView primaryVideoView;
    private VideoView thumbnailVideoView;
    //Newly Added for Thumbnail View Purpose
    private CardView card_view_thumbnailView;
    private TextView tvLocalParticipantName;
    private TextView participant_initial;
    private FrameLayout mainThumbnailView;

    /*
     * Android shared preferences used for settings
     */
    private SharedPreferences preferences;

    /*
     * Android application UI elements
     */
    private CameraCapturerCompat cameraCapturerCompat;

    private ImageButton connectActionFab;
    private ImageButton speakerActionFab;
    private ImageButton addFileActionFab;
    private ImageButton switchCameraActionFab;
    private ImageButton localVideoActionFab;
    private ImageButton muteActionFab;
    private ImageButton chatActionFab;
    private ImageButton shareScreenActionFab;
    private static ImageView chatBadge;
    //Newly Added
    private ImageButton captureActionFab;
    private ImageButton viewMeetingActionFab;
    private ImageButton viewParticipantControlFab;

    private ImageButton audioRefresh;
    private ImageButton meetingShareDetails;
    private ImageButton locationSharing;
    private ImageButton expandImage;


    private ProgressBar reconnectingProgressBar;
    private AlertDialog connectDialog;
    private TechrevRemoteParticipant selectedRemoteParticipant;



    private int savedVolumeControlStream;
    private MenuItem audioDeviceMenuItem;

    private VideoRenderer localVideoView;
    private boolean disconnectedFromOnDestroy;
    private boolean enableAutomaticSubscription;

    String package_name;

    private RecyclerView recyclerView;
    private ParticipantsAdapter participantsAdapter;
    private onClickInterface onclickInterface;
    private ONClickInterfaceParticipant onClickInterfaceParticipant;

    private ArrayList<TechrevRemoteParticipant> remoteParticipantList = new ArrayList<TechrevRemoteParticipant>();

    int getResourceID(String name, String type) {
        return getApplication().getResources().getIdentifier(name, type, getApplication().getPackageName());
    }

    boolean isRemoteViewRendered = false;
    boolean isShareScreenStarted = false;

    //Newly Added
    boolean leavingFromRoom = false;
    boolean checkLocalVideoOFF = false;


    private static final int REQUEST_MEDIA_PROJECTION = 10000;
    private VideoCallManager videoCallManager;
    private LocalVideoTrack screenVideoTrack;
    private ScreenCapturer screenCapturer;
    private MenuItem screenCaptureMenuItem;
    private ScreenCapturerManager screenCapturerManager;
    private final ScreenCapturer.Listener screenCapturerListener = new ScreenCapturer.Listener() {
        @Override
        public void onScreenCaptureError(String errorDescription) {
            Log.e(TAG, "Screen capturer error: " + errorDescription);
            stopScreenCapture();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(VideoActivity.this, "screen_capture_error",
                            Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onFirstFrameAvailable() {
            Log.d(TAG, "First frame from screen capturer available");
        }
    };


    // Added for DataTrack
    private LocalDataTrack localDataTrack;



    DataModel dModel;
    DataModel bankerdModel;


    File directory;
    File mypath;
    int MaxImageCaptureLimit = 0;


    private AudioManager audioManager;
    private int previousAudioMode;
    private boolean previousMicrophoneMute;


    //Adding Newly for Remote AudioTrack
    private RemoteAudioTrack remoteAudioTrack;
    RecyclerViewParticipantAdapter rVPadapter;
    RecyclerView rViewPopup;
    TextView emptyView;
    LinearLayout ll1;

    AlertDialog alertDialog1, alertDialog2;

    //For Location purpose Added
    final String gpsLocationProvider = LocationManager.GPS_PROVIDER;
    final String networkLocationProvider = LocationManager.NETWORK_PROVIDER;
    String wantPermission = Manifest.permission.ACCESS_FINE_LOCATION;


    //get access to location permission
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    //newly Added for Image Flip

    // type definition
    public static final int FLIP_VERTICAL = 1;
    public static final int FLIP_HORIZONTAL = 2;

    //Newly Added to validate BlueTooth
    android.bluetooth.BluetoothHeadset mBluetoothHeadset;
    static boolean IS_BLUETOOTH_CONNECTED = false;



    FrameLayout.LayoutParams layoutParamsPV = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

    //CoordinatorLayout.LayoutParams layoutParamsPV = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);


    boolean isUp = true;
    boolean isRemovingParticipant=false;
    boolean isAutoDisableAudio=false;
    boolean isAutoDisableVideo=false;
    boolean isDisableAudio=false;
    boolean isDisableVideo=false;
    boolean isConnectedInRoom=false;
    //End
    private int Comming_from=0;
    //private User patientObject;
    //public static OPENCALLDIALOG mCallback=null;
    private LinearLayout llTest;

    DisplayMetrics displaymetrics = new DisplayMetrics();
    int screenHight;
    int screenWidth;
    boolean isPopupShows = false;
    //End

    private CountDownTimer mCountDownTimer;

    /*TODO: Chat Module Variables // Added By Rupesh*/
    private static ArrayList<ChatDataModel.ChatData> chatList;
    private static AlertDialog chatAlertDialog;
    private static View mView;
    private static AlertDialog.Builder builder;
    private static Activity mActivity;
    private static FragmentManager fragmentManager;
    private static ImageView iv_attach_file;
    private static final int PICKFILE_RESULT_CODE = 1000;
    private static String uploaded_chat_file_docID;
    private static LinearLayout ll_attach_file , ll_send_file;
    private static AlertDialog chatAttachFileAlertDialog;
    private static final int FILE_PERMISSION_CODE = 1010;
    private static ListView listView;
    private VideoCallModel videoCallModel;
    //private AudioDeviceSelector audioDeviceSelector;
    private String[] call_views = { "Speaker View", "Selected View"};
    private Spinner sp_call_view;
    private LinearLayout ll_call_view_section;
    private int screenShowingParticipantPosition = 0;
    private EventModel speakingParticipantDetailsEventModel;
    private int DOCUMENT_ACCESS_DISAGREE_COUNT = -1;
    private MySharedPreference sharedPreference;
    private Camera camera;
    private int cameraId = 0;
    private APictureCapturingService pictureService;
    private ConnectivityManager manager;
    private boolean isSignatureCaptured = false;
    public static int CURRENT_CAMERA = 0; // 0: FRONT_CAMERA / 1 : BACK_CAMERA
    private ImageView ivback;
    private RelativeLayout rl_recording_section;
    private TextView tv_activeParticipantName;
    private VideoCallViewAdapter mVideoCallViewAdapter = null;
    private boolean isControlMenuClickable = true;
    private boolean isLocationButtonClicked = false;
    private String mActiveParticipant = "";
    private int mActiveParticipantIndex = 0;
    private boolean isMyViewActive = false;
    AlertDialog.Builder builderNoHost = null;
    AlertDialog dialogNoHost = null;
    private boolean SCREEN_SUBSCRIBED = false;
    private boolean isSpeakerOn = false;
    AudioManager mAudioManager = null;
    private CountDownTimer mCountDownTimerInBackground = null;
    private boolean IS_APP_IN_BACKGROUND = false;
    private boolean IS_VIDEO_DISABLED = false;

    private static final int SIGNATURE_INITIAL_CAPTURE_CODE = 9999;
    private final int CAMERA_REQUEST_FOR_SIGNATURE = 10191;
    private final int CAMERA_REQUEST_FOR_INITIAL = 10192;
    private final int REQUEST_CAMERA_CODE_FOR_SIGNATURE = 100981;
    private final int REQUEST_CAMERA_CODE_FOR_INITIAL = 100982;
    private boolean IS_REPLACE_SIGNATURE_INITIAL_DIALOG_SHOWN = false;
    private boolean IS_AUTHORIZATION_DIALOG_SHOWN_ALREADY = false;
    private boolean IS_AUDIT_JOIN_CALL_SENT_ALREADY = false;
    private boolean IS_REQUEST_CREATED_BY_CUSTOMER = false;
    private List<NotarizationActionModel.NotarizationActions> mNotarizationModel = new ArrayList<NotarizationActionModel.NotarizationActions>();
    private String isPrimarySigner, isWitness, customerType;
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
        //setContentView(R.layout.activity_video);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

        // setTitle("eNotary");
        //getActionBar().hide();

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
        mActivity = this;
        fragmentManager = getFragmentManager();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutParamsPV.setMargins(10, 420, 10, 160);
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
        package_name = getApplication().getPackageName();
        setContentView(R.layout.activity_video);

        sharedPreference = new MySharedPreference(VideoActivity.this);

        mView = getLayoutInflater().inflate(R.layout.activity_chat , null);
        builder = new AlertDialog.Builder(VideoActivity.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        chatAlertDialog = builder.create();

        iv_attach_file = mView.findViewById(R.id.attachment_action_fab);

        iv_attach_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachFileToChat();
            }
        });

        nootherparticipant = findViewById(getResourceID("nootherparticipant", "id"));
//        primaryVideoView = findViewById(getResourceID("primary_video_view", "id"));
        thumbnailVideoView = findViewById(getResourceID("thumbnail_video_view", "id"));
        mainThumbnailView = findViewById(getResourceID("mainThumbnailView", "id"));
        recyclerView = findViewById(getResourceID("participants_recycler_view", "id"));
        tvLocalParticipantName = findViewById(getResourceID("tvLocalParticipantName", "id"));
        card_view_thumbnailView = findViewById(getResourceID("card_view_thumbnailView", "id"));
        card_view_thumbnailView.setOnTouchListener(VideoActivity.this);
        reconnectingProgressBar = findViewById(getResourceID("reconnecting_progress_bar", "id"));

        connectActionFab = (ImageButton) findViewById(getResourceID("connect_action_fab", "id"));
        speakerActionFab = (ImageButton) findViewById(getResourceID("speaker_action_fab", "id"));
        addFileActionFab = (ImageButton) findViewById(getResourceID("add_file_action_fab", "id"));
        switchCameraActionFab = (ImageButton) findViewById(getResourceID("switch_camera_action_fab", "id"));
        localVideoActionFab = (ImageButton) findViewById(getResourceID("local_video_action_fab", "id"));
        muteActionFab = (ImageButton) findViewById(getResourceID("mute_action_fab", "id"));
        chatActionFab = (ImageButton) findViewById(getResourceID("chat_action_fab", "id"));
        chatBadge = (ImageView) findViewById(getResourceID("chat_badge", "id"));
        shareScreenActionFab = (ImageButton) findViewById(getResourceID("share_screen_action_fab", "id"));

        captureActionFab = (ImageButton) findViewById(getResourceID("capture_action_fab", "id"));
        viewMeetingActionFab = (ImageButton) findViewById(getResourceID("view_meeting_action_fab", "id"));
        viewParticipantControlFab = (ImageButton) findViewById(getResourceID("view_participant_list", "id"));

        audioRefresh = (ImageButton) findViewById(getResourceID("audio_refresh", "id"));
        meetingShareDetails = (ImageButton) findViewById(getResourceID("meeting_share_details", "id"));
        locationSharing = (ImageButton) findViewById(getResourceID("location_sharing", "id"));
        expandImage = (ImageButton) findViewById(getResourceID("expand_image", "id"));


        linearView = (LinearLayout) findViewById(getResourceID("llTest", "id"));
        capturedView = (LinearLayout) findViewById(getResourceID("llTestNew", "id"));
        pictureImage = (ImageView) findViewById(getResourceID("pictureImage", "id"));
        //Newly Added
        imgRecording = (ImageView) findViewById(getResourceID("imgRecording", "id"));
        tv_expire_time = (TextView) findViewById(getResourceID("tv_expire_time", "id"));
        participant_initial = (TextView) findViewById(getResourceID("participant_initial", "id"));
        video_view = (FrameLayout) findViewById(getResourceID("video_view", "id"));

        llTest = (LinearLayout) findViewById(getResourceID("llTest", "id"));
        participantsview = (RelativeLayout) findViewById(getResourceID("participantsview", "id"));
        video_view.setOnTouchListener(VideoActivity.this);
        video_view.setOnTouchListener(VideoActivity.this);
        card_view_thumbnailView.setOnTouchListener(VideoActivity.this);

        /*Added by Rupesh to enable speaker by default*/
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        /*speakerActionFab.setBackgroundResource(R.drawable.primary_circle_background);
        speakerActionFab.setColorFilter(getResources().getColor(R.color.white));
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        mAudioManager.setMode(AudioManager.MODE_IN_CALL);*/
        /*Added by Rupesh to enable speaker by default*/

        ivback = findViewById(R.id.ivback);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoActivity.this.finish();
            }
        });

        rl_recording_section = findViewById(R.id.recording_section);
        tv_activeParticipantName = findViewById(R.id.activeParticipantName);

        //participantsview = findViewById(R.id.participantsview);

        intializeUI();

        builderNoHost = new AlertDialog.Builder(VideoActivity.this);
        dialogNoHost = builderNoHost.setMessage("Host is not available in this meeting. Please try again later!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exitFromTheRoom();
                    }
                })
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create();

        initViews();

    }

    private class InitViewsTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<VideoActivity> activityReference;

        InitViewsTask(VideoActivity activity) {
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            VideoActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                // Handle the case where the activity is no longer valid
                cancel(true);
            } else {
                // Move these codes to onPreExecute
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            VideoActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                // Handle the case where the activity is no longer valid
                cancel(true);
            } else {
                // Background thread for heavy initialization
                try {
                    activity.initViewsInBackground();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            VideoActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                // Handle the case where the activity is no longer valid
            } else {
                // This method is executed on the UI thread after the background task is complete.
                // Update UI elements or perform other tasks that need to run on the main thread.
            }
        }

        @Override
        protected void onCancelled() {
            VideoActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                // Handle the case where the activity is no longer valid
            } else {
                // This method is executed on the UI thread if the AsyncTask is canceled.
                // Cleanup or handle cancellation here.
            }
        }
    }

    private void initViews() {
        Log.d(TAG , "Thread Name in initViews: "+Thread.currentThread().getName());
        new InitViewsTask(this).execute();
    }

    private void initViewsInBackground() throws JSONException {
        Log.d(TAG , "Thread Name in initViewsInBackground: "+Thread.currentThread().getName());
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            TWILIO_ACCESS_TOKEN = "";
            TWILIO_ROOM_NAME = "";
            BaseURL = "";
            authToken = "";
            requestID = "";
            passcode = "";
            member_Type = Constants.member_type_banker;
            maxImageCaptureLimit = "";
            isRecordingEnabled = 0;
        } else {
            TWILIO_ACCESS_TOKEN = extras.getString("TOKEN");
            TWILIO_ROOM_NAME = extras.getString("ROOM_NAME");
            BaseURL = extras.getString("BASE_URL");
            authToken = extras.getString("AUTHORIZATION_TOKEN");
            requestID = extras.getString("REQUEST_ID");
            member_Type = extras.getInt("MEMBER_TYPE");
            passcode = extras.getString("PASSCODE");
            MaxImageCaptureLimit = Integer.parseInt(extras.getString("MAX_IMAGE_CAPTURE_LIMIT"));
            userMeetingIdentifier = extras.getString("USER_MEETING_IDENTIFIER");
            clientId = extras.getString("CLIENT_ID");
            isRecordingEnabled = extras.getInt("RECORDING_ENABLED");
            meetingEndTime = extras.getString("MEETING_END_DATE");
            meetingStartTime = extras.getString("MEETING_START_DATE");
            meetingDuration = extras.getString("MEETING_DURATION");
            userId = extras.getString("USER_ID");
            isCoSigner = extras.getBoolean("IS_CO_SIGNER", false);
            userName = extras.getString("USER_DETAILS");
            reqeustParticipantId = extras.getString("REQUEST_PARTICIPANT_ID");
            JSONObject userObj = null;
            try {
                userObj = new JSONObject(userName);
                firstName = userObj.get("firstName").toString();
                lastName = userObj.get("lastName").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("===Data", "member_Type:" + member_Type);
            Log.d("===Data", "MaxImageCaptureLimit:" + MaxImageCaptureLimit);
            Log.d("===Data", "member_Type:" + member_Type);
            Log.d("===Data", "clientId:" + clientId);
            Log.d("===Data", "User Name: " +firstName  +" "+lastName);
            if (BaseURL != null && BaseURL.length() > 0) {
                BASE_URL_VAL = BaseURL + "api/";
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("===member_Type", "" + Constants.member_type_participant);
                    if (member_Type == Constants.member_type_participant) {
                        captureActionFab.setVisibility(View.GONE);
                        viewMeetingActionFab.setVisibility(View.GONE);
                        //shareScreenActionFab.setVisibility(View.GONE);
//                viewParticipantControlFab.setVisibility(View.GONE);

                        //New Features to show

                        //locationSharing.setVisibility(View.VISIBLE);

                    } else {
                        if (Constants.isCaptureImageEnable) {
                            //captureActionFab.setVisibility(View.VISIBLE);
                            //viewMeetingActionFab.setVisibility(View.VISIBLE);
                        } else {
                            captureActionFab.setVisibility(View.GONE);
                            viewMeetingActionFab.setVisibility(View.GONE);
                        }

                    }
                }
            });


        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (TWILIO_ACCESS_TOKEN == null || TWILIO_ACCESS_TOKEN.equalsIgnoreCase("") ||
                        TWILIO_ROOM_NAME == null || TWILIO_ROOM_NAME.equalsIgnoreCase("")) {

                    AlertDialog alertDialog = new AlertDialog.Builder(
                            VideoActivity.this).create();
                    finish();
                    // Setting Dialog Title
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Try again...");
                    alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    });


                    // Showing Alert Message

                    alertDialog.show();

                } else {

                    if (Build.VERSION.SDK_INT >= 29) {
                        screenCapturerManager = new ScreenCapturerManager(VideoActivity.this);
                    }

                    /*
                     * Set the initial state of the UI
                     */
                    if (member_Type == Constants.member_type_participant) {
                        captureActionFab.setVisibility(View.GONE);
                        viewMeetingActionFab.setVisibility(View.GONE);
                        //Newly Added
                        if (isRecordingEnabled == 1) {
                            imgRecording.setVisibility(View.VISIBLE);
                        } else {
                            //imgRecording.setVisibility(View.GONE);
                        }
                    } else {
                        //Newly Added
                        if (isRecordingEnabled == 1) {
                            imgRecording.setVisibility(View.VISIBLE);
                        } else {
                            //imgRecording.setVisibility(View.GONE);
                        }
                        if (Constants.isCaptureImageEnable) {
                            //captureActionFab.setVisibility(View.VISIBLE);
                            //viewMeetingActionFab.setVisibility(View.VISIBLE);
                        } else {
                            captureActionFab.setVisibility(View.GONE);
                            viewMeetingActionFab.setVisibility(View.GONE);
                        }

                    }
                    lmdAudioSwitch = new LMDAudioSwitch(VideoActivity.this);
                    videoCallManager = new VideoCallManager(VideoActivity.this, TWILIO_ACCESS_TOKEN, TWILIO_ROOM_NAME, userMeetingIdentifier);


                }
                if (isCoSigner) {
                    viewParticipantControlFab.setVisibility(View.GONE);
                    addFileActionFab.setVisibility(View.GONE);
                } else {
                    viewParticipantControlFab.setVisibility(View.VISIBLE);
                    addFileActionFab.setVisibility(View.VISIBLE);
                }

                sp_call_view = findViewById(R.id.call_view_spinner);
                mVideoCallViewAdapter = new VideoCallViewAdapter(VideoActivity.this,  call_views);
                sp_call_view.setAdapter(mVideoCallViewAdapter);

                sp_call_view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(VideoActivity.this, ""+call_views[position], Toast.LENGTH_SHORT).show();
                        if(position == 0){
                            mVideoCallViewAdapter.setSelection(0);
                            setSpeakerViewAsDefault(speakingParticipantDetailsEventModel);
                        }else {
                            mVideoCallViewAdapter.setSelection(1);
                            showParticipantScreenInFront(screenShowingParticipantPosition);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                ll_call_view_section = findViewById(R.id.call_view_section_layout);
                ll_call_view_section.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sp_call_view.performClick();
                    }
                });
                thumbnailVideoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isMyViewActive = true;
                        mainThumbnailView.setBackground(VideoActivity.this.getDrawable(R.drawable.selected_participant_background));
                        participantsAdapter.refreshParticipants(-1);
                        showThumbnailInPrimaryVideoSection();
                    }
                });
            }
        });

        initCamera();
        getAllNotarizationAction();

        pictureService = PictureCapturingServiceImpl.getInstance(this);
    }

    private void initCamera(){
        Log.d(TAG , "Thread Name in inintCamera: "+Thread.currentThread().getName());
        // do we have a camera?
        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(VideoActivity.this, "No camera on this device", Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
        else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(VideoActivity.this, new String[]{android.Manifest.permission.CAMERA}, 50);
            }else {
                cameraId = findFrontFacingCamera();
                if (cameraId < 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(VideoActivity.this, "No front facing camera found.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    releaseCameraAndPreview();
                    SurfaceView surfaceView = new SurfaceView(this);
                    try {
                        camera = Camera.open(cameraId);
                        camera.unlock();
                        camera.setPreviewDisplay(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void getRequestDetailsByRequestId(String message_id) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Show loading indicator
                ProgressDialog dialog = new ProgressDialog(VideoActivity.this);
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();

                Log.e(TAG, "Meeting ID: " + message_id);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("RequestId", message_id);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String data = obj.toString();

                Call<RequestDetailsModel> responseBodyCall = serviceLocal.getRequestDetailsByRequestId(authToken, data);
                responseBodyCall.enqueue(new Callback<RequestDetailsModel>() {
                    @Override
                    public void onResponse(Call<RequestDetailsModel> call, Response<RequestDetailsModel> response) {
                        // Dismiss loading indicator
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (response.isSuccessful() && response.body() != null) {
                            // Handle successful response
                            //Toast.makeText(VideoActivity.this, "Agree count updated.", Toast.LENGTH_SHORT).show();
                            if (response.body().getResults().getRequestCreatorTypeId() == 1) {
                                IS_REQUEST_CREATED_BY_CUSTOMER = true;
                            }
                        } else {
                            // Handle unsuccessful response
                            Log.d("onResponse", "Unsuccessful response");
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestDetailsModel> call, Throwable t) {
                        // Dismiss loading indicator
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        // Handle network failure
                        Log.d("onFailure", "Network call failed: " + t.toString());
                    }
                });
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void getAllNotarizationAction() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Show loading indicator
                /*ProgressDialog dialog = new ProgressDialog(VideoActivity.this);
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();*/

                Call<NotarizationActionModel> responseBodyCall = serviceLocal.getAllNotarizationAction(authToken);
                responseBodyCall.enqueue(new Callback<NotarizationActionModel>() {
                    @Override
                    public void onResponse(Call<NotarizationActionModel> call, Response<NotarizationActionModel> response) {
                        // Dismiss loading indicator
                        /*if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }*/
                        if (response.isSuccessful() && response.body() != null) {
                            // Handle successful response
                            Log.d(TAG, "onResponse: getAllNotarizationAction: "+new Gson().toJson(response.body()));
                            mNotarizationModel = response.body().getNotarizationActions();
                            Log.d(TAG, "onResponse: mNotarizationModel data: "+new Gson().toJson(mNotarizationModel));
                            NotarizationActionModel.getInstance().setNotarizationActions(mNotarizationModel);
                        } else {
                            // Handle unsuccessful response
                            Log.d("onResponse", "Unsuccessful response");
                        }
                    }

                    @Override
                    public void onFailure(Call<NotarizationActionModel> call, Throwable t) {
                        // Dismiss loading indicator
                        /*if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }*/
                        // Handle network failure
                        Log.d("onFailure", "Network call failed: " + t.toString());
                    }
                });
            }
        });
    }

    //Added by Rupesh
    private void showThumbnailInPrimaryVideoSection(){
        Log.d(TAG , "Thread Name in showThumbnailInPrimaryVideoSection: "+Thread.currentThread().getName());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                video_view.removeAllViews();
            }
        });
        VideoView videoView = new VideoView(this);
        video_view.addView(videoView);
        primaryVideoView = videoView;
        if (this.videoCallModel.getLocalVideoTrack() != null) {
            //localVideoTrack.removeRenderer(primaryVideoView); commented by Pankaj Khare
            if(thumbnailVideoView!=null)
            {
                this.videoCallModel.getLocalVideoTrack().removeRenderer(videoView);

            }

            this.videoCallModel.getLocalVideoTrack().addRenderer(videoView);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                participant_initial.setVisibility(View.GONE);
                video_view.setVisibility(View.VISIBLE);
                rl_recording_section.setVisibility(View.VISIBLE);
                tv_activeParticipantName.setText("You");
                mActiveParticipantIndex = -1;
            }
        });
    }

    private void releaseCameraAndPreview() {
        Log.d(TAG , "Thread Name in releaseCameraAndPreview: "+Thread.currentThread().getName());
        if (camera != null) {
            camera.lock();
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    // slide the view from below itself to the current position
    public void slideUp(View controlView , View participantView){
        Log.d(TAG , "Thread Name in slideUp: "+Thread.currentThread().getName());
        controlView.setVisibility(View.VISIBLE);

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                controlView.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(600);
        animate.setFillAfter(true);
        controlView.startAnimation(animate);
        isControlMenuClickable = true;

        TranslateAnimation animate2 = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                70,  // fromYDelta
                0);                // toYDelta
        animate2.setDuration(800);
        animate2.setFillAfter(true);
        participantView.startAnimation(animate2);

    }

    // slide the view from its current position to below itself
    public void slideDown(View controlView , View participantView){
        Log.d(TAG , "Thread Name in slideDown: "+Thread.currentThread().getName());
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                controlView.getHeight()+30); // toYDelta
        animate.setDuration(600);
        animate.setFillAfter(true);
        controlView.startAnimation(animate);
        isControlMenuClickable = false;

        TranslateAnimation animate2 = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                70); // toYDelta
        animate2.setDuration(800);
        animate2.setFillAfter(true);
        participantView.startAnimation(animate2);


    }

    public void onSlideViewButtonClick(View controlView , View participantView) {
        Log.d(TAG , "Thread Name in onSlideViewButtonClick: "+Thread.currentThread().getName());
        if (isUp) {
            slideDown(controlView , participantView);
        } else {
            slideUp(controlView , participantView);
        }
        isUp = !isUp;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG , "Thread Name in onTouch: "+Thread.currentThread().getName());
        if(video_view==v && remoteParticipantList!=null && remoteParticipantList.size()>0)
        {
            if(MotionEvent.ACTION_DOWN==event.getActionMasked()) {
                onSlideViewButtonClick(llTest , participantsview);
            }
            return true;
        }
        return true;
    }

    public void notifytoOhterParticipants()
    {
        Log.d(TAG , "Thread Name in notifytoOhterParticipants: "+Thread.currentThread().getName());
        Log.e(TAG , "remote participant list size in notifytoOhterParticipants: "+remoteParticipantList.size());
        if(participantsAdapter==null && remoteParticipantList!=null && remoteParticipantList.size()>1) {
            participantsview.setVisibility(View.VISIBLE);
            participantsAdapter = new ParticipantsAdapter(remoteParticipantList, VideoActivity.this, onclickInterface, isMyViewActive);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(participantsAdapter);
        }
        else if(remoteParticipantList!=null && remoteParticipantList.size()>1)
        {
            //participantsview.setVisibility(View.VISIBLE);
            participantsAdapter.notifyDataSetChanged();
            if (isMyViewActive){
                participantsAdapter.refreshParticipants(-1);
                showThumbnailInPrimaryVideoSection();
            }
        }
        else{
            if (participantsview!=null){
                //participantsview.setVisibility(View.GONE);
            }

            if (participantsAdapter!=null){
                try {
                    //participantsAdapter.notifyDataSetChanged();
                    if (isMyViewActive){
                        participantsAdapter.refreshParticipants(-1);
                        showThumbnailInPrimaryVideoSection();
                    }
                } catch (Exception e) {
                    Log.d(TAG , "Exception: "+e);
                    e.printStackTrace();
                }
            }
        }
        if (member_Type == 1) {
            if (dialogViewHost != null && dialogViewHost.isShowing()) {
                if (rVPadapter != null) {
                    rVPadapter.notifyDataSetChanged();
                }
                if (remoteParticipantList.size() > 0) {
                    rViewPopup.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                } else {
                    if (rViewPopup != null) {
                        rViewPopup.setVisibility(View.GONE);
                    }
                    if (emptyView != null) {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG , "Thread Name in onResume: "+Thread.currentThread().getName());
        if (sharedPreference!=null && sharedPreference.getBoolean(Constants.CALL_ENDED_IN_BACKGROUND)){
            exitFromTheRoom();
        }
        if (sharedPreference!=null && sharedPreference.getBoolean(Constants.SIGNATURE_CAPTURE_REQUEST_IN_BACKGROUND)){
            showSignatureCaptureDialog();
            sharedPreference.setBoolean(Constants.SIGNATURE_CAPTURE_REQUEST_IN_BACKGROUND , false);
        }
        if (sharedPreference!=null && sharedPreference.getBoolean(Constants.INITIAL_CAPTURE_REQUEST_IN_BACKGROUND)){
            showInitialCaptureDialog();
            sharedPreference.setBoolean(Constants.INITIAL_CAPTURE_REQUEST_IN_BACKGROUND , false);
        }
        if (sharedPreference!=null && sharedPreference.getBoolean(Constants.SIGNATURE_INITIAL_REPLACE_REQUEST_IN_BACKGROUND)){
            showReplaceSignatureInitialDialog();
            sharedPreference.setBoolean(Constants.SIGNATURE_INITIAL_REPLACE_REQUEST_IN_BACKGROUND , false);
        }

        if (!isInternetAvailable()){
            openNoInternetScreen();
        }else {
            closeNoInternetScreen();
        }
        if(lmdAudioSwitch!=null) {
            lmdAudioSwitch.start();
        }
        //Commented by Rupesh to resolve voice issue
        /*if(lmdAudioSwitch!=null) {
            lmdAudioSwitch.selectDeviceForVideoCall();
        }*/
        //Commented by Rupesh to resolve voice issue
        isRemovingParticipant=false;
        //        if(isAutoDisableVideo && videoCallManager!=null && videoCallModel!=null)
//        {
//            videoCallModel.getLocalVideoTrack().enable(true);
//            isAutoDisableVideo=false;
//        }
//        if(isAutoDisableAudio && videoCallManager!=null && videoCallModel!=null)
//        {
//            videoCallModel.getLocalAudioTrack().enable(true);
//            isAutoDisableAudio=false;
//        }

        boolean screenStatus = false;
        if(sp_call_view != null && sp_call_view.getSelectedItemPosition() == 0){
            screenStatus = false;
        }else {
            screenStatus = true;
        }

        if (sharedPreference != null && sharedPreference.getBoolean(Constants.SCREEN_SUBSCRIBED_IN_BACKGROUND)){
            screenStatus = true;
        }

        if (sharedPreference != null && sharedPreference.getBoolean(Constants.SCREEN_UNSUBSCRIBED_IN_BACKGROUND)){
            screenStatus = false;
        }

        EventBus.getDefault().register(this);
        if(videoCallManager!=null)
        {
            IS_APP_IN_BACKGROUND = false;
            videoCallManager.appInForeground(screenStatus);
        }
        if (Constants.isTimerEnable && meetingDuration != null) {
            startCountDown(meetingDuration);
        } else {
            //tv_expire_time.setText("Meeting in progress...");
        }

        if (!isMyViewActive && mainThumbnailView != null){
            mainThumbnailView.setBackground(VideoActivity.this.getDrawable(R.drawable.unselected_participant_background));
        }

    }

    @Override
    protected void onPause() {
        Log.d(TAG , "Thread Name in onPause: "+Thread.currentThread().getName());
        //AppSession.getSingletonInstance(this).setActivityRunning("VideoActivity");
        if(lmdAudioSwitch!=null) {
            lmdAudioSwitch.stop();
        }
        //        if(!isDisableVideo && videoCallManager!=null && videoCallModel!=null)
//        {
//            videoCallModel.getLocalVideoTrack().enable(false);
//            isAutoDisableVideo=true;
//        }
//        if(!isDisableAudio && videoCallManager!=null && videoCallModel!=null)
//        {
//            videoCallModel.getLocalAudioTrack().enable(false);
//            isAutoDisableAudio=true;
//        }
        EventBus.getDefault().unregister(this);
        if(videoCallManager!=null)
        {
            IS_APP_IN_BACKGROUND = true;
            videoCallManager.appInBackground();
            startTimerInBackground();
            mCountDownTimerInBackground.start();
        }

        if (camera != null) {
            camera.release();
            camera = null;
        }

        // Added by Rupesh to pause localVideoTrack
        //this.videoCallModel.getLocalVideoTrack().enable(false);

        super.onPause();

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG , "Thread Name in onDestroy: "+Thread.currentThread().getName());
        //sharedPreference.setBoolean(Constants.isScreenShared , false);

        if (screenVideoTrack != null) {
            screenVideoTrack.release();
            screenVideoTrack = null;
        }

        if(videoCallManager!=null)
        {
            videoCallManager.endForeground();
            videoCallManager.unbindService();
            videoCallManager=null;
        }
        if(lmdAudioSwitch!=null)
        {
            lmdAudioSwitch=null;
        }

        /*Added by Rupesh*/
        if (screenCapturerManager != null){
            screenCapturerManager.endForeground();
            screenCapturerManager.unbindService();
            screenCapturerManager = null;
        }
        /*Added by Rupesh*/

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        exitFromTheRoom();

        NotarizationActionUpdateManger.updateNotarizationAction(
                VideoActivity.this, authToken,
                requestID, "", userId, customerType,
                "18", "1", "");

        super.onDestroy();
    }

    public  void ScreenShareOn(RemoteParticipant remoteParticipant)
    {
        Log.d(TAG , "Thread Name in ScreenShareOn: "+Thread.currentThread().getName());
        if (selectedRemoteParticipant != null && selectedRemoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipant.getIdentity())) {
            selectedRemoteParticipant.setTechRevScreenEnable(true);
            selectedRemoteParticipant.setTechRevScreenSelected(true);
            //selectedRemoteParticipant.setTechRevVideoEnable(false);
            showHidePrimaryVideo(selectedRemoteParticipant);
        }

        notifytoOhterParticipants();

    }

    public  void ScreenShareOff(RemoteParticipant remoteParticipant, RemoteVideoTrack remoteVideoTrack)
    {
        Log.d(TAG , "Thread Name in ScreenShareOff: "+Thread.currentThread().getName());
        Log.e(TAG , "Remotepasrticipant: "+remoteParticipant);
        if (selectedRemoteParticipant != null && selectedRemoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipant.getIdentity())) {
            if(remoteVideoTrack.getName().equalsIgnoreCase("screen")) {
                selectedRemoteParticipant.setTechRevScreenEnable(false);
                if (primaryVideoView!=null && selectedRemoteParticipant.isTechRevScreenSelected()) {
                    remoteVideoTrack.removeRenderer(primaryVideoView);
                    primaryVideoView = null;
                }
            }
            showHidePrimaryVideo(selectedRemoteParticipant);
        }
        notifytoOhterParticipants();

    }

    private boolean checkPermissionForCameraAndMicrophone() {
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return resultCamera == PackageManager.PERMISSION_GRANTED &&
                resultMic == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForCameraAndMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECORD_AUDIO)) {
//            Toast.makeText(this,
//                    "R.string.permissions_needed",
//                    Toast.LENGTH_LONG).show();

            //Newly Added for Permisson Handling start
            if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED)

            ) {
                // If you do not have permission, request it
                ActivityCompat.requestPermissions(VideoActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                        1);
            }
            //Newly Added for Permission Handling Ends


        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    CAMERA_MIC_PERMISSION_REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doThis(EventModel eventModel) throws JSONException {

        speakingParticipantDetailsEventModel = eventModel;

        if(eventModel.getEventType() == Constants.EVENTTYPE_NONE)
            Log.e(TAG , "Inside doThis: 1 = EVENTTYPE_NONE");
        if(eventModel.getEventType() == Constants.EVENTTYPE_CONNECTED_IN_ROOM)
            isConnectedInRoom = true;
        Log.e(TAG , "Inside doThis: 2 = EVENTTYPE_CONNECTED_IN_ROOM");
        if(eventModel.getEventType() == Constants.EVENTTYPE_DISCONNECTED_FROM_ROOM)
            Log.e(TAG , "Inside doThis: 3 = EVENTTYPE_DISCONNECTED_FROM_ROOM");
        if(eventModel.getEventType() == Constants.EVENTTYPE_PARTICIPANT_CONNECTED_IN_ROOM)
            Log.e(TAG , "Inside doThis: 4 = EVENTTYPE_PARTICIPANT_CONNECTED_IN_ROOM");
        if(eventModel.getEventType() == Constants.EVENTTYPE_PARTICIPANT_DISCONNECTED_FROM_ROOM)
            Log.e(TAG , "Inside doThis: 5 = EVENTTYPE_PARTICIPANT_DISCONNECTED_FROM_ROOM");
        if(eventModel.getEventType() == Constants.EVENTTYPE_PARTICIPANT_MESSAGE_COMING_FROM_ROOM)
            Log.e(TAG , "Inside doThis: 6 = EVENTTYPE_PARTICIPANT_MESSAGE_COMING_FROM_ROOM");
        if(eventModel.getEventType() == Constants.EVENTTYPE_MESSAGE_SEND_TO_ROOM)
            Log.e(TAG , "Inside doThis: 7 = EVENTTYPE_MESSAGE_SEND_TO_ROOM");
        if(eventModel.getEventType() == Constants.EVENTTYPE_RECONNECTING)
            Log.e(TAG , "Inside doThis: 8 = EVENTTYPE_RECONNECTING");
        if(eventModel.getEventType() == Constants.EVENTTYPE_RECONNECTED)
            Log.e(TAG , "Inside doThis: 9 = EVENTTYPE_RECONNECTED");
        if(eventModel.getEventType() == Constants.EVENTTYPE_RESET)
            Log.e(TAG , "Inside doThis: 10 = EVENTTYPE_RESET");
        if(eventModel.getEventType() == Constants.EVENTTYPE_FAILURE)
            Log.e(TAG , "Inside doThis: 12 = EVENTTYPE_FAILURE");
        if(eventModel.getEventType() == Constants.EVENTTYPE_SCREEN_SUBSCRIBED)
            Log.e(TAG , "Inside doThis: 13 = EVENTTYPE_SCREEN_SUBSCRIBED");
        if(eventModel.getEventType() == Constants.EVENTTYPE_SCREEN_UNSUBSCRIBED)
            Log.e(TAG , "Inside doThis: 14 = EVENTTYPE_SCREEN_UNSUBSCRIBED");
        if(eventModel.getEventType() == Constants.EVENTTYPE_VIDEO_SUBSCRIBED)
            Log.e(TAG , "Inside doThis: 15 = EVENTTYPE_VIDEO_SUBSCRIBED");
        if(eventModel.getEventType() == Constants.EVENTTYPE_VIDEO_UNSUBSCRIBED)
            Log.e(TAG , "Inside doThis: 16 = EVENTTYPE_VIDEO_UNSUBSCRIBED");
        if(eventModel.getEventType() == Constants.EVENTTYPE_VIDEO_ENABLED)
            Log.e(TAG , "Inside doThis: 17 = EVENTTYPE_VIDEO_ENABLED");
        if(eventModel.getEventType() == Constants.EVENTTYPE_VIDEO_DISABLED)
            Log.e(TAG , "Inside doThis: 18 = EVENTTYPE_VIDEO_DISABLED");
        if(eventModel.getEventType() == Constants.EVENTTYPE_AUDIO_ENABLED)
            Log.e(TAG , "Inside doThis: 19 = EVENTTYPE_AUDIO_ENABLED");
        if(eventModel.getEventType() == Constants.EVENTTYPE_AUDIO_DISABLED)
            Log.e(TAG , "Inside doThis: 20 = EVENTTYPE_AUDIO_DISABLED");

        Log.e(TAG , "DATA TRACK DETAILS: "+new Gson().toJson(eventModel.getDataModel()));

        if (eventModel.getDataModel() != null){
            if (eventModel.getDataModel().getTo().equalsIgnoreCase("All") || eventModel.getDataModel().getTo().equalsIgnoreCase(userMeetingIdentifier)) {
                processRequest(eventModel.getDataModel());
            }
        }

        if(eventModel.getDataModel()!=null && eventModel.getEventType()== Constants.EVENTTYPE_PARTICIPANT_MESSAGE_COMING_FROM_ROOM) {
            Log.d("===Inside", "ProcessRequest onMessageValue: " + eventModel.getDataModel().getMessageType());
            Log.d("===Inside", "ProcessRequest onMessageValue: " + eventModel.getDataModel().getContent());

            Log.d("===Inside", "ProcessRequest onMessageValue: " + eventModel.getDataModel().getFrom());

            Log.d("===Inside", "ProcessRequest onMessageValue: " + eventModel.getDataModel().getTo());
            Log.d("===Inside", "ProcessReaquest userMeetingIdentifier: " + userMeetingIdentifier);


            /*if (eventModel.getDataModel().getTo().equalsIgnoreCase("All") || eventModel.getDataModel().getTo().equalsIgnoreCase(userMeetingIdentifier)) {
                Log.d("===Inside", "ProcessRequest onMessageValue: IF ");
//                    method = Class.forNme("com.blah.MyActivity").getMethod(dModel.getMessageType(),String.class);
//                    method.invoke(someInstanceOfMyActivity, stringValue);
                processRequest(eventModel.getDataModel());
            }*/
        }

        else if(eventModel.getEventType()== Constants.EVENTTYPE_SCREEN_SUBSCRIBED)
        {
            SCREEN_SUBSCRIBED = true;
            //Toast.makeText(VideoActivity.this, "started screen sharing", Toast.LENGTH_SHORT).show();
            //sharedPreference.setBoolean(Constants.isScreenShared , true);
            sp_call_view.setSelection(1);
            mVideoCallViewAdapter.setSelection(1);
            moveLocalVideoToThumbnailView(); // added By Rupesh Kumar Jena
            ScreenShareOn(eventModel.getTechrevRemoteParticipant().remoteParticipant);
            showParticipantScreenInFront(screenShowingParticipantPosition);
        }
        else if(eventModel.getEventType()== Constants.EVENTTYPE_SCREEN_UNSUBSCRIBED)
        {
            SCREEN_SUBSCRIBED = false;
            //sharedPreference.setBoolean(Constants.isScreenShared , false);
            ScreenShareOff(eventModel.getTechrevRemoteParticipant().remoteParticipant,eventModel.getRemoteVideoTrack());
        }
        else if(eventModel.getEventType()== Constants.EVENTTYPE_RECONNECTING)
        {
            reconnectingProgressBar.setVisibility(View.VISIBLE);

        }
        else if(eventModel.getEventType()== Constants.EVENTTYPE_PARTICIPANT_DISCONNECTED_FROM_ROOM)
        {
            String name = eventModel.getTechrevRemoteParticipant().remoteParticipant.getIdentity();
            List<String> splitString = Arrays.asList(name.split("\\-"));
            String participantName = "";
            if (splitString.size() > 0){
                participantName = splitString.get(1);
            }else {
                try {
                    participantName = removePrefix(name, "participant-");
                    participantName = removePrefix(name, "hoster-");
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            String finalParticipantName = participantName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(VideoActivity.this, finalParticipantName +" Left", Toast.LENGTH_SHORT).show();
                }
            });
            reconnectingProgressBar.setVisibility(View.GONE);
            removeRemoteParticipant(eventModel.getTechrevRemoteParticipant().remoteParticipant);
            if (name.contains("hoster-")){
                if (alertDialog1 != null){
                    if (alertDialog1.isShowing()){
                        alertDialog1.dismiss();
                    }
                }

                if (alertDialog2 != null){
                    if (alertDialog2.isShowing()){
                        alertDialog2.dismiss();
                    }
                }

                // Added By Rupesh
                //exitFromTheRoom();
                // Added By Rupesh

            }

            if (!isMyViewActive){
                if (remoteParticipantList != null && remoteParticipantList.size() > 0) {
                    participantsAdapter.refreshParticipants(0);
                } else {
                    participantsAdapter.refreshParticipants(-1);
                }
            }

            if (sp_call_view.getSelectedItemPosition() == 1 && SCREEN_SUBSCRIBED){
                showParticipantScreenInFront(screenShowingParticipantPosition);
            }

        }
        else if(eventModel.getEventType()== Constants.EVENTTYPE_FAILURE)
        {
            exitFromTheRoom();
        }
        else if(eventModel.getEventType()== Constants.EVENTTYPE_RECONNECTED)
        {
            reconnectingProgressBar.setVisibility(View.GONE);
            isConnectedInRoom=true;

        }
        else if(eventModel.getEventType()== Constants.EVENTTYPE_RESET)
        {
            if(eventModel.getVideoCallModel()!=null)
            {
                this.videoCallModel=eventModel.getVideoCallModel();
            }
        }
        else if(eventModel.getEventType()== Constants.EVENTTYPE_PARTICIPANT_CONNECTED_IN_ROOM)
        {
            if (!isInternetAvailable()){
                openNoInternetScreen();
            }else {
                closeNoInternetScreen();
            }

            if (dialogNoHost != null && dialogNoHost.isShowing()){
                dialogNoHost.dismiss();
            }
            String name = eventModel.getTechrevRemoteParticipant().remoteParticipant.getIdentity();
            List<String> splitString = Arrays.asList(name.split("\\-"));
            String participantName = "";
            if (splitString.size() > 0){
                participantName = splitString.get(1);
            }else {
                try {
                    participantName = removePrefix(name, "participant-");
                    participantName = removePrefix(name, "hoster-");
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            String finalParticipantName = participantName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(VideoActivity.this, finalParticipantName +" Joined", Toast.LENGTH_SHORT).show();
                }
            });
            reconnectingProgressBar.setVisibility(View.GONE);
            remoteParticipantList.add(eventModel.getTechrevRemoteParticipant());
            addRemoteParticipant(eventModel.getTechrevRemoteParticipant());
            Log.e(TAG , "New participant list size: "+remoteParticipantList.size());
            showNootherparticipant();
            notifytoOhterParticipants();
        }

        else if(eventModel.getEventType()== Constants.APP_STATUS_FOREGROUND)
        {
            reconnectingProgressBar.setVisibility(View.GONE);
            showAll(eventModel);
            boolean selectedFound=false;
            for (int i = 0; i < remoteParticipantList.size(); i++) {
                if (selectedRemoteParticipant != null && selectedRemoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipantList.get(i).remoteParticipant.getIdentity())) {
                    selectedFound=true;
                    showHidePrimaryVideo(selectedRemoteParticipant);
                }
            }
            if(!selectedFound)
            {
                if(remoteParticipantList.size()>0)
                {
                    selectedRemoteParticipant=remoteParticipantList.get(0);
                    showHidePrimaryVideo(selectedRemoteParticipant);
                }
            }
            notifiyOthersBackFromBackground();
        }
        else if(eventModel.getEventType()== Constants.EVENTTYPE_VIDEO_SUBSCRIBED)
        {
            for (int i = 0; i < remoteParticipantList.size(); i++) {
                if (selectedRemoteParticipant != null && selectedRemoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipantList.get(i).remoteParticipant.getIdentity())) {

                    showHidePrimaryVideo(selectedRemoteParticipant);
                }
            }
            showOtherParticipants();
        }
        else if(eventModel.getEventType()== Constants.EVENTTYPE_VIDEO_UNSUBSCRIBED)
        {
            for (int i = 0; i < remoteParticipantList.size(); i++) {
                if (selectedRemoteParticipant != null && selectedRemoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipantList.get(i).remoteParticipant.getIdentity())) {

                    showHidePrimaryVideo(selectedRemoteParticipant);
                }
            }
            showOtherParticipants();
        }
        /*else if(eventModel.getEventType()== Constants.EVENTTYPE_PARTICIPANT_DISCONNECTED_FROM_ROOM)
        {
            String name = eventModel.getTechrevRemoteParticipant().remoteParticipant.getIdentity();
            List<String> splitString = Arrays.asList(name.split("\\-"));
            String participantName = "";
            if (splitString.size() > 0){
                participantName = splitString.get(1);
            }else {
                try {
                    participantName = removePrefix(name, "participant-");
                    participantName = removePrefix(name, "hoster-");
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            String finalParticipantName = participantName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(VideoActivity.this, finalParticipantName +" Left", Toast.LENGTH_SHORT).show();
                }
            });
            isRemovingParticipant=true;
            removeRemoteParticipant(eventModel.getTechrevRemoteParticipant().remoteParticipant);
            isRemovingParticipant=false;
            if (name.contains("hoster-")){
                if (alertDialog1 != null){
                    if (alertDialog1.isShowing()){
                        alertDialog1.dismiss();
                    }
                }

                if (alertDialog2 != null){
                    if (alertDialog2.isShowing()){
                        alertDialog2.dismiss();
                    }
                }

            }
        }*/

        else if(eventModel.getEventType() == Constants.EVENTTYPE_CONNECTED_IN_ROOM)
        {

            if (!isInternetAvailable()){
                openNoInternetScreen();
            }else {
                closeNoInternetScreen();
            }

            if (sp_call_view.getSelectedItemPosition() == 1 && SCREEN_SUBSCRIBED){
                showParticipantScreenInFront(screenShowingParticipantPosition);
            }else {
                if (!isMyViewActive){
                    showNewParticipantVideoInFront(eventModel);
                }
            }

            try {
                checkIfUserAllowedNotaryToCaptureSignatureAndInitial(requestID , userId);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            getRequestDetailsByRequestId(requestID);

        }
        else if(eventModel.getEventType()== Constants.EVENTTYPE_DISCONNECTED_FROM_ROOM)
        {

            //sharedPreference.setBoolean(Constants.isScreenShared , false);
            reconnectingProgressBar.setVisibility(View.GONE);
            isConnectedInRoom=false;
            exitFromTheRoom();

        }
        else if(eventModel.getEventType()== Constants.EVENTTYPE_DOMINANT_SPEAKER){
            if(sp_call_view.getSelectedItemPosition() == 0){
                if(eventModel != null){
                    speakingParticipantDetailsEventModel = eventModel;
                }
                try {
                    setSpeakerViewAsDefault(eventModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showNewParticipantVideoInFront(EventModel eventModel){
        Log.d(TAG , "Thread Name in showNewParticipantVideoInFront: "+Thread.currentThread().getName());
        reconnectingProgressBar.setVisibility(View.GONE);

        showAll(eventModel);
        isConnectedInRoom=true;

        if (member_Type == 1) {
            if (dialogViewHost != null && dialogViewHost.isShowing()) {
                rVPadapter.notifyDataSetChanged();

                if (remoteParticipantList.size() > 0) {
                    rViewPopup.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                } else {
                    rViewPopup.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        }
        startLocationUpdates();
        if (member_Type == Constants.member_type_participant && Constants.isLocationEnable) {
            // getLocation(0); // commented by Pankaj because new code is written below
            //startLocationUpdates();
            locationSharing.setVisibility(View.VISIBLE);
        } else {
            locationSharing.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void notifiyOthersFromForground(RemoteParticipant remoteParticipant, int ActionType) throws JSONException {
        Log.d(TAG , "Thread Name in notifiyOthersFromForground: "+Thread.currentThread().getName());
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
        if (dModel.getTo().equalsIgnoreCase("All") || dModel.getTo().equalsIgnoreCase(userMeetingIdentifier)) {
            processRequest(dModel);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void notifiyOthersBackFromBackground() throws JSONException {

        Log.d(TAG , "Thread Name in notifiyOthersBackFromBackground: "+Thread.currentThread().getName());
        for (int i = 0; i < remoteParticipantList.size(); i++)
        {

            if(remoteParticipantList.get(i).getTechRevVideoEnable()==false)
            {
                notifiyOthersFromForground(remoteParticipantList.get(i).remoteParticipant,Constants.EVENTTYPE_VIDEO_DISABLED);
            }
            if(remoteParticipantList.get(i).getTechRevVideoEnable()==true)
            {
                notifiyOthersFromForground(remoteParticipantList.get(i).remoteParticipant,Constants.EVENTTYPE_VIDEO_ENABLED);
            }

            if(remoteParticipantList.get(i).getTechRevAudioEnable()==false)
            {
                notifiyOthersFromForground(remoteParticipantList.get(i).remoteParticipant,Constants.EVENTTYPE_AUDIO_DISABLED);
            }

            if(remoteParticipantList.get(i).getTechRevAudioEnable()==true)
            {
                notifiyOthersFromForground(remoteParticipantList.get(i).remoteParticipant,Constants.EVENTTYPE_AUDIO_ENABLED);
            }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showAll(EventModel eventModel)
    {
        Log.d(TAG , "Thread Name in showAll: "+Thread.currentThread().getName());
        //Newly Added for localDataTrack
        if(eventModel.getVideoCallModel()!=null)
        {
            this.videoCallModel=eventModel.getVideoCallModel();
        }
        if(this.videoCallModel!=null && this.videoCallModel.isConnectionStatus()) {
            remoteParticipantList.clear();
            if (eventModel.getRemoteParticipantList() != null && eventModel.getRemoteParticipantList().size() > 0) {
                for (TechrevRemoteParticipant techrevRemoteParticipant : eventModel.getRemoteParticipantList()) {
                    // Have to check all Room participant before adding into list
                    remoteParticipantList.add(techrevRemoteParticipant);
                    addRemoteParticipant(techrevRemoteParticipant);
                }
            }
            /*Added By Rupesh*/
            if (remoteParticipantList.size() == 0){
                if (dialogNoHost != null){
                    dialogNoHost.show();
                    dialogNoHost.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.color_primary));
                }
                //return;
            }else {
                if (dialogNoHost != null && dialogNoHost.isShowing()){
                    dialogNoHost.dismiss();
                }
            }
            /*Added By Rupesh*/
            showNootherparticipant();
            showOtherParticipants();
            moveLocalVideoToThumbnailView(); // added By Pankaj Khare
        }
    }

    public  void showOtherParticipants()
    {
        Log.d(TAG , "Thread Name in showOtherParticipants: "+Thread.currentThread().getName());
        notifytoOhterParticipants();
    }

    @SuppressLint("StaticFieldLeak")
    private void intializeUI() {
        Log.d(TAG , "Thread Name in intializeUI: "+Thread.currentThread().getName());
        onclickInterface = new onClickInterface() {
            @Override
            public void onClickVideo(int index , String participantName) {
                SCREEN_SUBSCRIBED = false;
                isMyViewActive = false;
                mainThumbnailView.setBackground(VideoActivity.this.getDrawable(R.drawable.unselected_participant_background));
                //sp_call_view.setSelection(0);
                //mVideoCallViewAdapter.setSelection(0);
                mActiveParticipantIndex = index;
                showParticipantVideoInFront(index);
                // Added by Rupesh
                if (!isMyViewActive){
                    Log.d(TAG , "PARTICIPANT NAME TO BE SHOWN IN MAIN VIEW: "+participantName);
                    mActiveParticipant = participantName;
                    tv_activeParticipantName.setText(participantName);
                }
                // Added by Rupesh
            }

            @Override
            public void onClickScreen(int index , String participantName) {
                Log.d(TAG , "Thread Name in onClickScreen: "+Thread.currentThread().getName());
                SCREEN_SUBSCRIBED = true;
                isMyViewActive = false;
                mainThumbnailView.setBackground(VideoActivity.this.getDrawable(R.drawable.unselected_participant_background));
                mActiveParticipantIndex = index;
                showParticipantScreenInFront(index);
                // Added by Rupesh
                if (!isMyViewActive){
                    Log.d(TAG , "PARTICIPANT NAME TO BE SHOWN IN MAIN VIEW: "+participantName);
                    mActiveParticipant = participantName;
                    tv_activeParticipantName.setText(participantName);
                    mActiveParticipantIndex = index;
                }
                // Added by Rupesh
            }
        };

        onClickInterfaceParticipant = new ONClickInterfaceParticipant() {
            @Override
            public void onClickParticipantCapture(int index) {
                Log.d(TAG , "Thread Name in onClickParticipantCapture: "+Thread.currentThread().getName());
                TechrevRemoteParticipant remoteParticipant = (TechrevRemoteParticipant) remoteParticipantList.get(index);
                Log.d("===CaptureInterface", "Called");

                if (!remoteParticipant.getTechRevVideoEnable() && remoteParticipantList.size() > 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(VideoActivity.this);
                    alert.setTitle("Alert");
                    alert.setMessage("Participant's camera is turned off so you cannot capture the image.");
                    alert.setPositiveButton("Ok", null);
                    alert.show();
                    return;
                }

                if (remoteParticipant != null) {
                    try {

                        alertDialog2 = new AlertDialog.Builder(VideoActivity.this).create();
                        alertDialog2.setTitle("Alert");
                        alertDialog2.setMessage("Please wait! Image capture is in progress.");
                        alertDialog2.setCancelable(false);
                        alertDialog2.show();


                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("from", userMeetingIdentifier);
                        jsonObject.put("to", remoteParticipant.remoteParticipant.getIdentity());
                        jsonObject.put("messageType", "CaptureImageFromCustomer");
                        jsonObject.put("content", "");
                        videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                    } catch (Exception e) {
                        Log.d("====Exception", "" + e.toString());
                    }

                }

            }

            @Override
            public void onClickParticipantAudio(int index) {
                Log.d(TAG , "Thread Name in onClickParticipantAudio: "+Thread.currentThread().getName());
                Log.d("===AudioInterface", "Called");

                boolean msgVal = false;
                TechrevRemoteParticipant remoteParticipant = (TechrevRemoteParticipant) remoteParticipantList.get(index);
                if (remoteParticipant != null) {

                    if (remoteParticipant.remoteParticipant.getRemoteAudioTracks().size() > 0) {
                        try {
                            RemoteAudioTrackPublication remoteAudioTrackPublication = remoteParticipant.remoteParticipant.getRemoteAudioTracks().get(0);
                            if (remoteAudioTrackPublication.isTrackEnabled() && remoteAudioTrackPublication.isTrackSubscribed()) {
                                Log.d("===Mic_Interface", "On");
                                msgVal = true;

                            } else {
                                //
                                Log.d("===Mic_Interface", "Off");
                                msgVal = false;
                            }

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("from", userMeetingIdentifier);
                            jsonObject.put("to", remoteParticipant.remoteParticipant.getIdentity());
                            jsonObject.put("messageType", "MuteOrUnmuteAudio");
                            jsonObject.put("messageValue", msgVal);
                            jsonObject.put("content", "");
                            videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                        } catch (Exception e) {
                            Log.d("====Exception", "" + e.toString());
                        }


                    }


                }

            }

            @Override
            public void onClickParticipantVideo(int index) {
                Log.d(TAG , "Thread Name in onClickParticipantVideo: "+Thread.currentThread().getName());
                Log.d("===VideoInterface", "Called");
                boolean msgVal = false;
                TechrevRemoteParticipant remoteParticipant = (TechrevRemoteParticipant) remoteParticipantList.get(index);
                if (remoteParticipant != null) {
                    try {
                        if (remoteParticipant.remoteParticipant.getRemoteVideoTracks().size() > 0) {
                            RemoteVideoTrackPublication remoteVideoTrackPublication = remoteParticipant.remoteParticipant.getRemoteVideoTracks().get(0);
                            if (remoteVideoTrackPublication.isTrackEnabled() && remoteVideoTrackPublication.isTrackSubscribed()) {
                                Log.d("===Video Interface", "On");
                                msgVal = true;

                            } else {
                                Log.d("===Video Interface", "Off");
                                msgVal = false;
                            }
                        }

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("from", userMeetingIdentifier);
                        jsonObject.put("to", remoteParticipant.remoteParticipant.getIdentity());
                        jsonObject.put("messageType", "SwitchOnOrOffVideo");
                        jsonObject.put("messageValue", msgVal);
                        jsonObject.put("content", "");
                        videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                    } catch (Exception e) {
                        Log.d("====Exception", "" + e.toString());
                    }

                }
            }

            @Override
            public void onClickParticipantCallEnd(int index) {
                Log.d(TAG , "Thread Name in onClickParticipantCallEnd: "+Thread.currentThread().getName());
                Log.d("===CallEndInterface", "Called");
                TechrevRemoteParticipant remoteParticipant = (TechrevRemoteParticipant) remoteParticipantList.get(index);
                if (remoteParticipant != null) {

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("from", userMeetingIdentifier);
                        jsonObject.put("to", remoteParticipant.remoteParticipant.getIdentity());
                        jsonObject.put("messageType", "LeaveFromRoom");
                        jsonObject.put("content", "Thank you for your time.");
                        videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                    } catch (Exception e) {
                        Log.d("====Exception", "" + e.toString());
                    }

                }
            }
        };

        participantsAdapter = new ParticipantsAdapter(remoteParticipantList, VideoActivity.this, onclickInterface, isMyViewActive);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(participantsAdapter);

        //Newly Added Starts
        if (member_Type == 1) {
            if (dialogViewHost != null && dialogViewHost.isShowing()) {
                rVPadapter.notifyDataSetChanged();
                if (remoteParticipantList.size() > 0) {
                    rViewPopup.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                } else {
                    rViewPopup.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }

            }
        } else {
        }
        //Newly Added Ends

        connectActionFab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (isControlMenuClickable){
                    commonExit();
                }
            }
        });

        if (Constants.isCaptureImageEnable) {
            captureActionFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AsyncTask<Void, Void, DocumentsByRequestIdModel>() {
                        @Override
                        protected DocumentsByRequestIdModel doInBackground(Void... voids) {
                            JSONObject json = new JSONObject();
                            try {
                                json.put("requestId", requestID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String jsonString = json.toString();
                            Call<DocumentsByRequestIdModel> docByReqID = serviceLocal.getDocumentsByRequestID(authToken, jsonString);

                            try {
                                Response<DocumentsByRequestIdModel> response = docByReqID.execute();
                                if (response.isSuccessful()) {
                                    return response.body();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(DocumentsByRequestIdModel response) {
                            if (response != null) {
                                // Handle the response and update UI accordingly
                                if (MaxImageCaptureLimit > 0) {
                                    // Rest of your logic based on the response
                                    // ...

                                    if (response.getTotal() == 0) {
                                        if (selectedRemoteParticipant != null) {
                                            RemoteVideoTrackPublication remoteVideoTrackPublication = selectedRemoteParticipant.remoteParticipant.getRemoteVideoTracks().get(0);
                                            if (remoteVideoTrackPublication.isTrackEnabled() && remoteVideoTrackPublication.isTrackSubscribed()) {
                                                Log.d("===Video1", "On");
                                                sendMessage();
                                            } else {
                                                Log.d("===Video1", "Off");
                                                // Add AlertPopup
                                                AlertDialog.Builder alert = new AlertDialog.Builder(VideoActivity.this);
                                                alert.setTitle("Alert");
                                                alert.setMessage("Participant's camera is turned off so you cannot capture the image.");
                                                alert.setPositiveButton("Ok", null);
                                                alert.show();
                                            }
                                        }
                                    } else if (response.getResults() != null) {
                                        if (response.getResults().size() < MaxImageCaptureLimit) {
                                            // Handle further logic based on the response
                                            // ...

                                            if (selectedRemoteParticipant != null) {
                                                if (selectedRemoteParticipant.remoteParticipant.getRemoteVideoTracks().size() > 0) {
                                                    RemoteVideoTrackPublication remoteVideoTrackPublication = selectedRemoteParticipant.remoteParticipant.getRemoteVideoTracks().get(0);
                                                    if (remoteVideoTrackPublication.isTrackEnabled() && remoteVideoTrackPublication.isTrackSubscribed()) {
                                                        sendMessage();
                                                    } else {
                                                        // Add AlertPopup
                                                        AlertDialog.Builder alert = new AlertDialog.Builder(VideoActivity.this);
                                                        alert.setTitle("Alert");
                                                        alert.setMessage("Participant's camera is turned off so you cannot capture the image.");
                                                        alert.setPositiveButton("Ok", null);
                                                        alert.show();
                                                    }
                                                } else {
                                                    // Add AlertPopup
                                                    AlertDialog.Builder alert = new AlertDialog.Builder(VideoActivity.this);
                                                    alert.setTitle("Alert");
                                                    alert.setMessage("Unable to capture the image, please try after sometime.");
                                                    alert.setPositiveButton("Ok", null);
                                                    alert.show();
                                                }
                                            } else {
                                                // Add AlertPopup
                                                AlertDialog.Builder alert = new AlertDialog.Builder(VideoActivity.this);
                                                alert.setTitle("Alert");
                                                alert.setMessage("Please select participant from the above participant's list and try again.");
                                                alert.setPositiveButton("Ok", null);
                                                alert.show();
                                            }
                                        } else {
                                            // Add AlertPopup
                                            AlertDialog.Builder alert = new AlertDialog.Builder(VideoActivity.this);
                                            alert.setTitle("Alert");
                                            alert.setMessage("As per your current plan, image capture limit is exceeded.");
                                            alert.setPositiveButton("Ok", null);
                                            alert.show();
                                        }
                                    } else {
                                        // Add AlertPopup
                                        AlertDialog.Builder alert = new AlertDialog.Builder(VideoActivity.this);
                                        alert.setTitle("Alert");
                                        alert.setMessage("Unable to get proper response from the server, please try later.");
                                        alert.setPositiveButton("Ok", null);
                                        alert.show();
                                    }
                                }
                            } else {
                                // Add AlertPopup
                                AlertDialog.Builder alert = new AlertDialog.Builder(VideoActivity.this);
                                alert.setTitle("Alert");
                                alert.setMessage("Unable to get response from the server, please try later.");
                                alert.setPositiveButton("Ok", null);
                                alert.show();
                            }
                        }
                    }.execute();
                }
            });

        } else {
            captureActionFab.setVisibility(View.GONE);
        }


        viewMeetingActionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, MeetingDetailsModel>() {
                    @Override
                    protected MeetingDetailsModel doInBackground(Void... voids) {
                        if (BaseURL != null && BaseURL.length() > 0 && requestID != null && requestID.length() > 0) {
                            BASE_URL_VAL = BaseURL + "api/";
                            JSONObject json = new JSONObject();
                            try {
                                json.put("requestId", requestID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String jsonString = json.toString();
                            Call<MeetingDetailsModel> meetingDetailsModelCall = serviceLocal.getAllMeetingDetails(authToken, jsonString);

                            try {
                                Response<MeetingDetailsModel> response = meetingDetailsModelCall.execute();
                                if (response.isSuccessful()) {
                                    return response.body();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(MeetingDetailsModel response) {
                        if (response != null) {
                            dialogView = new Dialog(VideoActivity.this);
                            dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialogView.setContentView(R.layout.popup_layout_design);
                            dialogView.setCancelable(true);
                            dialogView.show();

                            if (response.getResults() != null) {
                                if (response.getResults().getDocuments() != null) {
                                    LinearLayout linearLayout = dialogView.findViewById(R.id.linearLayout1);
                                    TextView txtViewUploadDoc = dialogView.findViewById(R.id.txtViewUploadDoc);
                                    if (response.getResults().getDocuments().size() > 0) {
                                        txtViewUploadDoc.setVisibility(View.VISIBLE);
                                        for (int x = 0; x < response.getResults().getDocuments().size(); x++) {
                                            ImageView image = new ImageView(VideoActivity.this);
                                            String temp = response.getResults().getDocuments().get(x).getDocUrl();
                                            String content = temp.substring(temp.indexOf('/') + 1);

                                            GlideUrl glideUrl = new GlideUrl(BASE_URL_VAL + content,
                                                    new LazyHeaders.Builder()
                                                            .addHeader("Authorization", authToken)
                                                            .build());

                                            Glide.with(VideoActivity.this)
                                                    .load(glideUrl)
                                                    .into(image);

                                            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 300);
                                            layoutParams.setMargins(10, 0, 0, 10);
                                            image.setLayoutParams(layoutParams);

                                            linearLayout.addView(image);
                                        }

                                    } else {
                                        txtViewUploadDoc.setVisibility(View.GONE);
                                    }

                                }
                            }

                            TextView titleName = dialogView.findViewById(R.id.titleName);
                            ImageView closeImg = dialogView.findViewById(R.id.close);

                            closeImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogView.dismiss();
                                }
                            });

                            if (response.getResults() != null) {
                                if (response.getResults().getMeetingTitle() != null) {
                                    titleName.setText(response.getResults().getMeetingTitle());
                                }
                            }

                            // Handle other UI updates using the 'response' object

                            // ... (rest of the code)

                        }
                    }
                }.execute();
            }
        });

        //Newly Added for Recording

        imgRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (member_Type == Constants.member_type_participant) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(VideoActivity.this);
                    alert.setTitle("Alert");
                    alert.setMessage("This meeting is being recorded and will be saved for future references as per the applicable RON state law requirements.");
                    alert.setPositiveButton("Ok", null);
                    alert.show();
                }

                if (member_Type == Constants.member_type_banker) {

                }


            }
        });


        //switchCameraActionFab.show();
        speakerActionFab.setOnClickListener(turnOnOffSpeakerListener());
        addFileActionFab.setOnClickListener(addFileListener());
        switchCameraActionFab.setOnClickListener(switchCameraClickListener());
        //localVideoActionFab.show();
        localVideoActionFab.setOnClickListener(localVideoClickListener());
        //muteActionFab.show();
        muteActionFab.setOnClickListener(muteClickListener());
        chatActionFab.setOnClickListener(chatClickListener());
        shareScreenActionFab.setOnClickListener(shareScreenClickListener());


        viewParticipantControlFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isControlMenuClickable){
                    sharedPreference.setBoolean(Constants.COSIGNER_ACTIVITY_IN_FOREGROUND , true);
                    Intent intent = new Intent(getApplicationContext(), AddCoSignerActivity.class);
                    intent.putExtra("AUTHORIZATION_TOKEN", authToken);
                    intent.putExtra("REQUEST_ID", requestID);
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("USER_MEETING_IDENTIFIER", userMeetingIdentifier);
                    intent.putExtra("IS_PRIMARY_SIGNER", isPrimarySigner);
                    startActivityForResult(intent, FLAG_ACTIVITY_ADD_CO_SIGNER);
                }
            }
        });

        audioRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        meetingShareDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (Constants.isLocationEnable) {
            locationSharing.setVisibility(View.VISIBLE);
            locationSharing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //getLocation(2);
                    // commented by Pankaj because new code is written below
                    if (isControlMenuClickable){
                        isLocationButtonClicked = true;
                        startLocationUpdates();
                    }

                }
            });
        } else {
            locationSharing.setVisibility(View.GONE);
        }


    }

    private void showParticipantScreenInFront(int index) {
        Log.d(TAG , "Thread Name in showParticipantScreenInFront: "+Thread.currentThread().getName());
        sp_call_view.setSelection(1);
        screenShowingParticipantPosition = index;
        if(sp_call_view.getSelectedItemPosition() == 1){
            if (Constants.isCaptureImageEnable) {

                for (int i = 0; i < remoteParticipantList.size(); i++) {
                    if (selectedRemoteParticipant != null && (selectedRemoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipantList.get(i).remoteParticipant.getIdentity()))) {
                        remoteParticipantList.get(i).setTechRevScreenSelected(true);
                    }
                }
                if (alertDialog2 == null) {
                    if(index != -1){
                        //video_view.setLayoutParams(layoutParamsPV);
                        TechrevRemoteParticipant remoteParticipant = (TechrevRemoteParticipant) remoteParticipantList.get(index);
                        if (!isMyViewActive){
                            showScreenOfSelectedParticipant(remoteParticipant);
                        }
                    }
                }
            }
        }else {
            sp_call_view.setSelection(0);
        }
    }

    private void setSpeakerViewAsDefault(EventModel eventModel) {
        Log.d(TAG , "Thread Name in setSpeakerViewAsDefault: "+Thread.currentThread().getName());
        int index = -1;
        for(int i = 0; i < remoteParticipantList.size() ; i++){
            Log.e(TAG , "Remote participantList value in set speaker: "+remoteParticipantList);
            Log.e(TAG , "Remote participant value in set speaker: "+remoteParticipantList.get(i).remoteParticipant);
            if(eventModel.getTechrevRemoteParticipant() != null && eventModel.getTechrevRemoteParticipant().remoteParticipant != null){
                String identity = remoteParticipantList.get(i).remoteParticipant.getIdentity();
                if(identity.equalsIgnoreCase(eventModel.getTechrevRemoteParticipant().remoteParticipant.getIdentity())){
                    index = i;
                    remoteParticipantList.get(i).setSpeaking(true);
                    Log.e(TAG , "Current participant who is speaking: "+remoteParticipantList.get(i).remoteParticipant.getIdentity());
                }else {
                    remoteParticipantList.get(i).setSpeaking(false);
                }
            }
        }
        //Got the remote participant who is speaking
        if(index != -1){
            mActiveParticipantIndex = index;
            if (!isMyViewActive){
                showParticipantVideoInFront(index);
            }
        }else {
            if(selectedRemoteParticipant != null){
                if (!isMyViewActive){
                    setSelectedRemoteParticipant(selectedRemoteParticipant);
                }
            }
        }
    }

    private void showParticipantVideoInFront(int index){
        Log.d(TAG , "Thread Name in showParticipantVideoInFront: "+Thread.currentThread().getName());
        TechrevRemoteParticipant remoteParticipant = (TechrevRemoteParticipant) remoteParticipantList.get(index);
//      showVideoOfSelectedParticipant(remoteParticipant);
        removeRenderedVideo(selectedRemoteParticipant);
        selectedRemoteParticipant = remoteParticipant;
        for (int i = 0; i < remoteParticipantList.size(); i++) {
            if (selectedRemoteParticipant != null && (selectedRemoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipantList.get(i).remoteParticipant.getIdentity()))) {
                remoteParticipantList.get(i).setTechRevScreenSelected(false);
            }
        }
        selectedRemoteParticipant.setTechRevScreenSelected(false);
        selectedRemoteParticipant.setTechRevVideoEnable(true);
        showHidePrimaryVideo(selectedRemoteParticipant);
    }

    @SuppressLint("SetTextI18n")
    private void addRemoteParticipant(TechrevRemoteParticipant remoteParticipant) {
        Log.d("====Inside", "addRemoteParticipant");
        Log.d(TAG , "Thread Name in addRemoteParticipant: "+Thread.currentThread().getName());
        if (selectedRemoteParticipant == null) {
            selectedRemoteParticipant = remoteParticipant;
            showHidePrimaryVideo(selectedRemoteParticipant);

        }

    }

    private void showHidePrimaryVideo(TechrevRemoteParticipant participant) {
        Log.d(TAG , "Thread Name in showHidePrimaryVideo: "+Thread.currentThread().getName());
        if (participant != null) {
            if(participant.isTechRevScreenSelected()) {
                removeRenderedVideo(participant);
            }
            else  if (participant.getTechRevVideoEnable()) {
                addrevRenderedVideo(participant);
            } else {
                removeRenderedVideo(participant);
            }
        }
    }

    /*
     * Set primary view as renderer for participant video track
     */

    private void removePrevRenderedVideo() {
        Log.d(TAG , "Thread Name in removePrevRenderedVideo: "+Thread.currentThread().getName());
        if (remoteParticipantList.size() > 0) {

            for (Object obj : remoteParticipantList) {
                TechrevRemoteParticipant remoteParticipant = (TechrevRemoteParticipant) obj;

                if (remoteParticipant != null) {

                    if (remoteParticipant.remoteParticipant.getRemoteVideoTracks().size() > 0) {

                        for (int index = 0; index < remoteParticipant.remoteParticipant.getVideoTracks().size(); index++) {
                            VideoTrack remoteVideoTrack = (VideoTrack) remoteParticipant.remoteParticipant.getVideoTracks().get(index).getVideoTrack();
                            if (remoteVideoTrack != null && primaryVideoView != null) {
//                                remoteVideoTrack.getRenderers().clear();
                                remoteVideoTrack.removeRenderer(primaryVideoView);
                            }

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                video_view.removeAllViews();
                                participant_initial.setVisibility(View.VISIBLE);
                                video_view.setVisibility(View.GONE);
                            }
                        });
                        setParticipantName(remoteParticipant.remoteParticipant.getIdentity());


                    }
                }

            }
        }

    }

    private class AddRenderedVideoTask extends AsyncTask<Void, Void, String> {

        private TechrevRemoteParticipant participant;

        public AddRenderedVideoTask(TechrevRemoteParticipant participant) {
            this.participant = participant;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String participantName = "";

            if (participant != null && participant.remoteParticipant != null) {
                String activeParticipant = participant.remoteParticipant.getIdentity();

                if (activeParticipant != null && activeParticipant.length() > 0) {
                    try {
                        List<String> splitString = Arrays.asList(activeParticipant.split("\\-"));

                        if (splitString.size() > 0) {
                            participantName = splitString.get(1);
                        } else {
                            participantName = removePrefix(activeParticipant, "participant-");
                        }
                    } catch (Exception e) {
                        participantName = activeParticipant;
                    }
                }
            }

            return participantName;
        }

        @Override
        protected void onPostExecute(String participantName) {
            // Update UI components on the main thread
            updatePrimaryVideoUIComponents(participant, participantName);
        }
    }

    private void updatePrimaryVideoUIComponents(TechrevRemoteParticipant participant, String participantName) {
        // Update UI components here
        Log.d(TAG , "Thread Name in updatePrimaryVideoUIComponents: "+Thread.currentThread().getName());
        if (participant != null && participant.remoteParticipant.getIdentity().equalsIgnoreCase(selectedRemoteParticipant.remoteParticipant.getIdentity())) {
            if (selectedRemoteParticipant != null && selectedRemoteParticipant.remoteParticipant.getRemoteVideoTracks().size() > 0) {
                for (int index = 0; index < selectedRemoteParticipant.remoteParticipant.getVideoTracks().size(); index++) {
                    VideoTrack remoteVideoTrack = (VideoTrack) selectedRemoteParticipant.remoteParticipant.getVideoTracks().get(index).getVideoTrack();
                    if (remoteVideoTrack != null && !remoteVideoTrack.getName().equals("screen") && !selectedRemoteParticipant.isTechRevScreenSelected()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                video_view.removeAllViews();
                                VideoView videoView = new VideoView(VideoActivity.this);
                                video_view.addView(videoView);
                                primaryVideoView = videoView;
                                remoteVideoTrack.addRenderer(videoView);
                                participant_initial.setVisibility(View.GONE);
                                video_view.setVisibility(View.VISIBLE);
                                rl_recording_section.setVisibility(View.VISIBLE);
                                tv_activeParticipantName.setText(participantName);
                            }
                        });

                        if (participantsAdapter != null && !isMyViewActive) {
                            participantsAdapter.refreshParticipants(mActiveParticipantIndex);
                        }

                        break;
                    }
                }
            }
        }
    }

    private void addrevRenderedVideo(TechrevRemoteParticipant participant) {
        Log.d(TAG , "Thread Name in addrevRenderedVideo: "+Thread.currentThread().getName());
        // Execute the AsyncTask to move heavy operations to a background thread
        new AddRenderedVideoTask(participant).execute();
    }

    private class RemoveRenderedVideoTask extends AsyncTask<Void, Void, Void> {

        private TechrevRemoteParticipant participant;

        public RemoveRenderedVideoTask(TechrevRemoteParticipant participant) {
            this.participant = participant;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            removeVideoTracks(participant);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Update UI components on the main thread if needed
        }
    }

    private void removeVideoTracks(TechrevRemoteParticipant participant) {
        Log.d(TAG , "Thread Name in removeVideoTracks: "+Thread.currentThread().getName());
        if (participant.remoteParticipant.getIdentity().equalsIgnoreCase(selectedRemoteParticipant.remoteParticipant.getIdentity())) {
            for (int index = 0; index < selectedRemoteParticipant.remoteParticipant.getVideoTracks().size(); index++) {
                VideoTrack remoteVideoTrack = (VideoTrack) selectedRemoteParticipant.remoteParticipant.getVideoTracks().get(index).getVideoTrack();

                if (remoteVideoTrack != null) {
                    String sName = remoteVideoTrack.getName();
                    Log.d("====removeRenderedVideo", sName);
                    if (primaryVideoView != null && selectedRemoteParticipant.isTechRevRemoveMyself()) {
                        remoteVideoTrack.removeRenderer(primaryVideoView);
                        primaryVideoView = null;
                    } else if (primaryVideoView != null && !selectedRemoteParticipant.isTechRevScreenEnable() && selectedRemoteParticipant.isTechRevScreenSelected() && "screen".equals(remoteVideoTrack.getName())) {
                        remoteVideoTrack.removeRenderer(primaryVideoView);
                        primaryVideoView = null;
                    } else if (primaryVideoView != null && !selectedRemoteParticipant.isTechRevScreenSelected() && !"screen".equals(remoteVideoTrack.getName())) {
                        remoteVideoTrack.removeRenderer(primaryVideoView);
                        primaryVideoView = null;
                    }
                }
            }

            if (selectedRemoteParticipant.isTechRevScreenSelected()) {
                if (selectedRemoteParticipant.getTechRevVideoEnable()) {
                    selectedRemoteParticipant.setTechRevScreenSelected(false);
                    addrevRenderedVideo(selectedRemoteParticipant);
                } else if (!selectedRemoteParticipant.isTechRevScreenEnable()) {
                    selectedRemoteParticipant.setTechRevScreenSelected(false);
                    //checkAndDisplayName();
                }
            } else {
                //checkAndDisplayName();
            }
        }
    }

    private void removeRenderedVideo(TechrevRemoteParticipant participant) {
        Log.d(TAG , "Thread Name in removeRenderedVideo: "+Thread.currentThread().getName());
        // Execute the AsyncTask to move heavy operations to a background thread
        new RemoveRenderedVideoTask(participant).execute();
    }

    private class ShowScreenTask extends AsyncTask<Void, Void, Boolean> {

        private TechrevRemoteParticipant participant;

        public ShowScreenTask(TechrevRemoteParticipant participant) {
            this.participant = participant;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return showScreen(participant);
        }

        @Override
        protected void onPostExecute(Boolean isScreenShown) {
            // Update UI components on the main thread if needed
            if (isScreenShown) {

            } else {
                showCameraTurnedOffAlert(participant);
            }
        }
    }

    private boolean showScreen(TechrevRemoteParticipant participant) {
        if (participant.remoteParticipant.getRemoteVideoTracks().size() > 0) {
            selectedRemoteParticipant = participant;
            selectedRemoteParticipant.setTechRevScreenSelected(true);
            for (int index = 0; index < participant.remoteParticipant.getVideoTracks().size(); index++) {
                VideoTrack remoteVideoTrack = (VideoTrack) participant.remoteParticipant.getVideoTracks().get(index).getVideoTrack();
                if (remoteVideoTrack != null && "screen".equals(remoteVideoTrack.getName())) {
                    removePrevRenderedVideo();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            VideoView videoView = new VideoView(VideoActivity.this);
                            videoView.setLayoutParams(layoutParamsPV);
                            videoView.setZOrderOnTop(false);
                            videoView.setZ(-1.0f);
                            videoView.setMirror(false);
                            video_view.addView(videoView);
                            primaryVideoView = videoView;
                            remoteVideoTrack.addRenderer(videoView);

                            isRemoteViewRendered = true;
                            participant_initial.setVisibility(View.GONE);
                            video_view.setVisibility(View.VISIBLE);
                            rl_recording_section.setVisibility(View.VISIBLE);
                        }
                    });
                    return true;
                }
            }
        }
        return false;
    }

    private void showCameraTurnedOffAlert(TechrevRemoteParticipant participant) {
        Log.d(TAG , "Thread Name in showCameraTurnedOffAlert: "+Thread.currentThread().getName());
        if (selectedRemoteParticipant != null && !selectedRemoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(participant.remoteParticipant.getIdentity())) {
            String s = participant.remoteParticipant.getIdentity();
            String nameOfParticipant = "";
            if (s != null && s.length() > 0) {
                try {
                    List<String> splitString = Arrays.asList(s.split("\\-"));
                    if (splitString.size() > 0) {
                        nameOfParticipant = splitString.get(1);
                    }
                } catch (Exception e) {
                    // Handle exception if needed
                }
            }

            if (!nameOfParticipant.equalsIgnoreCase("")) {
                nameOfParticipant = nameOfParticipant + "'s camera is turned off so please select another participant from the participant's list";
            } else {
                nameOfParticipant = "Selected participant's camera is turned off so please select another participant from the participant's list";
            }

            String finalNameOfParticipant = nameOfParticipant;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder alert = new AlertDialog.Builder(VideoActivity.this);
                    alert.setTitle("Alert");
                    alert.setMessage(finalNameOfParticipant);
                    alert.setPositiveButton("Ok", null);
                    alert.show();
                }
            });
        }
    }

    private void showScreenOfSelectedParticipant(TechrevRemoteParticipant participant) {
        Log.d(TAG , "Thread Name in showScreenOfSelectedParticipant: "+Thread.currentThread().getName());
        // Execute the AsyncTask to move heavy operations to a background thread
        new ShowScreenTask(participant).execute();
    }

    private class SetSelectedParticipantTask extends AsyncTask<TechrevRemoteParticipant, Void, Void> {

        @Override
        protected Void doInBackground(TechrevRemoteParticipant... participants) {
            for (TechrevRemoteParticipant participant : participants) {
                if (participant != null) {
                    processRemoteParticipant(participant);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Update UI components on the main thread if needed
        }

        private void processRemoteParticipant(TechrevRemoteParticipant participant) {
            Log.d(TAG , "Thread Name in processRemoteParticipant: "+Thread.currentThread().getName());
            if (participant.remoteParticipant.getRemoteVideoTracks().size() > 0) {
                RemoteVideoTrackPublication remoteVideoTrackPublication =
                        participant.remoteParticipant.getRemoteVideoTracks().get(0);

                /*
                 * Only remove video tracks that are subscribed to
                 */
                if (remoteVideoTrackPublication.isTrackSubscribed() && primaryVideoView != null) {
                    remoteVideoTrackPublication.getRemoteVideoTrack().removeRenderer(primaryVideoView);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            video_view.removeAllViews();
                        }
                    });
                }

                if (remoteVideoTrackPublication.isTrackSubscribed()) {
                    VideoTrack remoteVideoTrack = (VideoTrack) participant.remoteParticipant.getVideoTracks().get(0).getVideoTrack();
                    if (remoteVideoTrack != null) {
                        if (remoteVideoTrack.getName().equals("screen")) {
                            // Handle screen sharing case
                            // TODO: Add your screen sharing handling logic
                            onclickInterface.onClickVideo(0, "");
                        } else {
                            // Handle regular video track
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    VideoView videoView = new VideoView(VideoActivity.this);
                                    videoView.setZOrderOnTop(false);
                                    videoView.setMirror(false);
                                    video_view.addView(videoView);
                                    primaryVideoView = videoView;
                                    remoteVideoTrack.addRenderer(videoView);

                                    isRemoteViewRendered = true;
                                    participant_initial.setVisibility(View.GONE);
                                    video_view.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private void setSelectedRemoteParticipant(TechrevRemoteParticipant participant) {
        Log.d(TAG , "Thread Name in setSelectedRemoteParticipant: "+Thread.currentThread().getName());
        // Execute the AsyncTask to move heavy operations to a background thread
        new SetSelectedParticipantTask().execute(participant);
    }

    /*
     * Called when remote participant leaves the room
     */
    private class RemoveRemoteParticipantTask extends AsyncTask<RemoteParticipant, Void, Void> {

        @Override
        protected Void doInBackground(RemoteParticipant... participants) {
            for (RemoteParticipant remoteParticipant : participants) {
                processRemoteParticipant(remoteParticipant);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Update UI components on the main thread if needed
        }

        private void processRemoteParticipant(RemoteParticipant remoteParticipant) {
            Log.d(TAG , "Thread Name in processRemoteParticipant: "+Thread.currentThread().getName());
            for (int i = 0; i < remoteParticipantList.size(); i++) {
                TechrevRemoteParticipant participant = (TechrevRemoteParticipant) remoteParticipantList.get(i);
                if (remoteParticipant.getIdentity().equalsIgnoreCase(participant.remoteParticipant.getIdentity())) {
                    remoteParticipantList.remove(i);
                    notifytoOhterParticipants();
                    break;
                }
            }

            if (selectedRemoteParticipant != null && selectedRemoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(remoteParticipant.getIdentity())) {
                if (remoteParticipantList.size() > 0) {
                    selectedRemoteParticipant.setTechRevRemoveMyself(true);
                    removeRenderedVideo(selectedRemoteParticipant);
                    selectedRemoteParticipant = remoteParticipantList.get(0);
                    showHidePrimaryVideo(selectedRemoteParticipant);
                }
            }

            if (remoteParticipantList.isEmpty()) {
                showNootherparticipant();
            }

            if (member_Type == 1) {
                if (dialogViewHost != null && dialogViewHost.isShowing()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rVPadapter.notifyDataSetChanged();
                            if (remoteParticipantList.size() > 0) {
                                rViewPopup.setVisibility(View.VISIBLE);
                                emptyView.setVisibility(View.GONE);
                            } else {
                                rViewPopup.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void removeRemoteParticipant(RemoteParticipant remoteParticipant) {
        Log.d(TAG , "Thread Name in removeRemoteParticipant: "+Thread.currentThread().getName());
        // Execute the AsyncTask to move heavy operations to a background thread
        new RemoveRemoteParticipantTask().execute(remoteParticipant);
    }

    private class MoveVideoTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            // Perform heavy operations here
            String participantName = "";
            if (selectedRemoteParticipant != null && selectedRemoteParticipant.remoteParticipant != null) {
                String s = selectedRemoteParticipant.remoteParticipant.getIdentity();
                if (s.length() > 0) {
                    try {
                        List<String> splitString = Arrays.asList(s.split("\\-"));

                        if (splitString.size() > 0) {
                            participantName = splitString.get(1);
                        } else {
                            participantName = removePrefix(s, "participant-");
                            participantName = removePrefix(s, "hoster-");
                        }
                    } catch (Exception e) {
                        Log.d("====GetIdentity", "Exception:" + e.toString());
                    }
                }
            }
            return participantName;
        }

        @Override
        protected void onPostExecute(String participantName) {
            // Update UI components on the main thread
            updateUIComponents(participantName);
        }
    }

    private void updateUIComponents(String participantName) {
        Log.d(TAG , "Thread Name in updateUIComponents: "+Thread.currentThread().getName());
        try {
            Log.d(TAG , "Line 1");
            // Update UI components here
            card_view_thumbnailView.setVisibility(View.VISIBLE);
            Log.d(TAG , "Line 2");
            thumbnailVideoView.setVisibility(View.VISIBLE);
            Log.d(TAG , "Line 3");
            if (videoCallModel.getLocalVideoTrack() != null && thumbnailVideoView != null) {
                Log.d(TAG , "Line 4");
                videoCallModel.getLocalVideoTrack().removeRenderer(thumbnailVideoView);
                Log.d(TAG , "Line 5");
                videoCallModel.getLocalVideoTrack().addRenderer(thumbnailVideoView);
                Log.d(TAG , "Line 6");
            }
            Log.d(TAG , "Line 7");
            if (thumbnailVideoView != null) {
                Log.d(TAG , "Line 8");
                thumbnailVideoView.setMirror(false);
                Log.d(TAG , "Line 9");
            }
            Log.d(TAG , "Line 10");
            if (tvLocalParticipantName != null) {
                Log.d(TAG , "Line 11");
                tvLocalParticipantName.setText(participantName);
                Log.d(TAG , "Line 12");
            }
        } catch (Exception e) {
            Log.d(TAG , "Exception in updateUIComponents");
        }
    }

    private void moveLocalVideoToThumbnailView() {
        Log.d(TAG , "Thread Name in moveLocalVideoToThumbnailView: "+Thread.currentThread().getName());
        // Execute the AsyncTask to move heavy operations to a background thread
        new MoveVideoTask().execute();
    }

    private class ShowNoOtherParticipantTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return remoteParticipantList != null && !remoteParticipantList.isEmpty();
        }

        @Override
        protected void onPostExecute(Boolean hasParticipants) {
            // Update UI components on the main thread
            updateUIComponents(hasParticipants);
        }

        private void updateUIComponents(Boolean hasParticipants) {
            Log.d(TAG , "Thread Name in updateUIComponents hasParticipants: "+Thread.currentThread().getName());
            try {
                Log.d(TAG , "hasParticipants: "+hasParticipants);
                if (hasParticipants) {
                    Log.d(TAG , "hasParticipants: inside if");
                    nootherparticipant.setVisibility(View.GONE);
                    Log.d(TAG , "hasParticipants: before showHidePrimaryVideo");
                    showHidePrimaryVideo(selectedRemoteParticipant);
                    Log.d(TAG , "hasParticipants: after showHidePrimaryVideo");
                } else {
                    Log.d(TAG , "hasParticipants: inside else");
                    nootherparticipant.setVisibility(View.VISIBLE);
                    Log.d(TAG , "hasParticipants: inside else line 2");
                    video_view.setVisibility(View.GONE);
                    Log.d(TAG , "hasParticipants: inside else line 3");
                    participant_initial.setVisibility(View.GONE);
                    Log.d(TAG , "hasParticipants: inside else line 4");
                    selectedRemoteParticipant = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showNootherparticipant() {
        Log.d(TAG , "Thread Name in showNootherparticipant: "+Thread.currentThread().getName());
        // Execute the AsyncTask to move heavy operations to a background thread
        new ShowNoOtherParticipantTask().execute();
    }

    private class DisconnectClickListenerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return videoCallManager != null && isConnectedInRoom;
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            // Update UI components on the main thread
            handleDisconnectClick(isConnected);
        }

        private void handleDisconnectClick(Boolean isConnected) {
            Log.d(TAG , "Thread Name in handleDisconnectClick: "+Thread.currentThread().getName());
            /*
             * Disconnect from room
             */
            if (videoCallManager != null && isConnected) {
                videoCallManager.endForeground();
            } else {
                finish();
            }
        }
    }

    private View.OnClickListener disconnectClickListener() {
        // Return the click listener
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Execute the AsyncTask to move heavy operations to a background thread
                new DisconnectClickListenerTask().execute();
            }
        };
    }

    private class TurnOnOffSpeakerListenerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (isSpeakerOn) {
                isSpeakerOn = false;
            } else {
                isSpeakerOn = true;
            }
            return isSpeakerOn;
        }

        @Override
        protected void onPostExecute(Boolean isSpeakerOn) {
            // Update UI components on the main thread
            handleSpeakerToggle(isSpeakerOn);
        }

        private void handleSpeakerToggle(Boolean isSpeakerOn) {
            Log.d(TAG , "Thread Name in handleSpeakerToggle: "+Thread.currentThread().getName());
            if (isSpeakerOn) {
                isSpeakerOn = false;
                speakerActionFab.setBackgroundResource(R.drawable.white_circle_background);
                speakerActionFab.setColorFilter(getResources().getColor(R.color.color_primary));
                mAudioManager.setMode(AudioManager.MODE_IN_CALL);
                mAudioManager.setMode(AudioManager.MODE_NORMAL);
            } else {
                isSpeakerOn = true;
                speakerActionFab.setBackgroundResource(R.drawable.primary_circle_background);
                speakerActionFab.setColorFilter(getResources().getColor(R.color.white));
                mAudioManager.setMode(AudioManager.MODE_NORMAL);
                mAudioManager.setMode(AudioManager.MODE_IN_CALL);
            }
            mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        }
    }

    private View.OnClickListener turnOnOffSpeakerListener() {
        // Return the click listener
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Execute the AsyncTask to move heavy operations to a background thread
                new TurnOnOffSpeakerListenerTask().execute();
            }
        };
    }

    private class AddFileListenerTask extends AsyncTask<Void, Void, Intent> {

        @Override
        protected Intent doInBackground(Void... voids) {
            // Perform any background operations if needed
            Intent intent = new Intent(VideoActivity.this, MyCurrentUploadedDocumentsActivity.class);
            intent.putExtra("REQUEST_ID", requestID);
            intent.putExtra("AUTH_TOKEN", authToken);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("IS_REQUEST_CREATED_BY_CUSTOMER", IS_REQUEST_CREATED_BY_CUSTOMER);
            intent.putExtra("IS_PRIMARY_SIGNER", isPrimarySigner);
            return intent;
        }

        @Override
        protected void onPostExecute(Intent intent) {
            // Update UI components on the main thread
            if (intent != null) {
                startActivity(intent);
            }
        }
    }

    private View.OnClickListener addFileListener() {
        // Return the click listener
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Execute the AsyncTask to move heavy operations to a background thread
                new AddFileListenerTask().execute();
            }
        };
    }

    private class SwitchCameraClickListenerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // Perform any background operations if needed
            if (videoCallModel != null && isConnectedInRoom) {
                switchCamera();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Update UI components on the main thread if needed
        }

        private void switchCamera() {
            Log.d(TAG , "Thread Name in switchCamera: "+Thread.currentThread().getName());
            if (videoCallModel.getCameraCapturerCompat() != null) {
                if (isControlMenuClickable) {
                    CameraSource cameraSource = videoCallModel.getCameraCapturerCompat().getCameraSource();
                    videoCallModel.getCameraCapturerCompat().switchCamera();
                    videoCallModel.setCameraSource(cameraSource);
                    if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
                        if (cameraSource == CameraSource.BACK_CAMERA) {
                            CURRENT_CAMERA = 0;
                        } else {
                            CURRENT_CAMERA = 1;
                        }
                        Log.d(TAG, "CURRENT_CAMERA: " + CURRENT_CAMERA);
                        thumbnailVideoView.setMirror(cameraSource == CameraSource.BACK_CAMERA);
                    } else {
                        primaryVideoView.setMirror(cameraSource == CameraSource.BACK_CAMERA);
                    }
                }
            }
        }
    }

    private View.OnClickListener switchCameraClickListener() {
        // Return the click listener
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Execute the AsyncTask to move heavy operations to a background thread
                new SwitchCameraClickListenerTask().execute();
            }
        };
    }

    private class LocalVideoClickListenerTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            // Perform any background operations if needed
            int videoEnable = -1;
            if (videoCallModel != null && isConnectedInRoom) {
                if (videoCallModel.getLocalParticipant() != null) {
                    if (videoCallModel.getLocalVideoTrack() != null) {
                        if (videoCallModel.getLocalVideoTrack().isEnabled()) {
                            videoEnable = 1;
                        } else {
                            videoEnable = 0;
                        }
                    }
                }
            }
            return videoEnable;
        }

        @Override
        protected void onPostExecute(Integer videoEnable) {
            // Update UI components on the main thread
            handleLocalVideoClick(videoEnable);
        }

        private void handleLocalVideoClick(Integer videoEnable) {
            Log.d(TAG , "Thread Name in handleLocalVideoClick: "+Thread.currentThread().getName());
            if (videoEnable == 1) {
                try {
                    videoCallModel.getLocalVideoTrack().enable(false);
                    isDisableVideo = true;
                    int icon = getResourceID("camera_off", "drawable");
                    localVideoActionFab.setImageDrawable(ContextCompat.getDrawable(
                            VideoActivity.this, icon));

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("from", userMeetingIdentifier);
                    jsonObject.put("to", "All");
                    jsonObject.put("messageType", "CameraOff");
                    jsonObject.put("content", "");
                    videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                } catch (Exception e) {
                    Log.d("====Exception", "ProcessRequest " + e.toString());
                }
            } else if (videoEnable == 0) {
                videoCallModel.getLocalVideoTrack().enable(true);
                isDisableVideo = false;
                int icon = getResourceID("camera_on", "drawable");
                localVideoActionFab.setImageDrawable(ContextCompat.getDrawable(
                        VideoActivity.this, icon));
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("from", userMeetingIdentifier);
                    jsonObject.put("to", "All");
                    jsonObject.put("messageType", "CameraOn");
                    jsonObject.put("content", "");
                    videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                } catch (Exception e) {
                    Log.d("====Exception", "ProcessRequest " + e.toString());
                }
            }
        }
    }

    private View.OnClickListener localVideoClickListener() {
        // Return the click listener
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Execute the AsyncTask to move heavy operations to a background thread
                new LocalVideoClickListenerTask().execute();
            }
        };
    }

    private class MuteClickListenerTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            // Perform any background operations if needed
            int audioEnable = -1;
            if (videoCallModel != null && isConnectedInRoom) {
                if (videoCallModel.getLocalParticipant() != null) {
                    if (videoCallModel.getLocalAudioTrack() != null) {
                        if (videoCallModel.getLocalAudioTrack().isEnabled()) {
                            audioEnable = 1;
                        } else {
                            audioEnable = 0;
                        }
                    }
                }
            }
            return audioEnable;
        }

        @Override
        protected void onPostExecute(Integer audioEnable) {
            // Update UI components on the main thread
            handleMuteClick(audioEnable);
        }

        private void handleMuteClick(Integer audioEnable) {
            Log.d(TAG , "Thread Name in handleMuteClick: "+Thread.currentThread().getName());
            if (audioEnable == 1) {
                try {
                    videoCallModel.getLocalAudioTrack().enable(false);
                    isDisableAudio = true;
                    int icon = getResourceID("mic_off", "drawable");
                    muteActionFab.setImageDrawable(ContextCompat.getDrawable(
                            VideoActivity.this, icon));

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("from", userMeetingIdentifier);
                    jsonObject.put("to", "All");
                    jsonObject.put("messageType", "AudioMuted");
                    jsonObject.put("content", "");
                    videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                } catch (Exception e) {
                    Log.d("====Exception", "ProcessRequest " + e.toString());
                }
            } else if (audioEnable == 0) {
                videoCallModel.getLocalAudioTrack().enable(true);
                isDisableAudio = false;
                int icon = getResourceID("mic_on", "drawable");
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(
                        VideoActivity.this, icon));
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("from", userMeetingIdentifier);
                    jsonObject.put("to", "All");
                    jsonObject.put("messageType", "AudioUnMuted");
                    jsonObject.put("content", "");
                    videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                } catch (Exception e) {
                    Log.d("====Exception", "ProcessRequest " + e.toString());
                }
            }
        }
    }

    private View.OnClickListener muteClickListener() {
        // Return the click listener
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Execute the AsyncTask to move heavy operations to a background thread
                new MuteClickListenerTask().execute();
            }
        };
    }

    private class ChatClickListenerTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<VideoActivity> activityReference;

        ChatClickListenerTask(VideoActivity activity) {
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            VideoActivity activity = activityReference.get();
            if (activity != null) {
                try {
                    activity.getAllMessagesByMeetingId(activity.requestID, "New");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Update UI components on the main thread if needed
        }
    }

    private View.OnClickListener chatClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoActivity activity = (VideoActivity) view.getContext();
                new ChatClickListenerTask(activity).execute();
            }
        };
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllMessagesByMeetingId(String meeting_id, String req_type) {
        Log.d(TAG , "Thread Name in getAllMessagesByMeetingId: "+Thread.currentThread().getName());
        chatBadge.setVisibility(View.GONE);
        ProgressDialog dialog = new ProgressDialog(mActivity);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        Log.e(TAG, "Meeting ID: " + meeting_id);
        JSONObject obj = new JSONObject();
        try {
            obj.put("meetingId", meeting_id);
            String data = obj.toString();

            new AsyncTask<String, Void, ChatDataModel>() {

                @Override
                protected ChatDataModel doInBackground(String... params) {
                    String authToken = params[0];
                    String data = params[1];
                    Call<ChatDataModel> responseBodyCall = serviceLocal.getAllMessagesByMeetingId(authToken, data);

                    try {
                        Response<ChatDataModel> response = responseBodyCall.execute(); // Synchronous execution
                        return response.body();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(ChatDataModel response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    });
                    chatList = new ArrayList<>();

                    if (response != null) {
                        Log.e(TAG, "Chat Data: \n" + new Gson().toJson(response));
                        if (req_type.equals("New")) {
                            chatAlertDialog.setView(mView);
                        }

                        listView = mView.findViewById(R.id.chat_list_view);
                        TextView tv_no_data_found = mView.findViewById(R.id.no_data);
                        ImageView iv_go_back = mView.findViewById(R.id.back_btn);
                        ImageView iv_attach_file = mView.findViewById(R.id.attachment_action_fab);
                        ImageView iv_send_message = mView.findViewById(R.id.send_action_fab);
                        EditText et_message = mView.findViewById(R.id.message);

                        iv_go_back.setOnClickListener(view -> chatAlertDialog.dismiss());

                        iv_send_message.setOnClickListener(view -> {
                            try {
                                if (et_message.getText().toString().isEmpty()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mActivity, "Please enter a message", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    saveNewMessage(meeting_id, et_message.getText().toString(), userId, "Message");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });

                        if (response.getChatDataList().size() > 0) {
                            chatList.addAll(response.getChatDataList());
                            if (chatList.size() > 0) {
                                tv_no_data_found.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                                ChatViewAdapter adapter = new ChatViewAdapter(mActivity, chatList, userId, meeting_id, authToken);
                                listView.setAdapter(adapter);
                                listView.setSelection(adapter.getCount() - 1);
                                adapter.setMessageInterface(VideoActivity.this);
                            }
                        } else {
                            tv_no_data_found.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }

                        chatAlertDialog.show();
                    }
                }
            }.execute(authToken, data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void saveNewMessage(String meeting_id, String message, String userID, String req_type) throws JSONException {
        Log.d(TAG , "Thread Name in saveNewMessage: "+Thread.currentThread().getName());
        Log.e(TAG, "Meeting ID: " + meeting_id);
        JSONObject obj = new JSONObject();
        if (req_type.equals("Message")) {
            obj.put("meetingId", meeting_id);
            obj.put("message", message);
            obj.put("messageType", "1");
            obj.put("receiverId", "0");
            obj.put("receiverType", "0");
            obj.put("senderId", userID);
            obj.put("senderName", firstName + " " + lastName);
            obj.put("senderType", "0");
        } else {
            obj.put("meetingId", meeting_id);
            obj.put("docId", message);
            obj.put("messageType", "1");
            obj.put("receiverId", "0");
            obj.put("receiverType", "0");
            obj.put("senderId", userID);
            obj.put("senderName", firstName + " " + lastName);
            obj.put("senderType", "0");
        }
        String data = obj.toString();

        // ... (other parts of the method)

        new AsyncTask<String, Void, Response<ChatDataModel>>() {

            @Override
            protected Response<ChatDataModel> doInBackground(String... params) {
                String authToken = params[0];
                String data = params[1];
                Call<ChatDataModel> responseBodyCall = serviceLocal.saveNewMessage(authToken, data);

                try {
                    return responseBodyCall.execute(); // Synchronous execution
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Response<ChatDataModel> response) {
                if (chatAttachFileAlertDialog != null) {
                    chatAttachFileAlertDialog.dismiss();
                }

                if (response != null && response.isSuccessful()) {
                    ChatDataModel chatDataModel = response.body();
                    if (chatDataModel != null) {
                        // Access chatDataList from chatDataModel
                        List<ChatDataModel.ChatData> chatDataList = chatDataModel.getChatDataList();

                        if (mView != null) {
                            mView.findViewById(R.id.message).requestFocus();
                        }
                        hideSoftKeyboard(VideoActivity.this);
                        sendChatMessage("NewMessage", response.body().toString());
                        Log.e(TAG, "Chat Data: \n" + new Gson().toJson(response.body()));

                        // ... (rest of the logic)
                    }
                }
            }
        }.execute(authToken, data);

    }

    private void hideSoftKeyboard(Activity activity) {
        Log.d(TAG , "Thread Name in hideSoftKeyboard: "+Thread.currentThread().getName());
        View view = mView.findViewById(R.id.message);
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(view != null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void refreshChat() {
        Log.d(TAG , "Thread Name in refreshChat: "+Thread.currentThread().getName());
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    getAllMessagesByMeetingId(requestID, "Refresh");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Update UI components on the main thread if needed
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void attachFileToChat() {
        Log.d(TAG , "Thread Name in attachFileToChat: "+Thread.currentThread().getName());
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_file_chooser, null);
        dialogBuilder.setView(dialogView);

        ll_attach_file = dialogView.findViewById(R.id.attache_file);
        ll_send_file = dialogView.findViewById(R.id.send_file);

        ll_attach_file.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, FILE_PERMISSION_CODE);
            } else {
                chooseFile();
            }
        });

        ll_send_file.setOnClickListener(v -> new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    saveNewMessage(requestID, uploaded_chat_file_docID, userId, "Attachment");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Update UI components on the main thread if needed
            }
        }.execute());

        chatAttachFileAlertDialog = dialogBuilder.create();
        chatAttachFileAlertDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    private void chooseFile() {
        Log.d(TAG , "Thread Name in chooseFile: "+Thread.currentThread().getName());
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                String[] mimeTypes = {
                        "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip",
                        "image/*"
                };

                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    chooseFile.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                    if (mimeTypes.length > 0) {
                        chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    }
                } else {
                    String mimeTypesStr = "";
                    for (String mimeType : mimeTypes) {
                        mimeTypesStr += mimeType + "|";
                    }
                    chooseFile.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                }
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private View.OnClickListener shareScreenClickListener() {
        return v -> {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        Log.d("====ScreenTest", "IF");

                        // Newly Added
                        if (isShareScreenStarted) {
                            int icon = getResourceID("start_screen_share", "drawable");
                            shareScreenActionFab.setImageDrawable(ContextCompat.getDrawable(
                                    VideoActivity.this, icon));
                            isShareScreenStarted = false;
                            stopScreenCapture();
                        } else {
                            int icon = getResourceID("stop_screen_share", "drawable");
                            shareScreenActionFab.setImageDrawable(ContextCompat.getDrawable(
                                    VideoActivity.this, icon));
                            isShareScreenStarted = true;
                            startScreenCapture();
                        }
                    } catch (Exception e) {
                        Log.d("====ScreenShare", "Exception:" + e.toString());
                    }
                    return null;
                }
            }.execute();
        };
    }

    @SuppressLint("StaticFieldLeak")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void requestScreenCapturePermission() {
        Log.d(TAG , "Thread Name in requestScreenCapturePermission: "+Thread.currentThread().getName());
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Log.d(TAG, "Requesting permission to capture screen");
                MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                        getSystemService(Context.MEDIA_PROJECTION_SERVICE);

                // This initiates a prompt dialog for the user to confirm screen projection.
                startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),
                        REQUEST_MEDIA_PROJECTION);
                return null;
            }
        }.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_CODE_FOR_SIGNATURE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(CameraActivity.this, "Camera permission granted", Toast.LENGTH_LONG).show();
                captureImageThroughCamera("Signature");
            }
        }
        if (requestCode == REQUEST_CAMERA_CODE_FOR_INITIAL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(CameraActivity.this, "Camera permission granted", Toast.LENGTH_LONG).show();
                captureImageThroughCamera("Initial");
            }
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
                if (requestCode == REQUEST_MEDIA_PROJECTION) {
                    if (resultCode != AppCompatActivity.RESULT_OK) {
                        int icon = getResourceID("start_screen_share", "drawable");
                        shareScreenActionFab.setImageDrawable(ContextCompat.getDrawable(
                                VideoActivity.this, icon));
                        isShareScreenStarted = false;
                        stopScreenCapture();
                    }
                    screenCapturer = new ScreenCapturer(VideoActivity.this, resultCode, data, screenCapturerListener);
                    startScreenCapture();
                }
                if (resultCode == RESULT_OK) {
                    if (image_uri != null) {
                        Log.d("====Image URL", "Inside");
                        Log.d("====Image URL", "image_uri:" + image_uri.toString());
                        imageDialog(image_uri);
                    }
                }

                if (requestCode == PICKFILE_RESULT_CODE) {
                    Uri uri = data.getData();
                    String src = uri.getPath();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
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

                    Log.e(TAG, "Chosen file path: " + src);
                    Log.e(TAG, "Chosen file name: " + name);
                    Log.e(TAG, "Chosen File Mime Type: " + getMimeType(uri));
                    View myView = getLayoutInflater().inflate(R.layout.fragment_file_chooser, null);
                    TextView tv_file_name = myView.findViewById(R.id.file_name);
                    tv_file_name.setText(name);
                    uploadAttachedFile(bitmap, name, mime_type);
                }

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
                    Intent it = new Intent(mActivity, CameraActivity.class);
                    it.putExtra("REQUEST_ID" , requestID);
                    it.putExtra("AUTH_TOKEN" , authToken);
                    it.putExtra("USER_ID" , userId);
                    it.putExtra("CUSTOMER_TYPE" , customerType);
                    it.putExtra("TYPE" , "1");
                    it.putExtra("USER_MEETING_IDENTIFIER" , userMeetingIdentifier);
                    it.putExtra("bitmap", photo);
                    it.putExtra("resizedImageBytes", imageInBytes);
                    /*it.putExtra("VIDEO_CALL_MODEL_OBJ" , videoCallModel);*/
                    mActivity.startActivityForResult(it , SIGNATURE_INITIAL_CAPTURE_CODE);
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
                    Intent it = new Intent(mActivity, CameraActivity.class);
                    it.putExtra("REQUEST_ID" , requestID);
                    it.putExtra("AUTH_TOKEN" , authToken);
                    it.putExtra("USER_ID" , userId);
                    it.putExtra("CUSTOMER_TYPE" , customerType);
                    it.putExtra("TYPE" , "0");
                    it.putExtra("USER_MEETING_IDENTIFIER" , userMeetingIdentifier);
                    it.putExtra("bitmap", photo);
                    it.putExtra("resizedImageBytes", imageInBytes);
                    /*it.putExtra("VIDEO_CALL_MODEL_OBJ" , videoCallModel);*/
                    mActivity.startActivityForResult(it , SIGNATURE_INITIAL_CAPTURE_CODE);
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
    public void uploadAttachedFile(Bitmap bitmap, String file_name, String mime_type) {
        ProgressDialog dialog = new ProgressDialog(mActivity);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        new AsyncTask<Object, Void, Void>() {

            @Override
            protected Void doInBackground(Object... params) {
                @SuppressLint("StaticFieldLeak") String pathDetails = saveToInternalStorageFotAttachment(bitmap, file_name);
                String name = mypath.getName().replace("jpg", mime_type.split("/")[1]);
                Log.e("===pathDetails", "" + pathDetails);
                Log.e("===pathDetails", "Image Path:" + mypath);
                Log.e("===pathDetails", "Image Name:" + name);
                Log.e("===pathDetails", "Image Mime Type: " + mime_type);
                String extension = mypath.getName().split("\\.")[1];

                RequestBody requestBody1 = RequestBody.create(MediaType.parse(mime_type), mypath);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", name, requestBody1);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), name);

                Call<AttachedFileUploadResponseModel> call = serviceLocal.uploadAttachedFile(authToken, fileToUpload, filename, name, false, userId);
                call.enqueue(new Callback<AttachedFileUploadResponseModel>() {
                    @Override
                    public void onResponse(Call<AttachedFileUploadResponseModel> call, Response<AttachedFileUploadResponseModel> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        });
                        Log.d("====Inside1", "Success");
                        Log.d("====Inside1", "Response:" + response.code());
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                Log.d(TAG, "UPLOADED DOC ID:" + response.body().getDocId());
                                uploaded_chat_file_docID = response.body().getDocId();
                                ll_attach_file.setVisibility(View.GONE);
                                ll_send_file.setVisibility(View.VISIBLE);
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
                return null;
            }
        }.execute();
    }

    private class GetMimeTypeTask extends AsyncTask<Uri, Void, String> {

        @Override
        protected String doInBackground(Uri... uris) {
            Uri uri = uris[0];
            String mimeType = "*/*";
            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                ContentResolver cr = getContentResolver();
                mimeType = cr.getType(uri);
            } else {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
            }
            return mimeType;
        }

        @Override
        protected void onPostExecute(String mimeType) {
            // Do something with the mimeType
            Log.d("MimeType", "Obtained MimeType: " + mimeType);
            // Call your method or perform any actions with the mimeType here
        }
    }

    private String getMimeType(Uri uri) {
        try {
            return new GetMimeTypeTask().execute(uri).get(); // Blocking call to get the result
        } catch (Exception e) {
            e.printStackTrace();
            return "*/*"; // Return a default value in case of an error
        }
    }

    @SuppressLint("StaticFieldLeak")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startScreenCapture() {
        Log.d(TAG , "Thread Name in startScreenCapture: "+Thread.currentThread().getName());
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                if (Build.VERSION.SDK_INT >= 29) {
                    screenCapturerManager.startForeground();
                }

                if (screenCapturer == null) {
                    requestScreenCapturePermission();
                } else {
                    screenVideoTrack = LocalVideoTrack.create(VideoActivity.this, true, screenCapturer, "screen");

                    if (videoCallModel != null) {
                        videoCallModel.getLocalParticipant().publishTrack(screenVideoTrack);
                    }
                }
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void stopScreenCapture() {
        Log.d(TAG , "Thread Name in stopScreenCapture: "+Thread.currentThread().getName());
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                if (Build.VERSION.SDK_INT >= 29) {
                    screenCapturerManager.endForeground();
                }

                if (screenVideoTrack != null) {
                    if (videoCallModel != null) {
                        videoCallModel.getLocalParticipant().unpublishTrack(screenVideoTrack);
                    }
                    //screenVideoTrack.removeRenderer(localVideoView);
                    screenVideoTrack.release();
                    screenVideoTrack = null;
                }
                return null;
            }
        }.execute();
    }


    @SuppressLint({"RestrictedApi" , "StaticFieldLeak"})
    void imageDialog(Uri image_uri) {
        new AsyncTask<Uri, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Uri... uris) {
                // Load the image bitmap in the background
                try {
                    return MediaStore.Images.Media.getBitmap(getContentResolver(), uris[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                // UI operations should be done in the onPostExecute method
                if (bitmap != null) {
                    // Assuming 'YourActivity' is the actual name of your activity class
                    VideoActivity.this.dialogImage = new Dialog(VideoActivity.this);
                    VideoActivity.this.dialogImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    VideoActivity.this.dialogImage.setContentView(R.layout.dialog_image_popup);
                    VideoActivity.this.dialogImage.setCancelable(true);
                    ImageView imageView = VideoActivity.this.dialogImage.findViewById(R.id.popimageView);
                    TextView textView = VideoActivity.this.dialogImage.findViewById(R.id.title);

                    imageView.setImageBitmap(bitmap);
                    textView.setText("Captured Image");

                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add("Profile Picture");
                    arrayList.add("ID Proof Front");
                    arrayList.add("ID Proof Back");
                    arrayList.add("Custom");

                    Spinner checkInProviders = VideoActivity.this.dialogImage.findViewById(R.id.spinner);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(VideoActivity.this,
                            android.R.layout.simple_spinner_item, arrayList);

                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    checkInProviders.setAdapter(dataAdapter);
                    VideoActivity.this.dialogImage.show();
                }
            }
        }.execute(image_uri);
    }

    //Newly Added for Image capture Starts

    @SuppressLint("StaticFieldLeak")
    private void takePicture() {
        Log.d(TAG , "Thread Name in takePicture: "+Thread.currentThread().getName());
        new AsyncTask<Void, Void, Void>() {
            private WeakReference<VideoActivity> activityReference;

            @Override
            protected void onPreExecute() {
                // Store a weak reference to the activity
                activityReference = new WeakReference<>(VideoActivity.this);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                // Check if the activity still exists
                VideoActivity activity = activityReference.get();
                if (activity == null || activity.isFinishing()) {
                    // Activity is no longer valid, do not proceed
                    return null;
                }

                // Perform background operations here
                if (activity.videoCallModel != null) {
                    activity.videoCallModel.getCameraCapturerCompat().getCamera1().takePicture(activity.photographer);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Perform UI operations here, if needed
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDoneCapturingAllPhotos(TreeMap<String, byte[]> picturesTaken) {
        new AsyncTask<TreeMap<String, byte[]>, Void, Void>() {
            private WeakReference<VideoActivity> activityReference;

            @Override
            protected void onPreExecute() {
                // Store a weak reference to the activity
                activityReference = new WeakReference<>(VideoActivity.this);
            }

            @Override
            protected Void doInBackground(TreeMap<String, byte[]>... params) {
                // Check if the activity still exists
                VideoActivity activity = activityReference.get();
                if (activity == null || activity.isFinishing()) {
                    // Activity is no longer valid, do not proceed
                    return null;
                }

                TreeMap<String, byte[]> picturesTaken = params[0];

                if (picturesTaken != null && !picturesTaken.isEmpty()) {
                    picturesTaken.forEach((pictureUrl, pictureData) -> {
                        //convert the byte array 'pictureData' to a bitmap
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
                        // Perform any additional processing or UI updates here
                    });
                    activity.runOnUiThread(() -> {
                        Toast.makeText(activity, "Done capturing all photos!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    activity.runOnUiThread(() -> {
                        Toast.makeText(activity, "No camera detected!", Toast.LENGTH_SHORT).show();
                    });
                }
                return null;
            }
        }.execute(picturesTaken);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCaptureDone(String pictureUrl, byte[] pictureData) {
        new AsyncTask<byte[], Void, Bitmap>() {
            private WeakReference<VideoActivity> activityReference;

            @Override
            protected void onPreExecute() {
                // Store a weak reference to the activity
                activityReference = new WeakReference<>(VideoActivity.this);
            }

            @Override
            protected Bitmap doInBackground(byte[]... params) {
                // Check if the activity still exists
                VideoActivity activity = activityReference.get();
                if (activity == null || activity.isFinishing()) {
                    // Activity is no longer valid, do not proceed
                    return null;
                }

                byte[] pictureData = params[0];
                Bitmap bitmapOrg = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);

                if (bitmapOrg != null) {
                    int width = bitmapOrg.getWidth();
                    int height = bitmapOrg.getHeight();
                    int newWidth = 200;
                    int newHeight = 200;

                    // calculate the scale - in this case = 0.4f
                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;

                    // create a matrix for manipulation
                    Matrix matrix = new Matrix();
                    // resize the bitmap
                    matrix.postScale(scaleWidth, scaleHeight);
                    // rotate the Bitmap
                    matrix.postRotate(180);

                    // recreate the new Bitmap
                    return Bitmap.createBitmap(bitmapOrg, 0, 0, newWidth, newHeight, matrix, true);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap resizedBitmap) {
                // Check if the activity still exists
                VideoActivity activity = activityReference.get();
                if (activity == null || activity.isFinishing()) {
                    // Activity is no longer valid, do not proceed
                    return;
                }

                if (resizedBitmap != null) {
                    // Perform any additional processing or UI updates here
                    activity.uploadImageFile(resizedBitmap);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "take_picture_failed", Toast.LENGTH_LONG).show();
                        }
                    });
                    alertDialog1.dismiss();
                }
            }
        }.execute(pictureData);
    }

    @SuppressLint("NewApi")
    private int findFrontFacingCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIds = cameraManager.getCameraIdList();
            for (String cameraId : cameraIds) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    Log.d(TAG, "Front-facing camera found");
                    // Convert the camera ID from String to int
                    return Integer.parseInt(cameraId);
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return -1; // or any default value indicating not found
    }

    @SuppressLint("StaticFieldLeak")
    private final CameraCapturer.PictureListener photographer =
            new CameraCapturer.PictureListener() {
                @Override
                public void onShutter() {
                }

                @Override
                public void onPictureTaken(byte[] bytes) {
                    new AsyncTask<byte[], Void, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(byte[]... params) {
                            byte[] data = params[0];
                            Bitmap bitmapOrg = BitmapFactory.decodeByteArray(data, 0, data.length);
                            Bitmap finBitmap = null;

                            if (CURRENT_CAMERA == 1) {
                                int width = bitmapOrg.getWidth();
                                int height = bitmapOrg.getHeight();
                                int newWidth = width;
                                int newHeight = height;

                                // calculate the scale - in this case = 0.4f
                                float scaleWidth = ((float) newWidth) / width;
                                float scaleHeight = ((float) newHeight) / height;

                                // create a matrix for the manipulation
                                Matrix matrix = new Matrix();
                                // resize the bit map
                                matrix.postScale(scaleWidth, scaleHeight);
                                // rotate the Bitmap
                                matrix.postRotate(360);

                                // recreate the new Bitmap
                                finBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
                                        newWidth, newHeight, matrix, true);
                            } else {
                                Matrix m = new Matrix();
                                m.preScale(-1, 1);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                Bitmap dst = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
                                dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
                                finBitmap = dst;
                            }

                            return finBitmap;
                        }

                        @Override
                        protected void onPostExecute(Bitmap finBitmap) {
                            if (finBitmap != null) {
                                // Call your method to upload the image file
                                uploadImageFile(finBitmap);
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(VideoActivity.this,
                                                "take_picture_failed",
                                                Toast.LENGTH_LONG).show();
                                        alertDialog1.dismiss();
                                    }
                                });
                            }
                        }
                    }.execute(bytes);
                }
            };

    @SuppressLint("StaticFieldLeak")
    private void uploadPicture(UploadImageModel uploadModel) {
        Log.d(TAG , "Thread Name in uploadPicture: "+Thread.currentThread().getName());
        new AsyncTask<UploadImageModel, Void, String>() {
            @Override
            protected String doInBackground(UploadImageModel... params) {
                // Perform the upload operation in the background
                // You can use the provided UploadImageModel parameter to access the necessary data
                // Replace the code below with your actual upload logic

                if (videoCallModel != null && bankerdModel != null) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("from", userMeetingIdentifier);
                        jsonObject.put("to", bankerdModel.getFrom());

                        if (bankerdModel.getMessageType().equalsIgnoreCase("CaptureSignatureImageFromCustomer")) {
                            jsonObject.put("messageType", "uploadedSignatureImage");
                            JSONObject contentJson = new JSONObject();
                            contentJson.put("requestParticipantId", reqeustParticipantId);
                            contentJson.put("uploadedDocId", uploadModel.getUploadedDocId());
                            contentJson.put("userId", userId);
                            contentJson.put("requestId", requestID);
                            jsonObject.put("content", contentJson);
                        } else if (bankerdModel.getMessageType().equalsIgnoreCase("CaptureInitialImageFromCustomer")) {
                            jsonObject.put("messageType", "uploadedInitialImage");
                            JSONObject contentJson = new JSONObject();
                            contentJson.put("requestParticipantId", reqeustParticipantId);
                            contentJson.put("uploadedDocId", uploadModel.getUploadedDocId());
                            contentJson.put("userId", userId);
                            contentJson.put("requestId", requestID);
                            jsonObject.put("content", contentJson);
                        } else {
                            jsonObject.put("messageType", "uploadedImage");
                            jsonObject.put("content", uploadModel.getUploadedDocId());
                        }

                        Log.d(TAG, "Capture Image Data Track: " + new Gson().toJson(jsonObject));
                        return jsonObject.toString();
                    } catch (Exception e) {
                        Log.e("uploadPicture() ", e.getMessage() + "");
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null && videoCallModel != null) {
                    // Send the result to the main thread or perform any UI-related operation
                    // In this case, you can send the result directly using your existing method
                    videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(result);
                }
            }
        }.execute(uploadModel);
    }

    @SuppressLint("StaticFieldLeak")
    private void showPicture(DataModel dataModel) {
        Log.d(TAG , "Thread Name in showPicture: "+Thread.currentThread().getName());
        Log.d("===Inside", "ProcessRequest showPicture");

        // Download the picture based on datamodel.content; this will get DocID
        String documentID = dataModel.getContent();
        Log.d("===Inside", "ProcessRequest documentID:" + documentID);

        if (documentID != null && documentID.length() > 0) {
            Log.d("===Inside", "ProcessRequest documentID"); //
            Log.d("===InsideImageDownload", "authToken:" + authToken);
            Log.d("===InsideImageDownload", "documentID:" + documentID);

            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    try {
                        Call<ResponseBody> requestDownload = serviceLocal.downloadImage(authToken, documentID);
                        Response<ResponseBody> response = requestDownload.execute();

                        if (response.isSuccessful() && response.body() != null) {
                            InputStream input = response.body().byteStream();
                            return BitmapFactory.decodeStream(input);
                        }
                    } catch (IOException e) {
                        Log.e("===InsideImageDownload", "Error downloading image: " + e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (bitmap != null) {
                        AddImageDialog(bitmap, documentID);
                        Log.d("===InsideImageDownload", "ProcessRequest Done");
                    } else {
                        if (alertDialog2 != null && alertDialog2.isShowing()) {
                            alertDialog2.dismiss();
                            alertDialog2 = null;
                        }

                        runOnUiThread(() -> {
                            AlertDialog.Builder alert = new AlertDialog.Builder(VideoActivity.this);
                            alert.setTitle("Alert");
                            alert.setMessage("Unable to download captured image from the server, please try again later.");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        });
                    }
                }
            }.execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void openScreenshot(final File imageFile) {
        Log.d(TAG , "Thread Name in openScreenshot: "+Thread.currentThread().getName());
        new AsyncTask<Void, Void, Uri>() {
            @Override
            protected Uri doInBackground(Void... params) {
                return Uri.fromFile(imageFile);
            }

            @Override
            protected void onPostExecute(Uri uri) {
                if (uri != null) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "image/*");
                    startActivity(intent);
                } else {
                    // Handle error or show a toast
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void addCameraVideo(final DataModel dataModel) {
        Log.d(TAG , "Thread Name in addCameraVideo: "+Thread.currentThread().getName());
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                // Perform background tasks (e.g., taking a picture)
                bankerdModel = dataModel;
                takePicture();

                // Process and return the Bitmap if needed
                // Example: return processBitmap();
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                // Update UI or perform post-processing tasks
                // Example: showPicture(bitmap);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void dateTimeFormation(String utcDateTime, DateTimeFormatCallback callback) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                if (params.length > 0) {
                    String utcDateTime = params[0];
                    return performFormatting(utcDateTime);
                }
                return null;
            }

            @Override
            protected void onPostExecute(String formattedDateTime) {
                if (callback != null) {
                    callback.onFormatComplete(formattedDateTime);
                }
            }

            private String performFormatting(String utcDateTime) {
                String endDateTime = "";
                String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    Date date = simpleDateFormat.parse(utcDateTime);
                    SimpleDateFormat simpleDateFormatHeader = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
                    endDateTime = simpleDateFormatHeader.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return endDateTime;
            }
        }.execute(utcDateTime);
    }

    // ... other methods ...

    interface DateTimeFormatCallback {
        void onFormatComplete(String formattedDateTime);
    }

    @SuppressLint("StaticFieldLeak")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void processRequest(DataModel dModel) throws JSONException {
        Log.d(TAG , "Thread Name in processRequest: "+Thread.currentThread().getName());
        Log.e(TAG, "Process request::: " + dModel);
        if (dModel != null) {
            Log.e(TAG, "Process request Type::: " + dModel.getMessageType());
            switch (dModel.getMessageType()) {
                case "uploadedImage":
                    Log.d("===Inside", "ProcessRequest onMessageValue: uploadedImage ");
                    showPicture(dModel);
                    break;
                case "AudioMuted":
                    Log.d("===Inside", "ProcessRequest AudioMuted");
                    audioMuted(dModel);
                    break;
                case "AudioUnMuted":
                    Log.d("===Inside", "ProcessRequest AudioUnMuted");
                    audioUnMuted(dModel);
                    break;
                case "CameraOff":
                    CameraOff(dModel);
                    Log.d("===Inside", "ProcessRequest CameraOff");
                    break;
                case "CameraOn":
                    CameraOn(dModel);
                    Log.d("===Inside", "ProcessRequest CameraOn");
                    break;
                case "CaptureImageFromCustomer":
                case "CaptureSignatureImageFromCustomer":
                case "CaptureInitialImageFromCustomer":
                    Log.d("===Inside", "ProcessRequest onMessageValue: CaptureImageFromCustomer ");
                    alertDialog1 = new AlertDialog.Builder(VideoActivity.this).create();
                    alertDialog1.setTitle("Alert");
                    alertDialog1.setMessage("Capturing participant image is in progress.");
                    alertDialog1.setCancelable(false);
                    alertDialog1.show();
                    addCameraVideo(dModel);
                    break;
                case "LeaveFromRoom":
                case "NotaryCancelsRequest":
                case "NotaryEndCallOfCustomer":
                case "RemoveCustomersFromMobile":
                    // Handle LeaveFromRoom
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            if (member_Type == 2 && !leavingFromRoom) {
                                leavingFromRoom = true;
                            }
                            exitFromTheRoom();
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            // Handle UI updates after the background task
                        }
                    }.execute();
                    break;
                case "LocationSent":
                case "IP Address Captured":
                    break;
                case "MuteOrUnmuteAudio":
                    Log.e(TAG, "Inside MuteOrUnmuteAudio case");
                    muteUnmuteAudio(dModel);
                    break;
                case "SwitchOnOrOffVideo":
                    Log.e(TAG, "Inside SwitchOnOrOffVideo case");
                    SwitchOnOrOffVideo(dModel);
                    break;
                case "NewMessage":
                    // Handle NewMessage
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            if (chatAlertDialog != null && chatAlertDialog.isShowing()) {
                                try {
                                    updateNewMessageInChat(dModel);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                chatBadge.setVisibility(View.VISIBLE);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            // Handle UI updates after the background task
                        }
                    }.execute();
                    break;
                case "requestToAccessDoc":
                    getCustomerDisagreeCount(requestID);
                    break;
                case "RemoveMessage":
                case "NotifySignerToCaptureSignature":
                    showSignatureCaptureDialog();
                    break;
                case "NotifySignerToCaptureInitial":
                    showInitialCaptureDialog();
                    break;

                case "requestToReplaceSignature" :
                    showReplaceSignatureInitialDialog();
                    break;
            }
        }
    }

    private void showSignatureCaptureDialog() {
        //Toast.makeText(this, "Request to capture initial image", Toast.LENGTH_SHORT).show();
        // Show the capture signature dialog fragment
        CaptureSignerSignatureDialogFragment captureSignerSignatureDialogFragment = new CaptureSignerSignatureDialogFragment(mActivity, userMeetingIdentifier, videoCallModel, authToken, requestID, userId, customerType, new CaptureSignerSignatureDialogFragment.OptionSelectionInterface() {
            @Override
            public void onOptionSelected(int selectedOption) {
                //1 = Capture through camera & 2 = Draw through whiteboard
                if (selectedOption == 1) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    AlertDialog dialog = builder.setTitle("Capture Signature")
                            .setMessage("Are you sure, you want to capture your signature?")
                            .setCancelable(false)
                            .setPositiveButton("Ok, Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Toast.makeText(mActivity, "Opening camera", Toast.LENGTH_SHORT).show();
                                    if (ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission.CAMERA)
                                            == PackageManager.PERMISSION_DENIED) {
                                        captureImageThroughCamera("Signature");
                                    } else {
                                        ActivityCompat.requestPermissions(VideoActivity.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE_FOR_SIGNATURE);
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
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mActivity.getColor(R.color.color_primary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mActivity.getColor(R.color.red));
                    }

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    AlertDialog dialog = builder.setTitle("Draw Signature")
                            .setMessage("Are you sure, you want to draw your signature?")
                            .setCancelable(false)
                            .setPositiveButton("Ok, Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Toast.makeText(mActivity, "Opening whiteboard", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(mActivity, WhiteBoardActivity.class);
                                    it.putExtra("REQUEST_ID" , requestID);
                                    it.putExtra("AUTH_TOKEN" , authToken);
                                    it.putExtra("USER_ID" , userId);
                                    it.putExtra("CUSTOMER_TYPE" , customerType);
                                    it.putExtra("TYPE" , "1");
                                    it.putExtra("USER_MEETING_IDENTIFIER" , userMeetingIdentifier);
                                    /*it.putExtra("VIDEO_CALL_MODEL_OBJ" , videoCallModel);*/
                                    mActivity.startActivityForResult(it , SIGNATURE_INITIAL_CAPTURE_CODE);
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
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mActivity.getColor(R.color.color_primary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mActivity.getColor(R.color.red));
                    }

                }
            }
        });
        FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
        Fragment prev = mActivity.getFragmentManager().findFragmentByTag("signature_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        captureSignerSignatureDialogFragment.setCancelable(false);
        captureSignerSignatureDialogFragment.show(ft,"signature_dialog");
    }

    private void showInitialCaptureDialog() {
        //Toast.makeText(this, "Request to capture signature image", Toast.LENGTH_SHORT).show();
        // Show the capture initial dialog fragment
        CaptureSignerInitialDialogFragment captureSignerInitialDialogFragment = new CaptureSignerInitialDialogFragment(mActivity, userMeetingIdentifier, videoCallModel, authToken, requestID, userId, customerType, new CaptureSignerInitialDialogFragment.OptionSelectionInterface() {
            @Override
            public void onOptionSelected(int selectedOption) {
                //1 = Capture through camera & 2 = Draw through whiteboard
                if (selectedOption == 1) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Toast.makeText(mActivity, "Opening camera", Toast.LENGTH_SHORT).show();
                                    if (ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission.CAMERA)
                                            == PackageManager.PERMISSION_DENIED) {
                                        captureImageThroughCamera("Initial");
                                    } else {
                                        ActivityCompat.requestPermissions(VideoActivity.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE_FOR_INITIAL);
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    AlertDialog dialog = builder.setTitle("Capture Initial")
                            .setMessage("Are you sure, you want to capture your initial?")
                            .setPositiveButton("Ok, Proceed", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener)
                            .setCancelable(false)
                            .show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mActivity.getColor(R.color.color_primary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mActivity.getColor(R.color.red));
                    }

                } else {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Toast.makeText(mActivity, "Opening camera", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(mActivity, WhiteBoardActivity.class);
                                    it.putExtra("REQUEST_ID" , requestID);
                                    it.putExtra("AUTH_TOKEN" , authToken);
                                    it.putExtra("USER_ID" , userId);
                                    it.putExtra("CUSTOMER_TYPE" , customerType);
                                    it.putExtra("TYPE" , "0");
                                    it.putExtra("USER_MEETING_IDENTIFIER" , userMeetingIdentifier);
                                    /*it.putExtra("VIDEO_CALL_MODEL_OBJ" , videoCallModel);*/
                                    mActivity.startActivityForResult(it , SIGNATURE_INITIAL_CAPTURE_CODE);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    AlertDialog dialog = builder.setTitle("Draw Initial")
                            .setMessage("Are you sure, you want to draw your initial?")
                            .setPositiveButton("Ok, Proceed", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener)
                            .setCancelable(false)
                            .show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mActivity.getColor(R.color.color_primary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mActivity.getColor(R.color.red));
                    }

                }
            }
        });
        FragmentTransaction ft1 = mActivity.getFragmentManager().beginTransaction();
        Fragment prev1 = mActivity.getFragmentManager().findFragmentByTag("initial_dialog");
        if (prev1 != null) {
            ft1.remove(prev1);
        }
        ft1.addToBackStack(null);
        captureSignerInitialDialogFragment.setCancelable(false);
        captureSignerInitialDialogFragment.show(ft1,"initial_dialog");
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

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
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
                                // For Signature
                                NotarizationActionUpdateManger.updateNotarizationAction(
                                        VideoActivity.this, authToken,
                                        requestID, "", userId, customerType,
                                        finalSignatureActionID, "1", currentDocID);
                                // For Initial
                                NotarizationActionUpdateManger.updateNotarizationAction(
                                        VideoActivity.this, authToken,
                                        requestID, "", userId, customerType,
                                        finalInitialActionID, "1", currentDocID);

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
                                        VideoActivity.this, authToken,
                                        requestID, "", userId, customerType,
                                        finalDeniedActionID, "1", "");
                            } catch (Exception e) {
                                Log.d("====Exception", "" + e.toString());
                            }

                        }
                    })
                    .setCancelable(false)
                    .show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mActivity.getColor(R.color.color_primary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mActivity.getColor(R.color.red));
            }
        }
    }

    private void captureImageThroughCamera (String type) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (type.equalsIgnoreCase("Signature")) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_FOR_SIGNATURE);
        } else {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_FOR_INITIAL);
        }
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

    @SuppressLint("StaticFieldLeak")
    public void getCustomerDisagreeCount(String message_id) throws JSONException {
        Log.e(TAG, "Meeting ID: " + message_id);
        JSONObject obj = new JSONObject();
        obj.put("requestId", message_id);
        String data = obj.toString();

        new AsyncTask<Void, Void, CustomerDisagreeCountModel>() {
            @Override
            protected CustomerDisagreeCountModel doInBackground(Void... params) {
                Call<CustomerDisagreeCountModel> responseBodyCall = serviceLocal.getCustomerDisagreeCount(authToken, data);
                try {
                    Response<CustomerDisagreeCountModel> response = responseBodyCall.execute();
                    if (response.isSuccessful() && response.body() != null) {
                        // Handle the response on the UI thread
                        // Toast.makeText(VideoActivity.this, "Disagree count: " + response.body().getDisagreeCounts().get(0).getCustomerDisagreeCount(), Toast.LENGTH_SHORT).show();

                        if (IS_REQUEST_CREATED_BY_CUSTOMER) {
                            DOCUMENT_ACCESS_DISAGREE_COUNT = Integer.parseInt(response.body().getDisagreeCounts().get(0).getCustomerDisagreeCount());

                            if (DOCUMENT_ACCESS_DISAGREE_COUNT == 0) {
                                showDocumentAccessDialog("Document Access Required!", "By clicking 'I Agree', you agree to allow access of your uploaded document(s) to the Notary.");
                            } else {
                                SpannableStringBuilder builder = new SpannableStringBuilder();
                                String first = "By clicking 'I Agree', you agree to allow access of your uploaded document(s) to the Notary.\n\n";
                                String second = "Note: By clicking on 'I Disagree', the request will be cancelled automatically.";

                                SpannableString blackSpannable = new SpannableString(first);
                                blackSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, first.length(), 0);
                                builder.append(blackSpannable);

                                SpannableString redSpannable = new SpannableString(first);
                                redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, second.length(), 0);
                                builder.append(redSpannable);

                                showDocumentAccessConfirmationDialog("Document Access Required!", Html.fromHtml("<font color='#000000'>By clicking 'I Agree', you agree to allow access of your uploaded document(s) to the Notary.<br><br>    </font><font color='#FF0000'>Note: By clicking on 'I Disagree', the request will be cancelled automatically.</font>"));
                            }
                        } else {
                            try {
                                updateAgreeCount(requestID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.e("====IOException", e.toString());
                }
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateDisagreeCount(String message_id, int count) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressDialog dialog = new ProgressDialog(VideoActivity.this);
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();

                Log.e(TAG, "Meeting ID: " + message_id);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("requestId", message_id);
                    obj.put("customerDisagreeCount", count);
                    String data = obj.toString();

                    Call<CustomerDisagreeCountModel> responseBodyCall = serviceLocal.updateDisagreeCount(authToken, data);
                    responseBodyCall.enqueue(new Callback<CustomerDisagreeCountModel>() {
                        @Override
                        public void onResponse(Call<CustomerDisagreeCountModel> call, Response<CustomerDisagreeCountModel> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (response.isSuccessful() && response.body() != null) {
                                // Handle successful response
                                //Toast.makeText(VideoActivity.this, "Disagree count updated.", Toast.LENGTH_SHORT).show();
                                NotarizationActionUpdateManger.updateNotarizationAction(
                                        VideoActivity.this, authToken,
                                        requestID, "", userId, customerType,
                                        "51", "1", "");
                            } else {
                                // Handle unsuccessful response
                                Log.e("updateDisagreeCount", "Unsuccessful response");
                            }
                        }

                        @Override
                        public void onFailure(Call<CustomerDisagreeCountModel> call, Throwable t) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            // Handle network failure
                            Log.e("updateDisagreeCount", "Network call failed: " + t.toString());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void confirmDeleteRequest(String meeting_id) throws JSONException {
        Log.d(TAG , "Thread Name in sendUpdateToServer: "+Thread.currentThread().getName());
        ProgressDialog dialog = new ProgressDialog(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        Log.e(TAG, "Meeting ID: " + meeting_id);
        JSONObject obj = new JSONObject();
        obj.put("requestId", meeting_id);
        String data = obj.toString();

        Call<CosignerDetailsModel> responseBodyCall = serviceLocal.getCosignerDetailsByRequestId(authToken, data);
        responseBodyCall.enqueue(new Callback<CosignerDetailsModel>() {
            @Override
            public void onResponse(Call<CosignerDetailsModel> call, Response<CosignerDetailsModel> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                if (response.body() != null) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            JSONObject object = new JSONObject();
                            try {
                                object.put("RequestId", requestID);
                                object.put("RequestStatus", 2);

                                if (response.body().getCosigners().size() > 0) {
                                    object.put("CosignerList", response.body().getCosigners());
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            updateRequestStatusByCustomer(object);
                                        } catch (JSONException e) {
                                            Log.e(TAG ,e.toString());
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            public void onFailure(Call<CosignerDetailsModel> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.d("====onResponse", "Failure" + t.toString());
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void updateRequestStatusByCustomer(JSONObject obj) throws JSONException {
        Log.d(TAG , "Thread Name in updateRequestStatusByCustomer: "+Thread.currentThread().getName());
        ProgressDialog dialog = new ProgressDialog(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        Log.d(TAG, "Prepared Object: " + new Gson().toJson(obj));
        String data = obj.toString();

        Call<UpdateRequestStatusResponse> responseBodyCall = serviceLocal.updateRequestStatusByCustomer(authToken, data);
        responseBodyCall.enqueue(new Callback<UpdateRequestStatusResponse>() {
            @Override
            public void onResponse(Call<UpdateRequestStatusResponse> call, Response<UpdateRequestStatusResponse> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                if (response.body() != null) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            if (response.body().getStatus() == 1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            notifyServiceProvider();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(VideoActivity.this, "We are unable to cancel your request. Please contact support team.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            }
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            public void onFailure(Call<UpdateRequestStatusResponse> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.d("====onResponse", "Failure" + t.toString());
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void notifyServiceProvider() throws JSONException {
        Log.d(TAG , "Thread Name in notifyServiceProvider: "+Thread.currentThread().getName());
        ProgressDialog dialog = new ProgressDialog(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        JSONObject dataObject = new JSONObject();
        dataObject.put("userId", userId);
        dataObject.put("requestId", requestID);

        JSONObject finalObj = new JSONObject();
        finalObj.put("userId", userId);
        finalObj.put("topic", "serviceProviderRequestCancelled");
        finalObj.put("dataObject", dataObject);

        String data = finalObj.toString();

        Call<UpdateRequestStatusResponse> responseBodyCall = serviceLocal.notifyServiceProvider(authToken, data);
        responseBodyCall.enqueue(new Callback<UpdateRequestStatusResponse>() {
            @Override
            public void onResponse(Call<UpdateRequestStatusResponse> call, Response<UpdateRequestStatusResponse> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                if (response.body() != null) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            if (response.body().getStatus() == 1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(VideoActivity.this, "Your notarization request has been cancelled by you.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        exitFromTheRoom();
                                    }
                                });
                            } else if (response.body().getStatus() == 2) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(VideoActivity.this, "Sorry, the request is already cancelled.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        exitFromTheRoom();
                                    }
                                });
                            } else if (response.body().getStatus() == 3) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(VideoActivity.this, "Sorry, the request is already notarized.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        exitFromTheRoom();
                                    }
                                });
                            } else if (response.body().getStatus() == 4) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(VideoActivity.this, "Sorry, the request is already cancelled by notary.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        exitFromTheRoom();
                                    }
                                });
                            } else {
                                // Handle other cases or don't show any message
                            }
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            public void onFailure(Call<UpdateRequestStatusResponse> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.d("====onResponse", "Failure" + t.toString());
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void updateAgreeCount(String message_id) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Show loading indicator
                ProgressDialog dialog = new ProgressDialog(VideoActivity.this);
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();

                Log.e(TAG, "Meeting ID: " + message_id);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("RequestId", message_id);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String data = obj.toString();

                Call<CustomerDisagreeCountModel> responseBodyCall = serviceLocal.updateAgreeCount(authToken, data);
                responseBodyCall.enqueue(new Callback<CustomerDisagreeCountModel>() {
                    @Override
                    public void onResponse(Call<CustomerDisagreeCountModel> call, Response<CustomerDisagreeCountModel> response) {
                        // Dismiss loading indicator
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (response.isSuccessful() && response.body() != null) {
                            // Handle successful response
                            //Toast.makeText(VideoActivity.this, "Agree count updated.", Toast.LENGTH_SHORT).show();
                            NotarizationActionUpdateManger.updateNotarizationAction(
                                    VideoActivity.this, authToken,
                                    requestID, "", userId, customerType,
                                    "7", "1", "");
                        } else {
                            // Handle unsuccessful response
                            Log.d("onResponse", "Unsuccessful response");
                        }
                    }

                    @Override
                    public void onFailure(Call<CustomerDisagreeCountModel> call, Throwable t) {
                        // Dismiss loading indicator
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        // Handle network failure
                        Log.d("onFailure", "Network call failed: " + t.toString());
                    }
                });
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private void showDocumentAccessDialog(String title, String message) {
        Log.d(TAG , "Thread Name in showDocumentAccessDialog: "+Thread.currentThread().getName());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ProgressDialog agreeDialog = new ProgressDialog(VideoActivity.this);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.cancel();
                                agreeDialog.setMessage("Please wait");
                                agreeDialog.setCancelable(false);
                                agreeDialog.show();
                            }
                        });

                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                // Handle the "I Agree" action in the background
                                // For example, update UI or perform post-processing tasks
                                try {
                                    updateAgreeCount(requestID);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                // Update UI or perform post-processing tasks on the main thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (agreeDialog != null) {
                                            agreeDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }.execute();
                    }
                })
                .setNegativeButton("I Disagree", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ProgressDialog disagreeDialog = new ProgressDialog(VideoActivity.this);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.cancel();
                                disagreeDialog.setMessage("Please wait");
                                disagreeDialog.setCancelable(false);
                                disagreeDialog.show();
                            }
                        });

                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                // Handle the "I Disagree" action in the background
                                // For example, update UI or perform post-processing tasks
                                if (DOCUMENT_ACCESS_DISAGREE_COUNT == 0) {
                                    try {
                                        updateDisagreeCount(requestID, DOCUMENT_ACCESS_DISAGREE_COUNT++);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                confirmDeleteRequest(requestID);
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                // Update UI or perform post-processing tasks on the main thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (disagreeDialog != null) {
                                            disagreeDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }.execute();
                    }
                });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Creating dialog box
                AlertDialog alert = builder.create();
                alert.setTitle(title);
                alert.show();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void showDocumentAccessConfirmationDialog(String title, Spanned message) {
        Log.d(TAG , "Thread Name in showDocumentAccessConfirmationDialog: "+Thread.currentThread().getName());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ProgressDialog agreeDialog = new ProgressDialog(VideoActivity.this);
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               dialog.dismiss();
                               agreeDialog.setMessage("Please wait");
                               agreeDialog.setCancelable(false);
                               agreeDialog.show();
                           }
                       });

                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                // Handle the "I Agree" action in the background
                                // For example, update UI or perform post-processing tasks
                                try {
                                    updateAgreeCount(requestID);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                // Update UI or perform post-processing tasks on the main thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (agreeDialog != null) {
                                            agreeDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }.execute();
                    }
                })
                .setNegativeButton("I Disagree", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ProgressDialog disagreeDialog = new ProgressDialog(VideoActivity.this);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                disagreeDialog.setMessage("Please wait");
                                disagreeDialog.setCancelable(false);
                                disagreeDialog.show();
                            }
                        });

                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                // Handle the "I Disagree" action in the background
                                // For example, update UI or perform post-processing tasks
                                if (DOCUMENT_ACCESS_DISAGREE_COUNT == 0) {
                                    try {
                                        updateDisagreeCount(requestID, DOCUMENT_ACCESS_DISAGREE_COUNT++);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                confirmDeleteRequest(requestID);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                // Update UI or perform post-processing tasks on the main thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (disagreeDialog != null) {
                                            disagreeDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }.execute();
                    }
                });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Creating dialog box
                AlertDialog alert = builder.create();
                alert.setTitle(title);
                alert.show();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void updateNewMessageInChat(DataModel dModel) {
        Log.d(TAG , "Thread Name in updateNewMessageInChat: "+Thread.currentThread().getName());
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Handle the background tasks (e.g., updating new message in chat)
                try {
                    getAllMessagesByMeetingId(requestID, "Refresh");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // Update UI or perform post-processing tasks on the main thread
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }.execute();
    }

    private void CameraOff(DataModel dModel) {
        Log.d(TAG , "Thread Name in CameraOff: "+Thread.currentThread().getName());
        Log.d("====CameraOff", "ProcessRequest remoteParticipantList Size:" + remoteParticipantList.size());
        Log.d("====CameraOff", "ProcessRequest Called");
        for (int i = 0; i < remoteParticipantList.size(); i++) {
            Log.d("====CameraOff", "ProcessRequest Called inside for");
            TechrevRemoteParticipant participant = remoteParticipantList.get(i);
            if (participant.remoteParticipant.getIdentity().equalsIgnoreCase(dModel.getFrom())) {
                Log.d("====CameraOff", "ProcessRequest Called inside for IF");
                // If Equals Update Participate Adapter

                participant.setTechRevVideoEnable(false);

                int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        // participantsAdapter = new ParticipantsAdapter(remoteParticipantList, VideoActivity.this, onclickInterface);
                        // RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        // recyclerView.setLayoutManager(manager);
                        // recyclerView.setAdapter(participantsAdapter);
                        Log.d("===UI CameraOff", "ProcessRequest Called");
                        notifytoOhterParticipants();
                        if (selectedRemoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(dModel.getFrom())) {
                            removeRenderedVideo(selectedRemoteParticipant);
                        }
                    }
                });
            }
        }
    }

    private void CameraOn(DataModel dModel) {
        Log.d(TAG , "Thread Name in CameraOn: "+Thread.currentThread().getName());
        Log.d("====CameraOn", "ProcessRequest Called");
        for (int k = 0; k < remoteParticipantList.size(); k++) {
            Log.d("====CameraOn", "ProcessRequest Called inside for");
            TechrevRemoteParticipant techrevRemoteParticipant = remoteParticipantList.get(k);
            if (techrevRemoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(dModel.getFrom())) {
                Log.d("====CameraOn", "ProcessRequest Called inside for IF");
                // If Equals Update Participate Adapter

                Log.d("====CameraOn", "ProcessRequest remoteParticipantList Size:" + remoteParticipantList.size());
                techrevRemoteParticipant.setTechRevVideoEnable(true);

                int finalK = k;
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        // participantsAdapter = new ParticipantsAdapter(remoteParticipantList, VideoActivity.this, onclickInterface);
                        // RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        // recyclerView.setLayoutManager(manager);
                        // recyclerView.setAdapter(participantsAdapter);
                        Log.d("===UI CameraOn", "ProcessRequest CAlled");
                        notifytoOhterParticipants();
                        if (selectedRemoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(dModel.getFrom())) {
                            addrevRenderedVideo(selectedRemoteParticipant);
                        }
                    }
                });
            }
        }
    }

    private void audioUnMuted(DataModel dModel) {
        Log.d(TAG , "Thread Name in audioUnMuted: "+Thread.currentThread().getName());
        Log.d("====audioUnMuted", "ProcessRequest Called");
        for (int k = 0; k < remoteParticipantList.size(); k++) {
            Log.d("====audioUnMuted", "ProcessRequest Called inside for");

            if (remoteParticipantList.get(k).remoteParticipant.getIdentity().equalsIgnoreCase(dModel.getFrom())) {
                Log.d("====audioUnMuted", "ProcessRequest Called inside for IF");
                // If Equals Update Participate Adapter
                remoteParticipantList.get(k).setTechRevAudioEnable(true);
                Log.d("====audioUnMuted", "ProcessRequest remoteParticipantList Size:" + remoteParticipantList.size());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        // participantsAdapter = new ParticipantsAdapter(remoteParticipantList, VideoActivity.this, onclickInterface);
                        // RecyclerView.LayoutManager manager = new LinearLayoutManager(VideoActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        // recyclerView.setLayoutManager(manager);
                        // recyclerView.setAdapter(participantsAdapter);
                        Log.d("===UI audioUnMuted", "ProcessRequest CAlled");
                        notifytoOhterParticipants();
                    }
                });
            }
        }
    }

    private void audioMuted(DataModel dModel) {
        Log.d(TAG , "Thread Name in audioMuted: "+Thread.currentThread().getName());
        Log.d("====audioMuted", "ProcessRequest Called");
        for (int k = 0; k < remoteParticipantList.size(); k++) {
            Log.d("====audioMuted", "ProcessRequest Called inside for");
            TechrevRemoteParticipant remoteParticipant = remoteParticipantList.get(k);
            if (remoteParticipant.remoteParticipant.getIdentity().equalsIgnoreCase(dModel.getFrom())) {
                Log.d("====audioMuted", "ProcessRequest Called inside for IF");
                remoteParticipant.setTechRevAudioEnable(false);
                runOnUiThread(() -> {
                    notifytoOhterParticipants();
                });
            }
        }
    }

    private void SwitchOnOrOffVideo(DataModel dModel) {
        Log.d(TAG , "Thread Name in SwitchOnOrOffVideo: "+Thread.currentThread().getName());
        boolean msgval = dModel.getMessageValue();
        Log.d("===Inside", "ProcessRequest msgval:" + msgval);

        int icon = msgval ? getResourceID("camera_off", "drawable") : getResourceID("camera_on", "drawable");

        runOnUiThread(() -> {
            localVideoActionFab.setImageDrawable(ContextCompat.getDrawable(
                    VideoActivity.this, icon));
        });
    }

    private void muteUnmuteAudio(DataModel dModel) {
        Log.d(TAG , "Thread Name in muteUnmuteAudio: "+Thread.currentThread().getName());
        boolean msgval = dModel.getMessageValue();
        Log.d("===Inside", "ProcessRequest msgval:" + msgval);

        int icon = msgval ? getResourceID("mic_off", "drawable") : getResourceID("mic_on", "drawable");

        runOnUiThread(() -> {
            muteActionFab.setImageDrawable(ContextCompat.getDrawable(
                    VideoActivity.this, icon));
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBackPressed() {
        commonExit();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void commonExit() {
        // your code.
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        exitFromTheRoom();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity.this);
                AlertDialog dialog = builder.setMessage("Are you sure, you want to exit?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .setCancelable(false)
                        .show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.color_primary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.red));
            }
        });
    }

    private void exitFromTheRoom() {
        Log.d(TAG , "Thread Name in exitFromTheRoom: "+Thread.currentThread().getName());
        if (member_Type == 1) {
            // Newly Added start
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("from", userMeetingIdentifier);
                jsonObject.put("to", "All");
                jsonObject.put("messageType", "LeaveFromRoom");
                jsonObject.put("content", "Thank you for your time.");
                videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
            } catch (Exception e) {
                Log.d("====Exception", "" + e.toString());
            }
            // Newly Added End
        }

        // New data track added By Rupesh for notifying the web that the customer has left the call
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("from", userMeetingIdentifier);
            jsonObject.put("to", "All");
            jsonObject.put("messageType", "CustomerLeaveFromRoom");
            jsonObject.put("content", "Thank you for your time.");
            videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
        } catch (Exception e) {
            Log.d("====Exception", "" + e.toString());
        }
        // New data track added By Rupesh for notifying the web that the customer has left the call

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IS_AUTHORIZATION_DIALOG_SHOWN_ALREADY = false;
                IS_AUDIT_JOIN_CALL_SENT_ALREADY = false;
                disconnectClickListener();
                AddCoSignerActivity.getAddCoSignerActivityContext().exitFromTheRoom();
                VideoActivity.this.finish();
            }
        });

    }

    private void sendMessage() {
        Log.d(TAG , "Thread Name in sendMessage: "+Thread.currentThread().getName());
        // Display a progress dialog
        ProgressDialog dialog = new ProgressDialog(VideoActivity.this);
        dialog.setMessage("Please wait, Image capture is in progress.");
        dialog.setCancelable(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });

        // Perform network operation in the background
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (selectedRemoteParticipant != null && selectedRemoteParticipant.remoteParticipant.getRemoteVideoTracks().size() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("from", userMeetingIdentifier);
                        jsonObject.put("to", selectedRemoteParticipant.remoteParticipant.getIdentity());
                        jsonObject.put("messageType", "CaptureImageFromCustomer");
                        jsonObject.put("content", "");
                        videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                    } catch (Exception e) {
                        Log.d("====Exception", "" + e.toString());
                    }
                }

                // Dismiss the progress dialog on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    @SuppressLint("StaticFieldLeak")
    private void sendChatMessage(final String message_type, final String content) {
        Log.d(TAG , "Thread Name in sendChatMessage: "+Thread.currentThread().getName());
        new AsyncTask<Void, Void, Void>() {
            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                // Display a progress dialog
                dialog = new ProgressDialog(VideoActivity.this);
                dialog.setMessage("Sending chat message...");
                dialog.setCancelable(false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            protected Void doInBackground(Void... params) {
                if (videoCallModel != null) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("from", userMeetingIdentifier);
                        jsonObject.put("to", "All");
                        jsonObject.put("messageType", message_type);
                        jsonObject.put("content", content);
                        videoCallModel.getLocalDataTrackPublicationGlobal().getLocalDataTrack().send(jsonObject.toString());
                    } catch (Exception e) {
                        Log.d("====Exception", "" + e.toString());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // Dismiss the progress dialog
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public interface SaveCallback {
        void onDirectorySaved(String directoryPath);
    }

    private void saveToInternalStorage(Bitmap bitmapImage, SaveCallback callback) {
        Log.d(TAG , "Thread Name in saveToInternalStorage: "+Thread.currentThread().getName());
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Notify the callback with the directory path
        if (callback != null) {
            String directoryPath = directory.getAbsolutePath();
            callback.onDirectorySaved(directoryPath);
        }
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

    public void uploadImageFile(Bitmap bitmap) {
        final String[] pathDetails = new String[1];
        saveToInternalStorage(bitmap, new SaveCallback() {
            @Override
            public void onDirectorySaved(String directoryPath) {
                pathDetails[0] = directoryPath;
            }
        });
        Log.d("===pathDetails", "" + pathDetails[0]);
        Log.d("===pathDetails", "Image Path:" + mypath);
        Log.d("===pathDetails", "Image Name:" + mypath.getName());

        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), mypath);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", mypath.getName(), requestBody1);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), mypath.getName());
        Call<UploadImageModel> call = serviceLocal.uploadFile(authToken, fileToUpload, filename, "", false, requestID);
        call.enqueue(new Callback<UploadImageModel>() {
            @Override
            public void onResponse(Call<UploadImageModel> call, Response<UploadImageModel> response) {
                Log.d("====Inside1", "Success");
                Log.d("====Inside1", "Response:" + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.d("====Inside1", "getRequestId:" + response.body().getRequestId());
                        Log.d("====Inside1", "getUserId:" + response.body().getUserId());
                        Log.d("====Inside1", "getUploadedDocId:" + response.body().getUploadedDocId());
                        uploadPicture(response.body());
                        if(alertDialog1 != null && alertDialog1.isShowing()){
                            alertDialog1.dismiss();
                        }
                        if(alertDialog2 != null && alertDialog2.isShowing()){
                            alertDialog2.dismiss();
                        }

                    }
                } else {
                    if(alertDialog1 != null && alertDialog1.isShowing()){
                        alertDialog1.dismiss();
                    }
                    if(alertDialog2 != null && alertDialog2.isShowing()){
                        alertDialog2.dismiss();
                    }
                }
//                                     try {
//                                         Log.d("====Inside1","Response:"+response.body().string());
//                                     } catch (IOException e) {
//                                         e.printStackTrace();
//                                     }
            }

            @Override
            public void onFailure(Call<UploadImageModel> call, Throwable t) {
                Log.d("====Inside1", "Failure");
                Log.d("====Inside1", "Throwable:" + t.toString());
                if(alertDialog1 != null && alertDialog1.isShowing()){
                    alertDialog1.dismiss();
                }
                if(alertDialog2 != null && alertDialog2.isShowing()){
                    alertDialog2.dismiss();
                }

            }
        });

        // File Upload Work Ends


    }

    @SuppressLint("StaticFieldLeak")
    public void updateDocumentName(String fileName, String docName) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                String fileName = params[0];
                String docName = params[1];

                // Start Upload Document
                JSONObject json1 = new JSONObject();
                try {
                    json1.put("fileName", fileName);
                    json1.put("documentname", docName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonString1 = json1.toString();

                Call<ResponseBody> responseBodyCall = serviceLocal.updateDocumentName(authToken, jsonString1);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("====Inside", "Success");
                        Log.d("====Inside", "response:" + response.code());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VideoActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialogImage.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("====Inside", "Failure");
                        Log.d("====Inside", "Throwable:" + t.toString());
                        dialogImage.dismiss();
                    }
                });

                // End Upload Document
                return null;
            }
        }.execute(fileName, docName);
    }

    // Deleting the File Name
    @SuppressLint("StaticFieldLeak")
    public void deleteFileName(String fileName) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                String fileName = params[0];

                // Delete File Name Starts
                JSONObject json2 = new JSONObject();
                try {
                    json2.put("fileName", fileName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonString2 = json2.toString();
                Call<ResponseBody> responseBodyCall1 = serviceLocal.deleteFileName(authToken, jsonString2);
                responseBodyCall1.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("====Inside", "deleteFileName:Success");
                        Log.d("====Inside", "deleteFileName:response:" + response.code());
                        dialogImage.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("====Inside", "deleteFileName:Failure");
                        Log.d("====Inside", "deleteFileName:Throwable:" + t.toString());
                        dialogImage.dismiss();
                    }
                });
                // Delete File Name Ends
                return null;
            }
        }.execute(fileName);
    }

    @SuppressLint("StaticFieldLeak")
    private void AddImageDialog(Bitmap bitmap, String documentID) {
        Log.d(TAG , "Thread Name in AddImageDialog: "+Thread.currentThread().getName());
        Log.d("===Inside", "AddImageDialog");
        alertDialog2.dismiss();
        alertDialog2 = null;

        dialogImage = new Dialog(VideoActivity.this);
        dialogImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogImage.setContentView(R.layout.dialog_image_popup);
        dialogImage.setCancelable(true);

        LinearLayout linearLayout = (LinearLayout) dialogImage.findViewById(R.id.llPopup);
        ImageView imageView = (ImageView) dialogImage.findViewById(R.id.popimageView);
        TextView textView = (TextView) dialogImage.findViewById(R.id.title);
        Button btnSubmit = (Button) dialogImage.findViewById(R.id.btnSubmit);
        Button btnCancel = (Button) dialogImage.findViewById(R.id.btnCancel);
        EditText etCustomName = (EditText) dialogImage.findViewById(R.id.customName);

        imageView.setImageBitmap(flip(bitmap, FLIP_HORIZONTAL));
        textView.setText("Captured Image");

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Profile Picture");
        arrayList.add("ID Proof Front");
        arrayList.add("ID Proof Back");
        arrayList.add("Custom");

        Spinner checkInProviders = (Spinner) dialogImage.findViewById(R.id.spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VideoActivity.this,
                android.R.layout.simple_spinner_item, arrayList);

        checkInProviders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String strValue = checkInProviders.getItemAtPosition(i).toString();
                if (strValue.equals("Custom")) {
                    etCustomName.setVisibility(View.VISIBLE);
                } else {
                    etCustomName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checkInProviders.setAdapter(dataAdapter);
        dialogImage.show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("====Inside", "Submit");

                if (checkInProviders.getSelectedItem().toString().equals("Custom")) {
                    if (etCustomName.getText().toString().trim().length() > 0) {
                        // Perform background task to update document name
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                updateDocumentName(documentID, etCustomName.getText().toString());
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                // Update UI or perform any additional task after the background task completes
                            }
                        }.execute();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VideoActivity.this, "Enter custom document name", Toast.LENGTH_SHORT).show();
                            }
                        });
                        etCustomName.requestFocus();
                    }
                } else {
                    // Perform background task to update document name
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            updateDocumentName(documentID, checkInProviders.getSelectedItem().toString());
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            // Update UI or perform any additional task after the background task completes
                        }
                    }.execute();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("====Inside", "Cancel");
                // Perform background task to delete file name
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        deleteFileName(documentID);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        // Update UI or perform any additional task after the background task completes
                    }
                }.execute();
            }
        });
    }

    public static String removePrefix(String s, String prefix) throws ExecutionException, InterruptedException {
        if (s != null && prefix != null && s.startsWith(prefix)) {
            // Perform background task to remove prefix
            return new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    return s.substring(prefix.length());
                }

                @Override
                protected void onPostExecute(String result) {
                    // Update UI or perform any additional task after the background task completes
                }
            }.execute().get(); // This may cause ANR, consider using other asynchronous patterns
        }
        return s;
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        if (!isTryingLocationg) {
            isTryingLocationg = true;
            // Create the location request to start receiving updates
            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            //  mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

            // Create LocationSettingsRequest object using location request
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();

            // Check whether location settings are satisfied
            // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
            SettingsClient settingsClient = LocationServices.getSettingsClient(this);
            try {
                settingsClient.checkLocationSettings(locationSettingsRequest)
                        .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                            @Override
                            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                                // All location settings are satisfied. The client can initialize location requests here.
                                // ...
                                if (ActivityCompat.checkSelfPermission(
                                        VideoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                        ActivityCompat.checkSelfPermission(
                                                VideoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                                    return;
                                }

                                mLocationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        // do work here
                                        onLocationChanged(locationResult.getLastLocation());
                                    }
                                };
                                getFusedLocationProviderClient(VideoActivity.this).requestLocationUpdates(mLocationRequest, mLocationCallback,
                                        Looper.myLooper());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure, consider using a try-catch block to handle specific exceptions
                                // ...
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendLocationToServer(Location location) {
        /*Getting the current time to check if the location has been shared 30 mins before*/
        String currentDateAndTime = "";
        String lastLocationSharedDateAndTime = "";
        String lastLocationSharedRequestID = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
            currentDateAndTime = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault()).format(new Date());
            lastLocationSharedDateAndTime = sharedPreference.getString("LastLocationSharedTime");
            lastLocationSharedRequestID = sharedPreference.getString("LastLocationSharedRequestID");

            if (isLocationButtonClicked) {
                String finalCurrentDateAndTime2 = currentDateAndTime;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        shareLocation(location, finalCurrentDateAndTime2);
                    }
                }).start();
                return;
            }

            Log.d(TAG, "CURRENT REQUEST ID: " + requestID);
            Log.d(TAG, "STORED REQUEST ID: " + lastLocationSharedRequestID);
            Log.d(TAG, "CURRENT_DATE_AND_TIME: " + currentDateAndTime);
            Log.d(TAG, "LAST_LOCATION_SHARED_DATE_AND_TIME: " + lastLocationSharedDateAndTime);

            if (lastLocationSharedDateAndTime == null ||
                    lastLocationSharedDateAndTime.equalsIgnoreCase("NA") ||
                    lastLocationSharedDateAndTime.equalsIgnoreCase("") ||
                    lastLocationSharedRequestID == null ||
                    lastLocationSharedRequestID.equalsIgnoreCase("NA")
                    || lastLocationSharedRequestID.equalsIgnoreCase("")) {
                String finalCurrentDateAndTime = currentDateAndTime;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        shareLocation(location, finalCurrentDateAndTime);
                    }
                }).start();
            } else {
                long timeDifference = printDifference(simpleDateFormat.parse(lastLocationSharedDateAndTime), simpleDateFormat.parse(currentDateAndTime));
                long MAX_DURATION = TimeUnit.MILLISECONDS.convert(30, TimeUnit.MINUTES);
                Log.d(TAG, "MAX_DURATION: " + MAX_DURATION);
                Log.d(TAG, "Last location shared time difference: " + timeDifference);
                if (!requestID.equalsIgnoreCase(lastLocationSharedRequestID) || timeDifference >= MAX_DURATION) {
                    String finalCurrentDateAndTime1 = currentDateAndTime;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            shareLocation(location, finalCurrentDateAndTime1);
                        }
                    }).start();
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*Getting the current time to check if the location has been shared 30 mins before*/
    }

    @SuppressLint("StaticFieldLeak")
    private void shareLocation(Location location, String currentDateAndTime) {
        Log.d(TAG , "Thread Name in shareLocation: "+Thread.currentThread().getName());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String finalCurrentDateAndTime = currentDateAndTime;
                Log.d("====One", "" + requestID);
                Log.d("====Two", "" + userMeetingIdentifier);
                String pName = "";
                try {
                    List<String> splitString = Arrays.asList(userMeetingIdentifier.split("\\-"));
                    if (splitString.size() > 0) {
                        Log.d("====Three", "" + splitString.get(1));
                        pName = splitString.get(1);
                    }
                } catch (Exception e) {
                    Log.d("====Exception", "Exception:" + e.toString());
                }

                JSONObject json2 = new JSONObject();
                try {
                    WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

                    Log.d(TAG, "Latitude: " + location.getLatitude());
                    Log.d(TAG, "Longitude: " + location.getLongitude());
                    Log.d(TAG, "User ID: " + userId);
                    Log.d(TAG, "Request ID: " + requestID);
                    Log.d(TAG, "IP Address: " + ip);
                    if (location != null) {
                        json2.put("latitude", location.getLatitude());
                        json2.put("longitude", location.getLongitude());
                    } else {
                        json2.put("latitude", "");
                        json2.put("longitude", "");
                    }
                    json2.put("requestId", requestID);
                    json2.put("userId", userId);
                    json2.put("ipaddress", ip);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonString2 = json2.toString();
                Log.d("====jsonString2", "" + jsonString2);
                Log.d("====jsonString2", "authToken : " + authToken);

                Call<ResponseBody> responseBodyCall1 = serviceLocal.shareLocationData(authToken, jsonString2);
                responseBodyCall1.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("====jsonString2", "Success");
                        Log.d("====jsonString2", "Response:" + response.code());
                        sharedPreference.setString("LastLocationSharedTime", finalCurrentDateAndTime);
                        sharedPreference.setString("LastLocationSharedRequestID", requestID);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VideoActivity.this, "Your location has been shared successfully.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.d(TAG, "LOCATION SHARED SUCCESSFULLY!");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("====onResponse", "Failure" + t.toString());
                    }
                });

                return null;
            }
        }.execute();
    }

    //Added By Rupesh to find out the difference between two dates
    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public long printDifference (Date startDate, Date endDate){
        //milliseconds
        long difference = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + difference);

        /*long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);*/

        return difference;
    }
    //Added By Rupesh to find out the difference between two dates

    @SuppressLint("StaticFieldLeak")
    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.d(TAG, " onLocationChanged : " + msg);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                sendLocationToServer(location);
                if (mLocationRequest != null && mLocationCallback != null) {
                    try {
                        getFusedLocationProviderClient(VideoActivity.this).removeLocationUpdates(mLocationCallback);
                        mLocationCallback = null;
                        mLocationRequest = null;
                        isTryingLocationg = false;
                        Log.d(TAG, "StopLocation updates successful! ");
                    } catch (Exception exp) {
                        mLocationCallback = null;
                        mLocationRequest = null;
                        isTryingLocationg = false;
                        Log.d(TAG, " Security exception while removeLocationUpdates");
                    }
                }
                return null;
            }
        }.execute();
    }

    // Start for Flip Image

    public static Bitmap flip(Bitmap src, int type) {

        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if (type == FLIP_VERTICAL) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if (type == FLIP_HORIZONTAL) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    @SuppressLint("StaticFieldLeak")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startCountDown(String endStr) {
        Log.d(TAG , "Thread Name in startCountDown: "+Thread.currentThread().getName());
        if (mCountDownTimer == null) {
            try {
                LocalTime localTime = LocalTime.parse(endStr);
                int milisecond = localTime.toSecondOfDay() * 1000;

                if (milisecond > 0) {
                    new AsyncTask<Void, Long, Void>() {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            long millisUntilFinished = milisecond;

                            while (millisUntilFinished > 0) {
                                long secondsInMilli = 1000;
                                long minutesInMilli = secondsInMilli * 60;
                                long hoursInMilli = minutesInMilli * 60;

                                long elapsedHours = millisUntilFinished / hoursInMilli;
                                millisUntilFinished = millisUntilFinished % hoursInMilli;

                                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                                millisUntilFinished = millisUntilFinished % minutesInMilli;

                                long elapsedSeconds = millisUntilFinished / secondsInMilli;

                                publishProgress(elapsedHours, elapsedMinutes, elapsedSeconds);

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                millisUntilFinished -= 1000;
                            }

                            return null;
                        }

                        @Override
                        protected void onProgressUpdate(Long... values) {
                            long elapsedHours = values[0];
                            long elapsedMinutes = values[1];
                            long elapsedSeconds = values[2];

                            String strHour = "" + elapsedHours;
                            if (strHour.length() == 1) {
                                strHour = "0" + strHour;
                            }

                            String strMin = "" + elapsedMinutes;
                            if (strMin.length() == 1) {
                                strMin = "0" + strMin;
                            }

                            String strSecond = "" + elapsedSeconds;
                            if (strSecond.length() == 1) {
                                strSecond = "0" + strSecond;
                            }

                            if (elapsedHours == 0 && elapsedMinutes == 0 && !isPopupShows) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity.this);
                                builder.setTitle(getString(R.string.app_name));
                                builder.setCancelable(false);
                                builder.setMessage("This meeting is going to be expired within one minute.");
                                builder.setPositiveButton("Ok", null);
                                builder.show();
                                isPopupShows = true;
                            }
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity.this);
                            builder.setTitle(getString(R.string.app_name));
                            builder.setCancelable(false);
                            builder.setMessage("Meeting has expired.");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    exitFromTheRoom();
                                }
                            });
                            builder.show();
                        }
                    }.execute();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    private void setColorAndText(String name) {
        Log.d(TAG , "Thread Name in setColorAndText: "+Thread.currentThread().getName());
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(100), rnd.nextInt(100), rnd.nextInt(100));
        participant_initial.setTextColor(color);
        participant_initial.setText(getInitial(name));
    }

    private void setParticipantName(String identity) {
        Log.d(TAG , "Thread Name in setParticipantName: "+Thread.currentThread().getName());
        if (identity != null && identity.length() > 0) {
            try {
                List<String> splitString = Arrays.asList(identity.split("\\-"));
                if (splitString.size() > 0) {
                    setColorAndText(splitString.get(1));
                } else {
                    String resVal = removePrefix(identity, "participant-");
                    setColorAndText(resVal);
                }
            } catch (Exception e) {
                setColorAndText(identity);
            }
        } else {
            setColorAndText(identity);
        }
    }

    @Override
    public void sendMessage(String message) {

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void deleteMessage(String message_id) throws JSONException {
        Log.d(TAG , "Thread Name in deleteMessage: "+Thread.currentThread().getName());
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
        Log.e(TAG , "Meeting ID: "+message_id);

        // Wrap your network operations in an AsyncTask
        new AsyncTask<String, Void, DeleteMessageResponseModel>() {
            @Override
            protected DeleteMessageResponseModel doInBackground(String... params) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("messageId", params[0]);
                    String data = obj.toString();
                    Call<DeleteMessageResponseModel> responseBodyCall = serviceLocal.deleteMessage(authToken, data);
                    Response<DeleteMessageResponseModel> response = responseBodyCall.execute();
                    if (response.isSuccessful()) {
                        return response.body();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(DeleteMessageResponseModel result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });

                if (result != null && result.getResult().equals("1")) {
                    try {
                        sendChatMessage("RemoveMessage", result.toString());
                        refreshChat();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute(message_id);
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
        Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    /*Added by Rupesh*/

    private void startTimerInBackground(){
        try {
            Log.d(TAG , "Thread Name in startTimerInBackground: "+Thread.currentThread().getName());
            mCountDownTimerInBackground = new CountDownTimer(18000000, 1000) {

                public void onTick(long millisUntilFinished) {
                    if (!IS_APP_IN_BACKGROUND){
                        if (mCountDownTimerInBackground != null) {
                            mCountDownTimerInBackground.cancel();
                        }
                        mCountDownTimerInBackground = null;
                        if (IS_VIDEO_DISABLED){
                            IS_VIDEO_DISABLED = false;
                            if (videoCallModel.getLocalVideoTrack() != null &&
                                    videoCallModel.getLocalParticipant() != null){
                                //videoCallModel.getLocalVideoTrack().enable(true);
                                videoCallModel.getLocalParticipant().unpublishTrack(videoCallModel.getLocalVideoTrack());
                                videoCallModel.releaseVideoTrack();
                                videoCallManager.resumeVideoTracks();
                                moveLocalVideoToThumbnailView();
                                videoCallManager.resumeVideoTrackPublish();
                                if (isMyViewActive){
                                    thumbnailVideoView.performClick();
                                }
                                //videoCallModel.getLocalVideoTrack().enable(true);
                            }
                        }
                    }else {
                        if (!IS_VIDEO_DISABLED){
                        /*Log.d(TAG , "IS CAMERA USED BY OTHER APPLICATION: "+isCameraUsedByOtherApp());
                        if (isCameraUsedByOtherApp()){
                            if (videoCallModel != null){
                                //videoCallModel.getLocalVideoTrack().enable(false);
                            }
                        }*/
                            IS_VIDEO_DISABLED = true;
                        }
                    }
                }

                public void onFinish() {
                    mCountDownTimerInBackground = null;
                }

            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void checkIfUserAllowedNotaryToCaptureSignatureAndInitial(String meetingId, String userId) throws JSONException {
        Log.d(TAG , "Thread Name in checkIfUserAllowedNotaryToCaptureSignatureAndInitial: "+Thread.currentThread().getName());
        ProgressDialog dialog = new ProgressDialog(mActivity);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();
            }
        });
        Log.e(TAG, "Meeting ID: " + meetingId);
        JSONObject obj = new JSONObject();
        obj.put("RequestId", meetingId);
        obj.put("UserId", userId);
        String data = obj.toString();

        // Wrap your network operations in an AsyncTask
        new AsyncTask<String, Void, SignerSignatureInitialAuthorizationModel>() {
            @Override
            protected SignerSignatureInitialAuthorizationModel doInBackground(String... params) {
                try {
                    Call<SignerSignatureInitialAuthorizationModel> responseBodyCall = serviceLocal.getRequestParticipantByReqIdAndUserId(authToken, params[0]);
                    Response<SignerSignatureInitialAuthorizationModel> response = responseBodyCall.execute();
                    if (response.isSuccessful()) {
                        return response.body();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(SignerSignatureInitialAuthorizationModel result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });

                if (result != null && result.getAuthorizationDetails() != null) {
                    Log.d(TAG, "Signer Signature/Initial Allow Data: \n" + new Gson().toJson(result));
                    isPrimarySigner = result.getAuthorizationDetails().getIsPrimarySigner();
                    isWitness = result.getAuthorizationDetails().getIsWitness();
                    Log.d(TAG , "isPrimarySigner Value: "+isPrimarySigner);
                    Log.d(TAG , "isWitness Value: "+isWitness);

                    if (isWitness != null && !isWitness.equals("")) {
                        customerType = isWitness;
                    } else {
                        customerType = isPrimarySigner;
                    }

                    if (!IS_AUDIT_JOIN_CALL_SENT_ALREADY) {
                        Log.d(TAG , "SENDING AUDIT TRIAL REPORT FOR CONNECTED_IN_ROOM");
                        IS_AUDIT_JOIN_CALL_SENT_ALREADY = true;
                        NotarizationActionUpdateManger.updateNotarizationAction(
                                VideoActivity.this, authToken,
                                requestID, "", userId, customerType,
                                "5", "1", "");
                    }

                    /*if (!isCoSigner) {*/
                        if (!result.getAuthorizationDetails().getHasAuthorizedForSignature().equalsIgnoreCase("1") ||
                                !result.getAuthorizationDetails().getHasAuthorizedForInitial().equalsIgnoreCase("1")) {

                            if (mActivity != null && !mActivity.isFinishing() && !mActivity.isDestroyed()) {
                                // Ask here for the authorization
                                SignerAuthirizationDialogFragment authorizationDialogFragment = new SignerAuthirizationDialogFragment(mActivity, new SignerAuthirizationDialogFragment.OnAuthorizationActionPerformed() {
                                    @Override
                                    public void onAuthorizationGiven() {
                                        try {
                                            updateRequestParticipantCapture(requestID, userId);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onAuthorizationDenied() {
                                        IS_AUTHORIZATION_DIALOG_SHOWN_ALREADY = true;
                                        NotarizationActionUpdateManger.updateNotarizationAction(
                                                VideoActivity.this, authToken,
                                                requestID, "", userId, customerType,
                                                "33", "1", "");
                                    }
                                });
                                FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
                                Fragment prev = mActivity.getFragmentManager().findFragmentByTag("dialog");
                                if (prev != null) {
                                    ft.remove(prev);
                                }
                                ft.addToBackStack(null);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        authorizationDialogFragment.setCancelable(false);
                                        if (!IS_AUTHORIZATION_DIALOG_SHOWN_ALREADY) {
                                            authorizationDialogFragment.show(ft, "dialog");
                                        }
                                    }
                                });
                            }
                        }
                    /*}*/
                }
            }
        }.execute(data);
    }

    @SuppressLint("StaticFieldLeak")
    private void updateRequestParticipantCapture(String meetingId, String userId) throws JSONException {
        Log.d(TAG , "Thread Name in updateRequestParticipantCapture: "+Thread.currentThread().getName());
        ProgressDialog dialog = new ProgressDialog(mActivity);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();
            }
        });
        Log.e(TAG, "Meeting ID: " + meetingId);
        JSONObject obj = new JSONObject();
        obj.put("requestId", meetingId);
        obj.put("userId", userId);
        obj.put("updateCapture", "1");
        String data = obj.toString();

        // Wrap your network operations in an AsyncTask
        new AsyncTask<String, Void, CommonModel>() {
            @Override
            protected CommonModel doInBackground(String... params) {
                try {
                    Call<CommonModel> responseBodyCall = serviceLocal.updateRequestParticipantCapture(authToken, params[0]);
                    Response<CommonModel> response = responseBodyCall.execute();
                    if (response.isSuccessful()) {
                        return response.body();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(CommonModel result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });

                if (result != null) {
                    Log.e(TAG, "Signer Signature/Initial Authorization Update Response Data: \n" + new Gson().toJson(result));
                    NotarizationActionUpdateManger.updateNotarizationAction(
                            VideoActivity.this, authToken,
                            requestID, "", userId, customerType,
                            "25", "1", "");
                }
            }
        }.execute(data);
    }


    /*Added by Rupesh*/

}
