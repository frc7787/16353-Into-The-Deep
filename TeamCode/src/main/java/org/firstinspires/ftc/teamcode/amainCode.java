package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class amainCode extends OpMode {


    private DcMotor frontLeft, frontRight, backRight, backLeft;
    private Servo rotationServo, clawServo,extentionLeft,extentionRight;
    private double EXTENSIONLEFTPICKUP = 0.6 ;
    private double EXTENSIONRIGHTPICKUP = 0.7 ;

    private double ROTATIONPICKUP = 0.35;
    private double CLAWPICKUP = 0.5;
    private double EXTENSIONLEFTTRANSFER = 0.7;
    private double EXTENSIONRIGHTTRANSFER = 1;
    private double CLAWOPEN = 1;
    private double ROTATIONTRANSFER = 1 ;






    public void init() {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");



        extentionRight = hardwareMap.get(Servo.class,"extentionRight");
        extentionLeft = hardwareMap.get(Servo.class,"extentionLeft");
        clawServo = hardwareMap.get(Servo.class,"clawServo");
        rotationServo = hardwareMap.get(Servo.class,"rotationServo");
        extentionLeft.setDirection( Servo.Direction.REVERSE);
        extentionRight.setDirection(Servo.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);



        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);


        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }


    @Override
    public void loop() {
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





           if(gamepad2.dpad_left){
                clawServo.setPosition(ROTATIONTRANSFER);
             } else if(gamepad2.dpad_right){
                 rotationServo.setPosition(ROTATIONPICKUP);
            }else if (gamepad2.right_bumper){
                clawServo.setPosition(CLAWPICKUP);
            }else if (gamepad2.left_bumper){
                 clawServo.setPosition(CLAWOPEN);
           }else if (gamepad2.dpad_down) {
               extentionRight.setPosition(EXTENSIONRIGHTTRANSFER);
                extentionLeft.setPosition(EXTENSIONLEFTTRANSFER);
            }else if (gamepad2.dpad_up){
               extentionLeft.setPosition(EXTENSIONLEFTPICKUP);
                extentionRight.setPosition(EXTENSIONRIGHTPICKUP);
             }


            telemetry.update();


    }
}




/*



 */