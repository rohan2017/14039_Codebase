package org.firstinspires.ftc.teamcode.Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.CustomCV.BluePipeline;
import org.firstinspires.ftc.teamcode.CustomCV.EasyOpenCVExample;
import org.firstinspires.ftc.teamcode.CustomCV.RingStack;
import org.firstinspires.ftc.teamcode.HardwareSystems.ActionHandler;
import org.firstinspires.ftc.teamcode.HardwareSystems.Intake;
import org.firstinspires.ftc.teamcode.HardwareSystems.Shooter;
import org.firstinspires.ftc.teamcode.HardwareSystems.Wobble;
import org.firstinspires.ftc.teamcode.Movement.Localization.OdometerIMU2W;
import org.firstinspires.ftc.teamcode.Movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.Movement.MotionPlanning.RobotPoint;
import org.firstinspires.ftc.teamcode.Movement.Movement;
import org.firstinspires.ftc.teamcode.Utility.RobotHardware;
import org.firstinspires.ftc.teamcode.Utility.Timer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

@Autonomous(name="Blue Auto", group="Auto")
public class blueAuto extends LinearOpMode {

    // Declare OpMode Members
    private RobotHardware hardware = new RobotHardware();
    private Timer timer;
    private OdometerIMU2W odometer;
    private MecanumDrive drivetrain;
    private Movement movement;
    private ActionHandler handler;
    private Shooter shooter;
    private Intake intake;
    private Wobble wobble;
    // Vision
    private SkystoneDeterminationPipeline pipeline;
    private OpenCvCamera phoneCam;
    private int pos;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        timer.start();

        odometer.startTracking(0, 0, 0);
        telemetry.addData("status", "running");
        telemetry.update();
        int rings = pipeline.position;
        //hopper in position and reving hopper

        //shooter.hopperUp();
        //shooter.setShooterAngle(0.99);

        shooter.toggleShooter();
        shooter.setShooterPower(0.52);
        shooter.update();

        timer.waitMillis(1000);

        //move to first point and shoot powershot

        movement.moveToPointPD(new RobotPoint(43,140 ,-10,0),20,3);
        timer.waitMillis(1000);
        shooter.feedDisk();
        timer.waitMillis(500);

        //shoot first disk into goal
        //movement.moveToPointPD(new RobotPoint(40,144 ,-7,0),20,10);
        movement.pointInDirection(-5, 0.3);
        shooter.feedDisk();
        timer.waitMillis(500);

        //shoot seccond disk into goal
        //movement.moveToPointPD(new RobotPoint(40,144 ,0,0),20,10);
        movement.pointInDirection(0, 0.3);
        shooter.feedDisk();
        timer.waitMillis(500);

        //derev shooter

        shooter.setShooterPower(0);
        shooter.update();

        if (pos ==0){
            movement.moveToPointPD(new RobotPoint(-25,203 ,0,0),20,3);
            wobble.lowerArm();
            wobble.update();
            timer.waitMillis(1000);
        } else if(pos == 1){
            movement.moveToPointPD(new RobotPoint(29,260 ,0,0),20,3);
            wobble.lowerArm();
            wobble.update();
            timer.waitMillis(1000);
        } else {
            movement.moveToPointPD(new RobotPoint(-30,305 ,-5,0),20,3);
            wobble.lowerArm();
            wobble.update();
            timer.waitMillis(1000);
        }

        movement.pointInDirection(-180,1);

    }

    private void initialize(){

        hardware.hardwareMap(hardwareMap);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();

        pipeline = new SkystoneDeterminationPipeline();
        phoneCam.setPipeline(pipeline);
        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        drivetrain = new MecanumDrive(this, hardware);
        odometer = new OdometerIMU2W(this, hardware);
        timer = new Timer(this, odometer);
        movement = new Movement(this, drivetrain, odometer, timer);
        handler = new ActionHandler();
        movement.setActionHandler(handler);
        movement.useActionHandler = true;
        shooter = new Shooter(this, hardware, timer);
        intake = new Intake(this, hardware);
        wobble = new Wobble(this, hardware);

        drivetrain.initialize();
        odometer.initialize();
        odometer.initialize();
        shooter.initialize();
        intake.initialize();
        wobble.initialize();

        shooter.hopperUp();
        shooter.setShooterAngle(0.99);

        wobble.raiseArm();
        wobble.update();

        shooter.update();

        telemetry.addData("status","initialized");
        telemetry.addData("avg",pipeline.position);
        telemetry.addData("block",pipeline.getAnalysis());
        telemetry.update();

    }

    public static class SkystoneDeterminationPipeline extends OpenCvPipeline {

        /*
         * An enum to define the skystone position
         */
        public enum RingPosition {
            FOUR,
            ONE,
            NONE
        }

        /*
         * Some color constants
         */
        static final Scalar BLUE = new Scalar(0, 0, 255);
        static final Scalar GREEN = new Scalar(0, 255, 0);

        /*
         * The core values which define the location and size of the sample regions
         */
        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(130, 102);

        static final int REGION_WIDTH = 40;
        static final int REGION_HEIGHT = 45;

        final int FOUR_RING_THRESHOLD = 140;
        final int ONE_RING_THRESHOLD = 129;

        Point region1_pointA = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x,
                REGION1_TOPLEFT_ANCHOR_POINT.y);
        Point region1_pointB = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        /*
         * Working variables
         */
        Mat region1_Cb;
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();
        int avg1;

        // Volatile since accessed by OpMode thread w/o synchronization
        private volatile EasyOpenCVExample.SkystoneDeterminationPipeline.RingPosition position = EasyOpenCVExample.SkystoneDeterminationPipeline.RingPosition.FOUR;

        /*
         * This function takes the RGB frame, converts to YCrCb,
         * and extracts the Cb channel to the 'Cb' variable
         */
        void inputToCb (Mat input)
        {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 1);
        }

        @Override
        public void init (Mat firstFrame)
        {
            inputToCb(firstFrame);

            region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
        }

        @Override
        public Mat processFrame (Mat input)
        {
            inputToCb(input);

            avg1 = (int) Core.mean(region1_Cb).val[0];

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            position = EasyOpenCVExample.SkystoneDeterminationPipeline.RingPosition.FOUR; // Record our analysis
            if (avg1 > FOUR_RING_THRESHOLD) {
                position = EasyOpenCVExample.SkystoneDeterminationPipeline.RingPosition.FOUR;
            } else if (avg1 > ONE_RING_THRESHOLD) {
                position = EasyOpenCVExample.SkystoneDeterminationPipeline.RingPosition.ONE;
            } else {
                position = EasyOpenCVExample.SkystoneDeterminationPipeline.RingPosition.NONE;
            }

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill

            return input;
        }

        public int getAnalysis ()
        {
            return avg1;
        }

    }

}
