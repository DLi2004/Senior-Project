package org.firstinspires.ftc.teamcode;

import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class ObjectDetector {
    // https://ftc-docs.firstinspires.org/en/latest/programming_resources/vision/java_tfod_opmode/java-tfod-opmode.html

    private static final String VUFORIA_KEY =
        "AZhl9hf/////AAABmSdXtmMfH0UmgYqMCrsPKV0bbUVNyZM7oFtt7pSdNs6GidCqD6WJfXBgnaH1BW4YlZpX/+l7Pq6wld089Y1uB7vAfAgtj3uqyEKVZsAgApi6c0B53wCWvLKzPxczJAhIyPmJL5RzQCbyoN9pD8TtGXegj7c76YGvx4vjdnItPhfm87d2kkfqHBckPdKtqLtqzomR8qSaMemAfCAUBmrub1G/zx7+yvRCnbU8MPblYofjm/4pUINhJoKNfkHeZzMb3GNKubUkKnA9+d+O+M8rZdYxRcHhv0/453N9BpcegGJwTA1TRMvhDFB7ksSiyU+HSLGtHZW1DXTjoq9QkbLVLcVgQRiqsN49euJ29g85dxur";

    private static final String TFOD_MODEL_ASSET = "/converted_tflite/model_unquant.tflite";
    private static final String[] LABELS = {
        "Cardboard",
        "Glass",
        "Metal",
        "Paper",
        "Plastic",
        "Trash"
    };

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
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
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}
