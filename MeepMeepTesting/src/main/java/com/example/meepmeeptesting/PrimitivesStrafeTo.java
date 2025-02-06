package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.Vector2d;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class PrimitivesStrafeTo {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 40, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.685, 13.5)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(58, -68, -Math.PI / 2))

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
                // wall to sub
                .waitSeconds(0.3)
                .setTangent(-Math.PI/2)
                .lineToY(-67, null, new ProfileAccelConstraint(-70.0, 70.0))
                .setTangent(Math.PI/2)
                .splineToSplineHeading(new Pose2d(-2, -29, Math.PI /1.999), Math.PI / 2)
                // bar to wall pickup
                .setTangent(-Math.PI/2)
                .splineToSplineHeading(new Pose2d(58, -56, -Math.PI / 2), -Math.PI / 2)
                .waitSeconds(0.3)
                .setTangent(-Math.PI/2)
                .lineToY(-71, null, new ProfileAccelConstraint(-70.0, 70.0))
                // wall to sub 2nd time
                .setTangent(Math.PI / 2)
                .splineToSplineHeading(new Pose2d(-0.5, -31, Math.PI / 2), Math.PI / 2)










                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}