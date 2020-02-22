package org.firstinspires.ftc.teamcode.teleop;

//import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.SSMechRobot;
import org.intellij.lang.annotations.JdkConstants;

//@Config
@TeleOp (group = "tuning")
public class JavaTuningPIDF extends OpMode {

    private SSMechRobot robot;

    public static double kP = 13.0;
    public static double kI = 0.0;
    public static double kD = 8.0;
    public static double kF = 18.0;

    @Override
    public void init() {
        robot = new SSMechRobot();
        robot.init(hardwareMap);
        robot.getVSlide().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void start() {
        super.start();
        robot.getVSlide().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getLeftHook().setPosition(0.0);
        robot.getRightHook().setPosition(0.0);
        telemetry.addData("Status: ", "Starting");
        telemetry.update();
    }

    @Override
    public void loop() {
        robot.getVSlide().setPower(-gamepad2.right_stick_y);
        PIDFCoefficients coef = robot.getVSlide().getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("PIDF Coef: ", coef);
        telemetry.addData("Encoder Pos: ", robot.getVSlide().getCurrentPosition());
        if (gamepad2.a) {
            robot.getVSlide().setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(kP, kI, kD, kF));
        }
        telemetry.addData("Status: ", "Running");
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.brake();
        telemetry.addData("Status: ", "Terminated");
        telemetry.update();
    }
}
