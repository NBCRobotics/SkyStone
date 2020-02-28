package org.firstinspires.ftc.teamcode.pavvas_stuff.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Lift;

public class TeleOpLift implements Command {

    private Lift lift;
    private Gamepad gamepad;

    public TeleOpLift(Lift lift, Gamepad gamepad) {
        this.lift = lift;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        this.lift.setClawState(Lift.ClawState.OPEN);
        this.lift.setHorizontalSlidePower(0.0);
        this.lift.setLiftMotorPower(0.0);
    }

    @Override
    public void periodic() {
        if (this.gamepad.left_bumper) {
            this.lift.setClawState(Lift.ClawState.CLOSED);
        } else {
            this.lift.setClawState(Lift.ClawState.OPEN);
        }

        this.lift.setHorizontalSlidePower(gamepad.left_stick_y);
        this.lift.setLiftMotorPower(gamepad.right_stick_y);
    }

    @Override
    public void stop() {
        this.lift.setHorizontalSlidePower(0.0);
        this.lift.setLiftMotorPower(0.0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
