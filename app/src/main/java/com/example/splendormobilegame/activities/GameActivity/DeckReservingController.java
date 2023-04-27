package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.websocket.UserReaction;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;





import java.util.UUID;

import javax.smartcardio.Card;

public class DeckReservingController<T extends GameActivity> extends Controller {

    private T gameActivity;
    private TurnController turnController;
    private ReservationFromDeckMessageHandler reservationFromDeckMessageHandler;

    protected DeckReservingController(T activity, TurnController turnController) {
        super(activity);
        this.gameActivity = activity;
        this.turnController = turnController;
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

        Card card= getCardByUuid(cardUuid);
        
        MakeReservationFromDeck.DataDTO dataDTO = new MakeReservationFromDeck.DataDTO(Model.getUserUuid(),card.getCardTier());

        UserMessage userMessage = new UserMessage(UUID.randomUUID(), UserRequestType.MAKE_RESERVATION_FROM_DECK, dataDTO);
        CustomWebSocketClient.getInstance().send(userMessage);

    }

    public ReservationFromDeckMessageHandler getReservationFromDeckMessageHandler() {
        return reservationFromDeckMessageHandler;
    }

    public class ReservationFromDeckMessageHandler extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered ReservationFromDeckMessageHandler react method");

            
            MakeReservationFromDeck.ResponseData responseData = (MakeReservationFromDeck.ResponseData) ReactionUtils.getResponseData(serverMessage, MakeReservationFromDeck.ResponseData.class);


           

            reserveCard(responseData.userUuid, responseData.card);
            


            // TODO Update the view via `gameActivity` or other objects given in constructor

            // If this message pertains to me, it means I requested it, indicating that I have taken my action during my turn.
            // Therefore, I need to end my turn.


            activity.showToast("User "+getUserByUuid(responseData.userUuid).getName()+" reserved card from deck "+responseData.card.getCardTier());

            // Perhaps it was not the best decision to require the user to manually end their turn.
            // The server should handle this automatically.
            DeckReservingController.this.turnController.endTurn();

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
