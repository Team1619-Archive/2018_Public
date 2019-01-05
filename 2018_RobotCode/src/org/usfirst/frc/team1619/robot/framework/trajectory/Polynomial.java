package org.usfirst.frc.team1619.robot.framework.trajectory;

public class Polynomial {

	private double[] coefficients;

	public Polynomial(double... coefficients) {
		this.coefficients = coefficients;
	}

	public double evaluate(double parameter) {
		double result = 0.0;
		for (int i = 0; i < coefficients.length; i++) {
			result += Math.pow(parameter, i) * this.coefficients[i];
		}

		return result;
	}

	public double[] getCoefficients() {
		return this.coefficients;
	}

	public String toString() {
		return this.toString("x");
	}

	public String toString(String parameter) {
		if (this.coefficients.length == 0) {
			return "0";
		}

		String stringPolynomial = "";
		for (int i = 0; i < this.coefficients.length; i++) {
			if (coefficients[i] != 0) {
				stringPolynomial += this.getStringFor(parameter, this.coefficients[i], i) + " + ";
			}
		}

		return stringPolynomial.substring(0, stringPolynomial.length() - 3);
	}

	private String getStringFor(String parameter, double coefficient, int exponent) {
		String coefficientString = "" + coefficient;

		switch (exponent) {
		case 0:
			return coefficientString;
		case 1:
			return coefficientString + parameter;
		default:
			return coefficientString + parameter + "^" + exponent;
		}
	}

	public static Polynomial differentiate(Polynomial polynomial) {
		double[] coefficients = polynomial.getCoefficients();

		if (coefficients.length == 0) {
			return new Polynomial();
		}

		double[] derivativeCoefficients = new double[coefficients.length - 1];

		for (int i = 1; i < coefficients.length; i++) {
			derivativeCoefficients[i - 1] = coefficients[i] * i;
		}
		return new Polynomial(derivativeCoefficients);
	}

	public static Polynomial integrate(Polynomial polynomial, double constant) {
		double[] coefficients = polynomial.getCoefficients();
		double[] integralCoefficients = new double[coefficients.length + 1];

		integralCoefficients[0] = constant;
		for (int i = 0; i < coefficients.length; i++) {
			integralCoefficients[i + 1] = coefficients[i] / (double) (i + 1);
		}
		return new Polynomial(integralCoefficients);
	}

}
