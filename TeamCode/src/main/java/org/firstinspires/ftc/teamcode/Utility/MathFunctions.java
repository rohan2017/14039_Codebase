package org.firstinspires.ftc.teamcode.Utility;

import org.firstinspires.ftc.teamcode.Movement.MotionPlanning.RobotPoint;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import static java.lang.Math.*;

public class MathFunctions {

    // All credit for this function goes to team 11115 and their coder, Peter. Much appreciated!
    public static ArrayList<RobotPoint> lineCircleIntersection(double circleX, double circleY, double r,
                                                               double lineX1, double lineY1,
                                                               double lineX2, double lineY2){
        // Make sure the points don't exactly line up so the slopes work
        if(Math.abs(lineY1- lineY2) < 0.003){

            lineY1 = lineY2 + 0.003;

        }
        if(Math.abs(lineX1- lineX2) < 0.003){

            lineX1 = lineX2 + 0.003;

        }

        // Calculate the slope of the line
        double m1 = (lineY2 - lineY1)/(lineX2-lineX1);

        // The first coefficient in the quadratic
        double quadraticA = 1.0 + pow(m1,2);

        // Shift one of the line's points so it is relative to the circle
        double x1 = lineX1-circleX;
        double y1 = lineY1-circleY;


        // The second coefficient in the quadratic
        double quadraticB = (2.0 * m1 * y1) - (2.0 * pow(m1,2) * x1);

        // The third coefficient in the quadratic
        double quadraticC = ((pow(m1,2)*pow(x1,2)) - (2.0*y1*m1*x1) + pow(y1,2)-pow(r,2));


        ArrayList<RobotPoint> allPoints = new ArrayList<>();

        // This may give an error so we use a try catch
        try{
            // Now solve the quadratic equation given the coefficients
            double discriminant = sqrt(pow(quadraticB,2) - (4.0 * quadraticA * quadraticC));
            double xRoot1 = (-quadraticB + discriminant)/(2.0*quadraticA);

            // We know the line equation so plug into that to get root
            double yRoot1 = m1 * (xRoot1 - x1) + y1;

            // Add back in translations
            xRoot1 += circleX;
            yRoot1 += circleY;

            // Make sure it was within range of the segment
            double minX = lineX1 < lineX2 ? lineX1 : lineX2;
            double maxX = lineX1 > lineX2 ? lineX1 : lineX2;

            if(xRoot1 > minX && xRoot1 < maxX){

                allPoints.add(new RobotPoint(xRoot1, yRoot1, 0, 0));

            }

            // Do the same for the other root
            double xRoot2 = (-quadraticB - discriminant)/(2.0*quadraticA);

            double yRoot2 = m1 * (xRoot2 - x1) + y1;
            // Now we can add back in translations
            xRoot2 += circleX;
            yRoot2 += circleY;

            // Make sure it was within range of the segment
            if(xRoot2 > minX && xRoot2 < maxX){

                allPoints.add(new RobotPoint(xRoot2, yRoot2, 0, 0));

            }

        }catch(Exception e){
            // There are no real roots, and therefore no intersections
        }
        return allPoints;

    }

    public static double sine(double theta){
        return sin(toRadians(theta));
    }

    public static double cosine(double theta){
        return cos(toRadians(theta));
    }

    public static double calculateLaunchAngle(double distance) {

        return 0; // I made some calculations but then decided a lookup table would be better

    }

    public static ArrayList<Rect> getColorFilterBboxes(Mat frame_hsv, Scalar lower, Scalar upper, double min_area, double max_area) {

        Mat mask = new Mat(frame_hsv.size(), CvType.CV_8UC3);
        mask.setTo(new Scalar(0,0,0));
        Core.inRange(frame_hsv, lower, upper, mask);
        //Core.bitwise_and(lower_res, lower_res, mask);
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f[] contoursPoly  = new MatOfPoint2f[contours.size()];
        ArrayList<Rect> boundingRects = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++) {

            double cont_area = Imgproc.contourArea(contours.get(i));
            if(cont_area < max_area && cont_area > min_area) {

                contoursPoly[i] = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 2, true);
                boundingRects.add(Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray())));

            }

        }

        return boundingRects;

    }

}
