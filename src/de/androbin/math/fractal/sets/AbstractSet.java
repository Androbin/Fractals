package de.androbin.math.fractal.sets;

import de.androbin.opencl.*;
import java.io.*;

public abstract class AbstractSet {
  public final CLExecutor executor;
  
  public AbstractSet( final String name ) throws IOException {
    executor = new CLExecutor( name );
  }
  
  public abstract void setKernelArgs( final int index );
  
  public abstract void update( final double time );
}