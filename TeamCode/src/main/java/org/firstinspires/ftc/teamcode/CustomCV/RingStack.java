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
    private double size;

    @Override
    public Mat processFrame(Mat frame) {

        Mat workingMat = frame.clone();

        Imgproc.cvtColor(workingMat, workingMat, Imgproc.COLOR_RGB2HSV_FULL);
        Imgproc.GaussianBlur(workingMat, workingMat, new Size(5,5), 0);

        Scalar black = new Scalar(0, 0, 0);

        Imgproc.line(workingMat, new Point(0, 0), new Point(240, 0), black, 100);
        Imgproc.line(workingMat, new Point(0, 320), new Point(240, 320), black, 100);
        Imgproc.line(workingMat, new Point(240, 0), new Point(240, 320), black, 50);
        Imgproc.line(workingMat, new Point(0, 0), new Point(0, 320), black, 50);

        Scalar lower_yellow = new Scalar(15, 70, 60);
        Scalar upper_yellow = new Scalar(40, 250, 250);

        Mat mask = new Mat(workingMat.size(), CvType.CV_8UC3);
        mask.setTo(new Scalar(0,0,0));
        Core.inRange(workingMat, lower_yellow, upper_yellow, mask);

        ArrayList<Rect> yellow_rects = MathFunctions.getColorFilterBboxes(workingMat, black, black, 300, 6000);

        for(int b=0;b<yellow_rects.size();b++) {
            Rect yellow_rect = yellow_rects.get(b);

            size = yellow_rect.width * yellow_rect.height;

            double centerX = yellow_rect.x + yellow_rect.width/2;
            double centerY = yellow_rect.y + yellow_rect.height/2;

            Point center = new Point((int)centerX, (int)centerY);
            Imgproc.circle(frame, center, 10, lower_yellow, 5);
            Imgproc.rectangle(frame, yellow_rect, upper_yellow);

        }

        return mask;
    }

    public int getRings() {

        return rings;

    }

    public double getSize() {

        return size;

    }

}
