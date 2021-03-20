package org.firstinspires.ftc.teamcode.Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.HardwareSystems.Intake;
import org.firstinspires.ftc.teamcode.HardwareSystems.Shooter;
import org.firstinspires.ftc.teamcode.HardwareSystems.Wobble;
import org.firstinspires.ftc.teamcode.Movement.Localization.OdometerIMU2W;
import org.firstinspires.ftc.teamcode.Movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.Movement.MotionPlanning.RobotPoint;
import org.firstinspires.ftc.teamcode.Movement.Movement;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;
import org.firstinspires.ftc.teamcode.Utility.Timer;

@TeleOp(name="Tele-Op", group="TeleOp")

public class teleOp extends LinearOpMode {

    // Declare OpMode members.
    private RobotHardware hardware = new RobotHardware();
    private MecanumDrive drivetrain;
    private Movement movement;
    private OdometerIMU2W odometer;
    private Shooter shooter;
    private Intake intake;
    private Wobble wobble;
    private Timer time;

    private double goalX = -5;
    private double goalY = 360;

    private void initialize() {

        hardware.hardwareMap(hardwareMap);
        drivetrain = new MecanumDrive(this, hardware);
        odometer = new OdometerIMU2W(this, hardware);
        time = new Timer(this, odometer);
        movement = new Movement(this, drivetrain, odometer, time);
        shooter = new Shooter(this, hardware, time);
        intake = new Intake(this, hardware);
        wobble = new Wobble(this, hardware);
        drivetrain.initialize();
        drivetrain.setPowerBehavior("float");
        odometer.initialize();
        shooter.initialize();
        intake.initialize();

        telemetry.addData("Status", "Initialized - Welcome, Operators");
        telemetry.update();

    }

    @Override
    public void runOpMode() {

        // Wait for the game to start (driver presses PLAY)
        initialize();
        waitForStart();
        time.start();
        odometer.startTracking(0, 170, 0);

        shooter.setShooterAngle(0.99);
        shooter.setShooterPower(0.4);

        boolean released1 = false;
        boolean released2 = false;
        boolean released3 = false;
        boolean released4 = false;
        boolean released5 = false;
        boolean released6 = false;

        double intake_multiplier = 1;

        while(opModeIsActive()) {

            //CONTROLS
            boolean slow_drive = gamepad1.right_bumper;
            boolean shoot_ring = gamepad1.right_trigger > 0.5; //hold down for all 3
            boolean toggleHopper = gamepad1.b;
            boolean shooter_toggle_rev = gamepad1.a;
            boolean increase_shooter_power = gamepad1.dpad_up;
            boolean decrease_shooter_power = gamepad1.dpad_down;
            boolean increase_shooter_angle = gamepad1.dpad_right;
            boolean decrease_shooter_angle = gamepad1.dpad_left;
            boolean auto_aim = gamepad1.b;

            boolean powershot = gamepad1.left_stick_button && gamepad1.right_stick_button;

            double intake_power_ctrl = gamepad1.left_trigger;
            double intake_power;
            if(intake_power_ctrl < 0.3) {
                intake_power = 0;
            }else{
                intake_power = intake_power_ctrl;
            }
            boolean reverse_intake = gamepad1.left_bumper;

            boolean clamp_unclamp_wobble = gamepad1.x;
            boolean raise_lower_wobble = gamepad1.y;

            // DRIVING
            double powerScale;
            double x1, x2, y1, y2;

            if(slow_drive) {
                powerScale = 0.6;
            }else {
                powerScale = 1;
            }

            y1 = -gamepad1.right_stick_y;
            x1 = -gamepad1.right_stick_x;
            x2 = -gamepad1.left_stick_x;
            y2 = -gamepad1.left_stick_y;

            drivetrain.rf = (y1 + x1) * powerScale;
            drivetrain.rb = (y1 - x1) * powerScale;
            drivetrain.lf = (y2 - x2) * powerScale;
            drivetrain.lb = (y2 + x2) * powerScale;

            drivetrain.update();
            if(gamepad1.dpad_up){

            }
            // SHOOTER
            if(!auto_aim && released3) {
                movement.pointTowardsPoint(new RobotPoint(goalX,goalY, 0, 0), 3);
                shooter.autoAim(odometer.x, odometer.y, goalX, goalY);
            }
            released3 = auto_aim;

            if(!shooter_toggle_rev && released1) {
                shooter.toggleShooter();
            }
            released1 = shooter_toggle_rev;

            if(!toggleHopper && released2) {
                shooter.toggleHopper();
            }
            released2 = toggleHopper;

            if(shoot_ring) {
                shooter.feedDisk();
                time.waitMillis(300);
            }
            if (powershot){
                powerShots();
            }
            if(increase_shooter_power) {
                shooter.incrementPower(0.005);
            }else if(decrease_shooter_power) {
                shooter.incrementPower(-0.005);
            }

            if(increase_shooter_angle) {
                shooter.incrementAngle(0.005);
            }else if(decrease_shooter_angle) {
                shooter.incrementAngle(-0.005);
            }
            shooter.update();

            if (gamepad1.back){
                odometer.startTracking(-70, 0,0);
            }
            // INTAKE
            if(!reverse_intake && released4) {
                intake_multiplier = -intake_multiplier;
            }
            released4 = reverse_intake;

            intake.setPower(intake_power*intake_multiplier);
            intake.update(shooter.hopperPrimed);

            // WOBBLE
            if(!clamp_unclamp_wobble && released5) {
                wobble.toggleClamp();
            }
            released5 = clamp_unclamp_wobble;

            if(!raise_lower_wobble && released6) {
                wobble.toggleArm();
            }
            released6 = raise_lower_wobble;
            wobble.update();

            odometer.update();

            telemetry.addData("X", odometer.x);
            telemetry.addData("Y", odometer.y);
            telemetry.addData("Distance", Math.hypot(goalX-odometer.x, goalY-odometer.y));
            telemetry.addData("Heading", odometer.heading);
            telemetry.addData("ShooterPower", shooter.getPower());
            telemetry.addData("ShooterAngle", shooter.getAngle());
            telemetry.addData("intake power", intake_power);

            telemetry.update();

        }

    }
    public void powerShots(){
        shooter.toggleShooter();
        shooter.hopperUp();
        shooter.setShooterPower(0.47);
        shooter.update();
        movement.moveToPointPD(new RobotPoint(43,140 ,-10,0),20,3);
        movement.pointInDirection(-9, 0.7);
        shooter.feedDisk();
        time.waitMillis(100);
        movement.pointInDirection(-3, 0.7);
        shooter.feedDisk();
        time.waitMillis(100);
        movement.pointInDirection(2, 0.7);
        shooter.feedDisk();
        shooter.hopperDown();


    }

}