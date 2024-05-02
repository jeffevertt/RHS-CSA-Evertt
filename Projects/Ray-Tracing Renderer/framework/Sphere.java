package framework;

import java.awt.Color;

// RenderObject: Sphere
public class Sphere extends RenderObject {
    // Constants...
    public final double SPECULAR_REFLECT_FACTOR = 0.5;
    
    // Data...
    private double radius;
    private Color color;

    // Methods...
    public Sphere(Vec3 pos, double radius, Color color) {
        super(pos);

        this.radius = radius;
        this.color = color;
    }

    public double getRadius() {
        return radius;
    }
    public Color getColor() {
        return color;
    }

    public double rayIntersectDst(Vec3 rayOrigin, Vec3 rayDirUnit) {
        // Quadratic formula
        Vec3 delta = Vec3.subtract(rayOrigin, pos);
        double a = 1.0;
        double b = 2.0 * delta.dot(rayDirUnit);
        double c = delta.dot(delta) - radius * radius;
        double discr = b * b - 4 * a * c;
        if (discr < 0) {
            return -1.0;
        }
        double intDst = (-b - Math.sqrt(discr)) / (2.0 * a);
        return intDst;
    }
    public RayCastResult rayCast(Vec3 rayOrigin, Vec3 rayDirUnit, int maxReflections) {
        // intersect pos, distance, normal
        double intDst = rayIntersectDst(rayOrigin, rayDirUnit);
        if (intDst < 0) {
            return null;
        }
        Vec3 intPos = Vec3.add(rayOrigin, Vec3.multiply(rayDirUnit, intDst));
        Vec3 intNorm = Vec3.subtract(intPos, pos).unit();

        // shading...
        Color pixelColor = World.get().calcShadingAtSurfPt(intPos, intNorm, color, SPECULAR_REFLECT_FACTOR, maxReflections);

        // Intersects, setup the results...
        return new RayCastResult(intPos, intNorm, pixelColor);
    }
}