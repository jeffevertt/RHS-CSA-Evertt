package framework;

import java.awt.Color;

// RenderObject: Sphere
public class Plane extends RenderObject {
    // Consts...
    private final double GRIDPATTERN_WIDTH = 2;
    private final double SPECULAR_REFLECT_FACTOR = 0.5;

    // Enums...
    public enum ColorPattern {
        FLAT_COLOR,
        BLACK_AND_WHITE_GRID,
    };

    // Data...
    private Vec3 norm;          // Normal to the plane
    private Color color;
    private ColorPattern colorPattern;

    // Methods...
    public Plane(Vec3 pointOnPlane, Vec3 planeNormal, Color color) {
        this(pointOnPlane, planeNormal, color, ColorPattern.FLAT_COLOR);
    }
    public Plane(Vec3 pointOnPlane, Vec3 planeNormal, Color color, ColorPattern colorPattern) {
        super(pointOnPlane);

        this.norm = planeNormal;
        this.color = color;
        this.colorPattern = colorPattern;
    }

    public Vec3 getPlaneNormal() {
        return norm;
    }
    public Color getColor() {
        return color;
    }
    public Color getColorAtPoint(Vec3 pt) {
        if (colorPattern == ColorPattern.BLACK_AND_WHITE_GRID) {
            boolean modX = (pt.x % (GRIDPATTERN_WIDTH * 2)) < GRIDPATTERN_WIDTH;
            boolean modZ = (pt.z % (GRIDPATTERN_WIDTH * 2)) < GRIDPATTERN_WIDTH;
            if ((modX || modZ) && !(modX && modZ))  {
                return Color.white;
            }
            return Color.black;
        }
        return this.color;
    }

    public double rayIntersectDst(Vec3 rayOrigin, Vec3 rayDirUnit) {
        double denom = norm.dot(rayDirUnit);
        if (Math.abs(denom) <= 0.00001) {
            return -1.0;
        }
        
        // Intersects, setup the results...
        double t = Vec3.subtract(pos, rayOrigin).dot(norm) / denom;
        if (t <= 0.00001) {
            return -1.0;
        }
        return t;
    }
    public RayCastResult rayCast(Vec3 rayOrigin, Vec3 rayDirUnit, int maxReflections) {
        double intDst = rayIntersectDst(rayOrigin, rayDirUnit);
        if (intDst < 0) {
            return null;
        }
        Vec3 intPt = Vec3.add(rayOrigin, Vec3.multiply(rayDirUnit, intDst));

        // shading...
        Color pixelColor = World.get().calcShadingAtSurfPt(intPt, norm, getColorAtPoint(intPt), SPECULAR_REFLECT_FACTOR, 0);

        // create up the results and return it
        return new RayCastResult(intPt, this.norm, pixelColor);
    }
}