package org.firstinspires.ftc.teamcode.HardwareSystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;

public class Outtake {

    private LinearOpMode opMode;
    private RobotHardware hardware;

    public Outtake(LinearOpMode opMode, RobotHardware hardware) {

        this.opMode = opMode;
        this.hardware = hardware;

    }

    public void initialize() {

    }

    public void setShooterAngle(double angle) {

    }

    public void setShooterSpeed(double power) {

    }

    public void feedDisk() {

    }

    public void shoot(double angle, double power) {

        setShooterAngle(angle);
        setShooterSpeed(power);
        feedDisk();

    }

}
