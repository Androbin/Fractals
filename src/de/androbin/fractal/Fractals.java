package de.androbin.fractal;

import static de.androbin.fractal.Configuration.gui_.*;
import static de.androbin.fractal.Configuration.window_.*;
import static java.awt.event.KeyEvent.*;
import de.androbin.fractal.sets.*;
import de.androbin.gfx.*;
import de.androbin.opencl.*;
import de.androbin.shell.*;
import de.androbin.shell.env.*;
import de.androbin.shell.gfx.*;
import de.androbin.shell.input.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import org.lwjgl.*;

public final class Fractals extends AbstractShell implements AWTGraphics {
  private final Generator generator;
  
  private final AbstractSet set;
  private final Args args;
  
  public BufferedImage buffer;
  public int[] imageData;
  
  private boolean animate;
  private double speed;
  
  private final Point mouse = new Point();
  
  public Fractals( final AbstractSet set ) {
    this( set, DEPTH );
  }
  
  public Fractals( final AbstractSet set, final int depth ) {
    this.generator = new Generator( set );
    
    this.set = set;
    this.args = new Args( depth );
    
    this.animate = true;
    this.speed = 1.0;
    
    registerInputs();
  }
  
  private void coverMemory() {
    if ( buffer != null && buffer.getWidth() == getWidth() && buffer.getHeight() == getHeight() ) {
      return;
    }
    
    buffer = new BufferedImage( getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB );
    imageData = ( (DataBufferInt) buffer.getRaster().getDataBuffer() ).getData();
  }
  
  @ Override
  public void destroy() {
    generator.cleanup();
    set.executor.cleanup();
    CLExecutor.destroyCL();
  }
  
  public static void main( final String[] args ) {
    try {
      CLExecutor.initCL( 0, 0, null );
    } catch ( final LWJGLException e ) {
      e.printStackTrace();
      return;
    }
    
    final AbstractSet set;
    
    try {
      set = new JuliaSet();
    } catch ( final IOException e ) {
      e.printStackTrace();
      return;
    }
    
    final Fractals fractals = new Fractals( set );
    final AWTEnv env = new AWTEnv( fractals, FPS );
    env.start( TITLE );
    
    final CustomPane canvas = env.canvas;
    
    SwingUtilities.invokeLater( () -> {
      final Dimension desktopSize = SystemGraphics.getDesktopSize();
      final int windowSize = (int) ( desktopSize.height * SCALE );
      
      final JFrame window = new JFrame( "Fractals" );
      window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      window.setSize( windowSize, windowSize );
      window.setResizable( true );
      window.setLocationRelativeTo( null );
      window.setContentPane( canvas );
      window.setVisible( true );
    } );
  }
  
  @ Override
  protected void onResized( final int width, final int height ) {
  }
  
  private void registerInputs() {
    final Inputs inputs = getInputs();
    inputs.keyboard = new KeyInput() {
      @ Override
      public void keyPressed( final int keycode ) {
        switch ( keycode ) {
          case VK_ESCAPE:
            setRunning( false );
            break;
          
          case VK_PLUS:
            speed *= Math.E;
            break;
          
          case VK_MINUS:
            speed /= Math.E;
            break;
        }
      }
      
      @ Override
      public void keyReleased( final int keycode ) {
        if ( keycode == VK_SPACE ) {
          animate ^= true;
        }
      }
    };
    inputs.mouseMotion = new MouseMotionInput() {
      @ Override
      public void mouseMoved( final int x, final int y ) {
        mouse.setLocation( x, y );
      }
    };
    inputs.mouseWheel = new MouseWheelInput() {
      @ Override
      public void mouseWheelMoved( final int x, final int y,
          final int iclicks, final float fclicks ) {
        final double a = Math.exp( fclicks );
        final double ds = args.scale * ( a - 1.0 );
        
        final int size = Math.min( getWidth(), getHeight() );
        
        args.x += ds * ( 1.0 - 2.0 * x / size );
        args.y += ds * ( 1.0 - 2.0 * y / size );
        
        args.scale *= a;
      }
    };
  }
  
  @ Override
  public void render( final Graphics2D g ) {
    g.drawImage( buffer, 0, 0, null );
  }
  
  @ Override
  public void update( final float delta ) {
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
    
    coverMemory();
    
    generator.render( imageData, buffer.getWidth(), buffer.getHeight(), args );
  }
}