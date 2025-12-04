package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@TeleOp

public class mecanumDrive extends LinearOpMode {



    private DcMotor frontLeft,frontRight,backRight,backLeft;

    public void setup() {
        frontLeft =  hardwareMap.dcMotor.get( "frontLeft");
        frontRight = hardwareMap.dcMotor.get( "frontRight");
        backRight = hardwareMap.dcMotor.get( "backRight");
        backLeft = hardwareMap.dcMotor.get( "backLeft");

        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);




    }

    @Override
    public void runOpMode() {

        setup();

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        double minimumServoPos = 0.0;
        double maximumServoPos = 0.5;
        while (opModeIsActive()) {
            // run until the end of the match (driver presses STOP)
            double speed;
            double strafe;
            double turn;
            double lift;
            double upperStop = 10;
            double downStop = -10;

            while (opModeIsActive()) {
                //TODO: Make robot drive
                // driveBase.setMaxSpeed(myMaxSpeed);




                speed = -gamepad1.left_stick_y;
                strafe = gamepad1.left_stick_x;

                turn = gamepad1.right_stick_x;

                frontLeft.setPower(speed+turn+strafe);
                frontRight.setPower(speed-turn-strafe);
                backLeft.setPower (speed+turn-strafe);
                backRight.setPower(speed-turn+strafe);


                telemetry.addData("Status of OpMode: ", "Running");
                telemetry.addData("Front", speed);
                telemetry.addData("Strafe", strafe);
                telemetry.addData("Rotation", turn);




            }


        }
    }
}
