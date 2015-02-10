package org.usfirst.frc.team5271.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Encoder;

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
	Jaguar Elev2;
	Jaguar Elev1;
	CameraServer server;
	Talon Drive1;
	Talon Drive2;
	Talon Drive3;
	Talon Drive4;
	Encoder encoder;
	int defForce = 0; // the brake that stops the chain from moving down
	double posElevPower = .35; // the up power
	double negElevPower = -.15; // the down power
	
	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	stick = new Joystick(0);
    	Elev1 = new Jaguar(7);
    	Elev2 = new Jaguar(8);
    	limitSwitch = new DigitalInput(0);
    	server = CameraServer.getInstance();
        server.setQuality(50);
        server.startAutomaticCapture("cam0");
    	myRobot = new RobotDrive(3,4,5,6);
    	encoder = new Encoder(1, 2, false, Encoder.EncodingType.k4X);
    	/**Change (0,1) to whatever ports the drive motors are plugged into
    	*you may need to reverse the motors again if they start jamming
    	* use myRobot.setInvertedMotor(RobotDrive.MotorType.(motor name), true)
    	* replace (motor name) with kFrontLeft, kRearLeft, kFrontRight, or kRearRight
    	* The program seems to name them itself, so just guess and start reversing
    	*/
    			
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	autoLoopCounter = 0;
    	myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
    	myRobot.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    	myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
    	myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	if(autoLoopCounter < 100) //Check if we've completed 100 loops (approximately 2 seconds)
		{
			myRobot.drive(-0.5, 0.0); 	// drive forwards half speed
			Elev1.set(-.2);
			Elev2.set(.2);
			autoLoopCounter++;
			} else {
			myRobot.drive(0.0, 0.0); 	// stop robot
			Elev1.set(-0);
			Elev2.set(0);
			
		}
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    	myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
    	myRobot.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    	myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
    	myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
    	Elev1.enableDeadbandElimination(true);
    	Elev2.enableDeadbandElimination(true);
    	encoder.setMaxPeriod(.1);
    	encoder.setMinRate(10);
    	encoder.setDistancePerPulse(1);
    	encoder.setReverseDirection(true);
    	encoder.setSamplesToAverage(40);
    }
   
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        myRobot.arcadeDrive(stick, true);
        //enable counter weight
        if (stick.getRawButton(11)) {
        	defForce = 11;
        	
        //disable counterweight
        }
        if (stick.getRawButton(12)) {
        	defForce = 0;
        }
        //up one level 
        if (stick.getRawButton(5) && (limitSwitch.get() == false) && stick.getRawButton(9) == false) {
        	encoder.reset();
        	System.out.println("up button");
        	while (encoder.get() >= -300 && (limitSwitch.get() == false) && stick.getRawButton(9) == false) {
        		Elev1.set(posElevPower);
        		Elev2.set(posElevPower);
        		System.out.println(encoder.get());
        	}
        }
        else{
    		Elev1.set(defForce * 0.01);
    		Elev2.set(defForce * 0.01);
        //down one level
        if (stick.getRawButton(3) && (limitSwitch.get() == false) && stick.getRawButton(9) == false) {
        	encoder.reset();
        	while (encoder.get() <= 300 && (limitSwitch.get() == false) && stick.getRawButton(9) == false) {
        		Elev1.set(negElevPower);
        		Elev2.set(negElevPower);
        		System.out.println(encoder.get());
        	}
        	
        }
        else{
    		Elev1.set(defForce * 0.01);
    		Elev2.set(defForce * 0.01);
        
        	
        }
        //manual up
        if (stick.getRawButton(6)){
        	
        	Elev2.set(posElevPower);
        	Elev1.set(posElevPower);
        	
        //manual down	
        }
        else if (stick.getRawButton(4)){
        	Elev2.set(negElevPower);
        	Elev1.set(negElevPower);
        	
        }
        
        else{
        		Elev1.set(0.01 * defForce);
        		Elev2.set(0.01 * defForce);	
        //auto pick-up
        	if (stick.getRawButton(2)){ //pickup lines
        		while (stick.getRawButton(9) == false && (limitSwitch.get() == false)){
       				Elev1.set(negElevPower);
       				Elev2.set(negElevPower);
       			}
        		Elev1.set(posElevPower);
            	Elev2.set(posElevPower);
        		Timer.delay(.2);
                Elev1.set(0.01 * defForce);
                Elev2.set(0.01 * defForce);	
            
        	}
        
        	else{
        		Elev1.set(0.01 * defForce);
        		Elev2.set(0.01 * defForce);
        	}
        }
        	
        	
        }
    {
    //force encoder reset
    	if(stick.getRawButton(8) == true){
    		encoder.reset();
    	}
    }
        
    }
}

        
        	
        
        	
            /*if (limitSwitch.get() == true){
            	int encodervalue2 = 0;
            	while (encodervalue2 >= -2500) {
            		Elev1.set(-.25);
            		Elev2.set(-.25);
            		encodervalue2 = encoder.get();
            		System.out.println(encoder.get());
            	Elev1.set(0);
            	Elev2.set(0);
            	carriageLevel = 0;
           */ 
       // }
            
        
          	
   // }
   
    
    /**
     * This function is called periodically during test mode
     */
    