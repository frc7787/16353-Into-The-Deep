package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.opencv.imgproc.CLAHE;

@TeleOp
public class measurment extends OpMode {

    private Servo extentionRight, extentionLeft,rotationServo, clawServo;


    public void init() {
        extentionRight = hardwareMap.get(Servo.class,"extentionRight");
        extentionLeft = hardwareMap.get(Servo.class,"extentionLeft");
        rotationServo = hardwareMap.get(Servo.class,"rotationServo");
        clawServo = hardwareMap.get(Servo.class,"clawServo");
    }

    @Override
    public void loop () {

        rotationServo.setDirection(Servo.Direction.REVERSE);
        rotationServo.setPosition(1);
        telemetry.update();



    }
}