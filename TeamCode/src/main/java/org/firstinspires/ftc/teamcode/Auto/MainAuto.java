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

import static org.firstinspires.ftc.teamcode.Shared.hardwareInit;
import static org.firstinspires.ftc.teamcode.Shared.initMotors;
import static org.firstinspires.ftc.teamcode.Shared.leftBackDrive;
import static org.firstinspires.ftc.teamcode.Shared.leftFrontDrive;
import static org.firstinspires.ftc.teamcode.Shared.rightBackDrive;
import static org.firstinspires.ftc.teamcode.Shared.rightFrontDrive;
import static org.firstinspires.ftc.teamcode.Shared.runCallbacks;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
@Autonomous(name="Main Auto", group="Main")
class MainAuto extends LinearOpMode {
    public boolean getOpActive(){return opModeIsActive();}

    private enum mainState {
        findArtifactOrder,
        artifactLoop,
    }

    private enum loopState {
        collect3Artifacts,
        alignRobot,
        shootArtifacts,
    }

    @Override
    public void runOpMode() {
        // Declare OpMode members for each of the 4 motors.
        final ElapsedTime runtime = new ElapsedTime();

        hardwareInit(hardwareMap, telemetry, this::getOpActive);

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.addLine("Remember to have the wheels form a cross across the body!!!");
        telemetry.update();

        waitForStart();
        runtime.reset();

        initMotors();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontDrive.getPower(), rightFrontDrive.getPower());
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackDrive.getPower(), rightBackDrive.getPower());
            telemetry.addLine("Remember to have the wheels form a cross across the body!!!");
            telemetry.update();

            runCallbacks();
        }
    }}
