package org.team1619.models.outputs.motors;

import org.team1619.models.exceptions.ConfigurationInvalidTypeException;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.ModelFactory;
import org.team1619.utilities.YamlConfigParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class TalonGroup implements Motor {

	protected final Object fName;
	private final Talon fMaster;
	private final Set<Talon> fSlaves = new HashSet<>();

	public TalonGroup(Object name, Config config, YamlConfigParser parser, ModelFactory modelFactory) {
		fName = name;

		String master = config.getString("master");
		Motor motor = modelFactory.createMotor(master, parser.getConfig(master), parser);
		if (!(motor instanceof Talon)) {
			throw new ConfigurationInvalidTypeException("Talon", "master", motor);
		}
		fMaster = (Talon) motor;

		for (Object slaveName : config.getList("slaves")) {
			motor = modelFactory.createMotor(slaveName, parser.getConfig(slaveName), parser);
			if (!(motor instanceof Talon)) {
				throw new ConfigurationInvalidTypeException("Talon", "slave", motor);
			}

			Talon slave = (Talon) motor;
			slave.setHardware(OutputType.SLAVE, fMaster.getDeviceNumber());
			fSlaves.add(slave);
		}
	}

	@Override
	public void setHardware(OutputType outputType, double outputValue) {
		fMaster.setHardware(outputType, outputValue);
	}

	@Override
	public Map<Integer, Double> getMotorCurrentValues(){
		Map<Integer, Double> motorCurrentValues = fMaster.getMotorCurrentValues();
		for(Talon motor : fSlaves){
			motorCurrentValues.putAll(motor.getMotorCurrentValues());
		}
		return motorCurrentValues;
	}
}
