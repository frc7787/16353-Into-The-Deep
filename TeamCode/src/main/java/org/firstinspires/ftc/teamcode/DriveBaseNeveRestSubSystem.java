package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.drivebase.HDrive;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.arcrobotics.ftclib.hardware.RevIMU;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;
class NeveRestGBHDrive extends HDrive {
    Motor[] motors;

    public static final double kDefaultRightMotorAngle = Math.PI / 3;
    public static final double kDefaultLeftMotorAngle = 2 * Math.PI / 3;
    public static final double kDefaultSlideMotorAngle = 3 * Math.PI / 2;

    private double rightMotorAngle = kDefaultRightMotorAngle;
    private double leftMotorAngle = kDefaultLeftMotorAngle;
    private double slideMotorAngle = kDefaultSlideMotorAngle;
    public NeveRestGBHDrive(Motor mLeft, Motor mRight, Motor slide) {
        motors = new Motor[3];
        motors[MotorType.kLeft.value] = mLeft;
        motors[MotorType.kRight.value] = mRight;
        motors[MotorType.kSlide.value] = slide;
    }

    /**
     * The constructor that includes the angles of the motors.
     *
     * <p>
     * The default angles are {@value #kDefaultRightMotorAngle},
     * {@value #kDefaultLeftMotorAngle}, {@value #kDefaultSlideMotorAngle}.
     * </p>
     *
     * @param mLeft           one of the necessary primary drive motors
     * @param mRight          one of the necessary primary drive motors
     * @param slide           the necessary slide motor for the use of h-drive
     * @param leftMotorAngle  the angle of the left motor in radians
     * @param rightMotorAngle the angle of the right motor in radians
     * @param slideMotorAngle the angle of the slide motor in radians
     */
    public NeveRestGBHDrive(Motor mLeft, Motor mRight, Motor slide, double leftMotorAngle,
                  double rightMotorAngle, double slideMotorAngle) {
        motors = new Motor[3];
        motors[0] = mLeft;
        motors[1] = mRight;
        motors[2] = slide;

        this.leftMotorAngle = leftMotorAngle;
        this.rightMotorAngle = rightMotorAngle;
        this.slideMotorAngle = slideMotorAngle;
    }

    /**
     * Sets up the constructor for the holonomic drive.
     *
     * @param myMotors The motors in order of:
     *                 frontLeft, frontRight, backLeft, backRight.
     *                 Do not input in any other order.
     */
    public NeveRestGBHDrive(Motor... myMotors) {
        motors = myMotors;
    }

    public void driveFieldCentric(double strafeSpeed, double forwardSpeed,
                                  double turn, double heading,double[] maxOutputs) {
        strafeSpeed = clipRange(strafeSpeed);
        forwardSpeed = clipRange(forwardSpeed);
        turn = clipRange(turn);

        Vector2d vector = new Vector2d(strafeSpeed, forwardSpeed);
        vector = vector.rotateBy(-heading);

        double theta = vector.angle();

        double[] speeds = new double[motors.length];

        if (speeds.length == 3) {
            Vector2d leftVec = new Vector2d(Math.cos(leftMotorAngle), Math.sin(leftMotorAngle));
            Vector2d rightVec = new Vector2d(Math.cos(rightMotorAngle), Math.sin(rightMotorAngle));
            Vector2d slideVec = new Vector2d(Math.cos(slideMotorAngle), Math.sin(slideMotorAngle));

            speeds[MotorType.kLeft.value] = vector.scalarProject(leftVec) + turn;
            speeds[MotorType.kRight.value] = vector.scalarProject(rightVec) + turn;
            speeds[MotorType.kSlide.value] = vector.scalarProject(slideVec) + turn;

            normalize(speeds);

            motors[MotorType.kLeft.value].set(speeds[MotorType.kRight.value] * maxOutputs[0]);
            motors[MotorType.kRight.value].set(speeds[MotorType.kLeft.value] * maxOutputs[1]);
            motors[MotorType.kSlide.value].set(speeds[MotorType.kSlide.value] * maxOutputs[2]);
        }
        // this looks similar to mecanum because mecanum is a four wheel holonomic drivebase
        else {
            speeds[MotorType.kFrontLeft.value] = Math.sin(theta + Math.PI / 4);
            speeds[MotorType.kFrontRight.value] = Math.sin(theta - Math.PI / 4);
            speeds[MotorType.kBackLeft.value] = Math.sin(theta - Math.PI / 4);
            speeds[MotorType.kBackRight.value] = Math.sin(theta + Math.PI / 4);

            normalize(speeds, vector.magnitude());

            speeds[MotorType.kFrontLeft.value] += turn;
            speeds[MotorType.kFrontRight.value] -= turn;
            speeds[MotorType.kBackLeft.value] += turn;
            speeds[MotorType.kBackRight.value] -= turn;

            motors[MotorType.kFrontLeft.value]
                    .set(speeds[MotorType.kFrontLeft.value] * maxOutputs[0]);
            motors[MotorType.kFrontRight.value]
                    .set(speeds[MotorType.kFrontRight.value] * -maxOutputs[1]);
            motors[MotorType.kBackLeft.value]
                    .set(speeds[MotorType.kBackLeft.value] * maxOutputs[2]);
            motors[MotorType.kBackRight.value]
                    .set(speeds[MotorType.kBackRight.value] * -maxOutputs[3]);
        }

    }

    public void driveRobotCentric(double strafeSpeed, double forwardSpeed,
                                  double turn,double [] maxOutputs) {
        driveFieldCentric(strafeSpeed, forwardSpeed, turn, 0.0,maxOutputs);
    }



}
public class DriveBaseNeveRestSubSystem {
    private Motor frontLeft, frontRight, backRight, backLeft;
    private RevIMU imu;
    private NeveRestGBHDrive hDrive;
    private double[] maxOutputs = {0.6,0.7,0.6,0.6};
    public DriveBaseNeveRestSubSystem(HardwareMap hardwareMap, double [] myMaxSpeeds) {
        frontLeft = new Motor(hardwareMap, "frontLeft");
        frontRight = new Motor(hardwareMap, "frontRight");
        backRight = new Motor(hardwareMap, "backRight");
        backLeft = new Motor(hardwareMap, "backLeft");

        frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        frontLeft.encoder.setDirection(Motor.Direction.FORWARD);
        frontRight.encoder.setDirection(Motor.Direction.REVERSE);
        backRight.encoder.setDirection(Motor.Direction.REVERSE);
        backLeft.encoder.setDirection(Motor.Direction.FORWARD);

        imu = new RevIMU(hardwareMap);
        imu.init();
        hDrive = new NeveRestGBHDrive(frontLeft, frontRight, backLeft, backRight);
        for (int i=0;i<maxOutputs.length; i++) {
            maxOutputs[i] = myMaxSpeeds[i];
        }
    }
    public Motor getFrontLeft() {
        return frontLeft;
    }
    public Motor getFrontRight() {
        return frontRight;
    }
    public void drive(double forward, double strafe, double turn) {
        hDrive.driveRobotCentric(strafe, forward, turn, maxOutputs);
    }

    public void stop() {
        drive(0, 0, 0);
    }
    public void setMaxSpeed(double[] myMaxSpeeds) {
       for (int i=0;i<maxOutputs.length; i++) {
           maxOutputs[i] = myMaxSpeeds[i];
       }

    }

}
