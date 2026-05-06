package com.github.msx80.turnsofwar.animations;

public class SequentialAnimation extends BaseAnimation {

	IAnimation a1;
	IAnimation a2;
	boolean calledOnBegin = false;
	
	public SequentialAnimation(IAnimation a1, IAnimation a2, Runnable onEnd) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.onEnd = onEnd;
	}

	@Override
	public void draw() {
         if (!a1.isFinished()) {
             a1.draw();
         } else if (!a2.isFinished()) {
             a2.draw();
         }
	}

	@Override
	public boolean update() {
		if(onBegin!=null && !calledOnBegin)
		{
			calledOnBegin = true;
			onBegin.run();
		}
		
		
        if (!a1.isFinished()) {
            a1.update();
            return true;
        } else if (!a2.isFinished()) {
            a2.update();
        }

        
		if (isFinished()) // both finished 
		{
            if (onEnd != null) onEnd.run();
            return false; // Stop updating
        }
		return true;
	}

	@Override
	public boolean isFinished() {
		return a1.isFinished() && a2.isFinished();
	}

}
