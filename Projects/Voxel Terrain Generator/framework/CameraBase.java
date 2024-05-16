package framework;

import math.Mat4x4;
import math.Vec2;
import math.Vec3;

public class CameraBase {
    // Data
    private Vec3 pos;
    private Vec3 lookDir;               // Unit vector indicating the forward look direction
    private Vec3 upDir;                 // Unit vector indicating the up direction for the camera

    private double fovHorizFull;        // Full horizontal FOV (in degrees, vert is derived off the aspect ratio)
    private double nearClip, farClip;   // Distance to the near and far clip planes (from pos in direction of lookDir)

    // Constructor(s)
    public CameraBase(Vec3 pos, Vec3 forward) {
        this(pos, forward, Vec3.up(), 80, 0.1, 1000.0);
    }
    public CameraBase(Vec3 pos, Vec3 forward, Vec3 upDir, double fovHorizFull, double nearClipDst, double farClipDst) {
        this.pos = pos;
        this.lookDir = forward.unit();
        this.upDir = upDir.unit();
        this.fovHorizFull = fovHorizFull;
        this.nearClip = nearClipDst;
        this.farClip = farClipDst;
    }

    // Accessors
    public Vec3 getPos() {
        return this.pos;
    }
    public void setPos(Vec3 pos) {
        this.pos = pos;
    }
    public Vec3 forward() {
        return lookDir;
    }
    public Vec3 getLookDir() {
        return lookDir;
    }
    public void setLookDir(Vec3 lookDir) {
        this.lookDir = lookDir.unit();
    }
    public Vec3 backward() {
        return Vec3.multiply(lookDir, -1);
    }
    public Vec3 up() {
        return upDir;
    }
    public void setUpDir(Vec3 upDir) {
        this.upDir = upDir.unit();
    }
    public Vec3 down() {
        return Vec3.multiply(upDir, -1);
    }
    public Vec3 right() {
        return upDir.cross(lookDir);
    }
    public Vec3 left() {
        return Vec3.multiply(right(), -1);
    }    
    public Mat4x4 getWorldToCamera() {
        Mat4x4 invTrans = Mat4x4.transTrans( Vec3.multiply(pos, -1) );
        Mat4x4 rotTrans = Mat4x4.transFromBasis( right(), upDir, backward() ).transpose();
        return Mat4x4.multiply(rotTrans, invTrans);
    }
    public Mat4x4 getProjectionTransform() {
        return Mat4x4.transProj(fovHorizFull, nearClip, farClip, Window.get().getAspectWoverH());
    }

    // key & mouse helper methods
    public boolean isArrowKeyPressed_Left() {
        return Window.get().isArrowKeyPressed(Window.ArrowKey.Left);
    }
    public boolean isArrowKeyPressed_Right() {
        return Window.get().isArrowKeyPressed(Window.ArrowKey.Right);
    }
    public boolean isArrowKeyPressed_Up() {
        return Window.get().isArrowKeyPressed(Window.ArrowKey.Up);
    }
    public boolean isArrowKeyPressed_Down() {
        return Window.get().isArrowKeyPressed(Window.ArrowKey.Down);
    }
    public Vec2 getMouseCursorPos() {
        return Window.get().getMouseCursorPos();
    }

    // methods
    public void update(double deltaTime) {
    }
}
