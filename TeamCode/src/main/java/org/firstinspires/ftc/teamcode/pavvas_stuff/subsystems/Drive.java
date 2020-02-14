package org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Subsystem abstraction of the drive chassis
 */
public class Drive implements Subsystem {

    private HardwareMap hardwareMap;

    private DcMotorEx backLeftMotor;
    private DcMotorEx backRightMotor;
    private DcMotorEx frontLeftMotor;
    private DcMotorEx frontRightMotor;

    private double backLeftPower;
    private double backRightPower;
    private double frontLeftPower;
    private double frontRightPower;

    public Drive(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void setPower(double backLeftPower, double backRightPower, double frontLeftPower, double frontRightPower) {
        this.backLeftPower = backLeftPower;
        this.backRightPower = backRightPower;
        this.frontLeftPower = frontLeftPower;
        this.frontRightPower = frontRightPower;
    }

    @Override
    public void initHardware() {
        this.backLeftMotor = (DcMotorEx) this.hardwareMap.get(DcMotor.class, "bLDrive");
        this.backRightMotor = (DcMotorEx) this.hardwareMap.get(DcMotor.class, "bRDrive");
        this.frontLeftMotor = (DcMotorEx) this.hardwareMap.get(DcMotor.class, "fLDrive");
        this.frontRightMotor = (DcMotorEx) this.hardwareMap.get(DcMotor.class, "fRDrive");

        this.backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        this.backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        this.frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        this.frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        this.backLeftMotor.setPower(backLeftPower);
        this.backRightMotor.setPower(backRightPower);
        this.frontLeftMotor.setPower(frontLeftPower);
        this.frontRightMotor.setPower(frontRightPower);
    }
}