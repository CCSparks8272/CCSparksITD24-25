package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name= "EncoderTestAutonomous", group="Autonomous")



public class EncoderLiftTestAutonomous extends LinearOpMode
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
    private DcMotor motorHorzLift;
    private DcMotor motorVertLift;
    
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
        motorVertLift = hardwareMap.dcMotor.get("VERT_LIFT");
        motorHorzLift = hardwareMap.dcMotor.get("HORZ_LIFT");

        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
        motorVertLift.setDirection(DcMotor.Direction.FORWARD);
        motorHorzLift.setDirection(DcMotorSimple.Direction.FORWARD);

        //telemetry.addData("Encoder front right", motorFrontRight.getCurrentPosition());
        //telemetry.addData("Encoder front left", motorFrontLeft.getCurrentPosition());
        //telemetry.addData("Encoder back right", motorBackRight.getCurrentPosition());
        //telemetry.addData("Encoder back left", motorBackLeft.getCurrentPosition());
        telemetry.addData("Encoder Vert Lift", motorVertLift.getCurrentPosition());
        telemetry.addData("Encoder Horz Lift", motorHorzLift.getCurrentPosition());
        telemetry.update();

        setupEncoders();

        // Wait until autonomous starts
        waitForStart();

        while (opModeIsActive())
        {
            //telemetry.addData("Encoder front right", motorFrontRight.getCurrentPosition());
            //telemetry.addData("Encoder front left", motorFrontLeft.getCurrentPosition());
            //telemetry.addData("Encoder back right", motorBackRight.getCurrentPosition());
            //telemetry.addData("Encoder back left", motorBackLeft.getCurrentPosition());
            telemetry.addData("Encoder Vert Lift", motorVertLift.getCurrentPosition());
            telemetry.addData("Encoder Horz Lift", motorHorzLift.getCurrentPosition());
            telemetry.update();
            sleep(10);
        }
    }






    public void setupEncoders(){
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorVertLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorHorzLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorVertLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorHorzLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }




    public void stopMotors(int time)
    {
        motorFrontRight.setPower(0.0);
        motorBackRight.setPower(0.0);
        motorFrontLeft.setPower(0.0);
        motorBackLeft.setPower(0.0);
        motorVertLift.setPower(0.0);
        motorHorzLift.setPower(0.0);
        sleep(time);

    }




}

