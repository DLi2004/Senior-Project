package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.lang.reflect.Array;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Arrays;
import java.lang.Math;

@TeleOp(name="PanOpMode")
public class PanOpMode extends LinearOpMode {

    Robot robot = new Robot();

    DcMotor panMotor;

    double panPower;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize motors
        panMotor = hardwareMap.get(DcMotor.class, "broom");

        // Set modes for pan
        panMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        panMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        panMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Show the elapsed game time
            telemetry.addData("Run Time:", runtime.toString());

            int panPosition = armMotor.getCurrentPosition();
            if (gamepad2.a) {
                panPower = 1;
            } else {
                panPower = 0;
            }
            panMotor.setPower(panPower);
            telemetry.update();
        }
    }
}