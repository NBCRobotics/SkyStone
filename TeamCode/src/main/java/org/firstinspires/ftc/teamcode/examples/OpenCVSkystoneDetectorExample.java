package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.vision.OpenCVSkystoneDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@TeleOp
public class OpenCVSkystoneDetectorExample extends LinearOpMode {

    private OpenCVSkystoneDetector skystoneDetector;
    private OpenCvCamera camera;

    private static final OpenCvInternalCamera.CameraDirection CAMERA_DIRECTION = OpenCvInternalCamera.CameraDirection.BACK; // Which camera do we use?
    private static final OpenCvCameraRotation ROTATION = OpenCvCameraRotation.UPSIDE_DOWN; // How do we put our phone in?
    private static final int FRAME_WIDTH = 320;
    private static final int FRAME_HEIGHT = 240;

    @Override
    public void runOpMode() {
    while (!isStarted() && !isStopRequested()) {
        /*
         * Instantiate an OpenCvCamera object for the camera we'll be using.
         * In this sample, we're using the phone's internal camera. We pass it a
         * CameraDirection enum indicating whether to use the front or back facing
         * camera, as well as the view that we wish to use for camera monitor (on
         * the RC phone). If no camera monitor is desired, use the alternate
         * single-parameter constructor instead (commented out below)
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        // Comment the below line and uncomment the next line to prevent sending camera view to the monitor
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(CAMERA_DIRECTION, cameraMonitorViewId);
        // camera = OpenCvCameraFactory.getInstance().createInternalCamera(CAMERA_DIRECTION);

        camera.openCameraDevice(); // Open a connection to the camera

        /*
         * Set the camera to send streamed image through the Skystone Detector vision pipeline and
         * begin streaming images. Image resolution is FRAME_WIDTH x FRAME_HEIGHT pixels.
         */
        skystoneDetector = new OpenCVSkystoneDetector(FRAME_WIDTH, FRAME_HEIGHT);
        camera.setPipeline(skystoneDetector);
        camera.startStreaming(FRAME_WIDTH, FRAME_HEIGHT, ROTATION);

        // Alert user that we are ready for start!
        telemetry.addData("Status: ", "Initialized and Ready for Start!");
        telemetry.update();
    }

        waitForStart(); // Wait for the user to press Play

        while (opModeIsActive()) {
            telemetry.addData("Skystone Position: ", skystoneDetector.getSkystonePosition()); // This is the important one!
            telemetry.update();
        }
    }
}
