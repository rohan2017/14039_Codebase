package org.firstinspires.ftc.teamcode.Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.HardwareSystems.Intake;
import org.firstinspires.ftc.teamcode.HardwareSystems.Shooter;
import org.firstinspires.ftc.teamcode.Movement.Localization.OdometerIMU2W;
import org.firstinspires.ftc.teamcode.Movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;
import org.firstinspires.ftc.teamcode.Utility.Timer;

@TeleOp(name="Tele-Op", group="TeleOp")

public class teleOp extends LinearOpMode {

    // Declare OpMode members.
    private RobotHardware hardware = new RobotHardware();
    private MecanumDrive drivetrain;
    private OdometerIMU2W odometer;
    private Shooter shooter;
    private Intake intake;
    private Timer time;

    private void initialize() {

        hardware.hardwareMap(hardwareMap);
        time = new Timer(this);
        drivetrain = new MecanumDrive(this, hardware);
        odometer = new OdometerIMU2W(this, hardware);
        shooter = new Shooter(this, hardware, time);
        intake = new Intake(this, hardware);
        drivetrain.initialize();
        odometer.initialize();
        shooter.initialize();
        intake.initialize();

        telemetry.addData("Status", "Initialized - Welcome, Operators");
        telemetry.update();

    }

    @Override
    public void runOpMode() {

        // Wait for the game to start (driver presses PLAY)
        initialize();
        waitForStart();
        time.start();
        odometer.startTracking(0, 0, 0);
        shooter.toggleShooter();

        while(opModeIsActive()) {

            // DRIVING
            double powerScale;
            double x1, x2, y1, y2;

            if(gamepad1.left_bumper) {
                powerScale = 0.6;
            }else if(gamepad1.right_bumper) {
                powerScale = 0.2;
            }else {
                powerScale = 1;
            }

            y1 = -gamepad1.right_stick_y;
            x1 = -gamepad1.right_stick_x;
            x2 = -gamepad1.left_stick_x;
            y2 = -gamepad1.left_stick_y;

            drivetrain.rf = (y1 + x1) * powerScale;
            drivetrain.rb = (y1 - x1) * powerScale;
            drivetrain.lf = (y2 - x2) * powerScale;
            drivetrain.lb = (y2 + x2) * powerScale;

            drivetrain.update();

            // SHOOTER
            shooter.setShooterPower(gamepad1.left_trigger);

            // INTAKE
            intake.setPower(gamepad1.right_trigger);

        }

    }

}