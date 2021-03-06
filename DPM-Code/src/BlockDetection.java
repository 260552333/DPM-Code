import lejos.nxt.ColorSensor;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.Sound;
import lejos.util.Delay;
import lejos.util.Timer;
import lejos.util.TimerListener;

/**
 * BlockDetection.java
 * -figures out if a block is in front of the UltraSonic sensor
 * -if something is, it checks the color
 * @author Jit Kanetkar
 */
public class BlockDetection implements TimerListener{
	private final int MIN_DISTANCE = 20;
	private final int BLOCK_BLUE = 7;
	private static final int TIMER_PERIOD = 50;
	//private final int BLOCK_GREEN = 5;
	private UltrasonicPoller usPoller;
	private ColorSensor coSensor;
	private Color color;
	private Driver robot;
	private Object lock;
	private Timer timer;
	private boolean seesBlock = false;
	private boolean seesObject = false;
	/**
	 * Starts timer and sets local variables
	 * @param usPoller	gets the distance robot sees an object
	 */
	public BlockDetection(UltrasonicPoller usPoller, ColorSensor coSensor, Driver driver){
		this.coSensor = coSensor;
		this.usPoller = usPoller;
		this.lock = new Object();
		this.robot = driver;
		this.timer = new Timer(TIMER_PERIOD, this);
		
		timer.start();
	}
	@Override
	/**
	 * A timer run every 50ms which updates what the object detection Color and Ultrasonic sensors see.
	 * It constantly updates these values, which are called in the main method
	 */
	public void timedOut() {
		synchronized(lock){color = coSensor.getColor();}
		if(usPoller.getDistance() < MIN_DISTANCE){
			seesObject = true;
			detectBlock();
		}
		else {
			seesObject = false;
			seesBlock = false;
		}
	}
	/**
	 * updates seesBlock boolean based on the UltraSonic sensors' distance
	 */
	private void detectBlock(){
		//beeps if block is blue enough
		if(color.getBlue() > BLOCK_BLUE){			
			//Sound.beep();
			seesBlock = true;
		}
		else seesBlock = false;
	}
	/**
	 * @return whether a block is close to the UltraSonic sensors && has satisfactory colors
	 */
	public boolean seesBlock(){
		boolean boo;
		synchronized(lock){ boo = seesBlock;}
		return boo;
	}
	/**
	 * @return the Color object last seen by the ColorSensor
	 */
	public Color getColor(){
		Color col;
		synchronized(lock){ col = color;}
		return col;
	}
	/**
	 * @return the Blue seen by the ColorSensor
	 */
	public int getBlue() {
		int blue;
		synchronized(lock){ blue = color.getBlue();}
		return blue;
	}
	/**
	 * @return the Green seen by the ColorSensor
	 */
	public int getGreen(){
		int green;
		synchronized(lock){ green = color.getGreen();}
		return green;
	}
	/**
	 * @return the Red seen by the ColorSensor
	 */
	public int getRed(){
		int red;
		synchronized(lock){ red = color.getRed();}
		return red;
	}
	/**
	 * @return if the robot sees an object
	 */
	public boolean seesObject() {
		boolean boo;
		synchronized(lock){ boo = seesObject;}
		return boo;
	}
}
