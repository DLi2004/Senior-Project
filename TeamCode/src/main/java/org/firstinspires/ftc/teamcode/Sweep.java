/*
 * Autonomous for sweeper robot Masterman Senior Project 2023
 */

 package org.firstinspires.ftc.teamcode;
 import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
 import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
 import com.qualcomm.robotcore.eventloop.opmode.Disabled;
 import com.qualcomm.robotcore.util.ElapsedTime;
 
 @Autonomous(name="sweep")
 
 public class Sweep extends LinearOpMode {
     private DriveTrain driveTrain = new DriveTrain(hardwareMap);

     double twist;
     double forward;
     double strafe;
 
     @Override
     public void runOpMode() {
         waitForStart();
 
         while (opModeIsActive()) {
            /*
             * Rotate around until detect trash and then move forward towards it. If none detected, proceed in a random direction for a set amount of time. Repeat.
             */

            // Rotate around until detect trash

            // Move forward towards it
            // If none detected, proceed in a random direction for a set amount of time
            // Repeat
        }
    }
 }