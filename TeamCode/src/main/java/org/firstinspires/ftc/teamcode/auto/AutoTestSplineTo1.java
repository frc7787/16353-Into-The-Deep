package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Disabled
@Autonomous
public class AutoTestSplineTo1 extends LinearOpMode {
    private final Pose2d initialPose = new Pose2d(7.5, -61, -Math.PI / 2);

    private double ROTATIONNEUTRAL = 0.8;
    private Servo rotationServo;


    @Override public void runOpMode() {

        ElapsedTime elapsedTime = new ElapsedTime();
        rotationServo = hardwareMap.get(Servo .class,"rotationServo");
        rotationServo.setPosition(ROTATIONNEUTRAL);

        //MecanumDrive drive = new MecanumDrive.Builder(hardwareMap)
        //.setPose(initialPose)
        //.build();

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder firstBuilder = drive.actionBuilder(initialPose)
                //.lineToY(-50)
                .splineToConstantHeading(new Vector2d(0, -31), -Math.PI/2)
                .waitSeconds(2)
                .splineTo(new Vector2d(20,-40),0)
                //.waitSeconds(10)
                .splineTo(new Vector2d(35,-24),Math.PI/2)
                .splineToConstantHeading(new Vector2d(48,-12),Math.PI/2)
                .setTangent(-Math.PI/2)
                .lineToY(-57)   // south to push sample into zone
                .setTangent(-Math.PI/2)
                .lineToY(-50)   // north, backup out of zone
                // new position of turn
                //.turn(Math.PI)      // spin around for gripper to face wall
                .waitSeconds(1)
                .setTangent(Math.PI/2)
                .lineToY(-60)   // south to intermediate point, human player lines up specimen (was -62)
                .waitSeconds(1)
                .setTangent(Math.PI/2)
                .lineToY(-64,null,new ProfileAccelConstraint(-70.0,70.0))
                .setTangent(0)
                //.splineToConstantHeading(new Vector2d(24, -48),Math.PI);
                .splineToLinearHeading(new Pose2d(0,-48,Math.PI),0)
                .turnTo(-Math.PI/2)
                .setTangent(-Math.PI/2)
                .lineToY(-31);
                //.splineTo(new Vector2d(0,-31),Math.PI/2);
                //.waitSeconds(3)
                //.setTangent(0)
                //.lineToX(48)
                //.waitSeconds(3)
                //.setTangent(-Math.PI/2)
                //.lineToY(0);

        TrajectoryActionBuilder secondBuilder = firstBuilder.endTrajectory().fresh()
                .waitSeconds(5)
                .setTangent(0)
                //.lineToY(-48)
                .waitSeconds(3)
                .setTangent(0)
                .lineToX(0);

        Action first = firstBuilder.build();
        Action second = secondBuilder.build();


        while (!isStopRequested() && !opModeIsActive()) {
            Pose2d position = drive.localizer.getPose();
            telemetry.addData("Position during Init", position);
            telemetry.update();
        }

        waitForStart();
        elapsedTime.reset();


        Actions.runBlocking(first);
        //Actions.runBlocking(second);

        //Actions.runBlocking(barToObservationZone);

        //Actions.runBlocking(sample2ToObservationZone);

        //Actions.runBlocking(observationZoneToBar);

        //path to return to ob zone for second specimen
        //Actions.runBlocking(backToObservationZone);

        //insert going to wall pickup pos
        //Actions.runBlocking(pickupSecondSpecimen);

        //insert lifting specimen off wall
        //Actions.runBlocking(toBarSecondSpecimen);

        //insert placing specimen on high bar
        //Actions.runBlocking(bookItToObservationZone);
    }
}

