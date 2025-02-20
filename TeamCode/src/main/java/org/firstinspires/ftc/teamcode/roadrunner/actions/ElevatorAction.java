package org.firstinspires.ftc.teamcode.roadrunner.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.teamcode.Constants.*;



public class ElevatorAction {
    public DcMotor elevatorMotor;

    private double BUCKETHOME = 0.8;
    private double BUCKETIN = 0.2;
    private double ROTATIONPICKUP = 0.85;
    private double CLAWPICKUP = 0.46;
    private double ROTATIONNEUTRAL = 0.56;

    private double CLAWOPEN = 0.22;
    private double ROTATIONTRANSFER = 0.5;
    private double ROTATIONPRE = 0.81;
    private double TWISTTRANSFER= 0.47;
    private double TWISTPICKUP= 1;

    private int CLIPMOTORPREBUCKET = 3054;
    //private int CLIPMOTORBAR = 1880;
    //public static volatile int CLIPMOTORBAR = 1980;
    private int CLIPPING = CLIPMOTOR_BAR - 650;  // changed from 450
    private int CLIPMOTORPARK = 1175;
    private int CLIPMOTORHOME = 0;
    private double CLIPMOTORPOWER = 0.95;
    private double CLIPMOTORPOWERDOWN = 0.5;

    private ElapsedTime elapsedTime;
    private double timeOutSeconds;

    private RevTouchSensor clipTouchSensor;

    private Servo rotationServo, clawServo,twistServo, bucketServo, hockeystickServo;




    public ElevatorAction(HardwareMap hardwareMap){
        elevatorMotor = hardwareMap.get(DcMotor.class, "clipMotor");
        elevatorMotor.setDirection(DcMotor.Direction.REVERSE);

        clawServo = hardwareMap.get(Servo.class, CLAW_SERVO_NAME);
        rotationServo = hardwareMap.get(Servo.class, ROTATION_SERVO_NAME);
        bucketServo = hardwareMap.get(Servo.class, BUCKET_SERVO_NAME);
        twistServo = hardwareMap.get(Servo.class, TWIST_SERVO_NAME);
        hockeystickServo = hardwareMap.get(Servo.class, HOCKEYSTICK_SERVO_NAME);

        clipTouchSensor = hardwareMap.get(RevTouchSensor.class, "clipTouchSensor");
        elapsedTime = new ElapsedTime();
        timeOutSeconds = 4.0;

        elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    } // end of constructor ElevatorAction

    public class clippingPosition implements Action {
        private boolean initialized = false;
        private boolean stillClippingPosition = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                elapsedTime.reset();
                elevatorMotor.setTargetPosition(CLIPMOTOR_BAR);
                elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                elevatorMotor.setPower(CLIPMOTORPOWER);
                packet.put("Elevator going to","bar");
                initialized = true;
            } // end of not initialized
            else {
                int elevatorPosition = elevatorMotor.getCurrentPosition();
                boolean isFinished = (elevatorPosition > CLIPMOTOR_BAR - 10) || (elapsedTime.seconds() > timeOutSeconds);
                packet.put("Elevator Position",elevatorPosition);
                if (isFinished) {
                    //elevatorMotor.setPower(0);
                    //elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    //elevatorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    stillClippingPosition = false;
                    packet.put("Finished going to bar for clipping position",elevatorPosition);
                }
            } // end of else already initialized
            packet.put("clippingPosition",initialized);
            return stillClippingPosition;
        } // end of run method for clippingPosition Action
    } // end of clippingPosition Action

    public Action ClippingPosition() {
        return new clippingPosition();
    }

    public class clipIt implements Action {
        private boolean initializedClipit = false;
        private boolean stillClipit = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            packet.put("Elevator clipIT","TOP");
            if (!initializedClipit) {
                elapsedTime.reset();
                elevatorMotor.setTargetPosition(CLIPPING);
                elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                elevatorMotor.setPower(CLIPMOTORPOWERDOWN);

                packet.put("Elevator clipping","Initialize");
                initializedClipit = true;
            } // end of not initialized
            else {
                int elevatorPosition = elevatorMotor.getCurrentPosition();
                boolean isFinished = (elevatorPosition < (CLIPPING + 20) && (elevatorPosition > (CLIPPING - 20)))
                        || (elapsedTime.seconds() > timeOutSeconds);
                packet.put("Elevator Position",elevatorPosition);
                if (isFinished) {
                    elevatorMotor.setPower(0);
                    //elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    //elevatorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    stillClipit = false;
                    packet.put("Finished clipping",elevatorPosition);
                }
            } // end of else already initialized
            packet.put("clipIt",stillClipit);
            return stillClipit;
        } // end of run method for clipIt Action
    } // end of clipIt Action

    public Action ClipIt() {
        return new clipIt();
    }

    public class clipHome implements Action {
        private boolean initialized = false;
        private boolean stillClippingPosition = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                elapsedTime.reset();
                elevatorMotor.setTargetPosition(CLIPMOTORHOME);
                elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                elevatorMotor.setPower(CLIPMOTORPOWERDOWN);

                packet.put("Elevator homing","to the bottom");
                initialized = true;
            } // end of not initialized
            else {
                int elevatorPosition = elevatorMotor.getCurrentPosition();
                //boolean isFinished = (elevatorPosition < (CLIPMOTORHOME + 10) && (elevatorPosition > (CLIPMOTORHOME)))
                boolean isFinished = (elevatorPosition < (CLIPMOTORHOME + 10))
                        || clipTouchSensor.isPressed()
                        || (elapsedTime.seconds() > timeOutSeconds);
                packet.put("Elevator Position",elevatorPosition);
                if (isFinished) {
                    elevatorMotor.setPower(0);
                    //elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    //elevatorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    stillClippingPosition = false;
                    packet.put("Finished homing less than 10",(elevatorPosition < (CLIPMOTORHOME + 10)));
                    packet.put("Finished homing limit switch",clipTouchSensor.isPressed());
                    packet.put("Finished homing elevatorposition",elevatorPosition);
                }
            } // end of else already initialized
            packet.put("clipHome",initialized);
            return stillClippingPosition;
        } // end of run method for clipHome Action
    } // end of clipHome Action

    public Action ClipHome() {
        return new clipHome();
    }

    public class clipPark implements Action {
        private boolean initialized = false;
        private boolean stillClippingPosition = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                elapsedTime.reset();
                elevatorMotor.setTargetPosition(CLIPMOTORPARK);
                elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                elevatorMotor.setPower(CLIPMOTORPOWERDOWN);

                packet.put("Elevator bar parking","to the bottom");
                initialized = true;
            } // end of not initialized
            else {
                int elevatorPosition = elevatorMotor.getCurrentPosition();
                //boolean isFinished = (elevatorPosition < (CLIPMOTORHOME + 10) && (elevatorPosition > (CLIPMOTORHOME)))
                boolean isFinished = (elevatorPosition < (CLIPMOTORPARK + 5))
                        || (elapsedTime.seconds() > timeOutSeconds);
                packet.put("Elevator Position",elevatorPosition);
                if (isFinished) {
                    elevatorMotor.setPower(0);
                    //elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    //elevatorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    stillClippingPosition = false;
                    packet.put("Finished clip park less than 10",(elevatorPosition < (CLIPMOTORPARK + 10)));
                    packet.put("Finished clip park elevatorposition",elevatorPosition);
                }
            } // end of else already initialized
            packet.put("clipPark",initialized);
            return stillClippingPosition;
        } // end of run method for clipHome Action
    } // end of clipPark Action

    public Action ClipPark() {
        return new clipPark();
    }

    public class bucketPosition implements Action {
        private boolean initialized = false;
        private boolean stillClippingPosition = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                elapsedTime.reset();
                elevatorMotor.setTargetPosition(CLIPMOTOR_PREBUCKET);
                elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                elevatorMotor.setPower(CLIPMOTOR_POWERUP);
                packet.put("Elevator going to","bucket");
                initialized = true;
            } // end of not initialized
            else {
                int elevatorPosition = elevatorMotor.getCurrentPosition();
                boolean isFinished = (elevatorPosition > CLIPMOTOR_PREBUCKET - 10) || (elapsedTime.seconds() > timeOutSeconds);
                packet.put("Elevator Position",elevatorPosition);
                if (isFinished) {
                    //elevatorMotor.setPower(0);
                    //elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    //elevatorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    stillClippingPosition = false;
                    packet.put("Finished going to bucket",elevatorPosition);
                }
            } // end of else already initialized
            packet.put("bucketPosition",initialized);
            return stillClippingPosition;
        } // end of run method for bucketPosition Action
    } // end of bucketPosition Action

    public Action BucketPosition() {
        return new bucketPosition();
    }

    public class preTransferBlock implements Action {
        private boolean initialized = false;
        private boolean stillClippingPosition = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                elapsedTime.reset();
                bucketServo.setPosition(BUCKET_HOME);
                twistServo.setPosition(TWIST_TRANSFER);
                rotationServo.setPosition(ROTATION_TRANSFER);
                packet.put("PreTransferBlock","Block");
                initialized = true;
            } // end of not initialized
            else {
                boolean isFinished = (elapsedTime.seconds() > TIME_PRE_TRANSFER);
                packet.put("PreTransferBlockTime",isFinished);
                if (isFinished) {
                    stillClippingPosition = false;
                    packet.put("Finished PreTransferBlockTime",isFinished);
                }
            } // end of else already initialized
            packet.put("PreTransferBlockInitialized",initialized);
            return stillClippingPosition;
        } // end of run method for preTransferBlock Action
    } // end of preTransferBlock Action

    public Action PreTransferBlock() {
        return new preTransferBlock();
    }

    public class transferBlock implements Action {
        private boolean initialized = false;
        private boolean stillClippingPosition = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                elapsedTime.reset();
                clawServo.setPosition(CLAW_OPEN);
                packet.put("transferBlock","Open Claw");
                initialized = true;
            } // end of not initialized
            else {
                double transferBlockTime = 0.1;
                boolean isFinished = (elapsedTime.seconds() > transferBlockTime);
                packet.put("transferBlockTime",isFinished);
                if (isFinished) {
                    rotationServo.setPosition(ROTATION_NEUTRAL);
                    twistServo.setPosition(TWIST_PICKUP);
                    stillClippingPosition = false;
                    packet.put("Finished transferBlock",isFinished);
                }
            } // end of else already initialized
            packet.put("transferBlockInitialized",initialized);
            return stillClippingPosition;
        } // end of run method for transferBlock Action
    } // end of transferBlock Action

    public Action TransferBlock() {
        return new transferBlock();
    }

    public class dumpBucket implements Action {
        private boolean initialized = false;
        private boolean stillClippingPosition = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                elapsedTime.reset();
                bucketServo.setPosition(BUCKETIN);
                packet.put("dumpBucket","Bucket Servo In");
                initialized = true;
            } // end of not initialized
            else {
                boolean isFinished = (elapsedTime.seconds() > TIME_DUMP_BUCKET);
                packet.put("dumpBucket Time",isFinished);
                if (isFinished) {
                    bucketServo.setPosition(BUCKETHOME);
                    stillClippingPosition = false;
                    packet.put("Finished dumpBucket",isFinished);
                }
            } // end of else already initialized
            packet.put("Dump Bucket Initialized",initialized);
            return stillClippingPosition;
        } // end of run method for transferBlock Action
    } // end of transferBlock Action

    public Action DumpBucket() {
        return new dumpBucket();
    }

    public class pickupBlock implements Action {
        private boolean initialized = false;
        private boolean stillopen = true;
        private boolean stillClippingPosition = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                elapsedTime.reset();
                clawServo.setPosition(CLAW_OPEN);
                twistServo.setPosition(TWIST_PICKUP);
                rotationServo.setPosition(ROTATION_PICKUP);
                packet.put("PickupBlock","Block");
                initialized = true;
            } // end of not initialized
            else if (stillopen) { // initialized, but give the rotation servo time
                //double pickupBlockTime = 2;
                boolean isFinished = (elapsedTime.seconds() > TIME_PICKPUP_BLOCK);
                packet.put("PickupBlockTime",isFinished);
                if (isFinished) {
                    clawServo.setPosition(CLAW_PICKUP);
                    stillClippingPosition = false;
                    stillopen = false;
                    packet.put("Finished stillopen fo rotation; time to close claw",isFinished);
                }
            } else { // initialized, rotation servo done, give claw time to close
                //double closeClawTime = 3;
                boolean isFinished = (elapsedTime.seconds() > TIME_CLOSE_CLAW);
                if (isFinished) {
                    stillClippingPosition = false;
                    packet.put("Finished PickupBlockTime",isFinished);
                }

            }// end of else already initialized
            packet.put("PickupBlockInitialized",initialized);
            return stillClippingPosition;
        } // end of run method for pickupBlock Action
    } // end of pcikupBlock Action

    public Action PickupBlock() {
        return new pickupBlock();
    }

    public class hockeyStickOut implements Action {
        private boolean initialized = false;
        private boolean stillClippingPosition = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                elapsedTime.reset();
                hockeystickServo.setPosition(HOCKEYSTICK_OUT);
                packet.put("HockeyStickOut","Initialized to Out");
                initialized = true;
            } // end of not initialized
            else {
                boolean isFinished = (elapsedTime.seconds() > TIME_HOCKEY_STICK);
                packet.put("Hockey Stick Out Time",isFinished);
                if (isFinished) {
                    stillClippingPosition = false;
                    packet.put("Finished hockeyStick",isFinished);
                }
            } // end of else already initialized
            packet.put("Hockey Stick Initialized",initialized);
            return stillClippingPosition;
        } // end of run method for transferBlock Action
    } // end of transferBlock Action

    public Action HockeyStickOut() {
        return new hockeyStickOut();
    }

    public class hockeyStickIn implements Action {
        private boolean initialized = false;
        private boolean stillClippingPosition = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                elapsedTime.reset();
                hockeystickServo.setPosition(HOCKEYSTICK_INITIAL);
                packet.put("HockeyStickIn","Initialized to IN");
                initialized = true;
            } // end of not initialized
            else {
                boolean isFinished = (elapsedTime.seconds() > TIME_HOCKEY_STICK);
                packet.put("Hockey Stick In Time",isFinished);
                if (isFinished) {
                    stillClippingPosition = false;
                    packet.put("Finished hockeyStick IN",isFinished);
                }
            } // end of else already initialized
            packet.put("Hockey Stick IN Initialized",initialized);
            return stillClippingPosition;
        } // end of run method for transferBlock Action
    } // end of transferBlock Action

    public Action HockeyStickIn() {
        return new hockeyStickIn();
    }
} // end of elevatorAction
