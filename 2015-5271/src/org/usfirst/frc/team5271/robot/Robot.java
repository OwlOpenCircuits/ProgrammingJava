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
	RobotDrive myRobot;
	Joystick stick;
	int autoLoopCounter;
	DigitalInput limitSwitch;
	Jaguar testMotor;
	CameraServer server;
	
	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	myRobot = new RobotDrive(0,1);
    	/**Change (0,1) to whatever ports the drive motors are plugged into
    	*you may need to reverse the motors again if they start jamming
    	* use myRobot.setInvertedMotor(RobotDrive.MotorType.(motor name), true)
    	* replace (motor name) with kFrontLeft, kRearLeft, kFrontRight, or kRearRight
    	* The program seems to name them itself, so just guess and start reversing
    	*/
    	stick = new Joystick(0);
    	testMotor = new Jaguar(9);
    	limitSwitch = new DigitalInput(0);
    	server = CameraServer.getInstance();
        server.setQuality(50);
        server.startAutomaticCapture("cam0");
        direc = new Gyro(1);
        direc.reset();
        direc.startLiveWindowMode();
    	Encoder encoder = new Encoder;
    	
    			
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	autoLoopCounter = 0;
    	
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	if(autoLoopCounter < 100) //Check if we've completed 100 loops (approximately 2 seconds)
		{
			myRobot.drive(-0.5, 0.0); 	// drive forwards half speed
			testMotor.set(.2);
			autoLoopCounter++;
			} else {
			myRobot.drive(0.0, 0.0); 	// stop robot
			testMotor.set(0);
			
		}
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    	direc.reset();
    }
   
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
        myRobot.arcadeDrive(stick);
        
        SmartDashboard.putNumber("Gyro", direc.getAngle());
        System.out.println(direc.getAngle());
        direc.updateTable();
        
        if (stick.getRawButton(6)){
        	testMotor.set(-.1);
        	
        }
        else if (stick.getRawButton(4)){
        	testMotor.set(.1);
        }
        
        else if (stick.getRawButton(1)){
            while (stick.getRawButton(9) == false && (limitSwitch.get() == false)){
        	testMotor.set(.2);
            }
            testMotor.set(-.2);
        	Timer timer1 = new Timer();
        	timer1.delay(1);
        	testMotor.set(0);
        	
           /* if (limitSwitch.get() == true){
            	testMotor.set(-.2);
            	Timer timer1 = new Timer();
            	timer1.delay(1);
            	testMotor.set(0);
            }*/
        }
            
        else
        	testMotor.set(0);
        
       /*if (stick.getRawButton(1)){
        testMotor.set(.2);
        }
        if (limitSwitch.get() == true){
        	testMotor.set(-.2);
        	Timer timer1 = new Timer();
        	timer1.delay(1);
        	testMotor.set(0);
        }*/
        
       
        
        
    }
   
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
}
