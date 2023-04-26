package com.example.splendormobilegame.activities.WaitingRoom;

import android.util.Log;
import android.widget.Toast;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.R;
import com.example.splendormobilegame.activities.GameActivity.GameActivity;
import com.example.splendormobilegame.model.Card;
import com.example.splendormobilegame.model.Game;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.Noble;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.game.enums.CardTier;
import com.github.splendor_mobile_game.game.enums.TokenType;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.StartGame;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StartGameController extends Controller {

    protected StartGameController(CustomAppCompatActivity activity) {
        super(activity);
    }

    public void startGame() {
        // TODO check if there is more than 1 player

        //activity.showToast(message);
        //Toast.makeText(WaitingRoomActivity.this, R.string.players_warning, Toast.LENGTH_SHORT).show();

        // if everything is fine
        this.sendRequestToStartGame();
    }

    public void sendRequestToStartGame() {
        StartGame.UserDTO user = new StartGame.UserDTO(Model.getUserUuid());
        StartGame.RoomDTO room = new StartGame.RoomDTO(Model.getRoom().getUuid());
        StartGame.DataDTO data = new StartGame.DataDTO(user,room);
        UserMessage message = new UserMessage(UUID.randomUUID(), UserRequestType.START_GAME,data);

        CustomWebSocketClient.getInstance().send(message);
    }


    public class StartGameResponse extends UserReaction{
        @Override
        public UserMessage react(ServerMessage serverMessage){
            Log.i("UserReaction","Entered StartGameResponse");

            //Get the data form the request
            StartGame.ResponseData responseData = (StartGame.ResponseData) ReactionUtils.getResponseData(
                    serverMessage,StartGame.ResponseData.class
            );


            HashMap<TokenType, Integer> tokensOnTable = new HashMap<TokenType,Integer>(){{
                put(TokenType.RUBY,responseData.tokens.ruby);
                put(TokenType.EMERALD,responseData.tokens.emerald);
                put(TokenType.SAPPHIRE,responseData.tokens.sapphire);
                put(TokenType.DIAMOND,responseData.tokens.diamond);
                put(TokenType.ONYX,responseData.tokens.onyx);
                put(TokenType.GOLD_JOKER,responseData.tokens.gold);
            }};

            ArrayList<Noble> noblesOnTable =  new ArrayList<Noble>();
            for(StartGame.NobleDataResponse noble : responseData.nobles){
                noblesOnTable.add(new Noble(
                        noble.uuid,
                        noble.emeraldMinesRequired,
                        noble.sapphireMinesRequired,
                        noble.rubyMinesRequired,
                        noble.diamondMinesRequired,
                        noble.onyxMinesRequired
                ));
            }

            List<TokenType> tokenTypes = new ArrayList<>(EnumSet.allOf(TokenType.class));
            HashMap<CardTier,ArrayList<Card>> cardsOnTable = new HashMap<CardTier,ArrayList<Card>>(){{
                put(CardTier.LEVEL_1,new ArrayList<Card>());
                put(CardTier.LEVEL_2,new ArrayList<Card>());
                put(CardTier.LEVEL_3,new ArrayList<Card>());
            }};

            ArrayList<ArrayList<StartGame.MinesCardDataResponse>> minesCardDataResponses = new ArrayList<>(
                    Arrays.asList(
                            responseData.firstLevelMinesCards,
                            responseData.secondLevelMinesCards,
                            responseData.thirdLevelMinesCards
                    ));
            for(int i=0;i<minesCardDataResponses.size();i++){
                for(StartGame.MinesCardDataResponse mine : minesCardDataResponses.get(i)){
                    cardsOnTable.get(CardTier.fromInt(i+1)).add(new Card(
                            mine.uuid,
                            CardTier.fromInt(i+1),
                            mine.prestige,
                            mine.emeraldTokensRequired,
                            mine.sapphireTokensRequired,
                            mine.rubyTokensRequired,
                            mine.diamondTokensRequired,
                            mine.onyxTokensRequired,
                            EnumSet.allOf(TokenType.class)
                                    .stream()
                                    .filter(tokenType -> tokenType.color == mine.color)
                                    .findFirst()
                                    .orElse(null)
                    ));
                }
            }

            //Update the model
            Model.getRoom().setGame(new Game(tokensOnTable,noblesOnTable,cardsOnTable));

            activity.changeActivity(GameActivity.class);

            // Nice looking stuff
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            // Return null if you don't want to send anything to the server
            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse){
            activity.showToast("Cannot start game: "+errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse){
            activity.showToast("Cannot start game: "+errorResponse.data.error);
            return null;
        }
    }
}



















