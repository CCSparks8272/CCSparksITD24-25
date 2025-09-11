package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name= "Observation Zone Auto", group="Autonomous")



public class ObsZoneAuto extends LinearOpMode
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

        waitForStart();
        sleep(100);

        goForward(0.4, 200);

        strafeRight(0.2, 3000);

        goBackwards(0.4, 200);


    }


    public void goForward(double speed, int milliseconds)
    {
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

    public void strafeRight(double speed, int milliseconds)
    {
        motorFrontRight.setPower(-speed);
        motorBackRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorBackLeft.setPower(-speed);
        sleep(milliseconds);
        motorFrontRight.setPower(0.0);
        motorBackRight.setPower(0.0);
        motorFrontLeft.setPower(0.0);
        motorBackLeft.setPower(0.0);
        sleep( 200);
    }

    public void strafeLeft(double speed, int milliseconds)
    {
        strafeRight(-speed, milliseconds);
    }
}

