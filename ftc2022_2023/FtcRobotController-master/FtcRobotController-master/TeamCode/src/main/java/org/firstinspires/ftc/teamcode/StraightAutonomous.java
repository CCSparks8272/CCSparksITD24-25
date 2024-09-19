package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name= "straightAutonomous", group="Autonomous")



public class StraightAutonomous extends LinearOpMode
{

    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;

    private DcMotor linkageMotor;

    private Servo baseServoLeft;
    private Servo baseServoRight;

    @Override
    public void runOpMode()
    {
        //Sets up motors for action once autonomous starts
        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft = hardwareMap.dcMotor.get("FL");
        motorBackLeft = hardwareMap.dcMotor.get("BL");
        motorBackRight = hardwareMap.dcMotor.get("BR");
        //linkageMotor = hardwareMap.dcMotor.get("LINK");
        //baseServoLeft = hardwareMap.servo.get("GRAB_L");
        //baseServoRight = hardwareMap.servo.get("GRAB_R");
        //liftServoBack = hardwareMap.crservo.get("GRAB_BACK");
        //liftServoFront = hardwareMap.crservo.get("GRAB_FRONT");

        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);

        // Wait until autonomous starts
        waitForStart();
        //robot stays still while teammate's autonomous activates
        sleep(1000);


        motorFrontRight.setPower(0.5);
        motorFrontLeft.setPower(0.5);
        motorBackLeft.setPower(0.5);
        motorBackRight.setPower(0.5);
        //test();
        // Go forward toward foundation
        //goForward(0.6, 2000);
        sleep(2500);
        //goForward(0.0,15000);
        motorBackLeft.setPower(0.0);
        motorBackRight.setPower(0.0);
        motorFrontLeft.setPower(0.0);
        motorFrontRight.setPower(0.0);

    }


    public void goForward(double speed, int milliseconds)
    {
        //helper to go forward
        motorFrontRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorBackLeft.setPower(speed);
        motorBackRight.setPower(speed);
        sleep(milliseconds);
        motorFrontRight.setPower(0.0);
        motorFrontLeft.setPower(0.0);
        motorBackLeft.setPower(0.0);
        motorBackRight.setPower(0.0);
        sleep( 200);
    }


    public void goLeft(double speed, int milliseconds)
    {
        //helper to go left
        motorFrontRight.setPower(speed);
        motorBackRight.setPower(speed);
        motorFrontLeft.setPower(-speed);
        motorBackLeft.setPower(-speed);
        sleep(milliseconds);
        motorFrontRight.setPower(0.0);
        motorFrontLeft.setPower(0.0);
        motorBackLeft.setPower(0.0);
        motorBackRight.setPower(0.0);
        sleep( 200 );
    }





    public void goRight(double speed, int milliseconds)
    {
        //helper to go right
        motorFrontRight.setPower(-speed);
        motorBackRight.setPower(-speed);
        motorFrontLeft.setPower(speed);
        motorBackLeft.setPower(speed);
        sleep(milliseconds);
        motorFrontRight.setPower(0.0);
        motorFrontLeft.setPower(0.0);
        motorBackLeft.setPower(0.0);
        motorBackRight.setPower(0.0);
        sleep( 200);
    }



    public void goBackwards(double speed, int milliseconds)
    {
        //helper to go backwards
        motorFrontRight.setPower(-speed);
        motorBackRight.setPower(-speed);
        motorFrontLeft.setPower(-speed);
        motorBackLeft.setPower(-speed);
        sleep(milliseconds);
        motorFrontRight.setPower(0.0);
        motorBackRight.setPower(0.0);
        motorFrontLeft.setPower(0.0);
        motorBackLeft.setPower(0.0);
        sleep( 200);

    }

}

