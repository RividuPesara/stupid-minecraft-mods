package com.peshi.mobduplicator;

public final class DropSettings {
	private static volatile int minMultiplier = 2;
	private static volatile int maxMultiplier = 4;

	private DropSettings() {
	}

	public static int minMultiplier() {
		return minMultiplier;
	}

	public static int maxMultiplier() {
		return maxMultiplier;
	}

	public static void setMultiplier(int min, int max) {
		minMultiplier = min;
		maxMultiplier = max;
	}
}
