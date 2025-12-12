/* Copyright (c) 2021 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Auto;

import static org.firstinspires.ftc.teamcode.Shared.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;


@Autonomous(name="Main Auto", group="Main")
public class MainAuto extends LinearOpMode {
    // TODO: Try to find order for ~5 seconds. If unsuccessful, go random
    public boolean getOpActive(){return opModeIsActive();}

    private enum MainState {
        findArtifactOrder,
        artifactLoop,
    }

    private enum LoopState {
        collect3Artifacts,
        alignRobot,
        shootArtifacts,
    }

    private enum Order {
        Unknown, // Used when we can't find the order within 5 seconds
        GPP,
        PGP,
        PPG,
    }


    private Order matchOrder = Order.Unknown;
    private MainState currentState = MainState.findArtifactOrder;
    private LoopState currentLoopState = LoopState.collect3Artifacts;

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {
        hardwareInit(hardwareMap, telemetry, this::getOpActive);

        DcMotorEx flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.addLine("Remember to have the wheels form a cross across the body!!!");
        telemetry.update();

        // Call this before waitForStart bc camera startup takes a bit
        initRobot();

        waitForStart();
        runtime.reset();

        initMotors(flywheel);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            switch (currentState) {
                case findArtifactOrder:
                    findArtifactOrder();
                    break;
                case artifactLoop:
                    switch (currentLoopState) {
                        case collect3Artifacts:
                            collectArtifacts();
                            break;
                        case alignRobot:
                            alignRobot();
                            break;
                        case shootArtifacts:
                            shootArtifacts();
                            break;
                    }
                    break;
            }


            runCallbacks();
            addTelemetry();
        }
    }

    private void initRobot() {
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"),
                aprilTag
        );
        //visionPortal.resumeStreaming();
    }

    private void addTelemetry() {
        // Show the elapsed game time and wheel power.
        telemetry.addData("State", currentState.toString());
        telemetry.addData("Loop State", currentLoopState.toString());
        telemetry.addData("Order", matchOrder.toString());
        telemetry.addLine();

        telemetry.addData("Run Time", runtime);
        telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontDrive.getPower(), rightFrontDrive.getPower());
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackDrive.getPower(), rightBackDrive.getPower());
        telemetry.addLine("Remember to have the wheels form a cross across the body!!!");
        telemetry.update();
    }

    private void findArtifactOrder() {
        boolean seeRight = false;
        boolean seeLeft = false;

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
//            if (detection.metadata != null) {
//                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
//                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
//                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
//                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
//            } else {
//                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
//                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
//            }
            switch (detection.id) {
                case 21:
                    matchOrder = Order.GPP;
                    currentState = MainState.artifactLoop;
                    return;
                case 22:
                    matchOrder = Order.PGP;
                    currentState = MainState.artifactLoop;
                    return;
                case 23:
                    matchOrder = Order.PPG;
                    currentState = MainState.artifactLoop;
                    return;
                case 20:
                    seeLeft = true;
                    // We haven't found the main order tag yet, so don't return
                    break;
                case 24:
                    seeRight = true;
                    // We haven't found the main order tag yet, so don't return
                    break;
                default:
                    // We have no idea
                    break;
            }
        }
        if (runtime.seconds() > 5) {
            matchOrder = Order.Unknown;
            currentState = MainState.artifactLoop;
        }
        telemetry.addLine();
        if (seeLeft && seeRight) {
            // We see both but not the middle?
            // Move forward.
            telemetry.addData("Artifact Order Processor", "See Left and right tags");
        } else if (seeLeft) {
            // We only see the left tag
            // Move right
            telemetry.addData("Artifact Order Processor", "See Left tag");
            PowerMove.strafeRight(0.5);
        } else if (seeRight) {
            // We only see the right tag
            // Move left
            telemetry.addData("Artifact Order Processor", "See Right tag");
            PowerMove.strafeLeft(0.5);
        } else {
            // No Tags
            telemetry.addData("Artifact Order Processor", "See no tags");
        }
    }

    private void collectArtifacts() {
        // We don't need apriltags for this
        visionPortal.stopStreaming();
    }

    private void alignRobot() {
        // Re-enable apriltags
        visionPortal.resumeStreaming();
    }

    private void shootArtifacts() {

    }
}
