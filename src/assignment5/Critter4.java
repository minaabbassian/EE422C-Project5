/*
 * CRITTERS GUI Critter4.java
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
 * Critter4 class
 * Critter4 is a cheetah that runs randomly every turn
 * Critter4 loves to create children, and will do so every turn if it has twice the required energy
 * Critter4 will always fight with other critters unless they are clovers
 * Critter4 never chooses to fight with a clover.
 *
 */
public class Critter4 extends Critter {
	
	private int dir;

	
	@Override
	public void doTimeStep() {
		//runs in any direction
		int rand = Critter.getRandomInt(8);
		run(dir);
		//reproduces if has adequate energy to do so 
		if(getEnergy() >= 2*Params.MIN_REPRODUCE_ENERGY) {
			int direction = Critter.getRandomInt(8);
			Critter4 newBaby = new Critter4();
			reproduce(newBaby, direction);
		}
	}

	
	@Override
	/**
	 * fight 
	 * @param match represents the String opponent that Critter4 is fighting 
	 * @return boolean true if Critter4 fights, and boolean false if Critter4 does not fight 
	 * Critter4 will fight 100% of the time against anything but a clover
	 * It will not fight a clover
	 */
	public boolean fight(String match) {
		//fights 100% of the time against a clover
		if(match.contentEquals("@")) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * toString
	 * Returns a string representation equal to "4" representing Critter4
	 */
	public String toString() {
		return "4";
	}

    @Override
    public CritterShape viewShape() {
        return CritterShape.STAR;
    }

    @Override
    public javafx.scene.paint.Color viewOutlineColor() {
        return javafx.scene.paint.Color.MAGENTA;
    }
    
    @Override
    public javafx.scene.paint.Color viewFillColor() {
        return javafx.scene.paint.Color.GREENYELLOW;
    }
    
   

}