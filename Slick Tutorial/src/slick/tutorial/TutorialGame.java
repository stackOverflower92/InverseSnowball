package slick.tutorial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.*;

import com.google.gson.*;

/**
 * @author Can Accost || Dig Ballot
 */

public class TutorialGame extends BasicGame{
	//Attrubutes
	private TiledMap myMap;
	protected Character mainChar, mainOpponent;
	protected Rectangle team1Rect, team2Rect;
	private int playerCount = 0;
	private int damageChar, damageOpp;
	private boolean deadC = false, deadO = false;
	private GUI userInterface;
	private Gson gsonWrapper;
	private File settingsFile;
	private FileReader settingsReader;
	private BufferedReader settingsBuf;
	private UserSetting userSettings;

	//Methods
	//Commeto alla cazzo
	public TutorialGame() throws IOException{
        super("Prova Game");
        
        //GUI init
        userInterface = new GUI();
        
        //User settings init
        userSettings = new UserSetting();
        
        //Initializing currentSection to in game section
        //Change this to try different scenes
        userInterface.setSection(GameSection.MAIN_MENU);
        
        //Initializing gson stuff
        gsonWrapper = new GsonBuilder().setPrettyPrinting().create();
        
        //Read stuff from file (user settings)
        settingsFile = new File(calculateSettingsPath());
        
        if (settingsFile.exists()){
        	String content = "";
        	settingsReader = new FileReader(settingsFile);
        	settingsBuf = new BufferedReader(settingsReader);
        	
        	String[] names = new String[2];
        	
        	System.out.println("Settings file found, gonna extract informations");
        	
        	content = settingsBuf.readLine();
        	
        	System.out.println("Content: " + content);
        	
        	//closing stream
        	settingsReader.close();
        	
        	//Assign stuff to array
        	names = extractSettingsFields(content);
        	
        	userSettings.setPlayer1Name(names[0]);
        	userSettings.setPlayer2Name(names[1]);
        	
        	System.out.println("User settings: " + userSettings.toString());
        }
        else{
        	System.out.println("Settings file not found. Gonna set default.");
        }
        
    }
	
	private String[] extractSettingsFields(String toParse){
		String[] names = new String[2];
		int currentName = 0;
		names[0] = ""; names[1] = "";
		
		for (int i = 0; i < toParse.length(); i++){
			//If space is encountered change array place
			if (toParse.charAt(i) == '�'){
				currentName++;
				i++;
			}
			
			//Otherwise always construct string
			names[currentName] += toParse.charAt(i);
		}
		return names;
	}
	
	private String calculateSettingsPath(){
		String operatingSystem = System.getProperty("os.name").toLowerCase();

		//Windows
		if (operatingSystem.indexOf("win") >= 0){
			return "C:" + System.getProperty("file.separator") + "InverseSnowball" + System.getProperty("file.separator") + "settings.json";
		}
		//Mac
		else if (operatingSystem.indexOf("mac") >= 0){
			return System.getProperty("file.separator") + "InverseSnowball";
		}
		//Unix
		else if (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0 || operatingSystem.indexOf("aix") >= 0){
			return System.getProperty("file.separator") + "InverseSnowball" + System.getProperty("file.separator") + "settings.json";
		}
		//Solaris
		else if (operatingSystem.indexOf("sunos") >= 0){
			return System.getProperty("file.separator") + "InverseSnowball" + System.getProperty("file.separator") + "settings.json";
		}
		else{
			System.out.println("Your operating system is not supported.");
			return null;
		}
	}
	
	public Character getMainch(){
		return this.mainChar;
	}
	
	public Character getOpponent(){
		return this.mainOpponent;
	}
	 
	public void oppDamage(String team){
		if (team == "down"){
			mainOpponent.doDamage(1);
		}
		else{
			mainChar.doDamage(1);
		}
	}

	private void checkHP(){
    	
    	if (mainOpponent.getHP() == 0){
    		System.out.println("character:" + mainChar.getName() + "wins!!!!!");
    		deadO = true;
    	}
    	if (mainChar.getHP() == 0){
    		System.out.println("character:" + mainOpponent.getName() + "wins!!!!!");
    		deadC = true;
    	}
	}

    @Override
    public void init(GameContainer container) throws SlickException{
    	//Graphics settings
    	container.setShowFPS(false);
    	container.setVSync(true);
    	container.setTargetFrameRate(60);
    	
    	//Map initialization
        try{
			myMap = new TiledMap("assets/maps/mappabella.tmx");
		}
        catch (SlickException e){
        	System.out.println("[EXCEPTION] Loading TileMap");
			e.printStackTrace();
		}
        
        //Main character init
        try {
            mainChar = new Character(userSettings.getPlayer1Name(), 10, 535f, 535f, "assets/sprites/snowballfighe.png", 50, 50, playerCount, "down");
        }
        catch (IOException e){
        	e.printStackTrace();
        }
        playerCount++;
        
        //Main opponent init
        try {
            mainOpponent = new Character(userSettings.getPlayer2Name() ,10, 100f, 80f, "assets/sprites/snowballfighe.png", 50, 50, playerCount, "up");
        }
        catch (IOException e){
        	e.printStackTrace();
        }
        playerCount++;
        
        mainChar.setOppRect(mainOpponent.getMainRect());
        mainOpponent.setOppRect(mainChar.getMainRect());
        
        //Init team zones
        team1Rect = new Rectangle(0f, myMap.getTileHeight() * myMap.getHeight() - (myMap.getTileHeight() * 2), myMap.getWidth() * myMap.getTileWidth(), myMap.getTileHeight() * 2);
        team2Rect = new Rectangle(0f, 0f, myMap.getWidth() * myMap.getTileWidth(), myMap.getTileHeight() * 2);
    }
 
    @Override
    public void update(GameContainer container, int delta) throws SlickException{
    	//Updating all entities
    	Input keyIn = container.getInput();
    	
    	//Main menu
    	if (userInterface.getSection() == GameSection.MAIN_MENU){
    		userInterface.update(container, delta);
    	}
    	//Playing
    	if (userInterface.getSection() == GameSection.PLAYING){
    		userInterface.update(container, delta);
    		
    		if(mainChar.update(delta, container, team1Rect, team2Rect, "down")){
            	mainOpponent.doDamage(5);
        		System.out.println(mainOpponent.getName() + " " + mainOpponent.getHP());

        	}
        	if(mainOpponent.update(delta, container, team1Rect, team2Rect, "up")){
        		mainChar.doDamage(5);
        		System.out.println(mainChar.getName() + " " + mainChar.getHP());
        	}
        	
        	checkHP();
        	
        	if (keyIn.isKeyDown(Input.KEY_ESCAPE)){
        		container.exit();
        	}
    	}
    }
 
    public void render(GameContainer container, Graphics g) throws SlickException{
    	//GUI always knows how to draw by itself, let's call it out of every if/else statement
    	userInterface.render(container, g, mainChar, mainOpponent);
    	
    	//Playing
    	if (userInterface.getSection() == GameSection.PLAYING){
        	userInterface.render(container, g, mainChar, mainOpponent);
    		
    		//Rendering map to screen
        	myMap.render(0, 0);
        	
        	//Rendering sprite to screen
        	if(!deadC)
        		mainChar.render(container, g);
        	
        	if(!deadO)
        		mainOpponent.render(container, g);
    	}
    }

    //Main function follows
    public static void main(String[] arguments) throws IOException{
        try{
            AppGameContainer app = new AppGameContainer(new TutorialGame());
            
            app.setDisplayMode(1100, 630, false);
            app.start();
        }
        catch (SlickException e){
        	System.out.println("[EXCEPTION] Loading app");
            e.printStackTrace();
        }
    }
}
