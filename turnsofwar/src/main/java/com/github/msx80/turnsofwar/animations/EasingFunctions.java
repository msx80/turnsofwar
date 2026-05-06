package com.github.msx80.turnsofwar.animations;

public class EasingFunctions {
	
	@FunctionalInterface
	public interface Easing {
	    double calculate(double k);
	}
	
    public static final Easing LINEAR = k -> k;
    
    public static final Easing BACK_IN = k -> {
        double s = 1.70158;
        return k * k * ((s + 1) * k - s);
    };
    
    public static final Easing QUADRATIC = k -> k * k;
    
    public static final Easing QUADRATIC_IN_OUT = k -> {
        k *= 2;
        if (k < 1) return 0.5 * k * k;
        return -0.5 * ((k - 1) * (k - 3) - 1);
    };
    
    public static final Easing ELASTIC_OUT = k -> {
        if (k == 0) return 0;
        if (k == 1) return 1;
        return Math.pow(2, -10 * k) * Math.sin((k - 0.1) * 5 * Math.PI) + 1;
    };
}