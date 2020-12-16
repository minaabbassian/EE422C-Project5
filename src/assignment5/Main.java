/*
 * CRITTERS GUI Main.java
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

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application; //Main class extends this 
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	public static GridPane world = new GridPane();
	public static int shapeSize;
	public static ArrayList<String> critterList;
	public static int cellSize = 700/Params.WORLD_WIDTH;
	public static ArrayList<Label> critterStatistic = new ArrayList<Label>();
	
	public static String myPackage = Main.class.getPackage().toString().split(" ")[1];
	public Timeline animationTimeline = new Timeline();

	
	
    public static void main(String[] args) {
    	
        launch(args);
    }

    
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("CRITTERS");
		music();
		
		TabPane tabpane = new TabPane();
		
		//animation of the critter world
		Tab worldDisplay = new Tab();
		worldDisplay.setText("CRITTER WORLD ANIMATION");
		
		//statistics 
		Tab statistics = new Tab();
		statistics.setText("STATISTICS");
		
		//Parameters
		Tab params = new Tab();
		params.setText("PARAMETERS");
		
		//Gridpanes for the tabs
		GridPane worldPane = new GridPane();
		GridPane statsPane = new GridPane();
		GridPane paramsPane = new GridPane();
		
		GridPane worldPaneDivider = new GridPane();
		
		
		//////////////////////////////////////////////////////////////////////////
		
		//CREATE button
		critterList = critterList();
		//drop down menu
		ChoiceBox<String> dropDown = new ChoiceBox<String>();
		dropDown.getItems().addAll(critterList);
		TextField amount = new TextField("Enter Number");
		Button createButton = new Button("Create");
		//CREATE
		createButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String numberEntered = amount.getText();
				try {
					int amount;
					if(numberEntered.equals("Enter Number")) {
						amount = 0;
					} else {
						amount = Integer.parseInt(numberEntered);
					}
					String critterName = dropDown.getValue().toString();
					for(int i = 0; i < amount; i++) {
						Critter.createCritter(critterName);
						
					}
					Critter.displayWorld();
					//update stats
					updateCritterStats();
					createSound();
				} catch (InvalidCritterException | NumberFormatException e) {
					System.out.println("ERROR while Creating Critters");
				}
				amount.clear();
			}
			
		});
		
		//////////////////////////////////////////////////////////////////////////
		
		//STEP button
		Button timeStepButton = new Button("Step");
		TextField stepText = new TextField("Enter Number of Steps");
		//DO TIMESTEP
		timeStepButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String number = stepText.getText();
				try {
					int steps;
					if(number.equals("Enter Number of Steps")) {
						steps = 0;
					} else {
						steps = Integer.parseInt(number);
					}
					if(steps<1) {
						steps = 1;
					}
					for(int i = 0; i < steps; i++) {
						Critter.worldTimeStep();
					}
					Critter.displayWorld();
					//update stats
					updateCritterStats();
				} catch(NumberFormatException e) {
					System.out.println("ERROR while performing World Time Step");
				}
				stepText.clear();
			}
			
		});
	
		//////////////////////////////////////////////////////////////////////////
		
		//QUIT button 
		Button quitButton = new Button("Quit"); 
		//QUIT
		quitButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		
		//////////////////////////////////////////////////////////////////////////
		
		//CLEAR button
		Button clearButton = new Button("Clear");
		clearButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				Critter.clearWorld();
				randomColorBackground(); //EXTRA CREDIT
				Critter.displayWorld();
				
			}
		});
		
		//////////////////////////////////////////////////////////////////////////
		
		//Animation Settings
		//Slider for speed
		GridPane speedHolder = new GridPane();
		Label speedLabel = new Label("Frames Per Second");
		Slider speedSlider = new Slider(0, 10, 1);
		speedSlider.setShowTickLabels(true);
		speedSlider.setShowTickMarks(true);
		speedSlider.setBlockIncrement(1);
		speedSlider.setMajorTickUnit(2);
		GridPane.setConstraints(speedLabel, 0, 1);
		GridPane.setConstraints(speedSlider, 0, 2);
		speedHolder.getChildren().addAll(speedLabel, speedSlider);
		
		//Run & Stop animation Buttons
		GridPane startStop = new GridPane();
		Button animationButt = new Button("Run");
		
		animationButt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//disable all buttons
				createButton.setDisable(true);
				timeStepButton.setDisable(true);
				clearButton.setDisable(true);
				
				//get the frames per second
				double fps = speedSlider.getValue();
				
				Duration time = Duration.millis(1000/fps);
				
				KeyFrame k = new KeyFrame(time, new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						Critter.worldTimeStep();
						Critter.displayWorld();
						//update stats
						updateCritterStats();
					}
				});
				
				animationTimeline.getKeyFrames().add(k);
				animationTimeline.setCycleCount(Timeline.INDEFINITE);
				animationTimeline.play();
				
			}
		});
		
		Button stopButton = new Button("Stop");
		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				//enable all buttons
				animationTimeline.stop();
				Critter.displayWorld();
				createButton.setDisable(false);
				timeStepButton.setDisable(false);
				clearButton.setDisable(false);
			}
		});
		
		
		
		//set button constraints
		GridPane.setConstraints(animationButt, 0, 0);
		GridPane.setConstraints(stopButton, 1, 0);
		startStop.getChildren().addAll(animationButt, stopButton);
		
		//////////////////////////////////////////////////////////////////////////
		
		//Statistics Pane
		Label statsLabel = new Label("Statistics of Critters:");
		statsPane.addRow(0, statsLabel);
		
		//check boxes for each Critter in the statistics tab
		for(int i = 0; i < critterList.size(); i++) {
			CheckBox stats = new CheckBox(critterList.get(i));
			//Add each Critter to the statistics GridPane
			statsPane.add(stats, 0, i+1);
			//each Critter initially has no stats
			Label critterStat = new Label("none");
			critterStat.setVisible(false);
			//ArrayList of Labels for each Critter subclass 
			critterStatistic.add(critterStat);
			statsPane.add(critterStat, 1, i+1);
			
			stats.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					//if Critter is selected, show the Critter stats
					if(stats.isSelected()) {
						critterStat.setVisible(true);
					} else {
						critterStat.setVisible(false);
					}
				}
			});
		}
		
		//set the content of the statistics tab
		statistics.setContent(statsPane);
	
		
		//////////////////////////////////////////////////////////////////////////
		
		//Parameters Pane
		Label paramsLabel = new Label("Parameters:");
		paramsPane.addRow(0,  paramsLabel);
		
		//set Seed
		
		//Seed button 
		GridPane seedPane = new GridPane();
		TextField seedText  = new TextField("Enter Seed");
		Button seedButton = new Button("Set Seed");
		seedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String seed = seedText.getText();
				try {
					long seedNumber;
					if(seed.equals("Enter Seed")) {
						seedNumber = 1;
					} else {
						seedNumber = Long.parseLong(seed);
					}
					Critter.setSeed(seedNumber);
				} catch (NumberFormatException e) {
					System.out.println("ERROR while setting Seed Number");
				}
				seedText.clear();
			}
			
		});
		Label seedLabel = new Label("Set Seed:");
		GridPane.setConstraints(seedLabel, 0, 2);
		seedPane.getChildren().add(seedLabel);
		GridPane.setConstraints(seedText, 0, 3);
		seedPane.getChildren().add(seedText);
		GridPane.setConstraints(seedButton, 1, 3);
		seedPane.getChildren().add(seedButton);
		GridPane.setConstraints(seedPane, 0, 0);
		paramsPane.getChildren().add(seedPane);
		
		//set world width
		
		//Width button 
		GridPane widthPane = new GridPane();
		TextField widthText  = new TextField("Enter Width");
		Button widthButton = new Button("Enter");
		widthButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String width = widthText.getText();
				try {
					long widthVal;
					if(width.equals("Enter width")) {
						widthVal = Params.WORLD_WIDTH;
					} else {
						widthVal = Long.parseLong(width);
					}
					Params.WORLD_WIDTH = (int) widthVal;
					Critter.displayWorld();
				} catch (NumberFormatException e) {
					System.out.println("ERROR while setting width");
				}
				widthText.clear();
			}
			
		});
		Label widthLabel = new Label("Set Width:");
		GridPane.setConstraints(widthLabel, 0, 2);
		widthPane.getChildren().add(widthLabel);
		GridPane.setConstraints(widthText, 0, 3);
		widthPane.getChildren().add(widthText);
		GridPane.setConstraints(widthButton, 1, 3);
		widthPane.getChildren().add(widthButton);
		GridPane.setConstraints(widthPane, 0, 1);
		paramsPane.getChildren().add(widthPane);
		
		//set world height
		
		//Height button 
		GridPane heightPane = new GridPane();
		TextField heightText  = new TextField("Enter Height");
		Button heightButton = new Button("Enter");
		heightButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String height = heightText.getText();
				try {
					long heightVal;
					if(height.equals("Enter Height")) {
						heightVal = Params.WORLD_HEIGHT;
					} else {
						heightVal = Long.parseLong(height);
					}
					Params.WORLD_HEIGHT = (int) heightVal;
					Critter.displayWorld();
				} catch (NumberFormatException e) {
					System.out.println("ERROR while setting height");
				}
				heightText.clear();
			}
			
		});
		Label heightLabel = new Label("Set Height:");
		GridPane.setConstraints(heightLabel, 0, 2);
		heightPane.getChildren().add(heightLabel);
		GridPane.setConstraints(heightText, 0, 3);
		heightPane.getChildren().add(heightText);
		GridPane.setConstraints(heightButton, 1, 3);
		heightPane.getChildren().add(heightButton);
		GridPane.setConstraints(heightPane, 0, 2);
		paramsPane.getChildren().add(heightPane);
		
		//Set Cost Energy
		
		//Walk Cost button 
		GridPane walkCostPane = new GridPane();
		TextField walkCostText  = new TextField("Enter Walk Cost");
		Button walkCostButton = new Button("Enter");
		walkCostButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String walkCost = walkCostText.getText();
				try {
					long walkCostVal;
					if(walkCost.equals("Enter Walk Cost")) {
						walkCostVal = Params.WALK_ENERGY_COST;
					} else {
						walkCostVal = Long.parseLong(walkCost);
					}
					Params.WALK_ENERGY_COST = (int) walkCostVal;
					Critter.displayWorld();
				} catch (NumberFormatException e) {
					System.out.println("ERROR while setting walkCost");
				}
				walkCostText.clear();
			}
			
		});
		Label walkCostLabel = new Label("Set Walk Cost:");
		GridPane.setConstraints(walkCostLabel, 0, 2);
		walkCostPane.getChildren().add(walkCostLabel);
		GridPane.setConstraints(walkCostText, 0, 3);
		walkCostPane.getChildren().add(walkCostText);
		GridPane.setConstraints(walkCostButton, 1, 3);
		walkCostPane.getChildren().add(walkCostButton);
		GridPane.setConstraints(walkCostPane, 0, 3);
		paramsPane.getChildren().add(walkCostPane);
		
		
		//Set Run Cost
		
		//Run Cost button 
		GridPane runCostPane = new GridPane();
		TextField runCostText  = new TextField("Enter Run Cost");
		Button runCostButton = new Button("Enter");
		runCostButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String runCost = runCostText.getText();
				try {
					long runCostVal;
					if(runCost.equals("Enter Run Cost")) {
						runCostVal = Params.RUN_ENERGY_COST;
					} else {
						runCostVal = Long.parseLong(runCost);
					}
					Params.RUN_ENERGY_COST = (int) runCostVal;
					Critter.displayWorld();
				} catch (NumberFormatException e) {
					System.out.println("ERROR while setting runCost");
				}
				runCostText.clear();
			}
			
		});
		Label runCostLabel = new Label("Set Run Cost:");
		GridPane.setConstraints(runCostLabel, 0, 2);
		runCostPane.getChildren().add(runCostLabel);
		GridPane.setConstraints(runCostText, 0, 3);
		runCostPane.getChildren().add(runCostText);
		GridPane.setConstraints(runCostButton, 1, 3);
		runCostPane.getChildren().add(runCostButton);
		GridPane.setConstraints(runCostPane, 0, 4);
		paramsPane.getChildren().add(runCostPane);
		

		//Set Rest Cost
		
		//Rest Cost button 
		GridPane restCostPane = new GridPane();
		TextField restCostText  = new TextField("Enter Rest Cost");
		Button restCostButton = new Button("Enter");
		restCostButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String restCost = restCostText.getText();
				try {
					long restCostVal;
					if(restCost.equals("Enter Rest Cost")) {
						restCostVal = Params.REST_ENERGY_COST;
					} else {
						restCostVal = Long.parseLong(restCost);
					}
					Params.REST_ENERGY_COST = (int) restCostVal;
					Critter.displayWorld();
				} catch (NumberFormatException e) {
					System.out.println("ERROR while setting restCost");
				}
				restCostText.clear();
			}
			
		});
		Label restCostLabel = new Label("Set Rest Cost:");
		GridPane.setConstraints(restCostLabel, 0, 2);
		restCostPane.getChildren().add(restCostLabel);
		GridPane.setConstraints(restCostText, 0, 3);
		restCostPane.getChildren().add(restCostText);
		GridPane.setConstraints(restCostButton, 1, 3);
		restCostPane.getChildren().add(restCostButton);
		GridPane.setConstraints(restCostPane, 0, 5);
		paramsPane.getChildren().add(restCostPane);
		

		//Set Reproduce Cost
		
		//Reproduce Cost button 
		GridPane reproduceCostPane = new GridPane();
		TextField reproduceCostText  = new TextField("Enter Reproduce Cost");
		Button reproduceCostButton = new Button("Enter");
		reproduceCostButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String reproduceCost = reproduceCostText.getText();
				try {
					long reproduceCostVal;
					if(reproduceCost.equals("Enter Reproduce Cost")) {
						reproduceCostVal = Params.MIN_REPRODUCE_ENERGY;
					} else {
						reproduceCostVal = Long.parseLong(reproduceCost);
					}
					Params.MIN_REPRODUCE_ENERGY = (int) reproduceCostVal;
					Critter.displayWorld();
				} catch (NumberFormatException e) {
					System.out.println("ERROR while setting reproduceCost");
				}
				reproduceCostText.clear();
			}
			
		});
		Label reproduceCostLabel = new Label("Set Reproduce Cost:");
		GridPane.setConstraints(reproduceCostLabel, 0, 2);
		reproduceCostPane.getChildren().add(reproduceCostLabel);
		GridPane.setConstraints(reproduceCostText, 0, 3);
		reproduceCostPane.getChildren().add(reproduceCostText);
		GridPane.setConstraints(reproduceCostButton, 1, 3);
		reproduceCostPane.getChildren().add(reproduceCostButton);
		GridPane.setConstraints(reproduceCostPane, 0, 6);
		paramsPane.getChildren().add(reproduceCostPane);
		

		//Set Refresh Clover Count
		
		//Refresh Clover Count button 
		GridPane cloverCountPane = new GridPane();
		TextField cloverCountText  = new TextField("Enter Refresh Clover Count");
		Button cloverCountButton = new Button("Enter");
		cloverCountButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String cloverCount = cloverCountText.getText();
				try {
					long cloverCountVal;
					if(cloverCount.equals("Enter Refresh Clover Count")) {
						cloverCountVal = Params.REFRESH_CLOVER_COUNT;
					} else {
						cloverCountVal = Long.parseLong(cloverCount);
					}
					Params.REFRESH_CLOVER_COUNT = (int) cloverCountVal;
					Critter.displayWorld();
				} catch (NumberFormatException e) {
					System.out.println("ERROR while setting cloverCount");
				}
				cloverCountText.clear();
			}
			
		});
		Label cloverCountLabel = new Label("Set Refresh Clover Count:");
		GridPane.setConstraints(cloverCountLabel, 0, 2);
		cloverCountPane.getChildren().add(cloverCountLabel);
		GridPane.setConstraints(cloverCountText, 0, 3);
		cloverCountPane.getChildren().add(cloverCountText);
		GridPane.setConstraints(cloverCountButton, 1, 3);
		cloverCountPane.getChildren().add(cloverCountButton);
		GridPane.setConstraints(cloverCountPane, 0, 7);
		paramsPane.getChildren().add(cloverCountPane);
		

		//Set Photosynthesis Energy
		
		//Photosynthesis Energy button 
		GridPane photoEnergyPane = new GridPane();
		TextField photoEnergyText  = new TextField("Enter Photosynthesis Energy");
		Button photoEnergyButton = new Button("Enter");
		photoEnergyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String photoEnergy = photoEnergyText.getText();
				try {
					long photoEnergyVal;
					if(photoEnergy.equals("Enter Photosynthesis Energy")) {
						photoEnergyVal = Params.PHOTOSYNTHESIS_ENERGY_AMOUNT;
					} else {
						photoEnergyVal = Long.parseLong(photoEnergy);
					}
					Params.PHOTOSYNTHESIS_ENERGY_AMOUNT = (int) photoEnergyVal;
					Critter.displayWorld();
				} catch (NumberFormatException e) {
					System.out.println("ERROR while setting photoEnergy");
				}
				photoEnergyText.clear();
			}
			
		});
		Label photoEnergyLabel = new Label("Set Photosynthesis Energy:");
		GridPane.setConstraints(photoEnergyLabel, 0, 2);
		photoEnergyPane.getChildren().add(photoEnergyLabel);
		GridPane.setConstraints(photoEnergyText, 0, 3);
		photoEnergyPane.getChildren().add(photoEnergyText);
		GridPane.setConstraints(photoEnergyButton, 1, 3);
		photoEnergyPane.getChildren().add(photoEnergyButton);
		GridPane.setConstraints(photoEnergyPane, 0, 8);
		paramsPane.getChildren().add(photoEnergyPane);
		

		//Set Starting Energy
		
		//Starting Energy button 
		GridPane startEnergyPane = new GridPane();
		TextField startEnergyText  = new TextField("Enter Starting Energy");
		Button startEnergyButton = new Button("Enter");
		startEnergyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String startEnergy = startEnergyText.getText();
				try {
					long startEnergyVal;
					if(startEnergy.equals("Enter Starting Energy")) {
						startEnergyVal = Params.START_ENERGY;
					} else {
						startEnergyVal = Long.parseLong(startEnergy);
					}
					Params.START_ENERGY = (int) startEnergyVal;
					Critter.displayWorld();
				} catch (NumberFormatException e) {
					System.out.println("ERROR while setting startEnergy");
				}
				startEnergyText.clear();
			}
			
		});
		Label startEnergyLabel = new Label("Set Starting Energy:");
		GridPane.setConstraints(startEnergyLabel, 0, 2);
		startEnergyPane.getChildren().add(startEnergyLabel);
		GridPane.setConstraints(startEnergyText, 0, 3);
		startEnergyPane.getChildren().add(startEnergyText);
		GridPane.setConstraints(startEnergyButton, 1, 3);
		startEnergyPane.getChildren().add(startEnergyButton);
		GridPane.setConstraints(startEnergyPane, 0, 9);
		paramsPane.getChildren().add(startEnergyPane);
		
		

		//Set Look Energy
		
		//Look Energy button 
		GridPane lookEnergyPane = new GridPane();
		TextField lookEnergyText  = new TextField("Enter Look Energy");
		Button lookEnergyButton = new Button("Enter");
		lookEnergyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String lookEnergy = lookEnergyText.getText();
				try {
					long lookEnergyVal;
					if(lookEnergy.equals("Enter Look Energy")) {
						lookEnergyVal = Params.LOOK_ENERGY_COST;
					} else {
						lookEnergyVal = Long.parseLong(lookEnergy);
					}
					Params.LOOK_ENERGY_COST = (int) lookEnergyVal;
					Critter.displayWorld();
				} catch (NumberFormatException e) {
					System.out.println("ERROR while setting lookEnergy");
				}
				lookEnergyText.clear();
			}
			
		});
		Label lookEnergyLabel = new Label("Set Look Energy:");
		GridPane.setConstraints(lookEnergyLabel, 0, 2);
		lookEnergyPane.getChildren().add(lookEnergyLabel);
		GridPane.setConstraints(lookEnergyText, 0, 3);
		lookEnergyPane.getChildren().add(lookEnergyText);
		GridPane.setConstraints(lookEnergyButton, 1, 3);
		lookEnergyPane.getChildren().add(lookEnergyButton);
		GridPane.setConstraints(lookEnergyPane, 0, 10);
		paramsPane.getChildren().add(lookEnergyPane);
		
		
		//////////////////////////////////////////////////////////////////////////
		
		//Adding to the World Animation Tab 
		GridPane interactive = new GridPane();
		Label createLabel = new Label("Create Critters");
		GridPane.setConstraints(createLabel, 0, 0);
		interactive.getChildren().add(createLabel);
		GridPane.setConstraints(dropDown, 0, 1);
		interactive.getChildren().add(dropDown);
		GridPane.setConstraints(amount, 1, 1);
		interactive.getChildren().add(amount);
		GridPane.setConstraints(createButton, 2, 1);
		interactive.getChildren().add(createButton);
		
		
		Label stepLabel = new Label("World Time Step");
		GridPane.setConstraints(stepLabel, 0, 2);
		interactive.getChildren().add(stepLabel);
		GridPane.setConstraints(stepText, 0, 3);
		interactive.getChildren().add(stepText);
		GridPane.setConstraints(timeStepButton, 1, 3);
		interactive.getChildren().add(timeStepButton);
		
		
		GridPane.setConstraints(clearButton, 0, 6);
		interactive.getChildren().add(clearButton);
		GridPane.setConstraints(quitButton, 1, 6);
		interactive.getChildren().add(quitButton);
		GridPane.setConstraints(interactive, 0, 1);
		worldPane.getChildren().add(interactive);
		
		
		ScrollPane worldView = new ScrollPane();
		GridPane.setConstraints(worldView, 0, 0);
		worldView.setContent(world);
		worldPane.getChildren().add(worldView);
		
		//////////////////////////////////////////////////////////////////////////
		
		//Animation Pane 
		GridPane animationPane = new GridPane();
		GridPane.setConstraints(speedHolder, 0, 1);
		animationPane.getChildren().add(speedHolder);
		GridPane.setConstraints(startStop, 0, 2);
		animationPane.getChildren().add(startStop);
		
		//Divider for Animation Settings 
		GridPane.setConstraints(worldPane, 0, 0);
		worldPaneDivider.getChildren().add(worldPane);
		GridPane.setConstraints(animationPane, 1, 0);
		worldPaneDivider.getChildren().add(animationPane);
		Label animationTitle = new Label("ANIMATION SETTINGS:");
		GridPane.setConstraints(animationTitle, 0, 0);
		animationPane.getChildren().add(animationTitle);
		worldDisplay.setContent(worldPaneDivider);
		statistics.setContent(statsPane);
		params.setContent(paramsPane);
		
		//////////////////////////////////////////////////////////////////////////
		
		//World SETTINGS
		Rectangle2D bounds = Screen.getPrimary().getBounds();
		shapeSize = (int)(((bounds.getMaxX()/20) * 9) / Params.WORLD_WIDTH);
		tabpane.setMaxWidth(bounds.getMaxX()/3);
		//set min and max size of the world 
		world.setMinSize(Params.WORLD_WIDTH, Params.WORLD_HEIGHT);
		world.setMaxSize(((bounds.getMaxX()/3)*2), bounds.getMaxY());
	
		//EXTRA CREDIT -- changing background of grid
		//randomly select world color when a new world is created
		randomColorBackground();

		
		//Add all tabs
		tabpane.getTabs().addAll(worldDisplay, statistics, params);
		Scene scene = new Scene(tabpane, 1040, 780);
		Critter.displayWorld();
		// creates a scene object
		primaryStage.setScene(scene);
		// puts the scene onto the stage
		primaryStage.show();
		// display the stage with the scene
		// paints the icons
		
	}
	
	//////////////////////////////////////////////////////////////////////////

	//Background Music
	MediaPlayer bgMusic;
	public void music() {
		String s = System.getProperty("user.dir");
		s = s+"/src/"+myPackage+"/WetHandsMinecraftC418.mp3";
		Media h = new Media(Paths.get(s).toUri().toString());
		bgMusic = new MediaPlayer(h);
		bgMusic.play();
		
	}

	//Create Sound
	MediaPlayer sfxCreate;
	void createSound() {
		String s = System.getProperty("user.dir");
		s = s+"/src/"+myPackage+"/create.mp3";
		Media h = new Media(Paths.get(s).toUri().toString());
		sfxCreate = new MediaPlayer(h);
		sfxCreate.play();
		
	}
	
	//////////////////////////////////////////////////////////////////////////
	
	/**
	 * ADDED!!
	 * updateCritterStats
	 * Updates the stats String of each Critter subclass 
	 */
	public void updateCritterStats() {
		for(int i = 0; i < critterList.size(); i++) {
			String crittStat = "none";
			try {
				//get instances of each critter
				List<Critter> crittInstance = Critter.getInstances(critterList.get(i));
				//get classes of each critter
				Class<?> crittClass = Class.forName(myPackage + "." + critterList.get(i));
				//get runStats method of critter
				Method statsMethod = crittClass.getMethod("runStats", List.class);
				//get critter object
				Object crittObject = crittClass.newInstance();
				crittStat = (String)statsMethod.invoke(crittObject, crittInstance);
			} catch(Exception e) {}
			//set the critter stats for each critter
			critterStatistic.get(i).setText(crittStat);
		}
	}
	
	//////////////////////////////////////////////////////////////////////////
	
	/**
	 * ADDED!! 
	 * randomColorBackground
	 * For Extra Credit
	 * Sets the background of the world grid to 1 of 6 different colors
	 */
	public void randomColorBackground() {
		Random rand = new Random();
		int randNum = rand.nextInt(6);
		switch(randNum) {
		case 0:
			world.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY)));
			break;
			
		case 1:
			world.setBackground(new Background(new BackgroundFill(Color.LAVENDER, CornerRadii.EMPTY, Insets.EMPTY)));
			break;
			
		case 2:
			world.setBackground(new Background(new BackgroundFill(Color.KHAKI, CornerRadii.EMPTY, Insets.EMPTY)));
			break;
			
		case 3:
			world.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
			break;
			
		case 4:
			world.setBackground(new Background(new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, Insets.EMPTY)));
			break;
			
		case 5:
			world.setBackground(new Background(new BackgroundFill(Color.PALEGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
			break;
		
		default:
    		break;
		}
	}
	
	//////////////////////////////////////////////////////////////////////////

	/**
	 * ADDED!! 
	 * critterList
	 * @return ArrayList of Strings holding the names of all of the Critter subclasses 
	 */
	private static ArrayList<String> critterList(){
		ArrayList<String> names = new ArrayList<String>();
		try {
			File root = new File("./src/" + myPackage);
			for (File file : root.listFiles()) {
				if (!file.isDirectory()) {
				    String name = file.getName();
				    name = name.substring(0, name.lastIndexOf('.'));
				    try {
				    	if(!((name.equals("Critter")) || (!Critter.class.isAssignableFrom(Class.forName("assignment5" + "." + name))))){
				    		names.add(name);
				    	}
				    } catch (Exception e) {}
				}
			}
		} catch (Exception e) {
			
		}
		return names;
	}
}
