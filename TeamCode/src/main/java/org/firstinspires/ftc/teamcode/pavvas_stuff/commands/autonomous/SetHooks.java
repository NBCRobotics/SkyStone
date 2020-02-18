package org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Hooks;

public class SetHooks implements Command {

    private Hooks hooks;
    private Hooks.HooksState targetState;

    public SetHooks(Hooks hooks, Hooks.HooksState targetState) {
        this.hooks = hooks;
        this.targetState = targetState;
    }

    @Override
    public void start() {

    }

    @Override
    public void periodic() {
        this.hooks.setState(this.targetState);
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return this.hooks.getState() == this.targetState;
    }
}
