package com.example.splendormobilegame.model;

import android.content.Context;

import java.util.UUID;

public class Model {


    private static UUID userUuid;
    private static Room room;
    private static Context context;


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

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Model.context = context;
    }
}
