package org.firstinspires.ftc.teamcode.pavvas_stuff.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Parker;

public class TeleOpParker implements Command {

    private Parker parker;
    private Gamepad gamepad;

    private double power = 0.0;

    public TeleOpParker(Parker parker, Gamepad gamepad) {
        this.parker = parker;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        this.parker.setPower(power);
    }

    @Override
    public void periodic() {
        this.power = this.gamepad.left_trigger - this.gamepad.right_trigger;
        this.parker.setPower(power);
    }

    @Override
    public void stop() {
        this.parker.setPower(0.0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
