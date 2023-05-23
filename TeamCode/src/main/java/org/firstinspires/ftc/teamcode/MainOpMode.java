package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import vision.ObjectDetector;

@TeleOp(name = "Main")
public class MainOpMode extends LinearOpMode {
    private DriveTrain driveTrain;
    private Pan pan;
    private Broom broom;
    private ObjectDetector objectDetector;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware map
        pan = new Pan(hardwareMap);
        broom = new Broom(hardwareMap);
        driveTrain = new DriveTrain(hardwareMap);

        // Initialize object detector
        objectDetector = new ObjectDetector();

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        objectDetector.initVuforia();
        objectDetector.initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (objectDetector.tfod != null) {
            objectDetector.tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            objectDetector.tfod.setZoom(2.5, 16.0/9.0);
        }

        waitForStart();
        if (isStopRequested()) return;
        
        telemetry.addData("opmode", "ACTIVATED");
        telemetry.update();
        
        // Pan positions
        double initPosition = -25;
        double ground = initPosition;
        double dumpPosition = initPosition - 537.7 / 4;
        
        pan.panMove(ground);
        
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
                        pan.panRaise(dumpPosition);
                        isGround = false;
                    } else {
                        pan.panLower(ground);
                        isGround = true;
                    }
                    yPressedLastTime = true;
                }
            } else {
                yPressedLastTime = false;
                if (pan.getCurrentPosition() < dumpPosition + 25 && pan.getCurrentPosition() > dumpPosition - 25) {
                    pan.panStop();
                } else if (pan.getCurrentPosition() < ground + 25 && pan.getCurrentPosition() > ground - 25) {
                    pan.panStop();
                }
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

            if (objectDetector.tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = objectDetector.tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());

                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    boolean isTrashDetected = false;
                    boolean isRecyclableDetected = false;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("Label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("Left, Top (%d)", i), "%.03f , %.03f",
                                            recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("Right, Bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        i++;

                        // check label to see if the camera now sees trash
                        if (recognition.getLabel().equals("Trash")) {
                            isTrashDetected = true;
                            telemetry.addData("Object Detected", "Trash");
                        } else {
                            isTrashDetected = false;
                        }
                        
                        // check label to see if the camera now sees a recyclable object
                        if (recognition.getLabel().equals("Cardboard") || recognition.getLabel().equals("Glass") || recognition.getLabel().equals("Metal") || recognition.getLabel().equals("Paper") || recognition.getLabel().equals("Plastic")) {                                               //  ** ADDED **
                            isRecyclableDetected = false;
                            telemetry.addData("Object Detected", recognition.getLabel());
                        } else {
                            isRecyclableDetected = true;
                        }
                    }
                    telemetry.update();
                }
            }

            if (objectDetector.tfod == null) {
                objectDetector.tfod.shutdown();
            }
        }
    }
}
