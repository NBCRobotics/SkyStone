package org.firstinspires.ftc.teamcode.pavvas_stuff.opmodes;

import com.disnodeteam.dogecommander.DogeCommander;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.disnodeteam.dogecommander.DogeOpMode;

import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous.DriveWithDirectionForTime;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous.RunHorizontalSlideForTime;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous.RunLiftToPosition;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous.SetClaw;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.teleop.TeleOpDrive;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.teleop.TeleOpHooks;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.teleop.TeleOpLift;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.teleop.TeleOpParker;
import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Drive;
import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Hooks;
import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Lift;
import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Parker;

@Autonomous(name = "CommanderAutoTest", group = "CommanderOpModes")
public class CommanderAutoTest extends LinearOpMode implements DogeOpMode {

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

        commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.FORWARD, 2, 1));
        commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.DIAGONAL_LEFT_BACKWARD, 1, 0.5));
        commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.TURN_RIGHT, 3, 1));

        commander.runCommandsParallel(
                new RunLiftToPosition(lift, 5000),
                new RunHorizontalSlideForTime(lift, RunHorizontalSlideForTime.Direction.OUT, 2),
                new SetClaw(lift, Lift.ClawState.CLOSED)
        );
        commander.stop();
    }
}
