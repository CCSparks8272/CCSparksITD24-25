package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name = "HomemadeTelop", group = "Telop")






public class HomemadeSteamingHotTelop extends OpMode
{
    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;



    @Override
    public void init()
    {
        // Get motor objects from hardware map and save in variables.
        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft = hardwareMap.dcMotor.get("FL");
        motorBackLeft = hardwareMap.dcMotor.get("BL");
        motorBackRight = hardwareMap.dcMotor.get("BR");

        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);

    }


    @Override
    public void loop()
    {
        double left_side_power = gamepad1.left_stick_y;
        double right_side_power = gamepad1.right_stick_y;
        motorFrontLeft.setPower(left_side_power);
        motorFrontRight.setPower(right_side_power);
        motorBackLeft.setPower(left_side_power);
        motorBackRight.setPower(right_side_power);
    }


    @Override
    public void stop()
    {
        // Tell motors to stop.
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }

}

