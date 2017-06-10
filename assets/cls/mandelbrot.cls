kernel void mandelbrot( global int* result, const unsigned int img_w, const unsigned int img_h,
const double x, const double y, const double w, const double h, const unsigned int depth ) {
   const unsigned int itemId = get_global_id( 0 );
   
   const unsigned int size = img_w <= img_h ? img_w : img_h;
   
   const double cx = x + w * ( itemId % img_w ) / size - (double) ( img_w - size ) / size;
   const double cy = y + h * ( itemId / img_w ) / size - (double) ( img_h - size ) / size;
   
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
      
      if ( zr2 + zi2 >= 4 ) {
         result[ itemId ] = n * 73;
         return;
      }
   }
   
   result[ itemId ] = 0;
}