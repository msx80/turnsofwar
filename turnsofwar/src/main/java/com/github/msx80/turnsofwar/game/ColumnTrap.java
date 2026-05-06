package com.github.msx80.turnsofwar.game;

import java.util.function.Supplier;

public class ColumnTrap implements TrapAction 
{
    public final Supplier<Event> action;
	public final int colNum;
    public ColumnTrap(int colNum, Supplier<Event> action) { 
    	this.colNum = colNum;
    	this.action = action; }
    @Override public Event trigger() { return action.get(); }
}