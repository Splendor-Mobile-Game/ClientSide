package com.example.splendormobilegame;

import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;

public abstract class Controller {
    protected CustomAppCompatActivity activity;
    protected CustomWebSocketClient customWebSocketClient;
    protected Model model;

    protected Controller(CustomAppCompatActivity activity, CustomWebSocketClient customWebSocketClient, Model model) {
        this.activity = activity;
        this.customWebSocketClient = customWebSocketClient;
        this.model = model;
    }
}
