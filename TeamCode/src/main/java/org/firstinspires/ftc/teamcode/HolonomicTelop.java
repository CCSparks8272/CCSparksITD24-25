package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
@TeleOp(name = "Holonomic Telop - Motors Only", group = "Telop")
public class HolonomicTelop extends OpMode {
    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;

    private Odometry odo;

    @Override
    public void init() {
        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft  = hardwareMap.dcMotor.get("FL");
        motorBackLeft   = hardwareMap.dcMotor.get("BL");
        motorBackRight  = hardwareMap.dcMotor.get("BR");

        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);

        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        odo = new Odometry(
                hardwareMap,
                "leftOdo", "rightOdo", "centerOdo",
                8192.0,
                2.0,
                14.5,
                -6.5
        );
    }

    @Override
    public void loop() {
        odo.update();

        double ly = gamepad1.left_stick_y;
        double lx = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        if (ly <= 0.5 && ly >= -0.5) ly = 0.0;
        if (ly > 0.9) ly = 1.0;
        if (ly < -0.9) ly = -1.0;
        if (lx <= 0.5 && lx >= -0.5) lx = 0.0;
        if (rx <= 0.5 && rx >= -0.5) rx = 0.0;

        double fl =  ly - lx - rx;
        double fr = -ly - lx - rx;
        double br = -ly + lx - rx;
        double bl =  ly + lx - rx;

        double lt = gamepad1.left_trigger;
        double rt = gamepad1.right_trigger;

        if (lt > 0.5 && rt <= 0.5) {
            fl = Range.clip(fl, -1, 1);
            fr = Range.clip(fr, -1, 1);
            bl = Range.clip(bl, -1, 1);
            br = Range.clip(br, -1, 1);
        } else if (rt > 0.5 && lt <= 0.5) {
            fl = Range.clip(fl, -1, 1) * 0.4;
            fr = Range.clip(fr, -1, 1) * 0.4;
            bl = Range.clip(bl, -1, 1) * 0.4;
            br = Range.clip(br, -1, 1) * 0.4;
        } else {
            fl = Range.clip(fl, -1, 1) * 0.8;
            fr = Range.clip(fr, -1, 1) * 0.8;
            bl = Range.clip(bl, -1, 1) * 0.8;
            br = Range.clip(br, -1, 1) * 0.8;
        }

        motorFrontRight.setPower(-fr);
        motorFrontLeft.setPower(-fl);
        motorBackLeft.setPower(-bl);
        motorBackRight.setPower(-br);

        telemetry.addData("FR FL BR BL", "%.2f %.2f %.2f %.2f", fr, fl, br, bl);
        telemetry.addData("Odo X (in)", "%.2f", odo.getX());
        telemetry.addData("Odo Y (in)", "%.2f", odo.getY());
        telemetry.addData("Heading (deg)", "%.2f", Math.toDegrees(odo.getHeading()));
        telemetry.update();
    }

    @Override
    public void stop() {
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }

    static class Odometry {
        private final DcMotor leftEnc, rightEnc, centerEnc;
        private final double LATERAL_DISTANCE_IN;
        private final double CENTER_WHEEL_OFFSET_IN;
        private final double TICKS_PER_INCH;

        private double x = 0, y = 0, heading = 0;
        private int lastL = 0, lastR = 0, lastC = 0;
        private boolean first = true;

        private static final double LEFT_MULT = 1.0;
        private static final double RIGHT_MULT = 1.0;
        private static final double CENTER_MULT = 1.0;

        public Odometry(
                com.qualcomm.robotcore.hardware.HardwareMap hw,
                String leftName, String rightName, String centerName,
                double ticksPerRev, double wheelDiameterIn,
                double lateralDistanceIn, double centerWheelOffsetIn
        ) {
            leftEnc   = hw.dcMotor.get(leftName);
            rightEnc  = hw.dcMotor.get(rightName);
            centerEnc = hw.dcMotor.get(centerName);

            leftEnc.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightEnc.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            centerEnc.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftEnc.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightEnc.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            centerEnc.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            leftEnc.setDirection(DcMotorSimple.Direction.FORWARD);
            rightEnc.setDirection(DcMotorSimple.Direction.REVERSE);
            centerEnc.setDirection(DcMotorSimple.Direction.FORWARD);

            this.LATERAL_DISTANCE_IN = lateralDistanceIn;
            this.CENTER_WHEEL_OFFSET_IN = centerWheelOffsetIn;
            this.TICKS_PER_INCH = ticksPerRev / (Math.PI * wheelDiameterIn);
        }

        public void update() {
            final int l = leftEnc.getCurrentPosition();
            final int r = rightEnc.getCurrentPosition();
            final int c = centerEnc.getCurrentPosition();

            if (first) { lastL = l; lastR = r; lastC = c; first = false; return; }

            final int dL = l - lastL;
            final int dR = r - lastR;
            final int dC = c - lastC;

            lastL = l; lastR = r; lastC = c;

            final double dl = (dL / TICKS_PER_INCH) * LEFT_MULT;
            final double dr = (dR / TICKS_PER_INCH) * RIGHT_MULT;
            final double dc = (dC / TICKS_PER_INCH) * CENTER_MULT;

            final double dTheta = (dr - dl) / LATERAL_DISTANCE_IN;
            final double dForward = (dl + dr) / 2.0;
            final double dStrafe  = dc - dTheta * CENTER_WHEEL_OFFSET_IN;

            final double thetaMid = heading + dTheta / 2.0;
            final double cosT = Math.cos(thetaMid);
            final double sinT = Math.sin(thetaMid);

            x += dForward * cosT - dStrafe * sinT;
            y += dForward * sinT + dStrafe * cosT;
            heading += dTheta;
        }

        public double getX() { return x; }
        public double getY() { return y; }
        public double getHeading() { return heading; }
    }
}
