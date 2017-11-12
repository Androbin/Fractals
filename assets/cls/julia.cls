kernel void julia( global int* result, const unsigned int depth, const double scale,
const double x, const double y, const double px, const double py ) {
   const size_t idx = get_global_id( 0 );
   const size_t idy = get_global_id( 1 );
   
   const size_t stride = get_global_size( 0 );
   
   const size_t id = stride * idy + idx;
   
   const double cx = x + idx * scale;
   const double cy = y + idy * scale;
   
   double zr = cx + cx;
   double zi = cy + cy;
   
   double zr2 = zr * zr;
   double zi2 = zi * zi;
   
   for ( size_t n = 0; n < depth; ++n ) {
      zi = zr * zi;
      zi += zi;
      zi += py;
      zr = zr2 - zi2 + px;
      
      zr2 = zr * zr;
      zi2 = zi * zi;
      
      if ( zr2 + zi2 >= 4.0 ) {
         result[ id ] = n * 463141;
         return;
      }
   }
   
   result[ id ] = 0;
}