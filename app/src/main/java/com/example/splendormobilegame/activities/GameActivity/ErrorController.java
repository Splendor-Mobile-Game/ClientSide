package com.example.splendormobilegame.activities.GameActivity;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

public class ErrorController extends Controller {

    private errorMessageHandler errorMessageHandler;

    public ErrorController(CustomAppCompatActivity activity, CustomWebSocketClient customWebSocketClient, Model model) {
        super(activity, customWebSocketClient, model);
        this.errorMessageHandler = new errorMessageHandler();
    }

    public errorMessageHandler getErrorMessageHandler() {
        return errorMessageHandler;
    }

    public class errorMessageHandler extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered errorMessageHandler react method");
            activity.showToast("Error");
            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Error: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Error: " + errorResponse.data.error);
            return null;
        }
    }
}
