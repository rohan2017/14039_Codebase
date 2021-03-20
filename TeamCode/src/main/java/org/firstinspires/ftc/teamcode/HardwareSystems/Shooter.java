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
    private double angle;

    private double hopperUp = 0.435;
    private double hopperDown = 0.245;

    private double shooterPrime = 0.08;
    private double shooterFeed = 0.35;

    private double angleDown = 1.0;
    private double angleUp = 0.5;

    private double[] distances = {210, 230, 250, 270,290};
    private double[] powers = {0.6, 0.6, 0.55, 0.54, 0.51 };
    private double[] angles = {0.99, 0.99, 0.99, 0.99, 0.99};

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

        hardware.shooterLeft.setDirection(DcMotor.Direction.REVERSE);
        hardware.shooterRight.setDirection(DcMotor.Direction.REVERSE);

        hardware.shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        hardware.shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        setShooterAngle(angleUp);

        update();
    }

    public void toggleShooter() {
        revving = !revving;
    }

    public void autoAim(double currentX, double currentY, double targetX, double targetY) {

        int key = 0;
        double ratio = 0;
        double angle1=0,power1=0;

        double distance = Math.hypot(Math.abs(targetX-currentX), Math.abs(targetY-currentY));

        if(distance <= 290 && distance >= 210) {
            for(int i=0;i<distances.length;i++) {
                if(distance > distances[i]) {
                    key = i;
                    ratio = (distances[i+1]-distance)/(distances[i+1]-distances[i]);
                }
            }

             angle1 = (angles[key+1] - angles[key]) * ratio + angles[key];
             power1 = (powers[key+1] - powers[key]) * ratio + powers[key];
        }

        angle = angle1;
        power = power1;
    }

    public void setShooterAngle(double angle) {
        //Do stuff to the servo angle here
        this.angle = angle;
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

    public void powerShots(){


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

        if(revving && opMode.opModeIsActive()) {
            hardware.shooterLeft.setPower(power);
            hardware.shooterRight.setPower(power);
        }else {
            hardware.shooterLeft.setPower(0);
            hardware.shooterRight.setPower(0);
        }

        if(hopperPrimed) {
            hardware.shooterFeed.setPosition(shooterPrime);
            hardware.hopperLift.setPosition(hopperUp);
        }else {
            hardware.shooterFeed.setPosition(shooterPrime);
            hardware.hopperLift.setPosition(hopperDown);
        }

        if(angle <= angleDown && angle >= angleUp) {
            hardware.shooterAngle.setPosition(angle);
        }

    }

    public void incrementPower(double increment) {
        power += increment;
    }

    public void incrementAngle(double increment) {
        angle += increment;
    }

    public double getPower(){
        return power;
    }

    public double getAngle() {
        return angle;
    }

}
