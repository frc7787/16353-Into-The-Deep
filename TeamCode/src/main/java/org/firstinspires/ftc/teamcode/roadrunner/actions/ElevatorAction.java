package org.firstinspires.ftc.teamcode.roadrunner.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ElevatorAction {
    public DcMotor elevatorMotor;
    private int CLIPMOTORBAR = 1850;
    private int CLIPPING = CLIPMOTORBAR - 600;
    private int CLIPMOTORHOME = 0;
    private double CLIPMOTORPOWER = 0.5;
    private ElapsedTime elapsedTime;
    private double timeOutSeconds;

    private RevTouchSensor clipTouchSensor;


    public ElevatorAction(HardwareMap hardwareMap){
        elevatorMotor = hardwareMap.get(DcMotor.class, "clipMotor");
        elevatorMotor.setDirection(DcMotor.Direction.REVERSE);

        clipTouchSensor = hardwareMap.get(RevTouchSensor.class, "clipTouchSensor");
        elapsedTime = new ElapsedTime();
        timeOutSeconds = 2.0;

        elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    } // end of constructor ElevatorAction

    public class clippingPosition implements Action {
        private boolean initialized = false;
        private boolean stillClippingPosition = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                elapsedTime.reset();
                elevatorMotor.setTargetPosition(CLIPMOTORBAR);
                elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                elevatorMotor.setPower(CLIPMOTORPOWER);
                packet.put("Elevator going to","bar");
                initialized = true;
            } // end of not initialized
            else {
                int elevatorPosition = elevatorMotor.getCurrentPosition();
                boolean isFinished = (elevatorPosition > CLIPMOTORBAR - 10) || (elapsedTime.seconds() > timeOutSeconds);
                packet.put("Elevator Position",elevatorPosition);
                if (isFinished) {
                    elevatorMotor.setPower(0);
                    //elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    //elevatorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    stillClippingPosition = false;
                    packet.put("Finished going to bar",elevatorPosition);
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
        private boolean initialized = false;
        private boolean stillClippingPosition = true;


        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                elapsedTime.reset();
                elevatorMotor.setTargetPosition(CLIPPING);
                elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                elevatorMotor.setPower(CLIPMOTORPOWER);

                packet.put("Elevator clipping","below bar");
                initialized = true;
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
                    stillClippingPosition = false;
                    packet.put("Finished clipping",elevatorPosition);
                }
            } // end of else already initialized
            packet.put("clipIt",stillClippingPosition);
            return stillClippingPosition;
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
                elevatorMotor.setPower(CLIPMOTORPOWER);

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
} // end of elevatorAction
