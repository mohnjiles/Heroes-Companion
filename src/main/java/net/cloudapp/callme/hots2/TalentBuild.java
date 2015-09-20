package net.cloudapp.callme.hots2;

import net.cloudapp.callme.hots2.models.Hero;
import net.cloudapp.callme.hots2.models.Talent;

import java.util.List;

/**
 * Created by JT on 6/3/2015.
 */
public class TalentBuild {
    public List<Talent> talentList;
    public Hero hero;
    public String buildName;


    public List<Talent> getTalentList() {
        return talentList;
    }

    public void setTalentList(List<Talent> talentList) {
        this.talentList = talentList;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }
}
