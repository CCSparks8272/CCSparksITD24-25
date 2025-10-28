package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RRThreeWheelLocalizer;
import org.firstinspires.ftc.teamcode.SampleMecanumDrive;

/**
 * Minimal TeleOp to verify Road Runner pose from 3-wheel odometry.
 * - Injects RRThreeWheelLocalizer into your SampleMecanumDrive.
 * - Prints x/y/heading; you can also drive around if you wire gamepad controls in your drive class.
 */
@TeleOp(name = "Odometry Test", group = "Test")
public class OdometryTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        // Use your existing Quickstart drive
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        // Inject our custom 3-wheel localizer
        drive.setLocalizer(new RRThreeWheelLocalizer(hardwareMap));

        // Optional: zero starting pose
        drive.setPoseEstimate(new Pose2d(0, 0, 0));

        telemetry.addLine("Ready. Verify encoder directions before driving:");
        telemetry.addLine("- Push forward: L & R counts increase");
        telemetry.addLine("- Strafe left: Center count increases");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // If your SampleMecanumDrive supports field-centric drive, you can set powers here.
            // Example (uncomment if you have setWeightedDrivePower implemented):
            // drive.setWeightedDrivePower(
            //     new Pose2d(
            //         -gamepad1.left_stick_y,
            //          gamepad1.left_stick_x,
            //          gamepad1.right_stick_x
            //     )
            // );

            drive.update();

            Pose2d p = drive.getPoseEstimate();
            telemetry.addData("x (in)", "%.3f", p.getX());
            telemetry.addData("y (in)", "%.3f", p.getY());
            telemetry.addData("heading (deg)", "%.2f", Math.toDegrees(p.getHeading()));
            telemetry.update();
        }
    }

    public void update() {
    }



}


