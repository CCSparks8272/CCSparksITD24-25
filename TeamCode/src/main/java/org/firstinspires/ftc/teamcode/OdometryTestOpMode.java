package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.acmerobotics.dashboard.config.Config;

@TeleOp(name = "Odometry Visualization Test")
@Config
public class OdometryTestOpMode extends LinearOpMode {

    // Odometry constants
    public static double TICKS_PER_REV = 8192;
    public static double WHEEL_DIAMETER = 2.0; // in inches
    public static double TRACK_WIDTH = 14.5;   //distance between two encoders (just a place value)
    public static double CENTER_WHEEL_OFFSET = -6.5; // distance of center wheel from  center(placer)

    @Override
    public void runOpMode() {
        Odometry odo = new Odometry(
                hardwareMap,
                "leftEncoder", "rightEncoder", "centerEncoder",
                TICKS_PER_REV, WHEEL_DIAMETER, TRACK_WIDTH, CENTER_WHEEL_OFFSET
        );

        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        waitForStart();

        while (opModeIsActive()) {
            odo.update();

            double x = odo.getX();
            double y = odo.getY();
            double heading = odo.getHeading();

            telemetry.addData("X (in)", x);
            telemetry.addData("Y (in)", y);
            telemetry.addData("Heading (rad)", heading);
            telemetry.update();

            TelemetryPacket packet = new TelemetryPacket();
            Canvas canvas = packet.fieldOverlay();

            canvas.setStroke("blue");
            canvas.strokeCircle(x, y, 3);
            canvas.strokeLine(
                    x, y,
                    x + 6 * Math.cos(heading),
                    y + 6 * Math.sin(heading)
            );

            packet.put("X (in)", x);
            packet.put("Y (in)", y);
            packet.put("Heading (rad)", heading);

            dashboard.sendTelemetryPacket(packet);


            sleep(20);
        }
    }
}


