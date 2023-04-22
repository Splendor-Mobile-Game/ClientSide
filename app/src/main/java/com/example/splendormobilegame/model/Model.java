package com.example.splendormobilegame.model;

import android.app.Activity;

import java.util.UUID;

public class Model {


    private static UUID userUuid;
    private static Room room;

    public static UUID getUserUuid() {
        return userUuid;
    }

    public static void setUserUuid(UUID userUuid) {
        Model.userUuid = userUuid;
    }

    public static Room getRoom() {
        return room;
    }

    public static void setRoom(Room room) {
        Model.room = room;
    }
}
