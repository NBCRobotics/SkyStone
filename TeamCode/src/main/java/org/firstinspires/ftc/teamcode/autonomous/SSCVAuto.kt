package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.SSMechRobot
import org.firstinspires.ftc.teamcode.vision.OpenCVSkystoneDetector
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvInternalCamera

@Autonomous(name = "SSCVAuto", group = "examples")

class SSCVAuto : LinearOpMode() {
    private var skystoneDetector: OpenCVSkystoneDetector? = null
    private var camera: OpenCvCamera? = null
    val robot = SSMechRobot()

    @Throws(InterruptedException::class)
    override fun runOpMode() {

        /*
         * Instantiate an OpenCvCamera object for the camera we'll be using.
         * In this sample, we're using the phone's internal camera. We pass it a
         * CameraDirection enum indicating whether to use the front or back facing
         * camera, as well as the view that we wish to use for camera monitor (on
         * the RC phone). If no camera monitor is desired, use the alternate
         * single-parameter constructor instead (commented out below)
         */
        telemetry.addData("Status: ", "Autonomous Initialized")
        telemetry.update()
        robot.init(hardwareMap)
        //robot.vSlide?.mode = DcMotor.RunMode.RUN_TO_POSITION
        robot.vSlide?.targetPosition = robot.vSlide!!.currentPosition
        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        // Comment the below line and uncomment the next line to prevent sending camera view to the monitor
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(CAMERA_DIRECTION, cameraMonitorViewId)
        // camera = OpenCvCameraFactory.getInstance().createInternalCamera(CAMERA_DIRECTION);
        camera?.openCameraDevice() // Open a connection to the camera

        waitForStart()

        robot.leftHook?.position = 0.0
        robot.rightHook?.position = 0.0
        //robot.vSlide?.targetPosition = 50 + robot.vSlide!!.currentPosition


        /*
         * Set the camera to send streamed image through the Skystone Detector vision pipeline and
         * begin streaming images. Image resolution is FRAME_WIDTH x FRAME_HEIGHT pixels.
         */
        skystoneDetector = OpenCVSkystoneDetector(telemetry)
        camera?.setPipeline(skystoneDetector)
        camera?.startStreaming(FRAME_WIDTH, FRAME_HEIGHT, ROTATION)

        val pos = skystoneDetector?.skystonePosition ?: OpenCVSkystoneDetector.SkystonePosition.CENTER_STONE //Elvis Operator:
        // returns pos if not null, and left if null
        telemetry.addData("Skystone Position: ", pos) // This is the important one!
        telemetry.update()

        when (pos)
        {
            OpenCVSkystoneDetector.SkystonePosition.LEFT_STONE -> skyLeft()
            OpenCVSkystoneDetector.SkystonePosition.CENTER_STONE -> skyCenter()
            OpenCVSkystoneDetector.SkystonePosition.RIGHT_STONE -> skyRight()
        }
    }

/*    override fun init_loop() {
        telemetry.addData("Skystone Position: ", skystoneDetector!!.skystonePosition) // This is the important one!
        telemetry.update()
    }

    override fun loop() {
        telemetry.addData("Skystone Position: ", skystoneDetector!!.skystonePosition) // This is the important one!

        telemetry.update()
    }*/

    companion object {
        private val CAMERA_DIRECTION = OpenCvInternalCamera.CameraDirection.BACK // Which camera do we use?
        private val ROTATION = OpenCvCameraRotation.UPSIDE_DOWN // How do we put our phone in?
        private const val FRAME_WIDTH = 320
        private const val FRAME_HEIGHT = 240
    }

    fun skyLeft()
    {

    }

    fun skyCenter()
    {
        camera?.closeCameraDevice()
        robot.drive(-0.75,0.75)
        sleep(800)
        robot.pause()

        robot.drive(-0.75)
        sleep(500)
        robot.pause()

        robot.drive(0.50) //Drives Forward to the Stones
        sleep(2300)
        robot.pause()

        robot.claw?.position = 0.0
        robot.pause()

        sleep(500)
        robot.drive(-0.50)
        sleep(750)
        robot.pause()

        robot.strafe(1.0)//Heads to Foundation
        sleep(4500)
        robot.pause()

        robot.vSlide?.targetPosition = 1000 + robot.vSlide!!.currentPosition
        sleep(750)

        robot.hSlide?.position = 0.3
        sleep(550)

        robot.hSlide?.position = 0.5
        robot.drive(0.5)
        sleep(950)
        robot.pause()

        robot.claw?.position = robot.clawPinchPos //drops stone
        robot.pause()
        /*robot.drive(-0.5)
        sleep(1200)
        robot.pause()
        robot.vSlide?.targetPosition = robot.vSlide!!.currentPosition - 2500
        sleep(750)
        robot.pause()
        robot.hSlide?.position = 0.7
        sleep(550)
        robot.pause()
        robot.strafe(-0.5) //heads towards alliance bridge
        sleep(2100)
        robot.claw?.position = 0.0
        robot.pause()
        robot.strafe(-0.5)
        sleep(2400)
        robot.pause()*/
        moveFoundation()
    }

    fun skyRight()
    {

    }

    fun moveFoundation()
    {
        robot.vSlide?.targetPosition = robot.vSlide!!.currentPosition - 1000
        robot.strafe(0.5)
        sleep(250)
        robot.pause()
        robot.hookDown()
        sleep(500)
        robot.drive(-1.0)
        sleep(1000)
        robot.pause()
        robot.drive(-1.0, 0.75)
        sleep(1200)
        robot.pause()
        robot.drive(1.0)
        sleep(1300)
        robot.pause()
        robot.hookUp()
        park()
    }

    fun park()
    {
        robot.drive(-0.75)
        sleep(1500)
        robot.pause()
        robot.drive(0.75, -0.75)
        sleep(500)
        robot.pause()
        robot.tapeMeasure?.power = 0.75
        sleep(1000)
        robot.tapeMeasure?.power = 0.0
        robot.stop()
    }
}