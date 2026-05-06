package com.github.msx80.turnsofwar.animations;

public abstract class BaseAnimation implements IAnimation {

	 Runnable onEnd, onBegin;

	public Runnable getOnEnd() {
		return onEnd;
	}

	public IAnimation setOnEnd(Runnable onEnd) {
		this.onEnd = onEnd;
		return this;
	}

	public Runnable getOnBegin() {
		return onBegin;
	}

	public IAnimation setOnBegin(Runnable onBegin) {
		this.onBegin = onBegin;
		return this;
	}
	
	
	
}
