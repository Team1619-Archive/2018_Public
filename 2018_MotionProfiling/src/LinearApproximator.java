
public class LinearApproximator {

	private double[] parameterValues;
	private double[] values;

	public LinearApproximator(double[] parameterValues, double[] values) {
		assert parameterValues.length == values.length : "parameterValues and values must be the same length";

		this.parameterValues = parameterValues;
		this.values = values;
	}

	public int findIndexforParameter(double parameter, int approximateIndex) {

		int index = approximateIndex;
		if (this.parameterValues[index] > parameter) {
			while (this.parameterValues[index] > parameter) {

				if (index == 0) {
					return -1;
				}

				index--;
			}
		} else {
			while (this.parameterValues[index] < parameter) {

				if (index == this.parameterValues.length - 1) {
					return index;
				}

				index++;
			}

			index--; // Point the index to the parameter value just smaller than the target parameter
		}

		return index;
	}
	
	public int findIndexforValue(double value, int approximateIndex) {

		int index = approximateIndex;
		if (this.values[index] > value) {
			while (this.values[index] > value) {

				if (index == 0) {
					return -1;
				}

				index--;
			}
		} else {
			while (this.values[index] < value) {

				if (index == this.values.length - 1) {
					return index;
				}

				index++;
			}

			index--; // Point the index to the value just smaller than the target value
		}

		return index;
	}
	
	public double evaluateValue(double parameter, int index) {
		if (index == -1) {
			return this.values[0];
		}
		
		else if(index == this.parameterValues.length -1) {
			return this.values[this.values.length -1];
		}
		
		double x = (parameter - this.parameterValues[index])/ (this.parameterValues[index + 1] - this.parameterValues[index]);
		
		return x * (this.values[index + 1] - this.values[index]) + this.values[index];
		
	}
	
	public double evaluateParameter(double value, int index) {
		if (index == -1) {
			return this.parameterValues[0];
		}
		
		else if(index == this.values.length -1) {
			return this.parameterValues[this.values.length -1];
		}
		
		double x = (value - this.values[index])/ (this.values[index + 1] - this.values[index]);
		
		return x * (this.parameterValues[index + 1] - this.parameterValues[index]) + this.parameterValues[index];
	}

}
