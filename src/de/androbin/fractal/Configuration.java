package de.androbin.fractal;

import static de.androbin.util.JSONUtil.*;
import org.json.simple.*;

public final class Configuration {
  private static final JSONObject CONFIG = (JSONObject) parseJSON( "config.json" ).get();
  
  public static final class window_ {
    private static final JSONObject CONFIG_WINDOW = (JSONObject) CONFIG.get( "window" );
    
    public static final float SCALE = ( (Number) CONFIG_WINDOW.get( "scale" ) ).floatValue();
  }
  
  public static final class gui_ {
    private static final JSONObject CONFIG_GUI = (JSONObject) CONFIG.get( "gui" );
    
    public static final int FPS = ( (Number) CONFIG_GUI.get( "fps" ) ).intValue();
    public static final int DEPTH = ( (Number) CONFIG_GUI.get( "depth" ) ).intValue();
    
    public static final String TITLE = (String) CONFIG_GUI.get( "title" );
  }
}