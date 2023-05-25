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

    private final int PAN_Y = 100;

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
        objectDetector.initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (objectDetector.getTfod() != null) {
            objectDetector.getTfod().activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            objectDetector.getTfod().setZoom(2.5, 16.0/9.0);
        }

        waitForStart();
        if (isStopRequested()) return;
        
        telemetry.addData("opmode", "ACTIVATED");
        telemetry.update();
        
        // Pan positions
        double initPosition = pan.getPanPosition();
        double ground = initPosition;
        double ninetyDegrees = 134.425;
        double factor = 85/90;
        double dumpPosition = initPosition - 126.96;
        
        pan.panMove(ground);
        
        // Pan position checkers
        boolean isGround = true;
        boolean yPressedLastTime = false;
        
        // Broom position checkers
        boolean open = false;
        boolean xPressedLastTime = false;
        
        // Bin position checkers
        boolean atTrash = true;
        boolean aPressedLastTime = false;
        
        while (opModeIsActive()) {
            /* 
             * Drive controls. Mapping:
             * - Right stick x: Twist
             * - Right trigger: Forward
             * - Left trigger: Backward
             */

            double twist = gamepad1.right_trigger - gamepad1.left_trigger;
            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.right_stick_x;

            driveTrain.drivePower(forward, twist, strafe);
            
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
                // if (pan.getPanPosition() < dumpPosition + 25 && pan.getPanPosition() > dumpPosition - 25) {
                //     pan.panStop();
                // } else if (pan.getPanPosition() < ground + 25 && pan.getPanPosition() > ground - 25) {
                //     pan.panStop();
                // }
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
            
            // Move bin if a button is pressed
            if (gamepad1.a) {
                telemetry.addData("a button", "pressed");
                if (!aPressedLastTime) {
                    if (!atTrash) {
                        bin.trash();
                        atTrash = true;
                    } else {
                        bin.recycle();
                        atTrash = false;
                    }
                }
                aPressedLastTime = true;
            } else {
                aPressedLastTime = false;
            }

            telemetry.addData("Pan Position", pan.getPanPosition());
            telemetry.addData("Broom position", broom.getPosition());
            telemetry.addData("Bin position", bin.getPosition());
            telemetry.addData("Status", "Running");
            telemetry.update();

            if (objectDetector.getTfod() != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = objectDetector.getTfod().getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());

                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    boolean isTrashDetected = false;
                    boolean isRecyclableDetected = false;
                    for (Recognition recognition : updatedRecognitions) {
                        String label = recognition.getLabel();
                        telemetry.addData(String.format("Label (%d)", i), label);
                        telemetry.addData(String.format("Left, Top (%d)", i), "%.03f , %.03f",
                                            recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("Right, Bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        i++;

                        // check label to see if the camera now sees trash
                        if (recognition.getLabel().equals("Trash")) {
                            isTrashDetected = true;
                        } else {
                            isTrashDetected = false;
                        }
                        
                        // check label to see if the camera now sees a recyclable object
                        if (label.equals("Cardboard") || label.equals("Glass") || label.equals("Metal") || label.equals("Paper") || label.equals("Plastic")) {
                            isRecyclableDetected = true;
                        } else {
                            isRecyclableDetected = false;
                        }

                        // if trash is detected and the bottom of the bounding box is at
                        // the pan's y position, move the pan up
                        if (isTrashDetected || isRecyclableDetected) {
                            double center = (recognition.getLeft() + recognition.getRight()) / 2;
                            if (recognition.getBottom() < PAN_Y + 50 && center < PAN_X + 20 && center > PAN_X - 20) {
                                if (isTrashDetected) {
                                    bin.trash();
                                } else {
                                    bin.recycle();
                                }
                                sleep(500); // Pause before dumping object
                                pan.panMove(dumpPosition);
                                sleep(1000);
                                pan.panMove(ground);
                            } else {
                                // TODO: move to trash
                            }
                        }
                    }
                    telemetry.update();
                }
            }

            if (objectDetector.getTfod() == null) {
                objectDetector.getTfod().shutdown();
            }
        }
    }
}
