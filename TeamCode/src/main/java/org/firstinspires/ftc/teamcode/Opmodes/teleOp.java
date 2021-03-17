package org.firstinspires.ftc.teamcode.Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.HardwareSystems.Intake;
import org.firstinspires.ftc.teamcode.HardwareSystems.Shooter;
import org.firstinspires.ftc.teamcode.HardwareSystems.Wobble;
import org.firstinspires.ftc.teamcode.Movement.Localization.OdometerIMU2W;
import org.firstinspires.ftc.teamcode.Movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.Movement.Movement;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;
import org.firstinspires.ftc.teamcode.Utility.Timer;

@TeleOp(name="Tele-Op", group="TeleOp")

public class teleOp extends LinearOpMode {

    // Declare OpMode members.
    private RobotHardware hardware = new RobotHardware();
    private MecanumDrive drivetrain;
    private Movement movement;
    private OdometerIMU2W odometer;
    private Shooter shooter;
    private Intake intake;
    private Wobble wobble;
    private Timer time;

    private void initialize() {

        hardware.hardwareMap(hardwareMap);
        drivetrain = new MecanumDrive(this, hardware);
        odometer = new OdometerIMU2W(this, hardware);
        time = new Timer(this, odometer);
        movement = new Movement(this, drivetrain, odometer, time);
        shooter = new Shooter(this, hardware, time);
        intake = new Intake(this, hardware);
        wobble = new Wobble(this, hardware);
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
        shooter.setShooterAngle(0.99);
        shooter.setShooterPower(0.4);

        boolean a_released = false;
        boolean b_released = false;

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
            if(!gamepad2.a && a_released) {
                shooter.toggleShooter();
            }
            a_released = gamepad2.a;

            if(!gamepad2.b && b_released) {
                shooter.toggleHopper();
            }
            b_released = gamepad2.b;

            if(gamepad2.right_trigger > 0.5) {
                shooter.feedDisk();
                time.waitMillis(300);
            }

            if(gamepad2.dpad_up) {
                shooter.incrementPower(0.005);
            }else if(gamepad2.dpad_down) {
                shooter.incrementPower(-0.005);
            }

            if(gamepad2.dpad_right) {
                shooter.incrementAngle(0.005);
            }else if(gamepad2.dpad_left) {
                shooter.incrementAngle(-0.005);
            }

            shooter.update();

            // INTAKE
            intake.setPower(gamepad2.left_stick_y);
            intake.update(shooter.hopperPrimed);

            // WOBBLE


            odometer.update();

            telemetry.addData("X", odometer.x);
            telemetry.addData("Y", odometer.y);
            telemetry.addData("Heading", odometer.heading);
            telemetry.addData("ShooterPower", shooter.getPower());
            telemetry.addData("ShooterAngle", shooter.getAngle());
            telemetry.update();

        }

    }

}