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
import edu.wpi.first.wpilibj.GenericHID.Hand;

// Import Talon and gyro libraries
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.analog.adis16470.frc.ADIS16470_IMU;


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
  public XboxController xcontroller;
  public static final ADIS16470_IMU imu = new ADIS16470_IMU();
  
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
    LeftFront = new WPI_TalonSRX(2);
    RightFront = new WPI_TalonSRX(3);
    LeftBack = new WPI_TalonSRX(0);
    RightBack = new WPI_TalonSRX(1);
    RobotDT = new MecanumDrive(LeftFront, LeftBack, RightFront, RightBack);
    xcontroller = new XboxController(0);
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
    double x = xcontroller.getX(Hand.kLeft);
    double y = xcontroller.getTriggerAxis(Hand.kRight) - xcontroller.getTriggerAxis(Hand.kLeft);
    double z = xcontroller.getX(Hand.kRight);
    double yaw = imu.getAngleZ();

    // After you spin joystick R3 press A button to reset gyro angle
    if (xcontroller.getAButtonPressed()) {
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
