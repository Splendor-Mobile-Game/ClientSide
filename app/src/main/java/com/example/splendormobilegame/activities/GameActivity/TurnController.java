package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.reactions.GetTokens;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

public class TurnController<T extends GameActivity> extends Controller {

    private T gameActivity;

    protected TurnController(T activity) {
        super(activity);
        this.gameActivity = activity;
    }

    public void sendRequestToEndTurn() {
        // TODO
    }

    public class NewTurnAnnouncement extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered NewTurnAnnouncement");

            // Get the data from the request
            // ...

            // TODO Update the model
            // ...

            // TODO Update the view
            // ...
            // this.gameActivity.updateTurnIndicator()
            // if this is your turn
            // this.gameActivity.unblockButtons()
            // else
            // this.gameActivity.blockButtons()
            // ...

            // Return null if you don't want to send anything to the server
            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("NewTurnAnnouncement error: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("NewTurnAnnouncement error: " + errorResponse.data.error);
            return null;
        }

    }

}
