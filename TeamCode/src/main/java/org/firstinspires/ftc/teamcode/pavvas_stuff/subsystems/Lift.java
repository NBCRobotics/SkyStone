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
    private double horizontalSlidePosition = 0.0;

    private static final int LIFT_MAX_HEIGHT = 10740;

    public Lift(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public enum ClawState {
        OPEN(0.4),
        CLOSED(0.0);

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

        this.liftMotor.setDirection(DcMotor.Direction.REVERSE);
        this.claw.setDirection(Servo.Direction.FORWARD);
        this.horizontalSlide.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        liftMotor.setPower(this.liftMotorPower);
        horizontalSlide.setPosition(this.horizontalSlidePosition);
        claw.setPosition(this.clawState.getPosition());
    }

    public void setHorizontalSlidePosition(double input) {
        boolean touched = !touch.getState();
        input /= 2;
        input += 0.5;

        if (touched) {
            this.horizontalSlidePosition = (input > 0.5) ? input : 0.5;
        } else {
            this.horizontalSlidePosition = input;
        }
    }

    public void setLiftMotorPower(double input) {
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
}
