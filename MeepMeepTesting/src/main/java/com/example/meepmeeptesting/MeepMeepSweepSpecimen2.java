package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Map;

// beginning test of sweeping specimens with hockey stick
// this uses just a turn, which probably won't work

public class MeepMeepSweepSpecimen2 {


    public static void main(String[] args) {
        TranslationalVelConstraint fastVelocity = new TranslationalVelConstraint(60);
        ProfileAccelConstraint fastAcceleration = new ProfileAccelConstraint(-40,60);

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 40, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.685, 13.5)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(18, -60, -Math.PI / 2))

                // from wall at x=18 pre-loaded to sub
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(4,-24),Math.PI/2,fastVelocity,fastAcceleration)
                .waitSeconds(1)

                // FIRST SPIKE MARK
                // heading towards first spike marks, go midway
                // need to extend hockey stick
                .splineToLinearHeading(new Pose2d(24,-48,0),0)
                // go towards lh spike mark
                .splineToSplineHeading(new Pose2d(36,-36,Math.PI/4), Math.PI/4)
                //.waitSeconds(0.5)
                // sweep 1st block
                .splineToSplineHeading(new Pose2d(48, -48, -Math.PI/4),-Math.PI/4)

                // SECOND SPIKE MARK
                // go towards middle spike mark
                .splineToLinearHeading(new Pose2d(44, -36,Math.PI/3),Math.PI/3)
                //.waitSeconds(2)
                // sweep 2nd block
                // execute a small turn, hopefully sweeping the block a bit
                .splineToSplineHeading(new Pose2d(52,-34,0),-Math.PI/3)
                //.waitSeconds(2)
                // bring the block down into the observation zone
                .splineToConstantHeading(new Vector2d(52,-56),-Math.PI/2)

                // TIME TO GO TO WALL FOR CLIPPING
                // retract hockey stick
                // move to wall
                .splineToSplineHeading(new Pose2d(48, -60, Math.PI / 2), -Math.PI / 2)
                //.waitSeconds(1)
                .setTangent(Math.PI/2)
                .lineToY(-64, null, new ProfileAccelConstraint(-70.0, 70.0))


                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}