package org.firstinspires.ftc.teamcode.Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;

@TeleOp(name="Tele-Op", group="TeleOp")

public class teleOp extends LinearOpMode {

    // Declare OpMode members.
    private RobotHardware hardware = new RobotHardware();
    private MecanumDrive drivetrain;

    private void initialize() {

        hardware.hardwareMap(hardwareMap);
        drivetrain = new MecanumDrive(this, hardware);
        drivetrain.initialize();

        telemetry.addData("Status", "Initialized - Welcome, Operators");
        telemetry.update();

    }

    @Override
    public void runOpMode() {

        // Wait for the game to start (driver presses PLAY)
        initialize();
        waitForStart();

        while(opModeIsActive()) {

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

        }

    }

}