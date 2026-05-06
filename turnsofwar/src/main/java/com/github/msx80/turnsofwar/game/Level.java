package com.github.msx80.turnsofwar.game;

import java.util.ArrayList;
import java.util.List;

public class Level {
    public int mx, my;
    public String[] intro;
    public List<UnitSpawn> good = new ArrayList<>();
    public List<UnitSpawn> evil = new ArrayList<>();
    public List<TrapAction> traps = new ArrayList<>();
    public WinCondition win;
    public Runnable turner; // Logic called every turn (spawning, etc)

    public static class UnitSpawn {
        public Units type;
        public int x, y;
        public UnitSpawn(Units t, int x, int y) { this.type = t; this.x = x; this.y = y; }
    }
    
    public Level(int mx, int my, String[] intro, WinCondition win) {
        this.mx = mx;
        this.my = my;
        this.intro = intro;
        this.win = win;
    }
    
}