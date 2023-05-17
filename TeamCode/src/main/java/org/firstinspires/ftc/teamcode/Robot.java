package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.EventLoopManager;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Robot {
    HardwareMap hwMap = null;
    // Declare OpMode members.
    public ElapsedTime runtime = new ElapsedTime();

    public DcMotor panMotor = null;

    public int initArmPosition = 0;

    public IMU imu;
    public IMU.Parameters IMUParameters;

    public void init(HardwareMap ahwmap){
        hwMap = ahwmap;

        //  Get all drive motors
        panMotor = hwMap.get(DcMotor.class, "broom");

        IMUParameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));

        // Get IMU
        imu = hwMap.get(IMU.class, "imu");
        imu.initialize(IMUParameters);
    }

    public void setMotorPower(double p){
        armMotor.setPower(p);
    }
}