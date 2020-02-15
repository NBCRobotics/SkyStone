package org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Drive;

public class DriveWithDirectionForTime implements Command {

    private ElapsedTime timer;

    private Drive drive;
    private Direction direction;
    private double seconds;
    private double backLeftPower, backRightPower, frontLeftPower, frontRightPower;

    /**
     *
     * @param drive The drivebase to move. This should come from a field in the OpMode.
     * @param direction Direction in which to move the robot. See {@link Direction Direction} for possible directions
     * @param seconds Amount of time in seconds to run this Command for
     * @param power Power value to set the motors to. Range [0, 1]. This Command will take care of
     *              setting the appropriate directions.
     */
    public DriveWithDirectionForTime(Drive drive, Direction direction, double seconds, double power) {
        this.timer = new ElapsedTime();

        this.drive = drive;
        this.direction = direction;
        this.seconds = seconds;

        switch (this.direction) {
            case FORWARD:
                this.backLeftPower = power;
                this.backRightPower = power;
                this.frontLeftPower = power;
                this.frontRightPower = power;
                break;
            case BACKWARD:
                this.backLeftPower = -power;
                this.backRightPower = -power;
                this.frontLeftPower = -power;
                this.frontRightPower = -power;
                break;
            case STRAFE_LEFT:
                this.backLeftPower = power;
                this.backRightPower = -power;
                this.frontLeftPower = -power;
                this.frontRightPower = power;
                break;
            case STRAFE_RIGHT:
                this.backLeftPower = -power;
                this.backRightPower = power;
                this.frontLeftPower = power;
                this.frontRightPower = -power;
                break;
            case TURN_LEFT:
                this.backLeftPower = -power;
                this.backRightPower = power;
                this.frontLeftPower = -power;
                this.frontRightPower = power;
                break;
            case TURN_RIGHT:
                this.backLeftPower = power;
                this.backRightPower = -power;
                this.frontLeftPower = power;
                this.frontRightPower = -power;
                break;
            case DIAGONAL_LEFT_FORWARD:
                this.backLeftPower = power;
                this.backRightPower = 0.0;
                this.frontLeftPower = 0.0;
                this.frontRightPower = power;
                break;
            case DIAGONAL_RIGHT_FORWARD:
                this.backLeftPower = 0.0;
                this.backRightPower = power;
                this.frontLeftPower = power;
                this.frontRightPower = 0.0;
                break;
            case DIAGONAL_LEFT_BACKWARD:
                this.backLeftPower = 0.0;
                this.backRightPower = -power;
                this.frontLeftPower = -power;
                this.frontRightPower = 0.0;
                break;
            case DIAGONAL_RIGHT_BACKWARD:
                this.backLeftPower = -power;
                this.backRightPower = 0.0;
                this.frontLeftPower = 0.0;
                this.frontRightPower = -power;
                break;
        }
    }

    public enum Direction {
        FORWARD,
        BACKWARD,
        STRAFE_LEFT,
        STRAFE_RIGHT,
        TURN_LEFT,
        TURN_RIGHT,
        DIAGONAL_LEFT_FORWARD,
        DIAGONAL_RIGHT_FORWARD,
        DIAGONAL_LEFT_BACKWARD,
        DIAGONAL_RIGHT_BACKWARD
    }

    @Override
    public void start() {
        this.timer.reset();
        this.drive.setPower(backLeftPower, backRightPower, frontLeftPower, frontRightPower);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {
        drive.setPower(0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public boolean isCompleted() {
        return timer.seconds() > seconds;
    }
}
