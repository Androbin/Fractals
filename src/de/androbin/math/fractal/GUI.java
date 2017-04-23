package de.androbin.math.fractal;

import static de.androbin.math.fractal.Configuration.gui_.*;
import static java.awt.event.KeyEvent.*;
import de.androbin.gfx.*;
import de.androbin.math.fractal.sets.*;
import de.androbin.opencl.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

public final class GUI extends CustomPane implements Serializable
{
	private static final long	serialVersionUID	= 1L;
	
	private final Generator		generator;
	private BufferedImage		buffer;
	
	private AbstractSet			set;
	private int					depth;
	
	private double				dx;
	private double				dy;
	private double				scale				= 1.0;
	
	private double				time;
	private double				speed				= 1.0;
	
	public GUI( final AbstractSet set )
	{
		this( set, DEPTH );
	}
	
	public GUI( final AbstractSet set, final int depth )
	{
		this.generator = new Generator( set );
		
		this.set = set;
		this.depth = depth;
		
		registerListeners();
		
		delay_milli = DELAY;
		start( "Fractals" );
	}
	
	@ Override
	protected void destroy()
	{
		generator.cleanup();
		set.getExecutor().cleanup();
		CLExecutor.destroyCL();
	}
	
	private void registerListeners()
	{
		addKeyListener( new KeyAdapter()
		{
			@ Override
			public void keyPressed( final KeyEvent event )
			{
				switch ( event.getKeyCode() )
				{
					case VK_ESCAPE :
						running = false;
						break;
					
					case VK_PLUS :
						speed *= Math.E;
						break;
					
					case VK_MINUS :
						speed /= Math.E;
						break;
				}
			}
			
			@ Override
			public void keyReleased( final KeyEvent event )
			{
				if ( event.getKeyCode() == VK_SPACE )
				{
					active ^= true;
				}
			}
		} );
		addMouseWheelListener( new MouseWheelListener()
		{
			@ Override
			public void mouseWheelMoved( final MouseWheelEvent event )
			{
				final int size = Math.min( getWidth(), getHeight() );
				
				dx += ( 2f * event.getX() / size - 1f ) * scale;
				dy += ( 2f * event.getY() / size - 1f ) * scale;
				
				scale *= Math.exp( event.getPreciseWheelRotation() );
				
				updateBuffer();
				render();
			}
		} );
	}
	
	@ Override
	public void render( final Graphics2D g, final ImageObserver observer )
	{
		g.drawImage( buffer, 0, 0, observer );
	}
	
	@ Override
	protected void update( final float delta )
	{
		time += delta * speed * scale;
		set.update( time );
		updateBuffer();
	}
	
	private void updateBuffer()
	{
		if ( getWidth() <= 0 || getHeight() <= 0 )
		{
			return;
		}
		
		if ( buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight() )
		{
			buffer = new BufferedImage( getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB );
		}
		
		generator.render( buffer, dx, dy, scale, depth, time );
	}
}