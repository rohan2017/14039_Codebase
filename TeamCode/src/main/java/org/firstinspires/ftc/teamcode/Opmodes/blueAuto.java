package org.firstinspires.ftc.teamcode.Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.CustomCV.RingStackPipeline;
import org.firstinspires.ftc.teamcode.HardwareSystems.ActionHandler;
import org.firstinspires.ftc.teamcode.HardwareSystems.Intake;
import org.firstinspires.ftc.teamcode.HardwareSystems.Shooter;
import org.firstinspires.ftc.teamcode.HardwareSystems.Wobble;
import org.firstinspires.ftc.teamcode.Movement.Localization.OdometerIMU2W;
import org.firstinspires.ftc.teamcode.Movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.Movement.MotionPlanning.RobotPoint;
import org.firstinspires.ftc.teamcode.Movement.Movement;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;
import org.firstinspires.ftc.teamcode.Utility.Timer;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="Blue Auto", group="Auto")
public class blueAuto extends LinearOpMode {

    // Declare OpMode Members
    private RobotHardware hardware = new RobotHardware();
    private Timer timer;
    private OdometerIMU2W odometer;
    private MecanumDrive drivetrain;
    private Movement movement;
    private ActionHandler handler;
    private Shooter shooter;
    private Intake intake;
    private Wobble wobble;
    // Vision
    private RingStackPipeline pipeline;
    private OpenCvCamera phoneCam;
    private int rings;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        timer.start();
        telemetry.addData("status", "running");
        telemetry.update();

        if(pipeline.position == RingStackPipeline.RingPosition.FOUR) {
            rings = 4;
        }else if(pipeline.position == RingStackPipeline.RingPosition.ONE) {
            rings = 1;
        }else {
            rings = 0;
        }

        telemetry.addData("avg", pipeline.position);
        telemetry.addData("block", pipeline.getAnalysis());
        telemetry.update();

        phoneCam.stopStreaming();

        odometer.startTracking(0, 0, 0);

        // Raise wobble
        wobble.raiseArm();
        wobble.update();

        // Hopper in shooting position and start revving shooter
        shooter.hopperUp();
        shooter.setShooterAngle(0.99);
        shooter.toggleShooter();
        shooter.setShooterPower(0.47);
        shooter.update();

        movement.moveToPointPD(new RobotPoint(5,50 ,0,0),20,20);

        // Move to first point and shoot powershot
        movement.moveToPointPD(new RobotPoint(43,140 ,-10,0),20,3);
        movement.pointInDirection(-9, 0.7);
        shooter.feedDisk();
        timer.waitMillis(100);
        movement.pointInDirection(-3, 0.7);
        shooter.feedDisk();
        timer.waitMillis(100);
        movement.pointInDirection(2, 0.7);
        shooter.feedDisk();

        //rev-down shooter and run intake
        shooter.setShooterPower(0);
        shooter.hopperDown();
        shooter.update();

        if (rings == 0){
            movement.moveToPointPD(new RobotPoint(-2,195 ,0,0),20,5);
            timer.waitMillis(300);
            wobble.lowerArm();
            timer.waitMillis(500);
            wobble.unclamp();
            timer.waitMillis(300);
            wobble.update();
            timer.waitMillis(300);
            wobble.raiseArm();
            wobble.update();
        } else if(rings == 1){
            movement.moveToPointPD(new RobotPoint(53,260 ,0,0),20,5);
            timer.waitMillis(300);
            wobble.lowerArm();
            timer.waitMillis(500);
            wobble.unclamp();
            timer.waitMillis(300);
            wobble.update();
            timer.waitMillis(300);
            wobble.raiseArm();
            wobble.update();
        } else {
            movement.moveToPointPD(new RobotPoint(-8,310 ,-5,0),20,5);
            timer.waitMillis(300);
            wobble.lowerArm();
            timer.waitMillis(500);
            wobble.unclamp();
            timer.waitMillis(300);
            wobble.update();
            timer.waitMillis(300);
            wobble.raiseArm();
            wobble.update();
        }
/*
        movement.pointInDirection(-180,2);

        if (rings == 4) {
            intake.setPower(0.8);
            intake.update(false);
            shooter.hopperDown();
            shooter.update();


            movement.moveToPointPD(new RobotPoint(-40, 115, -180, 0), 20, 12);
            //intake.setPower(-0.5);
            //intake.update(false);
            //movement.deadReckon(0, -0.7, 0, 50);



            movement.deadReckon(0, -0.4, 0, 1000);

            movement.pointInDirection(-360, 3);

            movement.moveToPointPD(new RobotPoint(0, 51, -360, 0), 20, 6);
            wobble.lowerArm();
            wobble.update();
            movement.deadReckon(-0.25, 0, 0, 500);
            wobble.clamp();
            wobble.update();
            timer.waitMillis(300);
            wobble.raiseArm();
            timer.waitMillis(300);

            movement.moveToPointPD(new RobotPoint(-5, 155, -360, 0), 20, 6);

            shooter.hopperUp();
            shooter.setShooterAngle(0.8);
            shooter.setShooterPower(0.6);
            shooter.update();

            timer.waitMillis(800);
            shooter.feedDisk();
            timer.waitMillis(300);
            shooter.feedDisk();
            timer.waitMillis(300);
            shooter.feedDisk();
            timer.waitMillis(300);

        }else if(rings == 1) {
            shooter.hopperDown();
            shooter.update();

            intake.setPower(0.6);
            intake.update(false);
            movement.moveToPointPD(new RobotPoint(-40, 109, -180, 0), 20, 11);
            movement.deadReckon(0, -0.5, 0, 700);
            timer.waitMillis(300);

            //movement.pointInDirection(-360, 10);

            movement.moveToPointPD(new RobotPoint(0, 55, -360, 0), 20, 5);
            wobble.lowerArm();
            wobble.update();
            movement.deadReckon(-0.3, 0, 0, 300);
            wobble.clamp();
            wobble.update();
            timer.waitMillis(300);
            wobble.raiseArm();
            timer.waitMillis(300);

            movement.moveToPointPD(new RobotPoint(-5, 155, -360, 0), 20, 5);

            shooter.hopperUp();
            shooter.setShooterAngle(0.8);
            shooter.setShooterPower(0.6);
            shooter.update();

            timer.waitMillis(800);
            shooter.feedDisk();
            timer.waitMillis(300);

        }else {

            //movement.pointInDirection(-360, 10);

            movement.moveToPointPD(new RobotPoint(0, 55, -360, 0), 20, 5);
            wobble.lowerArm();
            wobble.update();
            movement.deadReckon(-0.2, 0, 0, 300);
            wobble.clamp();
            wobble.update();
            timer.waitMillis(300);
            wobble.raiseArm();
            timer.waitMillis(300);

        }

        if (rings == 0){
            wobble.raiseArm();
            wobble.update();
            movement.moveToPointPD(new RobotPoint(-2,195 ,-360,0),20,7);
            wobble.lowerArm();
            wobble.unclamp();
            wobble.update();
            timer.waitMillis(300);
            wobble.raiseArm();
            wobble.update();
        } else if(rings == 1){
            wobble.raiseArm();
            wobble.update();
            movement.moveToPointPD(new RobotPoint(53,260 ,-360,0),20,7);
            wobble.lowerArm();
            wobble.unclamp();
            wobble.update();
            timer.waitMillis(300);
            wobble.raiseArm();
            wobble.update();
        } else {
            wobble.raiseArm();
            wobble.update();
            movement.moveToPointPD(new RobotPoint(-8,304 ,-360,0),20,7);
            wobble.lowerArm();
            wobble.unclamp();
            wobble.update();
            timer.waitMillis(300);
            wobble.raiseArm();
            wobble.update();
        }
*/
        movement.moveToPointPD(new RobotPoint(0, 170, 0, 0), 20, 0.5);

    }

    private void initialize(){

        hardware.hardwareMap(hardwareMap);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();

        pipeline = new RingStackPipeline();
        pipeline.setLinearOpMode(this);
        phoneCam.setPipeline(pipeline);
        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        drivetrain = new MecanumDrive(this, hardware);
        odometer = new OdometerIMU2W(this, hardware);
        timer = new Timer(this, odometer);
        movement = new Movement(this, drivetrain, odometer, timer);
        handler = new ActionHandler();
        movement.setActionHandler(handler);
        movement.useActionHandler = true;
        shooter = new Shooter(this, hardware, timer);
        intake = new Intake(this, hardware);
        wobble = new Wobble(this, hardware);

        drivetrain.initialize();
        odometer.initialize();
        odometer.initialize();
        shooter.initialize();
        intake.initialize();
        wobble.initialize();
        hardware.wobbleClamp.setPosition(0.116);

        telemetry.addData("status","initialized");
        telemetry.update();

    }

}
