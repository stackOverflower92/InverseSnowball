package slick.tutorial;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Bullet  {
	private Vector2f position;
	private Vector2f speed;
	private int lived = 0;
	private boolean active = true;
	private Circle bulletOval;
	private Rectangle bulletRect;
	private String charTeam;
	private boolean touched=false;
	private SpriteSheet spriteSheet;
	private Animation current;
	private Image tmpImage;
	private int[] animDelays;
	private Image[] framesUp, framesDown;
	private Animation up, down;	
	
	
	
	
	public Bullet(Vector2f position, Vector2f speed){
		this.position = position;
		this.speed = speed;
		this.active = true;
		
		//bulletOval = new Circle(position.x, position.y, 10f);
		bulletRect = new Rectangle(position.x, position.y, 20, 20);
		
		
		
		
		try{
			tmpImage = new Image("assets/sprites/ominobrutto.png");
		}
		catch (SlickException e){
			e.printStackTrace();
		}
		spriteSheet = new SpriteSheet(tmpImage, 20, 50);
		animDelays = new int[spriteSheet.getHorizontalCount()];
		
		framesUp = new Image[spriteSheet.getHorizontalCount()];
		framesDown = new Image[spriteSheet.getHorizontalCount()];
		
		for (int i = 0; i < spriteSheet.getHorizontalCount(); i++){
			animDelays[i] = 200;
			framesUp[i] = spriteSheet.getSubImage(i, 0);
			framesDown[i] = spriteSheet.getSubImage(i, 1);
		}
		
		up = new Animation(framesUp, animDelays, false);
		down = new Animation(framesDown, animDelays, false);

	}
	
	public Bullet(String team){
		this.active = false;
		this.charTeam = team;
		this.position = new Vector2f();
		
		bulletOval = new Circle(0f, 0f, 10f);
		bulletRect = new Rectangle(position.x, position.y, 20, 20);
	}
	
	public boolean update(int t, int mul, String team, Rectangle rect){
		if (active){
			Vector2f realSpeed = speed.copy();
			realSpeed.set(0, mul * t * 0.3f);
			position.add(realSpeed);
			lived += t;
			
			if (lived > 8000){
				active = false;
			}
			
			if(!touched){
				bulletRect.setX(position.getX());
				bulletRect.setY(position.getY());
			}
			//Detecting collision
			if (team == "down"){
				current=up;
				if (this.bulletRect.intersects(rect)){
					System.out.println("Intersected main char");
					touched=true;
					bulletRect.setX(0);
					bulletRect.setY(0);
					return touched;
				}
			}
			else{
				current=down;
				if (this.bulletRect.intersects(rect)){
					System.out.println("Intersected main opponent");
					touched=true;	
					bulletRect.setX(0);
					bulletRect.setY(0);
					return touched;
				}
			}
			current.update(t);
		}
		return false;
	}
	
	public void render(GameContainer gc, Graphics g){
		if (active){
			g.setColor(Color.black);
			
			if(!touched){
				g.fillOval(position.x - 10, position.y - 10, 0, 0);
				current.draw(position.getX(),position.getY(), 20, 20);
			}
		}
	}
	
	public boolean isActive(){
		return this.active;
	}
	
	public void setActive(boolean s){
		this.active = s;
	}
	
}
