package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Pan {
    private DcMotor panMotor;
    HardwareMap hardwareMap;

    public Pan(HardwareMap hwm) {
        this.hardwareMap = hwm;
        this.panMotor = hardwareMap.get(DcMotorEx.class, "pan");
        this.panMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void panMove(double pos) {
        this.panMotor.setTargetPosition((int) Math.round(pos));
        this.panMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.panMotor.setPower(0.05);
    }

    public void panStop() {
        this.panMotor.setPower(0);
    }

    public double getPanPosition() {
        return this.panMotor.getCurrentPosition();
    }
}