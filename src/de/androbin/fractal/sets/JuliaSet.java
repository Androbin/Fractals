package de.androbin.fractal.sets;

import java.io.*;
import org.lwjgl.opencl.*;

public final class JuliaSet extends AbstractSet {
  private double px;
  private double py;
  
  public JuliaSet() throws IOException {
    super( "julia" );
  }
  
  @ Override
  public void setKernelArgs( final int index ) {
    final CLKernel kernel = executor.kernel;
    kernel.setArg( index + 0, px );
    kernel.setArg( index + 1, py );
  }
  
  @ Override
  public void update( final double x, final double y, final double t ) {
    final double s = Math.sin( t );
    final double c = Math.cos( t );
    
    px = s * y - c * x;
    py = s * x + c * y;
  }
}