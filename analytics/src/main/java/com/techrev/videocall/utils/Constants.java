package com.techrev.videocall.utils;

public class Constants {
    public static String API_BASE_URL = "";
    public static int member_type_banker = 1;
    public static int member_type_participant = 2;
    public static  boolean isLocationEnable = true;
    public static  boolean isCaptureImageEnable = true;
    public static  boolean isTimerEnable = true;
    public static  boolean isHostDisable = true;

    public static int EVENTTYPE_NONE = 1;
    public static int EVENTTYPE_CONNECTED_IN_ROOM = 2;
    public static int EVENTTYPE_DISCONNECTED_FROM_ROOM = 3;
    public static int EVENTTYPE_PARTICIPANT_CONNECTED_IN_ROOM = 4;
    public static int EVENTTYPE_PARTICIPANT_DISCONNECTED_FROM_ROOM = 5;
    public static int EVENTTYPE_PARTICIPANT_MESSAGE_COMING_FROM_ROOM = 6;
    public static int EVENTTYPE_MESSAGE_SEND_TO_ROOM = 7;
    public static int EVENTTYPE_RECONNECTING = 8;
    public static int EVENTTYPE_RECONNECTED = 9;
    public static int EVENTTYPE_RESET = 10;
    public static int EVENTTYPE_FAILURE = 11;
    public static int EVENTTYPE_SCREEN_SUBSCRIBED = 12;
    public static int EVENTTYPE_SCREEN_UNSUBSCRIBED = 13;
    public static int EVENTTYPE_VIDEO_SUBSCRIBED = 14;
    public static int EVENTTYPE_VIDEO_UNSUBSCRIBED = 15;
    public static int EVENTTYPE_VIDEO_ENABLED = 16;
    public static int EVENTTYPE_VIDEO_DISABLED = 17;
    public static int EVENTTYPE_AUDIO_ENABLED = 18;
    public static int EVENTTYPE_AUDIO_DISABLED = 19;
    public static int EVENTTYPE_DOMINANT_SPEAKER = 20;


    public static int APP_STATUS_FOREGROUND = 1;
    public static int APP_STATUS_BACKGROUND = 2;

    public static String isScreenShared = "isScreenShared";

    /*Added By Rupesh*/
    public static String CALL_ENDED_IN_BACKGROUND = "CALL_ENDED_IN_BACKGROUND";
    public static String SCREEN_SUBSCRIBED_IN_BACKGROUND = "SCREEN_SUBSCRIBED_IN_BACKGROUND";
    public static String SCREEN_UNSUBSCRIBED_IN_BACKGROUND = "SCREEN_UNSUBSCRIBED_IN_BACKGROUND";
    public static String COSIGNER_ACTIVITY_IN_FOREGROUND = "COSIGNER_ACTIVITY_IN_FOREGROUND";
    public static String SIGNATURE_CAPTURE_REQUEST_IN_BACKGROUND = "SIGNATURE_CAPTURE_REQUEST_IN_BACKGROUND";
    public static String INITIAL_CAPTURE_REQUEST_IN_BACKGROUND = "INITIAL_CAPTURE_REQUEST_IN_BACKGROUND";
    public static String SIGNATURE_INITIAL_REPLACE_REQUEST_IN_BACKGROUND = "SIGNATURE_INITIAL_REPLACE_REQUEST_IN_BACKGROUND";
    public static String CURRENT_SIGNATURE_INITIAL_TAG_REPLACE_DOC_ID = "CURRENT_SIGNATURE_INITIAL_TAG_REPLACE_DOC_ID";
    /*Added By Rupesh*/

}
