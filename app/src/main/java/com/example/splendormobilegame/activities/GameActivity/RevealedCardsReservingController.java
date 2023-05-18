package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.model.Card;
import com.example.splendormobilegame.model.Game;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.ReservedCard;
import com.example.splendormobilegame.model.Room;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.game.enums.TokenType;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.MakeReservationFromTable;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.UUID;

public class RevealedCardsReservingController<T extends GameActivity> extends Controller {

    private T gameActivity;
    private EndTurnController endTurnController;
    private ReservationFromRevealedMessageHandler reservationFromRevealedMessageHandler;

    protected RevealedCardsReservingController(T activity, CustomWebSocketClient customWebSocketClient, Model model, EndTurnController endTurnController) {
        super(activity, customWebSocketClient, model);
        this.gameActivity = activity;
        this.endTurnController = endTurnController;
        this.reservationFromRevealedMessageHandler = new ReservationFromRevealedMessageHandler();
    }

    public void reserveCard(UUID cardUuid) {
        // Maybe you want to check some things now
        // Then call the method to send request
        this.sendRequestToReserve(cardUuid);
    }

    private void sendRequestToReserve(UUID cardUuid) {
        MakeReservationFromTable.UserDTO userUuidDTO = new MakeReservationFromTable.UserDTO(model.getUserUuid());
        MakeReservationFromTable.CardDTO cardUuidDTO = new MakeReservationFromTable.CardDTO(cardUuid);
        MakeReservationFromTable.DataDTO dataDTO = new MakeReservationFromTable.DataDTO(cardUuidDTO,userUuidDTO);
        UserMessage message = new UserMessage(UUID.randomUUID(), UserRequestType.MAKE_RESERVATION_FROM_TABLE,dataDTO);

        customWebSocketClient.send(message);
    }

    public ReservationFromRevealedMessageHandler getReservationFromRevealedMessageHandler() {
        return reservationFromRevealedMessageHandler;
    }

    public class ReservationFromRevealedMessageHandler extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered ReservationFromRevealedMessageHandler react method");

            //Get the data from the request
            MakeReservationFromTable.ResponseData responseData = (MakeReservationFromTable.ResponseData) ReactionUtils.getResponseData(
                    serverMessage,MakeReservationFromTable.ResponseData.class
            );


            // Update the model
            Room room = model.getRoom();
            Game game = room.getGame();
            User user = room.getUserByUuid(responseData.reservee.userUuid);
            ReservedCard reservedCard = new ReservedCard(game.getCardByUuid(responseData.reservee.reservedCardUuid), user, true);

            //Manage reserved card
            game.reserveCard(user,reservedCard);
            if(responseData.gotGoldenToken){
                game.transferTokensToUser(TokenType.GOLD_JOKER ,1,user);
            }
            game.removeCardFromTable(reservedCard.getCard());

            //Add new card
            MakeReservationFromTable.CardDataResponse cardDataResponse = responseData.cardDataResponse;
            Card newCard = null;
            if(cardDataResponse!=null){
                newCard = new Card(
                        cardDataResponse.uuid,
                        cardDataResponse.cardTier,
                        cardDataResponse.points,
                        cardDataResponse.emeraldCost,
                        cardDataResponse.sapphireCost,
                        cardDataResponse.rubyCost,
                        cardDataResponse.diamondCost,
                        cardDataResponse.onyxCost,
                        cardDataResponse.additionalToken
                );
            }
            if(newCard != null){
                game.addNewCardToTable(newCard);
            }


            // If this message pertains to me, it means I requested it, indicating that I have taken my action during my turn.
            // Therefore, I need to end my turn.
            // Perhaps it was not the best decision to require the user to manually end their turn.
            // The server should handle this automatically.
            RevealedCardsReservingController.this.endTurnController.endTurn();
            gameActivity.updateScoreBoard();
            gameActivity.updateTokenNumber();
            gameActivity.updateReservedCards();
            gameActivity.updateCards();
            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Error while reserving from revealed: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Error while reserving from revealed: " + errorResponse.data.error);
            return null;
        }

    }

}
