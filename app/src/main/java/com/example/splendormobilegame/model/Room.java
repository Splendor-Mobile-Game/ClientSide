package com.example.splendormobilegame.model;

import com.github.splendor_mobile_game.database.Database;
import com.github.splendor_mobile_game.websocket.utils.Log;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class Room {


    private final UUID uuid;
    private final String name;
    private final String enterCode;
    private User owner;
    private ArrayList<User> users = new ArrayList<>();

    private Game game;


    public Room(UUID uuid, String name, String enterCode, User owner) {
        this.uuid      = uuid;
        this.name      = name;
        this.enterCode = enterCode;
        this.owner     = owner;

        this.users.add(owner);
    }


    public int getPlayerCount() {
        return users.size();
    }

    public boolean isMember(User user) {
        return users.contains(user);
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getEnterCode() {
        return enterCode;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        if (getPlayerCount() >= 4) return;
        this.users.add(user);
    }

    public void removeUser(User user) {
        if (getPlayerCount() <= 1) return;
        this.users.remove(user);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    public User getUserByUuid(UUID uuid) {
        // TODO
        return null;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return uuid.equals(room.uuid) && name.equals(room.name) && enterCode.equals(room.enterCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, enterCode);
    }


}
