package frc.robot.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;

/**
 * Gyro class for CTRE Pigeon Gyro
 * @category CHASSIS
 * @author Team2337 - EngiNERDs
 * Must be initiated after RobotMap
 */
public class Pigeon extends Subsystem {

	public static PigeonIMU pidgey;
	public static PigeonIMU.FusionStatus gyrofusionStatus;
	public static PigeonIMU.GeneralStatus gyroGenStatus;
	public double[] ypr_deg;
	public double[] xyz_dps;
	private int timeoutMs = 20;
	
	/**
	 * The subsystem to calibrate the Pigeon (gyro)
	 */
	public Pigeon() {
		pidgey = new PigeonIMU(RobotMap.pidgeyID);
		//Put this line in, in order to calibrate the pigeon to the correct degree mode
		//pidgey.enterCalibrationMode(CalibrationMode.BootTareGyroAccel, 10);
		gyrofusionStatus = new PigeonIMU.FusionStatus();
		gyroGenStatus = new PigeonIMU.GeneralStatus();
		ypr_deg = new double[3];
		xyz_dps = new double[3];
	}
	
	@Override
	protected void initDefaultCommand() {

	}
	

	/**
	 * Periodically request information from the device
	 */
	public void periodic() {
		
		pidgey.getFusedHeading(gyrofusionStatus);
		pidgey.getGeneralStatus(gyroGenStatus);
		pidgey.getYawPitchRoll(ypr_deg);
		pidgey.getRawGyro(xyz_dps);

		
		//SmartDashboard.putNumber("FusedHeading", pidgey.getFusedHeading());
		//SmartDashboard.putNumber("AbsoluteCompass", getThing());
		SmartDashboard.putNumber("yaw", getYaw());
		//SmartDashboard.putNumber("Pitch", getPitch());
		//SmartDashboard.putNumber("Roll", getRoll());
	}

	/**
	 * Gets the yaw from the gyro
	 * @return yaw
	 */
	public double getYaw() {
		double yaw = 0;
		yaw = ypr_deg[0];
		return yaw;
	}

	/**
	 * Returns the pitch from the gyro
	 * @return pitch
	 */
	public double getPitch() {
		double pitch = 0;
		pitch = ypr_deg[1];
		return pitch;
	}

	/**
     * Returns the roll value from the gyro
	 * @return roll
	 */
	public double getRoll() {
		double roll = 0;
		roll = ypr_deg[2];
		return roll;
	}

	/**
	 * Returns the absolute compass heading of the gyro 
	 * @return returns the absolute compass heading 
	 */
	public double getAbsoluteCompassHeading() {
		return pidgey.getAbsoluteCompassHeading();
	}
	
	/**
	 * Resets the pigeon 
	 */
	public void resetPidgey() {
		pidgey.setYaw(0, timeoutMs);
	}

	/**
	 * Gets the rate at which the robot is spinning
	 * @return angularRate
	 */
	public double getAngularRate() {
		double angularRate;
		angularRate = xyz_dps[2];
		return angularRate;
	}
	
	/**
	 * Use to manually set the yaw in degrees
	 */
	public void manualSetYaw(double yaw) {
		yaw *= 64;
		pidgey.setYaw(yaw, timeoutMs);

	}
	 /**
	 * Returns the FusedHeading value from the gyro
	 * @return FusedHeading
	 */
	public double getFusedHeading() {
		return pidgey.getFusedHeading();
	}	
	
	/**
    * Returns the Temperature value from the gyro
	* @return Temp
	*/
   public double getTemp() {
	   return pidgey.getTemp();

   }
}