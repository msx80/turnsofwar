package com.github.msx80.turnsofwar.game;

import java.util.function.Supplier;

public class TileTrap implements TrapAction {
    public final int x, y;
    private final Supplier<Event> action;
    
    public TileTrap(int x, int y, Supplier<Event> action) {
        this.x = x;
        this.y = y;
        this.action = action;
    }
    @Override public Event trigger() { return action.get(); }
}