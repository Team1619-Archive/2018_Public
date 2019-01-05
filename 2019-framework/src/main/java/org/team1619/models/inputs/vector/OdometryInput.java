package org.team1619.models.inputs.vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.inputs.bool.BooleanInput;
import org.team1619.models.inputs.numeric.NumericInput;
import org.team1619.shared.abstractions.ObjectsDirectory;
import org.team1619.utilities.Config;
import org.team1619.utilities.Odometry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class OdometryInput extends VectorInput {

	private static final Logger sLogger = LoggerFactory.getLogger(OdometryInput.class);

	protected final Config fConfig;
	protected final Odometry fOdometry = new Odometry();
	protected final ObjectsDirectory fSharedObjectsDirectory;

	@Nullable
	private NumericInput fDriverX;
	@Nullable
	private NumericInput fDriverY;

//	@Nullable
//	protected CSVPrinter fPrinter;

	@Nullable
	protected BooleanInput fPrintButton;
	@Nullable
	protected NumericInput fLeftEncoder;
	@Nullable
	protected NumericInput fRightEncoder;
	@Nullable
	protected NumericInput fHeadingSensor;
	@Nullable
	protected VectorInput fAccelerationSensor;

	private boolean fIsPrintButton = false;

	public OdometryInput(ObjectsDirectory objectsDirectory, Object name, Config config) {
		super(name, config);
		fConfig = config;
		fSharedObjectsDirectory = objectsDirectory;
	}

	@Override
	public void initialize() {
		fDriverX = fSharedObjectsDirectory.getNumericInputObject("ni_driver_x");
		fDriverY = fSharedObjectsDirectory.getNumericInputObject("ni_driver_y");

		fPrintButton = fSharedObjectsDirectory.getBooleanInputObject(fConfig.getString("print_button"));
		fLeftEncoder = fSharedObjectsDirectory.getNumericInputObject(fConfig.getString("left_encoder"));
		fRightEncoder = fSharedObjectsDirectory.getNumericInputObject(fConfig.getString("right_encoder"));
		fHeadingSensor = fSharedObjectsDirectory.getNumericInputObject(fConfig.getString("heading_sensor"));
		fAccelerationSensor = fSharedObjectsDirectory.getVectorInputObject(fConfig.getString("acceleration_sensor"));

		fOdometry.initialize(fLeftEncoder.get(), fRightEncoder.get(), fHeadingSensor.get());

//		String filePath = fConfig.getString("file_path");
//
//		try {
//			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
//			fPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
//					.withHeader("Left Encoder", "Right Encoder", "Heading Sensor", "X Acceleration", "Y Acceleration", "Z Acceleration", "Left Voltage", "Right Voltage"));
//		} catch (IOException e) {
//			throw new ConfigurationException("Creating data writer for odometry input " + getName() + " failed");
//		}
	}

	@Override
	public void update() {
		checkNotNull(fDriverX);
		checkNotNull(fDriverY);

		checkNotNull(fPrintButton);
		checkNotNull(fLeftEncoder);
		checkNotNull(fRightEncoder);
		checkNotNull(fHeadingSensor);
		checkNotNull(fAccelerationSensor);
//		checkNotNull(fPrinter);

		double leftEncoder = fLeftEncoder.get();
		double rightEncoder = fRightEncoder.get();
		double headingSensor = fHeadingSensor.get();

		fOdometry.update(leftEncoder, rightEncoder, headingSensor);

		boolean nextIsPrintButton = fPrintButton.get();
//		if (nextIsPrintButton) {
//			try {
//				fPrinter.printRecord(leftEncoder, rightEncoder, headingSensor, fAccelerationSensor.get().get(0), fAccelerationSensor.get().get(1), fAccelerationSensor.get().get(2), fDriverY.get() - fDriverX.get(), fDriverY.get() + fDriverX.get());
//			} catch (IOException e) {
//				throw new RuntimeException("");
//			}
//		} else if (fIsPrintButton) {
//			try {
//				fPrinter.flush();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

		fIsPrintButton = nextIsPrintButton;
	}

	@Override
	public List<Double> get() {
		return Arrays.asList(0.0);
		// return Arrays.asList(fOdometry.getPosition().x, fOdometry.getPosition().y);
	}
}
