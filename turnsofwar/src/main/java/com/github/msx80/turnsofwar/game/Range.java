package com.github.msx80.turnsofwar.game;
public class Range {
    public int min;
    public int max;

    // Constructor
    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    // Getters
    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    // Setters
    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    // Optional: Utility method to get a random value within this range
    public int getRandom(java.util.Random rand) {
        return rand.nextInt((max - min) + 1) + min;
    }
}