/*
 * FTC Team 22281 - SLAM MMDs 2022-2023 TeleOp
 */

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="drive")

public class Teleop extends LinearOpMode {
    private DriveTrain driveTrain = new DriveTrain(hardwareMap);

    @Override
    public void runOpMode() {
        waitForStart();

        while (opModeIsActive()) {
            /* 
             * Drive controls. Mapping:
             * - Right stick x: Twist
             * - Right trigger: Forward
             * - Left trigger: Backward
             */

            double twist = gamepad1.right_stick_x * 0.75;
            double forward = gamepad1.right_trigger - gamepad1.left_trigger;
            double strafe = 0;

            this.driveTrain.drivePower(twist, forward, strafe);
        }

        // Display Info
        telemetry.addData("Forward/Backward", "Right Trigger/ Left Trigger");
        telemetry.addData("Turn", "Left Joy Stick");
    }
}