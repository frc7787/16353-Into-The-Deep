package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous

public class Red2 extends LinearOpMode{
    private DcMotor armLifter,armExtender;
    private Servo ClawServo ;
    private DigitalChannel armLimit;

    DriveBaseSubSystem driveBase;
    double autoPower = 0.6;

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


        telemetry.addData("Status", "Initialized");
        telemetry.update();






        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        driveBase.drive(autoPower, 0, 0);
        sleep(1500);
        driveBase.drive(0, 0, -autoPower);
        sleep(1000);
        driveBase.drive(autoPower, 0, 0);
        sleep(1200);
        driveBase.drive(0, autoPower, 0);
        sleep(900);
        driveBase.drive(0, autoPower, 0);
        sleep(800);
        armExtender.setPower(0.6);
        sleep(1000);
        ClawServo.setPosition(5);
        sleep(100);
        armExtender.setPower(-0.3);
        sleep(1000);
        driveBase.drive(0, autoPower, 0);
        sleep(800);
        driveBase.drive(autoPower, 0, 0);
        sleep(700);
        driveBase.stop();
        int width = 12;
        int length = 12;
        float[][] state_positions = new float[width][length];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < length; j++)
                state_positions[i][j] = 0;

        if (!armLimit.getState()) {
            armExtender.setPower(0);
        }


    }
}