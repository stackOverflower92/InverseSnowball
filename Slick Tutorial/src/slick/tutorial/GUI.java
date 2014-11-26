package slick.tutorial;

import org.lwjgl.input.Cursor;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

public class GUI {
	//Attributes
	private String selected = "New Game";
	private String[] menuStrings;
	private Rectangle selectionRect;
	private Rectangle[] menuGrid;
	private String[] optionsStrings;
	private Rectangle[] optionsGrid;
	private int rectOffset = 30, selectedIndexMain = 0, selectedIndexOptions = 0;
	private GameSection currentSection;
	private GDUtilities util;
	private Rectangle pauseRectangle;
	private boolean pauseTrigger;
	
	//Constructors
	public GUI(){
		pauseTrigger = false;
		
		util = new GDUtilities();
		
		menuStrings = new String[4];
		optionsStrings = new String[4];
		menuGrid = new Rectangle[menuStrings.length];
		optionsGrid = new Rectangle[optionsStrings.length];
		
		//Initialize every rectangle
		for (int i = 0; i < menuGrid.length; i++){
			//Set to default location
			menuGrid[i] = new Rectangle(300, 123, 100, 25);
		}
		
		for (int i = 0; i < optionsGrid.length; i++){
			//Set to default location
			optionsGrid[i] = new Rectangle(300, 123, 100, 25);
		}
		
		//Assignment
		menuStrings[0] = "New Game";
		menuStrings[1] = "Options";
		menuStrings[2] = "Our website";
		menuStrings[3] = "Exit";
		
		optionsStrings[0] = "First Player Name";
		optionsStrings[1] = "Second Player Name";
		optionsStrings[2] = "Screen Resolution";
		optionsStrings[3] = "Audio Enabled";
		
		//Default Section
		currentSection = GameSection.MAIN_MENU;
		
		//Default rect init
		selectionRect = new Rectangle(300f, 123f, 100f, 25f);
		
		//Adapting menuGrid to contain every choice
		for (int i = 0; i < menuGrid.length; i++){
			menuGrid[i].setLocation(selectionRect.getX(), selectionRect.getY() + (i * rectOffset));
		}
	}
	
	//Methods
	public GameSection getSection(){
		return currentSection;
	}
	
	public void setSection(GameSection currentSection){
		this.currentSection = currentSection;
	}
	
	public void init(GameContainer container) throws SlickException {
		
	}
	
	public void update(GameContainer container, int delta) throws SlickException {
		Input userInput = container.getInput();
		
		if (currentSection == GameSection.MAIN_MENU){
			//Super section: main menu
			//Changing game section based on choice on user input
			if (userInput.isKeyPressed(Input.KEY_ENTER)){
				switch (selectedIndexMain){
				case 0:
					//First choice: play
					setSection(GameSection.PLAYING);
					break;
					
				case 1:
					//Second choice: open options
					setSection(GameSection.OPTIONS);
					break;
					
				case 2:
					//Third choice: website
					GDUtilities.openWebpage("http://www.onelasttrystudios.com");
					break;
					
				case 3:
					//Fourth choice: exit
					container.exit();
					break;
				}
			}
			
			//Updating current choice
			if (userInput.isKeyPressed(Input.KEY_UP)){
				if (selectedIndexMain > 0){
					selectionRect.setY(selectionRect.getY() - rectOffset);
					selectedIndexMain--;
				}
			}
			if (userInput.isKeyPressed(Input.KEY_DOWN)){
				if (selectedIndexMain < menuStrings.length - 1){
					selectionRect.setY(selectionRect.getY() + rectOffset);
					selectedIndexMain++;
				}
			}
			
			//Handling mouse choice
			if (userInput.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				if (menuGrid[0].contains(userInput.getMouseX(), userInput.getMouseY())){
					setSection(GameSection.PLAYING);
				}
				if (menuGrid[1].contains(userInput.getMouseX(), userInput.getMouseY())){
					setSection(GameSection.OPTIONS);
				}
				if (menuGrid[2].contains(userInput.getMouseX(), userInput.getMouseY())){
					GDUtilities.openWebpage("http://www.onelasttrystudios.com");
				}
				if (menuGrid[3].contains(userInput.getMouseX(), userInput.getMouseY())){
					container.exit();
				}
			}
		}
		
		if (currentSection == GameSection.OPTIONS){
			if (userInput.isKeyPressed(Input.KEY_UP)){
				if (selectedIndexOptions > 0){
					selectionRect.setY(selectionRect.getY() - rectOffset);
					selectedIndexOptions--;
				}
			}
			if (userInput.isKeyPressed(Input.KEY_DOWN)){
				if (selectedIndexOptions < menuStrings.length - 1){
					selectionRect.setY(selectionRect.getY() + rectOffset);
					selectedIndexOptions++;
				}
			}
			
			if (userInput.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				if (menuGrid[0].contains(userInput.getMouseX(), userInput.getMouseY())){
					setSection(GameSection.PLAYING);
				}
				if (menuGrid[1].contains(userInput.getMouseX(), userInput.getMouseY())){
					setSection(GameSection.OPTIONS);
				}
				if (menuGrid[2].contains(userInput.getMouseX(), userInput.getMouseY())){
					GDUtilities.openWebpage("http://www.onelasttrystudios.com");
				}
				if (menuGrid[3].contains(userInput.getMouseX(), userInput.getMouseY())){
					container.exit();
				}
			}
		}
		
		if (currentSection == GameSection.PLAYING){
			Input keyIn = container.getInput();
			
			pauseRectangle = new Rectangle(container.getHeight() - 200, -500, 400, 500);
			
			if (keyIn.isKeyPressed(Input.KEY_P)){
				pauseTrigger = true;
			}
			
			if (pauseTrigger){
				if (pauseRectangle.getY() >= 0){
					float y = pauseRectangle.getY();
					pauseRectangle.setY(y -= 1);
					System.out.println(y);
				}
			}
		}
	}
	
	public void render(GameContainer container, Graphics g, Character p1, Character p2) throws SlickException {
		if (currentSection == GameSection.MAIN_MENU){
			//Super section: main menu
			//Drawing menu strings
			for (int i = 0; i < menuStrings.length; i++){
				g.drawString(menuStrings[i], 300, 100 + 30 * (i + 1));
			}
			
			//Drawing choice rectangle
			g.setColor(Color.green);
			g.drawRect(selectionRect.getX(), selectionRect.getY(), selectionRect.getWidth(), selectionRect.getHeight());
			
			//Drawing grid rectangles (debugging only)
			/*g.setColor(Color.white);
			for (int i = 0; i < menuGrid.length; i++){
				g.drawRect(menuGrid[i].getX(), menuGrid[i].getY(), menuGrid[i].getWidth(), menuGrid[i].getHeight());
			}*/
			
			//Setting custom mouse cursor
			container.setMouseCursor("assets/sprites/cursor.png", 16, 16);
		}
		if (currentSection == GameSection.PLAYING){
			g.setColor(Color.white);
			
			g.drawString(p1.toString(), 830, 30);
			g.drawString(p2.toString(), 830, 100);
			
			//Hide mouse cursor while playing
			container.setMouseGrabbed(true);
			
			if (pauseTrigger){
				g.drawRect(pauseRectangle.getX(), pauseRectangle.getY(), pauseRectangle.getWidth(), pauseRectangle.getHeight());
			}
		}
		if (currentSection == GameSection.OPTIONS){
			//Setting custom mouse cursor
			container.setMouseCursor("assets/sprites/cursor.png", 16, 16);
			//Set mouse cursor to visible
			container.setMouseGrabbed(false);
			
			//Draw other stuff
			//Strings
			for (int i = 0; i < optionsStrings.length; i++){
				g.drawString(optionsStrings[i], 300, 100 + 30 * (i + 1));
				//g.drawRect(300,  100 + 30 * (i + 1), ttf.getWidth(optionsStrings[i]), ttf.getHeight(optionsStrings[i]));
			}
			//Rectangles		
		}
	}
}
