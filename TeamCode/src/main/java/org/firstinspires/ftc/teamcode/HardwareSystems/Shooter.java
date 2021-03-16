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
    public boolean hopperPrimed;

    private double hopperUp = 0.435;
    private double hopperDown = 0.245;

    private double shooterPrime = 0.08;
    private double shooterFeed = 0.35;


    public Shooter(LinearOpMode opMode, RobotHardware hardware, Timer time) {
        this.opMode = opMode;
        this.hardware = hardware;
        this.time = time;
    }

    public void initialize() {
        power = 0.;
        revving = false;
        hopperPrimed = false;
        hardware.shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        hardware.shooterLeft.setDirection(DcMotor.Direction.FORWARD);
        hardware.shooterRight.setDirection(DcMotor.Direction.FORWARD);

        hardware.shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        hardware.shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void toggleShooter() {
        revving = !revving;
    }

    public void autoAim(double distance) {
        //TODO
    }

    public void setShooterAngle(double angle) {
        //Do stuff to the servo angle here
        hardware.shooterAngle.setPosition(angle);
    }

    public void setShooterPower(double input_power) {
        power = input_power;
    }

    public void feedDisk() {
        if(revving && hopperPrimed && opMode.opModeIsActive()) {
            hardware.shooterFeed.setPosition(shooterFeed); //These values need to be changed
            time.waitMillis(150);
            hardware.shooterFeed.setPosition(shooterPrime);
            time.waitMillis(200);
        }
    }

    public void hopperUp() {
        hopperPrimed = true;
    }

    public void hopperDown() {
        hopperPrimed = false;
    }

    public void toggleHopper() {
        hopperPrimed = !hopperPrimed;
    }

    public void update() {
        if(hopperPrimed) {
            hardware.hopperLift.setPosition(hopperUp);
        }else {
            hardware.hopperLift.setPosition(hopperDown);
        }

        if(revving && opMode.opModeIsActive()) {
            hardware.shooterLeft.setPower(power);
            hardware.shooterRight.setPower(power);
        }else {
            hardware.shooterLeft.setPower(0);
            hardware.shooterRight.setPower(0);
        }
    }

    public void incrementPower(double increment) {
        power += increment;
    }

}
