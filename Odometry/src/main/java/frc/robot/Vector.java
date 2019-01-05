package frc.robot;

public class Vector {

    public final double x;
    public final double y;

    private Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector vector) {
        return new Vector(x + vector.x, y + vector.y);
    }

    public Vector rotate(double heading) {
        return new Vector(Math.cos(heading) * this.x - Math.sin(heading * this.y), Math.sin(heading) * this.x + Math.cos(heading) * y);
    }

    public Vector scale(double scalar) {
        return new Vector(x * scalar, y * scalar);
    }

    public static Vector of(double x, double y) {
        return new Vector(x, y);
    }

    public static Vector inDirection(double heading, double magnitude) {
        return new Vector(magnitude * Math.cos(heading), magnitude * Math.sin(heading));
    }
}
