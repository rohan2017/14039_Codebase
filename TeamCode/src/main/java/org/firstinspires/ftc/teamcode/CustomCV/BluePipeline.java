package org.firstinspires.ftc.teamcode.CustomCV;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class BluePipeline extends OpenCvPipeline {

    @Override
    public Mat processFrame(Mat frame) {

        return frame;

    }

    public int getTargetX() {

        return 0;

    }

    public int getTargetDist() {

        return 0;

    }

}
