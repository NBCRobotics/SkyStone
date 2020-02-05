package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.SSMechRobot

/**
 * Created by KasaiYuki on 9/20/2019.
 */

@TeleOp(name = "SSMechTeleOp", group = "TeleOp")
//@Disabled
class SSMechTeleOp : OpMode() {
    //using robot class for motors, servos etc
    val robot = SSMechRobot()
    val zero = 0.0.toFloat()
    var tooHigh = true //if v slide is too high
    var tooLow = true //if v slide is too low
    var touched = false //if touch sensor is pressed
    var slideP = 0.5 //h slide postion
    var linSlidePow: Float = 0.00.toFloat() //v slide power
    var curPos = 0
    var leftPower: Float = 0.0.toFloat()
    var rightPower: Float = 0.0.toFloat()
    val max = 10740 //10600
    var drive = 0.0


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

        robot.mechanumPOV(gamepad1)

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
        robot.vSlide?.power = -linSlidePow.toDouble() / 1//controls vertical slide, flips sign
        curPos = robot.vSlide!!.currentPosition

        // Horizontal slide calcs
        slideP = (gamepad2.left_stick_y.toDouble() / 2) + 0.5 // horizontal slide
        if (touched) { //toggle controls horizontal slide with the left stick of gp2
            if (slideP > 0.5) robot.hSlide?.position = slideP //1=back; 0=forward
            else robot.hSlide?.position = 0.5
        } else robot.hSlide?.position = slideP

        robot.pinch(gamepad2) //operates claw

        robot.dropHook(gamepad2) //operates hooks


        if (touched) telemetry.addData("Touch Sensor:", "Activated")

        if (tooHigh) telemetry.addData("Linear Slide Y Error:", "MAX HEIGHT REACHED")

        if (tooLow) telemetry.addData("Linear Slide Y Error:", "MIN HEIGHT REACHED")

        if (gamepad1.left_trigger > 0.0) telemetry.addData("Slowdown:", "Engaged!")

        telemetry.addData("Linear Slide V: $linSlidePow", "") //kotlin string templates

        telemetry.addData("Drive Motors:","front left: ${robot.fLDrive?.power}, front right: ${robot.fRDrive?.power}, " +
                "back left: ${robot.bLDrive?.power}, back right: ${robot.bRDrive?.power}")

        telemetry.addData("Attachments:", "HSlide = ${robot.hSlide?.position?.toFloat()}, " +
                "Claw = ${robot.claw?.position?.toFloat()}, " +
                "VSlide = ${curPos.toFloat()}", "")

        telemetry.addData("Gamepad Stick Vals:","Left Stick= ${gamepad1.left_stick_x}, ${gamepad1.left_stick_y}; " +
                "Right Stick = ${gamepad1.right_stick_x}, ${gamepad1.right_stick_y}")

        telemetry.addData("Hook: ${robot.leftHook?.position}", "")
    }

    override fun stop() {
        robot.brake()
        telemetry.addData("Status: ", "TeleOp Terminated")
        telemetry.update()
    }


    fun tankMode()
    {
        //slowDown = gamepad1.left_trigger + 2.0
        robot.slowDown = if(gamepad1.left_bumper) 2.35 else 1.00 //condensed if else


        //Tank Drive-sets power equal to numerical value of joystick positions
        leftPower = gamepad1.left_stick_y
        rightPower = gamepad1.right_stick_y
        robot.fLDrive?.power = leftPower.toDouble() / robot.slowDown
        robot.bLDrive?.power = leftPower.toDouble() / robot.slowDown
        robot.fRDrive?.power = rightPower.toDouble() / robot.slowDown
        robot.bRDrive?.power = rightPower.toDouble() / robot.slowDown
        telemetry.addData("front left: ${robot.fLDrive?.power}, front right: ${robot.fRDrive?.power}, " +
                "back left: ${robot.bLDrive?.power}, back right: ${robot.bRDrive?.power}", "")

    }


    /*
        Tank Mode 1: DPad control strafe, right joystick control power and turn
     */

    /*
        Tank Mode 2: Old Tank mode with triggers controlling strafe
     */

    /*
        Heli Mode: Left Joystick control power, right joystick controls turning, triggers control strafe
     */
}
