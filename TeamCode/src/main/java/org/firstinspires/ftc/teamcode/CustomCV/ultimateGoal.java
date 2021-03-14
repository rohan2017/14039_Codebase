package org.firstinspires.ftc.teamcode.CustomCV;

import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class ultimateGoal extends OpenCvPipeline {

    private static final Point corner = new Point(100, 100);
    private static final int width = 75, height = 75;
    private static final int rings4 = 20, rings1 = 10;
    private int avg, scenario;
    private Point pointA;
    private Point pointB;
    private Mat YCrCb;
    private Mat Cb;
    private Mat roi;
    public ringNum location = ringNum.ZERO;

    public Mat processFrame(Mat input) {
        init();
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCrCb, Cb,1);
        avg = (int) Core.mean(roi).val[0];
        Imgproc.rectangle(input, pointA, pointB, new Scalar(0, 0, 255), 2);
        getRings();
        return input;
    }

    public void init()
    {
        pointA = new Point(corner.x, corner.y);
        pointB = new Point(corner.x + width, corner.y + height);
        YCrCb = new Mat();
        Cb = new Mat();
        roi = Cb.submat(new Rect(pointA, pointB));
    }



    public void getRings() {
        if (avg >= rings4) {
            location = ringNum.FOUR;
            scenario = 4;
        } else if (avg >= rings1) {
            location = ringNum.ONE;
            scenario = 1;
        } else {
            location = ringNum.ZERO;
            scenario = 0;
        }
    }

    public int getScenario()
    {
        return scenario;
    }

    public int getAvg(){
        return avg;
    }


}
