package de.androbin.math.fractal;

import static de.androbin.math.fractal.Configuration.window_.*;
import de.androbin.math.fractal.sets.*;
import de.androbin.opencl.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import org.lwjgl.*;

public final class Fractals
{
	private Fractals()
	{
	}
	
	public static void main( final String[] args )
	{
		final AbstractSet set;
		
		try
		{
			CLExecutor.initCL();
			set = new JuliaSet();
		}
		catch ( final IOException | LWJGLException e )
		{
			e.printStackTrace();
			return;
		}
		
		SwingUtilities.invokeLater( () ->
		{
			final Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
			final int window_size = (int) ( screen_size.getHeight() * SCALE );
			
			final JFrame window = new JFrame( "Fractals" );
			window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			window.setSize( window_size, window_size );
			window.setResizable( true );
			window.setLocationRelativeTo( null );
			window.setContentPane( new GUI( set ) );
			window.setVisible( true );
		} );
	}
}
