package de.androbin.math.fractal;

import static de.androbin.lwjgl.util.BufferUtil.*;
import static de.androbin.opencl.CLBufferUtil.*;
import static org.lwjgl.BufferUtils.*;
import static org.lwjgl.opencl.CL10.*;
import de.androbin.math.fractal.sets.*;
import de.androbin.opencl.*;
import java.nio.*;
import org.lwjgl.opencl.*;

public final class Generator {
  private IntBuffer buffer;
  private CLMem mem;
  
  private final AbstractSet set;
  
  public Generator( final AbstractSet set ) {
    this.set = set;
  }
  
  public void cleanup() {
    clReleaseMemObject( mem );
  }
  
  public void renderAbsolute( final int[] imageData, final int width, final int height,
      final Args args ) {
    final int length = imageData.length;
    
    if ( buffer == null || buffer.capacity() != length ) {
      buffer = createIntBuffer( length );
      
      if ( mem != null ) {
        clReleaseMemObject( mem );
      }
      
      mem = createReadOnlyBuffer( length, Integer.BYTES );
    }
    
    final CLExecutor executor = set.executor;
    final CLKernel kernel = executor.kernel;
    kernel.setArg( 0, mem );
    kernel.setArg( 1, width );
    kernel.setArg( 2, height );
    kernel.setArg( 3, args.x );
    kernel.setArg( 4, args.y );
    kernel.setArg( 5, args.w );
    kernel.setArg( 6, args.h );
    kernel.setArg( 7, args.depth );
    
    set.setKernelArgs( 8 );
    executor.execute( length );
    
    readBuffer( mem, buffer );
    getBuffer( buffer, imageData );
  }
  
  public void renderRelative( final int[] imageData, final int width, final int height,
      final Args args ) {
    final double size = 2 * args.scale;
    
    args.x -= args.scale;
    args.y -= args.scale;
    
    args.w = size;
    args.h = size;
    
    renderAbsolute( imageData, width, height, args );
  }
}