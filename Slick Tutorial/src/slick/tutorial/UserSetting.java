package slick.tutorial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class UserSetting{
	//Attributes
	private String player1Name, player2Name, defaultLocation;
	private boolean audioEnabled;
	private int[] screenRes;
	private Gson gsonWrapper;
	private JsonParser jsonParser;
	private File settingsFile;
	private FileOutputStream settingStream;
	private OutputStreamWriter settingWriter;
	private Writer writer;
	
	//Constructors
	public UserSetting(){
		screenRes = new int[2];
		
		//Default names
		player1Name = "player1";
		player2Name = "player2";
		
		//Default resolution
		screenRes[0] = 1920;
		screenRes[1] = 1080;
		
		//Set-up gson stuff
		gsonWrapper = new GsonBuilder().setPrettyPrinting().create();
		jsonParser = new JsonParser();
	}
	
	//Methods
	public UserSetting(String player1Name, String player2Name, boolean audioEnabled, int[] screenRes){
		this.player1Name = player1Name;
		this.player2Name = player2Name;
		this.audioEnabled = audioEnabled;
		this.screenRes = screenRes;
		
		//Set-up gson stuff
		gsonWrapper = new GsonBuilder().setPrettyPrinting().create();
		jsonParser = new JsonParser();
	}
	
	public String getPlayer1Name() {
		return player1Name;
	}

	public void setPlayer1Name(String player1Name) {
		this.player1Name = player1Name;
	}

	public String getPlayer2Name() {
		return player2Name;
	}

	public void setPlayer2Name(String player2Name) {
		this.player2Name = player2Name;
	}

	public boolean isAudioEnabled() {
		return audioEnabled;
	}

	public void setAudioEnabled(boolean audioEnabled) {
		this.audioEnabled = audioEnabled;
	}

	public int[] getScreenRes() {
		return screenRes;
	}

	public void setScreenRes(int[] screenRes) {
		this.screenRes = screenRes;
	}

	public String getDefaultLocation() {
		return defaultLocation;
	}

	public void setDefaultLocation(String defaultLocation) {
		this.defaultLocation = defaultLocation;
	}

	@Override
	public String toString(){
		return player1Name + " " + player2Name;
	}
}
