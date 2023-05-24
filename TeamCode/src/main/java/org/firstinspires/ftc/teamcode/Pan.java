package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Pan {
    private DcMotor panMotor;
    HardwareMap hardwareMap;

    public final double TICKS_PER_ROTATION = 537.7;
    public final double GROUND = -25;
    public final double DUMP_POSITION = GROUND - TICKS_PER_ROTATION / 4;

    private boolean isGround;

    public Pan(HardwareMap hwm) {
        this.hardwareMap = hwm;
        this.panMotor = hardwareMap.get(DcMotorEx.class, "broom");
        this.panMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.panMotor.ground();
        this.isGround = true;

        // telemetry.addData("Status", "Broom Motor Initialized");
        // telemetry.update();
    }

    public void move(double pos) {
        this.panMotor.setTargetPosition((int) Math.round(pos));
        this.panMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.panMotor.setPower(0.05);
    }

    public void gound() {
        this.move(GROUND);
        this.isGround = true;
    }

    public void dump() {
        this.move(DUMP_POSITION);
        this.isGround = false;
    }

    public void toggle() {
        if (this.isGround) {
            this.dump();
        } else {
            this.ground();
        }
    }
    
    public void stop() {
        this.panMotor.setPower(0);
    }

    public double getPosition() {
        return this.panMotor.getCurrentPosition();
    }

    public boolean isBusy() {
        return this.panMotor.isBusy();
    }

}
