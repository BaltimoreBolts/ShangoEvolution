package frc.robot;

// Import Talon and gyro libraries
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.analog.adis16470.frc.ADIS16470_IMU;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

//Import Library for Star Burst Motor/
import edu.wpi.first.wpilibj.VictorSP;

//Import AnalogPotentiometer/
import edu.wpi.first.wpilibj.AnalogPotentiometer;

import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.Relay;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class GPM {
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
  public DigitalInput LineSensorLeft, LineSensorCenter, LineSensorRight;

  //Define Pot/
  public AnalogPotentiometer DorsalPot, FourBarPot;
  
  public GPM(){
    //Initialize Motors for GPM/
    DorsalMotor1 = new WPI_VictorSPX(16);
    DorsalMotor2 = new WPI_TalonSRX(32);
    FourBarMotor1 = new WPI_VictorSPX(15);
    FourBarMotor2 = new WPI_VictorSPX(14);
    CargoIntakeMotor = new WPI_TalonSRX(17);
    StarBurstMotor = new VictorSP(0);
    
    //Initialize Limit Switches for GPM/
    DorsalLimitUp = new DigitalInput(0);
    DorsalLimitDown = new DigitalInput(1);
    FourBarFwd = new DigitalInput(2);
    FourBarBack = new DigitalInput(3);
    StarburstLimitOpen = new DigitalInput(5);
    StarburstLimitClose = new DigitalInput(6);
    
    //Initialize Cargo/Sensor Components for GPM/
    CargoSensor = new DigitalInput(4);
    LineSensorLeft = new DigitalInput(7);
    LineSensorCenter = new DigitalInput(8);
    LineSensorRight = new DigitalInput(9);
    //CargoLight = new Relay(2, Relay.Direction.kForward);

   //Initialize Potentiometers for GPM/
    DorsalPot = new AnalogPotentiometer(0);
    FourBarPot = new AnalogPotentiometer(1);

    // Initalize some smartdashboard stuff/
    CargoIntakeMotor.setName("motor","cargomotor");
    DorsalLimitUp.setName("limits","DorsalUp");
    DorsalLimitDown.setName("limits","DorsalDown");
    FourBarFwd.setName("limits","FourBarFwd");
    FourBarBack.setName("limits","FourBarDown");
    DorsalMotor1.setName("motor","DorsalMotor1");
    DorsalMotor2.setName("motor","DorsalMotor2");
    FourBarMotor1.setName("motor","FourBarMotor1");
    FourBarMotor2.setName("motor","FourBarMotor2");
    FourBarPot.setName("pot","4barpot");
    DorsalPot.setName("pot","dorsalpot");
    LineSensorLeft.setName("sensor","LeftLineSensor");
    LineSensorCenter.setName("sensor","CenterLineSensor");
    LineSensorRight.setName("sensor","RightLineSensor");
    //LiveWindow.addSensor("sensor","4barpot",FourBarPot);

  }
  public void goPineHome(){
    //set value for each MC for 4barBack and DorsalDown in CAN/
    this.FourBarMotor1.set(ControlMode.PercentOutput, 0.1 );
    this.FourBarMotor2.set(ControlMode.PercentOutput, 0.1);
    this.DorsalMotor1.set(ControlMode.PercentOutput, -0.275);
    this.DorsalMotor2.set(ControlMode.PercentOutput, -0.275);
  }

  public void goSpongeHatch(){
      // set value for each MC for 4barBack and DorsalDown in CAN/
      this.FourBarMotor1.set(ControlMode.PercentOutput, -0.15 );
      this.FourBarMotor2.set(ControlMode.PercentOutput, -0.15);
      this.DorsalMotor1.set(ControlMode.PercentOutput, -0.25);
      this.DorsalMotor2.set(ControlMode.PercentOutput, -0.25);

  }

  public boolean haveCargo(){
      return this.CargoSensor.get(); 
  }
    
    public void setCargoMotor(double value){
      this.CargoIntakeMotor.set(ControlMode.PercentOutput, value);
    }
  
  
}