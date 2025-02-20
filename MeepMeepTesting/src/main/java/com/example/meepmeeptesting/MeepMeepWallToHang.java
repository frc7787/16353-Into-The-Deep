package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.Vector2d;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

// reasonably efficient spline paths from the wall to clipping

public class MeepMeepWallToHang
{
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(70, 70, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.685, 13.5)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(50, -64, Math.PI / 2))

                //.lineToY(-50)
                //.splineToConstantHeading(new Vector2d(0, -24), -Math.PI/2)
                //.splineTo(new Vector2d(20,-40),0)
                //.splineTo(new Vector2d(42,-24),Math.PI/2)
                //first
                //.setTangent(Math.PI/2)
                //.lineToY(-60, null, new ProfileAccelConstraint(-70.0, 70.0))
                //.strafeTo(new Vector2d(50,-60))
                //.setTangent(Math.PI/2)
                .splineToSplineHeading(new Pose2d(4, -33.5, -Math.PI /1.999), Math.PI / 2)
                .waitSeconds(2)
                //second                .setTangent(-Math.PI/2)
                .splineToSplineHeading(new Pose2d(50, -60, Math.PI / 2), -Math.PI / 2)
                .waitSeconds(1)
                //.setTangent(Math.PI/2)
                .strafeTo(new Vector2d(50,-64), null, null)
                //.lineToY(-64, null, new ProfileAccelConstraint(-70.0, 70.0))
                //third
                .setTangent(Math.PI / 2)
                .splineToSplineHeading(new Pose2d(-0.5, -33.5, -Math.PI / 2), Math.PI / 2)
                //away from bar
                .setTangent(-Math.PI/2)
                .strafeTo(new Vector2d(-0.5,-35))


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
