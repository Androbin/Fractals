package de.androbin.math.fractal.sets;

import java.io.*;
import org.lwjgl.*;

public final class MandelbrotSet extends AbstractSet
{
	public MandelbrotSet() throws IOException, LWJGLException
	{
		super( "mandelbrot" );
	}
	
	@ Override
	public void setKernelArgs( final int index )
	{
	}
	
	@ Override
	public void update( final double time )
	{
	}
}