package org.firstinspires.ftc.teamcode.pavvas_stuff.commands;

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

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
