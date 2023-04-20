package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.activities.WaitingRoom.WaitingRoomActivity;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.Room;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.reactions.CreateRoom;
import com.github.splendor_mobile_game.websocket.handlers.reactions.GetTokens;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

public class TokensController<T extends GameActivity> extends Controller {

    private T gameActivity;

    public TokensController(T activity) {
        super(activity);
        this.gameActivity = activity;
    }

    public class GetTokensResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered GetTokensResponse");

            // Get the data from the request
            GetTokens.ResponseData responseData = (GetTokens.ResponseData) ReactionUtils.getResponseData(
                    serverMessage, GetTokens.ResponseData.class
            );

            // TODO Update the model
            // ...

            // TODO Update the view
            // ...
            // this.gameActivity.updateScoreboard()
            // this.gameActivity.hideGettingTokensSideBar()
            // ...

            // if this is your turn, end it by calling
            // this.gameActivity.TurnController.sendRequestToEndTurn()

            // Return null if you don't want to send anything to the server
            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Cannot get tokens: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Cannot get tokens: " + errorResponse.data.error);
            return null;
        }

    }

}
