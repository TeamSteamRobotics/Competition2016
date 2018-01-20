package org.usfirst.frc.team5119.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	//Classes and inputs
	RobotDrive myRobot; //RobotDrive.class
	Joystick stick; //Joystick
	//Sensors and switches
	DigitalInput ballSensor;
	DigitalInput autonomousSwitch;
	DigitalInput extraSwitch;
	//Motor controllers
	Talon intakeMotor; //Talon motor controller that controls intake wheel motor
	Talon bottomLaunchMotor;
	Talon topLaunchMotor;
	//Motor values; should all be doubles
	double intakeSpeed;
	double topLaunchSpeed;   //Fine tune robot targeting
	double bottomLaunchSpeed; //oughta work
	double reverseIntakeSpeed;
	double reverseMiddleSpeed;
	double autoSpeed; //autonomous speed
	//Counters; Should all be ints
	int buttonTimerCounter;
	int autoLoopCounter; //For autonomous & periodic
	int oldCounter;
	//Control buttons
	int reverseIntakeButton;
	int intakeButton;
	int launchButton;
	//Status variables
	boolean reverseButtonChanged;
	boolean reverseOn;
	boolean intakeOn;
	boolean launchOn;
	boolean button1Changed;
	boolean sensorStillTripped;
	//Modifier variables; modify current values & don't fit into other categories
	int clockwise;
	int counterClockwise;
	int intakeSpeedUp;
	int intakeSpeedDown;
	boolean reverseIntake;
	
	
		 
	    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	myRobot = new RobotDrive(0,1); 								//Initialize RobotDrive class- RobotDrive CANNOT be edited
    	stick = new Joystick(0); 									//assign joystick
    	CameraServer server; 										//Camera
    	server = CameraServer.getInstance(); 						//next code lines get camera image and displays it
	    server.setSize(50);
	    server.startAutomaticCapture(0); 						//needs to be found in the RoboRio online tool
	    ballSensor = new DigitalInput(0);  							//Initialize sensor to port 5
	    autonomousSwitch = new DigitalInput(1);						//keeps track of which autonomous mode to use
	    extraSwitch = new DigitalInput(2);							//RENAME WHEN WE USE THIS
	    intakeMotor = new Talon(2);									//Motor that controls intake wheels	    
	    bottomLaunchMotor = new Talon(3);
	    topLaunchMotor = new Talon(4);
	    intakeSpeed = .45;
	    topLaunchSpeed = .78;
	    bottomLaunchSpeed = .78;
	    clockwise = 1;
	    counterClockwise = -1;
	    intakeOn = false;
	    launchOn = false;
	    button1Changed = true;
	    intakeButton = 2;
	    launchButton = 1;
	    intakeSpeedUp = 12;
	    intakeSpeedDown = 11;
	    oldCounter = 0;
	    sensorStillTripped = false;
	    reverseIntake = false;
	    reverseIntakeButton = 7;
	    reverseButtonChanged = true;
	    reverseOn = false;
	    reverseIntakeSpeed = .45;
	    reverseMiddleSpeed = .78;
	    buttonTimerCounter = -1;
	    autoSpeed = .74;
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
    public void autonomousPeriodic() { 								//Autonomous actions
    	if (autonomousSwitch.get() == false){
    		if(autoLoopCounter < 50) 									//Check if we've completed 75 loops (approximately 2 seconds)
			{
				myRobot.drive(autoSpeed, 0.0); 								// drive forwards 30% speed
    			autoLoopCounter++;
			} else {
				myRobot.drive(0.0, 0.0); 								// stop robot
			}
    	}
    	
    	else {
    		if(autoLoopCounter < 160) 									//Check if we've completed 200 loops (approximately 2 seconds)
			{
				myRobot.drive(0.42, 0.0); 								// drive forwards 30% speed
    			autoLoopCounter++;
			} else {
				myRobot.drive(0.0, 0.0); 								// stop robot
			}
    	}
    	
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){ 
    	intakeMotor.set(0);
    	bottomLaunchMotor.set(0);
    	topLaunchMotor.set(0);
    	autoLoopCounter = 0;
    	buttonTimerCounter = 0;
    	oldCounter = 0;
    	intakeOn = false;
    	reverseOn = false;
    	if(extraSwitch.get() == true){
    		DriverStation.reportError("Switch on", false);
    	}
    	else {
    		DriverStation.reportError("He's dead, Jim", false);
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() { //Everything that occurs during operator control
    	/* myRobot.arcade drive TODO verify controls
    	 * stick.getY() controls orientation of forward/backwards 
    	 * stick.getX() controls orientation of turn left/right
    	 * stick.getRawButton(int) binds certain actions to joystick button (int)
    	 */
        /*
         * Joystick code
         */
    	autoLoopCounter += 1;    	
    	
    	myRobot.arcadeDrive(-stick.getY(), -stick.getX(), true);
    	
    	
    	
    	if (autoLoopCounter > buttonTimerCounter){

    		if(stick.getRawButton(intakeButton)){ 						//If intake button pushed
        		buttonTimerCounter = autoLoopCounter + 50;
    			intakeOn = !intakeOn; 								//If intake motor on, turn it off, or vise versa
        	}
    	}
        
    	if (autoLoopCounter > buttonTimerCounter){
    		
    		if(stick.getRawButton(reverseIntakeButton)){ 						//If intake button pushed
    			buttonTimerCounter = autoLoopCounter + 15;
    			reverseOn = (reverseOn) ? false : true;								//If intake motor on, turn it off, or vise versa
        		
    		}
    	}

        
        if(intakeOn) { 												//If the intake button is toggled on
        	intakeBall();											//Run intake method to start intake
        }else {
        	if (!reverseOn){
        		intakeMotor.set(0);									//Turn off the intake motor
        	}
												
        
        }
        
        	if(reverseOn) { 											//If the intake button is toggled on
        		
        		reverseBall();											//Run intake method to start intake
        	}else {
        		if (!intakeOn){
        			
        			bottomLaunchMotor.set(0);
        			intakeMotor.set(0);									//Turn off the intake motor
        		}
        											
        	}
        
        
        /*if (ballSensor.get()){ 									//Check sensor; if interrupted, shut off motors
        	sensorInterupt();
        }*/
        if (ballSensor.get() == true){ //test sensor
        	if (sensorStillTripped == false){
        		sensorInterupt();
        		sensorStillTripped = true;
        	}
        }
        else {
        	sensorStillTripped = false;
        }
       // else {
        //	if (sensorTripped){
       // 		sensorUninterupt();
      //  	}
      //  }
        
        
        if(stick.getRawButton(launchButton)){ 						//If launch button pressed
        	launchBall();											//Call launchball method while it is pressed
        }
        else { //Turn motor off
        	bottomLaunchMotor.set(0);								//If lauch button not pressed, then turn off launch motors
        	topLaunchMotor.set(0);
        }       
        
        //if(feedBall)
        //{
        //	intakeMotor.set(intakeSpeed*counterClockwise);
        //	intakeOn = true;  											        	
        //}
        
        if(autoLoopCounter > oldCounter + 10){
        	if (stick.getRawButton(intakeSpeedUp) && intakeSpeed <= 1){ 					//increase intake speed
        
        		intakeSpeed += .02;
        		DriverStation.reportError("\n" + (intakeSpeed), false);
        		oldCounter = autoLoopCounter;
        	}
        	if (stick.getRawButton(intakeSpeedDown) && intakeSpeed >= -1){ 				//decrease intake speed
        		intakeSpeed -= .02;
        		DriverStation.reportError("\n" + (intakeSpeed), false);
        		oldCounter = autoLoopCounter;
        	}
        }
    
    }    
    
    public void intakeBall() { 										//Turns on intake motor (motor 2) and turns off launch motors
    	intakeMotor.set(intakeSpeed*counterClockwise);
    	bottomLaunchMotor.set(0);
    	topLaunchMotor.set(0);    	
    }
    
    public void reverseBall() { 										//Turns on intake motor (motor 2) and turns off launch motors
    	intakeMotor.set(reverseIntakeSpeed*clockwise);
    	bottomLaunchMotor.set(reverseMiddleSpeed*clockwise);
    	topLaunchMotor.set(0);    	
    }
    
    public void launchBall() { 										//Turns on launch motors, waits a second, and then turns on intake motor 
    	intakeMotor.set(0);
    	bottomLaunchMotor.set(bottomLaunchSpeed*counterClockwise); 	//i implemented top and bottom motor speeds lol
    	topLaunchMotor.set(topLaunchSpeed*clockwise);
    	Timer.delay(1); 											//Wait 1 second
    	//intakeMotor.set(intakeSpeed*counterClockwise);
        //intakeOn = true; 	
        Timer.delay(1.5);
        //intakeMotor.set(0);
        intakeOn = false;
    }
    
    public void sensorInterupt() { //If sensor is tripped, turns off all motors
    	intakeMotor.set(.1*clockwise);
    	Timer.delay(.1);
    	bottomLaunchMotor.set(.2*clockwise);
    	topLaunchMotor.set(0);
    	
    	Timer.delay(.1);
    	bottomLaunchMotor.set(0);
    	intakeMotor.set(0);
    	intakeOn = false;
    }
    
    public void sensorUninterupt() { //If sensor is not tripped
    	intakeMotor.set(0);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
}
