package org.firstinspires.ftc.teamcode.Movement.Localization;

import org.firstinspires.ftc.teamcode.Utility.RobotHardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class OdometerIMU2W extends Odometer{

    private LinearOpMode opMode;
    private RobotHardware hardware;

    /* Top-Down View of The Bottom of a Robot

    /===================\
    |                   |
    |  A------X-----O   |   A represents the vertical dead-wheel, and can be placed anywhere
    |     ^         |   |
    |     H         |   |   B represents the horizontal dead-wheel, and can be placed anywhere
    |           V-> |   |
    |               B   |
    |                   |
    \===================/

    The above diagram is a top-down view of the bottom of the robot, meaning that when you are
    looking at the robot from the top, A is to the left, O is to the right, and B is to the back.

    X denotes the center of the robot
    H denotes the horizontalOffset variable, or distance from A to X.
    V denotes the verticalOffset variable, or the distance from O to B. Does not matter very much

    Odometer measurements can be in whatever units you want, as long as you use the same units for every constant
    */

    private double horizontalOffset = 3.25;
    private double verticalOffset = 17.75;

    // These variables allow you to set the direction of the encoders regardless of any reversing going on elsewhere
    private double verticalDirection = -1;
    private double horizontalDirection = 1;

    //IMU
    private BNO055IMU imu;
    // Encoder Variables
    public  double vertical, horizontal;
    private double lastVertical, lastHorizontal;
    private double verticalChange, horizontalChange;
    private double ticksToDistance;
    // Math Variables
    private double headingChange, headingImu, lastHeadingImu, fuckedHeading;
    private double verticallAdjust, verticalExtra;
    private double horizontalAdjust, horizontalExtra;
    private double[] positionChangeVertical = {0, 0}; //Position change vector from vertical encoders
    private double[] positionChangeHorizontal = {0, 0}; //Position change vector from horizontal encoder
    private double[] totalRelativeMovement = {0, 0};
    private double[] totalPositionChange = {0, 0};

    public OdometerIMU2W(LinearOpMode opMode, RobotHardware robothardware){

        this.opMode = opMode;
        this.hardware = robothardware;
        this.imu = hardware.imu;

    }


    @Override
    public void initialize(){

        lastVertical = 0;
        lastHorizontal = 0;

        ticksToDistance = wheelRadius*2*Math.PI/ticksPerRevolution*gearRatio;

    }

    @Override
    public void update(){

        if(opMode.opModeIsActive()){

            // HERE IS WHERE TO CHANGE ENCODER OBJECTS
            vertical = hardware.intakeLeft.getCurrentPosition() * ticksToDistance * verticalDirection;
            horizontal = hardware.intakeRight.getCurrentPosition() * ticksToDistance * horizontalDirection;
            
            if(firstloop) {
                lastVertical = vertical;
                lastHorizontal = horizontal;
                lastHeadingImu = getImuHeading();
                firstloop = false;
            }

            verticalChange = vertical - lastVertical;
            horizontalChange = horizontal - lastHorizontal;

            headingImu = getImuHeading();

            headingChange = headingImu - lastHeadingImu;

            if (headingChange < -Math.PI){ // For example 355 to 2 degrees
                headingChange = 2*Math.PI + headingChange;
            }else if (headingChange > Math.PI) { // For example 2 to 355 degrees
                headingChange = -2*Math.PI + headingChange;
            }

            headingChange *= 1.0126;

            headingRadians += headingChange;

            // Calculating the position-change-vector from vertical encoder
            verticallAdjust = horizontalOffset * headingChange;
            verticalExtra = verticalChange - verticallAdjust;

            positionChangeVertical[1] = Math.cos(headingChange) * verticalExtra;
            positionChangeVertical[0] = Math.sin(headingChange) * verticalExtra;

            //Calculating the position-change-vector from horizontal encoder
            horizontalAdjust = verticalOffset * headingChange;
            horizontalExtra = horizontalChange - horizontalAdjust;

            positionChangeHorizontal[0] = Math.cos(headingChange) * horizontalExtra;
            positionChangeHorizontal[1] = Math.sin(headingChange) * horizontalExtra;

            //Add the two vectors together
            totalRelativeMovement[0] = positionChangeVertical[0] + positionChangeHorizontal[0];
            totalRelativeMovement[1] = positionChangeVertical[1] + positionChangeHorizontal[1];

            //Rotate the vector
            totalPositionChange[0] = totalRelativeMovement[0] * Math.cos(lastHeadingRadians) - totalRelativeMovement[1] * Math.sin(lastHeadingRadians);
            totalPositionChange[1] = totalRelativeMovement[0] * Math.sin(lastHeadingRadians) + totalRelativeMovement[1] * Math.cos(lastHeadingRadians);

            x = lastX + totalPositionChange[0];
            y = lastY + totalPositionChange[1];

            lastX = x;
            lastY = y;
            lastHeadingRadians = headingRadians;
            lastHeadingImu = headingImu;

            lastVertical = vertical;
            lastHorizontal = horizontal;

            heading = Math.toDegrees(headingRadians);
        }
    }

    // Utility Methods
    public void setEncoderDirections(double verticalDirection, double horizontalDirection){

        this.verticalDirection = verticalDirection;
        this.horizontalDirection = horizontalDirection;

    }

    public double getImuHeading() {
        //May need to change axis unit to work with vertical hubs
        Orientation angles = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        double heading = (angles.firstAngle + 360) % 360;
        //heading = heading * 1.7; // COMPENSATE FOR BAD GYRO CALIBRATION
        return Math.toRadians(heading);
    }

}
