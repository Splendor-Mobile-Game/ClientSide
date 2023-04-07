package com.example.splendormobilegame.websocket.reactions;

import android.content.Intent;
import android.util.Log;

import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.reactions.CreateRoom;

public class CreateRoomResponse extends UserReaction {

    public CreateRoomResponse(ServerMessage serverMessage) {
        super(serverMessage);
    }

    @Override
    public UserMessage react() {
        // serverMessage.getResult()

        CreateRoom.ResponseData responseData = (CreateRoom.ResponseData) serverMessage.getData();


        Log.i("UserReaction", "HEY FROM REACT");

        // Return null if you don't want to send anything to the server
        return null;
    }
}
