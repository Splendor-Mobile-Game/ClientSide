package com.example.splendormobilegame.activities.WaitingRoom;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.reactions.LeaveRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;


public class NewRoomOwnerController extends Controller {
    private NewRoomOwnerResponse newRoomOwnerResponse;

    public NewRoomOwnerController(CustomAppCompatActivity activity, CustomWebSocketClient customWebSocketClient, Model model) {
        super(activity, customWebSocketClient, model);
        this.newRoomOwnerResponse = new NewRoomOwnerResponse();
    }

    public NewRoomOwnerResponse getNewRoomOwnerResponse() {
        return newRoomOwnerResponse;
    }

    public class NewRoomOwnerResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {

            if (model.getRoom() != null) {
                LeaveRoom.ResponseData responseData = (LeaveRoom.ResponseData) ReactionUtils.getResponseData(serverMessage, LeaveRoom.ResponseData.class);

                User owner = model.getRoom().getUserByUuid(responseData.user.id);
                model.getRoom().setOwner(owner);

                activity.showToast("User: " + responseData.user.name + " is a new room owner.");
            }

            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Error while setting new room owner: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Error while setting new room owner: " + errorResponse.data.error);
            return null;
        }
    }
}