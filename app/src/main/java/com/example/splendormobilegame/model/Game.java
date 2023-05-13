package com.example.splendormobilegame.model;


import com.github.splendor_mobile_game.game.enums.CardTier;
import com.github.splendor_mobile_game.game.enums.TokenType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.EndTurn;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Game {

    private final HashMap<TokenType, Integer> tokensOnTable;
    private final ArrayList<Noble> noblesOnTable;
    private final ArrayList<ReservedCard> reservedCards;
    private final HashMap<CardTier, ArrayList<Card>> cardsOnTable;
    private UUID whosTurn;
    private ArrayList<EndTurn.PlayerDataResponse> playerRanking;


    public Game(HashMap<TokenType, Integer> tokensOnTable, ArrayList<Noble> noblesOnTable, HashMap<CardTier, ArrayList<Card>> cardsOnTable, UUID firstPlayer) {
        this.tokensOnTable = tokensOnTable;
        this.noblesOnTable = noblesOnTable;
        this.cardsOnTable = cardsOnTable;
        this.reservedCards = new ArrayList<>();
        this.whosTurn = firstPlayer;
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

    public void removeReservedCard(UUID userUuid, UUID cardUuid) {
        for(ReservedCard card : reservedCards){
            if(cardUuid.equals(card.getCard().getUuid()) && userUuid.equals(card.getUser().getUuid()))
            {
                reservedCards.remove(card);
                break;
            }
        }
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

    public void removeCardFromTable(Card card){
        cardsOnTable.get(card.getCardTier()).remove(card);
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
    public Integer getTokenValue(TokenType tokenType) {
        return tokensOnTable.get(tokenType);
    }
    public UUID getWhosTurn() {
        return whosTurn;
    }

    public void setWhosTurn(UUID whosTurn) {
        this.whosTurn = whosTurn;
    }

    public ArrayList<EndTurn.PlayerDataResponse> getPlayerRanking(){ return this.playerRanking; }

    public void setPlayerRanking(ArrayList<EndTurn.PlayerDataResponse> currentPlayerRanking){ this.playerRanking = currentPlayerRanking; }


}
