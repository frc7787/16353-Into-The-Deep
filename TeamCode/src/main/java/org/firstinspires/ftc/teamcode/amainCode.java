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

    private RevTouchSensor limitSwitch, clipTouchSensor;
    private DcMotor frontLeft, frontRight, backRight, backLeft,clipMotor,extentionMotor;
    private Servo rotationServo, clawServo,extentionLeft,extentionRight;

    private double ROTATIONPICKUP = 0.22;
    private double ROTATIONPREPICKUP = 0.3;
    private double ROTATIONNEUTRAL = 0.8;
    private double ROTATIONTRANSFER = 0.95 ;


    private double CLAWPICKUP = 0.19;
    private double CLAWOPEN = 0;
    private int  EXTENTIONPICKUP = -128;
    private int  EXTENTIONTRANSFER = 0;

    private int CLIPMOTORBUCKET = 3900;
    private int CLIPMOTORPREBUCKET = 3300;
    private int CLIPMOTORBAR = 1850;
    private int CLIPMOTORHOME = 0;
    private double CLIPMOTORPOWER = 0.5;

   // private  double NEUTRALCLAW = 0;
    //private double NEUTRALROTATION = 1;
    private boolean HOMING;
    private boolean HOMINGCLIP;

    private double extensionPower = 0;
    private double EXTENSIONPOWERMAX = 0.3;


    public void init() {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");

        clipMotor = hardwareMap.dcMotor.get("clipMotor");
        clipMotor.setDirection(DcMotor.Direction.REVERSE);

        clawServo = hardwareMap.get(Servo.class,"clawServo");
        //clawServo.setDirection(Servo.Direction.REVERSE);
        rotationServo = hardwareMap.get(Servo.class,"rotationServo");
        //extentionLeft.setDirection( Servo.Direction.REVERSE);
        //extentionRight.setDirection(Servo.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        clipMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        clipMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        clipMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);

        clipTouchSensor = hardwareMap.get(RevTouchSensor.class,"clipTouchSensor");
        limitSwitch = hardwareMap.get(RevTouchSensor.class,"limitSwitch");
        extentionMotor = hardwareMap.dcMotor.get("extentionMotor");
        extentionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extentionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extentionMotor.setDirection(DcMotor.Direction.REVERSE);

        HOMING = true;

        HOMINGCLIP = false;
        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }
    @Override
    public void start() {
        // initialise servo positions

    }

    @Override
    public void loop() {
        if(HOMING){ // homing the extention, so don't do other claw stuff
            extentionMotor.setPower(-0.2);
            if(limitSwitch.isPressed()){
                extentionMotor.setPower(0);
                extentionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                extentionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                HOMING = false;
            }
        }else{  // NOT HOMING extention, so other stuff can happen
            extensionPower = -gamepad2.left_stick_y;
            if (extensionPower > EXTENSIONPOWERMAX) { // extension power from stick too big
                extensionPower = EXTENSIONPOWERMAX;
            } else if (extensionPower < -EXTENSIONPOWERMAX) { // extension power from stick too big negative
                extensionPower = -EXTENSIONPOWERMAX;
            }
            if ((extensionPower<0) && limitSwitch.isPressed()) { // extension ALL the way in, DON'T move further
                telemetry.addData("Illegal Retraction!",extensionPower);
                extentionMotor.setPower(0);
                // already homed, trying to retract, so no Power!
            } else {
                telemetry.addData("Extension Power:", extensionPower);
                extentionMotor.setPower(extensionPower);
            }

            // bumpers for sample PICKUP
            if (gamepad2.right_bumper){
                clawServo.setPosition(CLAWPICKUP);
            } else if (gamepad2.left_bumper) {
                clawServo.setPosition(CLAWOPEN);
            }

            // dpad for claw ROTATION
            if (gamepad2.dpad_up) { // pickup
                rotationServo.setPosition(ROTATIONPICKUP);
            }
            else if (gamepad2.dpad_down) {  // pre-pickup
                rotationServo.setPosition(ROTATIONPREPICKUP);
            }
            else if (gamepad2.dpad_left) {  // neutral
                rotationServo.setPosition(ROTATIONNEUTRAL);
            }
            else if (gamepad2.dpad_right) { // transfer
                rotationServo.setPosition(ROTATIONTRANSFER);
            }

        } // end of not HOMING for extention

        // right hand geometric buttons for clip elevator
        if (gamepad2.triangle) { // bucket
            clipMotor.setTargetPosition(CLIPMOTORBUCKET);
            clipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            clipMotor.setPower(CLIPMOTORPOWER);
            telemetry.addData("Elevator going to","bucket");
        } else if (gamepad2.square) { // prebucket
            clipMotor.setTargetPosition(CLIPMOTORPREBUCKET);
            clipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            clipMotor.setPower(CLIPMOTORPOWER);
            telemetry.addData("Elevator going to","prebucket");
        } else if (gamepad2.circle) { // bar
            clipMotor.setTargetPosition(CLIPMOTORBAR);
            clipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            clipMotor.setPower(CLIPMOTORPOWER);
            telemetry.addData("Elevator going to","bar");
        } else if (gamepad2.cross) { // home
            clipMotor.setTargetPosition(CLIPMOTORHOME);
            clipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            clipMotor.setPower(CLIPMOTORPOWER);
           HOMINGCLIP = true;
            telemetry.addData("Elevator going to","home");
        }
        int elevatorPosition = clipMotor.getCurrentPosition();
        telemetry.addData("Elevator Encoder Position:", elevatorPosition);
          if (HOMINGCLIP && clipTouchSensor.isPressed() ) {
              clipMotor.setPower(0);
              HOMINGCLIP = false;
          }

        // all the mecanum drive code
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