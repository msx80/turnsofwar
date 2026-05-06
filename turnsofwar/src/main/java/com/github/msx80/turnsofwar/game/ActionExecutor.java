package com.github.msx80.turnsofwar.game;
@FunctionalInterface
public interface ActionExecutor {
    Event execute(Actions action, Unit u, int tx, int ty);
}