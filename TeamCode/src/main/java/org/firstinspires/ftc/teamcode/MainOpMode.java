package org.firstinspires.ftc.teamcode;

import java.util.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
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
    private Bin bin;
    private ObjectDetector objectDetector;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware map
        pan = new Pan(hardwareMap);
        broom = new Broom(hardwareMap);
        driveTrain = new DriveTrain(hardwareMap);
        bin = new Bin(hardwareMap);

        // Initialize object detector
        objectDetector = new ObjectDetector(hardwareMap);

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        objectDetector.initVuforia();
        // objectDetector.initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        // if (objectDetector.getTfod() != null) {
        //     objectDetector.getTfod().activate();

        //     // The TensorFlow software will scale the input images from the camera to a lower resolution.
        //     // This can result in lower detection accuracy at longer distances (> 55cm or 22").
        //     // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
        //     // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
        //     // should be set to the value of the images used to create the TensorFlow Object Detection model
        //     // (typically 16/9).
        //     objectDetector.getTfod().setZoom(2.5, 16.0/9.0);
        // }

        waitForStart();
        if (isStopRequested()) return;
        
        telemetry.addData("opmode", "ACTIVATED");
        telemetry.update();
        
        pan.panMove(ground);
        
        // Used to prevent multiple presses in debouncers
        boolean yPressedLastTime = false;
        boolean xPressedLastTime = false;
        boolean aPressedLastTime = false;
        
        // Padding for the pan
        double panPadding = 25;

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
            
            /*
             * Debouncers for the pan, broom, and bin
             */
            
            /*
             * Pan controls. Mapping:
             * - Y button: Toggle between ground and dump positions
             */

            if (gamepad1.y) {
                telemetry.addData("y button", "pressed");
                if (!yPressedLastTime) {
                    pan.toggle();
                    yPressedLastTime = true;
                }
            } else {
                yPressedLastTime = false;
                if (pan.getPosition() < pan.DUMP_POSITION + panPadding && pan.getPosition() > pan.DUMP_POSITION - panPadding) {
                    pan.stop();
                } else if (pan.getPosition() < pan.GROUND + panPadding && pan.getPosition() > pan.GROUND - panPadding) {
                    pan.stop();
                }
            }

            /*
             * Broom controls. Mapping:
             * - X button: Toggle between open and closed
             */
            
            if (gamepad1.x) {
                telemetry.addData("x button", "pressed");
                if (!xPressedLastTime) {
                    broom.toggle();
                }
                xPressedLastTime = true;
            } else {
                xPressedLastTime = false;
            }

            /*
             * Bin controls. Mapping:
             * - A button: Toggle between trash and recycle
             */

            if (gamepad1.a) {
                telemetry.addData("a button", "pressed");
                if (!aPressedLastTime) {
                    bin.toggle();
                }
                aPressedLastTime = true;
            } else {
                aPressedLastTime = false;
            }


            telemetry.addData("Pan Position", pan.getPanPosition());
            telemetry.addData("Status", "Running");

            // if (objectDetector.getTfod() != null) {
            //     // getUpdatedRecognitions() will return null if no new information is available since
            //     // the last time that call was made.
            //     List<Recognition> updatedRecognitions = objectDetector.getTfod().getUpdatedRecognitions();
            //     if (updatedRecognitions != null) {
            //         telemetry.addData("# Object Detected", updatedRecognitions.size());

            //         // step through the list of recognitions and display boundary info.
            //         int i = 0;
            //         boolean isTrashDetected = false;
            //         boolean isRecyclableDetected = false;
            //         for (Recognition recognition : updatedRecognitions) {
            //             telemetry.addData(String.format("Label (%d)", i), recognition.getLabel());
            //             telemetry.addData(String.format("Left, Top (%d)", i), "%.03f , %.03f",
            //                                 recognition.getLeft(), recognition.getTop());
            //             telemetry.addData(String.format("Right, Bottom (%d)", i), "%.03f , %.03f",
            //                     recognition.getRight(), recognition.getBottom());
            //             i++;

            //             // check label to see if the camera now sees trash
            //             if (recognition.getLabel().equals("Trash")) {
            //                 isTrashDetected = true;
            //                 telemetry.addData("Object Detected", "Trash");
            //             } else {
            //                 isTrashDetected = false;
            //             }
                        
            //             // check label to see if the camera now sees a recyclable object
            //             if (recognition.getLabel().equals("Cardboard") || recognition.getLabel().equals("Glass") || recognition.getLabel().equals("Metal") || recognition.getLabel().equals("Paper") || recognition.getLabel().equals("Plastic")) {                                               //  ** ADDED **
            //                 isRecyclableDetected = false;
            //                 telemetry.addData("Object Detected", recognition.getLabel());
            //             } else {
            //                 isRecyclableDetected = true;
            //             }
            //         }
            //         telemetry.update();
            //     }
            // }

            // if (objectDetector.getTfod() == null) {
            //     objectDetector.getTfod().shutdown();
            // }
        }
    }
}
