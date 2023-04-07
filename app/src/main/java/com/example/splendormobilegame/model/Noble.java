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



    public Noble(int emeraldCost, int sapphireCost, int rubyCost, int diamondCost, int onyxCost) {
        this.uuid         = UUID.randomUUID();
        this.emeraldCost  = emeraldCost;
        this.sapphireCost = sapphireCost;
        this.rubyCost     = rubyCost;
        this.diamondCost  = diamondCost;
        this.onyxCost     = onyxCost;
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
