package de.androbin.math.fractal;

import static org.lwjgl.BufferUtils.*;
import static org.lwjgl.opencl.CL10.*;
import de.androbin.math.fractal.sets.*;
import de.androbin.opencl.*;
import java.awt.image.*;
import java.nio.*;
import org.lwjgl.opencl.*;

public final class Generator
{
	private IntBuffer			buffer;
	private CLMem				mem;
	
	private final AbstractSet	set;
	
	public Generator( final AbstractSet set )
	{
		this.set = set;
	}
	
	public void cleanup()
	{
		clReleaseMemObject( mem );
	}
	
	public void render( final BufferedImage image, final double x, final double y, final double scale, final int depth, final double time )
	{
		final double size = 2 * scale;
		render( image, x - scale, y - scale, size, size, depth, time );
	}
	
	public void render( final BufferedImage image, final double x, final double y, final double w, final double h, final int depth, final double time )
	{
		final int length = image.getWidth() * image.getHeight();
		
		if ( buffer == null || buffer.capacity() != length )
		{
			buffer = createIntBuffer( length );
			
			if ( mem != null )
			{
				clReleaseMemObject( mem );
			}
			
			mem = CLExecutor.createReadOnlyBuffer( length, Integer.BYTES );
		}
		
		final CLKernel kernel = set.getKernel();
		kernel.setArg( 0, mem );
		kernel.setArg( 1, image.getWidth() );
		kernel.setArg( 2, image.getHeight() );
		kernel.setArg( 3, x );
		kernel.setArg( 4, y );
		kernel.setArg( 5, w );
		kernel.setArg( 6, h );
		kernel.setArg( 7, depth );
		
		set.setKernelArgs( 8 );
		set.getExecutor().execute( length );
		
		CLExecutor.enqueueReadBuffer( mem, buffer );
		
		buffer.position( 0 );
		buffer.get( ( (DataBufferInt) image.getRaster().getDataBuffer() ).getData() );
		buffer.rewind();
	}
}