package org.team1619.utilities;

public class PID {

	private double fP;
	private double fI;
	private double fD;

	public PID(double p, double i, double d) {
		fP = p;
		fI = i;
		fD = d;
	}

	public double get(double measuredValue) {
		return 0;
	}
}
