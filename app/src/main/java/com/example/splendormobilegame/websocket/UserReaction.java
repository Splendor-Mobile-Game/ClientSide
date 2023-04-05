package com.example.splendormobilegame.websocket;

import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;

public abstract class UserReaction {
    protected ServerMessage serverMessage;
    protected Model model;

    public UserReaction(ServerMessage serverMessage, Model model) {
        this.serverMessage = serverMessage;
        this.model = model;
    }

    public abstract UserMessage react();

}
