package de.androbin.math.fractal.sets;

import de.androbin.opencl.*;
import java.io.*;
import org.lwjgl.*;
import org.lwjgl.opencl.*;

public abstract class AbstractSet
{
	private final CLExecutor executor;
	
	public AbstractSet( final String name ) throws IOException, LWJGLException
	{
		executor = new CLExecutor( name );
	}
	
	public final CLExecutor getExecutor()
	{
		return executor;
	}
	
	public final CLKernel getKernel()
	{
		return getExecutor().kernel;
	}
	
	public abstract void setKernelArgs( final int index );
	
	public abstract void update( final double time );
}