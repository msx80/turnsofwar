package com.github.msx80.turnsofwar.game;
// A simple interface for the special 'onDie' logic
interface DeathEffect {
    void trigger(int x, int y);
}