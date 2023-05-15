package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;
import com.github.splendor_mobile_game.websocket.handlers.reactions.EndTurn;
import com.example.splendormobilegame.model.Game;
import com.example.splendormobilegame.model.Model;

import java.util.UUID;

public class GameEndingController<T extends GameActivity> extends Controller {

    private T gameActivity;
    private GameEndedMessageHandler gameEndedMessageHandler;

    protected GameEndingController(T activity, CustomWebSocketClient customWebSocketClient, Model model) {
        super(activity, customWebSocketClient, model);
        this.gameActivity = activity;
        this.gameEndedMessageHandler = new GameEndedMessageHandler();
    }


    public GameEndedMessageHandler getGameEndedMessageHandler() {
        return gameEndedMessageHandler;
    }

    public class GameEndedMessageHandler extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered GameEndedMessageHandler react method");


            EndTurn.ResponseDataEndGame responseDataEndGame = (EndTurn.ResponseDataEndGame) ReactionUtils.getResponseData(serverMessage, EndTurn.ResponseDataEndGame.class);
            model.getRoom().getGame().setPlayerRanking(responseDataEndGame.playerRanking);




            // TODO Update the view via `gameActivity` or other objects given in constructor

            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Error while ending the game: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Error while ending the game: " + errorResponse.data.error);
            return null;
        }

    }

}
