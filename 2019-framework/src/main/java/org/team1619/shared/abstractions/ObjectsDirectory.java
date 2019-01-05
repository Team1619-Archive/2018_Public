package org.team1619.shared.abstractions;

import org.team1619.models.inputs.bool.BooleanInput;
import org.team1619.models.inputs.numeric.NumericInput;
import org.team1619.models.inputs.vector.VectorInput;
import org.team1619.models.outputs.motors.Motor;
import org.team1619.models.outputs.solenoids.Solenoid;
import org.team1619.models.state.State;
import org.team1619.utilities.Config;
import org.team1619.utilities.YamlConfigParser;
import java.util.List;

public interface ObjectsDirectory {
	//Inputs
	public void registerAllInputs(YamlConfigParser booleanInputParser, YamlConfigParser numericInputParser, YamlConfigParser vectorInputParser);

	public void registerBooleanInput(String name, Config config);

	public void registerNumericInput(String name, Config config);

	public void registerVectorInput(String name, Config config);

	public BooleanInput getBooleanInputObject(String name);

	public NumericInput getNumericInputObject(String name);

	public VectorInput getVectorInputObject(String name);

	//States
	public void registerAllStates(YamlConfigParser parser);

	public void registerStates(Object name, YamlConfigParser statesParser, Config config);

	public State getStateObject(String name);

	//Outputs
	public void registerAllOutputs(YamlConfigParser motorsParser, YamlConfigParser solenoidsParser);

	public void registerMotor(String name, Config config, YamlConfigParser parser);

	public void registerSolenoid(String name, Config config, YamlConfigParser parser);

	public Motor getMotorObject(String motorName);

	public Solenoid getSolenoidObject(String solenoidName);
}
