package org.firstinspires.ftc.teamcode.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Run Motor", group="Prototyping")
public class MotorTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        final ElapsedTime runtime = new ElapsedTime();

        DcMotor motorToRun = hardwareMap.get(DcMotor.class, "test_motor");
        DcMotor motorToRun2 = hardwareMap.get(DcMotor.class, "test_motor2");
        

        motorToRun.setDirection(DcMotorSimple.Direction.FORWARD);
        motorToRun.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
        telemetry.addLine("Remember to have the wheels form a cross across the body!!!");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // Main branch test
        while (opModeIsActive()) {
            double power = gamepad1.left_stick_y;
            double power2 = gamepad1.right_stick_y;

            motorToRun.setPower(power);
            motorToRun2.setPower(power2);

            telemetry.addData("Motor 1", power * 100);
            telemetry.addData("Motor 2", power2 * 100);

            telemetry.update();
        }
    }
}
