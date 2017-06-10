kernel void mandelbrot( global int* result, const unsigned int stride, const double scale,
const double x, const double y, const unsigned int depth ) {
   const unsigned int id = get_global_id( 0 );
   
   const double cx = x + ( id % stride ) * scale;
   const double cy = y + ( id / stride ) * scale;
   
   const double cr = cx + cx;
   const double ci = cy + cy;
   
   double zr = 0;
   double zi = 0;
   
   double zr2 = zr * zr;
   double zi2 = zi * zi;
   
   for ( unsigned int n = 0; n < depth; n++ ) {
      zi = zr * zi;
      zi += zi;
      zi += ci;
      zr = zr2 - zi2 + cr;
      
      zr2 = zr * zr;
      zi2 = zi * zi;
      
      if ( zr2 + zi2 >= 4.0 ) {
         result[ id ] = n * 73;
         return;
      }
   }
   
   result[ id ] = 0;
}