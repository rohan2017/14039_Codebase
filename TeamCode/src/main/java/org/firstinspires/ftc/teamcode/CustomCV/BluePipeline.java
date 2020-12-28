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

public class BluePipeline extends OpenCvPipeline {

    private double targetX;
    private double targetY;
    private boolean targetFound = false;

    @Override
    public Mat processFrame(Mat frame) {
        //Mat lower_res = new Mat();
        //Imgproc.resize(frame, lower_res, new Size(500,200));

        Mat workingMat = frame.clone();

        Imgproc.cvtColor(workingMat, workingMat, Imgproc.COLOR_RGB2HSV_FULL);
        Imgproc.GaussianBlur(workingMat, workingMat, new Size(5,5), 0);

        Scalar lower_blue = new Scalar(51, 130, 130);
        Scalar upper_blue = new Scalar(171, 255, 250);

        ArrayList<Rect> blue_rects = MathFunctions.getColorFilterBboxes(workingMat, lower_blue, upper_blue, 450, 6000);

        Scalar lower_white = new Scalar(40, 0, 160);
        Scalar upper_white = new Scalar(200, 50, 255);

        ArrayList<Rect> white_rects = MathFunctions.getColorFilterBboxes(workingMat, lower_white, upper_white, 130, 3000);

        ArrayList<Rect> blue_rects_final = new ArrayList<>();
        ArrayList<Rect> white_rects_final = new ArrayList<>();

        if(blue_rects.size() > 0 && white_rects.size() > 0) {

            for(int i=0;i<blue_rects.size();i++) {

                Rect rect = blue_rects.get(i);
                if(rect.width < 4*rect.height) {
                    blue_rects_final.add(rect);
                }

            }

            for(int i=0;i<white_rects.size();i++) {

                Rect rect = white_rects.get(i);
                if(rect.width < 4*rect.height) {
                    white_rects_final.add(rect);
                }

            }

            if(blue_rects_final.size() > 0 && white_rects_final.size() > 0) {
                for(int b=0;b<blue_rects_final.size();b++) {
                    Rect blue_rect = blue_rects_final.get(b);
                    double blue_centerX = blue_rect.x + blue_rect.width/2;
                    double blue_centerY = blue_rect.y + blue_rect.height/2;
                    for(int w=0;w<white_rects_final.size();w++) {
                        Rect white_rect = white_rects_final.get(w);
                        double white_centerX = white_rect.x + white_rect.width/2;
                        double white_centerY = white_rect.y + white_rect.height/2;

                        if(Math.abs(blue_centerX-white_centerX) + Math.abs(blue_centerY-white_centerY) < 80) {

                            targetFound = true;
                            targetX = 0.1*blue_centerX + 0.9*white_centerX;
                            targetY = 0.1*blue_centerY + 0.9*white_centerY;
                            Point center = new Point((int)targetX, (int)targetY);
                            Scalar color = new Scalar(255, 255, 255);
                            Imgproc.circle(frame, center, 10, color, 5);

                        }

                    }

                }
            }

        }else{
            targetFound = false;
        }


        return frame;

    }

    public int getTargetX() {

        return 0;

    }

    public int getTargetDist() {

        return 0;

    }

}
