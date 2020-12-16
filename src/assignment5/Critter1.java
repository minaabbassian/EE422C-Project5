/*
 * CRITTERS GUI Critter1.java
 * EE422C Project 5 submission by
 * Replace <...> with your actual data.
 * Mina Abbassian
 * mea2947
 * 16170
 * Abdullah Haris
 * ah52897
 * 16185
 * Slip days used: <0>
 * Git URL: https://github.com/EE422C/fall-2020-pr5-fa20-pr5-pair-80.git
 * Fall 2020
 */

package assignment5;

import assignment5.Critter.CritterShape;
import assignment5.Critter.TestCritter;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;
import java.util.List;

/**
 * Critter1 class
 * Critter1 is also very stubborn and will not reproduce, because she does not like children.
 * Critter1 always chooses to run if she has enough energy to do so
 * Critter1 is scared of Critter3 from above and moves downward if Critter3 is above her 
 * If any other Critter is above her, Critter1 moves directly to the right 
 * If no critter is above Critter1, she runs directly up
 * 
 * 
 */
public class Critter1 extends Critter {
	
	private int direction;
	
	
	@Override
	public void doTimeStep() {
		//Checks vertically up one location
		direction = 2;
		String lookResult = look(direction, false);
    	if(!(lookResult == null)) {
    		if(lookResult.equals("3")) {
    			direction = 6; //move down if Critter3 is there
    		} else {
    			direction = 0; //else, move to the right if any other Critter is there
    		}
    		
    	} else {
    		direction = 2; //move up if nothing there
    	}
    	
        if(this.getEnergy() < Params.RUN_ENERGY_COST) {
        	walk(direction);
        } else {
        	run(direction);
        }
    }

	
	@Override
	/**
	 * fight 
	 * @param match represents the String opponent that Critter1 is fighting 
	 * @return boolean true if Critter1 fights, and boolean false if Critter1 does not fight 
	 * Critter1 will look one direction, and will run in that direction if it finds Critter3
	 * 		Otherwise, it will walk in that direction
	 */
	public boolean fight(String match) {
		//fights 100% of the time against a clover
		if(match.contentEquals("@")) {
			return true;
		}
		
		//fights 50% of the time
		int rand = Critter.getRandomInt(1);
		if(rand == 0) {
			return false; 
		}
		return true;
	}
	
	
	/**
	 * toString
	 * Returns a string representation equal to "1" representing Critter1
	 */
	public String toString() {
		return "1";
	}

    @Override
    public CritterShape viewShape() {
        return CritterShape.TRIANGLE;
    }

    @Override
    public javafx.scene.paint.Color viewOutlineColor() {
        return javafx.scene.paint.Color.BLUE;
    }
    
    @Override
    public javafx.scene.paint.Color viewFillColor() {
        return javafx.scene.paint.Color.RED;
    }
    
  

}
