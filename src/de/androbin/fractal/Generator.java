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
    coverMemory( imageData.length );
    
    final int size = Math.min( width, height );
    final double scale = 2.0 * args.scale / size;
    
    final double x = args.x - args.scale + ( size - width ) * scale;
    final double y = args.y - args.scale + ( size - height ) * scale;
    
    final CLExecutor executor = set.executor;
    final CLKernel kernel = executor.kernel;
    
    kernel.setArg( 0, mem );
    kernel.setArg( 1, args.depth );
    kernel.setArg( 2, scale );
    kernel.setArg( 3, x );
    kernel.setArg( 4, y );
    
    set.setKernelArgs( 5 );
    executor.execute( width, height );
    
    readBuffer( mem, buffer );
    getBuffer( buffer, imageData );
  }
}