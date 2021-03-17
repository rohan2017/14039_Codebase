package org.firstinspires.ftc.teamcode.Utility;

import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class RobotHardware {

    //Drive Motors
    public DcMotor rightFront, leftFront, leftBack, rightBack; //Drive Motor Objects
    //IMU
    public BNO055IMU imu;
    //Intake Motors
    public DcMotor intakeLeft, intakeRight;
    //Shooter Motors
    public DcMotor shooterLeft, shooterRight;
    public Servo shooterAngle, shooterFeed, hopperLift;
    public Servo wobbleLift, wobbleClamp;

    public void hardwareMap(HardwareMap hardwareMap) {

        //Drive-train
        rightFront = hardwareMap.dcMotor.get("driveFrontRight");
        leftFront = hardwareMap.dcMotor.get("driveFrontLeft");
        leftBack = hardwareMap.dcMotor.get("driveBackLeft");
        rightBack = hardwareMap.dcMotor.get("driveBackRight");

        //Intake
        intakeLeft = hardwareMap.dcMotor.get("intakeLeft");
        intakeRight = hardwareMap.dcMotor.get("intakeRight");

        //Shooter
        shooterLeft = hardwareMap.dcMotor.get("shooterLeft");
        shooterRight = hardwareMap.dcMotor.get("shooterRight");
        shooterAngle = hardwareMap.servo.get("shooterAngle");
        shooterFeed = hardwareMap.servo.get("shooterFeed");
        hopperLift = hardwareMap.servo.get("hopperLift");

        // Wobble
        wobbleLift = hardwareMap.servo.get("wobbleLift");
        wobbleClamp = hardwareMap.servo.get("wobbleClamp");

        //IMU
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters Params = new BNO055IMU.Parameters();
        Params.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        Params.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        Params.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opMode
        Params.loggingEnabled      = true;
        Params.loggingTag          = "IMU";
        Params.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(Params);

    }

    public void testHardware(){

        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightFront.setPower(0.3);

    }



}