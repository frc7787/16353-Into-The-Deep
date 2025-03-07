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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;


import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.actions.ElevatorAction;
import static org.firstinspires.ftc.teamcode.Constants.*;

@Disabled
@Autonomous
public class AutoBlocksOnly extends LinearOpMode {
    private final Pose2d initialPose = new Pose2d(-40.0, -66.5, 0); // changed y from -65


    private Servo rotationServo, clawServo,twistServo, bucketServo, hockeyStickServo;

    TranslationalVelConstraint fastVelocity = new TranslationalVelConstraint(60);
    ProfileAccelConstraint fastAcceleration = new ProfileAccelConstraint(-40,60);




    @Override public void runOpMode() {

        ElapsedTime elapsedTime = new ElapsedTime();
        rotationServo = hardwareMap.get(Servo .class,ROTATION_SERVO_NAME);
        hockeyStickServo = hardwareMap.get(Servo.class, HOCKEYSTICK_SERVO_NAME);
        twistServo = hardwareMap.get(Servo.class, TWIST_SERVO_NAME);
        clawServo = hardwareMap.get(Servo.class, CLAW_SERVO_NAME);
        bucketServo = hardwareMap.get(Servo.class, BUCKET_SERVO_NAME);

        rotationServo.setPosition(ROTATION_NEUTRAL);
        hockeyStickServo.setPosition(HOCKEYSTICK_INITIAL);
        twistServo.setPosition(TWIST_INITIAL);
        clawServo.setPosition(CLAW_OPEN);
        bucketServo.setPosition(BUCKET_HOME);

        //MecanumDrive drive = new MecanumDrive.Builder(hardwareMap)
        //.setPose(initialPose)
        //.build();

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        ElevatorAction elevator = new ElevatorAction(hardwareMap);

        TrajectoryActionBuilder firstBuilder = drive.actionBuilder(initialPose)
                // starting pre-loaded to sub
                .setReversed(true)
                //hang specimen onto bar
                //.splineToConstantHeading(new Vector2d(-4,-33.5),Math.PI/2)
                .splineToConstantHeading(new Vector2d(-47,-65), Math.PI)
                .waitSeconds(2);

        // bar to wall pickup


        TrajectoryActionBuilder secondBuilder = firstBuilder.endTrajectory().fresh()
                // FIRST SPIKE MARK, head towards

                //.splineToLinearHeading(new Pose2d(-24,-48,Math.PI),Math.PI)
                //.splineToSplineHeading(new Pose2d( -48,-45,Math.PI/2),Math.PI/2);
                .setTangent(Math.PI/2)
                .strafeTo(new Vector2d(-47,-60))
                .splineToSplineHeading(new Pose2d( -48,-45,Math.PI/2),Math.PI/2);


        TrajectoryActionBuilder thirdBuilder = secondBuilder.endTrajectory().fresh()

                //strafe hockey stick into sample, then pick up sample
                .strafeTo(new Vector2d(-55,-45))
                //.strafeTo(new Vector2d(-50,-45))
                .strafeTo(new Vector2d(-50,-43));
        //.waitSeconds(0.5);



        TrajectoryActionBuilder fourthBuilder = thirdBuilder.endTrajectory().fresh()
                // Bucket 1

                .splineToLinearHeading(new Pose2d(-56,-56,Math.PI/4),Math.PI*8/6);
        //.waitSeconds(0.5);

        TrajectoryActionBuilder fifthBuilder = fourthBuilder.endTrajectory().fresh()
                // Spike Mark 2
                /*
                .splineToSplineHeading(new Pose2d(-60,-42,Math.PI/2),Math.PI/2)
                .strafeTo(new Vector2d(-62,-42))
                .strafeTo(new Vector2d(-62,-40));
                 */
                .splineToSplineHeading(new Pose2d(-53,-45,Math.PI/2),Math.PI/2)
                .strafeTo(new Vector2d(-63,-45)) // changed from -64, to stop hitting wall
                .strafeTo(new Vector2d(-59,-43));

        TrajectoryActionBuilder sixthBuilder = fifthBuilder.endTrajectory().fresh()
                // Bucket 2
                .splineToLinearHeading(new Pose2d(-56,-56,Math.PI/4),Math.PI*8/6);

        TrajectoryActionBuilder seventhBuilder = sixthBuilder.endTrajectory().fresh()
                // park in sub
                //.setTangent(Math.PI/2)
                //.splineTo(new Vector2d(-10, -8), 0);
                .splineToLinearHeading(new Pose2d(-10,-8,Math.PI),0);



                // bar to wall pickup


/*  SKIP THE THIRD SPIKE MARK - IT TAKES TOO LONG
        TrajectoryActionBuilder fourthBuilder = thirdBuilder.endTrajectory().fresh()
                // THIRD SPIKE MARK
                // heading back up to third spike mark
                .splineToLinearHeading(new Pose2d(36,-53,Math.PI/2),Math.PI/2)
                .splineToSplineHeading(new Pose2d(36,-33,Math.PI/2), Math.PI/2)
                // almost there
                .splineToConstantHeading(new Vector2d(64,-21),0)
                // push block into zone
                .splineToLinearHeading(new Pose2d(56,-61,Math.PI/2),-Math.PI/2)
                .splineToLinearHeading(new Pose2d(50,-60,Math.PI/2),-Math.PI/2);

            TrajectoryActionBuilder fifthBuilder = fourthBuilder.endTrajectory().fresh()
                // from pickup specimen to clipping
        ;
 */







                //TrajectoryActionBuilder sixthBuilder = secondBuilder.endTrajectory().fresh()
                // extra one just in case you want to add something

        // new trajectory needed: first, action: elevator up to clipping position, lifts specimen from wall
        // north a bit, turn around again (tangents will now be NEGATIVE again)
        // west towards sub, north to sub, action: clipping, reverse to park if possible

/*
        TrajectoryActionBuilder extraBuilder = firstBuilder.endTrajectory().fresh()
                .waitSeconds(5)
                .lineToY(-48)
                .waitSeconds(3)
                .setTangent(0)
                .lineToX(0);

 */

        Action first = firstBuilder.build();
        Action second = secondBuilder.build();
        Action third = thirdBuilder.build();
        Action fourth = fourthBuilder.build();
        Action fifth = fifthBuilder.build();
        Action sixth = sixthBuilder.build();
        Action seventh = seventhBuilder.build();





        while (!isStopRequested() && !opModeIsActive()) {
            Pose2d position = drive.localizer.getPose();
            telemetry.addData("Position during Init", position);
            telemetry.update();
        }

        waitForStart();
        elapsedTime.reset();


        Actions.runBlocking(
                new SequentialAction(
                        // to the bucket
                        new ParallelAction(
                                first, elevator.BucketPosition()
                        ),
                        // dump the preloaded
                        elevator.DumpBucket(),
                        new ParallelAction(elevator.ClipHome(),second, elevator.HockeyStickOut()),
                        // towards spike mark 1
                        new ParallelAction(
                                third   // strafe into block, then pickup
                        ),
                        elevator.PickupBlock(),elevator.PreTransferBlock(),elevator.TransferBlock(),
                        // bucket deposit 1
                        new ParallelAction(
                                fourth,elevator.BucketPosition()
                        ),
                        elevator.DumpBucket(),
                        // towards spike mark 2
                        new ParallelAction(
                                fifth,elevator.ClipHome()
                        ),
                        elevator.PickupBlock(),elevator.PreTransferBlock(),elevator.TransferBlock(),
                        // bucket deposit 2
                        new ParallelAction(
                                sixth, elevator.BucketPosition()
                        ),
                        elevator.DumpBucket(),
                        // park
                        new ParallelAction(seventh,
                                elevator.ClipIt(),elevator.HockeyStickIn(),elevator.PreTransferBlock()),
                        elevator.ClipPark()



                ) // end of Sequential Action
        );
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




