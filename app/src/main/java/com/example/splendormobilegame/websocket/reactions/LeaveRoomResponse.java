package com.example.splendormobilegame.websocket.reactions;

import android.util.Log;

import com.example.splendormobilegame.WaitingRoomActivity;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.reactions.LeaveRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

public class LeaveRoomResponse extends UserReaction {

    public LeaveRoomResponse(ServerMessage serverMessage) {
        super(serverMessage);
    }

    @Override
    public UserMessage react() {
        Log.i("UserReaction", "Entered LeaveRoomResponse react method");

        if (Model.getRoom() != null) {
            // Case when other player has left the room

            LeaveRoom.ResponseData responseData = (LeaveRoom.ResponseData) Utils.getResponseData(serverMessage, LeaveRoom.ResponseData.class);

            User userToRemove = Model.getRoom().getUserByUuid(responseData.user.id);
            Model.getRoom().removeUser(userToRemove);

            // DEBUG PURPOSES START
            Model.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    WaitingRoomActivity activity = (WaitingRoomActivity) Model.getActivity();
                    String users = "";
                    for (User u: Model.getRoom().getUsers()) {
                        users += u.getName() + "\n";
                    }
                    activity.binding.debugUsers.setText(users);
                }
            });
            // DEBUG PURPOSES END

            Utils.showToast("User: " + responseData.user.name + " has left the room.");
        }

        return null;
    }

    @Override
    public UserMessage onFailure(ErrorResponse errorResponse) {
        Utils.showToast("Error while leaving the room: " + errorResponse.data.error);
        return null;
    }

    @Override
    public UserMessage onError(ErrorResponse errorResponse) {
        Utils.showToast("Error while leaving the room: " + errorResponse.data.error);
        return null;
    }

}
