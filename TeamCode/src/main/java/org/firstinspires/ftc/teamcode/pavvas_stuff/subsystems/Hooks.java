package org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hooks implements Subsystem {

    private HardwareMap hardwareMap;

    private Servo leftHook;
    private Servo rightHook;

    private HooksState state = HooksState.UP;

    public Hooks(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public enum HooksState {
        UP(0.2),
        DOWN(0.7);

        private final double position;

        private HooksState(double position) {
            this.position = position;
        }

        public double getPosition() {
            return this.position;
        }
    }

    public void setState(HooksState state) {
        this.state = state;
    }

    public HooksState getState() {
        return this.state;
    }

    @Override
    public void initHardware() {
        this.leftHook = hardwareMap.get(Servo.class, "leftHook");
        this.rightHook = hardwareMap.get(Servo.class, "rightHook");

        this.leftHook.setDirection(Servo.Direction.FORWARD);
        this.rightHook.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        leftHook.setPosition(this.state.getPosition());
        rightHook.setPosition(this.state.getPosition());
    }
}
