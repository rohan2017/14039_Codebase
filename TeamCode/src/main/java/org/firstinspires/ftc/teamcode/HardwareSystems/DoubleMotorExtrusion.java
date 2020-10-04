package org.firstinspires.ftc.teamcode.HardwareSystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import org.firstinspires.ftc.teamcode.Controllers.Pid;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;

/* An extrusion class to automate all extrusion tasks
The direction of the motors must be such that a positive power causes the encoder to increase
*/

// NOT USED THIS YEAR

public class DoubleMotorExtrusion { /*

    private LinearOpMode opMode;
    private RobotHardware hardware;

    private int liftMax = 99999; // Maximum lift heifght (in encoder readings)
    private int liftCurrent = 0; // Current position of the lift (lift encoder)
    private int liftTarget = 0; // Target position of the lift
    private int[] levels = {0}; // To assign discrete levels to the lift

    // This is figured out by extending the lift to halfway and determining what motor power causes it to stay put
    private double liftWeight = 0.2; //F term of the virtual PIDF controller for this lift
    private Pid controller;

    public DoubleMotorExtrusion(LinearOpMode opMode, RobotHardware hardware, String rightLiftMotorKey, String leftLiftMotorKey){
        this.opMode = opMode;
        this.hardware = hardware;

    }

    public void initialize(){

        if(opMode.opModeIsActive()) {

            //Reversing the motors so they don't fight and move in the right direction
            //The motor directions should be such that a positive power extends the lift
            hardware.liftRight.setDirection(DcMotor.Direction.FORWARD);
            hardware.liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);

            //Reset the encoders
            hardware.liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            hardware.liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            enable();

            controller = new Pid(0.06, 0.001, 0.01, 50, 0.5, 0);

        }

    }

    public void disable() {

        if(opMode.opModeIsActive()) {

            // Make lift motors go limp and stop running
            hardware.liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            hardware.liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            hardware.liftRight.setPower(0);
            hardware.liftLeft.setPower(0);
            hardware.liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            hardware.liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        }

    }

    public void enable() {

        if(opMode.opModeIsActive()) {

            // Gets the lift up and running after a disable()
            hardware.liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            hardware.liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            hardware.liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            hardware.liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }

    }

    public void setTargetPosition(int position){

        if(position > 0 && position < liftMax){
            liftTarget = position;
        }

    }

    public void goToLevelThreadHog(int level){ // 1 means the "first level", notice the -1

        setTargetPosition(levels[level-1]);

        while(opMode.opModeIsActive()){
            update();
        }

    }

    public void update(){

        if(opMode.opModeIsActive()) {

            liftCurrent = (hardware.liftLeft.getCurrentPosition() + hardware.liftRight.getCurrentPosition())/2;
            controller.update(liftTarget, liftCurrent);
            setPower(controller.correction + liftWeight);

        }

    }

    public void setPower(double power){

        if(opMode.opModeIsActive()) {

            hardware.liftRight.setPower(power);
            hardware.liftLeft.setPower(power);

        }

    }*/

}