package slick.tutorial;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;

public class Sprite {
	//Attributes
	private Image[] framesUp, framesDown, framesLeft, framesRight;
	private SpriteSheet spriteSheet;
	private Image tmpImage;
	private Animation up, down, left, right;
	public Animation current;
	private int[] animDelays;
	private static int DEFAULT_DELAY = 300;
	
	//Constructors
	public Sprite(){
		
	}
	
	//Constructor for Images
	/*
	 * STANDARD: 
	 * row 0 -> framesUp
	 * row 1 -> framesRight
	 * row 2 -> framesDown
	 * row 3 -> framesLeft
	 * */
	public Sprite(String tileSetPath, int widthPerTile, int heightPerTile){
		try{
			tmpImage = new Image(tileSetPath);
		}
		catch (SlickException e){
			System.out.println("[EXCEPTION] Error while loading image for sprite in Sprite.java");
			e.printStackTrace();
		}
		
		spriteSheet = new SpriteSheet(tmpImage, widthPerTile, heightPerTile);
		animDelays = new int[spriteSheet.getHorizontalCount()];
		
		framesUp = new Image[spriteSheet.getHorizontalCount()];
		framesRight = new Image[spriteSheet.getHorizontalCount()];
		framesLeft = new Image[spriteSheet.getHorizontalCount()];
		framesDown = new Image[spriteSheet.getHorizontalCount()];

		
		for (int i = 0; i < spriteSheet.getHorizontalCount(); i++){
			animDelays[i] = DEFAULT_DELAY;
			
			framesUp[i] = spriteSheet.getSubImage(i, 0);
			framesRight[i] = spriteSheet.getSubImage(i, 1);
			framesLeft[i] = spriteSheet.getSubImage(i, 3);
			framesDown[i] = spriteSheet.getSubImage(i, 2);
		}
		
		//Creating animations
		up = new Animation(framesUp, animDelays, false);
		right = new Animation(framesRight, animDelays, false);
		left = new Animation(framesLeft, animDelays, false);
		down = new Animation(framesDown, animDelays, false);
		
		//Default sprite
		current = down;
	}
	
	//Methods
	public void render(float X, float Y){
		current.draw((float)X, (float)Y);
	}

	public void Update(int delta){
		current.update(delta);
	}
	
	public void setFrame(char side){
		switch (side){
		case 'u':
			current = up; break;

		case 'd':
			current = down; break;
			
		case 'l':
			current = left; break;
			
		case 'r':
			current = right; break;
		}
	}
}
