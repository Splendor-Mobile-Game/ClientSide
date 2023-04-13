package com.example.splendormobilegame.model;

import android.app.Activity;

import java.util.UUID;

public class Model {


    private static UUID userUuid;
    private static Room room;
    private static Activity activity;

    // Currently when user is joining the room and they pass good code we need to store it and show in the room, because server does not send this code to us then
    private static String enteredRoomCode;


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

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        Model.activity = activity;
    }


    public static String getEnteredRoomCode() {
        return enteredRoomCode;
    }

    public static void setEnteredRoomCode(String enteredRoomCode) {
        Model.enteredRoomCode = enteredRoomCode;
    }
}
