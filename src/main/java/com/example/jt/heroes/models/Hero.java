package com.example.jt.heroes.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JT on 3/12/2015.
 */
public class Hero implements Serializable {
    public int id;
    public String name;
    public String title;
    public String description;
    public String role;
    public String franchise;
    public String type;
    public String lore;
    public int hp;
    public int hpPerLevel;
    public double hpRegen;
    public double hpRegenPerLevel;
    public int mana;
    public int manaPerLevel;
    public double manaRegenPerLevel;
    public double manaRegen;
    public double attackSpeed;
    public int attackDamage;
    public double attackDamagePerLevel;
    public List<Spell> spells;
    public List<Talent> talents;
    public int iconResourceId;
    private static final int serialVersionUID = 1337;

    public List<Talent> getTalents() {
        return talents;
    }

    public void setTalents(List<Talent> talents) {
        this.talents = talents;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public double getManaRegenPerLevel() {
        return manaRegenPerLevel;
    }

    public void setManaRegenPerLevel(double manaRegenPerLevel) {
        this.manaRegenPerLevel = manaRegenPerLevel;
    }

    public double getManaRegen() {
        return manaRegen;
    }

    public void setManaRegen(double manaRegen) {
        this.manaRegen = manaRegen;
    }

    public String getTitle() {
        return title;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFranchise() {
        return franchise;
    }

    public void setFranchise(String franchise) {
        this.franchise = franchise;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }

    public int getId() {
        return id;
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

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHpPerLevel() {
        return hpPerLevel;
    }

    public void setHpPerLevel(int hpPerLevel) {
        this.hpPerLevel = hpPerLevel;
    }

    public double getHpRegen() {
        return hpRegen;
    }

    public void setHpRegen(double hpRegen) {
        this.hpRegen = hpRegen;
    }

    public double getHpRegenPerLevel() {
        return hpRegenPerLevel;
    }

    public void setHpRegenPerLevel(double hpRegenPerLevel) {
        this.hpRegenPerLevel = hpRegenPerLevel;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getManaPerLevel() {
        return manaPerLevel;
    }

    public void setManaPerLevel(int manaPerLevel) {
        this.manaPerLevel = manaPerLevel;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public double getAttackDamagePerLevel() {
        return attackDamagePerLevel;
    }

    public void setAttackDamagePerLevel(double attackDamagePerLevel) {
        this.attackDamagePerLevel = attackDamagePerLevel;
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public void setSpells(List<Spell> spells) {
        this.spells = spells;
    }
}
