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

        DcMotor motorToRun = hardwareMap.get(DcMotor.class, "left_front");
        DcMotor motorToRun2 = hardwareMap.get(DcMotor.class, "left_back");
        DcMotor motorToRun3 = hardwareMap.get(DcMotor.class, "right_front");
        DcMotor motorToRun4 = hardwareMap.get(DcMotor.class, "right_back");
        

        // motorToRun.setDirection(DcMotorSimple.Direction.FORWARD);
        // motorToRun.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
        telemetry.addLine("Remember to have the wheels form a cross across the body!!!");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double power = gamepad1.left_stick_y;
            double power2 = gamepad1.right_stick_y;
            double power3 = gamepad1.left_stick_x;
            double power4 = gamepad1.right_stick_x;

            motorToRun.setPower(power);
            motorToRun2.setPower(power2);
            motorToRun3.setPower(power3);
            motorToRun4.setPower(power4);

            telemetry.addData("Motor 1", power * 100);
            telemetry.addData("Motor 2", power2 * 100);
            telemetry.addData("Motor 3", power3 * 100);
            telemetry.addData("Motor 4", power4 * 100);

            telemetry.update();
        }
    }
}
