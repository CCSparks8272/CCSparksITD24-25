package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "use this one for decode", group = "Telop")
public class HolonomicTelop extends OpMode {

    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;

    private DcMotor motorintake;
    private DcMotorEx motorLauncher;
    private DcMotor backintake;

    private Servo bootkicka;//buh nah rly
    private Servo ledLight1;
    private Servo ledLight2;

    private Odometry odo;
    private TurretVision turretVision;

    private boolean isBlue = true; // default

    // goBILDA LED Driver positions (servo signal positions)
    private static final double LED_OFF   = 0.0;
    private static final double LED_RED   = 0.28;
    private static final double LED_GREEN = 0.56;
    private static final double LED_BLUE  = 0.84;

    private static final double BOOT_UP_POS = 0.75;
    private static final double BOOT_DOWN_POS = 0.25;
    private static final double BOOT_NEUTRAL_POS = 0.50;

    private static final double LAUNCHER_TICKS_PER_REV = 28.0;
    private static final double TARGET_RPM = 2500;
    private static final double RPM_TOLERANCE = 150;
    private static final double DEADZONE = 0.05;
    private static final double SPEED_FAST = 1.00;
    private static final double SPEED_NORM = 0.80;
    private static final double SPEED_SLOW = 0.40;
    @Override
    public void init() {
        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft  = hardwareMap.dcMotor.get("FL");
        motorBackLeft   = hardwareMap.dcMotor.get("BL");
        motorBackRight  = hardwareMap.dcMotor.get("BR");
        motorintake     = hardwareMap.dcMotor.get("IM");
        motorLauncher   = (DcMotorEx)hardwareMap.dcMotor.get("LM");
        backintake      = hardwareMap.dcMotor.get("BIM");

        bootkicka = hardwareMap.servo.get("bootkicka");
        ledLight1 = hardwareMap.servo.get("led1");
        ledLight2 = hardwareMap.servo.get("led2");

        turretVision = new TurretVision(hardwareMap);
        turretVision.setAlliance(TurretVision.Alliance.BLUE);

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

        motorLauncher = (DcMotorEx) hardwareMap.dcMotor.get("LM");
        motorLauncher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        odo = new Odometry(
                hardwareMap,
                "leftOdo", "rightOdo", "centerOdo",
                2000.0,
                1.89,
                8.625,
                -3.14
        );
    }

    @Override
    public void init_loop() {
        if (gamepad1.x) isBlue = true;   // X = Blue
        if (gamepad1.b) isBlue = false;  // B = Red

        turretVision.setAlliance(isBlue ? TurretVision.Alliance.BLUE : TurretVision.Alliance.RED);

        telemetry.addData("Alliance", isBlue ? "BLUE" : "RED");
        telemetry.addData("Target Tag", turretVision.getActiveTargetId());
        telemetry.update();
    }

    @Override
    public void loop() {
        odo.update();
//button assignments

        double ly2 = gamepad2.left_stick_y;

        boolean bx = gamepad2.x;
        boolean bb = gamepad2.b;

        boolean intakeSlow = gamepad2.left_bumper;   // intake speed modifier
        boolean intakeFast = gamepad2.right_bumper;  // intake speed modifier

        boolean yy = gamepad2.y; // launcher forward
        boolean aa = gamepad2.a; // launcher reverse

        float lt2 = gamepad2.left_trigger;  // launcher speed modifier
        float rt2 = gamepad2.right_trigger; // launcher speed modifier

        boolean dpu = gamepad2.dpad_up;
        boolean dpd = gamepad2.dpad_down;

        double lt = gamepad1.left_trigger;
        double rt = gamepad1.right_trigger;
        double dz = 0.08;
        double y  = applyDeadzone(gamepad1.left_stick_y); // forward/back (invert)
        double x  = applyDeadzone( gamepad1.right_stick_x); // strafe
        double rx = applyDeadzone( gamepad1.left_stick_x); // turn

        if (Math.abs(y)  < dz) y  = 0;
        if (Math.abs(x)  < dz) x  = 0;
        if (Math.abs(rx) < dz) rx = 0;

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

        motorFrontRight.setPower(fl);
        motorFrontLeft.setPower(fr);
        motorBackLeft.setPower(-bl);
        motorBackRight.setPower(br);

//intake
        double ip = 0;//intake power

        if (bx) { // intake in
            ip = 1;

            if (intakeSlow && intakeFast) {
                ip = .75;
            } else if (intakeSlow) {
                ip = .5;
            } else if (intakeFast) {
                ip = 1;
            }

        } else if (bb) { // intake out
            ip = -1;

            if (intakeSlow && intakeFast) {
                ip = -.75;
            } else if (intakeSlow) {
                ip = -.5;
            } else if (intakeFast) {
                ip = -1;
            }
        }

//launcher
        double lp = 0;//launcher powa

        if (yy) { // launch forward
            lp =0.5;

            if (lt2 > 0.05 && rt2 > 0.05) {
                lp = 0.5;
            } else if (lt2 > 0.05) {
                lp = 0.35;
            } else if (rt2 > 0.05) {
                lp = 0.85;
            }

        } else if (aa) { // reverse
            lp = -0.5;

            if (lt2 > 0.05 && rt2 > 0.05) {
                lp = -0.5;
            } else if (lt2 > 0.05) {
                lp = -0.35;
            } else if (rt2 > 0.05) {
                lp = -0.85;
            }
        }
        // back launcher
        double blp = 0;
        if (ly2 > 0.2)
        {
            blp = -0.5;
        } else if (ly2 < -0.2) {
            blp = 0.5;
        }

//bootkicker(but not really)
        if (dpu) {
            bootkicka.setPosition(BOOT_UP_POS);
        } else if (dpd) {
            bootkicka.setPosition(BOOT_DOWN_POS);
        } else {
            bootkicka.setPosition(BOOT_NEUTRAL_POS);
        }
        // Apply intake/launcher powers
        motorLauncher.setPower(lp);
        motorintake.setPower(ip);
        backintake.setPower(blp);


        //launcher stuffs
        double ticksPerSec = motorLauncher.getVelocity();
        double launcherRpm = (ticksPerSec / LAUNCHER_TICKS_PER_REV) * 60.0;
        boolean atSpeed = Math.abs(launcherRpm - TARGET_RPM) <= RPM_TOLERANCE;
        double ledPos = atSpeed ? LED_GREEN : LED_RED;
        ledLight1.setPosition(ledPos);
        ledLight2.setPosition(ledPos);

        telemetry.addData("Launcher RPM", "%.0f", launcherRpm);
        telemetry.addData("At Speed", atSpeed);
        telemetry.addData("Target RPM", "%.0f", TARGET_RPM);




        boolean aimMode = gamepad2.right_stick_button;     // aim toggle button
        boolean readyToShoot = gamepad2.left_stick_button; // ready indicator button

        double turretPower;
        if (aimMode) {
            turretPower = turretVision.getTurretPower();
        } else {
            turretPower = Range.clip(gamepad2.left_stick_x * 0.33, -0.33, 0.33);
        }
        //turntablets.setPower(turretPower);



        telemetry.addData("FR FL BR BL", "%.2f %.2f %.2f %.2f", fr, fl, br, bl);
        telemetry.addData("Odo X (in)", "%.2f", odo.getX());
        telemetry.addData("Odo Y (in)", "%.2f", odo.getY());
        telemetry.addData("Heading (deg)", "%.2f", Math.toDegrees(odo.getHeading()));

        telemetry.addData("Aim", aimMode ? "ON" : "OFF");
        telemetry.addData("ReadyToShoot", readyToShoot ? "YES" : "NO");
        telemetry.addData("TagFound", turretVision.isTagFound());
        telemetry.addData("Bearing", "%.2f", turretVision.getLastBearing());
        telemetry.addData("TurretPower", "%.2f", turretPower);
        telemetry.addData("Detections", turretVision.getDetectionCount());
        telemetry.addData("IDs", turretVision.getDetectedIdsString());

        telemetry.update();
    }

    private static double applyDeadzone(double v) {
        return Math.abs(v) < DEADZONE ? 0.0 : v;
    }


    @Override
    public void stop() {
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        //turntablets.setPower(0);
        motorLauncher.setPower(0);
        motorintake.setPower(0);

        ledLight1.setPosition(LED_OFF);
        ledLight2.setPosition(LED_OFF);

        turretVision.close();
    }

    static class Odometry {

        private final DioQuadEncoder leftEnc, rightEnc, centerEnc;

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
                HardwareMap hw,
                String leftBase, String rightBase, String centerBase,
                double ticksPerRev, double wheelDiameterIn,
                double lateralDistanceIn, double centerWheelOffsetIn
        ) {
            // expects config names like leftOdoA/leftOdoB
            leftEnc   = new DioQuadEncoder(hw, leftBase + "A", leftBase + "B");
            rightEnc  = new DioQuadEncoder(hw, rightBase + "A", rightBase + "B");
            centerEnc = new DioQuadEncoder(hw, centerBase + "A", centerBase + "B");

            leftEnc.reset();
            rightEnc.reset();
            centerEnc.reset();

            this.LATERAL_DISTANCE_IN = lateralDistanceIn;
            this.CENTER_WHEEL_OFFSET_IN = centerWheelOffsetIn;
            this.TICKS_PER_INCH = ticksPerRev / (Math.PI * wheelDiameterIn);
        }

        public void update() {
            leftEnc.update();
            rightEnc.update();
            centerEnc.update();

            final int l = leftEnc.getPosition();
            final int r = rightEnc.getPosition();
            final int c = centerEnc.getPosition();

            if (first) {
                lastL = l; lastR = r; lastC = c;
                first = false;
                return;
            }

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


    static class DioQuadEncoder {
        private final DigitalChannel a;
        private final DigitalChannel b;

        private boolean lastA, lastB;
        private int position = 0;

        public DioQuadEncoder(HardwareMap hw, String aName, String bName) {
            a = hw.get(DigitalChannel.class, aName);
            b = hw.get(DigitalChannel.class, bName);

            a.setMode(DigitalChannel.Mode.INPUT);
            b.setMode(DigitalChannel.Mode.INPUT);

            lastA = a.getState();
            lastB = b.getState();
        }

        public void reset() {
            position = 0;
            lastA = a.getState();

            lastB = b.getState();
        }
        // Call every loop
        public void update() {
            boolean curA = a.getState();
            boolean curB = b.getState();

            if (curA == lastA && curB == lastB) return;

            // 1x decode (good enough for FTC loop rates)
            if (curA != lastA) {
                position += (curA == curB) ? -1 : 1;
            } else {
                position += (curA == curB) ? 1 : -1;
            }

            lastA = curA;
            lastB = curB;
        }

        public int getPosition() {
            return position;
        }
    }
}
