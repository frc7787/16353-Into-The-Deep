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
public class measurment extends LinearOpMode {

    //private RevBlinkinLedDriver baseLights;
    //private boolean blinkinTimer = false;

    // private Servo wristServo, ClawServo;
    private DcMotor armLifter, armExtender;
    // private DigitalChannel armExtenderLimit;
    private DcMotor frontLeft, frontRight, backRight, backLeft;

    public void setup() {
    }

    @Override
    public void runOpMode() {

        armLifter = hardwareMap.get(DcMotorEx.class, "armLifter");
        armLifter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        armExtender = hardwareMap.get(DcMotorEx.class, "armExtender");
        armExtender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        armExtender.setDirection(DcMotorSimple.Direction.REVERSE);
        // armExtenderLimit = hardwareMap.get(DigitalChannel.class, "armLimit");
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("armExtender", armExtender.getCurrentPosition());
            telemetry.addData("armLifter", armLifter.getCurrentPosition());

            telemetry.update();
        }

    }
}