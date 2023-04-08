package com.example.splendormobilegame.websocket.reactions;

import android.content.Intent;
import android.util.Log;

import com.example.splendormobilegame.WaitingRoomActivity;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.Room;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.reactions.CreateRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

public class CreateRoomResponse extends UserReaction {

    public CreateRoomResponse(ServerMessage serverMessage) {
        super(serverMessage);
    }

    @Override
    public UserMessage react() {
        Log.i("UserReaction", "Entered CreateRoomResponse");

        CreateRoom.ResponseData responseData = (CreateRoom.ResponseData) Utils.getResponseData(serverMessage, CreateRoom.ResponseData.class);

        Model.setRoom(new Room(
                responseData.room.uuid, responseData.room.name, responseData.room.enterCode, new User(responseData.user.id, responseData.user.name)
        ));

        Intent myIntent = new Intent(Model.getActivity(), WaitingRoomActivity.class);
        Model.getActivity().startActivity(myIntent);

        // Return null if you don't want to send anything to the server
        return null;
    }

    @Override
    public UserMessage onFailure(ErrorResponse errorResponse) {
        Utils.showToast("Cannot create room, your fault: " + errorResponse.data.error);
        return null;
    }

    @Override
    public UserMessage onError(ErrorResponse errorResponse) {
        Utils.showToast("Cannot create room, server's fault: " + errorResponse.data.error);
        return null;
    }
}
