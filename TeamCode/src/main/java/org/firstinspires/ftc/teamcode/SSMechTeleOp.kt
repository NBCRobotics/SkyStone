package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.Range
import kotlin.math.abs
import kotlin.math.max

/**
 * Created by KasaiYuki on 9/20/2018.
 */

@TeleOp(name = "SSMechTeleOp", group = "TeleOp")
//@Disabled
class SSMechTeleOp : OpMode() {
    //using robot class for motors, servos etc
    val robot = SSMechRobot()
    val zero = 0.0.toFloat()
    var slowDown = 1.85//default
    var tooHigh = true //if v slide is too high
    var tooLow = true //if v slide is too low
    var touched = false //if touch sensor is pressed
    var slideP = 0.5 //h slide postion
    var linSlidePow: Float = 0.00.toFloat() //v slide power
    var curPos = 0
    var leftPower: Float = 0.0.toFloat()
    var rightPower: Float = 0.0.toFloat()
    val max = 6990
    var strafePow: Double = 0.00
    var drive = 0.0
    var turn = 0.0


    override fun init() {
        telemetry.addData("Status: ", "TeleOp Initialized")
        telemetry.update()
        //initializes all parts
        robot.init(hardwareMap)
        robot.vSlide?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER //Use encoders for linear slide motor
        curPos = robot.vSlide!!.currentPosition

    }

    override fun start() { //runs once when play button is pushed
        robot.vSlide?.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robot.leftHook?.position = 0.0
        robot.rightHook?.position = 0.0
    }

    override fun loop() {

        /**
         * Gamepad1: Tank Drive-Left Stick y=Left Motor; right stick y=Right Motor
         * Gamepad2: Crane: Right Stick Y = Y Slide; Left Bumper = pinch claw; Left Stick Y= X Slide; a = hook
         */

        touched = !robot.touch!!.state //true if not pressed

        //Vertical Slide Power Calculation
        linSlidePow = gamepad2.right_stick_y //negative for up
        linSlidePow = when { //when pos is too low and stick is negative, do nothing; same for too high and positive
            tooLow and (linSlidePow > 0) -> 0.toFloat()
            tooHigh and (linSlidePow < 0) -> 0.toFloat()
            else -> gamepad2.right_stick_y
        }
        tooHigh = curPos >= max
        tooLow = curPos < 0
        //if (curPos > 1500) linSlidePow /= 1.2.toFloat() //slow slide if greater than value
        robot.vSlide?.power = -linSlidePow.toDouble()//controls vertical slide, flips sign

        // Horizontal slide calcs
        slideP = (gamepad2.left_stick_y.toDouble() / 2) + 0.5 // horizontal slide
        if (touched) { //toggle controls horizontal slide with the left stick of gp2
            if (slideP > 0.5) robot.hSlide?.position = slideP //1=back; 0=forward
            else robot.hSlide?.position = 0.5
        } else robot.hSlide?.position = slideP
        robot.pinch(gamepad2) //operates claw
        curPos = robot.vSlide!!.currentPosition
        robot.dropHook(gamepad2)

        if (touched) telemetry.addData("Touch Sensor:", "Activated")

        if (tooHigh) telemetry.addData("Linear Slide Y Error:", "MAX HEIGHT REACHED")

        if (tooLow) telemetry.addData("Linear Slide Y Error:", "MIN HEIGHT REACHED")

        if (gamepad1.left_bumper) telemetry.addData("Slowdown:", "Engaged!")

        telemetry.addData("Linear Slide V: $linSlidePow", "") //kotlin string templates

        telemetry.addData("Attachments:", "HSlide = ${robot.hSlide?.position?.toFloat()}, " +
                "Claw = ${robot.claw?.position?.toFloat()}, " +
                "VSlide = ${curPos.toFloat()}", "")

        telemetry.addData("GP: stick 1 = ${gamepad1.left_stick_x}, ${gamepad1.left_stick_y}; " +
                "stick 2 = ${gamepad1.right_stick_x}, ${gamepad1.right_stick_y}", "")
    }

    override fun stop() {
        robot.brake()
        telemetry.addData("Status: ", "TeleOp Terminated")
        telemetry.update()
    }

    fun povMode() {
        //POV Mode-left joystick=power(y power and strafing), right joystick=turning
        // Put powers in the range of -1 to 1 only if they aren't already (not
        // checking would cause us to always drive at full speed)

        slowDown = if (gamepad1.left_bumper) 3.0 else 1.00 //condensed if else


        var drive = (gamepad1.left_stick_y).toDouble()
        var turn = -gamepad1.left_stick_x.toDouble() * 1.5
        var strafe = -gamepad1.right_stick_x.toDouble()
        var nor = 0.0

        var frontLeftPower = (drive + turn + strafe)
        var backLeftPower = (drive - turn + strafe)
        var frontRightPower = (drive - turn - strafe)
        var backRightPower = (drive + turn - strafe)

        if (abs(frontLeftPower) > 1 || abs(backLeftPower) > 1 ||
                abs(frontRightPower) > 1 || abs(backRightPower) > 1) { //normalizing values to [-1.0,1.0]
            // Find the largest power
            nor = max(abs(frontLeftPower), abs(backLeftPower))
            nor = max(abs(frontRightPower), nor)
            nor = max(abs(backRightPower), nor)
        }
        // Divide everything by nor (it's positive so we don't need to worry
        // about signs)
        //need to compensate for difference in core hex and 40:1 motors
        robot.fLDrive?.power = frontLeftPower / slowDown / nor
        robot.bLDrive?.power = backLeftPower / slowDown / nor
        robot.fRDrive?.power = frontRightPower / slowDown / nor
        robot.bRDrive?.power = backRightPower / slowDown / nor
        telemetry.addData("front left: $frontLeftPower, front right: $frontRightPower, back left: $backLeftPower, back right: $backRightPower", "")

    }
}