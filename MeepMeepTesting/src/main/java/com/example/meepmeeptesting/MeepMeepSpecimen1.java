package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepSpecimen1 {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(70, 70, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.685, 13.5)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(10, -62, Math.PI/2))
                //.setTangent(0)
                //.splineTo(new Vector2d(48,48), Math.PI/2)
                //.splineToConstantHeading(new Vector2d(48,48),Math.PI/2)
                //.splineToLinearHeading(new Pose2d(48,48,0),Math.PI/2)
                //.splineToSplineHeading(new Pose2d(48,48,0), Math.PI/2)
                //.lineToX(30)
                //.turn(Math.toRadians(90))
                //.lineToY(30)
                //.turn(Math.toRadians(90))
                // .lineToX(0)
                //.turn(Math.toRadians(90))
                //.lineToY(0)
                //.turn(Math.toRadians(90))
                .lineToY(-36)
                .waitSeconds(2)
                .setTangent(Math.toRadians(10))
                .lineToX(35)
                //.splineTo(new Vector2d(48,0), Math.PI/2)
                //.splineToConstantHeading(new Vector2d(48,0),Math.PI)
                .splineToSplineHeading(new Pose2d(46,0,-Math.PI/2), 0)
                .setTangent(Math.PI/2)
                .lineToY(-54)
                .lineToY(-12)
                .setTangent(0)
                .lineToX(56)
                .setTangent(Math.PI/2)
                .lineToY(-54)
                .waitSeconds(2)
                .turnTo(Math.PI)
                .splineTo(new Vector2d(10,-36), Math.PI/2)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
