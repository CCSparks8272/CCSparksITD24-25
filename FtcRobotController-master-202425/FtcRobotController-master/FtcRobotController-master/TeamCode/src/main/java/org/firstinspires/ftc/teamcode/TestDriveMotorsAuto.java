package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name= "Test Drive Motors -- FR, FL, BR, BL", group="Autonomous")



public class TestDriveMotorsAuto extends LinearOpMode
{

    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;


    @Override
    public void runOpMode()
    {
        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft = hardwareMap.dcMotor.get("FL");
        motorBackLeft = hardwareMap.dcMotor.get("BL");
        motorBackRight = hardwareMap.dcMotor.get("BR");

        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);

        double speed = 0.8;
        waitForStart();

        sleep(100);
        telemetry.addData("Motor Testing", "FR, 80% speed forward.");
        telemetry.update();
        motorFrontRight.setPower(speed);
        sleep(5000);
        motorFrontRight.setPower(0.0);

        telemetry.addData("Motor Testing", "FL, 80% speed forward.");
        telemetry.update();
        motorFrontLeft.setPower(speed);
        sleep(5000);
        motorFrontLeft.setPower(0.0);

        telemetry.addData("Motor Testing", "BR, 80% speed forward.");
        telemetry.update();
        motorBackRight.setPower(speed);
        sleep(5000);
        motorBackRight.setPower(0.0);

        telemetry.addData("Motor Testing", "BL, 80% speed forward.");
        telemetry.update();
        motorBackLeft.setPower(speed);
        sleep(5000);
        motorBackLeft.setPower(0.0);

    }


}

