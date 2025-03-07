package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


// straight line paths for clearing blocks

public class MeepMeepBlocksOnly {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(70, 70, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.685, 13.5)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-40, -65, 0))
                // starting pre-loaded to sub
                .setReversed(true)
                //hang specimen onto bar
                //.splineToConstantHeading(new Vector2d(-4,-33.5),Math.PI/2)
                .splineToConstantHeading(new Vector2d(-46,-65), Math.PI)
                .waitSeconds(2)

                // FIRST SPIKE MARK, head towards

                //.splineToLinearHeading(new Pose2d(-24,-48,Math.PI),Math.PI)
                //.splineToSplineHeading(new Pose2d( -48,-45,Math.PI/2),Math.PI/2)
                        .setTangent(Math.PI/2)
                .strafeTo(new Vector2d(-46,-60))
                .splineToSplineHeading(new Pose2d( -48,-45,Math.PI/2),Math.PI/2)

                //strafe hockey stick into sample, then pick up sample
                .strafeTo(new Vector2d(-55,-45))
                //.strafeTo(new Vector2d(-50,-45))
                .strafeTo(new Vector2d(-50,-43))

                // Bucket 1

                .splineToLinearHeading(new Pose2d(-56,-56,Math.PI/4),Math.PI*8/6)
                .waitSeconds(1)

                // Spike Mark 2
                .splineToSplineHeading(new Pose2d(-53,-45,Math.PI/2),Math.PI/2)
                .strafeTo(new Vector2d(-65,-45))
                .strafeTo(new Vector2d(-60,-43))

                // Bucket 2
                .splineToLinearHeading(new Pose2d(-56,-56,Math.PI/4),Math.PI*8/6)

                // park in sub

                //.waitSeconds(1)
                //.splineToLinearHeading(new Pose2d(-10,-8,Math.PI),0)

                // Spike Mark 3!
                        .setTangent(Math.PI/4)
                //.splineToLinearHeading(new Pose2d(-58.5,-40,3*Math.PI/4),3*Math.PI/4)
                .splineToLinearHeading(new Pose2d(-59.5,-40,Math.toRadians(130)),Math.toRadians(135))
                // PoseX -59.5
                // PoseY -40,
                // PoseHeading 130 degrees
                // PoseTangent 135



                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}

