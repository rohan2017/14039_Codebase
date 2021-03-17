package org.firstinspires.ftc.teamcode.HardwareSystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Utility.RobotHardware;

public class Wobble {

    private LinearOpMode opMode;
    private RobotHardware hardware;

    private double downPos = 0.04;
    private double upPos = 0.75;

    private double clampedPos = 0;
    private double openPos = 0;

    private boolean raised;
    private double armPosition;

    public Wobble(LinearOpMode opMode, RobotHardware hardware){
        this.opMode = opMode;
        this.hardware = hardware;
    }

    public void initialize() {
        raised = false;
    }

    public void clamp() {
        hardware.wobbleLift.setPosition(0);
    }

    public void raiseArm() {
        raised = true;
    }

    public void lowerArm() {
        raised = false;
    }

    public void toggleArm() {
        raised = !raised;
    }

    public void update() {
        if(opMode.opModeIsActive()) {

            if(raised) {
                armPosition = upPos;
            }else{
                armPosition = downPos;
            }

            if(armPosition > downPos && armPosition < upPos) {
                hardware.wobbleLift.setPosition(armPosition);
            }
        }
    }


}
