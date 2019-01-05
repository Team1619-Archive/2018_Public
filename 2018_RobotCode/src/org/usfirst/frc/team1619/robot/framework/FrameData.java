package org.usfirst.frc.team1619.robot.framework;

import java.util.HashMap;
import java.util.Map;

public class FrameData {

	private static FrameData instance;

	public static FrameData getInstance() {
		if (instance == null) {
			instance = new FrameData();
		}

		return instance;
	}

	private int counter = 0;

	private Map<Integer, Double> numericData = new HashMap<>();
	private Map<Integer, Boolean> booleanData = new HashMap<>();

	private FrameData() {
	}

	public int createNumeric() {
		this.counter++;
		this.numericData.put(this.counter, 0.0);

		return this.counter;
	}

	public int createBoolean() {
		this.counter++;
		this.booleanData.put(this.counter, false);

		return this.counter;
	}

	public double getNumeric(int key) {
		return this.numericData.get(key);
	}

	public boolean getBoolean(int key) {
		return this.booleanData.get(key);
	}

	public void setNumeric(int key, double value) {
		this.numericData.put(key, value);
	}

	public void setBooleanData(int key, boolean value) {
		this.booleanData.put(key, value);
	}

	public void deleteNumeric(int key) {
		this.numericData.remove(key);
	}

	public void deleteBoolean(int key) {
		this.booleanData.remove(key);
	}

	public void reset() {
		for (int key : this.numericData.keySet()) {
			this.numericData.put(key, 0.0);
		}

		for (int key : this.booleanData.keySet()) {
			this.booleanData.put(key, false);
		}
	}

}
