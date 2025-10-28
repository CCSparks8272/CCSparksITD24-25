package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Minimal stand-in for Quickstart's SampleMecanumDrive, ONLY for odometry testing.
 * If you have the Quickstart version, use that instead.
 */
class SampleMecanumDrive {
    private Localizer localizer;
    private Pose2d pose = new Pose2d();

    public SampleMecanumDrive(HardwareMap hw) {
        // In Quickstart, motors and kinematics are set up here.
        // We keep this empty for a minimal odometry test build.
    }

    public void setLocalizer(Localizer loc) {
        this.localizer = loc;
    }

    public void setPoseEstimate(Pose2d pose) {
        this.pose = pose;
        if (localizer != null) localizer.setPoseEstimate(pose);
    }

    public Pose2d getPoseEstimate() {
        return (localizer != null) ? localizer.getPoseEstimate() : pose;
    }

    public void update() {
        if (localizer != null) {
            localizer.update();
            pose = localizer.getPoseEstimate();
        }
    }

    // If you add drivability, implement setWeightedDrivePower(...) here.
}
