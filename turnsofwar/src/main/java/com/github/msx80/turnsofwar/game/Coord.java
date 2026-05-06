package com.github.msx80.turnsofwar.game;

import java.util.Objects;

public class Coord {
    public int x;
    public int y;

    // Constructor
    public Coord(int min, int max) {
        this.x = min;
        this.y = max;
    }

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coord other = (Coord) obj;
		return x == other.x && y == other.y;
	}

	
	
}