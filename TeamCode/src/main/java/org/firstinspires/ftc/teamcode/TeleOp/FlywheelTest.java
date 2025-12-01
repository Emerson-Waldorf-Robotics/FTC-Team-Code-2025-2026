package org.firstinspires.ftc.teamcode.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@TeleOp(name="Wheel Axation Mode", group="Prototyping")
public class FlywheelTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        double speed = 0;

        final ElapsedTime runtime = new ElapsedTime();

        DcMotorEx motorToRun = hardwareMap.get(DcMotorEx.class, "test_motor");
        

        motorToRun.setDirection(DcMotorEx.Direction.REVERSE);
        // Make the motor brake when requested to stop
        motorToRun.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        //motorToRun.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motorToRun.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.addLine("Remember to have the wheels form a cross across the body!!!");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            if (gamepad1.b) {
                speed = 1;
            }

            if (gamepad1.x) {
                speed = 0.3;
            }

            if (gamepad1.a) {
                speed = 0.4;
            }

            if (gamepad1.y) {
                speed = 0.5;
            }

            //motorToRun.setVelocity(-360 * speed, AngleUnit.DEGREES);
            motorToRun.setPower(speed);

            telemetry.addData("Motor speed (rps)", motorToRun.getVelocity(AngleUnit.DEGREES)/360);
            telemetry.addData("Motor power", speed);
            telemetry.update();
        }
    }
}
