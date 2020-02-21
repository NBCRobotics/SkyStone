package org.firstinspires.ftc.teamcode.autonomous

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.SSMechRobot
import org.firstinspires.ftc.teamcode.vision.OpenCVSkystoneDetector
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvInternalCamera
import org.openftc.revextensions2.ExpansionHubEx

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
        robot.vSlide?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        //robot.vSlide?.mode = DcMotor.RunMode.RUN_TO_POSITION
        //robot.vSlide?.setPositionPIDFCoefficients(robot.kP)
        robot.vSlide?.targetPosition = robot.vSlide!!.currentPosition
        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        // Comment the below line and uncomment the next line to prevent sending camera view to the monitor
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(CAMERA_DIRECTION, cameraMonitorViewId)
        // camera = OpenCvCameraFactory.getInstance().createInternalCamera(CAMERA_DIRECTION);
        camera?.openCameraDevice() // Open a connection to the camera
        /*
        * Set the camera to send streamed image through the Skystone Detector vision pipeline and
        * begin streaming images. Image resolution is FRAME_WIDTH x FRAME_HEIGHT pixels.
        */
        skystoneDetector = OpenCVSkystoneDetector(20.0, 35.0, 50.0, 50.0, telemetry)
        camera?.setPipeline(skystoneDetector)
        camera?.startStreaming(FRAME_WIDTH, FRAME_HEIGHT, ROTATION)

        waitForStart()

        robot.leftHook?.position = 0.0
        robot.rightHook?.position = 0.0
        //robot.vSlide?.targetPosition = 50 + robot.vSlide!!.currentPosition
        telemetry.addData("Current Position", robot.vSlide!!.currentPosition)
        telemetry.update()
        sleep(200)

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
        moveToSkystone(750)
    }

    fun skyCenter()
    {
        moveToSkystone(0)
    }

    fun skyRight()
    {
        moveToSkystone(-750)
    }

    fun moveToSkystone(ofset: Long) //ofset adds strafe timing and recalculates time to strafe to foundation. positive values for left skystone
    {
        camera?.closeCameraDevice()
        robot.hSlide?.position = 0.0
        sleep(2000)
        robot.hSlide?.position = 0.5
        val batteryV = 0.0.toLong()
        if(!(robot.hub2!!.read12vMonitor(ExpansionHubEx.VoltageUnits.VOLTS) > 13.1)) {
            val batteryV = 13.2 - robot.hub2!!.read12vMonitor(ExpansionHubEx.VoltageUnits.VOLTS).toLong()
        }
        val bComp: Long = (batteryV * 5) //should equal to 200 when batteryV = 0.6
        robot.drive(-0.75,0.75)
        sleep(800)
        robot.pause()

        robot.drive(-0.75)
        sleep(500)
        robot.pause()

        robot.drive(1.0) //Drives Forward to the Stones
        sleep(500 + (bComp / 2))
        robot.brake()

        if(ofset > 0) { //skystone is on left
            robot.strafe(0.5)
            sleep(ofset)
        }
        if(ofset < 0) { //skystone is on right
            robot.strafe(-0.5)
            sleep(-ofset)
        }

        robot.drive(1.0) //Drives Forward to the Stones
        sleep(500 + (bComp / 2))
        robot.brake()

        robot.claw?.position = 0.0
        robot.pause()

        sleep(500)
        robot.drive(-1.0)
        sleep(600 + (bComp / 2))
        robot.pause()

        robot.strafe(1.0)//Heads to Foundation
        sleep(4500 - ofset)
        robot.pause()

        //robot.vSlide?.targetPosition = 1000 + robot.vSlide!!.currentPosition
        robot.vSlide?.power = 1.0
        sleep(600)
        robot.vSlide?.power = 0.0

        robot.hSlide?.position = 0.3
        sleep(550)
        robot.hSlide?.position = 0.5

        robot.drive(1.0)
        sleep(475)
        robot.pause()

        robot.claw?.position = robot.clawUpPos //drops stone
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


    fun moveFoundation()
    {
        robot.strafe(0.5)
        sleep(250)
        robot.pause()
        robot.hookDown()
        sleep(500)
        robot.drive(-1.0)
        sleep(1200)
        robot.pause()
        robot.drive(-1.0, 0.75)
        sleep(1500)
        robot.pause()
        robot.drive(0.75)
        sleep(866)
        robot.pause()
        robot.hookUp()
        //robot.vSlide?.targetPosition = robot.vSlide!!.currentPosition - 1000
        park()
    }

    fun park()
    {
        robot.drive(-1.0)
        sleep(1300)
        robot.pause()
        robot.drive(0.75, -0.75)
        sleep(700)
        robot.pause()
        robot.vSlide?.power = -1.0
        sleep(550)
        robot.vSlide?.power = 0.0
        robot.tapeMeasure?.power = 1.0
        sleep(700)
        robot.tapeMeasure?.power = 0.0
        robot.stop()
    }
}