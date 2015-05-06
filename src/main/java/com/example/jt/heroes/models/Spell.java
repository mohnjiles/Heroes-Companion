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
    public int cooldown;
    public int cost;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
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

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String type) {
        this.letter = type;
    }
}
