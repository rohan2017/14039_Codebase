package org.firstinspires.ftc.teamcode.HardwareSystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Utility.RobotHardware;
import org.firstinspires.ftc.teamcode.Utility.Timer;

public class Shooter {

    private LinearOpMode opMode;
    private RobotHardware hardware;
    private Timer time;

    private double power;
    private boolean revving;

    public Shooter(LinearOpMode opMode, RobotHardware hardware, Timer time) {
        this.opMode = opMode;
        this.hardware = hardware;
        this.time = time;
    }

    public void initialize() {
        power = 0;
        revving = false;
        hardware.shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        hardware.shooterLeft.setDirection(DcMotor.Direction.REVERSE);
        hardware.shooterRight.setDirection(DcMotor.Direction.FORWARD);

        hardware.shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        hardware.shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void toggleShooter() {
        if(revving) {
            revving = false;
        }else if(!revving) {
            revving = true;
        }
    }

    public void autoAim(double distance) {

    }

    public void setShooterAngle(double angle) {
        //Do stuff to the servo angle here
        hardware.shooterAngle.setPosition(angle);
    }

    public void setShooterPower(double power) {
        if(revving && opMode.opModeIsActive()) {
            hardware.shooterLeft.setPower(power);
            hardware.shooterRight.setPower(power);
        }else {
            hardware.shooterLeft.setPower(0);
            hardware.shooterRight.setPower(0);
        }
    }

    public void feedDisk() {
        if(revving && opMode.opModeIsActive()) {
            hardware.shooterFeed.setPosition(0.5); //These values need to be changed
            hardware.shooterFeed.setPosition(0.1);
        }
    }

    public void shoot(double angle, double power) {

        setShooterAngle(angle);
        setShooterPower(power);
        feedDisk();

    }

}
