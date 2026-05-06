package com.github.msx80.turnsofwar.animations;

import com.github.msx80.turnsofwar.animations.EasingFunctions.Easing;

public class Animation extends BaseAnimation {

	@FunctionalInterface
	interface FrameAction {
	    void execute(double perc, double time);
	}	
	
    private FrameAction draw;
    private int ttl;
    private Easing easing;
   
    private int frames = 0;
    private double time = 0;
    private double perc = 0;

    public Animation(FrameAction draw, int ttl, Easing easing, Runnable onEnd) {
        this.draw = draw;
        this.ttl = ttl;
        this.easing = (easing != null) ? easing : EasingFunctions.LINEAR;
        this.onEnd = onEnd;
    }

    public void draw()
    {
    	 if (draw != null) {
             draw.execute(perc, time);
         }
    }
    
 	public boolean update() {
        
 		if(frames == 0 && onBegin!=null) onBegin.run();
        frames++;
        
        // Calculate normalized progress (0.0 to 1.0)
        double pos = Math.min(1.0, (double) frames / ttl);
        
        // Apply easing
        this.perc = easing.calculate(pos);
        this.time = ttl * perc;

        if (isFinished()) {
            if (onEnd != null) onEnd.run();
            return false; // Stop updating
        }
        return true; // Keep updating
    }

    public boolean isFinished() {
        return frames >= ttl;
    }

    // Getters for external use
    public double getPerc() { return perc; }
    public double getTime() { return time; }
    
    
    public static IAnimation parallel(IAnimation a1, IAnimation a2, Runnable onEnd) {
        return new ParallelAnimation(a1, a2, onEnd);
    }

    public static IAnimation parallel(IAnimation a1, IAnimation a2) {
        return parallel(a1, a2, null);
    }

    public static IAnimation sequential(IAnimation a1, IAnimation a2, Runnable onEnd) {
        
        
        return new SequentialAnimation(a1, a2, onEnd);
    }

	public FrameAction getDraw() {
		return draw;
	}

	public int getTtl() {
		return ttl;
	}

	public Easing getEasing() {
		return easing;
	}

	public int getFrames() {
		return frames;
	}

	
	
}