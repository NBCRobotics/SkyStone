package org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Parker;

public class RunParkerForTime implements Command {

    private ElapsedTime timer;

    private Parker parker;
    private double seconds;

    public RunParkerForTime(Parker parker, double seconds) {
        this.timer = new ElapsedTime();

        this.parker = parker;
        this.seconds = seconds;
    }

    @Override
    public void start() {
        this.timer.reset();

    }

    @Override
    public void periodic() {
        this.parker.setPower(1.0);
    }

    @Override
    public void stop() {
        this.parker.setPower(0.0);
    }

    @Override
    public boolean isCompleted() {
        return this.timer.seconds() > seconds;
    }
}
