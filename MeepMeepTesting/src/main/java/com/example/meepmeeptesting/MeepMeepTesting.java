package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(70, 70, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.685, 13.5)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(7.5, -64, -Math.PI / 2))

                .lineToY(-50)
                .splineToConstantHeading(new Vector2d(0, -24), -Math.PI/2)
                .splineTo(new Vector2d(20,-40),0)
                                .splineTo(new Vector2d(42,-24),Math.PI/2)


                //.setTangent(-Math.PI/2)
                //.lineToY(-48)
                //.setTangent(0)
                //.lineToX(48)
                //.setTangent(-Math.PI/2)
                //.lineToY(0)
                //.lineToY(-48)
                //.setTangent(0)
                //.lineToX(0)


                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}