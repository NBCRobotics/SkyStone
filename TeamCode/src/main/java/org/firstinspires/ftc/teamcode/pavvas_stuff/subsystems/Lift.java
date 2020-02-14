package org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift implements Subsystem {

    private HardwareMap hardwareMap;

    private DcMotorEx liftMotor;

    private Servo claw;
    private Servo horizontalSlide;

    private ClawState state = ClawState.OPEN;

    private DigitalChannel touch;

    private double liftMotorPower;


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

    public void setState(ClawState state) {
        this.state = state;
    }

    @Override
    public void initHardware() {
        liftMotor = (DcMotorEx) hardwareMap.get(DcMotor.class, "vSlide");
        claw = hardwareMap.get(Servo.class, "claw");
        horizontalSlide = hardwareMap.get(Servo.class, "hSlide");
        touch = hardwareMap.get(DigitalChannel.class, "touch");

        liftMotor.setDirection(DcMotor.Direction.REVERSE);
        claw.setDirection(Servo.Direction.FORWARD);
        horizontalSlide.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void periodic() {

    }
}
