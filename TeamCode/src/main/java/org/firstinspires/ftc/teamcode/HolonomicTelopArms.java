package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Holonomic TeleOp - Lifts (Clean)", group = "TeleOp")
public class HolonomicTelopArms extends OpMode {
    private static final double DEADZONE = 0.05;
    private static final double SPEED_FAST = 1.00;
    private static final double SPEED_NORM = 0.80;
    private static final double SPEED_SLOW = 0.40;
    private static final int HORZ_MIN = -1300;
    private static final int HORZ_MAX = -100;
    private static final double HORZ_SCALE = 0.5;
    private static final double VERT_KG = 0.08;
    private static final double VERT_SCALE = 0.8;

    private DcMotor motorFR, motorFL, motorBR, motorBL;
    private DcMotor horzLift, vertLift;

    private Odo odo;

    @Override
    public void init() {
        motorFR = hardwareMap.dcMotor.get("FR");
        motorFL = hardwareMap.dcMotor.get("FL");
        motorBL = hardwareMap.dcMotor.get("BL");
        motorBR = hardwareMap.dcMotor.get("BR");
        vertLift = hardwareMap.dcMotor.get("VERT_LIFT");
        horzLift = hardwareMap.dcMotor.get("HORZ_LIFT");

        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFR.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBR.setDirection(DcMotorSimple.Direction.FORWARD);

        motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horzLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        vertLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorFL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        horzLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        vertLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        odo = new Odo(hardwareMap, "leftOdo", "rightOdo", "centerOdo", 8192.0, 2.0, 14.5, -6.5);
    }

    @Override
    public void loop() {
        odo.update();

        double y  = -applyDeadzone(gamepad1.left_stick_y);
        double x  =  applyDeadzone(gamepad1.left_stick_x);
        double rx =  applyDeadzone(gamepad1.right_stick_x);

        double speed = SPEED_NORM;
        if (gamepad1.left_trigger > 0.5 && gamepad1.right_trigger <= 0.5) speed = SPEED_FAST;
        else if (gamepad1.right_trigger > 0.5 && gamepad1.left_trigger <= 0.5) speed = SPEED_SLOW;

        double fl = y + x + rx;
        double fr = y - x - rx;
        double bl = y - x + rx;
        double br = y + x - rx;

        double max = Math.max(1.0, Math.max(Math.abs(fl),
                Math.max(Math.abs(fr), Math.max(Math.abs(bl), Math.abs(br)))));

        fl = (fl / max) * speed;
        fr = (fr / max) * speed;
        bl = (bl / max) * speed;
        br = (br / max) * speed;

        motorFL.setPower(fl);
        motorFR.setPower(fr);
        motorBL.setPower(bl);
        motorBR.setPower(br);

        double horzCmd = -applyDeadzone(gamepad2.left_stick_y) * HORZ_SCALE;
        int hPos = horzLift.getCurrentPosition();
        if ((hPos <= HORZ_MIN && horzCmd < 0) || (hPos >= HORZ_MAX && horzCmd > 0)) horzCmd = 0;
        horzLift.setPower(horzCmd);

        double vertStick = -applyDeadzone(gamepad2.right_stick_y);
        double vertCmd = (Math.abs(vertStick) > 0) ? vertStick * VERT_SCALE : VERT_KG;
        vertLift.setPower(Range.clip(vertCmd, -1, 1));

        telemetry.addData("Odo X (in)", "%.2f", odo.getX());
        telemetry.addData("Odo Y (in)", "%.2f", odo.getY());
        telemetry.addData("Heading (deg)", "%.2f", Math.toDegrees(odo.getHeading()));
        telemetry.update();
    }

    private static double applyDeadzone(double v) {
        return Math.abs(v) < DEADZONE ? 0.0 : v;
    }

    static class Odo {
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

        public Odo(com.qualcomm.robotcore.hardware.HardwareMap hw,
                   String leftName, String rightName, String centerName,
                   double ticksPerRev, double wheelDiameterIn,
                   double lateralDistanceIn, double centerWheelOffsetIn) {
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

            final int dL = l - lastL, dR = r - lastR, dC = c - lastC;
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
