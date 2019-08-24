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
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;

//Import Hand + Joystick control/
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Relay.Value;
//Import AnalogPotentiometer/
import edu.wpi.first.wpilibj.AnalogPotentiometer;

//Import Relay for Cargo Light/
import edu.wpi.first.wpilibj.Relay;

import com.ctre.phoenix.motorcontrol.ControlMode;
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
  
  //Define Xbox Joystick/
  public XboxController xcontroller1;
  public XboxController xcontroller2;
  
  //Define Relay/
  public Relay CargoLight;
  
  //Define camera variables
  public CameraServer RoboCam;
  public UsbCamera FrontCamera;
  public UsbCamera BackCamera;

  //Define GPM/
  private GPM mGPM;

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
    RobotDT = new MecanumDrive(LeftFront, LeftBack, RightFront, RightBack);
    
    //Initialize Xbox Controller or Joystick/
    xcontroller1 = new XboxController(0);
    xcontroller2 = new XboxController(1);
    
    //Initialize Cameras/
    RoboCam = CameraServer.getInstance();
    FrontCamera = RoboCam.startAutomaticCapture(0);
    BackCamera = RoboCam.startAutomaticCapture(1);

    //GPM Init/
    mGPM = new GPM();
 
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

    boolean c = mGPM.haveCargo();
    
    // Need signal due to distance for assurance that cargo is obtained/
    if (c) {
      CargoLight.set(Value.kOn);
    } else{
      CargoLight.set(Value.kOff);
    }
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
    double x = xcontroller2.getX(Hand.kLeft);
    //double y = xcontroller1.getTriggerAxis(Hand.kRight) - xcontroller1.getTriggerAxis(Hand.kLeft);
   // For testing just use left joystick. Use trigger axis code for GTA Drive/
    double y = -xcontroller2.getY(Hand.kLeft);
    double z = xcontroller2.getX(Hand.kRight);
    double yaw = imu.getAngleZ();
    RobotDT.driveCartesian(x, y, 0, 0);
 

    // After you spin joystick R3 press A button to reset gyro angle
    if (xcontroller1.getAButtonPressed()) {
      imu.reset();
  }
    
  if (xcontroller2.getBButtonPressed()){
       mGPM.goSpongeHatch();
  }

  if (xcontroller2.getXButtonPressed()){
      mGPM.goPineHome();
   
    }


    /** 
     * If Bumpers are pressed then Cargo Intake Motor = -1 for Intake  
     *if Triggers are pressed set value to 1 for Outtake
     *If triggers are released set value to 0*/
    if(xcontroller2.getBumperPressed(Hand.kLeft) && xcontroller2.getBumperPressed(Hand.kRight)) {
    mGPM.setCargoMotor(-1);
    } else if((xcontroller2.getTriggerAxis(Hand.kLeft) >0.75) && (xcontroller2.getTriggerAxis(Hand.kRight) >0.75)) {
      mGPM.setCargoMotor(1);
    } else{
    mGPM.setCargoMotor(0);
    }
  }
    
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {

    LiveWindow.updateValues();
  }
}
