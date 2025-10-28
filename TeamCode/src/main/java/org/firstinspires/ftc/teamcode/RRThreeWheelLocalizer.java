package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.PI;

/**
 * Road Runner localizer for a standard 3-wheel odometry setup:
 *  - Left wheel at (0, +L/2, 0)
 *  - Right wheel at (0, -L/2, 0)
 *  - Center (lateral) wheel at (+offset, 0, 90deg)
 *
 * Coordinate frame: +x forward, +y left, heading CCW (radians).
 */
public class RRThreeWheelLocalizer extends ThreeTrackingWheelLocalizer {
    private final DcMotorEx left, right, center;

    private final double ticksPerInch;

    public RRThreeWheelLocalizer(HardwareMap hw) {
        super(Arrays.asList(
                // Wheel poses relative to robot center
                new Pose2d(0.0,  DriveConstants.LATERAL_DISTANCE_IN / 2.0, 0.0),
                new Pose2d(0.0, -DriveConstants.LATERAL_DISTANCE_IN / 2.0, 0.0),
                new Pose2d(DriveConstants.CENTER_WHEEL_OFFSET_IN, 0.0, PI / 2.0)
        ));

        left   = hw.get(DcMotorEx.class, DriveConstants.LEFT_ODOM_NAME);
        right  = hw.get(DcMotorEx.class, DriveConstants.RIGHT_ODOM_NAME);
        center = hw.get(DcMotorEx.class, DriveConstants.CENTER_ODOM_NAME);

        // Ensure encoders are in raw tick mode (no built-in control)
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        center.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Directions MUST be verified on your robot:
        // Push robot forward: left & right counts should INCREASE.
        // Strafe robot left: center should INCREASE.
        left.setDirection(DcMotor.Direction.FORWARD);
        right.setDirection(DcMotor.Direction.REVERSE);
        center.setDirection(DcMotor.Direction.FORWARD);

        ticksPerInch = DriveConstants.TICKS_PER_REV / (PI * DriveConstants.ODO_WHEEL_DIAM_IN);
    }

    private double t2i(double ticks) {
        return ticks / ticksPerInch;
    }

    @Override
    public List<Double> getWheelPositions() {
        double l = t2i(left.getCurrentPosition())   * DriveConstants.LEFT_MULT;
        double r = t2i(right.getCurrentPosition())  * DriveConstants.RIGHT_MULT;
        double c = t2i(center.getCurrentPosition()) * DriveConstants.CENTER_MULT;
        return Arrays.asList(l, r, c);
    }

    @Override
    public List<Double> getWheelVelocities() {
        // getVelocity() returns ticks/second on modern SDK
        double l = t2i(left.getVelocity())   * DriveConstants.LEFT_MULT;
        double r = t2i(right.getVelocity())  * DriveConstants.RIGHT_MULT;
        double c = t2i(center.getVelocity()) * DriveConstants.CENTER_MULT;
        return Arrays.asList(l, r, c);
    }
}
