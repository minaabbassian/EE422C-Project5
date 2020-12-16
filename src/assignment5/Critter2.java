/*
 * CRITTERS GUI Critter2.java
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
 * Critter2 class
 * Critter2 is very very lazy.
 * Critter2 will only walk straight right to a position if it is unoccupied by any other critter
 * Critter2 does love babies and always chooses to reproduce if it has adequate energy left
 */
public class Critter2 extends Critter {
	private int dir;
	private int six;
	
	@Override
	/**
	 * doTimeStep
	 * Critter2 only walks and only does so every 6 turns
	 * Critter2 reproduces if it has adequate energy left
	 */
	public void doTimeStep() {
		//walks every 6 turns
		if(six == 0) {
			int dir = Critter.getRandomInt(8);
			six = 6;
			walk(dir);
			dir = (dir*Critter.getRandomInt(8) % 8);
		} else {
			six -= 1;
		}
					
		//reproduces if has adequate energy to do so 
		if(getEnergy() >= Params.MIN_REPRODUCE_ENERGY) {
			int direction = Critter.getRandomInt(8);
			Critter2 newBaby = new Critter2();
			reproduce(newBaby, direction);
		}
				
	}
	
	@Override
	/**
	 * fight
	 * @param match represents the opponent critter of Critter2 in a fight 
	 * @return boolean true if Critter2 fights, otherwise returns boolean false
	 * Critter2 always decides to run from Critter4 if Critter4 is one location next to it
	 * Critter2 decides to run from its opponent with a probability of 80%
	 */
	public boolean fight(String oponent) {
		// TODO Auto-generated method stub
		
		//first check if Critter4 is in any of the positions next to Critter2
		//If so, Critter2 runs in that direction
		String lookAround;
		for(int i = 0; i < 8; i++) {
			lookAround = look(i, false);
			if(!(lookAround == null)) {
	    		if(lookAround.equals("4")) {
	    			run(i);
	    		}
	    	} 
		}
		
		int prob = Critter.getRandomInt(100);
		
		//Critter2 only fights with a probability of 20%
		if(prob <= 20) {
			return true;
		}
		
		//Critter2 runs away with a probability of 80%
		int direction = Critter.getRandomInt(8);
		run(direction);
		return false;
	}
	
	
	/**
	 * toString
	 * Returns a String representation equal to '2' for Critter2
	 */
	public String toString() {
		return "2";
	}

    @Override
    public CritterShape viewShape() {
        return CritterShape.DIAMOND;
    }

    @Override
    public javafx.scene.paint.Color viewOutlineColor() {
        return javafx.scene.paint.Color.RED;
    }
    
    @Override
    public javafx.scene.paint.Color viewFillColor() {
        return javafx.scene.paint.Color.YELLOW;
    }
    
   

}
