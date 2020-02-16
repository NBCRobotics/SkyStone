package org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems;

import com.arcrobotics.ftclib.hardware.RevIMU;
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

    private RevIMU imu;

    private double backLeftPower = 0.0;
    private double backRightPower = 0.0;
    private double frontLeftPower = 0.0;
    private double frontRightPower = 0.0;

    public Drive(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.imu.reset();
    }

    public void setPower(double backLeftPower, double backRightPower, double frontLeftPower, double frontRightPower) {
        this.backLeftPower = backLeftPower;
        this.backRightPower = backRightPower;
        this.frontLeftPower = frontLeftPower;
        this.frontRightPower = frontRightPower;
    }

    public double getHeading() {
        return this.imu.getHeading();
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

        this.imu = new RevIMU(this.hardwareMap, "imu");
    }

    @Override
    public void periodic() {
        this.backLeftMotor.setPower(backLeftPower);
        this.backRightMotor.setPower(backRightPower);
        this.frontLeftMotor.setPower(frontLeftPower);
        this.frontRightMotor.setPower(frontRightPower);
    }
}
