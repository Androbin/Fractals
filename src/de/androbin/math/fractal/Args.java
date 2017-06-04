package de.androbin.math.fractal;

public final class Args {
  public double x;
  public double y;
  public double w;
  public double h;
  
  public double scale;
  
  public int depth;
  public double time;
  
  public Args( final int depth ) {
    this.scale = 1.0;
    this.depth = depth;
  }
  
  public Args( final Args args ) {
    this.x = args.x;
    this.y = args.y;
    this.w = args.w;
    this.h = args.h;
    
    this.scale = args.scale;
    
    this.depth = args.depth;
    this.time = args.time;
  }
}