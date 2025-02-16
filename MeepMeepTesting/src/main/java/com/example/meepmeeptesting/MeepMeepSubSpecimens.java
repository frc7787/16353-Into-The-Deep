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

public class MeepMeepSubSpecimens {


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
                // from wall pre-loaded to sub
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(4,-24),Math.PI/2,fastVelocity,fastAcceleration)
                .waitSeconds(1)

                // FIRST SPIKE MARK
                // heading towards first spike marks, go midway
                // need to extend hockey stick
                .splineToLinearHeading(new Pose2d(24,-36,0),0)
                // go towards lh spike mark
                .splineToSplineHeading(new Pose2d(36,-24,Math.PI/2), Math.PI/2)
                // almost there
                .splineToConstantHeading(new Vector2d(38,-12),0)
                // push block into zone
                .splineToSplineHeading(new Pose2d(48,-52,Math.PI/2),-Math.PI/2)


                // SECOND SPIKE MARK
                // heading back up to second spike mark
                .splineToLinearHeading(new Pose2d(36,-44,Math.PI/2),Math.PI/2)
                .splineToSplineHeading(new Pose2d(36,-24,Math.PI/2), Math.PI/2)
                // almost there
                .splineToConstantHeading(new Vector2d(44,-12),0)
                // push block into zone
                .splineToSplineHeading(new Pose2d(52,-52,Math.PI/2),-Math.PI/2)


                // THIRD SPIKE MARK
                // heading back up to second spike mark
                .splineToLinearHeading(new Pose2d(36,-44,Math.PI/2),Math.PI/2)
                .splineToSplineHeading(new Pose2d(36,-24,Math.PI/2), Math.PI/2)
                // almost there
                .splineToConstantHeading(new Vector2d(56,-12),0)
                // push block into zone
                .splineToLinearHeading(new Pose2d(56,-48,Math.PI/2),-Math.PI/2)
                .splineToLinearHeading(new Pose2d(56,-60,Math.PI/2),-Math.PI/2)


                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}