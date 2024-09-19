package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name= "StraightEndcoder", group="Autonomous")

public class StraightEndcoder extends LinearOpMode {



    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor AutoTemplate
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;

    private Servo popperServo;
    private Servo planeServo;

    @Override
    public void runOpMode() {



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
        setupEncoders();

        /*telemetry.addData("Encoder front right", motorFrontRight.getCurrentPosition());
        telemetry.addData("Encoder front left", motorFrontLeft.getCurrentPosition());
        telemetry.addData("Encoder back right", motorBackRight.getCurrentPosition());
        telemetry.addData("Encoder back left", motorBackLeft.getCurrentPosition());
        telemetry.update();*/

        List<Integer> faultyEncoders = new ArrayList<>();
        faultyEncoders.add(1); // Index 1 corresponds to the front-left motor.

        waitForStart();


        //robot stays still while teammate's autonomous activates
        sleep(1000);
        runPositionEncoders(0.5,9,10, faultyEncoders);
        stopMotors(1);
        sleep(1000);


        // Wiggle servo to pop one pixel.
        int wait_time = 500;
        popperServo.setPosition(0.8);
        sleep(wait_time);
        popperServo.setPosition(1.0);
        sleep(wait_time);
        popperServo.setPosition(0.8);
        sleep(wait_time);
        popperServo.setPosition(1.0);
        sleep(wait_time);
    }




    public void setupEncoders(){
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }




    public void stopMotors(int time)
    {
        motorFrontRight.setPower(0.0);
        motorBackRight.setPower(0.0);
        motorFrontLeft.setPower(0.0);
        motorBackLeft.setPower(0.0);
        sleep(time);

    }




    public void strafePositionEncoders(int power, int position, boolean left, boolean right, int time){
        int newFrontLeftTarget = 0;
        int newBackLeftTarget = 0;
        int newFrontRightTarget = 0;
        int newBackRightTarget = 0;

        if(left == true) {
            newBackRightTarget = motorBackRight.getCurrentPosition() + (int) (-position * COUNTS_PER_INCH);
            newFrontRightTarget = motorFrontRight.getCurrentPosition() + (int) (position * COUNTS_PER_INCH);
            newBackLeftTarget = motorBackLeft.getCurrentPosition() + (int) (-position * COUNTS_PER_INCH);
            newFrontLeftTarget = motorFrontLeft.getCurrentPosition() + (int) (position * COUNTS_PER_INCH);
        } else if (right == true) {
            newBackRightTarget = motorBackRight.getCurrentPosition() + (int) (position * COUNTS_PER_INCH);
            newFrontRightTarget = motorFrontRight.getCurrentPosition() + (int) (-position * COUNTS_PER_INCH);
            newBackLeftTarget = motorBackLeft.getCurrentPosition() + (int) (position * COUNTS_PER_INCH);
            newFrontLeftTarget = motorFrontLeft.getCurrentPosition() + (int) (-position * COUNTS_PER_INCH);
        } else {
            telemetry.addData("Path", "these are the droids your looking for, go and fix the code");
        }

        motorFrontRight.setTargetPosition(newFrontRightTarget);
        motorBackRight.setTargetPosition(newBackRightTarget);
        motorFrontLeft.setTargetPosition(newFrontLeftTarget);
        motorBackLeft.setTargetPosition(newBackLeftTarget);

        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        runtime.reset();
        motorFrontRight.setPower(Math.abs(power));
        motorFrontLeft.setPower(Math.abs(power));
        motorBackRight.setPower(Math.abs(power));
        motorBackLeft.setPower(Math.abs(power));

        while (opModeIsActive() &&
                (runtime.seconds() < time) &&
                (motorBackLeft.isBusy() && motorFrontLeft.isBusy() && motorBackRight.isBusy() && motorFrontRight.isBusy())) {

            // Display it for the driver.
        }

        // Stop all motion;
        motorFrontRight.setPower(Math.abs(0));
        motorFrontLeft.setPower(Math.abs(0));
        motorBackRight.setPower(Math.abs(0));
        motorBackLeft.setPower(Math.abs(0));

        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }






    public void runPositionEncoders(double power, int position, int time)
    {
        runPositionEncoders(power, position, time, new ArrayList<>());
    }

    public void runPositionEncoders(double power, int position, int time, List<Integer> faultyEncoders)
    {
        List<DcMotor> motors = new ArrayList<>();
        motors.add(motorFrontRight);
        motors.add(motorFrontLeft);
        motors.add(motorBackRight);
        motors.add(motorBackLeft);

        String[] motorNames = {"Front Right", "Front Left", "Back Right", "Back Left"};

        // Reset and prepare encoders.
        for (int i = 0; i < motors.size(); i++)
        {
            if (!faultyEncoders.contains(i))
            {
                motors.get(i).setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
        }

        position = (int)(position * COUNTS_PER_INCH);

        // Set target position for functioning encoders.
        for (int i = 0; i < motors.size(); i++)
        {
            if (!faultyEncoders.contains(i))
            {
                motors.get(i).setTargetPosition(position);
                motors.get(i).setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
        }

        runtime.reset();

        for (int i = 0; i < motors.size(); i++)
        {
            if (!faultyEncoders.contains(i))
            {
                motors.get(i).setPower(power);
            }
            else
            {
                motors.get(i).setPower(power * 0.15);
            }
        }

        /*for (DcMotor motor : motors)   // Apply power to all motors.
        {
            motor.setPower(power);
        }*/

        // Continue until motors reach their position or time expires.
        while (opModeIsActive() && (runtime.seconds() < time) && motors.stream().anyMatch(motor -> !faultyEncoders.contains(motors.indexOf(motor)) && motor.isBusy()))
        {
            for (int i = 0; i < motors.size(); i++)   // Telemetry data for debugging.
            {
                telemetry.addData("Encoder " + motorNames[i], motors.get(i).getCurrentPosition());
            }
            telemetry.update();
        }

        for (DcMotor motor : motors)   // Stop all motion.
        {
            motor.setPower(0);
        }
    }

}


