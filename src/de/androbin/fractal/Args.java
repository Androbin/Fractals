package de.androbin.fractal;

public final class Args {
  public double x;
  public double y;
  
  public double scale;
  
  public int depth;
  public double time;
  
  public Args( final int depth ) {
    this.scale = 1.0;
    this.depth = depth;
  }
}