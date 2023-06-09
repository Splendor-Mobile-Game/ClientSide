package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.Card;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.model.ReservedCard;
import com.example.splendormobilegame.websocket.UserReaction;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.github.splendor_mobile_game.game.enums.TokenType;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.MakeReservationFromDeck;


import java.util.UUID;

public class DeckReservingController<T extends GameActivity> extends Controller {

    private T gameActivity;
    private EndTurnController endTurnController;
    private ReservationFromDeckMessageHandler reservationFromDeckMessageHandler;

    public DeckReservingController(T activity, CustomWebSocketClient customWebSocketClient, Model model, EndTurnController endTurnController) {
        super(activity, customWebSocketClient, model);
        this.gameActivity = activity;
        this.endTurnController = endTurnController;
        this.reservationFromDeckMessageHandler = new ReservationFromDeckMessageHandler();
    }

    public void reserveCard(int cardTier) {
        // Maybe you want to check some things now
        // Then call the method to send request
        this.sendRequestToReserve(cardTier);
    }

    private void sendRequestToReserve(int cardTier) {

        MakeReservationFromDeck.DataDTO dataDTO = new MakeReservationFromDeck.DataDTO(model.getUserUuid(),  String.valueOf(cardTier));

        UserMessage userMessage = new UserMessage(UUID.randomUUID(), UserRequestType.MAKE_RESERVATION_FROM_DECK, dataDTO);
        customWebSocketClient.send(userMessage);
    }

    public ReservationFromDeckMessageHandler getReservationFromDeckMessageHandler() {
        return reservationFromDeckMessageHandler;
    }

    public class ReservationFromDeckMessageHandler extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            System.out.println("Entered ReservationFromDeckMessageHandler react method");

            MakeReservationFromDeck.ResponseData responseData = (MakeReservationFromDeck.ResponseData) ReactionUtils.getResponseData(serverMessage, MakeReservationFromDeck.ResponseData.class);

            User user=model.getRoom().getUserByUuid(responseData.userUuid);
            Card card=new Card(responseData.card.uuid,responseData.card.cardTier,responseData.card.prestige,responseData.card.tokensRequired.emerald,responseData.card.tokensRequired.sapphire, responseData.card.tokensRequired.ruby,responseData.card.tokensRequired.diamond,responseData.card.tokensRequired.onyx, responseData.card.bonusColor, responseData.card.cardID);

            boolean visible = user.getUuid().equals(model.getUserUuid());
            ReservedCard reservedCard= new ReservedCard(card, user, visible);
            model.getRoom().getGame().reserveCard(user, reservedCard);
            if(responseData.goldenToken){
                model.getRoom().getGame().transferTokensToUser(TokenType.GOLD_JOKER ,1,user);
            }

            gameActivity.updateScoreBoard();
            gameActivity.updateTokenNumber();
            gameActivity.updateReservedCards();
            gameActivity.updateCards();
            // If this message pertains to me, it means I requested it, indicating that I have taken my action during my turn.
            // Therefore, I need to end my turn.
            DeckReservingController.this.endTurnController.endTurn();

            // Update the view
            activity.showToast("User "+user.getName()+" reserved card from deck "+card.getCardTier());


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
