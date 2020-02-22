package org.firstinspires.ftc.teamcode.autonomous
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.SSMechRobot


/**
 * Created by KasaiYuki on 9/21/2018.
 */
@Autonomous(group = "Autonomous")
//@Disabled
class SSNyoomPark : LinearOpMode()
{
    val robot = SSMechRobot()

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        telemetry.addData("Status: ", "Autonomous Initialized")
        telemetry.update()
        robot.init(hardwareMap)
        waitForStart()

        sleep(25000)
        robot.tapeMeasure?.power = 1.0
        sleep(250)
        robot.stop()

        //https://www.reddit.com/r/FTC/comments/78l5o0/how_to_program_encoders/
        telemetry.addData("Status: ", "Autonomous Terminated")
        telemetry.update()
    }
}
