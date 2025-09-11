package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name= "MotorTestAutonomous", group="Autonomous")
public class MotorTestAutonomous extends LinearOpMode
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
        motorBackRight = hardwareMap.dcMotor.get("BR");
        motorBackLeft = hardwareMap.dcMotor.get("BL");

        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();

        // Test each motor in forward direction.
        testMotor(motorFrontRight, "Motor front right running forward.");
        testMotor(motorFrontLeft, "Motor front left running forward.");
        testMotor(motorBackRight, "Motor back right running forward.");
        testMotor(motorBackLeft, "Motor back left running forward.");

        // Test each motor in reverse direction.
        testMotor(motorFrontRight, "Motor front right running backward.", true);
        testMotor(motorFrontLeft, "Motor front left running backward.", true);
        testMotor(motorBackRight, "Motor back right running backward.", true);
        testMotor(motorBackLeft, "Motor back left running backward.", true);
    }

    private void testMotor(DcMotor motor, String message)
    {
        testMotor(motor, message, false);
    }

    private void testMotor(DcMotor motor, String message, boolean reverse)
    {
        telemetry.addData("Status", message);
        telemetry.update();

        double power;
        if (reverse)
        {
            power = -1.0;
        }
        else
        {
            power = 1.0;
        }

        motor.setPower(power);
        sleep(5000); // Run motor for 5 seconds.
        motor.setPower(0.0);

        telemetry.addData("Status", "Motor stopped.");
        telemetry.update();
        sleep(500); // Brief pause between motor tests.
    }
}
