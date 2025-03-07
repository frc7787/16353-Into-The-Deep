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
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;


import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.actions.ElevatorAction2;
import static org.firstinspires.ftc.teamcode.Constants.*;

@Disabled
@Autonomous
public class AutoWallToHang extends LinearOpMode {
    private final Pose2d initialPose = new Pose2d(50.0, -64, Math.PI / 2);


    private Servo rotationServo, clawServo,twistServo, bucketServo, hockeyStickServo;




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

        TranslationalVelConstraint fastVelocity = new TranslationalVelConstraint(60);
        ProfileAccelConstraint fastAcceleration = new ProfileAccelConstraint(-40,60);


        //MecanumDrive drive = new MecanumDrive.Builder(hardwareMap)
        //.setPose(initialPose)
        //.build();

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        ElevatorAction2 elevator = new ElevatorAction2(hardwareMap);

        TrajectoryActionBuilder firstBuilder = drive.actionBuilder(initialPose)
                // start position of wall to sub
                //.setTangent(Math.PI/2)
                //.lineToY(-60, null, new ProfileAccelConstraint(-70.0, 70.0))
                //.lineToY(-60, null, null)
                //.strafeTo(new Vector2d(50,-60))
                //.setTangent(Math.PI/2)
                .splineToSplineHeading(new Pose2d(4, -31.0, -Math.PI /1.999), Math.PI / 2, null, null)
                .waitSeconds(0.5);
                // bar to wall pickup


        TrajectoryActionBuilder secondBuilder = firstBuilder.endTrajectory().fresh()
                // sub to the wall
                .setTangent(-Math.PI/2)
                .splineToSplineHeading(new Pose2d(50, -58, Math.PI / 2), -Math.PI / 2, null,null)
                .waitSeconds(1)
                //.setTangent(Math.PI/2)
                //.lineToY(-64, null, new ProfileAccelConstraint(-70.0, 70.0));
                .strafeTo(new Vector2d(50,-64), null, null);



        TrajectoryActionBuilder thirdBuilder = secondBuilder.endTrajectory().fresh()
                // from the wall, back to the sub
                .setTangent(Math.PI / 2)
                .splineToSplineHeading(new Pose2d(-0.5, -28.0, -Math.PI / 2), Math.PI / 2,fastVelocity,fastAcceleration)
                .waitSeconds(0.5);


        TrajectoryActionBuilder fourthBuilder = thirdBuilder.endTrajectory().fresh()
                // try to PARK
                .setTangent(-Math.PI/2)
                .strafeTo(new Vector2d(40,-60));

                //.turnTo(-Math.PI/2)
                //.turnTo(-Math.PI/2,
                        //new TurnConstraints(2*Math.PI/3,-2*Math.PI/3,2*Math.PI/3))
                //.setTangent(-Math.PI/2)
                //.lineToY(-23);

        TrajectoryActionBuilder fifthBuilder = fourthBuilder.endTrajectory().fresh()
                // from pickup specimen to clipping
                .setTangent(-Math.PI/2)
                .lineToY(-30) //-48
                .setTangent(-Math.PI/6)  // 0
                //.lineToX(48,null, new ProfileAccelConstraint(-70.0,70.0));
                .lineToXLinearHeading(48,-Math.PI/6, null, new ProfileAccelConstraint(-70.0,70.0));
        //.lineToX(48);  //48



        TrajectoryActionBuilder sixthBuilder = secondBuilder.endTrajectory().fresh()
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
        //Action fifth = fifthBuilder.build();



        while (!isStopRequested() && !opModeIsActive()) {
            Pose2d position = drive.localizer.getPose();
            telemetry.addData("Position during Init", position);
            telemetry.update();
        }

        waitForStart();
        elapsedTime.reset();

        Actions.runBlocking(
                new SequentialAction(
                        // to the sub then clipping
                        new ParallelAction(
                                first,
                                elevator.ClippingPosition()
                        ),
                        // clip it

                        elevator.ClipIt(),

                        // back to the wall
                        new ParallelAction(
                                elevator.ClipHome(), second
                        ),

                        // back to the sub
                        new ParallelAction(
                                elevator.ClippingPosition(), third
                        ),
                        elevator.ClipIt(),

                        // desperately try to park!
                        fourth,
                        elevator.ClipHome()




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


