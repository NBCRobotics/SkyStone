package org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Lift;

public class RunHorizontalSlideForTime implements Command {

    private ElapsedTime timer;

    private Lift lift;
    private double seconds;
    private Direction direction;

    public enum Direction {
        IN,
        OUT
    }

    public RunHorizontalSlideForTime(Lift lift, Direction direction, double seconds) {
        this.timer = new ElapsedTime();

        this.lift = lift;
        this.seconds = seconds;
        this.direction = direction;
    }

    @Override
    public void start() {
        this.timer.reset();

        switch (this.direction) {
            default:
            case OUT:
                this.lift.setHorizontalSlidePower(0.0);
                break;
            case IN:
                this.lift.setHorizontalSlidePower(1.0);
                break;
        }
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {
        this.lift.setHorizontalSlidePower(0.5);
    }

    @Override
    public boolean isCompleted() {
        return this.timer.seconds() > seconds;
    }
}
