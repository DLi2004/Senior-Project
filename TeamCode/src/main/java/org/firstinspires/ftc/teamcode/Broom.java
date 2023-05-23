package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Broom {
    private Servo broomServo;
    HardwareMap hardwareMap;

    public final double CLOSED = 0.3;
    public final double OPEN = 0;

    private boolean isOpen;

    public Broom(HardwareMap hwm) {
        this.hardwareMap = hwm;
        this.broomServo = hardwareMap.get(Servo.class, "broom");

        this.broomServo.close();
        this.isOpen = false;
    }

    public void close() {
        this.broomServo.setPosition(CLOSED);
        this.isOpen = false;
    }

    public void open() {
        this.broomServo.setPosition(OPEN);
        this.isOpen = true;
    }

    public void toggle() {
        if (this.isOpen) {
            this.open();
        } else {
            this.close();
        }
    }
}
