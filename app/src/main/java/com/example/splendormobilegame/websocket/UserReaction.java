package com.example.splendormobilegame.websocket;

import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

public abstract class UserReaction {
    protected ServerMessage serverMessage;

    public UserReaction(ServerMessage serverMessage) {
        this.serverMessage = serverMessage;
    }

    public abstract UserMessage react();
    public abstract UserMessage onFailure(ErrorResponse errorResponse);
    public abstract UserMessage onError(ErrorResponse errorResponse);

}
