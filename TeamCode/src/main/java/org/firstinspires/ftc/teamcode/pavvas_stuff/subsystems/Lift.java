package org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift implements Subsystem {

    private HardwareMap hardwareMap;

    private DcMotorEx liftMotor;

    private Servo claw;
    private Servo horizontalSlide;

    private ClawState clawState = ClawState.OPEN;

    private DigitalChannel touch;

    private double liftMotorPower = 0.0;
    private double horizontalSlidePower = 0.0;

    private DcMotor.RunMode runMode = DcMotor.RunMode.RUN_USING_ENCODER;

    private static final int LIFT_MAX_HEIGHT = 10740;
    private int position = 0;

    public Lift(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public enum ClawState {
        OPEN(0.0),
        CLOSED(0.4);

        private double position;

        private ClawState(double position) {
            this.position = position;
        }

        public double getPosition() {
            return this.position;
        }
    }

    public void setClawState(ClawState state) {
        this.clawState = state;
    }

    @Override
    public void initHardware() {
        this.liftMotor = (DcMotorEx) hardwareMap.get(DcMotor.class, "vSlide");
        this.claw = hardwareMap.get(Servo.class, "claw");
        this.horizontalSlide = hardwareMap.get(Servo.class, "hSlide");
        this.touch = hardwareMap.get(DigitalChannel.class, "touch");

        this.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.liftMotor.setDirection(DcMotor.Direction.REVERSE);
        this.claw.setDirection(Servo.Direction.FORWARD);
        this.horizontalSlide.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        this.liftMotor.setMode(runMode);

        if (runMode == DcMotor.RunMode.RUN_USING_ENCODER) {
            this.liftMotor.setPower(this.liftMotorPower);
        } else if (runMode == DcMotor.RunMode.RUN_TO_POSITION) {
            this.liftMotor.setTargetPosition(position);
        }
        this.horizontalSlide.setPosition(this.horizontalSlidePower);
        this.claw.setPosition(this.clawState.getPosition());
    }

    /**
     *
     * @param input Range [0.0, 1.0]. 0.5 means no motion. 0.0 means forward. 1.0 means back
     */
    public void setHorizontalSlidePower(double input) {
        boolean touched = !touch.getState();
        input /= 2;
        input += 0.5;

        if (touched) {
            this.horizontalSlidePower = (input > 0.5) ? input : 0.5;
        } else {
            this.horizontalSlidePower = input;
        }
    }

    public void setLiftMotorPower(double input) {
        this.runMode = DcMotor.RunMode.RUN_USING_ENCODER;
        input *= -1;
        boolean tooLow = this.liftMotor.getCurrentPosition() < 0;
        boolean tooHigh = this.liftMotor.getCurrentPosition() >= LIFT_MAX_HEIGHT;

        // Check if we are going to be exceeding the bounds of the lift
        if (input < 0 && tooLow) {
            this.liftMotorPower = 0;
        } else if (input > 0 && tooHigh) {
            this.liftMotorPower = 0;
        } else { // if we aren't we are good to go
            this.liftMotorPower = input;
        }

    }

    public void setLiftMotorPosition(int position) {
        this.runMode = DcMotor.RunMode.RUN_TO_POSITION;
        this.position = position;
    }

    public int getLiftMotorPosition() {
        return this.liftMotor.getCurrentPosition();
    }

    public ClawState getClawState() {
        return this.clawState;
    }
}
