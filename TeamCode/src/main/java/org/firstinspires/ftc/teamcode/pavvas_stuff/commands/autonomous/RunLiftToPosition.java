package org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Lift;

public class RunLiftToPosition implements Command {

    private Lift lift;
    private int position;

    public RunLiftToPosition(Lift lift, int position) {
        this.lift = lift;
        this.position = position;
    }

    @Override
    public void start() {
        this.lift.setLiftMotorPosition(position);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {
        this.lift.setLiftMotorPower(0.0);
    }

    @Override
    public boolean isCompleted() {
        return this.lift.getLiftMotorPosition() == this.position;
    }
}
