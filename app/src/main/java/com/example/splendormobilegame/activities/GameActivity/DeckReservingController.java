package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.UUID;

public class DeckReservingController<T extends GameActivity> extends Controller {

    private T gameActivity;
    private ReservationFromDeckMessageHandler reservationFromDeckMessageHandler;

    protected DeckReservingController(T activity) {
        super(activity);
        this.gameActivity = activity;
        this.reservationFromDeckMessageHandler = new ReservationFromDeckMessageHandler();
    }

    public void reserveCard(UUID cardUuid) {
        // Maybe you want to check some things now
        // Then call the method to send request
        this.sendRequestToReserve(cardUuid);
    }

    private void sendRequestToReserve(UUID cardUuid) {
        // TODO Compose up the message to the server
        // TODO Send the message
    }

    public ReservationFromDeckMessageHandler getReservationFromDeckMessageHandler() {
        return reservationFromDeckMessageHandler;
    }

    public class ReservationFromDeckMessageHandler extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered ReservationFromDeckMessageHandler react method");

            // TODO Update the model
            // TODO Update the view via `gameActivity` or other objects given in constructor

            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Error while reserving from deck: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Error while reserving from deck: " + errorResponse.data.error);
            return null;
        }

    }

}
