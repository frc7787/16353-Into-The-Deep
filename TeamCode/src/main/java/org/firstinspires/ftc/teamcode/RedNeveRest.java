package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
@Autonomous
public class RedNeveRest extends LinearOpMode {

    private RevBlinkinLedDriver baseLights;

    private DcMotor armLifter,armExtender;
    private Servo ClawServo ;
    private DigitalChannel armLimit;
    private int newLeftTarget;
    private int newRightTarget;
    private int newBackLeftTarget;
    private int newBackRightTarget;

    private ElapsedTime runtime = new ElapsedTime();
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder

    static final double     DRIVE_GEAR_REDUCTION    = 0.26 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double ROBOT_LENGTH_INCH=0.0;//15.0; //15.0;
    static final double     COUNTS_PER_NEVEREST_REV    = 1680 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_NEVEREST_REDUCTION    = 0.26 ;     // No External Gearing.
    static final double     WHEEL_NEVEREST_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_NEVEREST_PER_INCH         = (COUNTS_PER_NEVEREST_REV * DRIVE_GEAR_NEVEREST_REDUCTION) /
            (WHEEL_NEVEREST_DIAMETER_INCHES * 3.1415);

    DriveBaseSubSystem driveBase;
    double autoPower = 0.6;
    double timeoutS=600;
    double leftInches=30.0;
    double rightInches=30.0;
    double [] speed={0.25, 1.0,.25,0.25 };
    //0: front left, 1: front right
    //2: back left, 3: back right
    static final double ALL_COUNTS[] = {COUNTS_PER_INCH,COUNTS_NEVEREST_PER_INCH,COUNTS_PER_INCH,COUNTS_PER_INCH};
    // direction 1: forward,
    //           2: backwrd;
    //           3: right
    //           4: left
    private void move(DcMotor leftDrive,DcMotor rightDrive, DcMotor backLeftDrive, DcMotor backRightDrive,
                      int direction, double leftInches, double rightInches,double backLeftInches, double backRightInches) {
        switch (direction){
            case 1:  //FORWARD
                leftDrive.setDirection(DcMotor.Direction.FORWARD);
                rightDrive.setDirection(DcMotor.Direction.REVERSE);
                backRightDrive.setDirection(DcMotor.Direction.REVERSE);
                backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
                break;

            case 2: //BACKWARD
                backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
                backRightDrive.setDirection(DcMotor.Direction.FORWARD);
                leftDrive.setDirection(DcMotor.Direction.REVERSE);
                rightDrive.setDirection(DcMotor.Direction.FORWARD);
                break;
            case 3:  //RIGHT
                rightDrive.setDirection(DcMotor.Direction.FORWARD);
                backRightDrive.setDirection(DcMotor.Direction.REVERSE);
                leftDrive.setDirection(DcMotor.Direction.FORWARD);
                backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
                break;
            case 4:  //LEFT
                leftDrive.setDirection(DcMotor.Direction.REVERSE);
                backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
                backRightDrive.setDirection(DcMotor.Direction.FORWARD);
                rightDrive.setDirection(DcMotor.Direction.REVERSE);
                break;
            default:  //FORWARD
                leftDrive.setDirection(DcMotor.Direction.FORWARD);
                rightDrive.setDirection(DcMotor.Direction.REVERSE);
                backRightDrive.setDirection(DcMotor.Direction.REVERSE);
                backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
                break;

        }


        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //driveBase.getFrontLeft();
        //Motor rightDrive=driveBase.getFrontRight();

        newLeftTarget = leftDrive.getCurrentPosition() + (int)(leftInches * ALL_COUNTS[0]);
        newRightTarget = rightDrive.getCurrentPosition() + (int)(rightInches * ALL_COUNTS[1]);
        leftDrive.setTargetPosition(newLeftTarget);
        rightDrive.setTargetPosition(newRightTarget);
        newBackLeftTarget = backLeftDrive.getCurrentPosition() + (int)((backLeftInches-ROBOT_LENGTH_INCH) * ALL_COUNTS[2]);
        newBackRightTarget = backRightDrive.getCurrentPosition() + (int)((backRightInches-ROBOT_LENGTH_INCH) * ALL_COUNTS[3]);
        backLeftDrive.setTargetPosition(newBackLeftTarget);
        backRightDrive.setTargetPosition(newBackRightTarget);
        // Turn On RUN_TO_POSITION
        ((DcMotor)leftDrive).setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ((DcMotor)rightDrive).setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ((DcMotor)backLeftDrive).setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ((DcMotor)backRightDrive).setMode(DcMotor.RunMode.RUN_TO_POSITION);


        // reset the timeout time and start motion.
        runtime.reset();
        ((DcMotor)leftDrive).setPower(Math.abs(speed[0]));
        ((DcMotor)rightDrive).setPower(Math.abs(speed[1]));
        ((DcMotor)backLeftDrive).setPower(Math.abs(speed[2]));
        ((DcMotor)backRightDrive).setPower(Math.abs(speed[3]));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        while (opModeIsActive() &&
                //(runtime.seconds() < timeoutS) &&
                (((DcMotor)leftDrive).isBusy() && ((DcMotor)rightDrive).isBusy()) &&
                (((DcMotor)backLeftDrive).isBusy() && ((DcMotor)backRightDrive).isBusy())) {

            // Display it for the driver.
            telemetry.addData("Running to",  " %7d :%7d", newLeftTarget,  newRightTarget);
            telemetry.addData("Currently at",  " at %7d :%7d; %7d:%7d",
                    leftDrive.getCurrentPosition(), rightDrive.getCurrentPosition(),
                    backLeftDrive.getCurrentPosition(), backRightDrive.getCurrentPosition());
            telemetry.update();
        }

        // Stop all motion;
        ((DcMotor)leftDrive).setPower(0);
        ((DcMotor)rightDrive).setPower(0);
        ((DcMotor)backLeftDrive).setPower(0);
        ((DcMotor)backRightDrive).setPower(0);

        // Turn off RUN_TO_POSITION
        ((DcMotor)leftDrive).setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ((DcMotor)rightDrive).setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ((DcMotor)backLeftDrive).setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ((DcMotor)backRightDrive).setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        sleep(250);   // optional pause after each move.


    }
    @Override
    public void runOpMode() {

        driveBase = new DriveBaseSubSystem(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        ClawServo = hardwareMap.get(Servo.class, "ClawServo");
        armLimit = hardwareMap.get(DigitalChannel.class, "armLimit");
        armLimit.setMode(DigitalChannel.Mode.INPUT);

        armLifter = hardwareMap.get(DcMotorEx.class, "armLifter");
        armLifter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armExtender = hardwareMap.get(DcMotorEx.class, "armExtender");
        armExtender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Wait for the game to start (driver presses PLAY)

        waitForStart();

        DcMotor leftDrive= hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor rightDrive = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backLeftDrive = hardwareMap.get(DcMotor.class,"backLeft");
        DcMotor backRightDrive= hardwareMap.get(DcMotor.class,"backRight");
        move(leftDrive,rightDrive,backLeftDrive,backRightDrive,1, leftInches,rightInches,leftInches,rightInches);
        move(leftDrive,rightDrive,backLeftDrive,backRightDrive,2, leftInches,rightInches,leftInches,rightInches);
        move(leftDrive,rightDrive,backLeftDrive,backRightDrive,3, leftInches+15,rightInches+15,leftInches+15,rightInches+15);
        baseLights = hardwareMap.get(RevBlinkinLedDriver.class, "baseLights");
        baseLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);





        /*
        int width = 12;
        int length = 12;
        float[][] state_positions= new float[width][length];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < length; j++)
                state_positions[i][j]=0;
        if (!armLimit.getState()) {
            armExtender.setPower(0);
        }
*/









        //TODO: Make robot drive
        //frontLeft.setPower(1)
        //frontRight.setPower(-1)
        //backLeft.setPower(1)
        //backRight.setPower(-1)
        //sleep(1000)
//            frontLeft.setPower(-1);
//            frontRight.setPower(-1);
//            backLeft.setPower(-1);
//            backRight.setPower(-1);
//            sleep(2000);
//            frontLeft.setPower(1);
//            frontRight.setPower(-1);
//            backLeft.setPower(1);
//            backRight.setPower(-1);
//            sleep(2000);
//            frontLeft.setPower(0);
//            frontRight.setPower(0);
//            backLeft.setPower(0);
//            backRight.setPower(0);
//            sleep(10000000);


    }
}

