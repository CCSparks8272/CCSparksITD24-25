package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.ArrayList;

@Autonomous(name= "BlueSpinRightRoundAutonomous", group="Autonomous")



public class BlueSpinRightRoundAutonomous extends LinearOpMode
{

    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;

    private Servo popperServo;
    private Servo planeServo;

    private DistanceSensor sensorRange;
    private ColorSensor color;


    @Override
    public void runOpMode() throws InterruptedException {
        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft = hardwareMap.dcMotor.get("FL");
        motorBackLeft = hardwareMap.dcMotor.get("BL");
        motorBackRight = hardwareMap.dcMotor.get("BR");
        popperServo = hardwareMap.servo.get("POPS");
        planeServo = hardwareMap.servo.get("PLANE");

        sensorRange = hardwareMap.get(DistanceSensor.class, "sensor_range");
        color = hardwareMap.get(ColorSensor.class, "color");

        popperServo.setPosition(0.8);
        planeServo.setPosition(0.85);

        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();
        popperServo.setPosition(0.8);
        int wait_time = 500;
        sleep(wait_time);

        goForward(0.3, 1000);
        sleep(200);
        goRight(0.3, 1000);
        sleep(200);
        resetEncoders();
        track(50.0, 0.2);
        double encAvg = getEncoderAvg();
        telemetry.addData("Spin", encAvg);
        telemetry.update();



        if(encAvg < 380)   // Left tape.
        {
            motorBackLeft.setPower(0.2);
            motorBackRight.setPower(0.2);
            motorFrontLeft.setPower(0.2);
            motorFrontRight.setPower(0.2);

            while (opModeIsActive() && color.red() < 500 && color.blue() < 500)
            {
                telemetry.addData("Red", color.red());
                telemetry.addData("Green", color.green());
                telemetry.addData("Blue", color.blue());
                telemetry.update();
                sleep(10);   // Small poll wait.
            }
            telemetry.addData("Red", color.red());
            telemetry.addData("Green", color.green());
            telemetry.addData("Blue", color.blue());
            telemetry.update();

            stopMotors();
            sleep(500);
        }
        else if (encAvg < 700)   // Middle tape.
        {
            motorBackLeft.setPower(0.2);
            motorBackRight.setPower(0.2);
            motorFrontLeft.setPower(0.2);
            motorFrontRight.setPower(0.2);

            while (opModeIsActive() && color.red() < 500 && color.blue() < 500)
            {
                telemetry.addData("Red", color.red());
                telemetry.addData("Green", color.green());
                telemetry.addData("Blue", color.blue());
                telemetry.update();
                sleep(10);   // Small poll wait.
            }
            telemetry.addData("Red", color.red());
            telemetry.addData("Green", color.green());
            telemetry.addData("Blue", color.blue());
            telemetry.update();

            stopMotors();
            sleep(500);
        }
        else      // Right tape.
        {
            goForward(0.2, 3000);
        }





        /*motorBackLeft.setPower(0.2);
        motorBackRight.setPower(0.2);
        motorFrontLeft.setPower(0.2);
        motorFrontRight.setPower(0.2);
        int colorAdjust = 50;
        while (opModeIsActive() && color.red() < 500 && color.blue() < 500)
        {
            telemetry.addData("Red", color.red());
            telemetry.addData("Green", color.green());
            telemetry.addData("Blue", color.blue());
            telemetry.update();
            sleep(10);   // Small poll wait.
        }
        telemetry.addData("Red", color.red());
        telemetry.addData("Green", color.green());
        telemetry.addData("Blue", color.blue());
        telemetry.update();
        stopMotors();
        sleep(500);

        track(20.0, 0.5);
        sleep((wait_time)/2);*/

        /*popperServo.setPosition(0.8);
        sleep(wait_time);
        popperServo.setPosition(1.0);
        sleep(wait_time);
        popperServo.setPosition(0.8);
        sleep(wait_time);
        popperServo.setPosition(1.0);
        sleep(wait_time);*/
        //   turn 90 degrees
        //   read sensor
        //   if dist is less than min dist
        //     update min dist to be the new minimum distance.
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


    public void track(double threshold, double speed)
    {
        rotate(speed);  // Rotate the robot at the specified speed.
        telemetry.addData("Dist ", sensorRange.getDistance(DistanceUnit.CM));
        telemetry.update();
        while (opModeIsActive() && sensorRange.getDistance(DistanceUnit.CM) > threshold)   // Loop to turn until the target is initially spotted.
        {
            telemetry.addData("Dist ", sensorRange.getDistance(DistanceUnit.CM));
            telemetry.update();
            sleep(10);   // Small poll wait.
        }
        stopMotors();
        sleep(500);   // Wait for the motors to stop and the sensor to stabilize.
        telemetry.addData("Dist ", sensorRange.getDistance(DistanceUnit.CM));
        telemetry.update();
        if (sensorRange.getDistance(DistanceUnit.CM) > threshold)   // Did it overshoot?
        {
            track(threshold, -speed / 2);  // Recursive call with half the speed in opposite direction.
        }
    }

    private void rotate(double speed)
    {
        motorFrontRight.setPower(speed);
        motorBackRight.setPower(speed);
        motorFrontLeft.setPower(-speed);
        motorBackLeft.setPower(-speed);
    }

    private void stopMotors()
    {
        motorFrontRight.setPower(0.0);
        motorBackRight.setPower(0.0);
        motorFrontLeft.setPower(0.0);
        motorBackLeft.setPower(0.0);
    }

    public void resetEncoders()
    {
        // Reset encoder values to zero for all motors.
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Set them back to a mode for freely running while still tracking encoder counts.
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    public double getEncoderAvg()
    {
        ArrayList<Integer> encoderValues = new ArrayList<>();
        encoderValues.add(motorFrontRight.getCurrentPosition());
        encoderValues.add(motorFrontLeft.getCurrentPosition());
        encoderValues.add(motorBackLeft.getCurrentPosition());
        encoderValues.add(motorBackRight.getCurrentPosition());
        double sum = 0;
        int validEncoders = 0;
        for (int value : encoderValues)
        {
            if (Math.abs(value) > 10) // Exclude near-zero values indicating a bad encoder.
            {
                sum += Math.abs(value);
                validEncoders++;
            }
        }
        // Avoid division by zero if all encoders are considered 'bad'.
        if (validEncoders == 0) return 0;
        return sum / validEncoders; // Return the average of valid encoder values.
    }

}

