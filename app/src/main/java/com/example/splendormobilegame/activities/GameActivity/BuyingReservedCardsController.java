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
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;
import com.github.splendor_mobile_game.websocket.handlers.reactions.BuyReservedMine;

import java.util.EnumMap;
import java.util.EnumSet;
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
        BuyReservedMine.UserDTO userUuidDTO = new BuyReservedMine.UserDTO(Model.getUserUuid());
        BuyReservedMine.CardDTO cardUuidDTO = new BuyReservedMine.CardDTO(cardUuid);
        BuyReservedMine.DataDTO dataDTO = new BuyReservedMine.DataDTO(userUuidDTO,cardUuidDTO);
        UserMessage message = new UserMessage(UUID.randomUUID(), UserRequestType.BUY_RESERVED_MINE,dataDTO);

        CustomWebSocketClient.getInstance().send(message);
    }

    public BuyReservedCardMessageHandler getBuyReservedCardMessageHandler() {
        return buyReservedCardMessageHandler;
    }

    public class BuyReservedCardMessageHandler extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered BuyReservedCardMessageHandler react method");

            //Get the data from the request
            BuyReservedMine.ResponseData responseData = (BuyReservedMine.ResponseData) ReactionUtils.getResponseData(
                    serverMessage,BuyReservedMine.ResponseData.class
            );

            BuyReservedMine.TokensDataResponse tokensDataResponse = responseData.buyer.tokens;
            EnumMap<TokenType,Integer> tokens = new EnumMap<TokenType,Integer>(TokenType.class);
            tokens.put(TokenType.RUBY,tokensDataResponse.ruby);
            tokens.put(TokenType.EMERALD,tokensDataResponse.emerald);
            tokens.put(TokenType.SAPPHIRE,tokensDataResponse.sapphire);
            tokens.put(TokenType.DIAMOND,tokensDataResponse.diamond);
            tokens.put(TokenType.ONYX,tokensDataResponse.onyx);
            tokens.put(TokenType.GOLD_JOKER,tokensDataResponse.gold);

            // Update the model
            Room room = Model.getRoom();
            Game game = room.getGame();
            User buyer = room.getUserByUuid(responseData.buyer.userUuid);
            Card boughtCard = game.getCardByUuid(responseData.buyer.cardUuid);


            buyer.addCard(boughtCard);
            game.removeReservedCard(buyer.getUuid(), boughtCard.getUuid());

            for(TokenType tokenType : EnumSet.allOf(TokenType.class)){
                buyer.setTokens(tokenType, tokens.get(tokenType));
            }
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
