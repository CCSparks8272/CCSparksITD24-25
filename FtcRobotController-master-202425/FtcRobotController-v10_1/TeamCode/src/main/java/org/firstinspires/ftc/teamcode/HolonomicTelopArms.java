package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import com.qualcomm.robotcore.hardware.CRServo;



@TeleOp(name = "Holonomic Telop - Lifts", group = "Telop")




public class HolonomicTelopArms extends OpMode
{
   /* static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor AutoTemplate
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    private ElapsedTime runtime = new ElapsedTime(); */

    static final double COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor AutoTemplate
    static final double DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;

    private DcMotor horzLift;
    private DcMotor vertLift;

    private Servo grab;
    private Servo wrist;



    @Override
    public void init()
    {
        // Get motor objects from hardware map and save in variables.
        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft = hardwareMap.dcMotor.get("FL");
        motorBackLeft = hardwareMap.dcMotor.get("BL");
        motorBackRight = hardwareMap.dcMotor.get("BR");

        vertLift = hardwareMap.dcMotor.get("VERT_LIFT");
        horzLift = hardwareMap.dcMotor.get("HORZ_LIFT");

        grab = hardwareMap.servo.get("GRAB");
        wrist = hardwareMap.servo.get("WRIST");
       // setupEncoders();
    }



    @Override
    public void loop()
    {
        // Put joystick positions into variables.
        double gamepad1LeftY = gamepad1.left_stick_y;
        double gamepad1LeftX = gamepad1.left_stick_x;
        double gamepad1RightX = gamepad1.right_stick_x;

        double gamepad2LeftY = gamepad2.left_stick_y;
        double gamepad2RightY = gamepad2.right_stick_y;

        if(gamepad2LeftY <= 0.5 && gamepad2LeftY >= -0.5) // Deadzone.
        {
            gamepad2LeftY = 0.0;
        }

        if(gamepad2RightY <= 0.5 && gamepad2RightY >= -0.5) // Deadzone.
        {
            gamepad2RightY = 0.0;
        }

        if(gamepad1LeftY <= 0.5 && gamepad1LeftY >= -0.5) // Deadzone.
        {
            gamepad1LeftY = 0.0;
        }

        if(gamepad1LeftY > 0.9) // Deadzone.
        {
            gamepad1LeftY = 1.0;
        }

        if(gamepad1LeftY < -0.9) // Deadzone.
        {
            gamepad1LeftY = -1.0;
        }

        if(gamepad1LeftX <= 0.5 && gamepad1LeftX >= -0.5) // Deadzone.
        {
            gamepad1LeftX = 0.0;
        }

        if(gamepad1RightX <= 0.5 && gamepad1RightX >= -0.5) // Deadzone.
        {
            gamepad1RightX = 0.0;
        }


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
        if(gamepad2ButtonA) {
            wrist.setPosition(0.9);
        }
        else if(gamepad2ButtonB) {
            wrist.setPosition(0.1);
        }
        if(gamepad2ButtonX) {
            grab.setPosition(0.9);
        }
        else if(gamepad2ButtonY) {
            grab.setPosition(0.1);
        }

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
        else if(gamepad1TrigRight > 0.5 && gamepad1TrigLeft <= 0.5)   // Slow mode. 40% speed.
        {
            FrontRight = Range.clip(FrontRight, -1, 1) * 0.4;
            FrontLeft = Range.clip(FrontLeft, -1, 1) * 0.4;
            BackLeft = Range.clip(BackLeft, -1, 1) * 0.4;
            BackRight = Range.clip(BackRight, -1, 1) * 0.4;
        }
        else                                                        // Normal mode. 80% speed.
        {
            FrontRight = Range.clip(FrontRight, -1, 1) * 0.8;
            FrontLeft = Range.clip(FrontLeft, -1, 1) * 0.8;
            BackLeft = Range.clip(BackLeft, -1, 1) * 0.8;
            BackRight = Range.clip(BackRight, -1, 1) * 0.8;
        }
        // Write the values to the motors.
        motorFrontRight.setPower(-FrontRight);
        motorFrontLeft.setPower(-FrontLeft);
        motorBackLeft.setPower(-BackLeft);
        motorBackRight.setPower(-BackRight);

        if(horzLift.getCurrentPosition() < -1300)
        {
            horzLift.setPower(Range.clip(gamepad2LeftY*0.5, 0, 1));
        }
        else if (horzLift.getCurrentPosition() > -100)
        {
            horzLift.setPower(Range.clip(gamepad2LeftY*0.5, -1, 0));
        }
        else
        {
            horzLift.setPower(Range.clip(gamepad2LeftY*0.5, -1, 1));
        }

        //if
        //vertLift.setPower(gamepad2RightY);

        // Telemetry.
        telemetry.addData("Encoder Vert Lift", vertLift.getCurrentPosition());
        telemetry.addData("Encoder Horz Lift", horzLift.getCurrentPosition());
        telemetry.update();
        telemetry.addData("front right", FrontRight);
        telemetry.addData("front left", FrontLeft);
        telemetry.addData("back right", BackRight);
        telemetry.addData("back left", BackLeft);
        telemetry.update();
    }
   /* public void setupEncoders(){
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }*/

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

