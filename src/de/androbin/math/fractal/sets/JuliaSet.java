package de.androbin.math.fractal.sets;

import java.io.*;
import org.lwjgl.*;
import org.lwjgl.opencl.*;

public final class JuliaSet extends AbstractSet
{
	private double	px;
	private double	py;
	
	public JuliaSet() throws IOException, LWJGLException
	{
		super( "julia" );
	}
	
	@ Override
	public void setKernelArgs( final int index )
	{
		final CLKernel kernel = getKernel();
		kernel.setArg( index + 0, px );
		kernel.setArg( index + 1, py );
	}
	
	@ Override
	public void update( final double time )
	{
		px = Math.sin( time );
		py = Math.cos( time );
	}
}