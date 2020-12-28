package org.firstinspires.ftc.teamcode.Opmodes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.CustomCV.BluePipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="Vision Test", group="Testing")

public class visionTest extends LinearOpMode {

    private OpenCvCamera phoneCam;
    private BluePipeline pipeline;

    private void initialize(){

        telemetry.addData("Status: ", "Initializing");
        telemetry.update();

        // Initialize all objects declared above

        // Vision ==================================================================================
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();

        pipeline = new BluePipeline();

        phoneCam.setPipeline(pipeline);

        telemetry.addData("Status: ", "Initialized");
        telemetry.update();

    }

    @Override
    public void runOpMode() {

        initialize();
        waitForStart();
        telemetry.addData("Status: ", "Running");
        telemetry.update();

        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        while (opModeIsActive()) {

            telemetry.addData("FPS", String.format("%.2f", phoneCam.getFps()));
            telemetry.update();

        }

    }

}