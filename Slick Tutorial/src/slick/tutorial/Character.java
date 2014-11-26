package slick.tutorial;

import java.io.IOException;

import slick.tutorial.Sprite;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Character extends TutorialGame {
	//Class attributes
	private int HP;
	protected float x = 30f, y = 30f;
	private String name;
	private Image[] framesUp, framesDown, framesLeft, framesRight;
	private SpriteSheet spriteSheet;
	private Image tmpImage;
	private Animation up, down, left, right;
	private Animation current;
	private int[] animDelays;
	private float moveOffset = 0.2f;
	private float spriteHeight = 45f;
	private float spriteWidth = (float)(spriteHeight * 0.75);
	private Bullet[] bullets;
	private int snowBallCount;
	private Vector2f vectPosition;
	private static int FIRE_RATE = 250;
	private static int DEFAULT_DELAY = 160;
	private boolean triggered = false, damage=false;
	private int delta = 0;
	private int tw, th;
	private int playerCount;
	private int[][] keySets;
	private Rectangle mainRect, oppRect;
	private String team;
	
	/*******CONSTRUCTORS*******/
	
	//Empty Constructor
	public Character() throws IOException {
	}
	 
	//First Constructor
	/*
	 * @Param The name of the character
	 * */
	public Character(String name) throws IOException {
		this.name = name;
	}
	
	//Second Constructor
	/*
	 * @Param The name of the character
	 * @Param Life points when initialized
	 * @Param X coord
	 * @Param Y coord
	 * @Param String containing the absolute path of the tileset
	 * @Param Single tile width
	 * @Param Single tile height
	 * */
	public Character(String name, int HP, float X, float Y, String tileSetPath, int tw, int th, int playerCount, String team) throws IOException{
		//Calling init function
		this.init(name, HP, X, Y, tileSetPath, tw, th, playerCount, team);
	}
	
	//*******METHODS*******/
	
	/**********************
	 * SETTERS AND GETTERS
	 * FOLLOW HERE
	 * ********************
	 * */
	
	//Sprite Handling
	public void setName(String name){
		//Setting name to parameter
		this.name = name;
	}
	
	public void setHP(int HP){
		//Setting HP to parameter
		this.HP = HP;
	}
	
	public void doDamage(int damage){
		//Setting HP to parameter
		this.HP -= damage;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public float getX(){
		return this.x;
	}
	
	public float getY(){
		return this.y;
	}
	
	public void setOppRect(Rectangle set){
		//Setting HP to parameter
		this.oppRect = set;
	}
	
	public Rectangle getMyRect(){
		//Setting HP to parameter
		return mainRect;
	}
	
	public void setSpriteHeight(float height){
		this.spriteHeight = height;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getHP(){
		return this.HP;
	}

	public Rectangle getMainRect(){
		return this.mainRect;
	}
	
	/*********************
	 * OTHER FUNCTIONS 
	 * FOLLOW HERE
	 * *******************
	 * */
	
	//INIT correspondent
	public void init(String name, int HP, float X, float Y, String tileSetPath, int tw, int th, int playerCount, String team){		
		this.HP = HP;
		this.name = name;
		this.x = X;
		this.y = Y;
		this.tw = tw;
		this.th = th;
		this.playerCount = playerCount;
		this.team = team;
		
		//Init keysets
		keySets = new int[3][5];

		keySets[0][0] = Input.KEY_A;
		keySets[0][1] = Input.KEY_W;
		keySets[0][2] = Input.KEY_D;
		keySets[0][3] = Input.KEY_S;
		keySets[0][4] = Input.KEY_SPACE;
		
		keySets[1][0] = Input.KEY_LEFT;
		keySets[1][1] = Input.KEY_UP;
		keySets[1][2] = Input.KEY_RIGHT;
		keySets[1][3] = Input.KEY_DOWN;
		keySets[1][4] = Input.KEY_ENTER;
		
		//Sprite sheet handling
		try{
			tmpImage = new Image(tileSetPath);
		}
		catch (SlickException e){
			e.printStackTrace();
		}
		
		spriteSheet = new SpriteSheet(tmpImage, tw, th);
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
		
		//Array of bullets
		bullets = new Bullet[5];
		
		System.out.println("[DEBUG] Gonna init bullets");
		
		//Init bullets
		for (int i = 0; i < bullets.length; i++){
			try{
				bullets[i] = new Bullet(team);
			}
			catch(Exception e){
				System.out.println("[EXCEPTION] loading bullets");
				e.printStackTrace();
			}
		}
		this.snowBallCount = 0;
		
		//Init char rectangle
		this.mainRect = new Rectangle(this.x, this.y, this.tw, this.th);
	}
	
	//UPDATE correspondent
	public boolean update(int delta, GameContainer container, Rectangle team1Rect, Rectangle team2Rect, String team){
		Input userInput = container.getInput();
		this.moveOffset = 0.2f;
		this.delta += delta;
		
		if (this.delta > FIRE_RATE && container.getInput().isKeyPressed(keySets[playerCount][4])){
			//Change right sprite while shooting
			if (team == "down")
				setFrame('u');
			else
				setFrame('d');
			
			bullets[snowBallCount] = new Bullet(new Vector2f(this.x + 18, this.y), new Vector2f(500, 100));
			snowBallCount++;
			
			if (snowBallCount >= bullets.length) snowBallCount = 0;
			
			this.delta = 0;
		}
		//Update anyway
		for (int i = 0; i < bullets.length; i++){
			if (team == "down"){
				damage = bullets[i].update(delta, -1, team, oppRect);
				//System.out.println(damage);
			}
			else{
				damage = bullets[i].update(delta, 1, team, oppRect);
				//System.out.println(damage);	
			}
		}
		//System.out.println("hp: "+ HP + "PG: " + name);
		//Updating user input
		if (userInput.isKeyDown(keySets[playerCount][1])){
			y -= moveOffset * delta;
			setFrame('u');
			current.update(delta);
		}
		
		if (userInput.isKeyDown(keySets[playerCount][3])){
			y += moveOffset * delta;
			setFrame('d');
			current.update(delta);
		}
		
		if (userInput.isKeyDown(keySets[playerCount][0])){
			x -= moveOffset * delta;
			setFrame('l');
			current.update(delta);
		}
		
		if (userInput.isKeyDown(keySets[playerCount][2])){
			x += moveOffset * delta;
			setFrame('r');
			current.update(delta);
		}

		if (team == "down"){
			//DX and SX bounds
			if (x + tw >= team1Rect.getMaxX()){
				x = team1Rect.getMaxX() - tw;
			}
			if (x <= team1Rect.getMinX()){
				x = 0;
			}
			
			//TOP and BOTTOM bounds
			if (y <= team1Rect.getMinY()){
				y = team1Rect.getMinY();
			}
			if (y + th >= team1Rect.getMaxY()){
				y = team1Rect.getMaxY() - th;
			}
		}
		
		//TODO: Copy, paste and adapt code above under here.
		else{
			//DX and SX bounds
			if (x + tw >= team2Rect.getMaxX()){
				x = team2Rect.getMaxX() - tw;
			}
			if (x <= team2Rect.getMinX()){
				x = 0;
			}
			
			//TOP and BOTTOM bounds
			if (y <= team2Rect.getMinY()){
				y = team2Rect.getMinY();
			}
			if (y + th >= team2Rect.getMaxY()){
				y = team2Rect.getMaxY() - th;
			}
		}
		
		//Updating sprite rectangle
		mainRect.setX(x);
		mainRect.setY(y);
		
		//Updating collisions
		if(damage){
			//System.out.println("damage:" + damage);
		}
		return damage;
	}
	
	//RENDER correspondent
	public void render(GameContainer gc, Graphics g){
		//Drawing current sprite
		current.draw(x, y, tw, th);
		
		//Rendering bullets
		for (int i = 0; i < bullets.length; i++){
			bullets[i].render(gc, g);
		}
		
		//Drawing char rect and coords (for debug purposes)
		//g.drawRect(this.mainRect.getX(), this.mainRect.getY(), this.tw, this.th);
		//g.drawString("mainRect.width = " + this.mainRect.getWidth() + "\nmainRect.height = " + this.mainRect.getHeight() + "\nmainChar.width = " + this.tw + "\nmainChar.height = " + this.th, 620, 150);
	}
	
	//Set right frame based on user input (used in UPDATE method above)
	private void setFrame(char side){
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
	
	//Override default Object.toString() function adapting it to this specific class
	@Override
	public String toString(){
		return "[Name: " + name + "]\n[X: " + x + "]\n[Y " + y + "][HP: " + HP + "]";
	}
}
