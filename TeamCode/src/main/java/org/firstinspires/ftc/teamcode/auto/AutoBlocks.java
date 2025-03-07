package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
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
import org.opencv.core.Mat;

@Disabled
@Autonomous
public class AutoBlocks extends LinearOpMode {
    private final Pose2d initialPose = new Pose2d(-16.5, -62, -Math.PI / 2);

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

        ElevatorAction elevator = new ElevatorAction(hardwareMap);

        TrajectoryActionBuilder firstBuilder = drive.actionBuilder(initialPose)
                // start position to sub for clipping
                .waitSeconds(5)
                .lineToY(-50)   // north a bit
                .setTangent(0)
                .lineToX(-4)     // west a bit, more into the center of sub
                .setTangent(-Math.PI/2)
                .lineToY(-23);   // north to the sub

        TrajectoryActionBuilder secondBuilder = firstBuilder.endTrajectory().fresh()
                // sub to behind the left hand spike mark
                //.waitSeconds(1)     // placeholder for action: CLIP
                //.afterTime(2, elevator.ClipIt())
                .setTangent(-Math.PI/2)
                .lineToY(-38)   // south to a midpoint
                .setTangent(0)
                .lineToX(-36)    // west to clear sub
                .setTangent(-Math.PI/2)
                .lineToY(-14)    // north past the right hand spike mark
                .setTangent(0)
                .lineToX(-47)   // west to line up with right hand spike mark
                .setTangent(-Math.PI/2)
                .lineToY(-62)   // south to push block
                .setTangent(-Math.PI/2)
                .lineToY(-14)   // north past the middle spike mark
                .setTangent(0)
                .lineToX(-55)   // west to line up with middle spike mark
                .setTangent(-Math.PI/2)
                .lineToY(-62)   // south to push block
                /*
                .setTangent(-Math.PI/2)
                .lineToY(-14)   // north past the left hand spike mark
                .setTangent(0)
                .lineToX(-63)   // west to line up with left hand spike mark
                .setTangent(-Math.PI/2)
                .lineToY(-50)  // south to push block

                 */
                //.lineToY(-45)   // go to intermediate point for launching into spline
                //.splineToLinearHeading(new Pose2d(-22,-10,0),0);    // spline to sub for parking
                .setTangent(Math.PI/2)
//                .lineToY(-10)
//                .turnTo(Math.PI)
//                .setTangent(0)
//                .lineToX(-22);
                .splineTo(new Vector2d(-10, -12), 0);
        // east to line up with left hand spike mark


        TrajectoryActionBuilder thirdBuilder = secondBuilder.endTrajectory().fresh()
                // parallel with clipHome, push in left hand spike mark, backup
                // turn around, move in for specimen pickup after a small wait

                .setTangent(-Math.PI/2)
                .lineToY(-60)   // south to push sample into zone
                .setTangent(-Math.PI/2)
                .lineToY(-55)   // north, backup out of zone
                // new position of turn
                .turn(Math.PI)      // spin around for gripper to face wall
                .setTangent(Math.PI/2)
                .lineToY(-65)   // south to intermediate point, human player lines up specimen (was -62)
                .setTangent(Math.PI/2)
                //.waitSeconds(2)
                .lineToY(-71.5,null,new ProfileAccelConstraint(-70.0,70.0));
        //.lineToY(-71);  // south to pickup specimen


        TrajectoryActionBuilder fourthBuilder = thirdBuilder.endTrajectory().fresh()
                // from pickup specimen to clipping
                .lineToY(-55)
                .setTangent(0)
                .lineToX(1)

                //.turnTo(-Math.PI/2)
                .turnTo(-Math.PI/2,
                        new TurnConstraints(2*Math.PI/3,-2*Math.PI/3,2*Math.PI/3))
                .setTangent(-Math.PI/2)
                .lineToY(-23);

        TrajectoryActionBuilder fifthBuilder = fourthBuilder.endTrajectory().fresh()
                // from pickup specimen to clipping
                .setTangent(-Math.PI/2)
                .lineToY(-30) //-48
                .setTangent(-Math.PI/6)  // 0
                .lineToX(48,null, new ProfileAccelConstraint(-70.0,70.0));
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
        //Action third = thirdBuilder.build();
        //Action fourth = fourthBuilder.build();
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
                                first, elevator.ClippingPosition()
                        ),
                        // clip it
                        elevator.ClipIt(),

                        // in front of spike mark
                        new ParallelAction(
                                second
                        ),

                        // homing elevator
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

