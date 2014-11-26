package slick.tutorial;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class GDUtilities{
	public GDUtilities(){
		
	}
	
	public static void openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)){
	        try{
	            desktop.browse(uri);
	        } 
	        catch (Exception e){
	            e.printStackTrace();
	        }
	    }
	}

	public static void openWebpage(URL url) {
	    try{
	        openWebpage(url.toURI());
	    }
	    catch (URISyntaxException e){
	        e.printStackTrace();
	    }
	}
	
	public static void openWebpage(String urlString){
	    try{
	        Desktop.getDesktop().browse(new URL(urlString).toURI());
	    }
	    catch (Exception e){
	        e.printStackTrace();
	    }
	}
}
