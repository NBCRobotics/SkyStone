package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.*
import org.openftc.revextensions2.ExpansionHubEx
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow

/*
    TODO: Create interfaces that lead to abstract class and seperate auto and tele
    TODO: Add PROPER Documentation
 */


/**
 * Created by KasaiYuki on 9/25/2018.
 */
class SSMechRobot {

    var hwdMap: HardwareMap? = null
    var bLDrive: DcMotor? = null
    var bRDrive: DcMotor? = null
    var fLDrive: DcMotor? = null
    var fRDrive: DcMotor? = null
    var vSlide: DcMotorEx? = null
    var tapeMeasure: DcMotor? = null
    var hSlide: Servo? = null
    var claw: Servo? = null
    var rightHook: Servo? = null
    var leftHook: Servo? = null
    var capstoneGate: Servo? = null
    var touch: DigitalChannel? = null
    var hub2: ExpansionHubEx? = null
    val clawUpPos = 0.6
    var slowDown = 1.85//default
    var slideP = 0.5 //h slide postion
    val max = 5800
    var linSlidePow: Float = 0.00.toFloat() //v slide power
    var tooHigh = true //if v slide is too high
    var tooLow = true //if v slide is too low
    var touched = false //if touch sensor is pressed
    var curPos = 0

    val kP = 13.0
    val kI = 0.0
    val kD = 8.0
    val kF = 18.0

    val cvFirstPercent = 20.0
    val cvPercentSpace = 35.0
    val cvStoneWidth = 55.0
    val cvStoneHeight = 50.0

    var motF = DcMotorSimple.Direction.FORWARD
    var motR = DcMotorSimple.Direction.REVERSE
    var serR = Servo.Direction.REVERSE
    var serF = Servo.Direction.FORWARD

    fun init(ahwdMap: HardwareMap) {
        //hardware maping motors, servos, and sensors
        //TODO: use list and iterate over
        var dcList = mutableListOf<DcMotor>() //creating empty list for dc motor
        //increment for each var above
        //map for each dc
        //similar for servo and sensor
        hwdMap = ahwdMap
/*        var motorList = arrayListOf<DcMotor?>(bLDrive, bRDrive, fLDrive, fRDrive, vSlide)
        for (i in 0..motorList.size) {
            motorList[i] = ahwdMap.dcMotor.get(motorList[i].toString())
        }

        var servoList = arrayListOf<Servo?>(hSlide, claw, leftHook, rightHook)
        (0..servoList.size).forEach { i -> servoList[i] = ahwdMap.servo.get(servoList[i].toString())}*/

        bLDrive = ahwdMap.dcMotor.get("bLDrive")
        bRDrive = ahwdMap.dcMotor.get("bRDrive")
        fLDrive = ahwdMap.dcMotor.get("fLDrive")
        fRDrive = ahwdMap.dcMotor.get("fRDrive")
        vSlide = ahwdMap.dcMotor.get("vSlide") as DcMotorEx
        tapeMeasure = ahwdMap.dcMotor.get("tapeMeasure")
        hSlide = ahwdMap.servo.get("hSlide")
        claw = ahwdMap.servo.get("claw")
        leftHook = ahwdMap.servo.get("leftHook")
        rightHook = ahwdMap.servo.get("rightHook")
        capstoneGate = ahwdMap.servo.get("capstoneGate")
        touch = ahwdMap.digitalChannel.get("touch")
        hub2 = ahwdMap.get(ExpansionHubEx::class.java, "Expansion Hub 2")

        //Setting direction
        bLDrive?.direction = motF
        bRDrive?.direction = motR
        fLDrive?.direction = motF
        fRDrive?.direction = motR
        vSlide?.direction = motR
        hSlide?.direction = serR
        claw?.direction = serF
        rightHook?.direction = serR
        leftHook?.direction = serF
        capstoneGate?.direction = serF
        tapeMeasure?.direction = motF


        bLDrive?.power = 0.0
        bRDrive?.power = 0.0
        fLDrive?.power = 0.0
        fRDrive?.power = 0.0
        bLDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        bRDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        vSlide?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        vSlide?.mode = DcMotor.RunMode.RUN_USING_ENCODER //Use encoders for linear slide motor
        curPos = this.vSlide!!.currentPosition
        //vSlide?.targetPosition = vSlide!!.currentPosition
        vSlide?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        vSlide?.setVelocityPIDFCoefficients(kP, kI, kD, kF)
        this.capstoneGate?.position = 0.8
    }


    //METHODS
    /**
     * Controls the left 2 motors at the same power
     *
     * @param pow the power assigned to both motors, flipped due to the joystick readings
     */
    fun leftPow(pow: Double) {
        bLDrive?.power = -pow
        fLDrive?.power = -pow
    }

    /**
     * Controls the right 2 motors at the same power
     *
     * @param pow the power assigned to both motors, flipped due to the joystick readings
     */
    fun rightPow(pow: Double) {
        bRDrive?.power = -pow
        fRDrive?.power = -pow
    }

    /**
     * Causes the robot to strafe left or right, by having the back left motor and top right motor spin one way,
     * and the other two spin the other way. Slows down the back motors to compensate for differences in hardware
     *
     * @param pow the power assigned to all motors before compensation and flipping the direction
     */
    fun strafe(pow: Double) //Positive Value = Left Strafe || Negative Value = Right Strafe
    {
        bLDrive?.power = -pow / 1.11
        fLDrive?.power = pow
        bRDrive?.power = pow / 1.12
        fRDrive?.power = -pow
    }

    /**
     * Controls all 4 motors with each side having it's own power.
     * Right side needs compensation due to the unaligned center of mass
     *
     * @param leftM the power assigned to the left 2 motors
     *
     * @param rightM the power assigned to the right 2 motors
     */
    fun drive(leftM: Double, rightM: Double) { //used for turning
        leftPow(leftM)
        rightPow(rightM) // * 1.5)
    }

    /**
     * Overloads the previous method by having both the side set to the same power
     *
     * @param pow the power assigned to all motors
     */
    fun drive(pow: Double) {
        drive(pow, pow)
    }

    fun mechanumPOV(gp: Gamepad) {
        //POV Mode-left joystick=power(y power and strafing), right joystick=turning
        // Put powers in the range of -1 to 1 only if they aren't already (not
        // checking would cause us to always drive at full speed)

        slowDown = gp.left_trigger + 1.0 //Dynamic Slowdown
        //slowDown = if(gamepad1.left_bumper) 1.75 else 1.00 //condensed if else

        var drive = (gp.left_stick_y).toDouble()
        var turn = -gp.left_stick_x.toDouble() * 1.5
        var strafe = -gp.right_stick_x.toDouble()
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
        this.fLDrive?.power = (frontLeftPower / slowDown) * 1.05
        this.bLDrive?.power = (backLeftPower / slowDown) * 1.05 / 1.12
        this.fRDrive?.power = (frontRightPower / slowDown) * 1.05
        this.bRDrive?.power = (backRightPower / slowDown) * 1.05 / 1.12

    }

    /**
     * Sets all the motors' power to zero
     */
    fun brake() {
        this.drive(0.0)
    }

    fun stop() {
        this.brake()
        this.vSlide?.power = 0.0
        this.hSlide?.position = 0.5
        this.tapeMeasure?.power = 0.0


    }

    fun nyoomPark(gp: Gamepad) {
        when {
            gp.left_trigger > 0 -> this.tapeMeasure?.power = -1.0
            gp.right_trigger > 0 -> this.tapeMeasure?.power = 1.0
            else -> this.tapeMeasure?.power = 0.0
        }
    }

    fun hookDown()
    {
        this.leftHook?.position = 0.7
        this.rightHook?.position = 0.72
    }

    fun hookUp()
    {
        this.leftHook?.position = 0.18
        this.rightHook?.position = 0.21
    }

    /**
     * Controls the foundation hooks. By holding a, the foundation hooks drop to a set position
     *
     * @param gp the gamepad used to control the hooks
     */
    fun foundHooks(gp: Gamepad) {
/*        var down = false
        var changed = false

        if(gp.a and !changed) {
            if(!gp.a)  down = !down
            changed = true
        } else if(!gp.a) changed = false

        if(down) { //up
            this.leftHook?.position = 0.0
            this.rightHook?.position = 0.0
        }
        else { //down
            this.leftHook?.position = 0.7
            this.rightHook?.position = 0.7
        }*/
        if (gp.right_bumper) { //hook down
            hookDown()
        } else { //default position
            hookUp()
        }

    }

    fun capGate(gp: Gamepad)
    {
        if(gp.left_bumper)
            this.capstoneGate?.position = 0.5
        else
            this.capstoneGate?.position = 0.8
    }


    fun pause() {
        this.brake()
        Thread.sleep(500)
    }

    /**
     * Rotates the claw used for grabbing stones. Currently NOT in use.
     *
     * @param gp the gamepad used to control the hooks
     */
    fun rotateClaw(gp: Gamepad) { //Controls claw for grabbing stones
        if (gp.left_bumper) { //hook down
            this.claw?.position = 0.00
        } else { //default position
            this.claw?.position = clawUpPos
        }
        /*
        Toggle Function
         */
/*        var pushedBefore = false
        if(gp.left_bumper)
        {
            if (pushedBefore)
            {
                this.claw?.position = 0.28
                pushedBefore = false
            }
            else if(!pushedBefore)
            {
                this.claw?.position = 0.00
                pushedBefore = true
            }
        }*/
    }

    /**
     *  Horizontal slide Power Calculation
     */
    fun hSlideCalc(gp: Gamepad): Double {
        touched = !this.touch!!.state //controls the touch sensor limit switch-true if not pressed

        slideP = (gp.left_stick_y.toDouble() / 2) + 0.5 // converts [-1.0,1.0] range to [0, 1.0] where 1=back; 0.5=stop; 0=forward
        return if (touched) { // if the touch sensor is pushed
            if (slideP > 0.5) (slideP) // and if the left stick is pushed backward, then change nothing
            else (0.5) // and if the left stick is in any other position do nothing
        } else (slideP) // if the touch sensor is not pushed change nothing
    }

    fun vSlideCalc(gp: Gamepad): Double {
        /**
         * Vertical Slide Power Calculation
         */
        linSlidePow = -gp.right_stick_y //negative for up if positive
        linSlidePow = when {
            tooLow and (linSlidePow < 0) -> 0.toFloat()
            tooHigh and (linSlidePow > 0) -> 0.toFloat()
            else -> -gp.right_stick_y
        } //when pos is zero or below and stick reads positive, do nothing; same for being at atleast 'max' and negative stick
        tooHigh = curPos >= max
        tooLow = curPos < 0
        curPos = this.vSlide!!.currentPosition
        return linSlidePow.toDouble().pow(3)
/*        when {
            linSlidePow < 0 -> return ((linSlidePow.toDouble().pow(2))) //negative values must become positive-squaring does this
            linSlidePow > 0 -> return (-(linSlidePow.toDouble().pow(2))) //positive values must become negative
            else -> return (0.toDouble()) //if value is zero or null don't move slide
        }*/
        //controls vertical slide, flips sign and squares
        //Cubing power gives finer control near 0 and more speed closer to 1/max
        //Flipped sign as gamepads have opposite signs and squaring a negative would remove this
    }
}

