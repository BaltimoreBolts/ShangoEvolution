/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;

//What does this mean????/
import edu.wpi.first.wpilibj.GenericHID.Hand;

//Import AnalogPotentiometer/
import edu.wpi.first.wpilibj.AnalogPotentiometer;

//Import Relay for Cargo Light/
import edu.wpi.first.wpilibj.Relay;

// Import Talon and gyro libraries
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.analog.adis16470.frc.ADIS16470_IMU;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

//Import Library for Star Burst Motor/
import edu.wpi.first.wpilibj.VictorSP;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  
  // Define macanumdrive variables
  public MecanumDrive RobotDT;
  public WPI_TalonSRX LeftFront;
  public WPI_TalonSRX RightFront;
  public WPI_TalonSRX LeftBack;
  public WPI_TalonSRX RightBack;
  
  //Define IMU for Robot Centric Drive/
  public static final ADIS16470_IMU imu = new ADIS16470_IMU();
  
  //Define Motors for GPM/
  public WPI_VictorSPX DorsalMotor1;
  public WPI_TalonSRX DorsalMotor2;
  public WPI_VictorSPX FourBarMotor1;
  public WPI_VictorSPX FourBarMotor2;
  public VictorSP StarBurstMotor;
  public WPI_TalonSRX CargoIntakeMotor;

  //Define Limit Switches for GPM/
  public DigitalInput DorsalLimitUp, DorsalLimitDown;
  public DigitalInput FourBarFwd, FourBarBack;
  public DigitalInput StarburstLimitOpen, StarburstLimitClose;
  public DigitalInput CargoSensor;

  //Define Pot/
  public AnalogPotentiometer DorsalPot, FourBarPot;
  
  //Define Xbox Joystick/
  public XboxController xcontroller1;
  public XboxController xcontroller2;
  
   //Define Relay/
  public Relay CargoLight;
  
  //Define camera variables
  public CameraServer RoboCam;
  public UsbCamera FrontCamera;
  public UsbCamera BackCamera;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    
    //Initialize Drive Train Motors/
    LeftFront = new WPI_TalonSRX(11);
    RightFront = new WPI_TalonSRX(13);
    LeftBack = new WPI_TalonSRX(10);
    RightBack = new WPI_TalonSRX(12);
    
    //Initialize Motors for GPM/
    DorsalMotor1 = new WPI_VictorSPX(16);
    DorsalMotor2 = new WPI_TalonSRX(32);
    FourBarMotor1 = new WPI_VictorSPX(15);
    FourBarMotor2 = new WPI_VictorSPX(14);
    CargoIntakeMotor = new WPI_TalonSRX(17);
    StarBurstMotor = new VictorSP(0);
    RobotDT = new MecanumDrive(LeftFront, LeftBack, RightFront, RightBack);
    
    //Initialize Limit Switches for GPM/
    DorsalLimitUp = new DigitalInput(0);
    DorsalLimitDown = new DigitalInput(1);
    FourBarFwd = new DigitalInput(2);
    FourBarBack = new DigitalInput(3);
    StarburstLimitOpen = new DigitalInput(5);
    StarburstLimitClose = new DigitalInput(6);
    
    //Initialize Cargo Components for GPM/
    CargoSensor = new DigitalInput(4);
    CargoLight = new Relay(2, Relay.Direction.kForward);
   //Initialize Potentiometers for GPM/
    DorsalPot = new AnalogPotentiometer(0);
    FourBarPot = new AnalogPotentiometer(1);
    
    //Initialize Xbox Controller or Joystick/
    xcontroller1 = new XboxController(0);
    xcontroller2 = new XboxController(1);
    
    //Initialize Cameras/
    RoboCam = CameraServer.getInstance();
    FrontCamera = RoboCam.startAutomaticCapture(0);
    BackCamera = RoboCam.startAutomaticCapture(1);
 
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    double x = xcontroller1.getX(Hand.kLeft);
    double y = xcontroller1.getTriggerAxis(Hand.kRight) - xcontroller1.getTriggerAxis(Hand.kLeft);
    double z = xcontroller1.getX(Hand.kRight);
    double yaw = imu.getAngleZ();

    // After you spin joystick R3 press A button to reset gyro angle
    if (xcontroller1.getAButtonPressed()) {
      imu.reset();

  }
   
   RobotDT.driveCartesian(x, y, z, 0);
  }
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
