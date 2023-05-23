package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.model.Card;
import com.example.splendormobilegame.model.Game;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.Room;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.game.enums.TokenType;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.BuyRevealedMine;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.UUID;

public class BuyingRevealedCardsController<T extends GameActivity> extends Controller {
    private T gameActivity;
    private EndTurnController endTurnController;
    private BuyRevealedCardMessageHandler buyRevealedCardMessageHandler;

    protected BuyingRevealedCardsController(T activity, CustomWebSocketClient customWebSocketClient, Model model, EndTurnController endTurnController) {
        super(activity, customWebSocketClient, model);
        this.gameActivity = activity;
        this.endTurnController = endTurnController;
        this.buyRevealedCardMessageHandler = new BuyRevealedCardMessageHandler();
    }


    public void buyRevealedCard(UUID cardUuid) {
        // Maybe you want to check some things now
        // Then call the method to send request
        this.sendRequest(cardUuid);
    }

    private void sendRequest(UUID cardUuid) {
        BuyRevealedMine.UserDTO userUuidDTO = new BuyRevealedMine.UserDTO(model.getUserUuid());
        BuyRevealedMine.CardDTO cardUuidDTO = new BuyRevealedMine.CardDTO(cardUuid);
        BuyRevealedMine.DataDTO dataDTO = new BuyRevealedMine.DataDTO(userUuidDTO,cardUuidDTO);
        UserMessage message = new UserMessage(UUID.randomUUID(), UserRequestType.BUY_REVEALED_MINE,dataDTO);

        customWebSocketClient.send(message);
    }

    public BuyRevealedCardMessageHandler getBuyRevealedCardMessageHandler() {
        return buyRevealedCardMessageHandler;
    }

    public class BuyRevealedCardMessageHandler extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage){
            Log.i("UserReaction", "Entered BuyRevealedCardMessageHandler react method");

            //Get the data from the request
            BuyRevealedMine.ResponseData responseData = (BuyRevealedMine.ResponseData) ReactionUtils.getResponseData(
                    serverMessage,BuyRevealedMine.ResponseData.class
            );
            BuyRevealedMine.TokensDataResponse tokensDataResponse = responseData.buyer.tokens;
            EnumMap<TokenType,Integer> tokens = new EnumMap<TokenType, Integer>(TokenType.class);
            tokens.put(TokenType.RUBY,tokensDataResponse.ruby);
            tokens.put(TokenType.EMERALD,tokensDataResponse.emerald);
            tokens.put(TokenType.SAPPHIRE,tokensDataResponse.sapphire);
            tokens.put(TokenType.DIAMOND,tokensDataResponse.diamond);
            tokens.put(TokenType.ONYX,tokensDataResponse.onyx);
            tokens.put(TokenType.GOLD_JOKER,tokensDataResponse.gold);

            BuyRevealedMine.CardDataResponse cardDataResponse = responseData.newCardRevealed;
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

            // Update the model
            Room room = model.getRoom();
            Game game = room.getGame();
            User buyer = room.getUserByUuid(responseData.buyer.userUuid);
            Card boughtCard = game.getCardByUuid(responseData.buyer.cardUuid);

            //Return tokens to main stack
            for(TokenType tokenType : EnumSet.allOf(TokenType.class)){
                game.addTokens(tokenType, buyer.getTokensCount(tokenType)-tokens.get(tokenType));
            }

            // Firstly the purchased card
            buyer.addCard(boughtCard);
            game.removeCardFromTable(boughtCard);

            // Then the new card
            if(newCard != null){
                game.addNewCardToTable(newCard);
            }

            //Lastly tokens
            for(TokenType tokenType : EnumSet.allOf(TokenType.class)){
                buyer.setTokens(tokenType, tokens.get(tokenType));
            }
            // TODO Update the view via `gameActivity` or other objects given in constructor

            // If this message pertains to me, it means I requested it, indicating that I have taken my action during my turn.
            // Therefore, I need to end my turn.
            // Perhaps it was not the best decision to require the user to manually end their turn.
            // The server should handle this automatically.
            BuyingRevealedCardsController.this.endTurnController.endTurn();

            gameActivity.updateScoreBoard();
            gameActivity.updateTokenNumber();
            gameActivity.updateCards();

            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Error while buying from revealed: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Error while buying from revealed: " + errorResponse.data.error);
            return null;
        }

    }

}
