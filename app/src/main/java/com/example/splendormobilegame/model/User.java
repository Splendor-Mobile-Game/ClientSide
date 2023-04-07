package com.example.splendormobilegame.model;

import com.github.splendor_mobile_game.game.enums.TokenType;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class User {

    private final String name;

    private UUID uuid;

    private HashMap<TokenType, Integer> tokens;

    private ArrayList<Card> cards;

    private ArrayList<Noble> nobles;


    public User(UUID uuid, String name) {
        this.uuid   = uuid;
        this.name   = name;
        this.tokens = new HashMap<>();
        this.cards  = new ArrayList<>();
        this.nobles = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


    // TODO
    // addToken, removeToken
    // addCard, removeCard
    // addNoble, removeNoble

    public HashMap<TokenType, Integer> getTokens() {
        return tokens;
    }

    public void setTokens(HashMap<TokenType, Integer> tokens) {
        this.tokens = tokens;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Noble> getNobles() {
        return nobles;
    }

    public void setNobles(ArrayList<Noble> nobles) {
        this.nobles = nobles;
    }


    // TODO get count of all user's tokens
    public int getAllTokensCount() {
        return 0;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uuid.equals(user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uuid);
    }

}
