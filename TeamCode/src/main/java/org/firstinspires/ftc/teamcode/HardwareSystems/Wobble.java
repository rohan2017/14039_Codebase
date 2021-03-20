package org.firstinspires.ftc.teamcode.HardwareSystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Utility.RobotHardware;

public class Wobble {

    private LinearOpMode opMode;
    private RobotHardware hardware;

    private double downPos = 0.032;
    private double upPos = 0.73;

    private double clampedPos = 0.116;
    private double openPos = 0.418;

    private boolean raised;
    private boolean clamped;
    private double armPosition;

    public Wobble(LinearOpMode opMode, RobotHardware hardware){
        this.opMode = opMode;
        this.hardware = hardware;
    }

    public void initialize() {
        raised = false;
        clamped = true;
        update();
        hardware.wobbleLift.setPosition(0.691); //Starting pos so it fits in 18
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

    public void clamp() {
        clamped = true;
    }

    public void unclamp(){
        clamped = false;
    }

    public void toggleClamp(){
        clamped = !clamped;
    }

    public void update() {
        if(opMode.opModeIsActive()) {

            if(raised) {
                armPosition = upPos;
            }else{
                armPosition = downPos;
            }

            if(armPosition >= downPos && armPosition <= upPos) {
                hardware.wobbleLift.setPosition(armPosition);
            }

            if(clamped) {
                hardware.wobbleClamp.setPosition(clampedPos);
            }else{
                hardware.wobbleClamp.setPosition(openPos);
            }
        }
    }


}
