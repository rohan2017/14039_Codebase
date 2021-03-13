package org.firstinspires.ftc.teamcode.Utility;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Timer {

    private ElapsedTime elapsedTime = new ElapsedTime();
    private LinearOpMode opMode;

    public Timer(LinearOpMode opMode){
        this.opMode = opMode;
    }

    public void start(){
        elapsedTime.reset();
    }

    public void waitMillis(double millis){

        double initialTime = elapsedTime.milliseconds();
        double endTime = millis+initialTime;

        while(opMode.opModeIsActive()) {
            if(elapsedTime.milliseconds() > endTime){
                break;
            }
        }

    }

    public double getTimeMillis(){
        return elapsedTime.milliseconds();
    }

}
