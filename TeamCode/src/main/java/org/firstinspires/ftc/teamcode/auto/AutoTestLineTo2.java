package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous
public class AutoTestLineTo2 extends LinearOpMode {
    private final Pose2d initialPose = new Pose2d(7.5, -55, -Math.PI / 2);

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
                .lineToY(-50)   // north a bit
                .setTangent(0)
                .lineToX(4)     // west a bit, more into the center of sub
                .setTangent(-Math.PI/2)
                .lineToY(-23)   // north to the sub
                // new trajectory should start here
                .waitSeconds(2)     // placeholder for action: CLIP
                .setTangent(-Math.PI/2)
                .lineToY(-30)   // south to a midpoint
                .setTangent(0)
                .lineToX(36)    // east towards the spike marks
                .setTangent(-Math.PI/2)
                .lineToY(-6)    // north past the left hand spike mark
                .setTangent(0)
                .lineToX(47)    // east to line up with left hand spike mark
                // turn was here

                .setTangent(-Math.PI/2)
                .lineToY(-60)   // south to push sample into zone
                .setTangent(-Math.PI/2)
                .lineToY(-55)   // north, backup out of zone
                // new position of turn
                .turn(Math.PI)      // spin around for gripper to face wall
                .setTangent(Math.PI/2)
                .lineToY(-62)   // south to intermediate point, human player lines up specimen
                .setTangent(Math.PI/2)
                .lineToY(-71);  // south to pickup specimen
        // new trajectory needed: first, action: elevator up to clipping position, lifts specimen from wall
        // north a bit, turn around again (tangents will now be NEGATIVE again)
        // west towards sub, north to sub, action: clipping, reverse to park if possible


        TrajectoryActionBuilder secondBuilder = firstBuilder.endTrajectory().fresh()
                .waitSeconds(5)
                .lineToY(-48)
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

