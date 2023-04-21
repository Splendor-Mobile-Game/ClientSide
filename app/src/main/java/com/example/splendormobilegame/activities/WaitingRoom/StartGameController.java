package com.example.splendormobilegame.activities.WaitingRoom;

import android.util.Log;
import android.widget.Toast;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.R;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

public class StartGameController extends Controller {

    protected StartGameController(CustomAppCompatActivity activity) {
        super(activity);
    }

    public void startGame() {
        // TODO check if there is more than 1 player
        // activity.showToast(message);
        // Toast.makeText(WaitingRoomActivity.this, R.string.players_warning, Toast.LENGTH_SHORT).show();

        // if everything is fine
        this.sendRequestToStartGame();
    }

    public void sendRequestToStartGame() {
        // TODO Compose message to the server
        // TODO Send request
    }


    public class StartGameResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered StartGameResponse react method");

            // TODO Initialize the entire model
            // TODO Move user to the GameActivity

            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Error while starting the game: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Error while starting the game: " + errorResponse.data.error);
            return null;
        }

    }
}
