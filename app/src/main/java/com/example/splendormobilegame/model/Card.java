package com.example.splendormobilegame.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.splendormobilegame.R;
import com.github.splendor_mobile_game.game.enums.CardTier;
import com.github.splendor_mobile_game.game.enums.TokenType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Card {

    private final UUID uuid;
    private final CardTier cardTier;
    private final TokenType bonusToken;
    private final int points;
    private final int emeraldCost;
    private final int sapphireCost;
    private final int rubyCost;
    private final int diamondCost;
    private final int onyxCost;
    private final int graphicsID;


    public Card(UUID uuid, CardTier cardTier, int points, int emeraldCost, int sapphireCost, int rubyCost, int diamondCost, int onyxCost, TokenType token, int cardID) {
        this.uuid = uuid;
        this.cardTier = cardTier;
        this.points = points;
        this.emeraldCost = emeraldCost;
        this.sapphireCost = sapphireCost;
        this.rubyCost = rubyCost;
        this.diamondCost = diamondCost;
        this.onyxCost = onyxCost;
        this.bonusToken = token;
        int numberOfCardImages = Arrays.stream(R.drawable.class.getFields()).map(Field::getName).filter(name -> name.contains("cards_bg")).collect(Collectors.toList()).size();
        this.graphicsID = cardID % numberOfCardImages + 1;
    }

    public UUID getUuid() {
        return uuid;
    }

    public CardTier getCardTier() {
        return cardTier;
    }


    public TokenType getBonusToken() {
        return bonusToken;
    }

    public int getPoints() {
        return points;
    }

    public int getEmeraldCost() {
        return emeraldCost;
    }

    public int getSapphireCost() {
        return sapphireCost;
    }

    public int getRubyCost() {
        return rubyCost;
    }

    public int getDiamondCost() {
        return diamondCost;
    }

    public int getOnyxCost() {
        return onyxCost;
    }

    public int getGraphicsID() { return graphicsID; }


    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("%s %d %d %d %d %d %d %s", cardTier.toString(), points, emeraldCost, sapphireCost, rubyCost, diamondCost, onyxCost, bonusToken.toString());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return points == card.points && emeraldCost == card.emeraldCost && sapphireCost == card.sapphireCost && rubyCost == card.rubyCost && diamondCost == card.diamondCost && onyxCost == card.onyxCost && uuid.equals(card.uuid) && cardTier == card.cardTier && bonusToken == card.bonusToken;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, cardTier, bonusToken, points, emeraldCost, sapphireCost, rubyCost, diamondCost, onyxCost);
    }
}
