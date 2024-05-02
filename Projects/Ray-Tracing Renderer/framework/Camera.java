package framework;

public class Camera {
    // Data
    private Vec3 pos;
    private Vec3 lookDir;               // Unit vector indicating the forward look direction
    private Vec3 upDir;                 // Unit vector indicating the up direction for the camera

    private double fovHorizFull;        // Full horizontal FOV (in degrees, vert is derived off the aspect ratio)
    private double nearClip, farClip;   // Distance to the near and far clip planes (from pos in direction of lookDir)

    private Vec3 lookAtPoint = Vec3.zero();
    private Vec3 axisOfRotPt = Vec3.zero(); // Angular velocity about central vertical axis (see updateCamera for details on motion model)
    private Vec3 axisOfRotDir = Vec3.up();  // Axis of rotation is defined by a point and a direction (the two vectors declared here)
    private double angVel = 0.0;        // Angular velocity about axis
    private double radDstVel = 0.0;     // Radial distance from central axis (set initially by pos in constructor)

    // Constructor(s)
    public Camera(Vec3 pos, Vec3 lookAtPoint) {
        this(pos, lookAtPoint, Vec3.up(), 110, 0.1, 1000.0);
    }
    public Camera(Vec3 pos, Vec3 lookAtPoint, Vec3 upDir, double fovHorizFull, double nearClipDst, double farClipDst) {
        this.pos = pos;
        this.lookDir = Vec3.subtract(lookAtPoint, pos).unit();
        this.upDir = upDir.unit();
        this.fovHorizFull = fovHorizFull;
        this.nearClip = nearClipDst;
        this.farClip = farClipDst;

        // Initialize motion model params
        this.lookAtPoint = lookAtPoint;
        this.axisOfRotPt = new Vec3(lookAtPoint.x, 0.0, lookAtPoint.z);
        this.axisOfRotDir = Vec3.up();
        this.angVel = 0.0;
        this.radDstVel = 0.0;
    }

    // Accessors
    public Vec3 getPos() {
        return this.pos;
    }
    public Vec3 forward() {
        return lookDir;
    }
    public Vec3 up() {
        return upDir;
    }
    public Vec3 right() {
        return lookDir.cross(upDir);
    }
    public Vec3 getNearClipCenter() {
        return Vec3.add(pos, Vec3.multiply(lookDir, nearClip));
    }
    public Vec3 getNearClipVecX() { // It's length matches the half size of the near clip rectangle (vector is in world space)
        double halfHorizFOV = fovHorizFull * 0.5;
        double clipRectHalfWidth = Util.tanDeg(halfHorizFOV) * nearClip;
        return Vec3.multiply(right(), clipRectHalfWidth);
    }
    public Vec3 getNearClipVecY() { // It's length matches the half size of the near clip rectangle (vector is in world space)
        double halfHorizFOV = fovHorizFull * 0.5;
        double clipRectHalfWidth = Util.tanDeg(halfHorizFOV) * nearClip;
        double canvasAspect = World.get().getCanvasSize().y / World.get().getCanvasSize().x;
        double clipRectHalfHeight = canvasAspect * clipRectHalfWidth;
        return Vec3.multiply(up(), clipRectHalfHeight);
    }
    public boolean isMoving() {
        return isMoving(1.0);
    }
    public boolean isMoving(double minThreshold) {
        // constants that just feel pretty good
        return (Math.abs(this.angVel) > (0.1 * minThreshold)) || (Math.abs(this.radDstVel) > (0.01 * minThreshold));
    }
    public Mat4x4 worldToCameraTrans() {
        Mat4x4 invTrans = Mat4x4.transTrans( Vec3.multiply(pos, -1) );
        Mat4x4 rotTrans = Mat4x4.transFromBasis( right(), upDir, lookDir).transpose();
        return Mat4x4.multiply(invTrans, rotTrans);
    }
    public Mat4x4 projTrans() {
        return Mat4x4.transProj(fovHorizFull, nearClip, farClip);
    }

    // Update methods
    public void updateMotion(double deltaTime) {
        // Constants...
        final double rotForceMult = 250.0;
        final double radDstForceMult = 40.0;
        final double velDampFactor = 0.8;
        final Vec2 minMaxRadDst = new Vec2(2.5, 4.9);

        // Motion model...
        //  Rotation is about a vertical axis in the center of the room.
        //  Two degrees of freedom: rotation about that axis & in/out of radius/dst-from-axis
        //  Arrow keys control both DOF (left/right: rot-angle, up/down: radial-dst-from-axis)
        double rotForce = 0.0f;
        double radDstForce = 0.0f;
        if (Window.get().getArrowKeyPressed(Window.ArrowKey.Left)) {
            rotForce -= rotForceMult * deltaTime;
        }
        if (Window.get().getArrowKeyPressed(Window.ArrowKey.Right)) {
            rotForce += rotForceMult * deltaTime;
        }
        if (Window.get().getArrowKeyPressed(Window.ArrowKey.Up)) {
            radDstForce -= radDstForceMult * deltaTime;
        }
        if (Window.get().getArrowKeyPressed(Window.ArrowKey.Down)) {
            radDstForce += radDstForceMult * deltaTime;
        }

        // Apply the force to velocity (dV = F * dt)
        angVel += rotForce * deltaTime;
        radDstVel += radDstForce * deltaTime;

        // Find the current angle & radial-dst from pos (both angle and dst are effectively stored in pos)...
        Vec3 posOnFloorPlane = new Vec3(pos.x, axisOfRotPt.y, pos.z);
        Vec2 dirVecOnFloorPlane = (new Vec2(posOnFloorPlane.x - axisOfRotPt.x, posOnFloorPlane.z - axisOfRotPt.z)).unit();
        double radDst = Vec3.distance(posOnFloorPlane, axisOfRotPt);
        double rotAngle = Util.atan2Deg(dirVecOnFloorPlane.y, dirVecOnFloorPlane.x);

        // Apply the velocity to the angle/dst (dP = V * dt)
        radDst += radDstVel * deltaTime;
        rotAngle += angVel * deltaTime;

        // Clamp radDst (and zero out the velocity if we hit a "wall")...
        if ((radDst < minMaxRadDst.x) || (radDst > minMaxRadDst.y)) {
            radDst = Util.clamp(radDst, minMaxRadDst.x, minMaxRadDst.y);
            radDstVel = 0;
        }

        // Recompute pos based on radDst & rotAngle...
        Mat4x4 transRot = Mat4x4.transRotAxis(this.axisOfRotDir, -rotAngle);
        double prevPosY = pos.y; 
        Vec3 radialOffset = transRot.transform(Vec3.multiply(Vec3.right(), radDst));
        pos = Vec3.add(radialOffset, axisOfRotPt);
        pos.y = prevPosY; // keep the camera in the horizontal plane (same height)

        // Use lookAtPoint to generate look direction & up vector...
        lookDir = Vec3.subtract(lookAtPoint, pos).unit();
        Vec3 camRight = Vec3.up().cross(lookDir);
        upDir = camRight.cross(lookDir);

        // Damp...
        angVel *= velDampFactor;
        radDstVel *= velDampFactor;
    }
    public void update(double deltaTime) {
        updateMotion(deltaTime);
    }
}
