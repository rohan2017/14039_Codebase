package org.firstinspires.ftc.teamcode.Opmodes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.teamcode.HardwareSystems.ActionHandler;
import org.firstinspires.ftc.teamcode.HardwareSystems.Intake;
import org.firstinspires.ftc.teamcode.HardwareSystems.Shooter;
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
    private Timer timer;
    private OdometerIMU2W odometer;
    private MecanumDrive drivetrain;
    private Movement movement;
    private ActionHandler handler;
    private Shooter shooter;
    private Intake intake;

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
