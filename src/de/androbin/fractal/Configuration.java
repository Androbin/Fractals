package de.androbin.fractal;

import de.androbin.json.*;

public final class Configuration {
  private static final XObject CONFIG = XUtil.readJSON( "config.json" ).get().asObject();
  
  public static final class window_ {
    private static final XObject CONFIG_WINDOW = CONFIG.get( "window" ).asObject();
    
    public static final float SCALE = CONFIG_WINDOW.get( "scale" ).asFloat();
  }
  
  public static final class gui_ {
    private static final XObject CONFIG_GUI = CONFIG.get( "gui" ).asObject();
    
    public static final int FPS = CONFIG_GUI.get( "fps" ).asInt();
    public static final int DEPTH = CONFIG_GUI.get( "depth" ).asInt();
    
    public static final String TITLE = CONFIG_GUI.get( "title" ).asString();
  }
}