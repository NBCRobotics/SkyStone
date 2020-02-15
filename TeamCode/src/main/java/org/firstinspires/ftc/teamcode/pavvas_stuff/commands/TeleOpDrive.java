package org.firstinspires.ftc.teamcode.pavvas_stuff.commands;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Drive;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class TeleOpDrive implements Command {

    private Drive drive;
    private Gamepad gamepad;

    public TeleOpDrive(Drive drive, Gamepad gamepad) {
        this.drive = drive;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        drive.setPower(0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public void periodic() {
        double slowDown = this.gamepad.left_trigger + 1.0;

        double dy = gamepad.left_stick_y;
        double dtheta = -gamepad.left_stick_x * 1.5;
        double dx = -gamepad.right_stick_x;

        double frontLeftPower = (dy + dtheta + dx) * 1.05;
        double backLeftPower = (dy - dtheta + dx) * 1.05 / 1.12;
        double frontRightPower = (dy - dtheta - dx) * 1.05;
        double backRightPower = (dy + dtheta - dx) * 1.05 / 1.12;

        double max = 0.0;

        if (abs(frontLeftPower) > 1 || abs(frontRightPower) > 1 || abs(backLeftPower) > 1 || abs(backRightPower) > 0) {
            max = max(abs(frontLeftPower), abs(frontRightPower));
            max = max(max, abs(backLeftPower));
            max = max(max, abs(backRightPower));
        }

        frontLeftPower /= max;
        backLeftPower /= max;
        frontRightPower /= max;
        backRightPower /= max;


        frontLeftPower /= slowDown;
        backLeftPower /= slowDown;
        frontRightPower /= slowDown;
        backRightPower /= slowDown;

        this.drive.setPower(backLeftPower, backRightPower, frontLeftPower, frontRightPower);
    }

    @Override
    public void stop() {
        this.drive.setPower(0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
