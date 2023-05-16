package com.example.splendormobilegame.model;

import android.app.Activity;

import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.NotInitializedException;

import java.net.URI;
import java.util.UUID;

public class Model {

    private static Model instance;

    private Model() {

    }

    public static void initialize() {
        instance = new Model();
    }

    public static Model getInstance() {
        if (instance == null) {
            throw new NotInitializedException("Please, run first `initialize` method before using this class!");
        }
        return instance;
    }

    private UUID userUuid;
    private Room room;

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
