package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class TurretVision {

    public enum Alliance { BLUE, RED }

    private final VisionPortal visionPortal;
    private final AprilTagProcessor aprilTag;

    // Goal tag IDs
    private static final int TAG_BLUE_GOAL = 20;
    private static final int TAG_RED_GOAL  = 24;

    private Alliance alliance = Alliance.BLUE; // default
    private int activeTargetId = TAG_BLUE_GOAL;

    private double kP = 0.02;         // turret responsiveness
    private double deadbandDeg = 1.0; // stop hunting near center
    private double maxPower = 0.35;

    private boolean lastFound = false;
    private double lastBearing = 0.0;

    public TurretVision(HardwareMap hw) {
        aprilTag = new AprilTagProcessor.Builder()
                // .setDecimation(2)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hw.get(WebcamName.class, "WebcamL"))
                .addProcessor(aprilTag)
                .enableLiveView(true)
                .build();

        setAlliance(Alliance.BLUE);
    }

    public void setAlliance(Alliance a) {
        alliance = a;
        activeTargetId = (a == Alliance.BLUE) ? TAG_BLUE_GOAL : TAG_RED_GOAL;
    }

    public Alliance getAlliance() { return alliance; }
    public int getActiveTargetId() { return activeTargetId; }

    public double getTurretPower() {
        AprilTagDetection target = pickActiveTarget(aprilTag.getDetections());
        if (target == null || target.ftcPose == null) {
            lastFound = false;
            return 0.0;
        }

        lastFound = true;
        lastBearing = target.ftcPose.bearing;

        double errDeg = lastBearing;
        if (Math.abs(errDeg) < deadbandDeg) return 0.0;
        return Range.clip(kP * errDeg, -maxPower, maxPower);
    }

    private AprilTagDetection pickActiveTarget(List<AprilTagDetection> detections) {
        AprilTagDetection best = null;
        double bestRange = Double.POSITIVE_INFINITY;

        for (AprilTagDetection d : detections) {
            if (d == null || d.ftcPose == null) continue;
            if (d.id != activeTargetId) continue;

            double r = d.ftcPose.range;
            if (r < bestRange) {
                bestRange = r;
                best = d;
            }
        }
        return best;
    }

    public boolean isTagFound() { return lastFound; }
    public double getLastBearing() { return lastBearing; }

    public int getDetectionCount() {
        return aprilTag.getDetections().size();
    }

    public String getDetectedIdsString() {
        List<AprilTagDetection> dets = aprilTag.getDetections();
        if (dets.isEmpty()) return "none";

        StringBuilder sb = new StringBuilder();
        int shown = 0;
        for (AprilTagDetection d : dets) {
            if (d == null) continue;
            if (shown > 0) sb.append(", ");
            sb.append(d.id);
            shown++;
            if (shown >= 6) { sb.append("..."); break; } // keep telemetry short
        }
        return sb.toString();
    }

    public void close() {
        visionPortal.close();
    }

    public void setKP(double kP) { this.kP = kP; }
    public void setDeadbandDeg(double deadbandDeg) { this.deadbandDeg = deadbandDeg; }
    public void setMaxPower(double maxPower) { this.maxPower = maxPower; }
}
