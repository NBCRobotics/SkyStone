package org.firstinspires.ftc.teamcode.pavvas_stuff.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Hooks;

public class TeleOpHooks implements Command {

    private Hooks hooks;
    private Gamepad gamepad;

    public TeleOpHooks(Hooks hooks, Gamepad gamepad) {
        this.hooks = hooks;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        this.hooks.setState(Hooks.HooksState.UP)
    }

    @Override
    public void periodic() {
        if (this.gamepad.a) {
            this.hooks.setState(Hooks.HooksState.DOWN);
        } else {
            this.hooks.setState(Hooks.HooksState.UP);
        }
    }

    @Override
    public void stop() {
        this.hooks.setState(Hooks.HooksState.UP);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
