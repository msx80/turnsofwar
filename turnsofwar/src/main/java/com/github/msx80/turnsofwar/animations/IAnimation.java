package com.github.msx80.turnsofwar.animations;

public interface IAnimation 
{
	public void draw();
	public boolean update();
	public boolean isFinished();
	public IAnimation setOnEnd(Runnable r);
	public IAnimation setOnBegin(Runnable r);
}
