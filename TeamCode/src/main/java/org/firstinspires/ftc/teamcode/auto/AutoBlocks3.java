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
public class AutoBlocks3 extends LinearOpMode {
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

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        ElevatorAction elevator = new ElevatorAction(hardwareMap);

        TrajectoryActionBuilder firstBuilder = drive.actionBuilder(initialPose)
                // BUCKET 1
                // takes pre-loaded sample to dump in bucket

                .setReversed(true)
                .splineToConstantHeading(new Vector2d(-47,-65), Math.PI);
        //.waitSeconds(1);


        TrajectoryActionBuilder secondBuilder = firstBuilder.endTrajectory().fresh()
                // FIRST SPIKE MARK, head towards

                .setTangent(Math.PI/2)
                .strafeTo(new Vector2d(-47,-60))
                .splineToSplineHeading(new Pose2d( -48,-45,Math.PI/2),Math.PI/2);


        TrajectoryActionBuilder thirdBuilder = secondBuilder.endTrajectory().fresh()
                //strafe hockey stick into sample, then pick up sample

                .strafeTo(new Vector2d(-55,-45))
                .strafeTo(new Vector2d(-50,-43));
        //.waitSeconds(0.5);


        TrajectoryActionBuilder fourthBuilder = thirdBuilder.endTrajectory().fresh()
                // Bucket 2

                .splineToLinearHeading(new Pose2d(-56,-56,Math.PI/4),Math.PI*8/6);
        //.waitSeconds(0.5);

        TrajectoryActionBuilder fifthBuilder = fourthBuilder.endTrajectory().fresh()
                // Spike Mark 2

                .splineToSplineHeading(new Pose2d(-53,-45,Math.PI/2),Math.PI/2)
                .strafeTo(new Vector2d(-63.5,-45)) // changed from -64, to stop hitting wall
                .strafeTo(new Vector2d(-59,-43));

        TrajectoryActionBuilder sixthBuilder = fifthBuilder.endTrajectory().fresh()
                // Bucket 3

                .splineToLinearHeading(new Pose2d(-56,-56,Math.PI/4),Math.PI*8/6);

        // FOR 3 BLOCKS ONLY, take out movement to the 3rd SPIKE MARK
        /*
        TrajectoryActionBuilder seventhBuilder = sixthBuilder.endTrajectory().fresh()
                // Spike Mark 3

                .setTangent(Math.PI/4)
                .splineToLinearHeading(new Pose2d(-57.5,-40.0,3*Math.PI/4),3*Math.PI/4);
        // changed from -57,-39.5 to

        TrajectoryActionBuilder eighthBuilder = seventhBuilder.endTrajectory().fresh()
                // Bucket 4

                .setTangent(-Math.PI/4)
                .splineToLinearHeading(new Pose2d(-56,-56,Math.PI/4),Math.PI*8/6);

         */

        TrajectoryActionBuilder ninthBuilder = sixthBuilder.endTrajectory().fresh()
                // park in sub

                .setTangent(Math.PI/2)
                .splineToLinearHeading(new Pose2d(-20,-9,0),0);


        Action first = firstBuilder.build();
        Action second = secondBuilder.build();
        Action third = thirdBuilder.build();
        Action fourth = fourthBuilder.build();
        Action fifth = fifthBuilder.build();
        Action sixth = sixthBuilder.build();
        //Action seventh = seventhBuilder.build();
        //Action eighth = eighthBuilder.build();
        Action ninth = ninthBuilder.build();




        while (!isStopRequested() && !opModeIsActive()) {
            Pose2d position = drive.localizer.getPose();
            telemetry.addData("Position during Init", position);
            telemetry.update();
        }

        waitForStart();
        elapsedTime.reset();


        Actions.runBlocking(
                new SequentialAction(
                        // to the bucket with the preloaded
                        // bucket deposit 1
                        new ParallelAction(
                                first, elevator.BucketPosition()
                        ),
                        // dump the preloaded
                        elevator.DumpBucket(),
                        new ParallelAction(elevator.ClipHome(),elevator.HockeyStickOut(),
                                new SequentialAction(second, third)),
                        // towards spike mark 1
                        // strafe into block, then pickup

                        elevator.PickupBlock(),elevator.PreTransferBlock(),elevator.TransferBlock(),
                        // bucket deposit 2
                        new ParallelAction(
                                fourth,elevator.BucketPosition()
                        ),
                        elevator.DumpBucket(),
                        // towards spike mark 2
                        new ParallelAction(elevator.ClipHome(),
                                new SequentialAction(fifth,
                                        elevator.PickupBlock(),elevator.PreTransferBlock(),elevator.TransferBlock())

                        ),
                        // bucket deposit 3
                        new ParallelAction(
                                sixth, elevator.BucketPosition()
                        ),
                        elevator.DumpBucket(),

                        /*  FOR 3 BLOCKS ONLY, take out movement to the 3rd SPIKE MARK
                        // spike mark 3
                        new ParallelAction(elevator.ClipHome(),elevator.HockeyStickIn(),
                                new SequentialAction(seventh,
                                        elevator.PickupBlock(),elevator.PreTransferBlock(),elevator.TransferBlock())
                        ),
                        // bucket deposit 4
                        new ParallelAction(elevator.BucketPosition(),eighth),
                        elevator.DumpBucket(),
                         */
                        new ParallelAction(elevator.ClipHome(),ninth,elevator.HockeyStickPark())


                ) // end of Sequential Action
        );  // end of Actions.runblocking

    } // end of runopmode
} // end of public class





