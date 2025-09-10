import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.odometry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp(name = "Three Wheel Odo Test")
public  class ThreeWheelOdoTest extends LinearOpMode {
    private odometry odometry;
        public void runOpMode() {

            odometry = new odometry();

            waitForStart();
            while (opModeIsActive()){
                odometry.update();
                telemetry.addData("X (in)", odometry.getX());
                telemetry.addData("Y (in)", odometry.getY());
                telemetry.addData("Heading (rad)", odometry.getHeading());
                telemetry.update();
            }
        }
    }
