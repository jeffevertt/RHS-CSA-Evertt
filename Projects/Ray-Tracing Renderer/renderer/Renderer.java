package renderer;

import java.awt.image.WritableRaster;
import java.util.ArrayList;

import framework.Camera;
import framework.RayCastResult;
import framework.RenderObject;
import framework.RendererBase;
import framework.Vec3;
import framework.World;

// In this lab, you will write a basic ray-tracing renderer.
//  A framework has already been created for you. You will be writing a couple of
// methods put it all together. Your method, render, will write each pixel in an
// image, renderTarget, by casting a ray out from the camera, using rayCast, to 
// determine it hits. When it hits something in the scene/world, you'll get the
// color value at that point and then write it into the image.
// 
// -> Quick video, ray tracing basics: https://www.youtube.com/watch?v=oCsgTrGLDiI&t=6s
// -> Here's another, more detail: https://www.youtube.com/watch?v=gBPNO6ruevk 
// 
// Tip(s)...
//  - The image should be projected onto the "near clip plane", iterate through pointss
//      on that near clip rectangle (use camera to query info about it) and generate
//      rays from the camera's origin. Do this for all the pixels in that renderTarget.

public class Renderer implements RendererBase {

    /* rayCast  Determines THE FIRST ray intersection with the objects in the scene. 
     *            the ray is an infinite ray (infinite length starting at rayOrigin, 
     *            in the direction of rayDirUnit).
     *          The scene is composed of objects of type RenderObject. The full set of
     *            objects can be queried using: World.get().getRenderObjects()
     *          Each RenderObject supports its own rayCast method. They are all basic
     *            primitive types like planes and spheres. Their implementation does the
     *            math to perform the intersection with itself. You should make use 
     *            of this method in the implementation of this method.
     * 
     *  @param  world supports the interface to get the list of RenderObjects.
     *  @param  rayOrigin the start position of the ray
     *  @param  rayDirUnit the direction of the ray (should be a unit vector)
     *  @return the result of the first intersection along the ray (null if no intersection)
     *          see RayCastResult for details of its contents.
     */
    public RayCastResult rayCast(World world, Vec3 rayOrigin, Vec3 rayDirUnit) {
        RayCastResult res = null;

        // TODO

        return res;
    }

    /* render   Performance ray trace rendering from the point of view of the provided camera.
     *          The method should iterate through each pixel in renderTarget, perform the
     *            expected ray tracing and write the resultant pixel color. 
     *          The provided camera object supports access to the camera position and orientation
     *            and in addition, methods to find the near clip plane (consider using this, hint hint).
     *          You should use the rayCast method that you implemented up above when ray tracing
     *            each pixel. Note that the RayCastResult object contains a resultant pixel color.
     * 
     *  @param  renderTarget is the image this method writes into. Use renderTarget.setPixel(x, y, color)
     *            so set individual pixels in the 2D coordinates of this image.
     *  @param  world supports the interface to get the list of RenderObjects.
     *  @param  camera is the camera objects that has a position, direction, field of view, etc.
     */
    public void render(WritableRaster renderTarget, World world, Camera camera) {

        // TODO

        // Remove this code - this is just to demonstrate how to write into the renderTarget
        for (int y = 0; y < renderTarget.getHeight(); y++) {
            for (int x = 0; x < renderTarget.getWidth(); x++) {
                renderTarget.setPixel(x, y, new int[] { (int)(Math.random() * 150), (int)(Math.random() * 150), (int)(Math.random() * 150), 255 }); // R, G, B, Alpha (0 to 255 values)
            }
        }
    }
}
