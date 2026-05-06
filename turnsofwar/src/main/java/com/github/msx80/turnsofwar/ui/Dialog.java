package com.github.msx80.turnsofwar.ui;

import java.util.List;

import com.github.msx80.omicron.basicutils.text.TextDrawerVariable;
import com.github.msx80.turnsofwar.animations.EasingFunctions;

public class Dialog {
	
	public DialogType type;
	public Runnable onClose;
	public List<String> strs;
	public int x, y, w, h;
	public int timer = 0;
	
	
	public Dialog(DialogType type, Runnable onClose, List<String> strs) {
		super();
		this.type = type;
		this.onClose = onClose;
		this.strs = strs;
		
		

  	  int tw = 0;
  	  for (String s : strs) {
    		int q = TextDrawerVariable.DEFAULT.width(s) / 2;
    		if (q>tw) tw=q;
  	  }
  	  
  	  int th = ( strs.size() * 7) / 2;
  	  
  	  this.w =tw*2+10; 
  	  this.h =(th*2)+10;
  	  this.x = 88-tw-5;
  	  this.y = 60-th;
  	  
		
	}


	public boolean animating() {
		return timer<30;
	}
	
	public double perc() {
		double perc = timer / 30d;
		return EasingFunctions.QUADRATIC.calculate(perc);
	}
	
	
	

}
