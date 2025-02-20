package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public final class Constants {
    // ALL Variables that are to be modified by FTC Dashboard should be marked public static volatile
    // Otherwise, use public static final

    // ------------------------------------------------------------------------
    // NAMES
    // ------------------------------------------------------------------------

    public static final String CLAW_SERVO_NAME = "clawServo";
    public static final String ROTATION_SERVO_NAME ="rotationServo";
    public static final String BUCKET_SERVO_NAME ="bucketServo";
    public static final String TWIST_SERVO_NAME = "twistServo";
    public static final String HOCKEYSTICK_SERVO_NAME = "hockeyStickServo";
    public static final String CLIP_MOTOR_NAME = "clipMotor";
    public static final String EXTENSION_MOTOR_NAME = "extentionMotor";
    public static final String CLIP_MOTOR_LIMIT_SWITCH_NAME = "clipTouchSensor";
    public static final String LIMIT_SWITCH_NAME = "limitSwitch";
    public static final String EXTENSION_LIMIT_SWITCH_NAME = "limitMaxExtension";


    // ------------------------------------------------------------------------
    // CONSTANTS that could change
    // ------------------------------------------------------------------------

    // BUCKET servo
    public static volatile double BUCKET_HOME = 0.8;
    public static volatile double BUCKET_IN = 0.2;
    // ROTATION servo
    public static volatile double ROTATION_PICKUP = 0.98;
    public static volatile double ROTATION_NEUTRAL = 0.7;
    public static volatile double ROTATION_TRANSFER = 0.65;
    public static volatile double ROTATION_PRE = 0.9;
    // CLAW servo
    public static volatile double CLAW_PICKUP = 0.46;
    public static volatile double CLAW_OPEN = 0.22;
    // TWIST servo
    public static volatile double TWIST_TRANSFER= 0.47;
    public static volatile double TWIST_INITIAL = 0.6;
    public static volatile double TWIST_PICKUP= 1;
    // HOCKEY STICK servo
    public static volatile double HOCKEYSTICK_INITIAL = 0.8;
    public static volatile double HOCKEYSTICK_OUT = 0.4;
    public static volatile double HOCKEYSTICK_PARK = 0.74;
    // MOTORS
    public static volatile int EXTENSION_MAX =2350;
    public static volatile int CLIPMOTOR_PREBUCKET = 3054;
    public static volatile int CLIPMOTOR_BAR = 1880;
    public static volatile int CLIPMOTOR_HOME = 0;
    public static volatile double CLIPMOTOR_POWER = 0.65;
    public static volatile double CLIPMOTOR_POWERUP = 1.0;
    public static volatile double CLIPMOTOR_POWERDOWN = 0.85;
    public static volatile double CLIPMOTOR_POWERDOWN_CLIPIT = 0.5;
    public static volatile double EXTENSION_POWERMAX = 0.95;

    public static volatile double TIME_PICKPUP_BLOCK = 0.75;
    public static volatile double TIME_CLOSE_CLAW = 0.75;
    public static volatile double TIME_PRE_TRANSFER = 0.75;
    public static volatile double TIME_DUMP_BUCKET = 1.0;
    public static volatile double TIME_HOCKEY_STICK = 1;
    public static volatile double TIME_TRANSFER_BLOCK = 0.5;



    // ------------------------------------------------------------------------
    // CONSTANTS that shouldn't change
    // ------------------------------------------------------------------------



}
