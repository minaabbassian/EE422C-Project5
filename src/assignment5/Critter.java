/*
 * CRITTERS GUI Critter.java
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

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/*
 * See the PDF for descriptions of the methods and fields in this
 * class.
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {


    int energy = 0;

    int x_coord;
    int y_coord;
    
    boolean hasMoved = false;
    boolean isFight = false;

  //  private static List<Critter> population = new ArrayList<Critter>();
  //  private static List<Critter> babies = new ArrayList<Critter>();
    
    static HashMap<String, ArrayList<Critter>> population = new HashMap<String, ArrayList<Critter>>();
    static HashMap<String, ArrayList<Critter>> babies = new HashMap<String, ArrayList<Critter>>();
    //holds the critters that have moved grid positions in a given worldTimeStep
    static HashMap<String, ArrayList<Critter>> populationMoved = new HashMap<String, ArrayList<Critter>>();
    

    /* Gets the package name.  This assumes that Critter and its
     * subclasses are all in the same package. */
    private static String myPackage;
    public static String myPackage1 = Main.class.getPackage().toString().split(" ")[1];

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /* START --- NEW FOR PROJECT 5 */
    public enum CritterShape {
        CIRCLE,
        SQUARE,
        TRIANGLE,
        DIAMOND,
        STAR
    }

    /* the default color is white, which I hope makes critters invisible by default
     * If you change the background color of your View component, then update the default
     * color to be the same as you background
     *
     * critters must override at least one of the following three methods, it is not
     * proper for critters to remain invisible in the view
     *
     * If a critter only overrides the outline color, then it will look like a non-filled
     * shape, at least, that's the intent. You can edit these default methods however you
     * need to, but please preserve that intent as you implement them.
     */
    public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.WHITE;
    }

    public javafx.scene.paint.Color viewOutlineColor() {
        return viewColor();
    }

    public javafx.scene.paint.Color viewFillColor() {
        return viewColor();
    }

    public abstract CritterShape viewShape();
    
    

    /**
     * look
     * Examines the location identified by the Critter's current coordinates 
     * 	and moving one or two positions in the indicated direction 
     * If steps is false, looks one location
     * If steps is true, looks two locations 
     * The Critter invoking look() will pay the LOOK_ENERGY_COST
     * @param direction
     * @param steps
     * @return If the location is unoccupied, look() returns null
     * 		   If the location is occupied, then look() returns the toString() result 
     * 				for the Critter in that location
     */
    protected final String look(int direction, boolean steps) {
    	
    	//pay the LOOK_ENERGY_COST
    	energy = energy - Params.LOOK_ENERGY_COST;
    	int look;
    	//get the x and y coordinates of the critter
    	int xCoord = this.x_coord;
    	int yCoord = this.y_coord;
    	
    	if(steps == true)
    		look = 2; //look 2 steps
    	else 
    		look = 1; //look 1 step
    	
    	//get the coordinate position of the location to look
    	switch(direction) {
    	case 0:
    		xCoord += look; //straight right 
    		break;
    		
    	case 1:
    		xCoord += look; //diagonally up and to the right
    		yCoord -= look;
    		break;
    		
    	case 2:
    		yCoord -= look; //straight up
    		break;
    		
    	case 3: 
    		xCoord -= look; //diagonally up and to left
    		yCoord -= look;
    		break;
    		
    	case 4:
    		xCoord -= look; //straight left
    		break;
    	
    	case 5:
    		xCoord -= look; //diagonally down and to left
    		yCoord += look; 
    		break;
    		
    	case 6:
    		yCoord += look; //diagonally down
    		break;
    		
    	case 7:
    		xCoord += look; //diagonally down and to right
    		yCoord += look;
    		break;
    	}
    	
    	//wrap-around world, need to correct coordinates
    	if(xCoord > (Params.WORLD_WIDTH - 1))
    		xCoord %= Params.WORLD_WIDTH;
    	else if(xCoord < 0)
    		xCoord += Params.WORLD_WIDTH;
    	if(yCoord > (Params.WORLD_HEIGHT - 1))
    		yCoord %= Params.WORLD_HEIGHT;
    	else if(yCoord < 0)
    		yCoord += Params.WORLD_HEIGHT;
    
    	//Iterate through population hashMap
    	Iterator<String> positionIter = population.keySet().iterator();
    	//Iterate through all of the position keys
    	while(positionIter.hasNext()) {
    		//get the position key
    		String position = positionIter.next();
    		//get the critter list in that position
    		ArrayList<Critter> critterList = population.get(position);
    		//iterate through the critters 
    		ListIterator<Critter> currCritter = critterList.listIterator();
    		while(currCritter.hasNext()) {
    			//get a critter
    			Critter c = currCritter.next();
    			//check whether each critter is in the position
    			if(c.x_coord == xCoord && c.y_coord == yCoord) {
    				if(c.getEnergy() >= 0) {
    					return c.toString();
    				}
    			}
    		}
    	}
    	
    	//return null if the position is unoccupied
    	return null;
    }

    
    /**
     * runStats
     * @param critters List of Critters
     * @return String of how many Critters of each type there are 
     * 			on the board
     */
    public static String runStats(List<Critter> critters) {
    	//String of Critter Statistics 
    	String critterStats = "" + critters.size() + " critters as follows -- ";
    	//Map holding Critter String and number of Critters
    	Map<String, Integer> critterMap = new HashMap<String, Integer>();
    	//Go through all of the critters 
    	for (Critter crit : critters) {
            String critterString = crit.toString();
            critterMap.put(critterString, critterMap.getOrDefault(critterString, 0) + 1);
        }
    	//Create the stats String for each Critter subclass 
    	String stat = "";
    	//go through all of the critters in each Critter subclass
    	for (String one : critterMap.keySet()) {
    		critterStats = critterStats.concat(stat + one + ":" + critterMap.get(one));
    		stat = ", ";
    	}
        return critterStats;
    }
    

    /**
     * displayWorld
     */
    public static void displayWorld() {
        // TODO Implement this method
    	
    	//first, clear the children 
    	Main.world.getChildren().clear();
    	
    	//the grid of the world 
    	for(int i = 0; i < Params.WORLD_HEIGHT; i++) {
    		for(int j = 0; j < Params.WORLD_WIDTH; j++) {
    			Shape s = new Rectangle(Main.shapeSize, Main.shapeSize);
    			s.setFill(null); //empty because it's just an outline
    			s.setStroke(Color.WHITE); //stroke is the outline color
    			Main.world.add(s, j, i); //row index is i, column index is j
    		}
    	}
    	
    	//printing the critters
    	Iterator<String> positionIter = population.keySet().iterator();
    	while(positionIter.hasNext()) {
    		//get the position key
    		String position = positionIter.next();
    		//get the critter list in that position
    		ArrayList<Critter> critterList = population.get(position);
    		//iterate through the critters 
    		ListIterator<Critter> currCritter = critterList.listIterator();
    		while(currCritter.hasNext()) {
    			//get a critter
    			Critter c = currCritter.next();
    			putCritter(c.viewFillColor(), c.viewOutlineColor(), c.x_coord, c.y_coord, c.viewShape());

    		}
    	}
    	
    }
    
    
    /**
     * ADDED!!
     * putCritter
     * Helper method for displayWorld()
     * Places a Critter in the grid world 
     * @param s CritterShape, either Circle, Square, Triangle, Diamond, or Star
     * @param o Color that the Critter shape is outlined with
     * @param f Color that the Critter shape is filled with
     * @param x x-coordinate of the Critter's position
     * @param y y-coordinate of the Critter's position
     */
    private static void putCritter(Color f, Color o, int x, int y, CritterShape s) {
    	int dim = Main.shapeSize;
    	int cellSize = Main.cellSize;
    	Shape critterShape = null;
    	double len = (double)dim/2.0;
    	double len2 = (double)dim - 1.0;
    	double len3 = (double)cellSize - 1.0;
    	
    	switch(s) {
    	case CIRCLE:
    		critterShape = new Circle((dim-1)/2);
    		critterShape.setStroke(o);
    		critterShape.setFill(f);
    		break;
    		
    	case SQUARE:
    		critterShape = new Rectangle(cellSize-3, cellSize-3);
    		critterShape.setStroke(o);
    		critterShape.setFill(f);
    		break;
    		
    	case TRIANGLE:
    		Polygon p = new Polygon();
    		Double[] dimensions = {len, 1.0, len2, len2, 1.0, len2};
    		p.getPoints().addAll(dimensions);
    		critterShape = p;
    		critterShape.setStroke(o);
    		critterShape.setFill(f);
    		break;
    		
    	case DIAMOND:
    		Polygon d = new Polygon();
    		Double[] values = {len, 1.0, len2, len, len, len2, 1.0, len};
    		d.getPoints().addAll(values);
    		critterShape = d;
    		critterShape.setStroke(o);
    		critterShape.setFill(f);
    		break;
    		
    	case STAR:
    		Polygon st = new Polygon();
    	
    		Double[] v = {(len3)*5.5/24.0, (len3)*12.0/24.0, (len3)*1.0/24.0, (len3)*21.0/24.0, 
    					(len3)*9.5/24.0, (len3)*17.0/24.0, (len3)*16.0/24.0, (len3)*23.0/24.0, 
    					(len3)*16.0/24.0, (len3)*16.0/24.0, (len3)*23.0/24.0, (len3)*12.0/24.0, 
    					(len3)*16.0/24.0,(len3)*8.0/24.0, (len3)*16.0/24.0, (len3)*1.0/24.0, 
    					(len3)*9.5/24.0, (len3)*7.0/24.0, (len3)*1.0/24.0,(len3)*4.0/24.0 };
    			
    		
    		st.getPoints().addAll(v);  
    		critterShape = st;
    		critterShape.setStroke(o);
    		critterShape.setFill(f);
    		break;
    	
    	default:
    		break;
    	}
    	
    	//add the critter to the critter world
    	Main.world.add(critterShape, x, y);
    }

   
	/* END --- NEW FOR PROJECT 5
			rest is unchanged from Project 4 */

    private static Random rand = new Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new Random(new_seed);
    }

    
    /**
     * createCriter
     * create and initialize a Critter subclass.
     * critter_class_name must be the unqualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown.
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void createCritter(String critter_class_name)
            throws InvalidCritterException {
  
    	try {
    		String name = myPackage + "." + critter_class_name;
    		Class added = Class.forName(name);
    		
    		//throws InvalidCritterException if cannot make the critter
    		if((critter_class_name == "Critter") || (!Critter.class.isAssignableFrom(added))){
    			throw new InvalidCritterException(critter_class_name);
    		}
    		
    		//create an instance and then cast that to a critter
    		Object inst = added.newInstance();
    		Critter newCritt = (Critter) inst;
    		
    		//initialize critter's parameters
    		newCritt.hasMoved = false;
    		newCritt.isFight = false;
    		newCritt.energy = Params.START_ENERGY;
    		newCritt.y_coord = getRandomInt(Params.WORLD_HEIGHT);
    		newCritt.x_coord = getRandomInt(Params.WORLD_WIDTH);
    		
    		//get the string coordinates of the critter's position
    		String pos = newCritt.x_coord + "_" + newCritt.y_coord;
    		
    		//if position is not in the hash map as a key yet, add it to the population hash map
    		if(!(population.containsKey(pos))) {
    			ArrayList<Critter> critterList = new ArrayList<Critter>();
    			critterList.add(newCritt);
    			population.put(pos, critterList);
    		}
    		
    		//if position is already in the hash map as a key
    		else {
    			ArrayList<Critter> list = population.get(pos);
    			list.add(newCritt);
    			population.replace(pos, list);
    		}
    		//Main.createSound();
    	}
    	
    	//catch an exception
    	catch (Exception e) {
    		throw new InvalidCritterException(critter_class_name);
    	}
    }

    
    
    /**
     * doEncounters
     * Traverse every single possible pairs of critters in the same position in the world 
     * Resolve all encounters between these pairs of critters A and B
     * Invoke the A.fight(B.toString()) method to determine how A wants to respond
     * Invoke the B.fight(A.toString()) method to determine how B wants to respond
     * If A and B are both still alive, generates two random numbers 
     * The critter that rolls the higher number wins and survives the encounter
     * If a critter loses a fight, then 1/2 of that loser's energy is awarded to the winner
     * The loser is dead and is removed from the critter collection before the end of this world time step
     */
    public static void doEncounters() {
    	
    	Iterator<String> populationIter = population.keySet().iterator();
		while (populationIter.hasNext()) { 
	        String pos = populationIter.next();
	        ArrayList<Critter> critterList = population.get(pos);
	        if(critterList.size() > 1) {
	        	int[] coords = stringToPos(pos);
	        	//get the integer value of the coordinates from the String key
	        	int x_copy = coords[0];
	        	int y_copy = coords[1];
	        	
	        	for(int i = 0; i < critterList.size(); i++) {
	        		for (int j = i+1; j < critterList.size(); j++) {
	        			Critter A = critterList.get(i);
	        			Critter B = critterList.get(j);
	        			if(A.getEnergy() > 0 && B.getEnergy() > 0 && A.x_coord == x_copy && A.y_coord == y_copy && B.x_coord == x_copy && B.y_coord == y_copy) {
	        				
	        				//Critters A and B are fighting
	        				A.isFight = true;
	        				B.isFight = true;
	        				
	        				//boolean AWantsToFight = A.fight(B.toString());
	        				//boolean BWantsToFight = B.fight(A.toString());
	        				int skillA = 0;
	        				int skillB = 0;
	        				
	        				//determine how A wants to respond
	        				if(A.fight(B.toString()) == true) {
	        					skillA = getRandomInt(A.energy);
	        				}
	        				
	        				//determine how B wants to respond
	        				if(B.fight(A.toString()) == true) {
	        					skillB = getRandomInt(B.energy);
	        				}
	        				if(A.x_coord == x_copy && B.x_coord == x_copy && A.y_coord == y_copy && B.y_coord == y_copy) {
	        					if(skillA > skillB) { // A wins
	        						A.energy += (int) Math.floor(B.energy*1.0*0.5);
	        						B.energy = 0;
	        					} else if (skillB > skillA) {
	        						B.energy += (int) Math.floor(A.energy*1.0*0.5); //B wins
	        						A.energy = 0;
	        					} else {
	        						if(getRandomInt(1) == 0) { // A wins
		        						A.energy += (int) Math.floor(B.energy*1.0*0.5);
		        						B.energy = 0;
	        						} else {
		        						B.energy += (int) Math.floor(A.energy*1.0*0.5); //B wins
		        						A.energy = 0;
	        						}
	        					}
	        				}
	        				
	        				//Critters A and B have completed their fight
	        				A.isFight = false;
	        				B.isFight = false;
	        				
	        				critterList.set(i, A);
	        				critterList.set(j, B);
	        			}
	        		}
	        	}
	        	
	        	//Iterate through the critters in that position
	        	Iterator<Critter> crittIter = critterList.iterator();
	        	while(crittIter.hasNext()) {
	        		Critter c = crittIter.next();
	        		//remove critters who have moved out of that grid position or who have died
	        		if(c.x_coord != x_copy || c.y_coord != y_copy || (c.energy <= 0)) {
	        			crittIter.remove();
	        		}
	        	}
	        	population.replace(pos, critterList);
	        }
		}
    }
    
    
    /**
     * ADDED!!
     * getCritterPosition
     * @return String representation of the critter's coordinates
     */
    String getCritterPosition() {
    	return this.x_coord + "_" + this.y_coord;
    }
    
    
    /**
     * ADDED!!
     * remakeMap
     * helper method for the worldTimeStep() method
     * Remakes the hash map based on the new x and y coordinate positions of critters
     * @param population
     */
    private static void remakeMap(HashMap<String, ArrayList<Critter>> population)
    {
    	//get a copy of the hash map
    	HashMap<String, ArrayList<Critter>> copy = new HashMap<String, ArrayList<Critter>>(population);
    	//clear the original hash map
    	population.clear();
    	
    	//iterates through all of the critters in the copy of the hash map
    	Iterator<String> populationIter = copy.keySet().iterator();
    	
    	while (populationIter.hasNext()) { 
    		
    		//get the String position
            String pos = populationIter.next();
            
            //get the critter list in that position
            ArrayList<Critter> critterList = copy.get(pos);
            
            //Iterate through the critters ArrayList 
            ListIterator<Critter> currCritter = critterList.listIterator();
            while(currCritter.hasNext()) {
            	Critter thisCritter = currCritter.next();
            	//get the new coordinates of the critter
            	String critterKey = thisCritter.getCritterPosition();
            	
            	//if the position is already a key in the hash map
            	if(population.containsKey(critterKey)) {
            		//add the critter to the ArrayList at key of coordinates
            		ArrayList<Critter> list = population.get(critterKey);
            		list.add(thisCritter);
            		population.replace(critterKey, list);
            	}
           
            	//if position is not already a key in the hash map
            	else {
            		ArrayList<Critter> newCritters = new ArrayList<Critter>();
            		newCritters.add(thisCritter);
            		population.put(critterKey, newCritters);
            	}
            }
    	}
    }
    
    
    /**
     * ADDED!!
     * stringToPos
     * @param coordinates String holding the x and y coordinates of the critter's position
     * @return Integer array holding the x and y position coordinates of the critter
     */
    private static final int[] stringToPos(String coordinates) {
    	//split the string between x and y coordinates
    	String[] splitString = coordinates.split("_");
    	int[] pos = new int[splitString.length];
    	int j = 0;
    	for(String c : splitString) {
    		pos[j] = Integer.parseInt(c);
    		j++;
    	}
    	return pos;
    }
    
    
    /**
     *
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.
     *        Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name)
            throws InvalidCritterException {
    	
    	//get the class name of the critter
    	String n = myPackage + "." + critter_class_name;
    	
    	List<Critter> critts = new ArrayList<Critter>();
    	Iterator<String> populationIter = population.keySet().iterator();
		while (populationIter.hasNext()) { 
	        String position = populationIter.next();
	        ArrayList<Critter> critterList = population.get(position);
	       
	        //get the instances of all critters that match the class name
	        for(int j = 0; j < critterList.size(); j++) {
	        	Critter c = critterList.get(j);
	        	if(c.getClass().getName().equals(n)){
	        		critts.add(c);
	        	}
	        }
		}
        return critts;
    }

    
    /**
     * clearWorld
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
    	babies.clear();
    	population.clear();
    }

    
    /**
     * worldTimeStep
     * Simulates one time step for every Critter in the critter collection 
     * Invokes the doTimeStep() method on every living critter in the critter collection
     * Fixes the population by removing and culling all critters that have died during the time step 
     * Once all critters have moved in the time step, resolves the encounters between pairs of critters
     * Inserts any new critters that have been reproduced or have moved in the world
     * Generates Clover critters using the values specified in Params.java and adds them to the population
     */
    public static void worldTimeStep() {
    	
    	//remake the hash map based on the x and y coordinates of the critters
    	remakeMap(population);
    	
    	//Iterate through the grid positions in the population HashMap
    	Iterator<String> populationIter = population.keySet().iterator();
    	while (populationIter.hasNext()) { 
            String pos = populationIter.next();
            ArrayList<Critter> critterList = population.get(pos);
    
            //Iterate through the critters ArrayList 
            ListIterator<Critter> currCritter = critterList.listIterator();
            while(currCritter.hasNext()) {
            	Critter thisCritter = currCritter.next();
            	thisCritter.hasMoved = false;
            	thisCritter.doTimeStep();
            	if(thisCritter.hasMoved || thisCritter.getEnergy() <= 0) {
            		currCritter.remove();
            	} else {
            		currCritter.set(thisCritter);
            	}
            }
            population.replace(pos, critterList);
        }
    	
    	fixPopulation();
    	mergePopulationMoved(populationMoved);
    	
    	doEncounters();
    	
    	fixPopulation();
    	mergePopulationMoved(populationMoved); //Stage 1 Complete
    
    	//deduct resting energy cost from all critters in the hash map
    	//could do a FOR EACH loop instead of iterator -- fixed
    	populationIter = population.keySet().iterator();
    	while(populationIter.hasNext()) {
    		String pos = populationIter.next();
            ArrayList<Critter> critterList = population.get(pos);
            
            //Iterate through the Critters attached to the keys in the Hash Map
            ListIterator<Critter> currCritter = critterList.listIterator();
            while(currCritter.hasNext()) {
            	Critter thisCritter = currCritter.next();
            	//deduct the rest energy cost from each critter
            	thisCritter.energy = thisCritter.energy - Params.REST_ENERGY_COST;
            	//remove all critters that have died after reducing the rest energy cost
            	if(thisCritter.getEnergy() <= 0) {
            		currCritter.remove();
            	} else {
            		currCritter.set(thisCritter);
            	}
            }
            population.replace(pos, critterList);
    	}
    	
    	mergePopulationMoved(babies);
    	
    	
    	//add the clovers in each refresh of the world 
    	try {
    		for(int j = 0; j < Params.REFRESH_CLOVER_COUNT; j++) {
    			createCritter("Clover");
    		}
    	} catch(InvalidCritterException p) {
    		p.printStackTrace();
    	}
    }
    
    
    /**
     * ADDED!!
     * mergePopulationMoved
     * Used as a helper function in worldTimeStep()
     * Merges the critters that have moved to the population hash map
     * @param changed hash map with the critters that have moved in during a world time step
     */
    private static void mergePopulationMoved(HashMap<String, ArrayList<Critter>> changed) {
    	//iterator through the critters that have moved
    	Iterator<String> populationMovedIterator = changed.keySet().iterator();
    	while (populationMovedIterator.hasNext()) { 
    		//get the position of a moved critter
            String pos = populationMovedIterator.next();
            ArrayList<Critter> critterList = population.get(pos);
            
            //if there are critters in that grid position
            if(!(critterList == null)) {
            	Iterator<Critter> positionChanged = changed.get(pos).iterator();
            	while(positionChanged.hasNext()) {
            		critterList.add(positionChanged.next());
            	}
            	population.replace(pos, critterList);
            
            //if there are critters already keyed onto that position
            } else {
            	ArrayList<Critter> newCritterList = new ArrayList<Critter>();
            	Iterator<Critter> positionChanged = changed.get(pos).iterator();
            	while(positionChanged.hasNext()) {
            		newCritterList.add(positionChanged.next());
            	}
            	//put the critter in the population hash map in its new position
            	population.put(pos, newCritterList);
            }
            
        }
    	//clear the moved population hash map to get ready for the new moved critters
    	changed.clear();
    }
    
    
    /**
     * ADDED!!
     * fixPopulation
     * Used in worldTimeStep() as a helper method
     * Deletes the keys in the population hash map that do not have any critters in it anymore 
     * 		when the critters move out of a coordinate position or when they die
     */
    private static void fixPopulation() {
    	//get a copy of the population hash map
    	HashMap<String, ArrayList<Critter>> copy = new HashMap<String, ArrayList<Critter>>(population);
    	Iterator<String> populationIterator = copy.keySet().iterator();
    	while (populationIterator.hasNext()) { 
            String pos = populationIterator.next();
            ArrayList<Critter> critterList = copy.get(pos);
            //clear the key if there is no more critters in that space
            if(critterList.size() == 0 || critterList == null) {
            	population.remove(pos);
            }
    	}
    }
    
    
    public abstract void doTimeStep();

    
    public abstract boolean fight(String oponent);

    
    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }

    
    protected int getEnergy() {
        return energy;
    }

    
    //Always add critter to populationMoved after moving.
    /**
     * walk
     * Moves a critter one position in one of the eight directions
     * Deducts Params.WALK_ENERGY_COST from the critter that invokes it 
     * @param direction
     */
    protected final void walk(int direction) {
    	//deduct this from the critter's energy
    	energy = energy - Params.WALK_ENERGY_COST;
    	
    	if(hasMoved == false) {
    		if((isFight == false || (isFight == true && unoccupiedPosition(direction, 1) == true)) && energy > 1) {
    			//get new critter position
    			int[] critterCoord = getCritterPos(direction, 1);
    			x_coord = critterCoord[0];
    			y_coord = critterCoord[1];
    			
    			hasMoved = true;
    			isFight = false;
    		
    			String critterPos = Integer.toString(x_coord) + "_" + Integer.toString(y_coord);
    			
    			ArrayList<Critter> critterList = populationMoved.get(critterPos);
    			
    			//if there are critters already in this grid position
    			if(!(critterList == null)) {
    				critterList.add(this);
    				populationMoved.replace(critterPos, critterList);
    				
    			//if the critter moves into an empty space	
    			} else {
    				ArrayList<Critter> newList = new ArrayList<Critter>();
    				newList.add(this);
    				populationMoved.put(critterPos, newList);
    			}
    			
    		}
    	}
    }

    
    /**
     * ADDED!!
     * unoccupiedPosition
     * @param dir is the integer representing the direction that the critter is moving 
     * @param amount is the integer representing the amount of spaces the critter is moving 
     * @return boolean true if the position that the critter is moving into is unoccupied, otherwise returns false
     */
    private boolean unoccupiedPosition(int dir, int amount) {
    	int[] critterPos = getCritterPos(dir, amount);
		String coord = Integer.toString(critterPos[0])+ "_" +Integer.toString(critterPos[1]);
		
		//if the position is unoccupied by another critter
		if(population.containsKey(coord) || populationMoved.containsKey(coord)) {
			return false;
		}

    	return true;
    }
    
    
    /**
     * ADDED!!
     * getCritterPos
     * @param dir is the integer representing the direction that the critter is moving 
     * @param amount is the integer representing the amount of spaces the critter is moving 
     * @return integer array holding the coordinates of the critter's new position in the world
     */
    private int[] getCritterPos(int dir, int amount) {
    	int x_num = 0;
    	int y_num = 0;
    	
    	if(dir == 7) {
    		x_num = (this.x_coord + amount) % Params.WORLD_WIDTH;
    		y_num = (this.y_coord + amount) % Params.WORLD_HEIGHT;
    	}
    	
    	if(dir == 5) {
    		x_num = (this.x_coord - amount) % Params.WORLD_WIDTH;
    		y_num = (this.y_coord + amount) % Params.WORLD_HEIGHT;
    	}
    
    	if(dir == 3) {
    		x_num = (this.x_coord - amount) % Params.WORLD_WIDTH;
    		y_num = (this.y_coord - amount) % Params.WORLD_HEIGHT;
    	}
    	
    	if(dir == 1) {
    		x_num = (this.x_coord + amount) % Params.WORLD_WIDTH;
    		y_num = (this.y_coord - amount) % Params.WORLD_HEIGHT;
    	}
    	
    	if(dir == 6) {
    		x_num = this.x_coord;
    		y_num = (this.y_coord + amount) % Params.WORLD_HEIGHT;
    	}
    	
    	if(dir == 4) {
    		x_num = (this.x_coord - amount) % Params.WORLD_WIDTH;
    		y_num = this.y_coord;
    	}
    	
    	if(dir == 2) {
    		x_num = this.x_coord;
    		y_num = (this.y_coord - amount) % Params.WORLD_HEIGHT;
    	}
    	
    	if(dir == 0) {
    		x_num = (this.x_coord + amount) % Params.WORLD_WIDTH;
    		y_num = this.y_coord;
    	}
    	
    	//wrap-around world, need to correct coordinates
    	if(x_num > (Params.WORLD_WIDTH - 1))
    		x_num %= Params.WORLD_WIDTH;
    	else if(x_num < 0)
    		x_num += Params.WORLD_WIDTH;
    	if(y_num > (Params.WORLD_HEIGHT - 1))
    		y_num %= Params.WORLD_HEIGHT;
    	else if(y_num < 0)
    		y_num += Params.WORLD_HEIGHT;
    	
    	int[] pos = new int[2];
    	pos[1] = y_num;
    	pos[0] = x_num;
    	return pos;
    }
    
    
    /**
     * run
     * Moves a critter two positions in the specified direction
     * Deducts Params.RUN_ENERGY_COST from the critter that invokes it 
     * @param direction is the direction that the critter is running 
     */
    protected final void run(int direction) {
    	
    	//deduct this from the critter's energy
    	energy = energy - Params.RUN_ENERGY_COST;
    	
    	if(hasMoved == false) {
    		if((isFight == false || (isFight == true && unoccupiedPosition(direction, 2) == true)) && energy > 0) {
    			
    			//get the critter's new position
    			int[] critterCoord = getCritterPos(direction, 2);
    			x_coord = critterCoord[0];
    			y_coord = critterCoord[1];
    			
    			hasMoved = true;
    			isFight = false;
    		
    			String critterPos = Integer.toString(x_coord) + "_" + Integer.toString(y_coord);
    			
    			ArrayList<Critter> critterList = populationMoved.get(critterPos);
    			
    			//if there are already critters in this grid position
    			if(!(critterList == null)) {
    				critterList.add(this);
    				populationMoved.replace(critterPos, critterList);
    				
    			//if critter moves into an empty space
    			} else {
    				ArrayList<Critter> newList = new ArrayList<Critter>();
    				newList.add(this);
    				populationMoved.put(critterPos, newList);
    			}
    			
    		}
    	}
    }
    	
    
    /**
     * reproduce
     * Confirms that the "parent" critter has enough energy to reproduce
     * Assigns the child energy equal to 1/2 of the parent's energy 
     * Assigns the child a position indicated by the parent's current position and the specified direction
     * @param offspring is the new Critter baby
     * @param direction is where the baby Critter is going to end up adjacent to the parent 
     */
    protected final void reproduce(Critter offspring, int direction) {
        // TODO: Complete this method
    	
    	//Confirm that the "parent" critter has enough energy to reproduce, otherwise return
    	if(this.energy < Params.MIN_REPRODUCE_ENERGY)
    		return;
    	
    	offspring.energy = (int) Math.floor(this.energy*0.5f); //round offspring's energy down
    	this.energy = (int) Math.ceil(this.energy*0.5f); //round parent's energy up
    	
    	reproduceSound();
    	
    	//initialize initial position of the offspring
    	offspring.x_coord = this.x_coord;
    	offspring.y_coord = this.y_coord;
    	
    	//move the baby 
    	offspring.getCritterPos(direction, 1);
    	
    	//string value of baby's coordinates
    	String babyCoords = offspring.x_coord + "_" + offspring.y_coord;
    	
    	//coordinates are not a key yet
    	if(!(babies.containsKey(babyCoords))) {
    		ArrayList<Critter> babyCritts = new ArrayList<Critter>();
    		babyCritts.add(offspring);
    		babies.put(babyCoords, babyCritts);
    		
    	} else { //coordinates are already a key in the hash map of babies
    		ArrayList<Critter> babyCritters = babies.get(babyCoords);
    		babyCritters.add(offspring);
    		babies.replace(babyCoords, babyCritters);
    	}
    	
    }
    
    
    //reproduce sound
  	MediaPlayer rSound;
  	protected final void reproduceSound() {
  		String s = System.getProperty("user.dir");
  		s = s+"/src/"+myPackage1 +"/reproduce.mp3";
  		Media h = new Media(Paths.get(s).toUri().toString());
  		rSound = new MediaPlayer(h);
  		rSound.play();
  		
  	}

    
    /**
     * The TestCritter class allows some critters to "cheat". If you
     * want to create tests of your Critter model, you can create
     * subclasses of this class and then use the setter functions
     * contained here.
     * <p>
     * NOTE: you must make sure that the setter functions work with
     * your implementation of Critter. That means, if you're recording
     * the positions of your critters using some sort of external grid
     * or some other data structure in addition to the x_coord and
     * y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
        	super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
        	super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you
         * are not using the population ArrayList that has been
         * provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            ArrayList<Critter> critterList = new ArrayList<Critter>();
            //iterator through the keys in the population hash map
            Iterator<String> populationIterator = population.keySet().iterator();
            while(populationIterator.hasNext()) {
            	String coord = populationIterator.next();
            	//iterator through the critters in a key in a hash map
            	Iterator<Critter> positionIterator = population.get(coord).iterator();
            	while(positionIterator.hasNext()) {
            		Critter critt = positionIterator.next();
            		//add the critters to the critter population
            		critterList.add(critt);
            	}
            }
            
            return critterList;
        }

        
        /**
         * This method getBabies has to be modified by you if you are
         * not using the babies ArrayList that has been provided in
         * the starter code.  In any case, it has to be implemented
         * for grading tests to work.  Babies should be added to the
         * general population at either the beginning OR the end of
         * every timestep.
         */
        protected static List<Critter> getBabies() {
        	 ArrayList<Critter> critterList = new ArrayList<Critter>();
             //iterator through the keys in the babies hash map
             Iterator<String> populationIterator = babies.keySet().iterator();
             while(populationIterator.hasNext()) {
             	String coord = populationIterator.next();
             	//iterator through the critter babies in a key in a hash map
             	Iterator<Critter> positionIterator = babies.get(coord).iterator();
             	while(positionIterator.hasNext()) {
             		Critter critt = positionIterator.next();
             		//add the babies to the critter population
             		critterList.add(critt);
             	}
             }
             
             return critterList;
         }
    }
}
