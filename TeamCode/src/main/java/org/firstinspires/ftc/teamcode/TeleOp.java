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

            /*
             * Gripper controls. Mapping:
             * - Right bumper: Toggle open and close
             */

            if (gamepad1.right_bumper) {
                gripper.toggle();
            }

            /* 
             * Wrist rotation. Mapping:
             * - Dpad right: Rotate front
             * - Dpad left: Rotate back
             */ 

            if (gamepad1.dpad_right) {
                wrist.rotateFront();
            }
            else if (gamepad1.dpad_left) {
                wrist.rotateBack();
            }

            /*
             * Lift controls. Mapping:
             * - A: Ground 
             * - X: Low
             * - Y: Mid
             * - B: High
             * - Left bumper: Reset
             */
            
            if (gamepad1.a) {
                lift.move(Lift.GROUND);
            }
            else if (gamepad1.x) {
                lift.move(Lift.LOW);
            }
            else if (gamepad1.y) {
                lift.move(Lift.MID);
            }
            else if (gamepad1.b) {
                lift.move(Lift.HIGH);
            }
            else if (gamepad1.left_bumper) {
                lift.move(Lift.RESET);
            }
        }

        // Display Info
        telemetry.addData("Forward/Backward", "Right Trigger/ Left Trigger");
        telemetry.addData("Turn", "Left Joy Stick");
    }
}