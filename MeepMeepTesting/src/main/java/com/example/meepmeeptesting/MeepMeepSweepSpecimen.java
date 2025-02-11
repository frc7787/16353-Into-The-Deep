package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Map;

public class MeepMeepSweepSpecimen {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 40, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.685, 13.5)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(0, -24, -Math.PI / 2))

                //.strafeToConstantHeading(new Vector2d(0,-24))
                //.strafeToLinearHeading(new Vector2d(0,-24),Math.PI/2)
                //.strafeToSplineHeading(new Vector2d(48,-60),-Math.PI/2)
                //.strafeToSplineHeading(new Vector2d(0,-24),Math.PI/2)
                //.waitSeconds(2)
                //.strafeToSplineHeading(new Vector2d(0,-30),Math.PI/2)
                //.strafeToSplineHeading(new Vector2d(48,-60),-Math.PI/2)
                //.strafeToSplineHeading(new Vector2d(48,-64),-Math.PI/2)
                //.setReversed(true)
                //.splineTo(new Vector2d(0,-24),Math.PI/2)
                //.setReversed(true)
                //.setTangent(0)
                // at the sub, go midway to lh spike mark
                .splineToLinearHeading(new Pose2d(24,-48,0),0)
                // go towards lh spike mark
                .splineToSplineHeading(new Pose2d(36,-36,Math.PI/4), Math.PI/4)
                .waitSeconds(1)
                //.turnTo(-Math.PI/4)
                .turnTo(-Math.PI/4, new TurnConstraints(Math.PI,-Math.PI,Math.PI))


                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}