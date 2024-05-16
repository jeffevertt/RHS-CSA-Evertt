package framework;

import math.Vec2;

/* NoiseMap - A generic interface to a 2D noise map (or a set of them).
 *  This can be used to overlay a composite set of maps and treat them
 *  as a single map (the same way you do a single map).
 */
public interface NoiseMap {
    /* calcValueAtPoint returns the value stored in the map at the 
     *  point specified by location pt. */
    public double calcValueAtPoint(Vec2 pt);
}
