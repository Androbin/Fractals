package de.androbin.fractal.sets;

import de.androbin.opencl.*;
import java.io.*;

public abstract class AbstractSet {
  public final CLExecutor executor;
  
  public AbstractSet( final String name ) throws IOException {
    executor = new CLExecutor( name );
    update( 0.0, 0.0, 0.0 );
  }
  
  public abstract void setKernelArgs( int index );
  
  public abstract void update( double x, double y, double t );
}