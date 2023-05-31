package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
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
import com.github.splendor_mobile_game.websocket.handlers.reactions.GetTokens;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class TokensController<T extends GameActivity> extends Controller {
    private T gameActivity;
    private EndTurnController endTurnController;
    private GetTokensMessageHandler getTokensMessageHandler;

    public TokensController(T activity, CustomWebSocketClient customWebSocketClient, Model model, EndTurnController endTurnController) {
        super(activity, customWebSocketClient, model);
        this.gameActivity = activity;
        this.endTurnController = endTurnController;
        this.getTokensMessageHandler = new GetTokensMessageHandler();
    }

    public void getTokens(int red, int blue, int green, int black, int white, int redReturned, int blueReturned, int greenReturned, int blackReturned, int whiteReturned) {
        // Maybe you want to check something here
        // and then call the method to send the request
        this.sendRequest(red, blue, green, black, white, redReturned, blueReturned, greenReturned, blackReturned, whiteReturned);
    }

    private void sendRequest(int red, int blue, int green, int black, int white, int redReturned, int blueReturned, int greenReturned, int blackReturned, int whiteReturned) {
        GetTokens.TokensChangeDTO tokensTakenDTO = new GetTokens.TokensChangeDTO(0,0,0,0,0);
        GetTokens.TokensChangeDTO tokensReturnedDTO = new GetTokens.TokensChangeDTO(0,0,0,0,0);
        tokensTakenDTO.ruby=red;
        tokensReturnedDTO.ruby=redReturned;
        tokensTakenDTO.sapphire=blue;
        tokensReturnedDTO.sapphire=blueReturned;
        tokensTakenDTO.emerald= green;
        tokensReturnedDTO.emerald=greenReturned;
        tokensTakenDTO.onyx=black;
        tokensReturnedDTO.onyx=blackReturned;
        tokensTakenDTO.diamond=white;
        tokensReturnedDTO.diamond=whiteReturned;


        GetTokens.DataDTO dataDTO = new GetTokens.DataDTO(model.getUserUuid(), tokensTakenDTO, tokensReturnedDTO);

        UserMessage message = new UserMessage(UUID.randomUUID(), UserRequestType.GET_TOKENS, dataDTO);

        customWebSocketClient.send(message);
    }

    public GetTokensMessageHandler getGetTokensMessageHandler() {
        return getTokensMessageHandler;
    }

    public class GetTokensMessageHandler extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered GetTokensMessageHandler react method");

            // Get the data from the response
            GetTokens.ResponseData responseData = (GetTokens.ResponseData) ReactionUtils.getResponseData(
                    serverMessage, GetTokens.ResponseData.class
            );

            // Get the data about tokens change
            GetTokens.TokensChangeDTO tokensTakenDataResponse = responseData.data.tokensTakenDTO;
            GetTokens.TokensChangeDTO tokensReturnedDataResponse = responseData.data.tokensReturnedDTO;


            Map<TokenType, Integer> tokensTaken = new HashMap<TokenType, Integer>();

            tokensTaken.put(TokenType.RUBY, tokensTakenDataResponse.ruby);
            tokensTaken.put(TokenType.SAPPHIRE, tokensTakenDataResponse.sapphire);
            tokensTaken.put(TokenType.EMERALD, tokensTakenDataResponse.emerald);
            tokensTaken.put(TokenType.DIAMOND, tokensTakenDataResponse.diamond);
            tokensTaken.put(TokenType.ONYX, tokensTakenDataResponse.onyx);

            Map<TokenType, Integer> tokensReturned = new HashMap<TokenType, Integer>();
            tokensReturned.put(TokenType.RUBY, tokensReturnedDataResponse.ruby);
            tokensReturned.put(TokenType.SAPPHIRE, tokensReturnedDataResponse.sapphire);
            tokensReturned.put(TokenType.EMERALD, tokensReturnedDataResponse.emerald);
            tokensReturned.put(TokenType.DIAMOND, tokensReturnedDataResponse.diamond);
            tokensReturned.put(TokenType.ONYX, tokensReturnedDataResponse.onyx);

            // Update the model
            Room room = model.getRoom();
            Game game = room.getGame();
            User user = room.getUserByUuid(responseData.data.userUuid);


            //add tokens to user and remove tokens from the table

            for(Map.Entry<TokenType, Integer> set : tokensTaken.entrySet()) {
                Log.i("TEST",set.getKey()+": tokenTaken: "+set.getValue());
                user.addTokens(set.getKey(),set.getValue());
                game.removeTokens(set.getKey(),set.getValue());
            }
            for(Map.Entry<TokenType, Integer> set : tokensReturned.entrySet()) {
                Log.i("TEST",set.getKey()+": tokenReturned: "+set.getValue());
                user.removeTokens(set.getKey(),set.getValue());
                game.addTokens(set.getKey(),set.getValue());
            }

            if(Model.getInstance().getUserUuid().equals(user.getUuid())) {
                gameActivity.ChangeRightSide();
                gameActivity.ClearTokenPointsView();
            }
            // TODO Update the view
            // ...
            // this.gameActivity.updateScoreboard()
            // this.gameActivity.hideGettingTokensSideBar()
            // ...

            // If this message pertains to me, it means I requested it, indicating that I have taken my action during my turn.
            // Therefore, I need to end my turn.
            // Perhaps it was not the best decision to require the user to manually end their turn.
            // The server should handle this automatically.
            TokensController.this.endTurnController.endTurn();

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

