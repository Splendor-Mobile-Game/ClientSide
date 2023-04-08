package com.example.splendormobilegame.model;


import com.github.splendor_mobile_game.game.enums.CardTier;
import com.github.splendor_mobile_game.game.enums.TokenType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Game {

    private final HashMap<TokenType, Integer> tokensOnTable;
    private final ArrayList<Noble> noblesOnTable;
    private final ArrayList<ReservedCard> reservedCards;
    private final HashMap<CardTier, ArrayList<Card>> cardsOnTable;
    private User whosTurn;




    public Game(HashMap<TokenType, Integer> tokensOnTable, ArrayList<Noble> noblesOnTable, HashMap<CardTier, ArrayList<Card>> cardsOnTable) {
        this.tokensOnTable = tokensOnTable;
        this.noblesOnTable = noblesOnTable;
        this.cardsOnTable  = cardsOnTable;
        this.reservedCards = new ArrayList<>();
    }



    public void removeTokens(TokenType tokenType, int amount) {
        if (tokensOnTable.get(tokenType) == null) return;
        int onTable = tokensOnTable.get(tokenType);
        if (amount > onTable) return; // Not enough tokens on table

        tokensOnTable.replace(tokenType, onTable - amount);
    }


    public void addTokens(TokenType tokenType, int amount) {
        int onTable = tokensOnTable.get(tokenType);
        tokensOnTable.replace(tokenType, onTable + amount);
    }


    public void reserveCard(User user, ReservedCard reservedCard) {
        reservedCards.add(reservedCard);
    }

    public Card getCardByUuid(UUID uuid) {
        for (ArrayList<Card> tierCards : cardsOnTable.values())
            for (Card card : tierCards) {
                if (card.getUuid().equals(uuid))
                    return card;
            }
        return null;
    }

    public Noble getNobleByUuid(UUID uuid) {
        for (Noble noble : noblesOnTable) {
            if (noble.getUuid().equals(uuid))
                return noble;
        }
        return null;
    }


    public void addNewCardToTable(Card card) {
        ArrayList<Card> cards = cardsOnTable.get(card.getCardTier());
        if (cards == null) return;
        if (cards.contains(card)) return;
        cards.add(card);
        cardsOnTable.replace(card.getCardTier(), cards);
    }

    public void transferNobleToUser(Noble noble, User user) {
        if (!noblesOnTable.contains(noble)) return;
        noblesOnTable.remove(noble);
        user.addNoble(noble);
    }

    public void transferTokensToUser(TokenType tokenType, int amount, User user) {
        removeTokens(tokenType, amount);
        user.addTokens(tokenType, amount);
    }

    public void transferTokensFromUser(TokenType tokenType, int amount, User user) {
        addTokens(tokenType, amount);
        user.removeTokens(tokenType, amount);
    }

    public User getWhosTurn() {
        return whosTurn;
    }

    public void setWhosTurn(User whosTurn) {
        this.whosTurn = whosTurn;
    }



}
