package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.MainActivity;
import com.example.splendormobilegame.activities.WaitingRoom.WaitingRoomActivityAdapter;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.LeaveRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.ArrayList;
import java.util.UUID;

public class LeavingController extends Controller {

    private LeaveRoomResponse leaveRoomResponse;

    public LeavingController(CustomAppCompatActivity activity) {
        super(activity);
        this.leaveRoomResponse = new LeaveRoomResponse();
    }

    public LeaveRoomResponse getLeaveRoomResponse() {
        return leaveRoomResponse;
    }

    public void leaveRoom() {
        // Maybe you want to check something before sending request
        this.sendRequest();
    }

    private void sendRequest() {
        LeaveRoom.RoomDTO roomDTO = new LeaveRoom.RoomDTO(Model.getRoom().getUuid());
        LeaveRoom.UserDTO userDTO = new LeaveRoom.UserDTO(Model.getUserUuid());
        LeaveRoom.DataDTO dataDTO = new LeaveRoom.DataDTO(roomDTO, userDTO);

        UserMessage userMessage = new UserMessage(UUID.randomUUID(), UserRequestType.LEAVE_ROOM, dataDTO);
        CustomWebSocketClient.getInstance().send(userMessage);

        // Don't bother with what server thinks, we want to leave
        Model.setRoom(null);

        activity.changeActivity(MainActivity.class);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public class LeaveRoomResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered LeaveRoomResponse react method");

            // TODO: Probably not needed
            if (Model.getRoom() != null) {
                // Case when other player has left the room

                LeaveRoom.ResponseData responseData = (LeaveRoom.ResponseData) ReactionUtils.getResponseData(serverMessage, LeaveRoom.ResponseData.class);

                User userToRemove = Model.getRoom().getUserByUuid(responseData.user.id);
                Model.getRoom().removeUser(userToRemove);

                // TODO Update the view

                activity.showToast("User: " + responseData.user.name + " has left the room.");
            }

            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Error while leaving the room: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Error while leaving the room: " + errorResponse.data.error);
            return null;
        }

    }

}
