public class CenterProfiles {

    //-------------   Center to Left Switch Profiles   -------------

    // Starts backed up to wall just right of center.
    // Travels to left switch to dump cube
    public static Spline.Point[] leftSwitchPoints_CenterToSwitch = new Spline.Point[]{
            new Spline.Point(1.4, 0.0, 90.0),
            new Spline.Point(-4.0, 9.5, 90.0)};

    // Starts at left switch
    // Travels backwards to a point lined up with cube pyramid
    public static Spline.Point[] leftSwitchPoints_SwitchToCenterForward = new Spline.Point[]{
            new Spline.Point(-4.0, 9.5, 270.0),
            new Spline.Point(0.0, 2.0, 270.0)};

    // Starts lined up with cube pyramid
    // Travels straight forward into the cube pyramid to pick up a cube
    public static Spline.Point[] leftSwitchPoints_CenterForwardToCenterCube = new Spline.Point[]{
            new Spline.Point(0.0, 2.0, 90.0),
            new Spline.Point(0.0, 6.75, 90.0)};

    // Starts at cube pyramid
    // Travels straight backwards
    public static Spline.Point[] leftSwitchPoints_CenterCubeToCenterForward = new Spline.Point[]{
            new Spline.Point(0.0, 5.5, 270.0),
            new Spline.Point(0.0, 2.0, 270.0)};

    // Starts a few feet back from cube pyramid
    // Travels forward to left switch to dump cube
    public static Spline.Point[] leftSwitchPoints_CenterForwardToSwitch = new Spline.Point[]{
            new Spline.Point(0.0, 2.0, 90.0),
            new Spline.Point(-4.25, 9.5, 90.0)};


    //-------------   Center to Right Switch Profiles   -------------

    // Starts backed up to wall just right of center.
    // Travels to right switch to dump cube
    public static Spline.Point[] rightSwitchPoints_CenterToSwitch = new Spline.Point[]{
            new Spline.Point(1.4, 0.0, 90.0),
            new Spline.Point(4.25, 9.5, 90.0)};

    // Starts at right switch
    // Travels backwards to a point lined up with cube pyramid
    public static Spline.Point[] rightSwitchPoints_SwitchToCenterForward = new Spline.Point[]{
            new Spline.Point(4.25, 9.5, 270.0),
            new Spline.Point(0.0, 2.0, 270.0)};

    // Starts lined up with cube pyramid
    // Travels straight forward into the cube pyramid to pick up a cube
    public static Spline.Point[] rightSwitchPoints_CenterForwardToCenterCube = new Spline.Point[]{
            new Spline.Point(0.0, 2.0, 90.0),
            new Spline.Point(0.0, 6.5, 90.0)};

    // Starts at cube pyramid
    // Travels straight backwards
    public static Spline.Point[] rightSwitchPoints_CenterCubeToCenterForward = new Spline.Point[]{
            new Spline.Point(0.0, 5.5, 270.0),
            new Spline.Point(0.0, 2.0, 270.0)};

    // Starts a few feet back from cube pyramid
    // Travels forward to right switch to dump cube
    public static Spline.Point[] rightSwitchPoints_CenterForwardToSwitch = new Spline.Point[]{
            new Spline.Point(0.0, 2.0, 90.0),
            new Spline.Point(4.25, 9.5, 90.0)};

    //-------------   CenterCube to LeftScale   -------------

    // Starts at cube pyramid
    // Travels to left scale doing a backwards arc to dump cube in left scale
    public static Spline.Point[] leftScalePoints_CenterCubeToScale = new Spline.Point[]{
            new Spline.Point(0.0, 5.2, 270.0),
            new Spline.Point(-4, 4, 170.0),
            new Spline.Point(-11, 9.8, 110.0),
            new Spline.Point(-12.0, 20.2, 65.0)};

    public static Spline.Point[] leftScalePoints_CenterCubeToScale_RL = new Spline.Point[]{
            new Spline.Point(-0.0, 5.2, 270.0),
            new Spline.Point(-4.0, 4.0, 170.0),
            new Spline.Point(-9.5, 9.8, 110.0),
            new Spline.Point(-8.0, 22.7, 45.0)};

    public static Spline.Point[] leftScalePoints_CenterCubeToScale_Side = new Spline.Point[]{
            new Spline.Point(0.0, 5.2, 270.0),
            new Spline.Point(-7, 4, 170.0),
            new Spline.Point(-13.5, 12.0, 100.0),
            new Spline.Point(-14.0, 21.7, 90.0),
            new Spline.Point(-13.5, 22.7, 20.0)};

    public static Spline.Point[] leftScalePoints_CenterCubeToScale_RL_Side = new Spline.Point[]{
            new Spline.Point(-0.0, 5.2, 270.0),
            new Spline.Point(-4.0, 4.0, 170.0),
            new Spline.Point(-9.5, 12.0, 100.0),
            new Spline.Point(-10.0, 24.3, 90.0),
            new Spline.Point(-9.5, 26.3, 20.0)};

    // Starts at left scale
    // Travels to switch fence to pick up a fence cube
    public static Spline.Point[] leftScalePoints_ScaleToFenceCube1 = new Spline.Point[]{
            new Spline.Point(-10.0, 23.2, 245.0),
            new Spline.Point(-6.3, 17.0, 270.0)};

    public static Spline.Point[] leftScalePoints_ScaleToFenceCube1_RL = new Spline.Point[]{
            new Spline.Point(-10.0, 23.2, 245.0),
            new Spline.Point(-6.3, 17.0, 270.0)};

    // Starts from switch fence with cube
    // Travels to scale to dump cube 
    public static Spline.Point[] leftScalePoints_FenceCube1ToScale = new Spline.Point[]{
            new Spline.Point(-7.0, 17.3, 90.0),
            new Spline.Point(-10.0, 23.2, 65.0)};

    //-------------   CenterCube to Right Scale   -------------

    // Starts at cube pyramid
    // Travels to left scale doing a backwards arc to dump cube in right scale
    public static Spline.Point[] rightScalePoints_CenterCubeToScale = new Spline.Point[]{
            new Spline.Point(0.0, 5.2, 270.0),
            new Spline.Point(4.0, 4.0, 10.0),
            new Spline.Point(9.5, 9.8, 70.0),
            new Spline.Point(10.0, 21.0, 115.0)};

    public static Spline.Point[] rightScalePoints_CenterCubeToScale_LR = new Spline.Point[]{
            new Spline.Point(0.0, 5.2, 270.0),
            new Spline.Point(4.0, 4.0, 10.0),
            new Spline.Point(9.5, 9.8, 70.0),
            new Spline.Point(8.0, 23.50, 140.0)};

    public static Spline.Point[] rightScalePoints_CenterCubeToScale_Side = new Spline.Point[]{
            new Spline.Point(0.0, 5.2, 270.0),
            new Spline.Point(4.0, 4.0, 10.0),
            new Spline.Point(13.5, 12.0, 80.0),
            new Spline.Point(14.0, 22.5, 90.0),
            new Spline.Point(13.5, 24.5, 160.0)};

    public static Spline.Point[] rightScalePoints_CenterCubeToScale_LR_Side = new Spline.Point[]{
            new Spline.Point(0.0, 5.2, 270.0),
            new Spline.Point(4.0, 4.0, 10.0),
            new Spline.Point(11.5, 12.0, 80.0),
            new Spline.Point(12.0, 24.50, 90.0),
            new Spline.Point(11.5, 26.50, 160.0)};

    // Starts at right scale
    // Travels to switch fence to pick up a fence cube
    public static Spline.Point[] rightScalePoints_ScaleToFenceCube6 = new Spline.Point[]{
            new Spline.Point(10, 23.2, 295.0),
            new Spline.Point(6.8, 18.5, 270.0),
            new Spline.Point(6.8, 18.0, 270.0)};

    public static Spline.Point[] rightScalePoints_ScaleToFenceCube6_LR = new Spline.Point[]{
            new Spline.Point(10, 23.2, 295.0),
            new Spline.Point(7.8, 18.5, 270.0),
            new Spline.Point(7.8, 18.0, 270.0)};

    // Starts from switch fence with cube
    // Travels to scale to dump cube
    public static Spline.Point[] rightScalePoints_FenceCube6ToScale = new Spline.Point[]{
            new Spline.Point(7.0, 17.3, 90.0),
            new Spline.Point(10.0, 23.2, 115.0)};


    //-------------   Test   -------------


    public static Spline.Point[] test1 = new Spline.Point[]{
            new Spline.Point(0.0, 0.0, 90.0),
            new Spline.Point(5.0, 5.0, 0.0)};

}