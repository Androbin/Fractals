package de.androbin.fractal;

import static de.androbin.lwjgl.util.BufferUtil.*;
import static de.androbin.opencl.CLBufferUtil.*;
import static org.lwjgl.BufferUtils.*;
import static org.lwjgl.opencl.CL10.*;
import de.androbin.fractal.sets.*;
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
  
  private void coverMemory( final int length ) {
    if ( buffer != null && buffer.capacity() == length ) {
      return;
    }
    
    buffer = createIntBuffer( length );
    
    if ( mem != null ) {
      clReleaseMemObject( mem );
    }
    
    mem = createReadOnlyBuffer( length, Integer.BYTES );
  }
  
  public void render( final int[] imageData, final int width, final int height,
      final Args args ) {
    final int length = imageData.length;
    
    coverMemory( length );
    
    final int size = Math.min( width, height );
    final double scale = 2.0 * args.scale / size;
    
    final double x = 1.0 + args.x - args.scale - (double) width / size;
    final double y = 1.0 + args.y - args.scale - (double) height / size;
    
    final CLExecutor executor = set.executor;
    final CLKernel kernel = executor.kernel;
    
    kernel.setArg( 0, mem );
    kernel.setArg( 1, width );
    kernel.setArg( 2, scale );
    kernel.setArg( 3, x );
    kernel.setArg( 4, y );
    kernel.setArg( 5, args.depth );
    
    set.setKernelArgs( 6 );
    executor.execute( length );
    
    readBuffer( mem, buffer );
    getBuffer( buffer, imageData );
  }
}