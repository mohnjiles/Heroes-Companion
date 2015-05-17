package com.example.jt.heroes.models;

import java.io.Serializable;

/**
 * Created by JT on 3/12/2015.
 */
public class Spell implements Serializable {
    public int id;
    public int heroId;
    public String name;
    public String description;
    public double cooldown;
    public double cost;
    public String letter;
    private static final int serialVersionUID = 7331;

    public int getId() {
        return id;
    }

    public int getHeroId() {
        return heroId;
    }

    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String type) {
        this.letter = type;
    }
}
