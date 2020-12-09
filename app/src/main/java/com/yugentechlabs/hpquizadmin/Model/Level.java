package com.yugentechlabs.hpquizadmin.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Level implements Serializable {

    public Level(){
        //just needed
    }

    public Level(ArrayList level1, int levelnum) {
        level = new ArrayList<String>();
        level=(ArrayList<String>)level1.clone();
        this.levelNumber = levelnum;
    }

    public ArrayList<String> getLevel() {
        return level;
    }

    public void setLevel(ArrayList<String> level) {
        this.level = level;
    }

    public int getLevelnum() {
        return levelNumber;
    }

    public void setLevelnum(int levelnum) {
        this.levelNumber = levelnum;
    }

    ArrayList<String> level;
    int levelNumber;


}