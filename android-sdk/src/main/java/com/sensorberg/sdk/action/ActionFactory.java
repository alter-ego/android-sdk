package com.sensorberg.sdk.action;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class ActionFactory {

    public interface ServerType {
        int URL_MESSAGE     = 1;
        int VISIT_WEBSITE   = 2;
        int IN_APP          = 3;
    }


    private static final String SUBJECT = "subject";
    private static final String BODY = "body";
    private static final String URL = "url";
    private static final String DELAY_TIME = "delayTime";
    private static final String CONTENT = "content";
    private static final String TYPE = "type";
    private static final String PAYLOAD = "payload";


    public static Action actionFromJSONObject(JSONObject contentJSON) throws JSONException {
        int actionType = contentJSON.getInt(TYPE);
        UUID actionUUID = UUID.fromString(contentJSON.getString("id"));

        long delayMilliseconds = contentJSON.optLong(DELAY_TIME, Action.NO_DELAY) * 1000;
        String messageString = contentJSON.getString(CONTENT);
        JSONObject message = new JSONObject(messageString);

        return getAction(actionType, message, actionUUID, delayMilliseconds);
    }

    public static Action getAction(int actionType, JSONObject message, UUID actionUUID, long delay) throws JSONException {
        if (message == null){
            return null;
        }
        Action value = null;
        String payload = null;
        if (message.opt(PAYLOAD) != JSONObject.NULL){
            payload = message.optString(PAYLOAD, null);
        }

        switch (actionType){
            case ServerType.URL_MESSAGE:{
                value = new UriMessageAction(
                        actionUUID,
                        message.getString(SUBJECT),
                        message.getString(BODY),
                        message.getString(URL),
                        payload,
                        delay
                );
                break;
            }
            case ServerType.VISIT_WEBSITE:{
                value = new VisitWebsiteAction(
                        actionUUID,
                        message.optString(SUBJECT, null),
                        message.optString(BODY, null),
                        Uri.parse(message.getString(URL)),
                        payload,
                        delay
                );
                break;
            }
            case ServerType.IN_APP:{
                value = new InAppAction(
                        actionUUID,
                        message.optString(SUBJECT, null),
                        message.optString(BODY, null),
                        payload,
                        Uri.parse(message.getString(URL)),
                        delay
                );
            }
        }
        return value;
    }
}
