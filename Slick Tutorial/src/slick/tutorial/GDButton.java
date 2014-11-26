package slick.tutorial;

import java.awt.Dimension;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class GDButton extends Graphics{
	//Attributes
	private float x, y, width, height, strOffsetX, strOffsetY;
	private String text;
	private Image image;
	private boolean borderVisible;
	
	//Constructors
	public GDButton(){
		borderVisible = true;
	}
	
	public GDButton(float x, float y, float width, float height, String text){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		borderVisible = true;
	}
	
	public GDButton(Vector2f location){
		this.x = location.getX();
		this.y = location.getY();
		borderVisible = true;
	}
	
	public GDButton(float x, float y, float width, float height, Image image){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = image;
		borderVisible = true;
	}
	
	public GDButton(Vector2f location, float width, float height, Image image){
		this.x = location.getX();
		this.y = location.getY();
		this.width = width;
		this.height = height;
		this.image = image;
		borderVisible = true;
	}
	
	public GDButton(Vector2f location, Dimension dimension){
		this.x = location.getX();
		this.y = location.getY();
		this.width = (float) dimension.getWidth();
		this.height = (float) dimension.getHeight();
		borderVisible = true;
	}
	
	public GDButton(Vector2f location, Dimension dimension, Image image){
		this.x = location.getX();
		this.y = location.getY();
		this.width = (float) dimension.getWidth();
		this.height = (float) dimension.getHeight();
		this.image = image;
		borderVisible = true;
	}

	//Methods
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public void drawSelf(){
		//TODO: Find way to know font size and replace that '10'
		setWidth(text.length() * 10);
		drawRect(x, y, width, height);
		//drawString(text, x + strOffsetX, y + strOffsetY);
		
	}
	
	public void drawSelf(float x, float y){
		moveTo(x, y);
		drawSelf();
	}
	
	public void removeSelf(){
		image = null;
		x = -100;
		y = -100;
		text = "";
		borderVisible = false;
	}
	
	public void moveTo(float x, float y){
		setX(x);
		setY(y);
	}
	
	public void resize(float width, float height){
		setWidth(width);
		setHeight(height);
	}
	
	public void resizeBy(float widthOffset, float heightOffset){
		this.width += widthOffset;
		this.height += heightOffset;
	}
}

