package org.firstinspires.ftc.teamcode.pavvas_stuff.opmodes;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous.DriveWithDirectionForTime;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous.RunHorizontalSlideForTime;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous.RunLiftToPosition;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous.RunParkerForTime;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous.SetClaw;
import org.firstinspires.ftc.teamcode.pavvas_stuff.commands.autonomous.SetHooks;
import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Drive;
import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Hooks;
import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Lift;
import org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems.Parker;
import org.firstinspires.ftc.teamcode.vision.OpenCVSkystoneDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(group = "CommanderOpModes")
public class CommanderBuildingZoneBlue extends LinearOpMode implements DogeOpMode {

    /*
    private static final OpenCvInternalCamera.CameraDirection CAMERA_DIRECTION = OpenCvInternalCamera.CameraDirection.BACK; // Which camera do we use?
    private static final OpenCvCameraRotation ROTATION = OpenCvCameraRotation.UPSIDE_DOWN; // How do we put our phone in?
    private static final int FRAME_WIDTH = 320;
    private static final int FRAME_HEIGHT = 240;

     */

    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive = new Drive(hardwareMap);
        Lift lift = new Lift(hardwareMap);
        Hooks hooks = new Hooks(hardwareMap);
        Parker parker = new Parker(hardwareMap);

        commander.registerSubsystem(drive);
        commander.registerSubsystem(hooks);
        commander.registerSubsystem(lift);
        commander.registerSubsystem(parker);
        commander.init();

        waitForStart();

        commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.FORWARD, 1, 1));
        commander.runCommand(new SetClaw(lift, Lift.ClawState.CLOSED));
    }
}
