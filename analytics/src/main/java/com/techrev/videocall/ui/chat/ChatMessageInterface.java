package com.techrev.videocall.ui.chat;

import org.json.JSONException;

public interface ChatMessageInterface {

    public void sendMessage(String message);
    public void deleteMessage(String message_id) throws JSONException;

}
