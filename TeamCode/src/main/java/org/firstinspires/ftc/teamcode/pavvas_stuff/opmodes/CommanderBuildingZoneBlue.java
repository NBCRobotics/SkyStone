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

        /*
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createInternalCamera(CAMERA_DIRECTION, cameraMonitorViewId);
        camera.openCameraDevice(); // Open a connection to the camera

        OpenCVSkystoneDetector skystoneDetector = new OpenCVSkystoneDetector(telemetry);
        camera.setPipeline(skystoneDetector);
        camera.startStreaming(FRAME_WIDTH, FRAME_HEIGHT, ROTATION);

        OpenCVSkystoneDetector.SkystonePosition skystonePosition = OpenCVSkystoneDetector.SkystonePosition.CENTER_STONE;

        while (!isStarted() && !isStopRequested()) {
            skystonePosition = skystoneDetector.getSkystonePosition();
            telemetry.addData("Skystone Position", skystonePosition);
            telemetry.update();
        }

         */

        waitForStart();

        // ----------------------------------------
        // ROBOT MOTION BEGINS
        // ----------------------------------------

        // telemetry.addData("Status: ", "Going for CENTER_STONE");
        // telemetry.update();

        // Move towards quarry, raise the lift and extends horizontal slide so we can grab the stone
        commander.runCommandsParallel(
                new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.STRAFE_LEFT, 1, 1),
                new RunLiftToPosition(lift, 100),
                new RunHorizontalSlideForTime(lift, RunHorizontalSlideForTime.Direction.OUT, 3)
        );

        // Turn and move so our claw is right above the stone
        commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.TURN_LEFT, 1, 1));
        commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.FORWARD, 1, 1));

        // Drop the lift and close the claw
        commander.runCommand(new RunLiftToPosition(lift, 10));
        commander.runCommand(new SetClaw(lift, Lift.ClawState.CLOSED));

        // Drive over to the Building Zone and get in position to drop the stone
        commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.BACKWARD, 0.5, 0.5));
        commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.STRAFE_LEFT, 5, 1));
        commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.FORWARD, 0.5, 0.5));

        // Move the foundation into the corner
        commander.runCommand(new SetHooks(hooks, Hooks.HooksState.DOWN));
        commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.BACKWARD, 1, 1));
        commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.TURN_LEFT, 1, 1));

        // Flatten the foundation against the corner and drop the stone
        commander.runCommandsParallel(
                new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.FORWARD, 1, 1),
                new SetClaw(lift, Lift.ClawState.OPEN),
                new RunLiftToPosition(lift, 100)
        );

        // Turn and shoot parker
        commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.TURN_RIGHT, 1, 1));
        commander.runCommandsParallel(
                new RunParkerForTime(parker, 7),
                new RunLiftToPosition(lift, 0)
        );

        requestOpModeStop();

        // ----------------------------------------
        // ROBOT MOTION ENDS
        // ----------------------------------------

        /*
        switch (skystonePosition) {
            default:
            case CENTER_STONE:
                telemetry.addData("Status: ", "Going for CENTER_STONE");
                telemetry.update();

                // Move towards quarry, raise the lift and extends horizontal slide so we can grab the stone
                commander.runCommandsParallel(
                        new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.STRAFE_LEFT, 1, 1),
                        new RunLiftToPosition(lift, 100),
                        new RunHorizontalSlideForTime(lift, RunHorizontalSlideForTime.Direction.OUT, 3)
                );

                // Turn and move so our claw is right above the stone
                commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.TURN_LEFT, 1, 1));
                commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.FORWARD, 1, 1));

                // Drop the lift and close the claw
                commander.runCommand(new RunLiftToPosition(lift, 10));
                commander.runCommand(new SetClaw(lift, Lift.ClawState.CLOSED));

                // Drive over to the Building Zone and get in position to drop the stone
                commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.BACKWARD, 0.5, 0.5));
                commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.STRAFE_LEFT, 5, 1));
                commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.FORWARD, 0.5, 0.5));

                // Move the foundation into the corner
                commander.runCommand(new SetHooks(hooks, Hooks.HooksState.DOWN));
                commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.BACKWARD, 1, 1));
                commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.TURN_LEFT, 1, 1));

                // Flatten the foundation against the corner and drop the stone
                commander.runCommandsParallel(
                        new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.FORWARD, 1, 1),
                        new SetClaw(lift, Lift.ClawState.OPEN),
                        new RunLiftToPosition(lift, 100)
                );

                // Turn and shoot parker
                commander.runCommand(new DriveWithDirectionForTime(drive, DriveWithDirectionForTime.Direction.TURN_RIGHT, 1, 1));
                commander.runCommand(new RunParkerForTime(parker, 7));
         */
    }
}
