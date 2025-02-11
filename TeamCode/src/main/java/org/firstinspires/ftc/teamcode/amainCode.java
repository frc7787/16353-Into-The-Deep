package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.Math.round;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class amainCode extends OpMode {

    private RevTouchSensor limitSwitch, clipTouchSensor, limitMaxExtension;
    private DcMotor frontLeft, frontRight, backRight, backLeft,clipMotor,extentionMotor;
    private Servo rotationServo, clawServo,twistServo, bucketServo;
   //old servos not in use anymore extentionLeft,extentionRight
   private double BUCKETHOME = 0.8;
    private double BUCKETIN = 0.2;
    private double ROTATIONPICKUP = 0.85;
    private double CLAWPICKUP = 0.46;
    private double ROTATIONNEUTRAL = 0.56;

    private double CLAWOPEN = 0.22;
    private double ROTATIONTRANSFER = 0.5;
    private double ROTATIONPRE = 0.81;
    private double TWISTTRANSFER= 0.47;
    private double TWISTPICKUP= 1;
   // private double ROTATIONPICKUP = 0.22;
   // private double ROTATIONPREPICKUP = 0.36; // was 0.3, but hitting the sub bar, then 0.33
   // private double ROTATIONNEUTRAL = 0.8;
   // private double ROTATIONTRANSFER = 0.95 ;


    //private double CLAWPICKUP = 0.32;
  //  private double CLAWOPEN = 0;
    private int  EXTENTIONPICKUP = -128;
    private int  EXTENTIONTRANSFER = 0;

    private int EXTENSIONMAX =2350;
    // january 23 2025 CLIPMOTORBUCKET was 3900

   // private int CLIPMOTORBUCKET = 4219;
    private int CLIPMOTORPREBUCKET = 3054;
    //January 23 2025 CLIPMOTORBAR WAS 1850 CHANGED TO 1900
    private int CLIPMOTORBAR = 1880;
    private int CLIPMOTORHOME = 0;
    private double CLIPMOTORPOWER = 0.5;
    private double CLIPMOTORPOWERUP = 0.8;
    private int clipManualTarget = 0;

   // private  double NEUTRALCLAW = 0;
    //private double NEUTRALROTATION = 1;
    private boolean HOMING;
    private boolean HOMINGINITCLIP;
    private boolean HOMINGINITEXTENSION;
    private boolean HOMINGCLIP;
   private ElapsedTime elapsedTime;

    private double extensionPower = 0;
    private double EXTENSIONPOWERMAX = 0.95;

    private IMU imu;

    private RevHubOrientationOnRobot controlHubOrientation = new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.LEFT
    );
    private IMU.Parameters imuParameters = new IMU.Parameters(controlHubOrientation);



    public void init() {

        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(imuParameters);
        imu.resetYaw();

        elapsedTime = new ElapsedTime();
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");

        clipMotor = hardwareMap.dcMotor.get("clipMotor");
        clipMotor.setDirection(DcMotor.Direction.REVERSE);

        clawServo = hardwareMap.get(Servo.class,"clawServo");
        rotationServo = hardwareMap.get(Servo.class,"rotationServo");
        bucketServo = hardwareMap.get(Servo.class,"bucketServo");
        twistServo = hardwareMap.get(Servo.class,"twistServo");




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
        limitMaxExtension = hardwareMap.get(RevTouchSensor.class,"limitMaxExtension");

        extentionMotor = hardwareMap.dcMotor.get("extentionMotor");
        extentionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extentionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extentionMotor.setDirection(DcMotor.Direction.REVERSE);

        HOMING = true; // set this TRUE at the beginning, so that the HOMING routine will run until clip and extension have both homed
        HOMINGINITCLIP = true; // set this TRUE at the beginning, so that the clip motor will home before anything else
        HOMINGINITEXTENSION = true; // set this TRUE at the beginning, so that the extension motor will home before anything else


        HOMINGCLIP = false; // set this FALSE at the beginning; this is the boolean for Homing as a target position

        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }
    @Override
    public void start() {
        elapsedTime.reset();

    }

    @Override
    public void loop() {
        if (HOMING) {
            rotationServo.setPosition(ROTATIONNEUTRAL);
            if (HOMINGINITEXTENSION) {
            // homing the extention, so don't do other claw stuff
                extentionMotor.setPower(-0.2);
                telemetry.addData("HOMING: limit switch IN is NOT pressed.","HOMING INIT EXTENSION true");
                if(limitSwitch.isPressed()){
                    extentionMotor.setPower(0);
                    extentionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    extentionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                    HOMINGINITEXTENSION = false;
                    telemetry.addData("HOMING: limit switch IN is pressed", "HOMING INIT EXTENSION false");
            } } // end of IF HOMINGINITEXTENSION
            if (HOMINGINITCLIP) {
                clipMotor.setPower(-0.2);
                telemetry.addData("HOMING CLIP: limit switch is NOT pressed", "HOMING INIT CLIP true");
                if (clipTouchSensor.isPressed()) {
                    clipMotor.setPower(0);
                    clipMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    clipMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                    HOMINGINITCLIP = false;
                    telemetry.addData("HOMING CLIP: limit switch IS pressed", "HOMING INIT CLIP false");
                }
            } // end of IF HOMING EXTENSION
            HOMING = (HOMINGINITEXTENSION || HOMINGINITCLIP) && (elapsedTime.seconds()<3);
        } else {  // NOT HOMING extention, so other stuff can happen
            extensionPower = -gamepad2.left_stick_y;
            if (extensionPower > EXTENSIONPOWERMAX) { // extension power from stick too big
                extensionPower = EXTENSIONPOWERMAX;
            } else if (extensionPower < -EXTENSIONPOWERMAX) { // extension power from stick too big negative
                extensionPower = -EXTENSIONPOWERMAX;
            }

            if (limitSwitch.isPressed()) {
                telemetry.addData("limit switch IN is pressed",extensionPower);
            }
            if (limitMaxExtension.isPressed()) {
                telemetry.addData("limit switch OUT is pressed",extensionPower);
            }

            int extensionPosition = extentionMotor.getCurrentPosition();
            telemetry.addData("Extension Encoder Position:", extensionPosition);

            if (((extensionPower<0) && limitSwitch.isPressed()) || extensionPosition <0) { // extension ALL the way IN, DON'T move further
                telemetry.addData("Illegal Retraction!",extensionPower);
                extentionMotor.setPower(0);
                extentionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                extentionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                // already homed, trying to retract, so no Power!
            } else if ((extensionPower>0) && (limitMaxExtension.isPressed() || extensionPosition > EXTENSIONMAX)) { // extension ALL the way OUT, DON'T move further
                telemetry.addData("Illegal Extension!", extensionPower);
                extentionMotor.setPower(0);
                // already all the way out, trying to extend, so no Power!
            }
            else {
                telemetry.addData("Extension Power:", extensionPower);
                telemetry.addData("Extension Position:", extensionPosition);
                if ((extensionPosition > (EXTENSIONMAX - 200)) || (extensionPosition < 200)) {
                    extentionMotor.setPower(extensionPower*0.5);
                } else{
                    extentionMotor.setPower(extensionPower);
                }
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
                twistServo.setPosition(TWISTPICKUP);

            }
            else if (gamepad2.dpad_down) {  // pre-pickup
                rotationServo.setPosition(ROTATIONPRE);
                twistServo.setPosition(TWISTPICKUP);

            }
            else if (gamepad2.dpad_left) {  // neutral
                rotationServo.setPosition(ROTATIONNEUTRAL);
                twistServo.setPosition(TWISTPICKUP);
            }
            else if (gamepad2.dpad_right ) { // transfer
                bucketServo.setPosition(BUCKETHOME);//TO HOME BUCKET
                rotationServo.setPosition(ROTATIONTRANSFER);
                twistServo.setPosition(TWISTTRANSFER);
            }

        } // end of not HOMING for extention

        // right hand geometric buttons for clip elevator
        if (gamepad2.triangle) { // bucket

        } else if (gamepad2.square) { // prebucket USED TO BE PREBUCKET NOW IT IS BUCKET
            clipMotor.setTargetPosition(CLIPMOTORPREBUCKET);
            clipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            clipMotor.setPower(CLIPMOTORPOWERUP);
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
        } else if (gamepad1.cross) { // clipping
            clipMotor.setTargetPosition(CLIPMOTORBAR - 550);
            clipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            clipMotor.setPower(CLIPMOTORPOWER);
            telemetry.addData("Elevator going to","clipped");
        }else if (gamepad2.left_trigger>0.9) {
            bucketServo.setPosition(BUCKETIN);
        }else if(gamepad2.right_trigger>0.9) {
            bucketServo.setPosition(BUCKETHOME);
        }else if (abs(gamepad2.right_stick_y)>0.05) {
            clipManualTarget = clipMotor.getCurrentPosition() - round(gamepad2.right_stick_y*100);
            clipMotor.setTargetPosition(clipManualTarget);
            clipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            clipMotor.setPower(CLIPMOTORPOWER);
            telemetry.addData("Elevator MANUAL control. Target Position: ",clipManualTarget);
            HOMINGCLIP = true; // a bit of a hack; if using manual, and the elevator reaches the bottom and
            // presses the limit switch, we want the clipmotor turned OFF
        } else if (gamepad1.dpad_up && gamepad1.triangle) {
            //HOMING = true;
            //HOMINGINITCLIP = true;
            //elapsedTime.reset();
        }

        int elevatorPosition = clipMotor.getCurrentPosition();
        telemetry.addData("Elevator Encoder Position:", elevatorPosition);
          if (HOMINGCLIP && clipTouchSensor.isPressed() ) {
              clipMotor.setPower(0);
              clipMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
              HOMINGCLIP = false;
          }

        // all the mecanum drive code
        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        double thetaRadians = Math.atan2(drive, strafe);
        thetaRadians -= imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double power = Math.hypot(strafe, drive);

        double sin_theta = Math.sin(thetaRadians - Math.PI / 4.0);
        double cos_theta = Math.cos(thetaRadians - Math.PI / 4.0);

        double max = Math.max(abs(cos_theta), abs(sin_theta));

        double frontLeftPower  = power * cos_theta / max + turn;
        double frontRightPower = power * sin_theta / max - turn;
        double backLeftPower   = power * sin_theta / max + turn;
        double backRightPower  = power * cos_theta / max - turn;

        double turnMagnitude = abs(turn);

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