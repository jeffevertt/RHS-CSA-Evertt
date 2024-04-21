package framework;

import java.awt.Color;

public class RayCastResult {
    public Vec3 pos;        // Point at which the ray intersects with the RenderObject
    public Vec3 normUnit;   // The normal (perpendicular vector) of the RenderObject at the point of intersection
    public Color color;     // Lit color of the RenderObject at the intersection point

    public RayCastResult(Vec3 pos, Vec3 normUnit, Color color) {
        this.pos = pos;
        this.normUnit = normUnit;
        this.color = color;
    }
    public double calcDstFromCamera() {
        Camera camera = World.get().getCamera();
        return Vec3.distance(pos, camera.getPos());
    }
}
