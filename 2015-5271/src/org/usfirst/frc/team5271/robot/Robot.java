
package org.usfirst.frc.team5271.robot;
import edu.wpi.first.wpilibj.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
	
	RobotDrive mainDrive;
	Joystick driveStick;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
    public void robotInit() {
    	mainDrive = new RobotDrive(1, 2, 3, 4);
    	driveStick = new Joystick(1);

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        while (isOperatorControl() && isEnabled()) {
        	mainDrive.arcadeDrive(driveStick);
        	Timer.delay(0.01);
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
