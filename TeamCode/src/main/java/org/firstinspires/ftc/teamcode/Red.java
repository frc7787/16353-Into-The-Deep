package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous

public class Red extends LinearOpMode{
    private RevBlinkinLedDriver baseLights;
    private boolean blinkinTimer = false;
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
        baseLights = hardwareMap.get(RevBlinkinLedDriver.class, "baseLights");
        baseLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);


        telemetry.addData("Status", "Initialized");
        telemetry.update();






        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        driveBase.drive(autoPower, 0, 0);
        sleep(1000);
        driveBase.drive(0, 0, autoPower);
        sleep(290);
        driveBase.drive(0.5, 0, 0);
        sleep(50);
        armLifter.setPower(0.6);
        sleep(300);
        armLifter.setPower(0);
        armExtender.setPower(0.6);
        sleep(1000);
        armExtender.setPower(0);
        ClawServo.setPosition(5);
        sleep(100);
        armExtender.setPower(-0.3);
        sleep(1000);
        driveBase.drive(0, autoPower, 0);
        sleep(1000);
        driveBase.drive(autoPower, 0, 0);
        sleep(730);
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