package de.androbin.math.fractal;

import static de.androbin.math.fractal.Configuration.gui_.*;
import static java.awt.event.KeyEvent.*;
import de.androbin.gfx.*;
import de.androbin.math.fractal.sets.*;
import de.androbin.opencl.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public final class GUI extends CustomPane {
  private final Generator generator;
  private BufferedImage buffer;
  private int[] imageData;
  
  private AbstractSet set;
  private final Args args;
  private boolean animate;
  private double speed;
  
  private final Point mouse = new Point();
  
  public GUI( final AbstractSet set ) {
    this( set, DEPTH );
  }
  
  public GUI( final AbstractSet set, final int depth ) {
    this.generator = new Generator( set );
    
    this.set = set;
    this.args = new Args( depth );
    this.animate = true;
    this.speed = 1.0;
    
    registerListeners();
    
    delayMilli = DELAY;
    start( "Fractals" );
  }
  
  @ Override
  protected void destroy() {
    generator.cleanup();
    set.executor.cleanup();
    CLExecutor.destroyCL();
  }
  
  private void registerListeners() {
    addKeyListener( new KeyAdapter() {
      @ Override
      public void keyPressed( final KeyEvent event ) {
        switch ( event.getKeyCode() ) {
          case VK_ESCAPE :
            running = false;
            break;
          
          case VK_PLUS :
            speed *= Math.E;
            break;
          
          case VK_MINUS :
            speed /= Math.E;
            break;
        }
      }
      
      @ Override
      public void keyReleased( final KeyEvent event ) {
        if ( event.getKeyCode() == VK_SPACE ) {
          animate ^= true;
        }
      }
    } );
    addMouseWheelListener( new MouseWheelListener() {
      @ Override
      public void mouseWheelMoved( final MouseWheelEvent event ) {
        final int size = Math.min( getWidth(), getHeight() );
        final double a = Math.exp( event.getPreciseWheelRotation() );
        
        final double ds = args.scale * ( a - 1.0 );
        args.x += ds * ( 1.0 - 2.0 * event.getX() / size );
        args.y += ds * ( 1.0 - 2.0 * event.getY() / size );
        args.scale *= a;
      }
    } );
    addMouseMotionListener( new MouseMotionAdapter() {
      @ Override
      public void mouseMoved( MouseEvent e ) {
        mouse.setLocation( e.getPoint() );
      }
    } );
  }
  
  @ Override
  public void render( final Graphics2D g ) {
    g.drawImage( buffer, 0, 0, null );
  }
  
  @ Override
  protected void update( final float delta ) {
    if ( animate ) {
      args.time += delta * speed * args.scale;
      
      final double x = 2.0 * mouse.x / getWidth() - 1.0;
      final double y = 2.0 * mouse.y / getHeight() - 1.0;
      
      set.update( x, y, args.time );
    }
    
    updateBuffer();
  }
  
  private void updateBuffer() {
    if ( getWidth() <= 0 || getHeight() <= 0 ) {
      return;
    }
    
    if ( buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight() ) {
      buffer = new BufferedImage( getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB );
      imageData = ( (DataBufferInt) buffer.getRaster().getDataBuffer() ).getData();
    }
    
    generator.render( imageData, buffer.getWidth(), buffer.getHeight(), args );
  }
}