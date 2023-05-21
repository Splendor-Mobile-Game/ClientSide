package com.example.splendormobilegame.model;

import java.util.Objects;
import java.util.UUID;

public class Noble {

    //Noble -> Arystokrata, Magnat

    private final UUID uuid;
    private final int emeraldCost; // Green
    private final int sapphireCost;  // Blue
    private final int rubyCost;  // Red
    private final int diamondCost;  // White
    private final int onyxCost;  // Black
    private final int graphicsID;


    public Noble(UUID uuid, int emeraldCost, int sapphireCost, int rubyCost, int diamondCost, int onyxCost, int graphicsID) {
        this.uuid = uuid;
        this.emeraldCost = emeraldCost;
        this.sapphireCost = sapphireCost;
        this.rubyCost = rubyCost;
        this.diamondCost = diamondCost;
        this.onyxCost = onyxCost;
        this.graphicsID = graphicsID;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getPoints() {
        return 3;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Noble noble = (Noble) o;
        return emeraldCost == noble.emeraldCost && sapphireCost == noble.sapphireCost && rubyCost == noble.rubyCost && diamondCost == noble.diamondCost && onyxCost == noble.onyxCost && uuid.equals(noble.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, emeraldCost, sapphireCost, rubyCost, diamondCost, onyxCost);
    }
}
