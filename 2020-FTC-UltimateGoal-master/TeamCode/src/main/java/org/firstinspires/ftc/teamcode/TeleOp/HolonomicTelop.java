package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
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

    private DcMotor linkageMotor;

    private DcMotor liftMotorBack;
    private DcMotor liftMotorFront;

    private Servo baseServoLeft;
    private Servo baseServoRight;


    /*
    private DcMotor scraper;
    private Servo liftServo;
    private Servo lemonServo;*/
    //private Servo grabber;

    //private Servo reeeServo;
    //private boolean locked;


    @Override
    public void init()
    {
        // Get motor objects from hardware map and save in variables.
        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft = hardwareMap.dcMotor.get("FL");
        motorBackLeft = hardwareMap.dcMotor.get("BL");
        motorBackRight = hardwareMap.dcMotor.get("BR");
        linkageMotor = hardwareMap.dcMotor.get("LINK");
        liftMotorBack = hardwareMap.dcMotor.get("GRAB_BACK");
        liftMotorFront = hardwareMap.dcMotor.get("GRAB_FRONT");
        baseServoLeft = hardwareMap.servo.get("GRAB_L");
        baseServoRight = hardwareMap.servo.get("GRAB_R");
        //liftMotorap.dcMotor.gaet("MRLIFT");
        //        //scraper = hardwareMap = hardwareM.dcMotor.get("YEET");
        //liftServo = hardwareMap.servo.get("MRSLIFT");
        //lemonServo = hardwareMap.servo.get("LC");
        //reeeServo = hardwareMap.servo.get("REEE");

        //lemonServo.setPosition(1.0);
        //liftServo.setPosition(0.8);
        //grabber.setPosition(1.0);
    }



    @Override
    public void loop()
    {
        //scraper.setDirection(DcMotor.Direction.REVERSE);

        // Put joystick positions into variables.
        double gamepad1LeftY = gamepad1.left_stick_y;
        double gamepad1LeftX = gamepad1.left_stick_x;
        double gamepad1RightX = gamepad1.right_stick_x;
        double gamepad2LeftY = gamepad2.left_stick_y;
        double gamepad2RightY = gamepad2.right_stick_y;

        // Get button values and put them in variables.
        boolean gamepad2ButtonA = gamepad2.a;
        boolean gamepad2ButtonB = gamepad2.b;
        boolean gamepad2ButtonX = gamepad2.x;
        boolean gamepad2ButtonY = gamepad2.y;
        double gamepad1TrigLeft = gamepad1.left_trigger;
        double gamepad1TrigRight = gamepad1.right_trigger;

        // Apply holonomic formulas to new variables for each motor's speed.
        double FrontLeft = gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        double FrontRight = -gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        double BackRight = -gamepad1LeftY + gamepad1LeftX - gamepad1RightX;
        double BackLeft = gamepad1LeftY + gamepad1LeftX - gamepad1RightX;

        // Adjust speed values to mode.
        // Right trigger ==> precision mode.
        // Left trigger ==> speed mode.
        if(gamepad1TrigLeft > 0.5 && gamepad1TrigRight <= 0.5)   // Speed mode.
        {
            // Clip the speed values so that they fit the range [-1, 1].
            FrontRight = Range.clip(FrontRight, -1, 1);
            FrontLeft = Range.clip(FrontLeft, -1, 1);
            BackLeft = Range.clip(BackLeft, -1, 1);
            BackRight = Range.clip(BackRight, -1, 1);
        }
        else if(gamepad1TrigRight > 0.5 && gamepad1TrigLeft <= 0.5)   // Slow mode.
        {
            // Clip the speed values so that they fit the range [-1, 1].
            // Then adjust the values to 50% power.
            FrontRight = Range.clip(FrontRight, -1, 1) * 0.5;
            FrontLeft = Range.clip(FrontLeft, -1, 1) * 0.5;
            BackLeft = Range.clip(BackLeft, -1, 1) * 0.5;
            BackRight = Range.clip(BackRight, -1, 1) * 0.5;
        }
        else   // Normal mode.
        {
            // Clip the speed values so that they fit the range [-1, 1].
            // Then adjust the values to 90% power.
            FrontRight = Range.clip(FrontRight, -1, 1) * 0.9;
            FrontLeft = Range.clip(FrontLeft, -1, 1) * 0.9;
            BackLeft = Range.clip(BackLeft, -1, 1) * 0.9;
            BackRight = Range.clip(BackRight, -1, 1) * 0.9;
        }

        // Write the values to the motors.
        motorFrontRight.setPower(FrontRight);
        motorFrontLeft.setPower(FrontLeft);
        motorBackLeft.setPower(BackLeft);
        motorBackRight.setPower(BackRight);
        linkageMotor.setPower( 0.8 * gamepad2LeftY);
        // Set servo to lock / unlock.

        if(gamepad2ButtonB)
        {
            liftMotorBack.setPower(0.8);
            liftMotorFront.setPower(-0.8);
        }
        else if(gamepad2ButtonA)
        {
            liftMotorBack.setPower(-0.8);
            liftMotorFront.setPower(0.8);
        }
        else
        {
            liftMotorBack.setPower(0.0);
            liftMotorFront.setPower(0.0);
        }

        if(gamepad2ButtonX)
        {
            baseServoLeft.setPosition(0.8);
            baseServoRight.setPosition(0.2);
        }
        else if(gamepad2ButtonY)
        {
            baseServoLeft.setPosition(0.2);
            baseServoRight.setPosition(0.8);
        }

    /*
        // Control the lift.
        liftMotor.setPower(-gamepad2LeftY);
        scraper.setPower(gamepad2RightY);
        */
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

