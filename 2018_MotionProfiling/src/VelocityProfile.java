import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


public class VelocityProfile {

    private SkidSteerRobot robot;
    private Spline spline;

    private double[] scaledTimeValues;
    private double[] scaledCurvatures;
    private double[] scaledVelocityValues;
    private double[] scaledHeadingValues;
    private double[] scaledMaxAngularAccelerationValues;
    private double frequency;

    private double[] scaledCurvatures_D;
    private double[] scaledVelocityValues_D;
    private double[] scaledHeadingValues_D;
    private double[] scaledDistanceValues_D;
    private double distanceStepsForOutput;


    private double[] curvatures;
    private double[] distanceValues;
    private double[] velocityValues;
    private double[] headingValues;
    private double[] timeValues;
    private double[] maxAngularAccelerationValues;

    public VelocityProfile(SkidSteerRobot robot, Spline spline, double frequency, double distanceStepsForOutput) {
        this.spline = spline;
        this.robot = robot;
        this.frequency = frequency;
        this.distanceStepsForOutput = distanceStepsForOutput;
    }

    public static void write(VelocityProfile velocityProfile, double initialHeading, double finalHeading, double[] velocity, double[] heading, double[] curvatures, String trajectoryName, double frequency, double distanceStepsForOutput)
            throws IOException {


        // Set the file path to be in the correct place in the 2018_RobotCode so the files do not have to be copied by hand
        // The projects 2018_MotionProfiling and 2018_RobotCode must be in the same folder for this to work

        // GIT Path - use to put files into RobotCode when using GIT
        String path = "..\\2018_RobotCode\\src\\org\\usfirst\\frc\\team1619\\robot\\trajectories\\";

        // SVN Path - use to put files into RobotCode when using SVN - Must update branch name in path
        // String path = "..\\..\\..\\2018_RobotCode\\branches\\PowerUp_Dev\\src\\org\\usfirst\\frc\\team1619\\robot\\trajectories\\";

        // Hard code to specific directory
        // String path = "C:\\Users\\nevin\\Team 1619 Code\\GIT\\2018_RobotCode\\src\\org\\usfirst\\frc\\team1619\\robot\\trajectories\\"; 

        // Create the directory if needed
        new File(path).mkdirs();

        PrintWriter writer = new PrintWriter(path + trajectoryName + ".java",
                "UTF-8");

        String trajectoryClass = "package org.usfirst.frc.team1619.robot.trajectories;import org.usfirst.frc.team1619.robot.framework.trajectory.TrajectoryData;"
                + "public class " + trajectoryName + "  extends TrajectoryData{" + "public " + trajectoryName + "() {"
                + "super(" + distanceStepsForOutput + "," + initialHeading + "," + finalHeading + ", new double[] {0.0}, new double[] {";

        for (int i = 0; i < velocityProfile.scaledDistanceValues_D.length - 1; i++) {
            trajectoryClass += velocityProfile.scaledDistanceValues_D[i] + ",";
        }
        trajectoryClass += velocity[velocity.length - 1] + "}, new double[] {";

        for (int i = 0; i < velocity.length - 1; i++) {
            trajectoryClass += velocity[i] + ",";
        }
        trajectoryClass += velocity[velocity.length - 1] + "}, new double[] {";

        for (int i = 0; i < heading.length - 1; i++) {
            trajectoryClass += heading[i] + ",";
        }
        trajectoryClass += heading[heading.length - 1] + "}, new double[] {";

        for (int i = 0; i < curvatures.length - 1; i++) {
            trajectoryClass += curvatures[i] + ",";
        }
        trajectoryClass += curvatures[curvatures.length - 1] + "}); }}";

        writer.println(trajectoryClass);
        writer.close();

    }

    public void calculate(double tDelta, double distanceDelta) {

        int numberPoints = (int) Math.ceil(this.spline.getDomainMax() / tDelta) + 1; // tDelta is not accurate for
        // endpoint
        double[] tValues = new double[numberPoints];
        double[] arcLengthValues = new double[numberPoints];

        double t = 0.0;
        tValues[0] = t;
        arcLengthValues[0] = 0.0;

        Spline.Point derivative = this.spline.evaluateFirstDerivatives(t);
        double previousArcLengthDerivative = Math.sqrt(Math.pow(derivative.getX(), 2) + Math.pow(derivative.getY(), 2));

        for (int i = 1; i < numberPoints - 1; i++) {
            t = i * tDelta;

            derivative = this.spline.evaluateFirstDerivatives(t);
            double arcLengthDerivative = Math.sqrt(Math.pow(derivative.getX(), 2) + Math.pow(derivative.getY(), 2));

            tValues[i] = t;
            arcLengthValues[i] = arcLengthValues[i - 1]
                    + tDelta * (arcLengthDerivative + previousArcLengthDerivative) / 2;
            previousArcLengthDerivative = arcLengthDerivative;
        }

        derivative = this.spline.evaluateFirstDerivatives(spline.getDomainMax());
        double arcLengthDerivative = Math.sqrt(Math.pow(derivative.getX(), 2) + Math.pow(derivative.getY(), 2));

        tValues[numberPoints - 1] = this.spline.getDomainMax();
        arcLengthValues[numberPoints - 1] = arcLengthValues[numberPoints - 2]
                + (this.spline.getDomainMax() - t) * (arcLengthDerivative + previousArcLengthDerivative) / 2;

        LinearApproximator arcLengthApproximator = new LinearApproximator(tValues, arcLengthValues);

        double distance = arcLengthValues[numberPoints - 1];

        double[] distanceValues = new double[(int) Math.ceil(distance / distanceDelta)];
        double[] curvatures = new double[(int) Math.ceil(distance / distanceDelta)];
        double[] velocityValues = new double[(int) Math.ceil(distance / distanceDelta)];
        double[] headingValues = new double[(int) Math.ceil(distance / distanceDelta)];
        double[] maxAngularAccelerationValues = new double[(int) Math.ceil(distance / distanceDelta)];

        distanceValues[0] = 0.0;
        double previousVelocity = 0.0;
        velocityValues[0] = previousVelocity;
        headingValues[0] = spline.evaluateHeading(0);
        maxAngularAccelerationValues[0] = 0;

        double previousCurvature = this.spline.evaluateCurvature(0);
        int index = 0;
        double maxVelocity = 0.0;
        curvatures[0] = 0;
        for (int i = 1; i < velocityValues.length - 1; i++) {

            index = arcLengthApproximator.findIndexforValue(i * distanceDelta, index);
            t = arcLengthApproximator.evaluateParameter(i * distanceDelta, index);
            headingValues[i] = this.spline.evaluateHeading(t);
            double curvature = this.spline.evaluateCurvature(t);

            double maxVelocityForTangentialAccleration = Math.min(
                    Math.sqrt(Math.pow(previousVelocity, 2) + 2 * this.robot.getAcceleration() * distanceDelta),
                    this.robot.getMaxVelocity());
            double a = curvature;
            double b = (curvature - previousCurvature) * previousVelocity;
            double posC = -1.0 * (previousCurvature * Math.pow(previousVelocity, 2)) - (2 * distanceDelta + this.robot.getAngularAcceleration());
            double negC = -1.0 * (previousCurvature * Math.pow(previousVelocity, 2)) + (2 * distanceDelta + this.robot.getAngularAcceleration());

            double maxVelocityForRotationalAcceleration = 0.0;

            if (curvature == 0.0) {
                if (curvature == 0.0) {
                    maxVelocityForRotationalAcceleration = maxVelocityForTangentialAccleration;
                } else {

                    double v1Hat = -1.0 * ((2 * distanceDelta * this.robot.getAngularAcceleration())
                            / (previousCurvature * previousVelocity)) - previousVelocity;
                    double v2Hat = ((2 * distanceDelta * this.robot.getAngularAcceleration())
                            / (previousCurvature * previousVelocity)) - previousVelocity;
                    maxVelocityForRotationalAcceleration = Math.max(v1Hat, v2Hat);

                }
            } else {
                double v1 = (-1.0 * b + Math.sqrt(Math.pow(b, 2) - 4 * a * posC)) / (2 * a);
                double v2 = (-1.0 * b - Math.sqrt(Math.pow(b, 2) - 4 * a * posC)) / (2 * a);
                double v1Star = (-1.0 * b + Math.sqrt(Math.pow(b, 2) - 4 * a * negC)) / (2 * a);
                double v2Star = (-1.0 * b - Math.sqrt(Math.pow(b, 2) - 4 * a * negC)) / (2 * a);
                if (curvature > 0) {
                    if (Math.pow(b, 2) - 4 * a * negC < 0) {
                        maxVelocityForRotationalAcceleration = Math.max(v1, v2);
                    } else {
                        maxVelocityForRotationalAcceleration = Math.max(Math.max(v2, v2Star), Math.max(v1Star, v1));
                    }

                } else if (curvature < 0) {
                    if (Math.pow(b, 2) - 4 * a * posC < 0) {
                        maxVelocityForRotationalAcceleration = Math.max(v1Star, v2Star);
                    } else {
                        maxVelocityForRotationalAcceleration = Math.max(Math.max(v1Star, v1), Math.max(v2, v2Star));
                    }
                }
            }

            maxAngularAccelerationValues[i] = maxVelocityForRotationalAcceleration;
            curvatures[i] = curvature;
            velocityValues[i] = Math.min(maxVelocityForRotationalAcceleration, maxVelocityForTangentialAccleration);
            distanceValues[i] = i * distanceDelta;

            previousVelocity = velocityValues[i];
            previousCurvature = curvature;

            if (velocityValues[i] > maxVelocity) {
                maxVelocity = velocityValues[i];
            }
        }

        previousVelocity = 0.0;
        curvatures[curvatures.length - 1] = 0;
        velocityValues[velocityValues.length - 1] = previousVelocity;
        distanceValues[distanceValues.length - 1] = distance;

        double maxVelocityForTangentialDeceleration = Math.min(
                Math.sqrt(Math.pow(previousVelocity, 2)
                        + 2 * this.robot.getDeceleration() * (distance - distanceValues[distanceValues.length - 2])),
                this.robot.getMaxVelocity());

        previousVelocity = Math.min(maxVelocityForTangentialDeceleration, velocityValues[velocityValues.length - 2]);
        velocityValues[velocityValues.length - 2] = previousVelocity;

        for (int i = velocityValues.length - 3; i >= 0; i--) {
            maxVelocityForTangentialDeceleration = Math.min(
                    Math.sqrt(Math.pow(previousVelocity, 2) + 2 * this.robot.getDeceleration() * distanceDelta),
                    this.robot.getMaxVelocity());

            previousVelocity = Math.min(maxVelocityForTangentialDeceleration, velocityValues[i]);
            velocityValues[i] = previousVelocity;
        }

        this.curvatures = curvatures;
        this.distanceValues = distanceValues;
        this.velocityValues = velocityValues;
        this.headingValues = headingValues;
        this.maxAngularAccelerationValues = maxAngularAccelerationValues;


        this.timeValues = new double[this.distanceValues.length];
        this.timeValues[0] = 0.0;
        for (int i = 1; i < this.velocityValues.length; i++) {
            double velocityAvg = (velocityValues[i] + velocityValues[i - 1]) / 2;
            this.timeValues[i] = (distanceValues[i] - distanceValues[i - 1]) / velocityAvg + this.timeValues[i - 1];
        }

        //Scaling by Time
        this.scaledTimeValues = new double[(int) (this.timeValues[timeValues.length -
                1] * this.frequency)];

        this.scaledCurvatures = new double[(int) (this.timeValues[timeValues.length -
                1] * this.frequency)];

        this.scaledVelocityValues = new double[(int)
                (this.timeValues[timeValues.length - 1] * this.frequency)];

        this.scaledHeadingValues = new double[(int)
                (this.timeValues[timeValues.length - 1] * this.frequency)];

        //Scaling by Distance
        this.scaledDistanceValues_D = new double[(int) (this.distanceValues[this.distanceValues.length -
                1] / this.distanceStepsForOutput)];

        this.scaledVelocityValues_D = new double[(int)
                (this.distanceValues[distanceValues.length - 1] / this.distanceStepsForOutput)];

        this.scaledHeadingValues_D = new double[(int)
                (this.distanceValues[distanceValues.length - 1] / this.distanceStepsForOutput)];

        this.scaledCurvatures_D = new double[(int)
                (this.distanceValues[distanceValues.length - 1] / this.distanceStepsForOutput)];

        //Max Angular Acceleration
        this.scaledMaxAngularAccelerationValues = new double[(int)
                (this.distanceValues[distanceValues.length - 1] / this.distanceStepsForOutput)];

        int timeIndex = 0;
        int distanceStep = 0;

        this.scaledDistanceValues_D[0] = 0.0;
        this.scaledCurvatures_D[0] = 0.0;
        this.scaledTimeValues[0] = 0.0;
        this.scaledVelocityValues[0] = 0.0;
        this.scaledDistanceValues_D[0] = 0.0;
        this.scaledMaxAngularAccelerationValues[0] = 0.0;


        for (int i = 1; i < this.scaledDistanceValues_D.length; i++) {
            while (this.distanceValues[distanceStep] < i * this.distanceStepsForOutput) {
                distanceStep++;
            }
            if (Math.abs((i / this.distanceStepsForOutput) - distanceValues[distanceStep - 1]) < Math
                    .abs((i / this.distanceStepsForOutput) - distanceValues[distanceStep])) {
                distanceStep = distanceStep - 1;
            }

            this.scaledDistanceValues_D[i] = this.distanceValues[distanceStep];
            this.scaledVelocityValues_D[i] = this.velocityValues[distanceStep];
            this.scaledHeadingValues_D[i] = this.headingValues[distanceStep];
            this.scaledCurvatures_D[i] = this.curvatures[distanceStep];
            this.scaledMaxAngularAccelerationValues[i] = this.maxAngularAccelerationValues[distanceStep];


        }


        for (int i = 1; i < this.scaledTimeValues.length; i++) {
            while (this.timeValues[timeIndex] < i / this.frequency) {
                timeIndex++;
            }

            if (Math.abs((i / this.frequency) - timeValues[timeIndex - 1]) < Math
                    .abs((i / this.frequency) - timeValues[timeIndex])) {
                timeIndex = timeIndex - 1;
            }

            this.scaledTimeValues[i] = timeValues[timeIndex];
            this.scaledVelocityValues[i] = velocityValues[timeIndex];
            this.scaledHeadingValues[i] = this.headingValues[timeIndex];
            this.scaledCurvatures[i] = this.curvatures[timeIndex];
        }

        String points = "";
        index = 0;
        double maxCurvature = 0.0;
        double totalCurvature = 0.0;
        for (int i = 0; i <= 1000; i++) {
            double d = i / 1000.0 * distance;
            index = arcLengthApproximator.findIndexforValue(d, (int) Math.max(index, 0.0));
            t = arcLengthApproximator.evaluateParameter(d, index);

            double curvature = spline.evaluateCurvature(t);

            if (Math.abs(curvature) > maxCurvature) {
                maxCurvature = curvature;
            }

            totalCurvature += curvature;
            points += "(" + Math.round(d * 10000.0) / 10000.0 + "," + Math.round(curvature * 10000.0) / 10000.0 + "),";

        }

//        System.out.println(points);
    }

    public String toDesmos() {
        String points = "";
        for (int i = 0; i <= 100; i++) {
            int j = (int) (i / 100.0 * (scaledVelocityValues_D.length - 1));

            points += "(" + scaledDistanceValues_D[j] + "," + scaledVelocityValues_D[j] + "),";
        }

        return points.substring(0, points.length() - 1);
    }

    public String getMaxAngularAccelerationValues() {
        String points = "";
        for (int i = 0; i <= 100; i++) {
            int j = (int) (i / 100.0 * (scaledMaxAngularAccelerationValues.length - 1));

            points += "(" + String.format("%.2f", scaledDistanceValues_D[j]) + "," + String.format("%.2f", scaledMaxAngularAccelerationValues[j]) + "),";
        }

        return points.substring(0, points.length() - 1);
    }

    public static void generateProfile(String trajectoryName, boolean print, SkidSteerRobot robot, Spline.Point[] points, double velocityWeight, double accelerationWeight, double frequency, double distanceStepsForOutput, double tDelta, double distanceDelta) throws IOException {
        Spline spline = Spline.quinticHermiteSplineFor(velocityWeight, accelerationWeight, points);
        VelocityProfile velocityProfile = new VelocityProfile(robot, spline, frequency, distanceStepsForOutput);

        velocityProfile.calculate(tDelta, distanceDelta);


        //DONT BE SILLY
//        for (int j = 0; j < velocityProfile.scaledVelocityValues_D.length; j++) {
//            if (j < velocityProfile.scaledVelocityValues_D.length / 4.0) {
//                velocityProfile.scaledVelocityValues_D[j] = 4.0;
//            } else{
//                velocityProfile.scaledVelocityValues_D[j] = 8.0;
//            }
//        }

        VelocityProfile.write(velocityProfile, points[0].getHeading(), points[points.length - 1].getHeading(), velocityProfile.scaledVelocityValues_D, velocityProfile.scaledHeadingValues_D, velocityProfile.scaledCurvatures_D, trajectoryName, velocityProfile.frequency, velocityProfile.distanceStepsForOutput);
        if (print) {
            System.out.println("--------------" + trajectoryName + "--------------");
            System.out.println("Time: " + velocityProfile.scaledVelocityValues.length / velocityProfile.frequency);
            System.out.println("Spline");
            System.out.println(spline);
            System.out.println();
            System.out.println("Velocity Profile");
            System.out.println(velocityProfile.toDesmos());
            System.out.println();
            System.out.println("MaxAngularAccelerationValues");
            System.out.println(velocityProfile.getMaxAngularAccelerationValues());
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        SkidSteerRobot fastRobot = new SkidSteerRobot(2.1, 6.0, 6.0, 10.0, 0.05);
        SkidSteerRobot mediumRobot = new SkidSteerRobot(2.1, 5.5, 5.5, 8.5, 0.05);
        SkidSteerRobot slowRobot = new SkidSteerRobot(2.1, 4.0, 4.0, 4.5, 0.05);
        SkidSteerRobot slowAccelRobot = new SkidSteerRobot(2.1, 4.0, 4.0, 8.5, 0.05);
        SkidSteerRobot fastDecellRobot = new SkidSteerRobot(2.1, 5.5, 8.0, 8.5, 0.05);


        // ---------------------------------------DRIVE STRAIGHT AUTO ---------------------------------------
        generateProfile("DRIVE_STRAIGHT_SIDES", false, mediumRobot, new Spline.Point[]{new Spline.Point(0.0, 0.0, 90.0), new Spline.Point(0.0, 15.0, 90.0)}, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("DRIVE_STRAIGHT_CENTER", false, mediumRobot, new Spline.Point[]{new Spline.Point(0.0, 0.0, 90.0), new Spline.Point(0.0, 10.0, 90)}, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);


        // --------------------------------------- Starting for LEFT SIDE ---------------------------------------

        // Left to Scale
        generateProfile("Left_LeftScale_LeftToScale", false, fastRobot, LeftProfiles.leftScalePoints_LeftToScale, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_LeftScale_ScaleToFenceCube1", false, mediumRobot, LeftProfiles.leftScalePoints_ScaleToFenceCube1, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_LeftScale_FenceCube1ToScale", false, mediumRobot, LeftProfiles.leftScalePoints_FenceCube1ToScale, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_LeftScale_ScaleToFenceCube2", false, slowAccelRobot, LeftProfiles.leftScalePoints_ScaleToFenceCube2, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_LeftScale_FenceCube2ToScale", false, mediumRobot, LeftProfiles.leftScalePoints_FenceCube2ToScale, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);

        // Left to Left Switch
        generateProfile("Left_LeftSwitch_LeftToBackupPoint", false, mediumRobot, LeftProfiles.leftSwitchPoints_leftToBackupPoint, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_LeftSwitch_BackupPointToLeftSwitch", false, mediumRobot, LeftProfiles.leftSwitchPoints_BackupPointToLeftSwitch, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_LeftSwitch_LeftSwitchToFenceCube1LineUp", false, mediumRobot, LeftProfiles.leftSwitchPoints_LeftSwitchToFenceCube1LineUp, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_LeftSwitch_FenceCube1LineUpToFenceCube1", false, slowRobot, LeftProfiles.leftSwitchPoints_LineUpForFenceCube1ToFenceCube1, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_LeftSwitch_FenceCube1BackUp", false, mediumRobot, LeftProfiles.leftSwitchPoints_FenceCube1BackUp, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_LeftSwitch_FenceCube1BackUpToLeftSwitch", false, mediumRobot, LeftProfiles.leftSwitchPoints_FenceCube1BackUpToLeftSwitch, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);

        // Left to Right Switch
        generateProfile("Left_RightSwitch_LeftToRightSwitchLineUp", false, mediumRobot, LeftProfiles.rightSwitchPoints_leftToRightSwitchLineUp, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_RightSwitch_RightSwitchLineUpToRightSwitch", false, mediumRobot, LeftProfiles.rightSwitchPoints_RightSwitchLineUpToRightSwitch, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);


        //Left To Right Scale
        generateProfile("Left_RightSale_LeftToScale", false, mediumRobot, LeftProfiles.rightScalePoints_LeftToScale, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_RightSale_ScaleToFenceCube6", false, slowRobot, LeftProfiles.rightScalePoints_ScaleToFenceCube6, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_RightSale_FenceCube6ToScale", false, mediumRobot, LeftProfiles.rightScalePoints_FenceCube6ToScale, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Left_RightSale_ScaleToFenceCube5", false, slowRobot, LeftProfiles.rightScalePoints_ScaleToFenceCube5, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);


        // --------------------------------------- Starting for RIGHT SIDE ---------------------------------------

        // Right to Right Side
        generateProfile("Right_RightScale_RightToScale", false, fastRobot, RightProfiles.rightScalePoints_RightToScale, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Right_RightScale_ScaleToFenceCube6", false, mediumRobot, RightProfiles.rightScalePoints_ScaleToFenceCube6, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Right_RightScale_FenceCube6ToScale", false, mediumRobot, RightProfiles.rightScalePoints_FenceCube6ToScale, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Right_RightScale_ScaleToFenceCube5", false, mediumRobot, RightProfiles.rightScalePoints_ScaleToFenceCube5, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Right_RightScale_FenceCube5ToScale", false, mediumRobot, RightProfiles.rightScalePoints_FenceCube5ToScale, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);

        // Right to left Switch
        generateProfile("Right_LeftSwitch_RightToLeftSwitch", false, mediumRobot, RightProfiles.leftSwitchPoints_RightToLeftSwitch, 0.65, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Right_LeftSwitch_LeftSwitchToFenceCube1LineUp", false, mediumRobot, RightProfiles.leftSwitchPoints_LeftSwitchToFenceCube1LineUp, 0.65, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Right_LeftSwitch_FenceCube1LineUpToFenceCube1", false, mediumRobot, RightProfiles.leftSwitchPoints_LineUpForFenceCube1ToFenceCube1, 0.65, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Right_LeftSwitch_FenceCube1BackUp", false, mediumRobot, RightProfiles.leftSwitchPoints_FenceCube1BackUp, 0.65, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Right_LeftSwitch_FenceCube1BackUpToLeftSwitch", false, mediumRobot, RightProfiles.leftSwitchPoints_FenceCube1BackUpToLeftSwitch, 0.65, 0.0, 200, 0.05, 0.0001, 0.0008);

        //      --------------------------------------- Starting from CENTER ---------------------------------------

        // Center to Left Switch
        generateProfile("Center_LeftSwitch_CenterToSwitch", false, mediumRobot, CenterProfiles.leftSwitchPoints_CenterToSwitch, 0.8, 0.5, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_LeftSwitch_SwitchToCenterForward", false, mediumRobot, CenterProfiles.leftSwitchPoints_SwitchToCenterForward, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_LeftSwitch_CenterForwardToCenterCube", false, mediumRobot, CenterProfiles.leftSwitchPoints_CenterForwardToCenterCube, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_LeftSwitch_CenterCubeToCenterForward", false, mediumRobot, CenterProfiles.leftSwitchPoints_CenterCubeToCenterForward, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_LeftSwitch_CenterForwardToSwitch", false, mediumRobot, CenterProfiles.leftSwitchPoints_CenterForwardToSwitch, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);

        // Center to Right Switch
        generateProfile("Center_RightSwitch_CenterToSwitch", false, mediumRobot, CenterProfiles.rightSwitchPoints_CenterToSwitch, 0.5, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_RightSwitch_SwitchToCenterForward", false, mediumRobot, CenterProfiles.rightSwitchPoints_SwitchToCenterForward, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_RightSwitch_CenterForwardToCenterCube", false, mediumRobot, CenterProfiles.rightSwitchPoints_CenterForwardToCenterCube, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_RightSwitch_CenterCubeToCenterForward", false, mediumRobot, CenterProfiles.rightSwitchPoints_CenterCubeToCenterForward, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_RightSwitch_CenterForwardToSwitch", false, mediumRobot, CenterProfiles.rightSwitchPoints_CenterForwardToSwitch, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);

        // CenterCube to Left Scale
        generateProfile("Center_LeftScale_CenterCubeToScale", false, mediumRobot, CenterProfiles.leftScalePoints_CenterCubeToScale, 0.8, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_LeftScale_CenterCubeToScale_Side", true, fastDecellRobot, CenterProfiles.leftScalePoints_CenterCubeToScale_Side, 0.8, 0.0, 200, 0.05, 0.0001, 0.0008);

        generateProfile("Center_LeftScale_CenterCubeToScale_RL", false, mediumRobot, CenterProfiles.leftScalePoints_CenterCubeToScale_RL, 0.8, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_LeftScale_CenterCubeToScale_RL_Side", true, fastDecellRobot, CenterProfiles.leftScalePoints_CenterCubeToScale_RL_Side, 0.8, 0.0, 200, 0.05, 0.0001, 0.0008);

        generateProfile("Center_LeftScale_ScaleToFenceCube1", false, slowRobot, CenterProfiles.leftScalePoints_ScaleToFenceCube1, 0.65, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_LeftScale_ScaleToFenceCube1_RL", false, slowRobot, CenterProfiles.leftScalePoints_ScaleToFenceCube1_RL, 0.65, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_LeftScale_FenceCube1ToScale", false, slowRobot, CenterProfiles.leftScalePoints_FenceCube1ToScale, 0.65, 0.0, 200, 0.05, 0.0001, 0.0008);

        // CenterCube to Right Scale
        generateProfile("Center_RightScale_CenterCubeToScale", false, mediumRobot, CenterProfiles.rightScalePoints_CenterCubeToScale, 0.8, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_RightScale_CenterCubeToScale_LR", false, mediumRobot, CenterProfiles.rightScalePoints_CenterCubeToScale_LR, 0.8, 0.0, 200, 0.05, 0.0001, 0.0008);

        generateProfile("Center_RightScale_CenterCubeToScale_Side", true, fastDecellRobot, CenterProfiles.rightScalePoints_CenterCubeToScale_Side, 0.8, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_RightScale_CenterCubeToScale_LR_Side", true, fastDecellRobot, CenterProfiles.rightScalePoints_CenterCubeToScale_LR_Side, 0.8, 0.0, 200, 0.05, 0.0001, 0.0008);

        generateProfile("Center_RightScale_ScaleToFenceCube6", false, slowRobot, CenterProfiles.rightScalePoints_ScaleToFenceCube6, 0.65, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_RightScale_ScaleToFenceCube6_LR", false, slowRobot, CenterProfiles.rightScalePoints_ScaleToFenceCube6_LR, 0.65, 0.0, 200, 0.05, 0.0001, 0.0008);
        generateProfile("Center_RightScale_FenceCube6ToScale", false, slowRobot, CenterProfiles.rightScalePoints_FenceCube6ToScale, 0.65, 0.0, 200, 0.05, 0.0001, 0.0008);


        //      --------------------------------------- TEST ---------------------------------------

        generateProfile("Test1", false, fastRobot, CenterProfiles.test1, 0.85, 0.0, 200, 0.05, 0.0001, 0.0008);

    }


}
