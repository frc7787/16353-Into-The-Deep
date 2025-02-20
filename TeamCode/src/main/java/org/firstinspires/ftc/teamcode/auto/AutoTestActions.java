package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;


import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.actions.ElevatorAction;
import static org.firstinspires.ftc.teamcode.Constants.*;

@Autonomous
public class AutoTestActions extends  LinearOpMode{

    private Servo rotationServo, clawServo,twistServo, bucketServo, hockeyStickServo;


    @Override public void runOpMode() {
        ElapsedTime elapsedTime = new ElapsedTime();
        rotationServo = hardwareMap.get(Servo.class, ROTATION_SERVO_NAME);
        hockeyStickServo = hardwareMap.get(Servo.class, HOCKEYSTICK_SERVO_NAME);
        twistServo = hardwareMap.get(Servo.class, TWIST_SERVO_NAME);
        clawServo = hardwareMap.get(Servo.class, CLAW_SERVO_NAME);
        bucketServo = hardwareMap.get(Servo.class, BUCKET_SERVO_NAME);

        rotationServo.setPosition(ROTATION_NEUTRAL);
        hockeyStickServo.setPosition(HOCKEYSTICK_INITIAL);
        twistServo.setPosition(TWIST_INITIAL);
        clawServo.setPosition(CLAW_OPEN);
        bucketServo.setPosition(BUCKET_HOME);

        ElevatorAction elevator = new ElevatorAction(hardwareMap);

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("Not started yet", "waiting");
            telemetry.update();
        }

        waitForStart();
        elapsedTime.reset();

        Actions.runBlocking(
                new SequentialAction(
                        elevator.PickupBlock(),
                        elevator.PreTransferBlock(),
                        elevator.TransferBlock(),
                        elevator.BucketPosition(),
                        elevator.DumpBucket(),
                        elevator.ClipHome()
        )

        );

        sleep(10000);


    } // end of public void runOpMode
}
