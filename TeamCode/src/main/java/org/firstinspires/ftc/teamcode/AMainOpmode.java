
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class AMainOpmode extends OpMode {

    //private RevBlinkinLedDriver baseLights;
    //private boolean blinkinTimer = false;
    private RevTouchSensor rotationLimitSwitch, extensionLimitSwitch;
   // private Servo wristServo, ClawServo;
    private DcMotor armLifter, armExtender;
   // private DigitalChannel armExtenderLimit;
    private DcMotor frontLeft, frontRight, backRight, backLeft;
    private Servo clawServo, rotationServo;
    private double CLAWOPENPOISITION = 0.23;

    private double CLAWCLOSEDPOISITION = 0.13;
    private double CLAWDROPPOISITION = 0.0;
    private double CLAWGRABPOISITION = 0.63;
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        rotationLimitSwitch = hardwareMap.get(RevTouchSensor.class,"rotationLimitSwitch");
        extensionLimitSwitch = hardwareMap.get(RevTouchSensor.class,"extensionLimitSwitch");

        clawServo = hardwareMap.get(Servo.class,"clawServo");
        rotationServo = hardwareMap.get(Servo.class,"rotationServo");

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
        armLifter.setDirection(DcMotor.Direction.FORWARD);

        armExtender = hardwareMap.get(DcMotorEx.class, "armExtender");
        armExtender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armExtender.setDirection(DcMotor.Direction.FORWARD);
        armExtender.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armExtender.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armLifter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armLifter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }


    @Override
    public void loop() {




        // Wait for the game to start (driver presses PLAY)

        // run until the end of the match (driver presses STOP)
        double speed;
        double strafe;
        double turn;
        double Deadzone = 0.1;
       //     double power = gamepad2.right_stick_y;
      //  double extender = gamepad2.left_stick_y;

        double downStop = -10;


           /* // If we pressed the left bumper, lift the arm.

            if (Math.abs(gamepad2.right_stick_y) < Deadzone) {
                power = 0;

            } else {
                power= gamepad2.right_stick_y;
            }
            if (Math.abs(extender) < Deadzone) {
                armExtender.setPower(0);
            }else if(Math.abs(extender) > -Deadzone){
                armExtender.setPower(0);
            }*/
          /*  if(homingLimit){
                armExtender.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                armLifter.setMode(DcMotor);
            }
            */

                if(rotationLimitSwitch.isPressed()){
                    telemetry.addData("rot limit switch pressed", rotationLimitSwitch.isPressed());
                }else{
                    telemetry.addData("rot not pressed","not pressed");
                }
                if(extensionLimitSwitch.isPressed()){
                    telemetry.addData("ext limit switch pressed", extensionLimitSwitch.isPressed());

                }else{
                    telemetry.addData("ext not pressed","ext not pressed");
                }



                if(gamepad2.dpad_up){
                armExtender.setTargetPosition(-2000);
                armLifter.setTargetPosition(4500);
                armExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armLifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armExtender.setPower(0.9);
                armLifter.setPower(0.9);
                } else if(gamepad2.dpad_right){
                armExtender.setTargetPosition(-650);
                armLifter.setTargetPosition(5940);
                armExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armLifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armExtender.setPower(0.9);
                armLifter.setPower(0.9);
                rotationServo.setPosition(CLAWGRABPOISITION);
                } else if(gamepad2.dpad_down){
                armExtender.setTargetPosition(-700);
                armLifter.setTargetPosition(6885);
                armExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armLifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armExtender.setPower(0.9);
                armLifter.setPower(0.9);
                rotationServo.setPosition(CLAWGRABPOISITION);
                } else if(gamepad2.triangle){
                armExtender.setTargetPosition(-3700);
                armLifter.setTargetPosition(3150);
                armExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armLifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armExtender.setPower(0.9);
                armLifter.setPower(0.9);
                rotationServo.setPosition(CLAWDROPPOISITION);
                } else if(gamepad2.cross){
                armExtender.setTargetPosition(-2029);
                armLifter.setTargetPosition(4457);
                armExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armLifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armExtender.setPower(0.9);
                armLifter.setPower(0.9);
                } else if(gamepad2.right_trigger > 0.7){
                clawServo.setPosition(CLAWOPENPOISITION);
                } else if(gamepad2.right_trigger <= 0.7){
                clawServo.setPosition(CLAWCLOSEDPOISITION);
                } else if(gamepad2.dpad_left){
                    /*
                    armExtender.setTargetPosition(10);
                    armLifter.setTargetPosition(10);
                    armLifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    armExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rotationServo.setPosition(CLAWGRABPOISITION);
                    armExtender.setPower(0.5);
                    armLifter.setPower(0.5);
                     */
                    armExtender.setTargetPosition(-2029);
                    armLifter.setTargetPosition(4457);
                    armExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    armLifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    armExtender.setPower(0.9);
                    armLifter.setPower(0.9);
                }

//

            speed = -gamepad1.left_stick_y;
            strafe = gamepad1.left_stick_x;

            turn = gamepad1.right_stick_x;
            //            lift = gamepad1.right_trigger - gamepad1.left_trigger;

            frontLeft.setPower(speed + turn + strafe);
            frontRight.setPower(speed - turn - strafe);
            backLeft.setPower(speed + turn - strafe);
            backRight.setPower(speed - turn + strafe);


          // armLifter.setPower(gamepad2.right_stick_y);
          // armExtender.setPower(gamepad2.left_stick_y);
            telemetry.addData("Extension power",armExtender.getPower());
            telemetry.addData("extension mode",armExtender.getMode());
            telemetry.addData("extension target",armExtender.getTargetPosition());
            telemetry.addData("extension current",armExtender.getCurrentPosition());
            telemetry.addData("lifter target",armLifter.getTargetPosition());
            telemetry.addData("lifter current",armLifter.getCurrentPosition());
            telemetry.addData("lifter power",armLifter.getPower());
            telemetry.addData("extension mode",armLifter.getMode());
            telemetry.addData("lifter target",armLifter.getTargetPosition());
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




    /*
    


     */

