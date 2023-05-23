package org.firstinspires.ftc.teamcode;

import com.vuforia.Vuforia;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

public class ObjectDetector {
    // https://ftc-docs.firstinspires.org/en/latest/programming_resources/vision/java_tfod_opmode/java-tfod-opmode.html

    private static final String VUFORIA_KEY =
        "AZhl9hf/////AAABmSdXtmMfH0UmgYqMCrsPKV0bbUVNyZM7oFtt7pSdNs6GidCqD6WJfXBgnaH1BW4YlZpX/+l7Pq6wld089Y1uB7vAfAgtj3uqyEKVZsAgApi6c0B53wCWvLKzPxczJAhIyPmJL5RzQCbyoN9pD8TtGXegj7c76YGvx4vjdnItPhfm87d2kkfqHBckPdKtqLtqzomR8qSaMemAfCAUBmrub1G/zx7+yvRCnbU8MPblYofjm/4pUINhJoKNfkHeZzMb3GNKubUkKnA9+d+O+M8rZdYxRcHhv0/453N9BpcegGJwTA1TRMvhDFB7ksSiyU+HSLGtHZW1DXTjoq9QkbLVLcVgQRiqsN49euJ29g85dxur";

    private static final String TFOD_MODEL_FILE = "model_unquant.tflite";
    private static final String[] LABELS = {
        "0 Cardboard",
        "1 Glass",
        "2 Metal",
        "3 Paper",
        "4 Plastic",
        "5 Trash"
    };
    
    private HardwareMap hardwareMap;
    
    public ObjectDetector(HardwareMap hwm) {
        hardwareMap = hwm;
    }

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    /**
     * Initialize the Vuforia localization engine.
     */
    public void initVuforia() {
        /*
        * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
        */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    public void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }

    /**
     * Get the Vuforia Localizer
     */
    public VuforiaLocalizer getVuforia() {
        return vuforia;
    }

    /**
     * Get the TensorFlow Object Detector
     */
    public TFObjectDetector getTfod() {
        return tfod;
    }
}
