package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.subsystems.Pigeon;

import frc.robot.Robot;


import static frc.robot.RobotMap.*;

public class SwerveDriveSubsystem extends HolonomicDrivetrain {
    public static final double WHEELBASE = 14.5;  // Swerve bot: 14.5 Comp bot: 20.5
    public static final double TRACKWIDTH = 13.5; // Swerve bot: 13.5 Comp bot: 25.5

    public static final double WIDTH = 20;  // Swerve bot: 20 Comp bot: 37
    public static final double LENGTH = 19; // Swerve bot: 19 Comp bot: 32

	/*
	 * 0 is Front Right
	 * 1 is Front Left
	 * 2 is Back Left
	 * 3 is Back Right
	 */
	private SwerveDriveModule[] mSwerveModules;

    ///private AHRS mNavX = new AHRS(SPI.Port.kMXP, (byte) 200);

    public SwerveDriveSubsystem() {
        super(WIDTH, LENGTH);
        zeroGyro();
/*
        if (Robot.PRACTICE_BOT) {
            mSwerveModules = new SwerveDriveModule[] {
                    new SwerveDriveModule(0, new CANSparkMax(RobotMap.DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR, MotorType.kBrushless), new CANSparkMax(4), 255.5859),
                    new SwerveDriveModule(1, new CANSparkMax(6), new CANSparkMax(5), 338.906),
                    new SwerveDriveModule(2, new CANSparkMax(2), new CANSparkMax(1), 13.359),
                    new SwerveDriveModule(3, new CANSparkMax(7), new CANSparkMax(8), 15.82),
            };

            mSwerveModules[0].setDriveInverted(true);
            mSwerveModules[3].setDriveInverted(true);
        } else {
            */
            mSwerveModules = new SwerveDriveModule[] {
                    new SwerveDriveModule(0,
                            new CANSparkMax(DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR, MotorType.kBrushless),
                            new CANSparkMax(DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR, MotorType.kBrushless),
                            87.890),
                    new SwerveDriveModule(1,
                            new CANSparkMax(DRIVETRAIN_FRONT_RIGHT_ANGLE_MOTOR, MotorType.kBrushless),
                            new CANSparkMax(DRIVETRAIN_FRONT_RIGHT_DRIVE_MOTOR, MotorType.kBrushless),
                            235.195),
                    new SwerveDriveModule(2,
                            new CANSparkMax(DRIVETRAIN_BACK_RIGHT_ANGLE_MOTOR, MotorType.kBrushless),
                            new CANSparkMax(DRIVETRAIN_BACK_RIGHT_DRIVE_MOTOR, MotorType.kBrushless),
                            320.976),
                    new SwerveDriveModule(3,
                            new CANSparkMax(DRIVETRAIN_BACK_LEFT_ANGLE_MOTOR, MotorType.kBrushless),
                            new CANSparkMax(DRIVETRAIN_BACK_LEFT_DRIVE_MOTOR, MotorType.kBrushless),
                            245.742),
            };

            mSwerveModules[0].setDriveInverted(true);
            mSwerveModules[3].setDriveInverted(true);


        for (SwerveDriveModule module : mSwerveModules) {
            module.setTargetAngle(0);
            module.setDriveGearRatio(5.7777);
            module.setDriveWheelRadius(module.getDriveWheelRadius() * 1.05);
            /*   removed for motion magic
            module.setMotionConstraints(getMaxAcceleration(), getMaxVelocity());
            */
        }
    }

    public double[] calculateSwerveModuleAngles(double forward, double strafe, double rotation) {
        if (isFieldOriented()) {
            double angleRad = Math.toRadians(getGyroAngle());
            double temp = forward * Math.cos(angleRad) + strafe * Math.sin(angleRad);
            strafe = -forward * Math.sin(angleRad) + strafe * Math.cos(angleRad);
            forward = temp;
        }

        double a = strafe - rotation * (WHEELBASE / TRACKWIDTH);
        double b = strafe + rotation * (WHEELBASE / TRACKWIDTH);
        double c = forward - rotation * (TRACKWIDTH / WHEELBASE);
        double d = forward + rotation * (TRACKWIDTH / WHEELBASE);

        return new double[]{
                Math.atan2(b, c) * 180 / Math.PI,
                Math.atan2(b, d) * 180 / Math.PI,
                Math.atan2(a, d) * 180 / Math.PI,
                Math.atan2(a, c) * 180 / Math.PI
        };
    }
/*
    public AHRS getNavX() {
        return mNavX;
    }
*/

public double getAngle() {
    return Pigeon.pidgey.getAbsoluteCompassHeading();
}
    public double getGyroAngle() {
        double angle = getAngle() - getAdjustmentAngle();
        angle %= 360;
        if (angle < 0) angle += 360;

        if (Robot.PRACTICE_BOT) {
            return angle;
        } else {
            return 360 - angle;
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public double getGyroRate() {
        return 0;//mNavX.getRate();
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public double getRawGyroAngle() {
        double angle = getAngle();
        angle %= 360;
        if (angle < 0) angle += 360;

        return angle;
    }

    public SwerveDriveModule getSwerveModule(int i) {
        return mSwerveModules[i];
    }

    @Override
    public void holonomicDrive(double forward, double strafe, double rotation, boolean fieldOriented) {
        forward *= getSpeedMultiplier();
        strafe *= getSpeedMultiplier();

        if (fieldOriented) {
            double angleRad = Math.toRadians(getGyroAngle());
            double temp = forward * Math.cos(angleRad) +
                    strafe * Math.sin(angleRad);
            strafe = -forward * Math.sin(angleRad) + strafe * Math.cos(angleRad);
            forward = temp;
        }

        double a = strafe - rotation * (WHEELBASE / TRACKWIDTH);
        double b = strafe + rotation * (WHEELBASE / TRACKWIDTH);
        double c = forward - rotation * (TRACKWIDTH / WHEELBASE);
        double d = forward + rotation * (TRACKWIDTH / WHEELBASE);

        double[] angles = new double[]{
                Math.atan2(b, c) * 180 / Math.PI,
                Math.atan2(b, d) * 180 / Math.PI,
                Math.atan2(a, d) * 180 / Math.PI,
                Math.atan2(a, c) * 180 / Math.PI
        };

        double[] speeds = new double[]{
                Math.sqrt(b * b + c * c),
                Math.sqrt(b * b + d * d),
                Math.sqrt(a * a + d * d),
                Math.sqrt(a * a + c * c)
        };

        double max = speeds[0];

        for (double speed : speeds) {
            if (speed > max) {
                max = speed;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (Math.abs(forward) > 0.05 ||
                    Math.abs(strafe) > 0.05 ||
                    Math.abs(rotation) > 0.05) {
                mSwerveModules[i].setTargetAngle(angles[i] + 180);
            } else {
                mSwerveModules[i].setTargetAngle(mSwerveModules[i].getTargetAngle());
            }
            mSwerveModules[i].setTargetSpeed(speeds[i]);
        }
    }

    @Override
    public void stopDriveMotors() {
        for (SwerveDriveModule module : mSwerveModules) {
            module.setTargetSpeed(0);
        }
    }
    
    public void resetMotors() {
    	for(int i = 0; i < mSwerveModules.length; i++) {
    		mSwerveModules[i].resetMotor();
    	}
    }

    public SwerveDriveModule[] getSwerveModules() {
        return mSwerveModules;
    }

    @Override
    public double getMaxAcceleration() {
        return 5.5;
    }

    @Override
    public double getMaxVelocity() {
        return 10;
    }
}
