
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class AMainOpmode extends LinearOpMode {

    //private RevBlinkinLedDriver baseLights;
    //private boolean blinkinTimer = false;

   // private Servo wristServo, ClawServo;
    private DcMotor armLifter, armExtender;
   // private DigitalChannel armExtenderLimit;
    private DcMotor frontLeft, frontRight, backRight, backLeft;

    public void setup() {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");

        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);
        armLifter = hardwareMap.get(DcMotorEx.class, "armLifter");
        armLifter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armLifter.setDirection(DcMotorSimple.Direction.REVERSE);

        armExtender = hardwareMap.get(DcMotorEx.class, "armExtender");
        armExtender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armExtender.setDirection(DcMotorSimple.Direction.REVERSE);
        armExtender.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armExtender.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armLifter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armLifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    @Override
    public void runOpMode() {

       // armExtenderLimit = hardwareMap.get(DigitalChannel.class, "armLimit");
        //armExtenderLimit.setMode(DigitalChannel.Mode.INPUT);
        //wristServo = hardwareMap.get(Servo.class, "AirPlane");
       // ClawServo = hardwareMap.get(Servo.class, "ClawServo");
       // baseLights = hardwareMap.get(RevBlinkinLedDriver.class, "baseLights");
       // baseLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);

        armExtender.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armExtender.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armLifter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armLifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        setup();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        double speed;
        double strafe;
        double turn;
        double Deadzone = 0.1;
        double power = gamepad2.right_stick_y;
        double extender = gamepad2.left_stick_y;
        double downStop = -10;

        while (opModeIsActive()) {

            // If we pressed the left bumper, lift the arm.

            if (Math.abs(gamepad2.right_stick_y) < Deadzone) {
                power = 0;

            } else {
                power= gamepad2.right_stick_y;
            }
            if (Math.abs(extender) < Deadzone) {
                armExtender.setPower(0);
            }else if(Math.abs(extender) > -Deadzone){
                armExtender.setPower(0);
            }
           /* if(gamepad2.dpad_up){
                armExtender.setTargetPosition(1646);
                armLifter.setTargetPosition(250);
                armExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armLifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armLifter.setPower(0.8);
                armExtender.setPower(0.8);
            }
//           */

            speed = -gamepad1.left_stick_y;
            strafe = gamepad1.left_stick_x;

            turn = gamepad1.right_stick_x;
            //            lift = gamepad1.right_trigger - gamepad1.left_trigger;

            frontLeft.setPower(speed + turn + strafe);
            frontRight.setPower(speed - turn - strafe);
            backLeft.setPower(speed + turn - strafe);
            backRight.setPower(speed - turn + strafe);


           armLifter.setPower(gamepad2.right_stick_y);
           armExtender.setP(gamepad2.left_stick_y);
            telemetry.addData("Extension power",armExtender.getPower());
            telemetry.addData("extension mode",armExtender.getMode());
            telemetry.addData("extension target",armExtender.getTargetPosition());
            telemetry.addData("extension target",armExtender.getCurrentPosition());

            telemetry.addData("lifter power",armLifter.getCurrentPosition());
            telemetry.addData("lifter power",armLifter.getPower());
            telemetry.addData("extension mode",armLifter.getMode());
            telemetry.addData("extension target",armLifter.getTargetPosition());
            telemetry.update();

/*
            }
            if (gamepad1.y) {
                armExtender.setPower(0.6);
            } else if (gamepad1.a) {
                //if armlimit pressed stop arm
                if (!armExtenderLimit.getState()) {
                    armExtender.setPower(0);
                } else {
                    armExtender.setPower(-0.6);
                }
            } else {
                armExtender.setPower(0);
            }

            if (gamepad2.dpad_up) {
                wristServo.setPosition(0);
            }

            if (gamepad2.dpad_down) {
                wristServo.setPosition(1);
            }
            if (gamepad2.right_bumper) {
                ClawServo.setPosition(1);
            }
            if (gamepad2.left_bumper) {
                ClawServo.setPosition(0);
            }
            telemetry.addData("is servo work", wristServo.getPosition());

            telemetry.addData("is limit pressed", armExtenderLimit.getState());
            telemetry.update();
*/
        }
    }

}


    /*
    


     */

