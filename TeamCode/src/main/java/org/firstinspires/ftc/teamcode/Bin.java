package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Bin {
    private Servo binServo;
    HardwareMap hardwareMap;

    public final double TRASH = 0.12;
    public final double RECYCLE = 0.78;

    private boolean isTrash;

    public Bin(HardwareMap hwm) {
        this.hardwareMap = hwm;
        this.binServo = hardwareMap.get(Servo.class, "bin");

        this.binServo.setPosition(TRASH);
        this.isTrash = true;
    }

    public void trash() {
        this.binServo.setPosition(TRASH);
        this.isTrash = true;
    }

    public void recycle() {
        this.binServo.setPosition(RECYCLE);
        this.isTrash = false;
    }

    public void toggle() {
        if (this.isTrash) {
            this.recycle();
        } else {
            this.trash();
        }
    }
    
    public double getPosition() {
        return this.binServo.getPosition();
    }
}