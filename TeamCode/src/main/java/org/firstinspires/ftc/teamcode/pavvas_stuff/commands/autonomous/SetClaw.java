package org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Lift;

public class SetClaw implements Command {

    private Lift lift;
    private Lift.ClawState targetState;

    public SetClaw(Lift lift, Lift.ClawState targetState) {
        this.lift = lift;
        this.targetState = targetState;
    }

    @Override
    public void start() {

    }

    @Override
    public void periodic() {
        this.lift.setClawState(targetState);
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return this.lift.getClawState() == this.targetState;
    }
}
