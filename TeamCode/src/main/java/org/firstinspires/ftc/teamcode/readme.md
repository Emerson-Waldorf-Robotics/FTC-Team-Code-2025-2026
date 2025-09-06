# How to use the shared code:

## Importing:

- Search the codebase for lines with "UPDATEME". These lines need to be updated for the project.
- Import the shared module into your OpMode with `import static org.firstinspires.ftc.teamcode.Shared.*;`

## Initialization:

### Linear OpMode

- In the main OpMode class, add the following function: `public boolean getOpActive(){return opModeIsActive();}`
- In the OpMode file, in runOpMode, BEFORE `waitForStart`, add the line `hardwareInit(hardwareMap, telemetry, this::getOpActive);`
- In the OpMode file, in runOpMode, AFTER `waitForStart`, add the line `initMotors();`
- In your main loop, add the line `runCallbacks();`

### Iterative OpMode

- In the main OpMode class, in Init, add the line `hardwareInit(hardwareMap, telemetry, () -> true);`.
- In the main OpMode class, in Start, add the line `initMotors();`
- In the main OpMode class, in Loop, add the line `runCallbacks();`


# Features of the shared code

- Easy movement using `forward`, `backward`, `turnRight`, `turnLeft`, `strafeRight`, `strafeLeft`, `motoGO`, `waitMoveDone`, and `motorBusy`
  - Most should be self explanatory but one thing is important: 
  - these functions just queue up the movement. they don't perform it. Call `motoGO` to run all queued up movement.
  - `motoGO` returns instantly. To wait for movement to be finished, use `waitMoveDone` or set a callback (explained later) on `motorBusy`
- Button toggling system (WIP)
  - For now, this system is possible to use but requires a **lot** of code to use. I hope to make it easier on the developer soon. (Maybe something like callbacks?)
  - Check out [this](https://github.com/Emerson-Waldorf-Robotics/FTC-Team-Code-2024-2025/blob/jacob/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/RunFullTeleOp.java#L397) to see an example
- Extensible callback system
  - Easily run code in a specific amount of time or when a condition becomes true!
  - To run a function (or lambda) when a specific amount of time has passed: `registerCallback(callback, delayMillis);`
  - To run a function when another function returns true: `registerCheckingCallback(callback, checkFunction)`

# WARNING

### Be aware that any vars defined in the Shared class and not in a function are **NOT** redefined on a new opMode start. I had to learn this the hard way.
