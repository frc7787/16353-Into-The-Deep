package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlocks1 {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(70, 70, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.685, 13.5)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-10, -62, Math.PI/2))
                .lineToY(-36)   // up to sub for clipping
                .waitSeconds(2)     // clipping time here
                .setTangent(Math.toRadians(10))
                .lineToX(-25)   // strafe left to intermediate point
                // SPLINE over to 1st block
                //.splineTo(new Vector2d(-48,0), -Math.PI/2)
                .splineToConstantHeading(new Vector2d(-42,-10),-Math.PI/2)
                //.splineToSplineHeading(new Pose2d(-46,0,Math.PI), -Math.PI)
                .setTangent(Math.PI/2)
                .lineToY(-60)   // push back 1st block
                .lineToY(-12)   // back to spike marks
                .setTangent(0)
                .lineToX(-56)   // over to 2nd block
                .setTangent(Math.PI/2)
                .lineToY(-58)   // push back 2nd block
                .lineToY(-12)   // back to spike marks
                .setTangent(0)
                .lineToX(-62)   // over to 3rd block
                .setTangent(Math.PI/2)
                .lineToY(-54)   // push back 3rd block
                //.splineToConstantHeading(new Vector2d(-20,-10),Math.PI/2)
                //.splineTo(new Vector2d(-20,-10), Math.PI)
                .lineToY(-45)
                //.splineToLinearHeading(new Pose2d(-22, -10, 0), 0)
                .setTangent(-Math.PI/2)
                .lineToY(-10)
                .turnTo(0)
                .setTangent(0)
                .lineToX(-22)
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
