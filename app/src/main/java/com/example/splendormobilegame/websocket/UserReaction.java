package com.example.splendormobilegame.websocket;

import com.example.splendormobilegame.model.Model;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;

public abstract class UserReaction {
    protected ServerMessage serverMessage;

    public UserReaction(ServerMessage serverMessage) {
        this.serverMessage = serverMessage;
    }

    public abstract UserMessage react();

}
