package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.UUID;

public class BuyingReservedCardsController<T extends GameActivity> extends Controller {

    private T gameActivity;
    private TurnController turnController;
    private BuyReservedCardMessageHandler buyReservedCardMessageHandler;

    protected BuyingReservedCardsController(T activity, TurnController turnController) {
        super(activity);
        this.gameActivity = activity;
        this.turnController = turnController;
        this.buyReservedCardMessageHandler = new BuyReservedCardMessageHandler();
    }

    public void buyReservedCard(UUID cardUuid) {
        // Maybe you want to check some things now
        // Then call the method to send request
        this.sendRequest(cardUuid);
    }

    private void sendRequest(UUID cardUuid) {
        // TODO Compose up the message to the server
        // TODO Send the message
    }

    public BuyReservedCardMessageHandler getBuyReservedCardMessageHandler() {
        return buyReservedCardMessageHandler;
    }

    public class BuyReservedCardMessageHandler extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered BuyReservedCardMessageHandler react method");

            // TODO Update the model
            // TODO Update the view via `gameActivity` or other objects given in constructor

            // If this message pertains to me, it means I requested it, indicating that I have taken my action during my turn.
            // Therefore, I need to end my turn.
            // Perhaps it was not the best decision to require the user to manually end their turn.
            // The server should handle this automatically.
            BuyingReservedCardsController.this.turnController.endTurn();

            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Error while buying from reserved: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Error while buying from reserved: " + errorResponse.data.error);
            return null;
        }

    }

}
