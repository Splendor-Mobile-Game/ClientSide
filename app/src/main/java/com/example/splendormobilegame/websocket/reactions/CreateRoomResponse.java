package com.example.splendormobilegame.websocket.reactions;

import android.util.Log;

import com.example.splendormobilegame.websocket.Model;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;

public class CreateRoomResponse extends UserReaction {

    public CreateRoomResponse(ServerMessage serverMessage, Model model) {
        super(serverMessage, model);
    }

    @Override
    public UserMessage react() {
        Log.i("UserReaction", "HEY FROM REACT");

        // Return null if you don't want to send anything to the server
        return null;
    }
}
