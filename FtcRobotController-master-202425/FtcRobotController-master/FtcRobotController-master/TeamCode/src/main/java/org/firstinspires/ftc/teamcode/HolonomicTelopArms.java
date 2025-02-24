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

    //private DcMotor winch;
    private DcMotor horzLift;
    private DcMotor vertLift;
    private DcMotor intake;

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

        //winch = hardwareMap.dcMotor.get("WINCH");
        vertLift = hardwareMap.dcMotor.get("VERT_LIFT");
        horzLift = hardwareMap.dcMotor.get("HORZ_LIFT");
        horzLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake = hardwareMap.dcMotor.get("INTAKE");
        //motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        //motorBackRight.setDirection(DcMotor.Direction.FORWARD);
        //motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        //motorFrontRight.setDirection(DcMotor.Direction.FORWARD);

        grab = hardwareMap.servo.get("GRAB");
        wrist = hardwareMap.servo.get("WRIST");
        wrist.setPosition(1.1);
        grab.setPosition(0.0);
       // setupEncoders();
    }



    @Override
    public void loop()
    {
        grabber();
        intake();
        // Put joystick positions into variables.
        double gamepad1LeftY = -gamepad1.left_stick_y;
        double gamepad1LeftX = gamepad1.left_stick_x;
        double gamepad1RightX = -gamepad1.right_stick_x;
        double gamepad1RightY = gamepad1.right_stick_y;

        double gamepad2LeftY = gamepad2.left_stick_y;
        double gamepad2LeftX = gamepad2.left_stick_x;
        double gamepad2RightY = gamepad2.right_stick_y;
        double gamepad2RightX = gamepad2.right_stick_x;

        if(gamepad2LeftY <= 0.5 && gamepad2LeftY >= -0.5) // Deadzone.
        {
            gamepad2LeftY = 0.0;
        }

        if(gamepad2LeftX <= 0.5 && gamepad2LeftX >= -0.5) // Deadzone.
        {
            gamepad2LeftX = 0.0;
        }

        if(gamepad2RightY <= 0.5 && gamepad2RightY >= -0.5) // Deadzone.
        {
            gamepad2RightY = 0.0;
        }

        if(gamepad2RightX <= 0.5 && gamepad2RightX >= -0.5) // Deadzone.
        {
            gamepad2RightX = 0.0;
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

        vertLift.setPower(0.5*gamepad2RightY);
        horzLift.setPower(0.5*gamepad2LeftY);

        // Write the values to the motors.
        motorFrontRight.setPower(FrontRight);
        motorFrontLeft.setPower(FrontLeft);
        motorBackLeft.setPower(BackLeft);
        motorBackRight.setPower(BackRight);

      /*  if(horzLift.getCurrentPosition() > -2240)
        {
            horzLift.setPower(Range.clip(gamepad2LeftY*0.5, 0, 1));
        }
        else if (horzLift.getCurrentPosition() < 0)
        {
            horzLift.setPower(Range.clip(gamepad2LeftY*0.5, -1, 0));
        }
        else
        {
            horzLift.setPower(Range.clip(gamepad2LeftY*0.5, -1, 1));
        }

        if (vertLift.getCurrentPosition() < 1000)
        {

            vertLift.setPower(Range.clip(gamepad2LeftX*0.5, 0, 1));

        }

        if (vertLift.getCurrentPosition() > -100)
        {

            vertLift.setPower(Range.clip(gamepad2LeftX*0.5, -1, 0));

        }
        if (vertLift.getPower() == 0.0 && vertLift.getCurrentPosition() > 200) {
                vertLift.setPower(-0.1);
        }
*/
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

    private boolean isPressed = false;
    private boolean previousAState = false;
    private boolean previousBState = false;

    public void intake()
    {
        boolean gamepad2ButtonY = gamepad2.y;
        boolean gamepad2ButtonX = gamepad2.x;

        if (gamepad2ButtonX)
        {
            intake.setPower(.8);
        }
        else if (gamepad2ButtonY)
        {
            intake.setPower(-.8);
        }
        else {
            intake.setPower(0.0);
        }
    }

    public void grabber()
    {
        boolean gamepad2ButtonA = gamepad2.a;
        boolean gamepad2ButtonB = gamepad2.b;

        if (gamepad2ButtonA && !previousAState)
        {
            isPressed = true;
        }
        if (gamepad2ButtonB && !previousBState)
        {
            isPressed = false;
        }

        previousAState = gamepad2ButtonA;
        previousBState = gamepad2ButtonB;

        if (isPressed)
        {
            wrist.setPosition(0.5);
            grab.setPosition(0.3);
        } else
        {
            wrist.setPosition(1.1);
            grab.setPosition(0.0);
        }
    }
    public void setupEncoders(){
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        horzLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vertLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        horzLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        vertLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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

