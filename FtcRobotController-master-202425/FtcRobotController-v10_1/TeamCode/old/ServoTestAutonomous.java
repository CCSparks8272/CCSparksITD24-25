package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name= "ServoTestAutonomous", group="Autonomous")



public class ServoTestAutonomous extends LinearOpMode
{

    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;
    
    private Servo popperServo;
    private Servo planeServo;

    @Override
    public void runOpMode()
    {
        //Sets up motors for action once autonomous starts
        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft = hardwareMap.dcMotor.get("FL");
        motorBackLeft = hardwareMap.dcMotor.get("BL");
        motorBackRight = hardwareMap.dcMotor.get("BR");
        popperServo = hardwareMap.servo.get("POPS");
        planeServo = hardwareMap.servo.get("PLANE");

        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Pixel pop server position", popperServo.getPosition());
        telemetry.update();

        // Wait until autonomous starts
        waitForStart();

        //robot stays still while teammate's autonomous activates
        sleep(1000);


        popperServo.setPosition(0.8);
        sleep(1000);
        
        telemetry.addData("Pixel pop server position", popperServo.getPosition());
        telemetry.update();
        
        popperServo.setPosition(1.0);
        sleep(1000);
        
        telemetry.addData("Pixel pop server position", popperServo.getPosition());
        telemetry.update();
        
        popperServo.setPosition(0.8);
        sleep(1000);
        
        telemetry.addData("Pixel pop server position", popperServo.getPosition());
        telemetry.update();
        
        popperServo.setPosition(1.0);
        sleep(1000);
        
        telemetry.addData("Pixel pop server position", popperServo.getPosition());
        telemetry.update();

        planeServo.setPosition(0.5);
        sleep(1000);
        planeServo.setPosition(0.85);
        sleep(1000);
    }


    public void goForward(double speed, int milliseconds)
    {
        //helper to go forwa7rd
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

