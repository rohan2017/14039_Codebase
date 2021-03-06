package org.firstinspires.ftc.teamcode.Movement.Localization;

public class Odometer {

    // Wheel & Encoder Measurements - need to be in the same units as other measurements
    protected double wheelRadius = 2.4; //Radius of the omnidirectional dead-wheels
    protected double ticksPerRevolution = 8192; //How many ticks are in one revolution of the encoder
    protected double gearRatio = 1.0; //How many rotations of the wheel per 1 rotation of the encoder
    
    protected boolean firstloop;
    public double x, y, heading;
    protected double lastX, lastY, lastHeadingRadians, headingRadians;

    public void initialize(){}
    public void startTracking(double initialX, double initialY, double initialHeading){

        lastX = initialX;
        lastY = initialY;
        headingRadians = Math.toRadians(initialHeading);
        lastHeadingRadians = headingRadians;
        firstloop = true;

    }
    public void update(){}
}
