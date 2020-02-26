package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.teamcode.SSMechRobot
import org.openftc.revextensions2.ExpansionHubEx

/**
 * Created by KasaiYuki on 9/20/2019.
 */

@TeleOp(name = "SSMechTeleOp", group = "TeleOp")
//@Disabled
class SSMechTeleOp : OpMode() {
    //using robot class for motors, servos etc
    val robot = SSMechRobot()
    val zero = 0.0.toFloat()


    var leftPower: Float = 0.0.toFloat()
    var rightPower: Float = 0.0.toFloat()

    var drive = 0.0


    override fun init() {
        telemetry.addData("Status: ", "TeleOp Initialized")
        telemetry.update()
        //initializes all parts
        robot.init(hardwareMap)
        //robot.vSlide?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER //Use encoders for linear slide motor
    }

    override fun start() { //runs once when play button is pushed
        robot.leftHook?.position = 0.0
        robot.rightHook?.position = 0.0
    }

    override fun loop() {
        /**
         * Gamepad1: Mechanum Drive: Left Stick Y = power, Left Stick X = strafe, Right Stick X = turn; right bumper = Foundation hook; left bumper = capstone gate
         * Gamepad2: Right Stick Y = Y Slide; Left Bumper = pinch claw; Left Stick Y= X Slide; Triggers = tape measure;
         */

        robot.mechanumPOV(gamepad1) //Drive Power Calculation
        robot.vSlide?.power = robot.vSlideCalc(gamepad2)
        robot.hSlide?.position = robot.hSlideCalc(gamepad2)
        robot.rotateClaw(gamepad2) //operates claw
        robot.foundHooks(gamepad1) //operates foundation hooks
        robot.nyoomPark(gamepad2) //operates the tape measure
        robot.capGate(gamepad1) //operates the tape measure
        if(gamepad1.y)
            robot.turnAround()

        if (!robot.touch!!.state) telemetry.addData("Touch Sensor:", "Activated")

        if (robot.tooHigh) telemetry.addData("Linear Slide Y Error:", "MAX HEIGHT REACHED")

        if (robot.tooLow) telemetry.addData("Linear Slide Y Error:", "MIN HEIGHT REACHED")

        if (gamepad1.left_trigger > 0.0) telemetry.addData("Slowdown:", "Engaged at ${gamepad1.left_trigger}")


        telemetry.addData("Linear Slides", "V: ${robot.vSlide?.power}  ;  H: ${robot.hSlide?.position}") //kotlin string templates

        telemetry.addData("Vertical Linear Slide Encoder Position,", robot.curPos)

        telemetry.addData("Drive Motors:","front left: ${robot.fLDrive?.power}, front right: ${robot.fRDrive?.power}, " +
                "back left: ${robot.bLDrive?.power}, back right: ${robot.bRDrive?.power}")

        telemetry.addData("Attachments:", "Claw = ${robot.claw?.position?.toFloat()}, " +
                "Tape Measure = ${robot.tapeMeasure?.power}" +
                "Hooks: ${robot.leftHook?.position},${robot.rightHook?.position}" +
                "Capstone Gate = ${robot.capstoneGate?.position}")

        telemetry.addData("Gamepad Stick Vals (x,y):","Left Stick= ${gamepad1.left_stick_x}, ${gamepad1.left_stick_y}; " +
                "Right Stick = ${gamepad1.right_stick_x}, ${gamepad1.right_stick_y}")

        telemetry.addData("Battery Voltage: ", robot.hub2?.read12vMonitor(ExpansionHubEx.VoltageUnits.VOLTS))

        telemetry.addData("Current IMU Angle Header: ", robot.imu?.angularOrientation)

        telemetry.addData("Current Angle: ", robot.imu?.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES)?.firstAngle)
    }

    override fun stop() {
        robot.brake()
        robot.vSlide?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        telemetry.addData("Status: ", "TeleOp Terminated")
        telemetry.update()
    }

}
