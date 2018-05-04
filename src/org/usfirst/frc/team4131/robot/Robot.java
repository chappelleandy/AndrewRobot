package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
	//Declaration of Variables:
	
	//Hands are for the Xbox controller, the HIDGeneric Class requires indication of left or right for certain things
	Hand LeftHand = GenericHID.Hand.kLeft;
	Hand RightHand = GenericHID.Hand.kRight;	
	XboxController Controller = new XboxController(0);
	
	//Talons:
	WPI_TalonSRX DriveLeft1 = new WPI_TalonSRX(0); 
	WPI_TalonSRX DriveLeft2 = new WPI_TalonSRX(1);
	WPI_TalonSRX DriveRight1 = new WPI_TalonSRX(2);
	WPI_TalonSRX DriveRight2 = new WPI_TalonSRX(3);
	WPI_TalonSRX Unused = new WPI_TalonSRX(4); //might be used later for something
	WPI_TalonSRX Handler = new WPI_TalonSRX(5);
	WPI_TalonSRX Arms = new WPI_TalonSRX(6);
	WPI_TalonSRX ArmsRoller = new WPI_TalonSRX(7);
	WPI_TalonSRX Launcher = new WPI_TalonSRX(8);
	
	//Brake mode and Coast mode
	NeutralMode Brake = NeutralMode.Brake;
	NeutralMode Coast = NeutralMode.Coast;
	
	//Groups of Talons for driving simplicity
	SpeedControllerGroup Left = new SpeedControllerGroup(DriveLeft1, DriveLeft2);
	SpeedControllerGroup Right = new SpeedControllerGroup(DriveRight1, DriveRight2);
	
	//Drive Base
	DifferentialDrive myDrive = new DifferentialDrive(Left, Right);
	
	//Encoders
	Encoder ArmsEncoder = new Encoder(8,9); //DIO 8 and 9 for the Arms motor
	Encoder ShooterEncoder = new Encoder(4,5); //DIO 4 and 5 for the Shooter motor
	
	//Limit Switches
	DigitalInput ArmSwitchAbsoluteStop = new DigitalInput(7);
	DigitalInput HandlerSwitch = new DigitalInput(6);
	
	@Override
	public void teleopPeriodic() {
		//Drive Function, takes controller input and outputs to Drive
		myDrive.arcadeDrive(Controller.getY(LeftHand), Controller.getX(RightHand), false);
    	
		//Stow Arms
		//if(Controller.getXButtonPressed() && )
    	System.out.println("ArmsEncoder: " + ArmsEncoder.get());
    	
    	
	}

}