package org.firstinspires.ftc.teamcode.Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Movement.Localization.OdometerIMU2W;
import org.firstinspires.ftc.teamcode.Movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.Movement.MotionPlanning.RobotPoint;
import org.firstinspires.ftc.teamcode.Movement.Movement;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;
import org.firstinspires.ftc.teamcode.Utility.Timer;
import java.util.ArrayList;

@Autonomous(name="Movement Test", group="Testing")
public class movementTest extends LinearOpMode {

    // Declare OpMode Members
    private OdometerIMU2W odometer;
    private MecanumDrive drivetrain;
    private Movement movement;
    private Timer timer;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        timer.start();
        odometer.startTracking(0, 0, 0);
        telemetry.addData("status","running");
        telemetry.update();

        movement.setGlobalVelocity(0.4, 0, 0.8);

        timer.waitMillis(5000);

        drivetrain.freeze();

    }

    private void initialize(){
        RobotHardware.hardwareMap(hardwareMap);

        odometer = new OdometerIMU2W();
        drivetrain = new MecanumDrive();
        timer = new Timer(this, odometer);
        movement = new Movement(this, drivetrain, odometer);
        drivetrain.initialize();
        odometer.initialize();

        telemetry.addData("status","initialized");
        telemetry.update();

    }
}