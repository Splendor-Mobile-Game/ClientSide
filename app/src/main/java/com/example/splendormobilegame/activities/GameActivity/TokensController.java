package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.reactions.GetTokens;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

public class TokensController<T extends GameActivity> extends Controller {

    private T gameActivity;
    private TurnController turnController;
    private GetTokensMessageHandler getTokensMessageHandler;

    public TokensController(T activity, TurnController turnController) {
        super(activity);
        this.gameActivity = activity;
        this.turnController = turnController;
        this.getTokensMessageHandler = new GetTokensMessageHandler();
    }

    public void getTokens(int red, int blue, int green, int black, int white) {
        // Maybe you want to check something here
        // and then call the method to send the request
        this.sendRequest(red, blue, green, black, white);
    }

    private void sendRequest(int red, int blue, int green, int black, int white) {
        // TODO Compose up the message
        // TODO Send the message
    }

    public GetTokensMessageHandler getGetTokensMessageHandler() {
        return getTokensMessageHandler;
    }

    public class GetTokensMessageHandler extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered GetTokensMessageHandler");

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

            // If this message pertains to me, it means I requested it, indicating that I have taken my action during my turn.
            // Therefore, I need to end my turn.
            // Perhaps it was not the best decision to require the user to manually end their turn.
            // The server should handle this automatically.
            TokensController.this.turnController.endTurn();

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
