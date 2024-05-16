package app;

import framework.CameraBase;
import framework.WorldBase;
import math.Mat3x3;
import math.Vec2;
import math.Vec3;

/* In this lab, you will be implementing a camera that lets you fly/run around the
 *   world. Before any modifications, you'll just be stuck looking forward and unable
 *   to move. All of your code can go in this file.
 * 
 * Inputs...
 *   - Arrow keys (including WASD) cab be queried with the methods...
 *          isArrowKeyPressed_Left(), isArrowKeyPressed_Right(),
 *          isArrowKeyPressed_Up(), isArrowKeyPressed_Down()
 *   - Mouse position can be queried with...
 *          getMouseCursorPos()
 * 
 * You would make the motion nice and smooth (using acceleration & velocity for position).
 *  You can get and set the position of the camera in 3D space with getPos() and setPos(...).
 *  The mouse look is a bit tricker. The orientation of the camera is specified by two 
 *  vectors: lookDirection (use getLookDir() & setLookDir(...) for access) and 
 *  upVector (use up() & setUpDir(...) for access). You can use the Mat3x3 class to help
 *  you with rotation about an axis. For example, the static Mat3x3.transRotAxis(...) method
 *  creates a 3x3 rotation matrix about the specified axis. You can use this to rotate vectors.
 *  I'd suggest watching a youTube video about rotating vectors with 3x3 matrices to get
 *  you started.
 */
public class Camera extends CameraBase {
    // consts

    // data

    // constructor(s)
    public Camera(Vec3 pos, Vec3 forward) {
        super(pos, forward);
    }

    // methods
    private void updateMotion(double deltaTime) {
        // World access
        WorldBase world = WorldBase.get();

        // TODO
    }
    private void updateLookDirection(double deltaTime) {
        // TODO
    }
    public void update(double deltaTime) {
        updateMotion(deltaTime);
        updateLookDirection(deltaTime);
    }
}
