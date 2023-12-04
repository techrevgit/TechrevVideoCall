package com.techrev.analytics;

import org.json.JSONException;

public interface ChatMessageInterface {

    public void sendMessage(String message);
    public void deleteMessage(String message_id) throws JSONException;

}
