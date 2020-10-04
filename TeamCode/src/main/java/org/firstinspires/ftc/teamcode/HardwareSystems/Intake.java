package org.firstinspires.ftc.teamcode.HardwareSystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;

public class Intake {

    private LinearOpMode opMode;
    private RobotHardware hardware;

    public Intake(LinearOpMode opMode, RobotHardware hardware) {

        this.hardware = hardware;
        this.opMode = opMode;

    }

    public void initialize() {

        hardware.intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void setPower(double power) { //Setting a positive power should intake

        hardware.intake.setPower(power);

    }

}
