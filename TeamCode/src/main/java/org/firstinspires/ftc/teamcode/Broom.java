package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Broom {
    private Servo broomServo;
    HardwareMap hardwareMap;

    public Broom(HardwareMap hwm) {
        this.hardwareMap = hwm;
        this.broomServo = hardwareMap.get(Servo.class, "broom");
    }

    public void close() {
        this.broomServo.setPosition(0.3);
    }

    public void open() {
        this.broomServo.setPosition(0);
    }
}
