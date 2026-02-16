package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name="Blue", group="Auto")
public class DecodeAuto extends LinearOpMode {

    private DcMotor motorFrontRight, motorFrontLeft, motorBackRight, motorBackLeft;
    private DcMotor motorintake, backintake;
    private DcMotorEx motorLauncher;


    // From your TeleOp
    private static final double LAUNCHER_TICKS_PER_REV = 28.0;
    private static final double TARGET_RPM = 4500.0;
    private static final double RPM_TOLERANCE = 150.0;

    // ---- Tune these on the field ----
    // Turn: start with these; adjust TURN_TIME_MS until it’s ~15°
    private static final double TURN_POWER = 0.35;
    private static final long   TURN_TIME_MS = 550; // <-- tweak (likely 150–300ms)

    // Launcher power (your teleop “fast” was ~0.85)
    private static final double LAUNCH_POWER = 1.5;

    // Feeding
    private static final double INTAKE_POWER = 1.0;
    private static final double BACK_INTAKE_POWER = 0.75;
    private static final long FEED_TIME_MS = 10000;

    // Safety timeouts
    private static final long SPINUP_TIMEOUT_MS = 10000;

    @Override
    public void runOpMode() {

        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft  = hardwareMap.dcMotor.get("FL");
        motorBackLeft   = hardwareMap.dcMotor.get("BL");
        motorBackRight  = hardwareMap.dcMotor.get("BR");

        motorintake = hardwareMap.dcMotor.get("IM");
        backintake  = hardwareMap.dcMotor.get("BIM");
        motorLauncher = (DcMotorEx) hardwareMap.dcMotor.get("LM");

        // Match your TeleOp motor directions
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);

        motorLauncher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("Ready: Turn ~15deg then shoot");
        telemetry.addLine("Tune TURN_TIME_MS if angle is off");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // 1) Turn ~15 degrees (in place)
        turnInPlaceTime(TURN_POWER, TURN_TIME_MS);
        // 2) Spin up launcher until at speed (or timeout)
        spinUpToSpeed(LAUNCH_POWER, SPINUP_TIMEOUT_MS);

        // 3) Feed shot
        motorintake.setPower(INTAKE_POWER);
        backintake.setPower(BACK_INTAKE_POWER);
        sleep(FEED_TIME_MS);

        // 4) Go forward a bit
        sleep(200);
        goForward(60, 200);
        // 5) Stop
        stopAll();
    }

    private void turnInPlaceTime(double power, long ms) {
        // Positive power = turn one direction. If it turns the wrong way, flip the sign.
        motorFrontLeft.setPower(+power);
        motorBackLeft.setPower(+power);
        motorFrontRight.setPower(-power);
        motorBackRight.setPower(-power);

        sleep(ms);

        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        sleep(100);
    }

    private void spinUpToSpeed(double power, long timeoutMs) {
        motorLauncher.setPower(power);

        long start = System.currentTimeMillis();
        while (opModeIsActive() && (System.currentTimeMillis() - start) < timeoutMs) {
            double ticksPerSec = motorLauncher.getVelocity();
            double rpm = (ticksPerSec / LAUNCHER_TICKS_PER_REV) * 60.0;
            boolean atSpeed = Math.abs(rpm - TARGET_RPM) <= RPM_TOLERANCE;

            telemetry.addData("Launcher RPM", "%.0f", rpm);
            telemetry.addData("At Speed", atSpeed);
            telemetry.update();

            if (atSpeed) break;
        }
    }

    private void stopAll() {
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);

        motorintake.setPower(0);
        backintake.setPower(0);
        motorLauncher.setPower(0);
    }

    private void goForward(double power, long timeMs) {

        motorFrontRight.setPower(-power);
        motorFrontLeft.setPower(-power);
        motorBackLeft.setPower(+power);
        motorBackRight.setPower(-power);

        sleep(timeMs);

        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);

        sleep(75);
    }


}
