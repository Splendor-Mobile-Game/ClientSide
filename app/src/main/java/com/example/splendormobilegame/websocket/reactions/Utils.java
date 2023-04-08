package com.example.splendormobilegame.websocket.reactions;

import android.widget.Toast;

import com.example.splendormobilegame.model.Model;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.utils.json.JsonParser;
import com.github.splendor_mobile_game.websocket.utils.json.exceptions.JsonParserException;
import com.google.gson.Gson;

public class Utils {

    public static void showToast(String message) {
        Model.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(Model.getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Object getResponseData(ServerMessage serverMessage, Class<?> responseDataClass) {
        try {
            return JsonParser.parseJson((new Gson()).toJson(serverMessage.getData()), responseDataClass);
        } catch (JsonParserException e) {
            throw new RuntimeException(e);
        }
    }
}
