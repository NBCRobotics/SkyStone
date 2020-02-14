package org.firstinspires.ftc.teamcode.pavvas_stuff.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hooks implements Subsystem {

    private HardwareMap hardwareMap;

    private Servo leftHook;
    private Servo rightHook;

    public Hooks(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {

    }

    @Override
    public void periodic() {

    }
}
