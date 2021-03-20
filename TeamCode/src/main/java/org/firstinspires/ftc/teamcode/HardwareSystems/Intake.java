package org.firstinspires.ftc.teamcode.HardwareSystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Utility.RobotHardware;

public class Intake {

    private LinearOpMode opMode;
    private RobotHardware hardware;

    private double power;

    public Intake(LinearOpMode opMode1, RobotHardware hardware1) {

        this.hardware = hardware1;
        this.opMode = opMode1;

    }

    public void initialize() {

        hardware.intakeLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hardware.intakeRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        hardware.intakeLeft.setDirection(DcMotor.Direction.REVERSE);
        hardware.intakeRight.setDirection(DcMotor.Direction.FORWARD);

        hardware.intakeLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        hardware.intakeRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

    }

    public void setPower(double power) { //Setting a positive power should intake
        this.power = -power;
    }

    public void update(boolean hopperUp) {
        if(opMode.opModeIsActive()){
            if((!hopperUp && power <= 0) || (power >= 0)){
                hardware.intakeLeft.setPower(power);
                hardware.intakeRight.setPower(power);
            } else{
                hardware.intakeLeft.setPower(0);
                hardware.intakeRight.setPower(0);
            }
        }
    }

}
