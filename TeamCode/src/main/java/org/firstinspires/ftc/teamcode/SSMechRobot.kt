package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.*
import kotlin.math.abs
import kotlin.math.max
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.openftc.revextensions2.RevBulkData

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
    var vSlide: DcMotor? = null
    var hSlide: Servo? = null
    var claw: Servo? = null
    var rightHook: Servo? = null
    var leftHook: Servo? = null
    var touch: DigitalChannel? = null
    val clawPinchPos = 0.40
    var slowDown = 1.85//default


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
        vSlide = ahwdMap.dcMotor.get("vSlide")
        hSlide = ahwdMap.servo.get("hSlide")
        claw = ahwdMap.servo.get("claw")
        leftHook = ahwdMap.servo.get("leftHook")
        rightHook = ahwdMap.servo.get("rightHook")
        touch = ahwdMap.digitalChannel.get("touch")

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


        bLDrive?.power = 0.0
        bRDrive?.power = 0.0
        fLDrive?.power = 0.0
        fRDrive?.power = 0.0
        bLDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        bRDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        vSlide?.mode = DcMotor.RunMode.RUN_USING_ENCODER //Use encoders for linear slide motor
    }

    //METHODS
    /**
     * Controls the left 2 motors at the same power
     *
     * @param pow the power assigned to both motors, flipped due to the joystick readings
     */
    fun leftPow(pow: Double){
        bLDrive?.power = -pow
        fLDrive?.power = -pow
    }

    /**
     * Controls the right 2 motors at the same power
     *
     * @param pow the power assigned to both motors, flipped due to the joystick readings
     */
    fun rightPow(pow: Double){
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
        rightPow(rightM * 1.5)
    }

    /**
     * Overloads the previous method by having both the side set to the same power
     *
     * @param pow the power assigned to all motors
     */
    fun drive(pow: Double)
    {
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

    /**
     * Controls the foundation hooks. By holding a, the foundation hooks drop to a set position
     *
     * @param gp the gamepad used to control the hooks
     */
    fun dropHook(gp: Gamepad)
        {
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
        if(gp.a) { //hook down
            this.leftHook?.position = 0.7
            this.rightHook?.position = 0.72
        }
        else { //default position
            this.leftHook?.position = 0.18
            this.rightHook?.position = 0.21
        }

    }

    /**
     * Controls the claw used for grabbing stones by spinning the drive servo and translating the linear gear.
     * Currently NOT in use.
     *
     * @param gp the gamepad used to control the hooks
     */
    fun clamp(gp: Gamepad) //Controls claw for grabbing stones
    {
        if(gp.a) this.claw?.position = 0.55
        if(gp.b) this.claw?.position = 0.45
    }

    /**
     * Rotates the claw used for grabbing stones. Currently NOT in use.
     *
     * @param gp the gamepad used to control the hooks
     */
    fun pinch(gp: Gamepad) { //Controls claw for grabbing stones
        if(gp.left_bumper) { //hook down
            this.claw?.position = 0.00
        }
        else { //default position
            this.claw?.position = clawPinchPos
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
}

