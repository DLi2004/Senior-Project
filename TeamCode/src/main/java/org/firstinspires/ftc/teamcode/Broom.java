package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Broom {
    private Servo broomServo;
    HardwareMap hardwareMap;

    private double delta = 0.01;

    public Broom(HardwareMap hwm) {
        this.hardwareMap = hwm;
        this.broomServo = hardwareMap.get(Servo.class, "broom");
    }

    public void close() {
        /*
         * Close broom normally
         */
        this.broomServo.setPosition(0.4);
    }

    public void closeSlow() {
        /*
         * Close broom by moving in small steps
         */
        double pos = this.broomServo.getPosition();
        if (pos > 0.11) {
            this.broomServo.setPosition(pos - this.delta);
        }
    }

    public void open() {
        /*
         * Open broom normally
         */
        this.broomServo.setPosition(0.1);
    }

    public void openSlow() {
        /*
         * Open broom by moving in small steps
         */
        double pos = this.broomServo.getPosition();
        if (pos < 0.39) {
            this.broomServo.setPosition(pos + this.delta);
        }
    }

    public boolean isOpen() {
        return this.broomServo.getPosition() > 0.39; // Add 0.1 buffer
    }

    public boolean isClosed() {
        return this.broomServo.getPosition() < 0.11; // Add 0.1 buffer
    }
    
    public double getPosition() {
        return this.broomServo.getPosition();
    }
}
