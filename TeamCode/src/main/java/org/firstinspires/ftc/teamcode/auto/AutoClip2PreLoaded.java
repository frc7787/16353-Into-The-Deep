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
public class AutoClip2PreLoaded extends LinearOpMode {
    private final Pose2d initialPose = new Pose2d(18.0, -64, -Math.PI / 2);


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
                //.splineToConstantHeading(new Vector2d(4,-24),Math.PI/2,fastVelocity,fastAcceleration)
                .splineToConstantHeading(new Vector2d(4,-33.5),Math.PI/2,null,null)

                .waitSeconds(0.5);

        TrajectoryActionBuilder secondBuilder = firstBuilder.endTrajectory().fresh()
                // sub to the wall in order to pickup the second pre-loaded
                .setTangent(-Math.PI/2)
                .splineToSplineHeading(new Pose2d(50, -58, Math.PI / 2), -Math.PI / 2, null,null)
                .waitSeconds(1)
                //.setTangent(Math.PI/2)
                //.lineToY(-64, null, new ProfileAccelConstraint(-70.0, 70.0));
                .strafeTo(new Vector2d(50,-66.5), null, null); // BACK ONE MORE

        TrajectoryActionBuilder thirdBuilder = secondBuilder.endTrajectory().fresh()
                // from the wall, back to the sub
                .setTangent(Math.PI / 2)
                .splineToSplineHeading(new Pose2d(-0.5, -32.0, -Math.PI / 2), Math.PI / 2,fastVelocity,fastAcceleration)
                .waitSeconds(0.5);


        TrajectoryActionBuilder fourthBuilder = thirdBuilder.endTrajectory().fresh()
                // FIRST SPIKE MARK
                // heading towards first spike marks, go midway
                // need to extend hockey stick
                .splineToLinearHeading(new Pose2d(24,-45,0),0)
                // go towards lh spike mark
                .splineToSplineHeading(new Pose2d(37,-33,Math.PI/2), Math.PI/2)
                // almost there
                .splineToConstantHeading(new Vector2d(44,-21),0)    // changed x from 45
                // push block into zone
                .splineToSplineHeading(new Pose2d(48,-61,Math.PI/2),-Math.PI/2);



        TrajectoryActionBuilder fifthBuilder = fourthBuilder.endTrajectory().fresh()
                // SECOND SPIKE MARK - push AND pickup plus 1
                // heading back up to second spike mark
                .splineToLinearHeading(new Pose2d(36,-53,Math.PI/2),Math.PI/2)
                .splineToSplineHeading(new Pose2d(36,-33,Math.PI/2), Math.PI/2)
                // almost there
                .splineToConstantHeading(new Vector2d(56,-21),0)
                // push block into zone
                .splineToSplineHeading(new Pose2d(56,-50,Math.PI/2),-7*Math.PI/8)

                // ADD in the movement away from the wall to prepare for specimen pickup
                .splineToLinearHeading(new Pose2d(50,-62,Math.PI/2),Math.PI)
                .waitSeconds(1.0)
                .strafeTo(new Vector2d(50,-67));



        TrajectoryActionBuilder sixthBuilder = fifthBuilder.endTrajectory().fresh()
                // at the wall, picked up the plus 1 specimen, head to the bar to clip
                .splineToSplineHeading(new Pose2d(6, -31.0, -Math.PI /1.999), Math.PI / 2, null, null)
                .waitSeconds(0.5);

        TrajectoryActionBuilder seventhBuilder = sixthBuilder.endTrajectory().fresh()
                // try to PARK
                .setTangent(-Math.PI/2)
                .strafeTo(new Vector2d(40,-60));

/* // COMMENTED OUT
        TrajectoryActionBuilder fifthBuilder = fourthBuilder.endTrajectory().fresh()
                // sub to the wall in order to pickup plus 2 specimen
                .setTangent(-Math.PI/2)
                .splineToSplineHeading(new Pose2d(50, -58, Math.PI / 2), -Math.PI / 2, null,null)
                .waitSeconds(1)
                //.setTangent(Math.PI/2)
                //.lineToY(-64, null, new ProfileAccelConstraint(-70.0, 70.0));
                .strafeTo(new Vector2d(50,-66.5), null, null); // BACK ONE MORE

        TrajectoryActionBuilder sixthBuilder = fifthBuilder.endTrajectory().fresh()
                // from the wall, back to the sub
                .setTangent(Math.PI / 2)
                .splineToSplineHeading(new Pose2d(-0.5, -32.0, -Math.PI / 2), Math.PI / 2,fastVelocity,fastAcceleration)
                .waitSeconds(0.5);




 */ // END OF COMMENTED OUT
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
        ;
        // new trajectory needed: first, action: elevator up to clipping position, lifts specimen from wall
        // north a bit, turn around again (tangents will now be NEGATIVE again)
        // west towards sub, north to sub, action: clipping, reverse to park if possible


        TrajectoryActionBuilder extraBuilder = firstBuilder.endTrajectory().fresh()
                .waitSeconds(5)
                .lineToY(-48)
                .waitSeconds(3)
                .setTangent(0)
                .lineToX(0);

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
                        // to the sub then clipping first pre-loaded
                        new ParallelAction(
                                first, elevator.ClippingPosition()
                        ),
                        // clip it
                        elevator.ClipIt(),
                        // back to the wall for second pre-loaded
                        new ParallelAction(
                                elevator.ClipHome(), second
                        ),
                        // back to the sub then clipping second pre-loaded
                        new ParallelAction(third, elevator.ClippingPosition()),
                        // clip it
                        elevator.ClipIt(),
                        // push the first spike mark
                        fourth,
                        // SECOND SPIKE MARK - push AND pickup plus 1
                        new ParallelAction(
                                fifth, elevator.ClipHome()
                        ),
                        // back to the sub then clipping first spike mark
                        new ParallelAction(
                                elevator.ClippingPosition(),sixth
                        ),
                        elevator.ClipIt(),
                        seventh,
                        elevator.ClipHome()


                        // push fourth block
                        //fourth


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



