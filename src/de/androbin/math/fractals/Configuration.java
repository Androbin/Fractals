package de.androbin.math.fractal;

import static de.androbin.util.JSONUtil.*;
import org.json.simple.*;

public final class Configuration
{
	private static final JSONObject CONFIG = (JSONObject) parseJSON( "config.json" );
	
	public static final class window_
	{
		private static final JSONObject	CONFIG_DISPLAY	= (JSONObject) CONFIG.get( "window" );
		
		public static final float		SCALE			= ( (Number) CONFIG_DISPLAY.get( "scale" ) ).floatValue();
	}
	
	public static final class gui_
	{
		private static final JSONObject	CONFIG_VIEWER	= (JSONObject) CONFIG.get( "gui" );
		
		public static final int			DEPTH			= ( (Number) CONFIG_VIEWER.get( "depth" ) ).intValue();
		public static final int			DELAY			= ( (Number) CONFIG_VIEWER.get( "delay" ) ).intValue();
		
		public static final String		TITLE			= (String) CONFIG_VIEWER.get( "title" );
	}
}