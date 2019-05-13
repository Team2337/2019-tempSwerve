package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.motion.AutonomousPaths;
import frc.robot.util.Side;

public class ScoreScaleFromStart extends CommandGroup {
	private static final double SAME_SIDE_ELEVATOR_WAIT = 1.0;
	private static final double OPPOSITE_SIDE_ELEVATOR_WAIT = 6.0;

	public ScoreScaleFromStart(Robot robot, Side startSide, Side scaleSide, StartingOrientation orientation) {
		if (startSide == Side.LEFT) {
			if (scaleSide == Side.LEFT) {
				// Same side
				if (orientation == StartingOrientation.FORWARDS) {
					addSequential(new FollowPathCommand(robot.getDrivetrain(), AutonomousPaths.LEFT_START_FORWARD_TO_LEFT_SCALE_FRONT));
				} else {
					addSequential(new FollowPathCommand(robot.getDrivetrain(), AutonomousPaths.LEFT_START_TO_LEFT_SCALE_SIDE_STEP_1));
				}
			} else {
				// Opposite side
				if (orientation == StartingOrientation.FORWARDS) {
					addSequential(new FollowPathCommand(robot.getDrivetrain(), AutonomousPaths.LEFT_START_FORWARD_TO_RIGHT_SCALE_FRONT));
				} else {
					addSequential(new FollowPathCommand(robot.getDrivetrain(), AutonomousPaths.LEFT_START_TO_RIGHT_SCALE_SIDE_STEP_1));
					addSequential(new SetDrivetrainAngleCommand(robot.getDrivetrain(), 270));
					addSequential(new FollowPathCommand(robot.getDrivetrain(), AutonomousPaths.LEFT_START_TO_RIGHT_SCALE_SIDE_STEP_2));
				}
			}
		} else {
			if (scaleSide == Side.LEFT) {
				// Opposite side
				if (orientation == StartingOrientation.FORWARDS) {
					addSequential(new FollowPathCommand(robot.getDrivetrain(), AutonomousPaths.RIGHT_START_FORWARD_TO_LEFT_SCALE_FRONT));
				} else {
					addSequential(new FollowPathCommand(robot.getDrivetrain(), AutonomousPaths.RIGHT_START_TO_LEFT_SCALE_SIDE_STEP_1));
					addSequential(new SetDrivetrainAngleCommand(robot.getDrivetrain(), 270));
					addSequential(new FollowPathCommand(robot.getDrivetrain(), AutonomousPaths.RIGHT_START_TO_LEFT_SCALE_SIDE_STEP_2));
				}
			} else {
				// Same side
				if (orientation == StartingOrientation.FORWARDS) {
					addSequential(new FollowPathCommand(robot.getDrivetrain(), AutonomousPaths.RIGHT_START_FORWARD_TO_RIGHT_SCALE_FRONT));
				} else {
					addSequential(new FollowPathCommand(robot.getDrivetrain(), AutonomousPaths.RIGHT_START_TO_RIGHT_SCALE_SIDE_STEP_1));
				}
			}
		}
	}
}