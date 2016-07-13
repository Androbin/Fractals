kernel void mandelbrot( global int* result, const unsigned int img_w, const unsigned int img_h,
const double x, const double y, const double w, const double h, const unsigned int depth )
{
   const unsigned int itemId = get_global_id( 0 );
   
   const unsigned int size = img_w <= img_h ? img_w : img_h;
   
   const double cx = x + w * ( itemId % img_w ) / size - (double) ( img_w - size ) / size;
   const double cy = y + h * ( itemId / img_w ) / size - (double) ( img_h - size ) / size;
   
   const double c_r = cx + cx;
   const double c_i = cy + cy;
   
   double zr = 0;
   double zi = 0;
   
   double zrsqr = zr * zr;
   double zisqr = zi * zi;
   
   for ( unsigned int n = 0; n < depth; n++ )
   {
      zi = zr * zi;
      zi += zi;
      zi += c_i;
      zr = zrsqr - zisqr + c_r;
      
      zrsqr = zr * zr;
      zisqr = zi * zi;
      
      if ( zrsqr + zisqr >= 4 )
      {
         result[ itemId ] = n * 73;
         return;
      }
   }
   
   result[ itemId ] = 0;
}