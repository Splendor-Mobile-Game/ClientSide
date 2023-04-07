package com.example.splendormobilegame.model;


import com.github.splendor_mobile_game.game.enums.CardTier;
import com.github.splendor_mobile_game.game.enums.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Game {

    private HashMap<TokenType, Integer> tokensOnTable;

    private ArrayList<Noble> noblesOnTable;

    private ArrayList<ReservedCard> reservedCards;

    private HashMap<CardTier, ArrayList<Card>> cardsOnTable;

    private User whosTurn;




    public Game(HashMap<TokenType, Integer> tokensOnTable, ArrayList<Noble> noblesOnTable, HashMap<CardTier, ArrayList<Card>> cardsOnTable) {
        this.tokensOnTable = tokensOnTable;
        this.noblesOnTable = noblesOnTable;
        this.cardsOnTable  = cardsOnTable;
        this.reservedCards = new ArrayList<>();
    }



    public void removeTokens(User user, TokenType tokenType, int amount) {
        int onTable = tokensOnTable.get(tokenType);
        if (amount > onTable) return; // Not enough tokens on table

        tokensOnTable.replace(tokenType, onTable - amount);
        // TODO remove tokens from User's property
    }


    public void addTokens(User user, TokenType tokenType, int amount) {
        int onTable = tokensOnTable.get(tokenType);

        tokensOnTable.replace(tokenType, onTable + amount);
        // TODO add tokens to User's property
    }


    public void reserveCard(User user, ReservedCard reservedCard) {
        reservedCards.add(reservedCard);
    }

    public Card getCardByUuid(UUID uuid) {
        // TODO
        return null;
    }

    public Noble getNobleByUuid(UUID uuid) {
        // TODO
        return null;
    }

    public void addNewCardToTable(Card card) {
        // TODO
    }

    public void transferNobleToUser(Noble noble, User user) {
        // TODO
    }

    public User getWhosTurn() {
        return whosTurn;
    }

    public void setWhosTurn(User whosTurn) {
        this.whosTurn = whosTurn;
    }



}
