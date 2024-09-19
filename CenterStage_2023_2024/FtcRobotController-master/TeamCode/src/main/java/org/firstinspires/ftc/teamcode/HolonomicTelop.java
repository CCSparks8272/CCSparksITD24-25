package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import com.qualcomm.robotcore.hardware.CRServo;



@TeleOp(name = "HolonomicTelop", group = "Telop")






public class HolonomicTelop extends OpMode
{
    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;

    private DcMotor liftUp;
    private DcMotor liftDown;

    private Servo popperServo;
    private Servo planeServo;


    @Override
    public void init()
    {
        // Get motor objects from hardware map and save in variables.
        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft = hardwareMap.dcMotor.get("FL");
        motorBackLeft = hardwareMap.dcMotor.get("BL");
        motorBackRight = hardwareMap.dcMotor.get("BR");



        liftUp = hardwareMap.dcMotor.get("UP");
        liftDown = hardwareMap.dcMotor.get("DOWN");
        liftDown.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        popperServo = hardwareMap.servo.get("POPS");
        planeServo = hardwareMap.servo.get("PLANE");

        popperServo.setPosition(0.8);
        planeServo.setPosition(0.85);

    }



    @Override
    public void loop()
    {
        // Put joystick positions into variables.
        double gamepad1LeftY = gamepad1.left_stick_y;
        double gamepad1LeftX = gamepad1.left_stick_x;
        double gamepad1RightX = gamepad1.right_stick_x;

        if(gamepad1LeftY <= 0.5 && gamepad1LeftY >= -0.5)
        {
            gamepad1LeftY = 0.0;
        }

        if(gamepad1LeftX <= 0.5 && gamepad1LeftX >= -0.5)
        {
            gamepad1LeftX = 0.0;
        }

        if(gamepad1RightX <= 0.5 && gamepad1RightX >= -0.5)
        {
            gamepad1RightX = 0.0;
        }
        double gamepad2LeftX = gamepad2.left_stick_x;
        double gamepad2LeftY = gamepad2.right_stick_y;

        // Get button values and put them in variables.
        boolean gamepad2ButtonA = gamepad2.a;
        boolean gamepad2ButtonB = gamepad2.b;
        boolean gamepad2ButtonX = gamepad2.x;
        boolean gamepad2ButtonY = gamepad2.y;
        boolean gamepad1ButtonA = gamepad1.a;
        boolean gamepad1ButtonB = gamepad1.b;
        boolean gamepad1ButtonX = gamepad1.x;
        boolean gamepad1ButtonY = gamepad1.y;
        double gamepad1TrigLeft = gamepad1.left_trigger;
        double gamepad1TrigRight = gamepad1.right_trigger;
        boolean gamepad2BumpLeft = gamepad2.left_bumper;
        boolean gamepad2BumpRight = gamepad2.right_bumper;
        boolean gamepad2TrigLeft = gamepad2.left_trigger > 0.5;
        boolean gamepad2TrigRight = gamepad2.right_trigger > 0.5;

        // Apply holonomic formulas to new variables for each motor's speed.
        double FrontLeft = gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        double FrontRight = -gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        double BackRight = -gamepad1LeftY + gamepad1LeftX - gamepad1RightX;
        double BackLeft = gamepad1LeftY + gamepad1LeftX - gamepad1RightX;

        // Adjust speed values to mode.
        // Right trigger ==> precision mode.
        // Left trigger ==> speed mode.
        if(gamepad1TrigLeft > 0.5 && gamepad1TrigRight <= 0.5)   // Speed mode. 100% speed.
        {
            FrontRight = Range.clip(FrontRight, -1, 1);
            FrontLeft = Range.clip(FrontLeft, -1, 1);
            BackLeft = Range.clip(BackLeft, -1, 1);
            BackRight = Range.clip(BackRight, -1, 1);
        }
        else if(gamepad1TrigRight > 0.5 && gamepad1TrigLeft <= 0.5)   // Slow mode. 50% speed.
        {
            FrontRight = Range.clip(FrontRight, -1, 1) * 0.5;
            FrontLeft = Range.clip(FrontLeft, -1, 1) * 0.5;
            BackLeft = Range.clip(BackLeft, -1, 1) * 0.5;
            BackRight = Range.clip(BackRight, -1, 1) * 0.5;
        }
        else   // Normal mode. 90% speed.
        {
            FrontRight = Range.clip(FrontRight, -1, 1) * 0.75;
            FrontLeft = Range.clip(FrontLeft, -1, 1) * 0.75;
            BackLeft = Range.clip(BackLeft, -1, 1) * 0.75;
            BackRight = Range.clip(BackRight, -1, 1) * 0.75;
        }

        // Write the values to the motors.
        motorFrontRight.setPower(FrontRight);
        motorFrontLeft.setPower(FrontLeft);
        motorBackLeft.setPower(BackLeft);
        motorBackRight.setPower(BackRight);

        telemetry.addData("front right", FrontRight);
        telemetry.addData("front left", FrontLeft);
        telemetry.addData("back right", BackRight);
        telemetry.addData("back left", BackLeft);
        telemetry.update();


        if(gamepad2BumpLeft && !gamepad2BumpRight && !gamepad2TrigLeft && !gamepad2TrigRight)   // Lift up.
        {
            liftUp.setPower(0.4);
            liftDown.setPower(0.0);
        }
        else if(!gamepad2BumpLeft && gamepad2BumpRight && !gamepad2TrigLeft && !gamepad2TrigRight)   // Lift down.
        {
            liftUp.setPower(0.0);
            liftDown.setPower(0.4);
        }
        else if(!gamepad2BumpLeft && !gamepad2BumpRight && gamepad2TrigLeft && !gamepad2TrigRight)
        {
            liftUp.setPower(-0.4);
            liftDown.setPower(0.0);
        }
        else if(!gamepad2BumpLeft && !gamepad2BumpRight && !gamepad2TrigLeft && gamepad2TrigRight)
        {
            liftUp.setPower(0.0);
            liftDown.setPower(-0.4);
        }
        else
        {
            liftUp.setPower(0.0);
            liftDown.setPower(0.0);
        }

        if(gamepad1ButtonA || gamepad2ButtonA)   // Button A on either controller launches airplane.
        {
            planeServo.setPosition(0.5);
        }


        if(gamepad2ButtonB)   // Button B on either controller locks airplane.
        {
            planeServo.setPosition(0.85);
        }
        if(gamepad2ButtonX){
            popperServo.setPosition(1.0);
        }
        if(gamepad2ButtonY){
            popperServo.setPosition(0.8);
        }
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

