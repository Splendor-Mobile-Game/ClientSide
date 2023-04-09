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
import com.github.splendor_mobile_game.websocket.handlers.reactions.JoinRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

public class JoinRoomResponse extends UserReaction {

    public JoinRoomResponse(ServerMessage serverMessage) {
        super(serverMessage);
    }

    @Override
    public UserMessage react() {
        Log.i("UserReaction", "Entered JoinRoomResponse react method");

        JoinRoom.ResponseData responseData = (JoinRoom.ResponseData) Utils.getResponseData(serverMessage, JoinRoom.ResponseData.class);

        if (Model.getRoom() == null) {
            // Case when we are joining the room

            JoinRoom.UserDataResponse owner = responseData.users.get(0);
            Room room = new Room(responseData.room.uuid, responseData.room.name, Model.getEnteredRoomCode(), new User(owner.uuid, owner.name));

            // DEBUG PURPOSES START
            String message = "";
            for (int i = 1; i < responseData.users.size(); i++) {
                room.addUser(new User(responseData.users.get(i).uuid, responseData.users.get(i).name));
                message += responseData.users.get(i).name + "\n";
            }
            // DEBUG PURPOSES END

            Model.setRoom(room);

            Intent myIntent = new Intent(Model.getActivity(), WaitingRoomActivity.class);
            Model.getActivity().startActivity(myIntent);

            // DEBUG PURPOSES START
            Utils.showToast(message);
            // DEBUG PURPOSES END

            // Return null if you don't want to send anything to the server
            return null;

        } else {
            // Case when we are in the waiting room already

            JoinRoom.UserDataResponse newUser = responseData.users.get(responseData.users.size()-1);
            Model.getRoom().addUser(new User(newUser.uuid, newUser.name));

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

            Utils.showToast("User: " + newUser.name + " has joined to the room.");

            return null;
        }
    }

    @Override
    public UserMessage onFailure(ErrorResponse errorResponse) {
        Utils.showToast("Cannot join to the room: " + errorResponse.data.error);
        return null;
    }

    @Override
    public UserMessage onError(ErrorResponse errorResponse) {
        Utils.showToast("Cannot join to the room: " + errorResponse.data.error);
        return null;
    }

}
