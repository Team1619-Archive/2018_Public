public class RightProfiles {
    //-------------   Right to Right Scale Profiles   -------------

    // Starts backed up to corner of driver station wall (right).
    // Travels to left scale to dump cube
    public static Spline.Point[] rightScalePoints_RightToScale = new Spline.Point[]{
            new Spline.Point(9.6, 0.0, 90.0),
            new Spline.Point(8.0, 21.25, 125.0)
    };

    public static Spline.Point[] rightScalePoints_ScaleToFenceCube6 = new Spline.Point[]{
            new Spline.Point(8.0, 21.25, 305.0),
            new Spline.Point(6.5, 16.25, 240.0)};

    public static Spline.Point[] rightScalePoints_FenceCube6ToScale = new Spline.Point[]{
            new Spline.Point(6.5, 16.25, 60.0),
            new Spline.Point(7.5, 21.25, 130.0)};

    public static Spline.Point[] rightScalePoints_ScaleToFenceCube5 = new Spline.Point[]{
            new Spline.Point(7.5, 21.25, 310.0),
            new Spline.Point(2.7, 16.25, 250.0)};

    public static Spline.Point[] rightScalePoints_FenceCube5ToScale = new Spline.Point[]{
            new Spline.Point(2.5, 16.25, 70.0),
            new Spline.Point(5.5, 20.25, 110.0)};

    //---------------------------Start on Right to Right Switch Profiles----------------------------/

    // Right side backwards to Right Switch
    public static Spline.Point[] rightSwitchPoints_RightToRightSwitch = new Spline.Point[]{
            new Spline.Point(9.4, 0.0, 90),
            new Spline.Point(11.0, 10.0, 90),
            new Spline.Point(8.0, 12.5, 180)};

    // Right Switch to Fence Cube 6 Line Up    
    public static Spline.Point[] rightSwitchPoints_RightSwitchToFenceCube6LineUp = new Spline.Point[]{
            new Spline.Point(8.0, 12.5, 0),
            new Spline.Point(10, 16.0, 90),
            new Spline.Point(8, 20.0, 180),
            new Spline.Point(5.4, 18.0, 270)};

    // Fence Cube 6 Line Up to Fence Cube 6
    public static Spline.Point[] rightSwitchPoints_LineUpForFenceCube6ToFenceCube6 = new Spline.Point[]{
            new Spline.Point(5.4, 18.0, 270),           
            new Spline.Point(5.4, 16.3, 270)};
    
    // Fence Cube 6 backup
    public static Spline.Point[] rightSwitchPoints_FenceCube6BackUp = new Spline.Point[]{
            new Spline.Point(5.4, 16.3, 270),           
            new Spline.Point(5.4, 17.0, 90)};
   
    // Fence Cube 6 to Right Switch
    public static Spline.Point[] rightSwitchPoints_FenceCube6BackUpToRightSwitch = new Spline.Point[]{
            new Spline.Point(5.4, 17.0, 90),           
            new Spline.Point(5.4, 15.0, 270)};
   
    
    //---------------------------Start on Right to Left Switch Profiles ----------------------------

    // Right side backwards to Left Switch
    public static Spline.Point[] leftSwitchPoints_RightToLeftSwitch = new Spline.Point[]{
            new Spline.Point(9.4, 0.0, 90),
            new Spline.Point(4.5, 5.8, 180),
            new Spline.Point(-7, 5.8, 180),
            new Spline.Point(-11.0, 10.0, 90),
            new Spline.Point(-8.0, 12.5, 0)};
    
    // Left Switch to Fence Cube 1 Line Up
    public static Spline.Point[] leftSwitchPoints_LeftSwitchToFenceCube1LineUp = new Spline.Point[]{
            new Spline.Point(-8.0, 12.5, 180),
            new Spline.Point(-10, 16.0, 90),
            new Spline.Point(-8, 20.0, 0),
            new Spline.Point(-5.4, 18.0, 270)};
    
    // Fence Cube 1 Line Up to Fence Cube 1
    public static Spline.Point[] leftSwitchPoints_LineUpForFenceCube1ToFenceCube1 = new Spline.Point[]{
            new Spline.Point(-5.4, 18.0, 270),           
            new Spline.Point(-5.4, 16.3, 270)};
    
    // Fence Cube 1 backup
    public static Spline.Point[] leftSwitchPoints_FenceCube1BackUp = new Spline.Point[]{
            new Spline.Point(-5.4, 16.3, 270),           
            new Spline.Point(-5.4, 17.0, 90)};

    // Fence Cube 1 to Left Switch
    public static Spline.Point[] leftSwitchPoints_FenceCube1BackUpToLeftSwitch = new Spline.Point[]{
            new Spline.Point(-5.4, 17.0, 90),           
            new Spline.Point(-5.4, 15.0, 270)};


}


