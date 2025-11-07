package org.firstinspires.ftc.teamcode.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@TeleOp(name="Wheelaxation Mode", group="Prototyping")
public class FlywheelTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        double speed = 0;

        int state = 0;
        boolean state_changed = false;

        final ElapsedTime runtime = new ElapsedTime();

        DcMotorEx motorToRun = hardwareMap.get(DcMotorEx.class, "test_motor");
        

        motorToRun.setDirection(DcMotorEx.Direction.REVERSE);
        // Make the motor brake when requested to stop
        motorToRun.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorToRun.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.addLine("Remember to have the wheels form a cross across the body!!!");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            if (gamepad1.b) {
                speed = 0;
            }

            if (gamepad1.x) {
                speed = 0.3;
            }

            if (gamepad1.a) {
                speed = 0.6;
            }

            if (gamepad1.y) {
                speed = 1;
            }

            motorToRun.setVelocity(-360 * speed, AngleUnit.DEGREES);

            telemetry.addData("Motor speed (rps)", motorToRun.getVelocity(AngleUnit.DEGREES)/360);
            telemetry.update();
        }
    }
}
