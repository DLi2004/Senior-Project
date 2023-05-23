package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Main")
public class MainOpMode extends LinearOpMode {
    private DriveTrain driveTrain;
    private Pan pan;
    private Broom broom;

    public void runOpMode() throws InterruptedException {
        // Initialize hardware map
        pan = new Pan(hardwareMap);
        broom = new Broom(hardwareMap);
        driveTrain = new DriveTrain(hardwareMap);

        waitForStart();
        if (isStopRequested()) return;
        
        telemetry.addData("opmode", "ACTIVATED");
        telemetry.update();
        
        // Pan positions
        double initPosition = pan.getPanPosition();
        double ground = initPosition;
        double dumpPosition = initPosition + 537.7 / 4;
        
        // Pan position checkers
        boolean isGround = true;
        boolean yPressedLastTime = false;
        
        // Broom position checkers
        boolean open = true;
        boolean xPressedLastTime = false;
        
        while (opModeIsActive()) {
            /* 
             * Drive controls. Mapping:
             * - Right stick x: Twist
             * - Right trigger: Forward
             * - Left trigger: Backward
             */

            double twist = gamepad1.right_trigger - gamepad1.left_trigger;
            double forward = gamepad1.left_stick_y;
            double strafe = gamepad1.right_stick_x;

            driveTrain.drivePower(twist, forward, strafe);
            
            // Move pan if y button is pressed
            if (gamepad1.y) {
                telemetry.addData("y button", "pressed");
                if (!yPressedLastTime) {
                    if (isGround) {
                        pan.panMove(dumpPosition);
                        isGround = false;
                    } else {
                        pan.panMove(ground);
                        isGround = true;
                    }
                    
                    yPressedLastTime = true;
                }
            } else {
                yPressedLastTime = false;
            }
            
            // Move broom if x button is pressed
            if (gamepad1.x) {
                telemetry.addData("x button", "pressed");
                if (!xPressedLastTime) {
                    if (open) {
                        broom.close();
                        open = false;
                    } else {
                        broom.open();
                        open = true;
                    }
                }
                xPressedLastTime = true;
            } else {
                xPressedLastTime = false;
            }

            telemetry.addData("Pan Position", pan.getPanPosition());
            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }
}
