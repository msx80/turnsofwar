package com.github.msx80.turnsofwar.ui;

public enum DialogType {
	START(4,1,15,14),
	END(4,1,12,12),
	DEFEAT(6,12,0,1),
	POPUP(5,11,15,14);
	
	public int[] colors;
	
	private DialogType(int... colors)
	{
		this.colors = colors;
	}

}
