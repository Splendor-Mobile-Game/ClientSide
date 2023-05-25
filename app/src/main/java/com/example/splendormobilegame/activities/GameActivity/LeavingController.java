package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.MainActivity;
import com.example.splendormobilegame.activities.WaitingRoom.WaitingRoomActivityAdapter;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.Noble;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.game.enums.TokenType;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.LeaveRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class LeavingController<T extends GameActivity> extends Controller {

    private T gameActivity;
    private LeaveRoomResponse leaveRoomResponse;


    public LeavingController(T activity, CustomWebSocketClient customWebSocketClient, Model model) {
        super(activity, customWebSocketClient, model);
        this.gameActivity = activity;
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
        LeaveRoom.RoomDTO roomDTO = new LeaveRoom.RoomDTO(model.getRoom().getUuid());
        LeaveRoom.UserDTO userDTO = new LeaveRoom.UserDTO(model.getUserUuid());
        LeaveRoom.DataDTO dataDTO = new LeaveRoom.DataDTO(roomDTO, userDTO);

        UserMessage userMessage = new UserMessage(UUID.randomUUID(), UserRequestType.LEAVE_ROOM, dataDTO);
        customWebSocketClient.send(userMessage);

        // Don't bother with what server thinks, we want to leave
        model.setRoom(null);

        activity.changeActivity(MainActivity.class);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public class LeaveRoomResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered LeaveRoomResponse react method");

            // TODO: Probably not needed
            if (model.getRoom() != null) {
                // Case when other player has left the room

                LeaveRoom.ResponseData responseData = (LeaveRoom.ResponseData) ReactionUtils.getResponseData(serverMessage, LeaveRoom.ResponseData.class);
                User userToRemove = model.getRoom().getUserByUuid(responseData.user.id);

                HashMap<TokenType, Integer> tokens=userToRemove.getTokens();

                tokens.forEach((tokenType, amount) -> {

                    model.getRoom().getGame().transferTokensFromUser(tokenType, amount, userToRemove);
                });

                model.getRoom().removeUser(userToRemove);


                gameActivity.updateScoreBoard();
                gameActivity.updateTokenNumber();


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
