package com.example.splendormobilegame.model;

import java.util.Objects;

public class ReservedCard {

    private final Card card;
    private final User user;
    private boolean visible;


    public ReservedCard(Card card, User user, boolean visible) {
        this.card = card;
        this.user = user;
        this.visible = visible;
    }

    public Card getCard() {
        return card;
    }

    public User getUser() {
        return user;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservedCard reservedCard = (ReservedCard) o;
        return card.equals(reservedCard.card) && user.equals(reservedCard.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, user);
    }
}
