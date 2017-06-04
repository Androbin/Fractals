package de.androbin.math.fractal;

import static de.androbin.math.fractal.Configuration.window_.*;
import de.androbin.gfx.*;
import de.androbin.math.fractal.sets.*;
import de.androbin.opencl.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import org.lwjgl.*;

public final class Fractals {
  private Fractals() {
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
    
    SwingUtilities.invokeLater( () -> {
      final Dimension desktopSize = SystemGraphics.getDesktopSize();
      final int windowSize = (int) ( desktopSize.height * SCALE );
      
      final JFrame window = new JFrame( "Fractals" );
      window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      window.setSize( windowSize, windowSize );
      window.setResizable( true );
      window.setLocationRelativeTo( null );
      window.setContentPane( new GUI( set ) );
      window.setVisible( true );
    } );
  }
}