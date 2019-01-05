public class LeftProfiles {

    //-------------   Start on Left to Left Scale Profiles   -------------

    // Starts backed up to corner of driver station wall (left).
    // Travels to left scale to dump cube

    public static Spline.Point[] rightScalePoints_LeftToScale = new Spline.Point[]{
            new Spline.Point(-9.54, 0, 90.0),
            new Spline.Point(-9.54, 15.075, 75.0),
            new Spline.Point(-2.04, 18.575, 0.0),
            new Spline.Point(3.96, 18.575, 0.0),
            new Spline.Point(8.0, 22.60, 115.0)};

    public static Spline.Point[] rightScalePoints_ScaleToFenceCube6 = new Spline.Point[]{
            new Spline.Point(8.86, 22.35, 295.0),
            new Spline.Point(4.86, 16.85, 240.0)};

    public static Spline.Point[] rightScalePoints_FenceCube6ToScale = new Spline.Point[]{
            new Spline.Point(5.36, 17.35, 60.0),
            new Spline.Point(6.36, 22.35, 105.0)};

    public static Spline.Point[] rightScalePoints_ScaleToFenceCube5 = new Spline.Point[]{
            new Spline.Point(6.36, 21.35, 295.0),
            new Spline.Point(-0.95, 16.35, 245.0)};


    public static Spline.Point[] leftScalePoints_LeftToScale = new Spline.Point[]{
            new Spline.Point(-9.6, 0.0, 90.0),
            new Spline.Point(-8.0, 22.25, 65.0)};

    public static Spline.Point[] leftScalePoints_ScaleToFenceCube1 = new Spline.Point[]{
            new Spline.Point(-8.0, 21.25, 245.0),
            new Spline.Point(-5.5, 15.25, 300.0)};

    public static Spline.Point[] leftScalePoints_FenceCube1ToScale = new Spline.Point[]{
            new Spline.Point(-6.5, 16.25, 120.0),
            new Spline.Point(-7.0, 21.25, 45.0)};

    public static Spline.Point[] leftScalePoints_ScaleToFenceCube2 = new Spline.Point[]{
            new Spline.Point(-7.5, 21.25, 225.0),
            new Spline.Point(-2.0, 17.55, 310.0)};

    public static Spline.Point[] leftScalePoints_FenceCube2ToScale = new Spline.Point[]{
            new Spline.Point(-2.2, 18.25, 130.0),
            new Spline.Point(-4.5, 22.25, 60.0)};
    
    //---------------------------Start on Left to Left Switch Profiles----------------------------/

    // Left side backwards to backup point
    public static Spline.Point[] leftSwitchPoints_leftToBackupPoint = new Spline.Point[]{
            new Spline.Point(-9.4, 0.0, 90),
            new Spline.Point(-11.5, 18, 90)};

    // Backup Point to Left Switch   
    public static Spline.Point[] leftSwitchPoints_BackupPointToLeftSwitch = new Spline.Point[]{
            new Spline.Point(-11.5, 18, 270),
            new Spline.Point(-7.0, 13, 0)};

    // Left Switch to Fence Cube 1 Line Up
    public static Spline.Point[] leftSwitchPoints_LeftSwitchToFenceCube1LineUp = new Spline.Point[]{
            new Spline.Point(-7.0, 13, 180),           
            new Spline.Point(-6.5, 23, 100)};
    
    // Fence Cube 1 Line Up to Fence Cube 1
    public static Spline.Point[] leftSwitchPoints_LineUpForFenceCube1ToFenceCube1 = new Spline.Point[]{
            new Spline.Point(-6.5, 23, 280),           
            new Spline.Point(-5.5, 17, 270)};
    
    // Fence Cube 1 backup
    public static Spline.Point[] leftSwitchPoints_FenceCube1BackUp = new Spline.Point[]{
            new Spline.Point(-5.5, 17, 90),           
            new Spline.Point(-5, 20, 90)};
   
    // Fence Cube 1 to Left Switch
    public static Spline.Point[] leftSwitchPoints_FenceCube1BackUpToLeftSwitch = new Spline.Point[]{
            new Spline.Point(-5, 20, 270),           
            new Spline.Point(-4.5, 15.5, 300)};

 
    
    //---------------------------Start on Left to Right Switch Profiles ----------------------------

    // Left side backwards to line up for Right Switch
    public static Spline.Point[] rightSwitchPoints_leftToRightSwitchLineUp = new Spline.Point[]{
            new Spline.Point(-9.4, 0.0, 90),
            new Spline.Point(-4.5, 5.0, 0),
            new Spline.Point(7, 5.0, 0),
//            new Spline.Point(11.0, 10.0, 90),
            new Spline.Point(12.0, 14, 0)};
    
    // Right Switch line up to Right switch
    public static Spline.Point[] rightSwitchPoints_RightSwitchLineUpToRightSwitch = new Spline.Point[]{
    		new Spline.Point(12.0, 14, 180),           
    		new Spline.Point(7.0, 14, 180)};
  
    


}
