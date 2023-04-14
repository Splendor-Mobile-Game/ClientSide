package com.example.splendormobilegame.websocket;

import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

public abstract class UserReaction {

    public UserReaction() {

    }

    public abstract UserMessage react(ServerMessage serverMessage);

    public abstract UserMessage onFailure(ErrorResponse errorResponse);

    public abstract UserMessage onError(ErrorResponse errorResponse);

}
