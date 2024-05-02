package framework;

// base class for something that can be rendered
public class RenderObject {
    // Member variables...
    protected Vec3 pos = null;

    // Accessors...
    public Vec3 getPos() {
        return Vec3.copy(this.pos);
    }

    // Public methods...

    // rayCast - find first intersection with the ray starting at 
    //  position start, going in the direction of unit vector dirUnit
    //  for an infinite length.
    // If the ray does not intersect with the RenderObject, null will 
    //  be returned.
    public RayCastResult rayCast(Vec3 rayOrigin, Vec3 rayDirUnit) {
        return rayCast(rayOrigin, rayDirUnit, 1);
    }
    public RayCastResult rayCast(Vec3 rayOrigin, Vec3 rayDirUnit, int maxReflections) {
        return null;
    }
    public double rayIntersectDst(Vec3 rayOrigin, Vec3 rayDirUnit) {
        return -1.0;
    }

    // Member functions (methods)...
    protected RenderObject(Vec3 pos) {
        this.pos = pos;
    }

    protected void onDestroyed() {
    }

    protected void update(double deltaTime) {
    }
}
