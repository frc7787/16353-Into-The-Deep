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
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
@Disabled
@Autonomous
public class AutoHang extends LinearOpMode {
    private final Pose2d initialPose = new Pose2d(8, -58, Math.PI / 2);

    @Override public void runOpMode() {

        ElapsedTime elapsedTime = new ElapsedTime();

        //MecanumDrive drive = new MecanumDrive.Builder(hardwareMap)
                //.setPose(initialPose)
                //.build();

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder startToBarBuilder = drive.actionBuilder(initialPose)
                .setTangent(Math.PI / 2)
                .splineTo(new Vector2d(4, -27), Math.PI/2);

        TrajectoryActionBuilder barToObservationZoneBuilder = startToBarBuilder.endTrajectory().fresh()
                .setTangent(-Math.PI / 2)
                .splineToLinearHeading(new Pose2d(26, -36, Math.PI), Math.PI / 2)
                .setTangent(Math.PI / 2)
                .splineToSplineHeading(new Pose2d(30, -21, -Math.PI /2), Math.PI/2)
                .setTangent(Math.PI/2)
                .splineToConstantHeading(new Vector2d(47, -10), 0)
                .setTangent(-Math.PI/2)
                .splineToConstantHeading(new Vector2d(47, -56), -Math.PI / 2);

                //push second sample to zone
                //.setTangent(Math.PI / 2)
                //.splineToSplineHeading(new Pose2d(47, -20, -Math.PI /2), Math.PI / 4)
                //.setTangent(Math.PI/4)
                //.splineToLinearHeading(new Pose2d(50, -15, -Math.PI /4), Math.PI / 2);
        // ends before contacting second sample


        TrajectoryActionBuilder sample2ToObservationBuilder = barToObservationZoneBuilder.endTrajectory().fresh()
                .setTangent(-Math.PI/4)
                .splineTo(new Vector2d(58, -28), -Math.PI/2)
                .setTangent(-Math.PI/2)
                .splineToSplineHeading(new Pose2d(58, -56, -Math.PI /2), -Math.PI / 2)
                .waitSeconds(0.3)
                .setTangent(-Math.PI/2)
                .lineToY(-67, null, new ProfileAccelConstraint(-70.0, 70.0));

        TrajectoryActionBuilder observationZoneToBarBuilder = sample2ToObservationBuilder.endTrajectory().fresh()
                .setTangent(Math.PI/2)
                .splineToSplineHeading(new Pose2d(-2, -30, Math.PI /1.999), Math.PI / 2);

        TrajectoryActionBuilder backToObservationZoneBuilder = observationZoneToBarBuilder.endTrajectory().fresh()
                .setTangent(-Math.PI/2)
                .splineToSplineHeading(new Pose2d(58, -56, -Math.PI/2), -Math.PI/2);

        TrajectoryActionBuilder pickupSecondSpecimenBuilder = backToObservationZoneBuilder.endTrajectory().fresh()
                .setTangent(-Math.PI/2)
                .lineToY(-70.069, null, new ProfileAccelConstraint(-70.0, 70.0));

        TrajectoryActionBuilder toBarSecondSpecimenBuilder = drive.actionBuilder(new Pose2d(58, -67, Math.PI/-2))
                .setTangent(Math.PI/2)
                .splineToSplineHeading(new Pose2d(-0.5, -27, Math.PI /2), Math.PI / 2);

        Action bookItToObservationZone = toBarSecondSpecimenBuilder.endTrajectory().fresh()
                .setTangent(-Math.PI/2)
                .splineTo(new Vector2d(30, -60), 0)
                .setTangent(0)
                .splineToConstantHeading(new Vector2d(60, -60), 0)
                .build();

        Action observationZoneToBar = observationZoneToBarBuilder.build();
        Action barToObservationZone = barToObservationZoneBuilder.build();
        Action sample2ToObservationZone = sample2ToObservationBuilder.build();
        Action startToBar = startToBarBuilder.build();
        Action backToObservationZone = backToObservationZoneBuilder.build();
        Action pickupSecondSpecimen = pickupSecondSpecimenBuilder.build();
        Action toBarSecondSpecimen = toBarSecondSpecimenBuilder.build();


        while (!isStopRequested() && !opModeIsActive()) {
            Pose2d position = drive.localizer.getPose();
            telemetry.addData("Position during Init", position);
            telemetry.update();
        }

        waitForStart();
        elapsedTime.reset();


        Actions.runBlocking(startToBar);

        Actions.runBlocking(barToObservationZone);

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

