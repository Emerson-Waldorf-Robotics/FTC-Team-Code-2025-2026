package org.firstinspires.ftc.teamcode.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Wheel Axation Mode", group="Prototyping")
public class FlywheelTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        double power_level = 0;

        int state = 0;
        boolean state_changed = false;

        final ElapsedTime runtime = new ElapsedTime();

        DcMotor motorToRun = hardwareMap.get(DcMotor.class, "test_motor");
        

        motorToRun.setDirection(DcMotor.Direction.REVERSE);
        motorToRun.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.addLine("Remember to have the wheels form a cross across the body!!!");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            if (gamepad1.b) {
                power_level = 0;
            }

            if (gamepad1.x) {
                power_level = 0.30;
            }

            if (gamepad1.a) {
                power_level = 0.60;
            }

            if (gamepad1.y) {
                power_level = 1;
            }

            motorToRun.setPower(-power_level);

            telemetry.addData("Motor power", power_level * 100);
            telemetry.update();
        }
    }
}
