package com.example.splendormobilegame.activities.WaitingRoom;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.reactions.LeaveRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;


public class NewRoomOwnerController extends Controller {
    private NewRoomOwnerResponse newRoomOwnerResponse;

    public NewRoomOwnerController(CustomAppCompatActivity activity) {
        super(activity);
        this.newRoomOwnerResponse = new NewRoomOwnerResponse();
    }

    public NewRoomOwnerResponse getNewRoomOwnerResponse() {
        return newRoomOwnerResponse;
    }

    public class NewRoomOwnerResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {

            if (Model.getRoom() != null) {
                LeaveRoom.ResponseData responseData = (LeaveRoom.ResponseData) ReactionUtils.getResponseData(serverMessage, LeaveRoom.ResponseData.class);

                User owner = Model.getRoom().getUserByUuid(responseData.user.id);
                Model.getRoom().setOwner(owner);

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