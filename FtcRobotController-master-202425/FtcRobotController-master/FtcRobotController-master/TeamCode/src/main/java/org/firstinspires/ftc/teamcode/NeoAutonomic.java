package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name= "Go Forward 2 Seconds", group="Autonomous")



public class NeoAutonomic extends LinearOpMode
{
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor AutoTemplate
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;

    private DcMotor horzLift;
    private DcMotor vertLift;

    private Servo grab;
    private Servo wrist;

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

        horzLift = hardwareMap.dcMotor.get("HORZ_LIFT");
        vertLift = hardwareMap.dcMotor.get("VERT_LIFT");

        grab = hardwareMap.servo.get("GRAB");
        wrist = hardwareMap.servo.get("WRIST");
        wrist.setPosition(1.1);
        grab.setPosition(0.0);


        waitForStart();
        sleep(100);

        goForward(0.4, 2000);
        sleep(50);

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
    public void specialDelivery(double vertpos, double grabpos, double wristpos) {
        while (vertLift.getCurrentPosition() < vertpos) {
            vertLift.setPower(0.5);
        }
        vertLift.setPower(0.0);
        grab.setPosition(grabpos);
        wrist.setPosition(wristpos);
    }
}

