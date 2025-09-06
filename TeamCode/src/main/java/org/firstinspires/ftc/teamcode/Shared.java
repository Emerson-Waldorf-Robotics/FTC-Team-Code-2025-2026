// UPDATEME: Change to correct package id
package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import androidx.annotation.NonNull;

//import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


// TODO: Consider using another shared file for comp specific stiff and this one for comp general stuff

/** @noinspection unused*/
public class Shared {
    // Declare OpMode members for each of the 4 motors.
    public static final ElapsedTime runtime = new ElapsedTime();
    public static DcMotorEx leftFrontDrive = null;
    public static DcMotorEx leftBackDrive = null;
    public static DcMotorEx rightFrontDrive = null;
    public static DcMotorEx rightBackDrive = null;

    //public static HuskyLens Camera = null;


    public static int[] motorStartPositions = null;


    public static HashMap<String, Boolean> actions = null;

    //TODO: Rework callbacks to use vectors of callbacks and millis
    public static ConcurrentHashMap<Double, ArrayList<Runnable>> callbacks = null;
    public static ConcurrentHashMap<Supplier<Boolean>, Runnable> checking_callbacks = null;

    // The telemetry from the calling class
    private static Telemetry telemetry = null;

    // The opModeIsActive function from the calling class. Use a lambda that returns true for iterative opModes
    private static Supplier<Boolean> opModeIsActive = null;

    public static void hardwareInit(HardwareMap hardwareMap, Telemetry tel, Supplier<Boolean> op) {
        // Initialize Callbacks (Fix HUGE bug where callbacks persist through opModes)
        callbacks          = new ConcurrentHashMap<>();
        checking_callbacks = new ConcurrentHashMap<>();

        // Reset runtime too
        runtime.reset();

        // Probably a good idea to re-init a few other things too
        actions = new HashMap<>(16);


        telemetry = tel;
        opModeIsActive = op;

        // Driving stuff
        leftFrontDrive  = hardwareMap.get(DcMotorEx.class, "left_front");
        leftBackDrive  = hardwareMap.get(DcMotorEx.class, "left_back");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "right_front");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "right_back");

        motorStartPositions = new int[]{
                leftBackDrive.getCurrentPosition(),
                rightBackDrive.getCurrentPosition(),
                leftFrontDrive.getCurrentPosition(),
                rightFrontDrive.getCurrentPosition()
        };

        leftFrontDrive.setDirection(DcMotorEx.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotorEx.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotorEx.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotorEx.Direction.REVERSE);
    }

    public static void initMotors(){
        // UPDATEME: Add per comp initialization here

    }

    public static void MoveMotor(int where, @NonNull DcMotorEx motor, boolean exact, int vel){
        telemetry.addLine("Running motor...");
        telemetry.addLine("--------------------------------------------------");
        telemetry.addData("Moving by", "%d", where);

        if (exact){
            telemetry.update();

            motor.setTargetPosition(where);
        } else {
            int spos = motor.getCurrentPosition();
            int epos = spos + where;
            telemetry.addData("Start position", "%d", spos);
            telemetry.addData("End Position", "%d", epos);
            telemetry.update();

            motor.setTargetPosition(epos);
        }

        motor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        motor.setVelocity(vel);
    }

    public static boolean isTrue(Boolean totest){
        return Boolean.TRUE.equals(totest);
    }

    /// Easier to read way of checking if a button is toggled
    public static boolean isToggled(String value){
        return isTrue(actions.get(value));
    }


    // Callbacks

    /// Register a callback to be ran later
    public static void registerCallback(Runnable callback, int delayMillis){
        // Get current time in nanoseconds
        double now = runtime.milliseconds();


        if (!callbacks.containsKey(now)){
            callbacks.put(now, new ArrayList<>());
        }
        // Paranoia
        Objects.requireNonNull(callbacks.get(now)).add(callback);
    }

    /// Register a callback to be ran later
    public static void registerCheckingCallback(Runnable callback, Supplier<Boolean> check){
        // If check passes, run right now
        if (check.get()) {
            callback.run();
            return;
        }

        // Add it to the callbacks HashMap
        checking_callbacks.put(check, callback);
    }

    /// Run and delete all callbacks that have expired
    public static void runCallbacks(){
        // Get current (run)time in milliseconds
        double now = runtime.milliseconds();

        // TODO: I should move these to the class namespace so as to avoid excessive memory allocations but that would taint the namespace. What to do...?
        // List of id's to remove
        ArrayList<Double> toRemove = new ArrayList<>();
        ArrayList<Supplier<Boolean>> toRemoveCheck = new ArrayList<>();

        if (!callbacks.isEmpty()) {
            telemetry.addLine("Callbacks:");
        }
        for (Map.Entry<Double, ArrayList<Runnable>> entry : callbacks.entrySet()){
            telemetry.addData("\t", "in %fms: %d callbacks", entry.getKey() - now, entry.getValue().size());

            // Check if time has expired
            if (entry.getKey() - now <= 0) {
                // Run the callbacks
                for (Runnable callback : entry.getValue()){
                    callback.run();
                }

                toRemove.add(entry.getKey());
            }
        }

        for (double id : toRemove){
            callbacks.remove(id);
        }

        for (Map.Entry<Supplier<Boolean>, Runnable> entry : checking_callbacks.entrySet()){
            telemetry.addData("\t", "%s: %s", entry.getKey().toString(), entry.getValue().toString());

            if (entry.getKey().get()) {
                // Run the callback
                entry.getValue().run();

                toRemoveCheck.add(entry.getKey());
            }
        }

        for (Supplier<Boolean> id : toRemoveCheck){
            checking_callbacks.remove(id);
        }
    }


    // Automatic Movement

    /// Positions of motors
    private static final int[] motpos = {
            // LFront
            0,
            // LBack
            0,
            // RFront
            0,
            // RBack
            0
    };

    public static void forward(int howmuch){
        motpos[0] += howmuch;
        motpos[1] += howmuch;
        motpos[2] += howmuch;
        motpos[3] += howmuch;
    }

    public static void backward(int howmuch){
        forward(-howmuch);
    }

    public static void turnRight(int degrees){
        turnLeft(-degrees);
    }

    public static void turnLeft(int degrees){
        motpos[0] += degrees;
        motpos[1] += degrees;
        motpos[2] -= degrees;
        motpos[3] -= degrees;
    }

    public static void strafeRight(int howmuch){
        motpos[0] += howmuch;
        motpos[1] -= howmuch;
        motpos[2] -= howmuch;
        motpos[3] += howmuch;
    }

    public static void strafeLeft(int howmuch){
        strafeRight(-howmuch);
    }

    public static void motoGO(double vel){
        leftFrontDrive.setTargetPosition(motpos[0]);
        leftBackDrive.setTargetPosition(motpos[1]);
        rightFrontDrive.setTargetPosition(motpos[2]);
        rightBackDrive.setTargetPosition(motpos[3]);

        leftFrontDrive.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        leftFrontDrive.setVelocity(vel);
        leftBackDrive.setVelocity(vel);
        rightBackDrive.setVelocity(vel);
        rightFrontDrive.setVelocity(vel);
    }

    public static void waitMoveDone(){
        telemetry.addLine("Waiting for motors to stop...");
        telemetry.update();
        while (motorBusy()) {}
    }

    public static boolean motorBusy(){
        return leftFrontDrive.isBusy() || leftBackDrive.isBusy() || rightBackDrive.isBusy() || rightFrontDrive.isBusy();
    }

    public static void eggOfEaster(){
        telemetry.speak("Never gonna give you up, never gonna let you down, never gonna run around and desert you");
    }
}
