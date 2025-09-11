package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

class Odometry {
    private DcMotorEx leftEncoder, rightEncoder, centerEncoder;

    private double TICKS_PER_REV;
    private double WHEEL_DIAM_IN;
    private double TICKS_PER_INCH;
    private double TRACK_WIDTH;
    private double CENTER_WHEEL_OFFSET;

    private int prevLeft, prevRight, prevCenter;
    private double x, y, heading;

    public Odometry(HardwareMap hw, String leftName, String rightName, String centerName,
                    double ticksPerRev, double wheelDiamIn, double trackWidth, double centerOffset) {
        leftEncoder = hw.get(DcMotorEx.class, leftName);
        rightEncoder = hw.get(DcMotorEx.class, rightName);
        centerEncoder = hw.get(DcMotorEx.class, centerName);

        leftEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        centerEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftEncoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightEncoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        centerEncoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        TICKS_PER_REV = ticksPerRev;
        WHEEL_DIAM_IN = wheelDiamIn;
        TICKS_PER_INCH = TICKS_PER_REV / (Math.PI * WHEEL_DIAM_IN);

        TRACK_WIDTH = trackWidth;
        CENTER_WHEEL_OFFSET = centerOffset;

        prevLeft = prevRight = prevCenter = 0;
        x = y = heading = 0.0;
    }

    private double ticksToInches(int ticks) {
        return ticks / TICKS_PER_INCH;
    }

    public void update(){
        int leftTicks = leftEncoder.getCurrentPosition();
        int rightTicks = rightEncoder.getCurrentPosition();
        int centerTicks = centerEncoder.getCurrentPosition();

        int dL = leftTicks - prevLeft;
        int dR = rightTicks - prevRight;
        int dC = centerTicks - prevCenter;

        prevLeft = leftTicks;
        prevRight = rightTicks;
        prevCenter = centerTicks;

        double dL_in = ticksToInches(dL);
        double dR_in = ticksToInches(dR);
        double dC_in = ticksToInches(dC);

        double dTheta = (dR_in - dL_in) / TRACK_WIDTH;
        double dx, dy;

        if (Math.abs(dTheta) < 1e-6) {
            dx = dC_in;
            dy = (dL_in + dR_in) / 2.0;
        } else {
            double r = (dL_in + dR_in) / (2.0 * dTheta);
            dx = dC_in + CENTER_WHEEL_OFFSET * dTheta;
            dy = r * 2.0 * Math.sin(dTheta / 2.0);
        }

        heading += dTheta;
        double cos = Math.cos(heading);
        double sin = Math.sin(heading);
        x += dx * cos - dy * sin;
        y += dx * sin + dy * cos;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getHeading() { return heading; }
}
