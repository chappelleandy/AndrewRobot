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

public class Robot extends IterativeRobot {

	// Declaration of Variables:
	// Hands are for the Xbox controller, the HIDGeneric Class requires indication of left or right for certain things
	Hand LeftHand = GenericHID.Hand.kLeft;
	Hand RightHand = GenericHID.Hand.kRight;
	XboxController Controller = new XboxController(0);

	// Talons:
	WPI_TalonSRX DriveLeft1 = new WPI_TalonSRX(0);
	WPI_TalonSRX DriveLeft2 = new WPI_TalonSRX(1);
	WPI_TalonSRX DriveRight1 = new WPI_TalonSRX(2);
	WPI_TalonSRX DriveRight2 = new WPI_TalonSRX(3);
	WPI_TalonSRX Launcher = new WPI_TalonSRX(4); // might be used later for something
	WPI_TalonSRX Handler = new WPI_TalonSRX(5);//used to be 5, swapped bc of 5/7 testing
	WPI_TalonSRX Arms = new WPI_TalonSRX(6);
	WPI_TalonSRX ArmsRoller = new WPI_TalonSRX(7);


	// Brake mode and Coast mode
	NeutralMode Brake = NeutralMode.Brake;
	NeutralMode Coast = NeutralMode.Coast;

	// Groups of Talons for driving simplicity
	SpeedControllerGroup Left = new SpeedControllerGroup(DriveLeft1, DriveLeft2);
	SpeedControllerGroup Right = new SpeedControllerGroup(DriveRight1, DriveRight2);

	// Drive Base
	DifferentialDrive myDrive = new DifferentialDrive(Left, Right);

	// Encoders
	Encoder ArmsEncoder = new Encoder(8, 9); // DIO 8 and 9 for the Arms motor
	boolean MoveArmToEncoder;
	Encoder ShooterEncoder = new Encoder(4, 5); // DIO 4 and 5 for the Shooter motor
	int ShootingSpeed = 0;
	boolean IsShooting;
	boolean spitOut;
	// Limit Switches
	DigitalInput ArmSwitchAbsoluteStop = new DigitalInput(7);
	boolean MoveArmToLimit;
	DigitalInput HandlerSwitch = new DigitalInput(6);

	@Override
	public void teleopInit() {
		ArmsEncoder.reset();
	}
	
	@Override
	public void teleopPeriodic() {
		// Drive Function, takes controller input and outputs to Drive
		myDrive.arcadeDrive(-Controller.getY(LeftHand),  Controller.getX(RightHand), false);
		
		// Stow Arms
		if (ArmSwitchAbsoluteStop.get()) { // Check limit switch
			MoveArmToLimit = false;
			Arms.set(0);
			ArmsRoller.set(0);
		} else if (MoveArmToLimit) { // Drive motor if the logic tells it to
			Arms.set(1);
		} else if (Controller.getBButtonPressed() && !MoveArmToEncoder) { // if button pressed on controller, stop everything and retract
			MoveArmToLimit = true;
			ArmsRoller.set(0);
			Handler.set(0);
		}

		// Intake boulder
		if (ArmsEncoder.get() >= 795 && !MoveArmToLimit) { // Check encoder value, and if its in the out range, spin ArmsRoller
			MoveArmToEncoder = false;
			Arms.set(0);
			ArmsRoller.set(-1);
			Handler.set(-1);
			if (!HandlerSwitch.get()) { // If the Boulder is triggering the handler switch, move arms back in and stop the roller and handler
				ArmsRoller.set(0);
				Handler.set(0);
				MoveArmToLimit = true;
			}
		} else if (MoveArmToEncoder) {
			Arms.set(-1);
		} else if (Controller.getAButtonPressed() && !MoveArmToLimit) {
			MoveArmToEncoder = true;
		}

		// Spit out Boulder
		if (Controller.getYButtonReleased() && !IsShooting) {
			Handler.set(0);	
			spitOut = false;
		} else if (Controller.getYButtonPressed() && !IsShooting) {
			spitOut = true;
		} if (spitOut && !IsShooting) {
			Handler.set(1);
		}
		
		if (Controller.getTriggerAxis(RightHand) < 0.2 && !spitOut) {
			Handler.set(0);
			IsShooting = false;
		} else if (Controller.getTriggerAxis(RightHand) >= 0.2 && !spitOut) {
			IsShooting = true;
		} if (IsShooting && !spitOut) {
			Handler.set(-1);
		}
		
		if(Controller.getPOV() != -1) {
			if(Controller.getPOV() == 0) {
				Launcher.set(1);			} 
			if(Controller.getPOV() == 180) {
				Launcher.set(0);
			}
			if(Controller.getPOV() == 270) {
				Launcher.set(.7);
			}
		}
		
	}
}