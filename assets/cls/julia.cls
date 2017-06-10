kernel void julia( global int* result, const unsigned int img_w, const unsigned int img_h,
const double x, const double y, const double w, const double h, const unsigned int depth,
const double px, const double py ) {
   const unsigned int itemId = get_global_id( 0 );
   
   const unsigned int size = img_w <= img_h ? img_w : img_h;
   
   const double cx = x + w * ( itemId % img_w ) / size - (double) ( img_w - size ) / size;
   const double cy = y + h * ( itemId / img_w ) / size - (double) ( img_h - size ) / size;
   
   double zr = cx + cx;
   double zi = cy + cy;
   
   double zr2 = zr * zr;
   double zi2 = zi * zi;
   
   for ( unsigned long n = 0; n < depth; n++ ) {
      zi = zr * zi;
      zi += zi;
      zi += py;
      zr = zr2 - zi2 + px;
      
      zr2 = zr * zr;
      zi2 = zi * zi;
      
      if ( zr2 + zi2 >= 4 ) {
         result[ itemId ] = n * 73;
         return;
      }
   }
   
   result[ itemId ] = 0;
}