package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class MainNeveRestOpModeRed extends LinearOpMode {

    private RevBlinkinLedDriver baseLights;
    private boolean blinkinTimer = false;

    private int blinkinDelay = 2000;
    private Servo AirPlane,ClawServo ;
    private DcMotor armLifter,armExtender;
    private DigitalChannel armLimit;
    private double [] myMaxSpeeds={1.0, 1.0,1.0,1.0};
    private double [] myDesiredSpeeds={1.0, 1.0,1.0,1.0};



    DriveBaseNeveRestSubSystem driveBase;

    @Override
    public void runOpMode() {

        armLifter = hardwareMap.get(DcMotorEx.class, "armLifter");
        armLifter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armExtender = hardwareMap.get(DcMotorEx.class, "armExtender");
        armExtender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        driveBase = new DriveBaseNeveRestSubSystem(hardwareMap,myMaxSpeeds);
        armLimit = hardwareMap.get(DigitalChannel.class, "armLimit");
        armLimit.setMode(DigitalChannel.Mode.INPUT);
        ClawServo = hardwareMap.get(Servo.class, "ClawServo");
        AirPlane = hardwareMap.get(Servo.class, "AirPlane");
        //ClawServo = hardwareMap.get(Servo.class, "ClawServo");
        baseLights = hardwareMap.get(RevBlinkinLedDriver.class, "baseLights");
        baseLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        //while(!isStarted())   {}
        waitForStart();
        //resetBlinkin();
        double minimumServoPos = 0.0;
        double maximumServoPos = 0.5;
        while(opModeIsActive())  {
            // run until the end of the match (driver presses STOP)
            double forward;
            double strafe;
            double rotation;
            double lift;
            double upperStop = 10;
            double downStop = -10;

            while (opModeIsActive()) {
                //TODO: Make robot drive
                driveBase.setMaxSpeed(myDesiredSpeeds);
                forward = -gamepad1.left_stick_y;
                strafe = gamepad1.left_stick_x;
                if (gamepad1.right_bumper) {
                    strafe = 0.5;
                    for (int i=0;i<myMaxSpeeds.length;i++)
                        myDesiredSpeeds[i]=myMaxSpeeds[i];
                } else if (gamepad1.left_bumper) {
                    strafe = -0.5;
                    for (int i=0;i<myMaxSpeeds.length;i++)
                        myDesiredSpeeds[i]=myMaxSpeeds[i];
                } else
                    strafe = 0;
                rotation = gamepad1.right_stick_x;
                //            lift = gamepad1.right_trigger - gamepad1.left_trigger;

                telemetry.addData("Status of OpMode: ", "Running");

                //            testMotor.setPower(forward);
                //            telemetry.addData("Target Power: ", forward);
                //            telemetry.addData("Motor Power: ", testMotor.getPower());
                telemetry.addData("Left Stick", gamepad1.left_stick_x);


                telemetry.addData("Front", forward);
                telemetry.addData("Strafe", strafe);
                telemetry.addData("Rotation", rotation);
                telemetry.addData("Left bumber: ", gamepad1.left_bumper);

                driveBase.drive(forward, strafe, rotation);

                // If we pressed the left bumper, lift the arm.
                if (gamepad2.left_bumper) {
                    armLifter.setPower(0.4);
                } else if (gamepad2.right_bumper) {
                    armLifter.setPower(-0.2);
                } else {
                    armLifter.setPower(0);

                }
                if (gamepad2.y) {
                    armExtender.setPower(0.6);
                } else if (gamepad2.a) {
                    //if armlimit pressed stop arm
                    if (!armLimit.getState()) {
                        armExtender.setPower(0);
                    } else {
                        armExtender.setPower(-0.6);
                    }
                } else {
                    armExtender.setPower(0);
                    //drone launcher
                }

                if (gamepad2.dpad_up) {
                    AirPlane.setPosition(0);
                }

                if (gamepad2.dpad_down) {
                    AirPlane.setPosition(1);
                }
                if (gamepad2.right_bumper) {
                    //ClawServo.setPosition(1);
                }
                if (gamepad2.left_bumper) {
                    //ClawServo.setPosition(0);
                }
                if (gamepad2.dpad_left) {
                    ClawServo.setPosition(0.3);
                }
                if (gamepad2.dpad_right) {
                    ClawServo.setPosition(0.4);
                }

                if (gamepad2.b) {
                    maximumServoPos += .1;
                }
                if (gamepad2.x) {
                    maximumServoPos -= .1;
                }
                if (gamepad1.a){
                    //telemetry.addData("Before My max speed is", myMaxSpeed);
                    //telemetry.update();
                    //sleep(3000)
                    for (int i=0;i<myMaxSpeeds.length;i++)
                        myDesiredSpeeds[i]=0.5*myMaxSpeeds[i];

                }
                if (gamepad1.b) {
                    for (int i=0;i<myMaxSpeeds.length;i++)
                        myDesiredSpeeds[i]=myMaxSpeeds[i];
                }
                telemetry.addData("is servo work", ClawServo.getPosition());

                telemetry.addData("is limit pressed", armLimit.getState());
                //telemetry.addData("My max speed is", myMaxSpeed);

                telemetry.update();
            }
        }
    }






}

