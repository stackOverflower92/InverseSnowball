//Packages import
package slick.tutorial;

import java.awt.Point;
import java.io.IOException;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class SnowBall extends Character{
	//Variables
	private SpriteSheet spriteSheet;
	private Image[] frames;
	private Animation animation;
	private Image tmpImage;
	private boolean isVisible;
	private int[] animDelays;
	private static int DEFAULT_DELAY = 100;
	private float x, y, initX, initY;
	private Animation current;
	private boolean isAvailable;
	private int count;
	private boolean triggered;
	private float moveOffset = 0.01f;
	
	//Constructors
 	public SnowBall() throws IOException{
 		//Default visibility is set to false
		this.setVisibility(false);
	}
 	
	public SnowBall(String tileSetPath, int tw, int th) throws IOException{
 		//Default visibility is set to false
		this.setVisibility(false);
		
		//Init everything
		//System.out.println("[DEBUG] init function entered in class constructor: " + (this.getClass()).getName());
		
		//Load spritesheet
		try{
			tmpImage = new Image(tileSetPath);
		}
		catch(SlickException e){
			System.out.println("[EXCEPTION] Image loading");
			e.printStackTrace();
		}

		//Init spritesheet
		try{
			spriteSheet = new SpriteSheet(tmpImage, tw, th);
		}
		catch(Exception e){
			System.out.println("[EXCEPTION] SpriteSheet loading");
			e.printStackTrace();
		}
		
		//Allocating arrays
		animDelays = new int[spriteSheet.getHorizontalCount()];
		frames = new Image[spriteSheet.getHorizontalCount()];
		
		System.out.println("[DEBUG] horizontal count: " + spriteSheet.getHorizontalCount());
		
		for (int i = 0; i < spriteSheet.getHorizontalCount(); i++){
			animDelays[i] = DEFAULT_DELAY;
			
			frames[i] = spriteSheet.getSubImage(i, 0);
		}
		
		//Init animations
		try{
			animation = new Animation(frames, animDelays, true);
		}
		catch(Exception e){
			System.out.println("[EXCEPTION] Animation loading");
			e.printStackTrace();
		}
		
		this.current = this.animation;
		this.triggered = false;
	}

	//Methods
 	public void setVisibility(boolean set){
 		this.isVisible = set;
 	}

 	public boolean getVisibility(){
 		return this.isVisible;
 	}
 	
 	
 	public void setAvailability(boolean set){
 		this.isAvailable = set;
 	}
 	

 	public boolean getAvailability(){
 		return this.isAvailable;
 	}
 	
 	
 	public void setCount(int set){
 		this.count = set;
 	}
 	

 	public int getCount(){
 		return this.count;
 	}
 	
 	
 	public void setPosition(float x, float y){
 		this.x = x; this.y = y;
 	}
 	

 	public Point getPosition(){
 		Point tmp = new Point();
 		
 		tmp.setLocation((double)this.x, (double)this.y);
 		return tmp;
 	}
 	
 	
 	public void setTrigger(boolean set, float currentX, float currentY){
 		this.triggered = set;
 		this.initX = currentX;
 		this.initY = currentY;
 	}
 	

 	public boolean getTrigger(){
 		return this.triggered;
 	}
 	
 	
	public void render(){
		if (this.getVisibility()){
			this.current.draw(this.x, this.y);
		}
	}
	

	public void update(int delta, GameContainer container, String chTeam){
		//User input handling
		Input userInput = container.getInput();
		
		if (userInput.isKeyDown(Input.KEY_SPACE)) this.triggered = true;
		
		//Always update position based on mainCh position
		if (this.triggered == false)
			this.x = super.x; this.y = super.y;
		
		if (this.triggered == true){
			this.x = this.initX; this.y = this.initY;
			this.setVisibility(true);
			
			//Move ball only if on screen
			if (this.y <= container.getScreenHeight() && this.y >= 0){
				if (chTeam == "client"){
					this.y -= this.moveOffset * delta;
					System.out.println("[DEBUG] Ball " + this.count + " | X: " + this.x + " Y: " + this.y);
				}
				else{
					this.y += this.moveOffset * delta;
					System.out.println("[DEBUG] Ball " + this.count + " | X: " + this.x + " Y: " + this.y);
				}
			}
			else{
				this.setVisibility(false);
				this.triggered = false;
			}
		}
	}
	
	
	public void init(String tileSetPath, int tw, int th, float chX, float chY){
		System.out.println("[DEBUG] init function entered in class: " + (this.getClass()).getName());
		
		//Load spritesheet
		try{
			tmpImage = new Image(tileSetPath);
		}
		catch(SlickException e){
			System.out.println("[EXCEPTION] Image loading");
			e.printStackTrace();
		}

		//Init spritesheet
		try{
			spriteSheet = new SpriteSheet(tmpImage, tw, th);
		}
		catch(Exception e){
			System.out.println("[EXCEPTION] SpriteSheet loading");
			e.printStackTrace();
		}
		
		animDelays = new int[spriteSheet.getHorizontalCount()];
		
		for (int i = 0; i < spriteSheet.getHorizontalCount(); i++){
			animDelays[i] = DEFAULT_DELAY;
			
			frames[i] = spriteSheet.getSubImage(i, 0);
		}
		
		//Init animations
		try{
			animation = new Animation(frames, animDelays, true);
		}
		catch(Exception e){
			System.out.println("[EXCEPTION] Animation loading");
			e.printStackTrace();
		}
		
		this.current = this.animation;
		this.triggered = false;
	}
}