package org.firstinspires.ftc.teamcode.Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.CustomCV.BluePipeline;
import org.firstinspires.ftc.teamcode.CustomCV.RedPipeline;
import org.firstinspires.ftc.teamcode.HardwareSystems.ActionHandler;
import org.firstinspires.ftc.teamcode.HardwareSystems.Intake;
import org.firstinspires.ftc.teamcode.HardwareSystems.Shooter;
import org.firstinspires.ftc.teamcode.Movement.Localization.OdometerIMU2W;
import org.firstinspires.ftc.teamcode.Movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.Movement.MotionPlanning.RobotPoint;
import org.firstinspires.ftc.teamcode.Movement.Movement;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;
import org.firstinspires.ftc.teamcode.Utility.Timer;
import org.openftc.easyopencv.OpenCvCamera;
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
    // Vision
    private BluePipeline pipeline;
    private OpenCvCamera phoneCam;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        timer.start();
        odometer.startTracking(0, 0, 0);
        telemetry.addData("status", "running");
        telemetry.update();

        shooter.hopperDown();
        shooter.update();
        timer.waitMillis(500);
        intake.setPower(0.6);
        intake.update(shooter.hopperPrimed);
        timer.waitMillis(1000);

        movement.moveToPointPD(new RobotPoint(0,40 ,0,0),20,2);
        timer.waitMillis(4000);

        shooter.hopperUp();
        shooter.update();
        timer.waitMillis(500);

        shooter.toggleShooter();
        shooter.setShooterPower(0.8);
        shooter.update();
        timer.waitMillis(1000);

        shooter.feedDisk();
        shooter.feedDisk();
        shooter.feedDisk();
        shooter.setShooterPower(0);
        shooter.update();
    }

    private void initialize(){

        hardware.hardwareMap(hardwareMap);
/*
        // CV
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();

        pipeline = new BluePipeline();
        phoneCam.setPipeline(pipeline);
        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
*/

        drivetrain = new MecanumDrive(this, hardware);
        odometer = new OdometerIMU2W(this, hardware);
        timer = new Timer(this, odometer);
        movement = new Movement(this, drivetrain, odometer, timer);
        handler = new ActionHandler();
        movement.setActionHandler(handler);
        movement.useActionHandler = true;
        shooter = new Shooter(this, hardware, timer);
        intake = new Intake(this, hardware);

        drivetrain.initialize();
        odometer.initialize();
        shooter.initialize();
        intake.initialize();
        odometer.initialize();

        telemetry.addData("status","initialized");
        telemetry.update();

    }

}
