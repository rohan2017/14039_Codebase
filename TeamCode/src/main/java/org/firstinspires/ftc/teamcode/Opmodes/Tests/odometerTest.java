package org.firstinspires.ftc.teamcode.Opmodes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.teamcode.Movement.Localization.Odometer3W;
import org.firstinspires.ftc.teamcode.Movement.Localization.OdometerIMU2W;
import org.firstinspires.ftc.teamcode.Movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.Movement.Movement;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;
import org.firstinspires.ftc.teamcode.Utility.Timer;

@Autonomous(name="Odometer Test", group="Testing")

public class odometerTest extends LinearOpMode {

    // Declare OpMode Members
    private RobotHardware hardware = new RobotHardware();
    private OdometerIMU2W odometer;
    //private OdometerIMU2W odometer;
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

        while(opModeIsActive()){

            odometer.update();
            telemetry.addData("X", odometer.x);
            telemetry.addData("Y", odometer.y);
            telemetry.addData("Heading", odometer.heading);
            //telemetry.addData("Horizontal", odometer.horizontal);
            telemetry.update();

        }

    }

    private void initialize(){
        hardware.hardwareMap(hardwareMap);

        odometer = new OdometerIMU2W(this, hardware);
        drivetrain = new MecanumDrive(this, hardware);
        timer = new Timer(this);
        movement = new Movement(this, drivetrain, odometer, timer);

        drivetrain.initialize();
        odometer.initialize();

        telemetry.addData("status","initialized");
        telemetry.update();

    }

}
