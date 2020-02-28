package org.firstinspires.ftc.teamcode.pavvas_stuff.opmodes;

import com.disnodeteam.dogecommander.DogeCommander;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.teleop.TeleOpDrive;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.teleop.TeleOpHooks;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.teleop.TeleOpLift;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.teleop.TeleOpParker;
import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Drive;
import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Hooks;
import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Lift;
import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Parker;

@TeleOp(name = "CommanderTeleOp", group = "CommanderOpModes")
public class CommanderTeleOp extends LinearOpMode implements DogeOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive = new Drive(hardwareMap);
        Hooks hooks = new Hooks(hardwareMap);
        Lift lift = new Lift(hardwareMap);
        Parker parker = new Parker(hardwareMap);

        commander.registerSubsystem(drive);
        commander.registerSubsystem(hooks);
        commander.registerSubsystem(lift);
        commander.registerSubsystem(parker);
        commander.init();

        waitForStart();

        commander.runCommandsParallel(
                new TeleOpDrive(drive, gamepad1),
                new TeleOpHooks(hooks, gamepad2),
                new TeleOpLift(lift, gamepad2),
                new TeleOpParker(parker, gamepad2)
        );

        commander.stop();
    }
}
