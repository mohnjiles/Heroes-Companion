package com.example.jt.heroes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jt.heroes.models.Hero;
import com.example.jt.heroes.models.Spell;
import com.example.jt.heroes.models.Talent;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JT on 3/14/2015.
 */
public class HeroDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "heroes-magic";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public HeroDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void addHero(Hero hero, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put("id", hero.getId());
        cv.put("name", hero.getName());
        cv.put("title", hero.getTitle());
        cv.put("role", hero.getRole());
        cv.put("franchise", hero.getFranchise());
        cv.put("type", hero.getType());
        cv.put("hp", hero.getHp());
        cv.put("hp_regen", hero.getHpRegen());
        cv.put("energy", hero.getMana());
        cv.put("energy_regen", hero.getManaRegen());
        cv.put("attack_speed", hero.getAttackSpeed());
        cv.put("attack_damage", hero.getAttackDamage());
        cv.put("hp_per_level", hero.getHpPerLevel());
        cv.put("hp_regen_per_level", hero.getHpRegenPerLevel());
        cv.put("damage_per_level", hero.getAttackDamagePerLevel());
        cv.put("energy_per_level", hero.getManaPerLevel());
        cv.put("energy_regen_per_level", hero.getManaRegenPerLevel());
        cv.put("lore", hero.getLore());
        cv.put("description", hero.getDescription());

        db.insert("heroes", null, cv);

    }

    public void addSpell(Spell spell, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put("hero_id", spell.getHeroId());
        values.put("name", spell.getName());
        values.put("cost", spell.getCost());
        values.put("cooldown", spell.getCooldown());
        values.put("description", spell.getDescription());
        values.put("hotkey", spell.getLetter());

        db.insert("spells", null, values);

    }

    public void addTalent(Talent talent, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put("hero_id", talent.getHeroId());
        values.put("tier", talent.getTalentTier());
        values.put("name", talent.getName());
        values.put("description", talent.getDescription());

        db.insert("talents", null, values);
    }

    public void insertDescriptions(String description, int heroId, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();


        cv.put("description", description);
        db.update("heroes", cv, "id = ?", new String[]{String.valueOf(heroId)});

        Log.d("Insert Descriptions", String.format("Inserted description for hero %d: %s", heroId, description));
    }

    public int getHeroIdFromName(String heroName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM heroes WHERE name = ?", new String[]{heroName});

        if (cursor.moveToFirst()) {
            int value = Integer.parseInt(cursor.getString(0));
            cursor.close();
            return value;
        }

        cursor.close();
        return 0;
    }

    public Hero getHeroById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM heroes WHERE id = ?", new String[]{String.valueOf(id)});
        
        if (cursor.moveToFirst()) {
            Hero hero = new Hero();
            hero.setId(Integer.parseInt(cursor.getString(0)));
            hero.setName(cursor.getString(1));
            hero.setTitle(cursor.getString(2));
            hero.setRole(cursor.getString(3));
            hero.setFranchise(cursor.getString(4));
            hero.setType(cursor.getString(5));
            hero.setHp(Integer.parseInt(cursor.getString(6)));
            hero.setHpRegen(Double.parseDouble(cursor.getString(7)));
            hero.setMana(Integer.parseInt(cursor.getString(8)));
            hero.setManaRegen(Double.parseDouble(cursor.getString(9)));
//                hero.setSpeed(Double.parseDouble(cursor.getString(10)));
            hero.setAttackSpeed(Double.parseDouble(cursor.getString(11)));
//                hero.setAttackRange(Double.parseDouble(cursor.getString(12)));
            hero.setAttackDamage(Integer.parseInt(cursor.getString(13)));
            hero.setHpPerLevel(Integer.parseInt(cursor.getString(14)));
            hero.setHpRegenPerLevel(Double.parseDouble(cursor.getString(15)));
            hero.setAttackDamagePerLevel(Double.parseDouble(cursor.getString(16)));
            hero.setManaPerLevel(Integer.parseInt(cursor.getString(17)));
            hero.setManaRegenPerLevel(Double.parseDouble(cursor.getString(18)));
            hero.setLore(cursor.getString(19));
            hero.setDescription(cursor.getString(20));
            hero.setIconResourceId(Utils.getResourceIdByName(context, hero.getName()));

            Cursor spellCursor = db.rawQuery("SELECT * FROM spells WHERE hero_id = ?",
                    new String[]{String.valueOf(hero.getId())});
            List<Spell> spellList = new ArrayList<>();
            if (spellCursor.moveToFirst()) {
                do {
                    Spell spell = new Spell();
                    spell.setHeroId(Integer.valueOf(spellCursor.getString(1)));
                    spell.setName(spellCursor.getString(2));
                    spell.setCost(Double.valueOf(spellCursor.getString(3)) + 0);
                    spell.setCooldown(Double.valueOf(spellCursor.getString(4)) + 0);
                    spell.setDescription(spellCursor.getString(5));
                    spell.setLetter(spellCursor.getString(6));

                    spellList.add(spell);
                } while (spellCursor.moveToNext());

                spellCursor.close();
            }

            hero.setSpells(spellList);

            Cursor talentCursor = db.rawQuery("SELECT * FROM talents WHERE hero_id = ?",
                    new String[]{String.valueOf(hero.getId())});

            List<Talent> talentList = new ArrayList<>();

            if (talentCursor.moveToFirst()) {
                do {
                    Talent talent = new Talent();
                    talent.setHeroId(Integer.valueOf(talentCursor.getString(1)));
                    talent.setTalentTier(Integer.valueOf(talentCursor.getString(2)));
                    talent.setName(talentCursor.getString(3));
                    talent.setDescription(talentCursor.getString(4));

                    talentList.add(talent);
                } while (talentCursor.moveToNext());

                talentCursor.close();
            }

            hero.setTalents(talentList);

            return hero;
        }
        
        return null;
    }

    public List<Hero> getAllHeroes() {
        List<Hero> listHeroes = new ArrayList<Hero>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor allHeroCursor = db.rawQuery("SELECT * FROM heroes ORDER BY name", null);

        if (allHeroCursor.moveToFirst()) {
            do {
                Hero hero = new Hero();
                hero.setId(Integer.parseInt(allHeroCursor.getString(0)));
                hero.setName(allHeroCursor.getString(1));
                hero.setTitle(allHeroCursor.getString(2));
                hero.setRole(allHeroCursor.getString(3));
                hero.setFranchise(allHeroCursor.getString(4));
                hero.setType(allHeroCursor.getString(5));
                hero.setHp(Integer.parseInt(allHeroCursor.getString(6)));
                hero.setHpRegen(Double.parseDouble(allHeroCursor.getString(7)));
                hero.setMana(Integer.parseInt(allHeroCursor.getString(8)));
                hero.setManaRegen(Double.parseDouble(allHeroCursor.getString(9)));
//                hero.setSpeed(Double.parseDouble(allHeroCursor.getString(10)));
                hero.setAttackSpeed(Double.parseDouble(allHeroCursor.getString(11)));
//                hero.setAttackRange(Double.parseDouble(allHeroCursor.getString(12)));
                hero.setAttackDamage(Integer.parseInt(allHeroCursor.getString(13)));
                hero.setHpPerLevel(Integer.parseInt(allHeroCursor.getString(14)));
                hero.setHpRegenPerLevel(Double.parseDouble(allHeroCursor.getString(15)));
                hero.setAttackDamagePerLevel(Double.parseDouble(allHeroCursor.getString(16)));
                hero.setManaPerLevel(Integer.parseInt(allHeroCursor.getString(17)));
                hero.setManaRegenPerLevel(Double.parseDouble(allHeroCursor.getString(18)));
                hero.setLore(allHeroCursor.getString(19));
                hero.setDescription(allHeroCursor.getString(20));
                hero.setIconResourceId(Utils.getResourceIdByName(context, hero.getName()));

                Cursor spellCursor = db.rawQuery("SELECT * FROM spells WHERE hero_id = ?",
                        new String[]{String.valueOf(hero.getId())});
                List<Spell> spellList = new ArrayList<>();
                if (spellCursor.moveToFirst()) {
                    do {
                        Spell spell = new Spell();
                        spell.setHeroId(Integer.valueOf(spellCursor.getString(1)));
                        spell.setName(spellCursor.getString(2));
                        spell.setCost(Double.valueOf(spellCursor.getString(3)) + 0);
                        spell.setCooldown(Double.valueOf(spellCursor.getString(4)) + 0);
                        spell.setDescription(spellCursor.getString(5));
                        spell.setLetter(spellCursor.getString(6));

                        spellList.add(spell);
                    } while (spellCursor.moveToNext());

                    spellCursor.close();
                }

                hero.setSpells(spellList);

                Cursor talentCursor = db.rawQuery("SELECT * FROM talents WHERE hero_id = ?",
                        new String[]{String.valueOf(hero.getId())});

                List<Talent> talentList = new ArrayList<>();

                if (talentCursor.moveToFirst()) {
                    do {
                        Talent talent = new Talent();
                        talent.setHeroId(Integer.valueOf(talentCursor.getString(1)));
                        talent.setTalentTier(Integer.valueOf(talentCursor.getString(2)));
                        talent.setName(talentCursor.getString(3));
                        talent.setDescription(talentCursor.getString(4));

                        talentList.add(talent);
                    } while (talentCursor.moveToNext());

                    talentCursor.close();
                }

                hero.setTalents(talentList);

                listHeroes.add(hero);
            } while (allHeroCursor.moveToNext());

            allHeroCursor.close();
        }

        return listHeroes;
    }
}
