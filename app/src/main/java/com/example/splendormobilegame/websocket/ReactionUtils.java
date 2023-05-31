package com.example.splendormobilegame.websocket;

import android.app.Activity;
import android.widget.Toast;

import com.example.splendormobilegame.model.Model;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.utils.json.JsonParser;
import com.github.splendor_mobile_game.websocket.utils.json.exceptions.JsonParserException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ReactionUtils {

    public static void showToast(Activity activity, String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Object getResponseData(ServerMessage serverMessage, Class<?> responseDataClass) {
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            return JsonParser.parseJson(gson.toJson(serverMessage.getData()), responseDataClass);
        } catch (JsonParserException e) {
            throw new RuntimeException(e);
        }
    }
}
