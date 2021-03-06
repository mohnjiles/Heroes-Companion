package net.cloudapp.callme.hots3.models;

import java.io.Serializable;

/**
 * Created by JT on 3/14/2015.
 */
public class Talent implements Serializable {

    private int id;
    private int heroId;
    private int tier;
    private String name;
    private String description;
    private static final int serialVersionUID = 1333337;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeroId() {
        return heroId;
    }

    public void setHeroId(int heroId) {
        this.heroId = heroId;
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

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }
}
