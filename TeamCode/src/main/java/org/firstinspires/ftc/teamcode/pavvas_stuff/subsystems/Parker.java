package org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Parker implements Subsystem {

    private HardwareMap hardwareMap;

    private DcMotorEx motor;

    private double power = 0.0;

    public Parker(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void setPower(double power) {
        this.power = power;
    }

    @Override
    public void initHardware() {
        this.motor = (DcMotorEx) this.hardwareMap.get(DcMotor.class, "tapeMeasure");
        this.motor.setDirection(DcMotor.Direction.FORWARD);
    }

    @Override
    public void periodic() {
        this.motor.setPower(this.power);
    }
}
