package de.androbin.math.fractal.sets;

import java.io.*;

public final class MandelbrotSet extends AbstractSet {
  public MandelbrotSet() throws IOException {
    super( "mandelbrot" );
  }
  
  @ Override
  public void setKernelArgs( final int index ) {
  }
  
  @ Override
  public void update( final double x, final double y, final double t ) {
  }
}