package org.firstinspires.ftc.teamcode.Opmodes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HardwareSystems.ActionHandler;
import org.firstinspires.ftc.teamcode.HardwareSystems.Intake;
import org.firstinspires.ftc.teamcode.HardwareSystems.Shooter;
import org.firstinspires.ftc.teamcode.Movement.Localization.OdometerIMU2W;
import org.firstinspires.ftc.teamcode.Movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.Movement.MotionPlanning.PathingAgent;
import org.firstinspires.ftc.teamcode.Movement.MotionPlanning.RobotPoint;
import org.firstinspires.ftc.teamcode.Movement.Movement;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;
import org.firstinspires.ftc.teamcode.Utility.Timer;
import java.util.ArrayList;

@Autonomous(name="Pure Pursuit Test", group="Testing")
@Disabled
public class purePursuitTest extends LinearOpMode {

    // Declare OpMode Members
    private RobotHardware hardware = new RobotHardware();
    private Timer timer;
    private OdometerIMU2W odometer;
    private MecanumDrive drivetrain;
    private Movement movement;
    private ActionHandler handler;
    private Shooter shooter;
    private Intake intake;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        timer.start();
        odometer.startTracking(0, 0, 0);
        telemetry.addData("status","running");
        telemetry.update();

        ArrayList<RobotPoint> deliverPath = new ArrayList<>();
        deliverPath.add(new RobotPoint(55, 75.5, -90, 0.8));
        deliverPath.add(new RobotPoint(-40, 30, -90, 0.5));
        deliverPath.add(new RobotPoint(-165, 63, -90, 0.7));
        deliverPath.add(new RobotPoint(-175, 65, -90, 0.8));

        while(opModeIsActive()) {
            //RobotPoint target = PathingAgent.getTargetPoint(odometer.x, odometer.y, 50, deliverPath);
            movement.pointInDirection(90, 0.3);
            movement.pointInDirection(0, 0.3);
            movement.pointInDirection(-90, 0.3);
            movement.pointInDirection(0, 0.3);
            movement.pointInDirection(3, 0.3);
            movement.pointInDirection(0, 0.3);
            movement.pointInDirection(-3, 0.3);
            telemetry.addData("RobotX", odometer.x);
            telemetry.addData("RobotY", odometer.y);

            telemetry.update();
            odometer.update();
        }

    }

    private void initialize(){
        hardware.hardwareMap(hardwareMap);

        drivetrain = new MecanumDrive(this, hardware);
        odometer = new OdometerIMU2W(this, hardware);
        timer = new Timer(this, odometer);
        movement = new Movement(this, drivetrain, odometer, timer);
        handler = new ActionHandler();
        movement.setActionHandler(handler);
        movement.useActionHandler = true;
        shooter = new Shooter(this, hardware, timer);
        intake = new Intake(this, hardware);

        drivetrain.initialize();
        odometer.initialize();
        shooter.initialize();
        intake.initialize();
        odometer.initialize();

        telemetry.addData("status","initialized");
        telemetry.update();

    }

}
