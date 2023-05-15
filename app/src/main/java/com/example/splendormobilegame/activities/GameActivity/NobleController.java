package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.model.Game;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.Noble;
import com.example.splendormobilegame.model.Room;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.reactions.EndTurn;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;


public class NobleController<T extends GameActivity>  extends Controller {

    private T gameActivity;
    private NobleReceivedMessageHandler nobleReceivedResponse;



    public NobleController(CustomAppCompatActivity activity) {
        super(activity);
        this.nobleReceivedResponse = new NobleReceivedMessageHandler();
    }

    public NobleReceivedMessageHandler getNobleReceivedResponse() {
        return nobleReceivedResponse;
    }

    public class NobleReceivedMessageHandler extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered NobleReceivedResponse react method");
            EndTurn.ResponseDataNobleReceived responseData = (EndTurn.ResponseDataNobleReceived) ReactionUtils.getResponseData(
                    serverMessage, EndTurn.ResponseDataNobleReceived.class
            );


            Room room = Model.getRoom();
            Game game = room.getGame();
            User user = room.getUserByUuid(responseData.userUuid);
            Noble noble = game.getNobleByUuid(responseData.nobleUuid);

            game.transferNobleToUser(noble, user);

gameActivity.updateNobleCardsRecyclerView();
gameActivity.setNobleBackground(responseData.nobleUuid);
            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Error while receiving noble: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Error while receiving noble: " + errorResponse.data.error);
            return null;
        }

    }

}
