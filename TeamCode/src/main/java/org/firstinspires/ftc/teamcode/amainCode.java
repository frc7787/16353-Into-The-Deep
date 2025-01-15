package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class amainCode extends OpMode {

    private RevTouchSensor limitSwitch;
    private DcMotor frontLeft, frontRight, backRight, backLeft,clipMotor,extentionMotor;
    private Servo rotationServo, clawServo,extentionLeft,extentionRight;

    private double ROTATIONPICKUP = 0.35;
    private double CLAWPICKUP = 0.18;
   private int  EXTENTIONPICKUP = -128;
    private int  EXTENTIONTRANSFER = 0;

    private double CLAWOPEN = 0;
    private double ROTATIONTRANSFER = 1 ;

    private boolean HOMING;







    public void init() {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");

        clipMotor = hardwareMap.dcMotor.get("clipMotor");




        clawServo = hardwareMap.get(Servo.class,"clawServo");
        rotationServo = hardwareMap.get(Servo.class,"rotationServo");
        //extentionLeft.setDirection( Servo.Direction.REVERSE);
        //extentionRight.setDirection(Servo.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        clipMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        clipMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        clipMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);

        limitSwitch = hardwareMap.get(RevTouchSensor.class,"limitSwitch");
        extentionMotor = hardwareMap.dcMotor.get("extentionMotor");
        extentionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extentionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extentionMotor.setDirection(DcMotor.Direction.REVERSE);

        HOMING = true;

        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }
    @Override
    public void start() {
        // initialise servo positions

    }

    @Override
    public void loop() {
        if(HOMING){
            extentionMotor.setPower(-0.5);
            if(limitSwitch.isPressed()){
                extentionMotor.setPower(0);
                extentionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                extentionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                HOMING = false;
            }
        }else{
            extentionMotor.setPower(-gamepad2.left_stick_y);
            if (gamepad2.right_bumper){
                clawServo.setPosition(CLAWPICKUP);
            }else if (gamepad2.left_bumper){
                clawServo.setPosition(CLAWOPEN);

            }






        }









        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        double thetaRadians = Math.atan2(drive, strafe);

        double power = Math.hypot(strafe, drive);

        double sin_theta = Math.sin(thetaRadians - Math.PI / 4.0);
        double cos_theta = Math.cos(thetaRadians - Math.PI / 4.0);

        double max = Math.max(Math.abs(cos_theta), Math.abs(sin_theta));

        double frontLeftPower  = power * cos_theta / max + turn;
        double frontRightPower = power * sin_theta / max - turn;
        double backLeftPower   = power * sin_theta / max + turn;
        double backRightPower  = power * cos_theta / max - turn;

        double turnMagnitude = Math.abs(turn);

        if ((power + turnMagnitude) > 1.0) {
            frontLeftPower  /= power + turnMagnitude;
            frontRightPower /= power + turnMagnitude;
            backLeftPower   /= power + turnMagnitude;
            backRightPower  /= power + turnMagnitude;
        }

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);

        telemetry.update();


    }
}




/*


 */