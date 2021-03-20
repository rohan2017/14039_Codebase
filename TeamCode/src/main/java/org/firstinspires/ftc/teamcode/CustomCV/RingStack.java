package org.firstinspires.ftc.teamcode.CustomCV;

import org.firstinspires.ftc.teamcode.Utility.MathFunctions;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class RingStack extends OpenCvPipeline {

    private int rings;
    private int average;

    @Override
    public Mat processFrame(Mat frame) {

        Mat workingMat = frame.clone();

        Imgproc.cvtColor(workingMat, workingMat, Imgproc.COLOR_RGB2HSV_FULL);
        Imgproc.GaussianBlur(workingMat, workingMat, new Size(15,15), 0);

        final Scalar black = new Scalar(0, 0, 0);

        Imgproc.line(workingMat, new Point(0, 0), new Point(240, 0), black, 180);
        Imgproc.line(workingMat, new Point(0, 320), new Point(240, 320), black, 320);
        Imgproc.line(workingMat, new Point(240, 0), new Point(240, 320), black, 170);
        Imgproc.line(workingMat, new Point(0, 0), new Point(0, 320), black, 230);

        final Scalar lower_yellow = new Scalar(0, 0, 10);
        final Scalar upper_yellow = new Scalar(35, 255, 255);

        Mat mask = new Mat(workingMat.size(), CvType.CV_8UC3);
        mask.setTo(new Scalar(0,0,0));
        Core.inRange(workingMat, lower_yellow, upper_yellow, mask);

        average = (int)Core.mean(mask).val[0];

        return frame;
    }

    public int getRings() {

        return rings;

    }

    public int getAverage() {

        return average;

    }

}
